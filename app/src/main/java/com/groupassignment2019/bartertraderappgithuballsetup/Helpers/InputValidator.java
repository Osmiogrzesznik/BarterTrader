package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.groupassignment2019.bartertraderappgithuballsetup.PostNewItemActivity;

public class InputValidator {
    // TODO: 12/11/2019 displays only Required Field ? On registerActivity
    public static final String REQUIRED_FIELD = "Required field";
    private final Context ctx;

    public InputValidator(Context ctx) {
        this.ctx = ctx;
    }

    public void say(String msg) {
        if (this.ctx == null) {
            return;
        }
        Log.d("BOLO", msg);
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public boolean notEmpty(EditText et) {
        boolean notEmptyLocal = notEmpty(txtFrom(et));
        if (!notEmptyLocal) { // if field IS EMPTY (is not not-empty) set error on field
            et.setError(REQUIRED_FIELD);
            say("\"" + et.getText().toString() + "\" is not valid." + REQUIRED_FIELD);
            return false;
        }
        return true;
    }


    public boolean notEmpty(String s) {
        return !s.trim().isEmpty();
    }

    public boolean notEmptyAll(EditText[] manyEditTexts) {
        boolean allOk = true;
        for (EditText et : manyEditTexts) {
            boolean newOk = notEmpty(et);
            allOk = allOk && newOk;
        }
        return allOk;
    }

    public boolean notEmptyAll(String[] strings) {
        boolean allOk = true;
        for (String string : strings) {
            allOk = allOk && notEmpty(string);
        }
        return allOk;
    }

    public boolean notShorterThan(EditText et, int minLength) {
        if (!notShorterThan(txtFrom(et), minLength)) {
            String msg = "minimum " + minLength + " characters";
            et.setError(msg);
            say("\"" + et.getText().toString() + "\" is not valid." + msg);
            return false;
        }
        return true;
    }

    public boolean notShorterThan(String s, int minLength) {
        System.out.println("s: \"" + s + "\" is " + s.trim().length() + " chars long. Min Length is:" + minLength);
        return (notEmpty(s) && s.trim().length() >= minLength);
    }

    public boolean doesNotContainInvalidCharacters(EditText et, String[] invalidCharacters) {
        boolean allOk = true;
        String value = et.getText().toString();
        for (String invalidCharacter : invalidCharacters) {
            allOk = allOk && !value.contains(invalidCharacter);// if et contains this invalidCharacter allOk = allOk && false so then --> allOk = false;
        }
        if (!allOk) {
            et.setError("cannot contain");
            say("\"" + et.getText().toString() + "\" is not valid value for this editText");
        }
        return allOk;
    }

    public boolean isValidEmail(EditText et) {
        boolean ok = isValidEmail(txtFrom(et));
        if (!ok) {
            et.setError("email not valid");
            say("\"" + et.getText().toString() + "\" is not vali. email not valid");
        }
        return ok;
    }

    public boolean isValidEmail(String s) {
        return Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    public boolean isValidPhone(String s) {
        return Patterns.PHONE.matcher(s.trim()).matches();
    }

    public boolean isValidPhone(EditText et) {
        boolean ok = isValidPhone(txtFrom(et));
        if (!ok) {
            et.setError("phone not valid");
            say("\"" + et.getText().toString() + "\" is not valid phone number");
        }
        return ok;
    }


    public String txtFrom(EditText et) {
        return et.getText().toString();
    }
}

