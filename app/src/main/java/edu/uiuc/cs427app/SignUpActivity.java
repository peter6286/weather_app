package edu.uiuc.cs427app;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/** SignUpActivity class that formulates the page a user lands on to sign up.
     Methods in this class will create the sign up page as well as make changes
     to the customized UI depending on user input and create the database 
     to handle sign up activity. 
**/
public class SignUpActivity extends AppCompatActivity {
    private UserDao userDao;
    private CityDao cityDao;
    private LinkUserCityDao linkUserCityDao;
    private UserCityDatabase db;
    private EditText eUsername;
    private EditText ePassword;
    private EditText ePasswordConfirm;
    private Switch eDefaultThemeSwitch;
    private Switch eButtonStyleSwitch;
    private Switch eTextSizeSwitch;
    private Button eSignUpButton;
    private UIManager uiManager;

    List<Button> allButtons = new ArrayList<>();
     /** onCreate method for sign up activity 
          @param     savedInstanceState     saved user instance
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDb();
        super.onCreate(savedInstanceState);
        uiManager = new UIManager();
        // Get theme selection
        uiManager.preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        boolean isDefaultTheme = uiManager.getThemePreference();
        // Set the theme based on the preference
        if (isDefaultTheme) {
            setTheme(R.style.Theme_NonDefault);
        } else {
            setTheme(R.style.Theme_Default);
        }
        setContentView(R.layout.activity_sign_up);

        uiManager.currentLayout =  findViewById(R.id.signUpRoot);
        uiManager.changeStyleRecursive(uiManager.currentLayout);

        eUsername = findViewById(R.id.signUpUsername);
        ePassword = findViewById(R.id.signUpPassword);
        ePasswordConfirm = findViewById(R.id.signUpPasswordConfirm);
        eDefaultThemeSwitch = findViewById(R.id.defaultThemeSwitch);
        eButtonStyleSwitch = findViewById(R.id.buttonStyleSwitch);
        eTextSizeSwitch = findViewById(R.id.textSizeSwitch);
        eSignUpButton = findViewById(R.id.completeSignUp);

        /** The eSignUpButton.setOnClickListener() code is responsible for handling the click event on the sign-up button. The View.OnClickListener interface is implemented to provide a callback method that is invoked when the button is clicked.
         **/
        eSignUpButton.setOnClickListener(new View.OnClickListener() {

            /** The onClick() method first checks if the username, password, and password confirmation fields are all empty. If any of the fields are empty, a toast message is displayed to the user. **/
            @Override
            public void onClick(View v) {
                String inputUsername = eUsername.getText().toString();
                String inputPassword = ePassword.getText().toString();
                String inputPasswordConfirm = ePasswordConfirm.getText().toString();
                Boolean isDefaultTheme = eDefaultThemeSwitch.isChecked();
                Boolean isRounded = eButtonStyleSwitch.isChecked();
                Boolean isLargeText = eTextSizeSwitch.isChecked();


                if (inputUsername.isEmpty() || inputPassword.isEmpty() || inputPasswordConfirm.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if (!inputPassword.equals(inputPasswordConfirm)) {
                    Toast.makeText(SignUpActivity.this, "Password fields do not match", Toast.LENGTH_SHORT).show();
                } else {
                    ProfileManager PM = new ProfileManager();
                    PM.userDao = userDao;
                    ProfileManager.SignUpResult out = PM.signUp(inputUsername, inputPassword, isDefaultTheme, isRounded, isLargeText);
                    if (out.isSuccess()) {
                        uiManager.resetStylePreference();
                        Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, out.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    /** onPostCreate will dictate the UI layout as set by the user on the sign up page
    @param    savedInstanceState    user saved instance data
    **/

    /** The eDefaultThemeSwitch.setOnCheckedChangeListener(), eButtonStyleSwitch.setOnCheckedChangeListener(), and eTextSizeSwitch.setOnCheckedChangeListener() functions are responsible for handling the checked change event on the theme, button style, and text size switches, respectively. The CompoundButton.OnCheckedChangeListener interface is implemented to provide a callback method that is invoked when the switch is checked or unchecked**/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
        eDefaultThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                uiManager.setThemePreference(isChecked);
                // Recreate the activity to apply the theme
                recreate();
            }
        });

        eButtonStyleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                uiManager.setButtonPreference(isChecked);
                // Recreate the activity to apply the theme
                recreate();
            }
        });

        eTextSizeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                uiManager.setTextSizePreference(isChecked);
                // Recreate the activity to apply the theme
                recreate();
            }
        });
    }

    /** createDb will create the database used for sign up activity
    **/
    public void createDb() {
        Context context = this;
        db = Room.databaseBuilder(context, UserCityDatabase.class, "database").allowMainThreadQueries().build();
        userDao = db.userDao();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }





}
