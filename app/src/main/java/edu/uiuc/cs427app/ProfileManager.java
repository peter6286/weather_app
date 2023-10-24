package edu.uiuc.cs427app;

import java.util.List;

public class ProfileManager {

    UserDao userDao;

    // Sign-up result class
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

    // Sign-in result class
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

    // Sign-up method
    public SignUpResult signUp(String username, String password) {
        try {
            // Check if the username already exists
            if (userDao.checkUserExistence(username)) {
                List<User> users = userDao.findUsersByName(username);
                User foundUser = users.get(0);
                return new SignUpResult(false, "Username already exists.", foundUser);
            }

            // Create new user and insert into DB
            User newUser = new User(username,password);
            userDao.insert(newUser);

            return new SignUpResult(true, "Successfully signed up.", newUser);

        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e.getCause());
        }
    }

    // Sign-in method
    public SignInResult signIn(String username, String password) {
        try {
            // Check if the username exists
            if (userDao.checkUserExistence(username)) {
                List<User> users = userDao.findUsersByName(username);
                User foundUser = users.get(0);

                // Check if the password matches
                if (foundUser.getPassword().equals(password)) {
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
}
