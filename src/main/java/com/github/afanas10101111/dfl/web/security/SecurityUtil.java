package com.github.afanas10101111.dfl.web.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
    public static long getAuthUserId() {
        return 100001;
    }
}
