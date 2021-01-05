package com.urcodebin.api.security;

import javassist.NotFoundException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class SecurityConstants {
    
    public static final long EXPIRATION_TIME = 7_200_000; // 2 Hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String LOGIN_URL = "/api/account/login";
    public static final String SIGN_UP_URL = "/api/account/signup";
    public static final String PUBLIC_PASTE_URL = "/api/paste/public";

    /**
     * The secret is not stored in plain text as it would raise security issues
     * as it will be publicly available in the open-source project. To ensure
     * security, the {@key SECRET} is encoded in a .properties file and is then
     * decoded using Jasypt String Encryptor. This process requires a
     * password which is safely stored as an environment variable called
     * {@env JASYPT_ENCRYPTOR_PASSWORD}
     */
    public static String getSecret() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //We need the Jasypt password that is used to decrypt the SECRET property.
        encryptor.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"));

        InputStream inputStream;
        try {
            Properties prop = new EncryptableProperties(encryptor);
            String propFileName = "application.properties";

            inputStream = SecurityConstants.class.getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            String secret = prop.getProperty("SECRET");
            if(secret == null){
                throw new NotFoundException("SECRET property was not found in .properties file");
            }
            return secret;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }
}