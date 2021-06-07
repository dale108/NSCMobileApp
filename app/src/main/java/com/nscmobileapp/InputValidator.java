package com.nscmobileapp;

import android.content.SharedPreferences;

import java.util.regex.Pattern;

public class InputValidator {

    public static ValidationState validateInputs(String email, String password, String userName) {
        if (email == null || password == null || userName == null) {
            return new ValidationState(false, "values must not be null");
        }

        Pattern emailPattern = Pattern.compile("^([A-Za-z0-9+_.-]+)@([A-Za-z0-9+_.-]+)$");
        Pattern noWhitespacePattern = Pattern.compile("^\\w+$");

        if (emailPattern.matcher(email).matches()) {
            if (noWhitespacePattern.matcher(password).matches()
                    && noWhitespacePattern.matcher(userName).matches()) {
                return new ValidationState(true, "validation successful!");
            } else {
                return new ValidationState(false, "Poorly formatted username or password");
            }
        } else {
            return new ValidationState(false, "improperly formatted email address");
        }
    }

    public static SharedPreferences setSharedpreferance(SharedPreferences pref, String key, String value)
    {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
        return pref;
    }

    public static class ValidationState {
        public boolean isValid;
        public String message;

        public ValidationState(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }
    }
}
