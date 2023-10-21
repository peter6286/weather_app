package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {
    private EditText eUsername;
    private EditText ePassword;
    private Button eSignInButton;
    private Button eSignUpButton;
    private String username = "quanhn2";
    private String password = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                else if (inputUsername.equals(username) && inputPassword.equals(password)) {
                    Toast.makeText(SignInActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignInActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
}