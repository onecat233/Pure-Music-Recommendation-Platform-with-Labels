package com.example.music.utils;

import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static String getDecryptedAttribute(HttpSession session, String attributeName) throws Exception {
        String encryptedValue = (String) session.getAttribute(attributeName);
        return encryptedValue != null ? EncryptionUtils.decrypt(encryptedValue) : null;
    }

    public static void setEncryptedAttribute(HttpSession session, String attributeName, String value) throws Exception {
        session.setAttribute(attributeName, EncryptionUtils.encrypt(value));
    }
}

