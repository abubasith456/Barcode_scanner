package com.example.grootan.utils;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        try {
            pattern = Pattern.compile(EMAIL_PATTERN);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public boolean validate(final String email) {
        try {
//            matcher = pattern.matcher(email);
//            return matcher.matches();

            return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
            return false;
        }
    }
}