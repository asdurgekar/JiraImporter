package com.cigniti.util;

import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
       String username = "<>";
       String password = "<>";
       return new PasswordAuthentication(username, password);
    }
}
