package edu.uiuc.cs427app;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProfileManagerTest {

    private ProfileManager profileManager;
    private UserDao userDao;

    @Before
    public void setUp() {
        userDao = new InMemoryUserDao();
        profileManager = new ProfileManager();
        profileManager.userDao = userDao; // Manually inject the userDao
    }

    @Test
    public void testSignUp_UsernameExists() {
        userDao.insert(new User("existingUser", "existingPassword"));

        ProfileManager.SignUpResult result = profileManager.signUp("existingUser", "newPassword", true, true, true);

        assertFalse(result.isSuccess());
        assertEquals("Username already exists.", result.getMessage());
    }

    @Test
    public void testSignUp_Success() {
        ProfileManager.SignUpResult result = profileManager.signUp("newUser", "newPassword", true, true, true);

        assertTrue(result.isSuccess());
        assertEquals("Successfully signed up.", result.getMessage());
    }

    @Test
    public void testSignIn_UsernameNotFound() {
        ProfileManager.SignInResult result = profileManager.signIn("nonExistentUser", "anyPassword");

        assertFalse(result.isSuccess());
        assertEquals("Username not found.", result.getMessage());
    }

    @Test
    public void testSignIn_WrongPassword() {
        userDao.insert(new User("existingUser", "correctPassword"));

       ProfileManager.SignInResult result = profileManager.signIn("existingUser", "wrongPassword");

        assertFalse(result.isSuccess());
        assertEquals("Invalid password.", result.getMessage());
    }

    @Test
    public void testSignIn_Success() {
        userDao.insert(new User("existingUser", "correctPassword"));

        ProfileManager.SignInResult result = profileManager.signIn("existingUser", "correctPassword");

        assertTrue(result.isSuccess());
        assertEquals("Successfully signed in.", result.getMessage());
    }

    // Basic in-memory UserDao for testing
    class InMemoryUserDao implements UserDao {
        List<User> users = new ArrayList<>();

        @Override
        public void insert(User user) {
            users.add(user);
        }

        @Override
        public List<User> findUsersByName(String username) {
            List<User> result = new ArrayList<>();
            for (User user : users) {
                if (user.getUserName().equals(username)) {
                    result.add(user);
                }
            }
            return result;
        }
        @Override
        public Integer saveUserUI(String userName, Boolean isDefault, Boolean isRounded, Boolean isLargeText) {
            for (User user : users) {
                if (user.getUserName().equals(userName)) {
                    user.setDefaultTheme(isDefault);
                    user.setIsRounded(isRounded);
                    user.setIsLargeText(isLargeText);
                    return 1; // Assuming we just update a single user
                }
            }
            return 0; // No user found with that username
        }
        @Override
        public boolean checkUserExistence(String userName) {
            return users.stream().anyMatch(u -> u.getUserName().equals(userName));
        }


    }
}
