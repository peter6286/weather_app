package edu.uiuc.cs427app;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/** ProfileManager class will be used to manage a user's
    profile details. 
    Methods will teacker a user's sign in and sign up results
    as well as encrypt/decrypt their passwords.  **/
public class ProfileManager {

    UserDao userDao;

    private static final byte[] SECRET_KEY_BYTES = {
            // Replace with your desired key bytes
            (byte) 0x2b, (byte) 0x7e, (byte) 0x15, (byte) 0x16,
            (byte) 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6,
            (byte) 0xab, (byte) 0xf7, (byte) 0x15, (byte) 0x88,
            (byte) 0x09, (byte) 0xcf, (byte) 0x4f, (byte) 0x3c
    };

    SecretKeySpec key=new SecretKeySpec(SECRET_KEY_BYTES, "AES");


    /** class to track a user's sign up results **/
    public class SignUpResult {
        private boolean success;
        private String message;
        private User new_user;



        public SignUpResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.new_user = user;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getNew_user() { return new_user; }
    }

     /** class to track a user's sign in results **/
    public class SignInResult {
        private boolean success;
        private String message;
        private User check_user;

        public SignInResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.check_user = user;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getCheck_user() { return check_user; }
    }

     /** method to process a user's sign up
         @param    username    a unique name given to a user
         @param    password    a password given by the user for sign up and sign in
         @param    isDefaultTheme    dictates which theme to be used for the user
         @param    isRounded    dictates whether buttons are rounded or squares
         @param    isLargeText    dictates size of text
         @return    SignUpResult that tells the user if sign up was successful and why it wasn't if so.
         **/
    public SignUpResult signUp(String username, String password, Boolean isDefaultTheme, Boolean isRounded, Boolean isLargeText) {
        try {
            // Check if the username already exists
            if (userDao.checkUserExistence(username)) {
                List<User> users = userDao.findUsersByName(username);
                User foundUser = users.get(0);
                return new SignUpResult(false, "Username already exists.", foundUser);
            }


            String encrypted_password=null;
            try {
                encrypted_password=encryptPassword(password,key);
            } catch (NoSuchPaddingException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e) {
                throw new RuntimeException(e);
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            }
            
            // Create user and add to db
            User newUser = new User(username,encrypted_password.toString());
            newUser.setDefaultTheme(isDefaultTheme);
            newUser.setIsRounded(isRounded);
            newUser.setIsLargeText(isLargeText);

            userDao.insert(newUser);

            return new SignUpResult(true, "Successfully signed up.", newUser);

        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e.getCause());
        }
    }

    /** method to process a user's sign up
         @param    username    a unique name given to a user
         @param    password    a password given by the user for sign up and sign in
         @return   SignInResult that tells the user if sign in was successful and why it wasn't if so.
    **/
    public SignInResult signIn(String username, String password) {
        try {
            // Check if the username exists
            if (userDao.checkUserExistence(username)) {
                List<User> users = userDao.findUsersByName(username);
                User foundUser = users.get(0);

                // Check if the password matches
                String hashedPassword=foundUser.getPassword();
//                Log.v("op2",hashedPassword+" "+key.toString());
                String userPassword=decryptPassword(hashedPassword,key);
//                Log.v("op1",userPassword);

                if (userPassword.equals(password)) {
                    return new SignInResult(true, "Successfully signed in.", foundUser);
                } else {
                    return new SignInResult(false, "Invalid password.", foundUser);
                }
            } else {
                return new SignInResult(false, "Username not found.", null);
            }
        } catch (Exception e) {
            return new SignInResult(false, "Failed to connect to database.", null);
        }
    }




    /** Encrypts/Hashes the password to store in the database for added security 
        @param    password     password to be encrypted
        @param    secretKey    secret key to encrypt with
        return    encrypted password string
    **/
    public static String encryptPassword(String password, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());

        StringBuilder hexString = new StringBuilder();

        for (byte b : encryptedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /** Decrypts the hashed password to get the original passwords 
        @param    encryptedPassword     encrypted password to decrypt
        @param    secretKey             secret key to decrypt with
        return    decrypted password string
    **/
    public static String decryptPassword(String encryptedPassword, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedBytes = new byte[encryptedPassword.length() / 2];
        for (int i = 0; i < encryptedBytes.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(encryptedPassword.substring(index, index + 2), 16);
            encryptedBytes[i] = (byte) j;
        }

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        Log.v("op",decryptedBytes.toString());
        return new String(decryptedBytes);
    }
}
