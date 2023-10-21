package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText eUsername;
    private EditText ePassword;
    private EditText ePasswordConfirm;
    private Switch eDarkThemeSwitch;
    private Button eSignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        eUsername = findViewById(R.id.signUpUsername);
        ePassword = findViewById(R.id.signUpPassword);
        ePasswordConfirm = findViewById(R.id.signUpPasswordConfirm);
        eSignUpButton = findViewById(R.id.completeSignUp);
        eDarkThemeSwitch = findViewById(R.id.darkThemeSwitch);

        eSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = eUsername.getText().toString();
                String inputPassword = ePassword.getText().toString();
                String inputPasswordConfirm = ePasswordConfirm.getText().toString();
                Boolean inputDarkTheme = eDarkThemeSwitch.isChecked();

                if (inputUsername.isEmpty() || inputPassword.isEmpty() || inputPasswordConfirm.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if (!inputPassword.equals(inputPasswordConfirm)) {
                    Toast.makeText(SignUpActivity.this, "Password fields do not match", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}