package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ApplicationProvider;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {
    private UserDao userDao;
    private CityDao cityDao;
    private LinkUserCityDao linkUserCityDao;
    private UserCityDatabase db;
    private EditText eUsername;
    private EditText ePassword;
    private Button eSignInButton;
    private Button eSignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDb();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        eUsername = findViewById(R.id.signInUsername);
        ePassword = findViewById(R.id.signInPassword);
        eSignInButton = findViewById(R.id.signInButton);
        eSignUpButton = findViewById(R.id.signUpButton);

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
                        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("signed_in", true).apply();
                        sharedPreferences.edit().putString("username", inputUsername).apply();
                        Toast.makeText(SignInActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
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
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createDb() {
        Context context = this;
        db = Room.databaseBuilder(context, UserCityDatabase.class, "database").allowMainThreadQueries().build();
        userDao = db.userDao();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }
}