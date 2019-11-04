package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import android.text.BoringLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Matcher;

public class InputValidator {
    public static final String REQUIRED_FIELD = "Required field";


    public boolean notEmpty(EditText et){
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            et.setError(REQUIRED_FIELD);
            return false;
        }
            return true;
    }

    public boolean notEmptyAll(EditText[] manyEditTexts){
     boolean allOk = true;
     for (EditText et: manyEditTexts){
         allOk = allOk && notEmpty(et);
     }
     return allOk;
    }

    public boolean notShorterThan (EditText et,int minLength){
        if (et.getText().toString().trim().length() < 6) {
            et.setError("must be >= " + minLength + " characters");
            return false;
        }
        return true;
    }

    public boolean doesNotContainInvalidCharacters(EditText et, String[] invalidCharacters){
        boolean allOk = true;
        String value = et.getText().toString();
        for (String invalidCharacter : invalidCharacters){
            allOk = allOk && ! value.contains(invalidCharacter);// if et contains this invalidCharacter allOk = allOk && false so then --> allOk = false;
        }
        if(! allOk ){
            et.setError("cannot contain" );
        }
        return allOk;
    }

    public boolean isValidEmail(EditText et){
        boolean ok = Patterns.EMAIL_ADDRESS.matcher(text(et)).matches();
        if(!ok){et.setError("email not valid");};
        return ok;
    }

    public boolean isValidPhone(EditText et){
        boolean ok = Patterns.PHONE.matcher(text(et)).matches();
        if(!ok){et.setError("phone not valid");};
        return ok;
    }


    public String text(EditText et){
        return et.getText().toString().trim();
    }
}
