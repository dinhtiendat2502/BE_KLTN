package com.app.toeic.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AvatarHelper {
    public String getAvatar(String avatar) {
        if (avatar == null || avatar.isEmpty()) {
            return "https://png.pngtree.com/png-clipart/20210129/ourmid/pngtree-default-male-avatar-png-image_2811083.jpg";
        }
        return avatar;
    }
}
