package com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners;

import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class ShowPasswordOnTouchListener implements View.OnTouchListener {
    private EditText passwordEditText;

    public ShowPasswordOnTouchListener(EditText passwordfield) {
        this.passwordEditText = passwordfield;
    }
    public boolean onTouch(View v, MotionEvent event) {

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case MotionEvent.ACTION_UP:
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }
        return true;
    }
}
