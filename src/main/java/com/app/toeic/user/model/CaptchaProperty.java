package com.app.toeic.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;

public record CaptchaProperty(String answer, byte[] captcha) {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaptchaProperty(var thatAnswer, var thatCaptcha))) {
            return false;
        }
        return answer.equals(thatAnswer) && Arrays.equals(captcha, thatCaptcha);
    }

    @Override
    public int hashCode() {
        var result = answer.hashCode();
        result = 31 * result + Arrays.hashCode(captcha);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("answer", answer)
                .append("captcha", captcha)
                .toString();
    }
}
