package com.app.toeic.payment.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.RequestUtils;
import com.app.toeic.util.ServerHelper;
import com.app.toeic.util.VnpayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("payment/vn-pay")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VnPayController {
    UserService userService;
    SystemConfigService systemConfigService;
    RestTemplate restTemplate;

    @PostMapping("create")
    @SneakyThrows
    public Object createPayment(@RequestBody VnPayBody body) {
        var vnpReturnUrl = systemConfigService.getConfigValue(VnpayUtils.VN_PAY_RETURN_URL);
        var vnpTxnRef = String.valueOf(System.currentTimeMillis());
        var vnpParams = buildParamVnPay(body, vnpTxnRef, vnpReturnUrl);

        var cld = Calendar.getInstance(TimeZone.getTimeZone(VnpayUtils.TIME_ZONE_DEFAULT));
        var formatter = new SimpleDateFormat(VnpayUtils.DATE_FORMAT);
        var vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put(VnpayUtils.VNP_CREATE_DATE_KEY, vnpCreateDate);
        cld.add(Calendar.MINUTE, VnpayUtils.DEFAULT_TIME_END);

        var vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put(VnpayUtils.VNP_EXPIRE_DATE_KEY, vnpExpireDate);
        var fieldNames = vnpParams.keySet().stream().sorted().toList();
        var hashData = new StringBuilder();
        var query = new StringBuilder();
        var itr = fieldNames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = vnpParams.get(fieldName);
            if (StringUtils.isNotBlank(fieldValue)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        var secureHash = VnpayUtils.hmacSHA512(VnpayUtils.VNP_SECRET_KEY_VALUE, hashData.toString());
        var paymentUrl = MessageFormat.format(
                "{0}?{1}&{2}={3}",
                VnpayUtils.VNP_PAY_URL,
                query,
                VnpayUtils.VNP_SECURE_HASH_TYPE_KEY,
                secureHash
        );
        return ResponseVO
                .builder()
                .success(true)
                .data(paymentUrl)
                .build();
    }

    @PostMapping("confirm")
    public Object confirmOrder(@RequestBody QueryVnPayBody body, HttpServletRequest request) {
        var rs = ResponseVO.builder().success(false).build();
        var vnpParams = buildParamQuery(body.txnRef(), body.transDate());
        var headers = RequestUtils.createHeaders();
        var httpEntity = new HttpEntity<>(vnpParams, headers);
        var response = restTemplate.postForEntity(VnpayUtils.VNP_QUERY_URL, httpEntity, Object.class);
        if (response.getStatusCode()
                    .is2xxSuccessful() && response.getBody() != null && response.getBody() instanceof Map<?, ?> map) {
            if (map.containsKey("vnp_ResponseCode") && "00".equals(map.get("vnp_ResponseCode"))
                    && map.containsKey("vnp_TransactionStatus") && "00".equals(map.get("vnp_TransactionStatus"))) {
                var success = userService.updateUserType(request);
                rs.setSuccess("SUCCESS".equals(success));
            }
        }
        return rs;
    }


    @NotNull
    private HashMap<String, String> buildParamVnPay(
            VnPayBody body,
            String vnpTxnRef,
            String vnpReturnUrl
    ) {
        var vnpParams = new HashMap<String, String>();
        vnpParams.put(VnpayUtils.VNP_VERSION_KEY, VnpayUtils.VNP_VERSION_VALUE);
        vnpParams.put(VnpayUtils.VNP_COMMAND_KEY, VnpayUtils.VNP_COMMAND_VALUE);
        vnpParams.put(VnpayUtils.VNP_TMN_CODE_KEY, VnpayUtils.VNP_TMN_CODE_VALUE);
        vnpParams.put(VnpayUtils.VNP_AMOUNT_KEY, String.valueOf(body.amount()));
        vnpParams.put(VnpayUtils.VNP_CURR_CODE_KEY, VnpayUtils.VNP_CURR_CODE_VALUE);
        vnpParams.put(VnpayUtils.VNP_BANK_CODE_KEY, StringUtils.EMPTY);
        vnpParams.put(VnpayUtils.VNP_TXN_REF_KEY, vnpTxnRef);
        vnpParams.put(VnpayUtils.VNP_ORDER_INFO_KEY, body.orderInfo());
        vnpParams.put(VnpayUtils.VNP_RETURN_URL_KEY, vnpReturnUrl);
        vnpParams.put(VnpayUtils.VNP_IP_ADDR_KEY, ServerHelper.getClientIp());
        vnpParams.put(VnpayUtils.VNP_ORDER_TYPE_KEY, VnpayUtils.VNP_ORDER_TYPE_VALUE);
        vnpParams.put(VnpayUtils.VNP_LOCALE_KEY, VnpayUtils.VNP_LOCALE_VALUE);
        return vnpParams;
    }

    private HashMap<String, String> buildParamQuery(String vnpTxnRef, String transDate) {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        var vnp_CreateDate = formatter.format(cld.getTime());
        var vnpParams = new HashMap<String, String>();
        var requestId = VnpayUtils.getRandomNumber(8);
        var vnIpAddr = ServerHelper.getClientIp();
        vnpParams.put(VnpayUtils.VNP_REQUEST_ID, requestId);
        vnpParams.put(VnpayUtils.VNP_VERSION_KEY, VnpayUtils.VNP_VERSION_VALUE);
        vnpParams.put(VnpayUtils.VNP_COMMAND_KEY, VnpayUtils.VNP_COMMAND_QUERY_VALUE);
        vnpParams.put(VnpayUtils.VNP_TMN_CODE_KEY, VnpayUtils.VNP_TMN_CODE_VALUE);
        vnpParams.put(VnpayUtils.VNP_TXN_REF_KEY, vnpTxnRef);
        vnpParams.put(VnpayUtils.VNP_ORDER_INFO_KEY, vnpTxnRef);
        vnpParams.put(VnpayUtils.VNP_TRANSACTION_DATE_KEY, transDate);
        vnpParams.put(VnpayUtils.VNP_CREATE_DATE_KEY, vnp_CreateDate);
        vnpParams.put(VnpayUtils.VNP_IP_ADDR_KEY, vnIpAddr);
        var hash_Data = String.join(
                "|",
                requestId,
                VnpayUtils.VNP_VERSION_VALUE,
                VnpayUtils.VNP_COMMAND_QUERY_VALUE,
                VnpayUtils.VNP_TMN_CODE_VALUE,
                vnpTxnRef,
                transDate,
                vnp_CreateDate,
                vnIpAddr,
                vnpTxnRef
        );
        var vnp_SecureHash = VnpayUtils.hmacSHA512(VnpayUtils.VNP_SECRET_KEY_VALUE, hash_Data);
        vnpParams.put(VnpayUtils.VNP_SECURE_HASH_TYPE_KEY, vnp_SecureHash);
        return vnpParams;
    }


    public record VnPayBody(long amount, String orderInfo) {
        public long amount() {
            return amount * 100;  // remove decimal point
        }
    }

    public record QueryVnPayBody(String txnRef, String transDate) {
    }

}
