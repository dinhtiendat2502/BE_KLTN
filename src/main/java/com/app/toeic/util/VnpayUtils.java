package com.app.toeic.util;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@UtilityClass
public class VnpayUtils {
    public static final String VNP_REQUEST_ID = "vnp_RequestId";
    public static final String VNP_VERSION_KEY = "vnp_Version";
    public static final String VNP_COMMAND_KEY = "vnp_Command";
    public static final String VNP_TMN_CODE_KEY = "vnp_TmnCode";
    public static final String VNP_AMOUNT_KEY = "vnp_Amount";
    public static final String VNP_CURR_CODE_KEY = "vnp_CurrCode";
    public static final String VNP_BANK_CODE_KEY = "vnp_BankCode";
    public static final String VNP_TXN_REF_KEY = "vnp_TxnRef";
    public static final String VNP_ORDER_INFO_KEY = "vnp_OrderInfo";
    public static final String VNP_RETURN_URL_KEY = "vnp_ReturnUrl";
    public static final String VNP_IP_ADDR_KEY = "vnp_IpAddr";
    public static final String VNP_ORDER_TYPE_KEY = "vnp_OrderType";
    public static final String VNP_LOCALE_KEY = "vnp_Locale";
    public static final String VNP_CREATE_DATE_KEY = "vnp_CreateDate";
    public static final String VNP_TRANSACTION_DATE_KEY = "vnp_TransactionDate";
    public static final String VNP_EXPIRE_DATE_KEY = "vnp_ExpireDate";
    public static final String VNP_SECURE_HASH_TYPE_KEY = "vnp_SecureHash";

    public static final String VNP_VERSION_VALUE = "2.1.0";
    public static final String VNP_COMMAND_VALUE = "pay";
    public static final String VNP_COMMAND_QUERY_VALUE = "querydr";
    public static final String VNP_CURR_CODE_VALUE = "VND";
    public static final String VNP_TMN_CODE_VALUE = "GLE8YXG4";
    public static final String VNP_SECRET_KEY_VALUE = "ZCVPMHAELZKRPGTFLWJDPLQVPHBWEKXG";
    public static final String VNP_ORDER_TYPE_VALUE = "other";
    public static final String VNP_LOCALE_VALUE = "vn";
    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_QUERY_URL = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    public static final String ALGORITHM_HMAC = "HmacSHA512";
    public static final String VN_PAY_RETURN_URL = "VN_PAY_RETURN_URL";
    public static final String TIME_ZONE_DEFAULT = "Etc/GMT+7";
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final int DEFAULT_TIME_END = 15;

    private static final Random rnd = new Random();

    public String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final var hmac512 = Mac.getInstance(ALGORITHM_HMAC);
            var hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, ALGORITHM_HMAC);
            hmac512.init(secretKey);
            var dataBytes = data.getBytes(StandardCharsets.UTF_8);
            var result = hmac512.doFinal(dataBytes);
            var sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ignored) {
            return "";
        }
    }

    public static String getRandomNumber(int len) {
        var chars = "0123456789";
        var sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
