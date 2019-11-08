package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class InputValidator {
    public static final String REQUIRED_FIELD = "Required field";


    public boolean notEmpty(EditText et){
        if (!notShorterThan(et,1)) {
            et.setError(REQUIRED_FIELD);
            return false;
        }
            return true;
    }


    public boolean notEmpty(String s){
        return !s.trim().isEmpty();
    }

    public boolean notEmptyAll(EditText[] manyEditTexts){
     boolean allOk = true;
     for (EditText et: manyEditTexts){
         allOk = allOk && notEmpty(et);
     }
     return allOk;
    }

    public boolean notEmptyAll(String[] strings){
        boolean allOk = true;
        for (String string: strings){
            allOk = allOk && notEmpty(string);
        }
        return allOk;
    }

    public boolean notShorterThan (EditText et,int minLength){
        if (notShorterThan(txtFrom(et),minLength)) {
            et.setError("minimum " + minLength + " characters");
            return false;
        }
        return true;
    }

    public boolean notShorterThan (String s,int minLength){
        System.out.println("s: \""+ s +"\" is " + s.trim().length() + " chars long. Min Length is:" + minLength);
        return (notEmpty(s) && s.trim().length() >= minLength);

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
        boolean ok = isValidEmail(txtFrom(et));
        if(!ok){et.setError("email not valid");};
        return ok;
    }

    public boolean isValidEmail(String s){
        return Patterns.EMAIL_ADDRESS.matcher(s.trim()).matches();
    }

    public boolean isValidPhone(String s){
        return Patterns.PHONE.matcher(s.trim()).matches();
    }

    public boolean isValidPhone(EditText et){
        boolean ok = isValidPhone(txtFrom(et));
        if(!ok){et.setError("phone not valid");};
        return ok;
    }


    public String txtFrom(EditText et){
        return et.getText().toString();
    }
}

