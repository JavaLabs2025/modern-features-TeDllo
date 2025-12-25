package org.lab.auth;

import java.util.UUID;

public class AuthenticationContext {

    private static final ThreadLocal<UUID> USER_ID = new ThreadLocal<>();

    public static UUID get() {
        return USER_ID.get();
    }

    public static void set(UUID userId) {
        USER_ID.set(userId);
    }

    public static void clear() {
        USER_ID.remove();
    }

}
