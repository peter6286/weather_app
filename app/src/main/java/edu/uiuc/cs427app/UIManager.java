package edu.uiuc.cs427app;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/** UIManager class is used to house the methods used to make changes on the UI.
**/
public class UIManager {
    public SharedPreferences preferences;
    public ViewGroup currentLayout;
    public UserDao userDao;
    public void setThemePreference(boolean isDefaultTheme) {
        //SharedPreferences preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDefaultTheme", isDefaultTheme);
        editor.apply();
    }

    /** getThemePreference method to get the saved theme preference **/
    public boolean getThemePreference() {
        //SharedPreferences preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        return preferences.getBoolean("isDefaultTheme", false);
    }

    /** setButtonPreference method to set the button preference 
        @param    isRounded    is button preference to be rounded **/
    public void setButtonPreference(boolean isRounded) {
        //SharedPreferences preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isRounded", isRounded);
        editor.apply();
    }


    /** getButtonPreference method to get button preference **/
    public boolean getButtonPreference() {
        //SharedPreferences preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        return preferences.getBoolean("isRounded", false);
    }

    /** setTextSizePreference method to set text size preference 
        @param    isTextLarge    is text large preference enabled **/
    public void setTextSizePreference(boolean isTextLarge){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isTextLarge", isTextLarge);
        editor.apply();
    }
    
    /** getTextSizePreference method to get text size preference **/
    public boolean getTextSizePreference() {
        return preferences.getBoolean("isTextLarge", false);
    }

    /** setSwitchTextSize method to set switch text size 
        @param    currentSwitch   current switch
        @param    isTextLarge     is current text large user preference 
    **/
    private void setSwitchTextSize(Switch currentSwitch, boolean isTextLarge) {
        if (isTextLarge) {
            currentSwitch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else {
            currentSwitch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    /** setEditTextSize method to set edit text size 
        @param    editText        edit text
        @param    isTextLarge     is current text large user preference 
    **/
    private void setEditTextSize(EditText editText, boolean isTextLarge) {
        if (isTextLarge) {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    /** setTextViewSize method to set text view size 
        @param    textView        text view
        @param    isTextLarge     is current text large user preference 
    **/
    public void setTextViewSize(TextView textView, boolean isTextLarge) {
        if (isTextLarge) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    /** setButtonStyle method to set text view size 
        @param    button           button
        @param    isRounded        is rounded button user preference 
    **/
    public void setButtonStyle(Button button, boolean isRounded) {
        if (isRounded) {
            button.setBackgroundResource(R.drawable.round_button);
        } else {
            button.setBackgroundResource(R.drawable.square_button);
        }
    }

    /** changeStyleRecursive method to set theme preferences for component recursively
        @param    currentGroup group of UI componenets to modify
    **/
    public void changeStyleRecursive(ViewGroup currentGroup) {
        Boolean isRounded = getButtonPreference();
        Boolean isTextLarge = getTextSizePreference();

        for (int i = 0; i < currentGroup.getChildCount(); i++) {
            View childView = currentGroup.getChildAt(i);
            if (childView instanceof Button && !(childView instanceof Switch)) {
                Button currentButton = (Button) childView;
                setButtonStyle(currentButton, isRounded);
            } else if (childView instanceof EditText) {
                EditText currentEditText = (EditText) childView;
                setEditTextSize(currentEditText, isTextLarge);
            } else if (childView instanceof Switch) {
                Switch currentSwitch = (Switch) childView;
                setSwitchTextSize(currentSwitch, isTextLarge);
            } else if (childView instanceof TextView) {
                TextView currentTextView = (TextView) childView;
                setTextViewSize(currentTextView, isTextLarge);
            }
            else if (!(childView instanceof Space)) {
                ViewGroup nextGroup = (ViewGroup) childView;
                this.changeStyleRecursive(nextGroup);
            }

        }
    }
    /** resetStylePreference method to reset to default style for theme
     *
     */
    public void resetStylePreference() {
        this.setThemePreference(false);
        this.setButtonPreference(false);
        this.setTextSizePreference(false);
    }
}
