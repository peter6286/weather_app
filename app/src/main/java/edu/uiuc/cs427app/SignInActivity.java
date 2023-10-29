package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ApplicationProvider;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/** SignInActivity class that formulates the page a user lands on to sign in.
    Methods will create the sign in page functionality and create the database used
    to house sign in data. **/
public class SignInActivity extends AppCompatActivity {
    private UserDao userDao;
    private CityDao cityDao;
    private LinkUserCityDao linkUserCityDao;
    private UserCityDatabase db;
    private EditText eUsername;
    private EditText ePassword;
    private Button eSignInButton;
    private Button eSignUpButton;
    private UIManager uiManager;
    /** onCreate method that will formualate the page to allow users
    to sign in
    @param savedInstanceState current saved state data
    **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDb();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        eUsername = findViewById(R.id.signInUsername);
        ePassword = findViewById(R.id.signInPassword);
        eSignInButton = findViewById(R.id.signInButton);
        eSignUpButton = findViewById(R.id.signUpButton);
        uiManager = new UIManager();
        uiManager.preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        Log.d("DEBUG", "UI" + uiManager.toString());

        /**
         The eSignInButton.setOnClickListener() and eSignUpButton.setOnClickListener() functions are responsible for handling the click events on the "Sign in" and "Sign up" buttons, respectively. The View.OnClickListener interface is implemented to provide a callback method that is invoked when the button is clicked. **/

        eSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = eUsername.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    ProfileManager PM = new ProfileManager();
                    PM.userDao = userDao;
                    ProfileManager.SignInResult out = PM.signIn(inputUsername, inputPassword);
                    if (out.isSuccess()) {
                        uiManager.preferences.edit().putBoolean("signedIn", true).apply();
                        uiManager.preferences.edit().putString("userName", inputUsername).apply();
                        uiManager.setThemePreference(out.getCheck_user().getDefaultTheme());
                        uiManager.setButtonPreference(out.getCheck_user().getIsRounded());
                        uiManager.setTextSizePreference(out.getCheck_user().getIsLargeText());
                        Toast.makeText(SignInActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        eSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiManager.resetStylePreference();
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    /** createDb method that will create the database used for sign in activity
    **/
    public void createDb() {
        Context context = this;
        db = Room.databaseBuilder(context, UserCityDatabase.class, "database").allowMainThreadQueries().build();
        userDao = db.userDao();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }
}
