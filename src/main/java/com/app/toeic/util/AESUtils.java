package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;

@Log
@UtilityClass
public class AESUtils {
    private static final byte[] IV = new byte[16];

    public static String encrypt(String data) {
        return encrypt(data, Constant.AES_KEY, false);
    }

    public static String decrypt(String data) {
        return decrypt(data, Constant.AES_KEY, false);
    }

    public static String encrypt(String data, String key, boolean isUrl) {
        try {
            var property = getCipherProperty(key);
            property.cipher().init(true, property.keyParamWithIv());
            byte[] inputBytes = data.getBytes(StandardCharsets.UTF_8);
            int inputLength = inputBytes.length;
            byte[] output = new byte[property.cipher().getOutputSize(inputLength)];
            int length = property.cipher.processBytes(inputBytes, 0, inputLength, output, 0);
            property.cipher().doFinal(output, length);
            return isUrl ? Base64.getUrlEncoder().encodeToString(output) : Base64.getEncoder().encodeToString(output);
        } catch (Exception e) {
            log.log(Level.WARNING, "AESUtils >> encrypt >> Exception: ", e);
            return StringUtils.EMPTY;
        }
    }

    public static String decrypt(String data, String key, boolean isUrl) {
        try {
            var property = getCipherProperty(key);
            property.cipher().init(false, property.keyParamWithIv());
            var inputBytes = isUrl ? Base64.getUrlDecoder().decode(data) : Base64.getDecoder().decode(data);
            var inputLength = inputBytes.length;
            var output = new byte[property.cipher().getOutputSize(inputLength)];
            var length = property.cipher().processBytes(inputBytes, 0, inputLength, output, 0);
            property.cipher().doFinal(output, length);
            return new String(output, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            log.log(Level.WARNING, "AESUtils >> decrypt >> Exception: ", e);
            return StringUtils.EMPTY;
        }
    }

    private static CipherProperty getCipherProperty(String key) {
        var aesEngine = new AESEngine();
        var block = new CBCBlockCipher(aesEngine);
        var cipher = new PaddedBufferedBlockCipher(block);
        var keyParam = new KeyParameter(key.getBytes(StandardCharsets.UTF_8));
        var keyParamWithIv = new ParametersWithIV(keyParam, IV, 0, 16);
        return new CipherProperty(cipher, keyParamWithIv);
    }

    private record CipherProperty(PaddedBufferedBlockCipher cipher, ParametersWithIV keyParamWithIv) {

    }

}
