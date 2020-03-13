package com.groupassignment2019.bartertraderappgithuballsetup;

import android.content.Context;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.InputValidator;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ValidatorTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.groupassignment2019.bartertraderappgithuballsetup", appContext.getPackageName());
    }

    @Ignore
    public void validator() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        InputValidator iv = new InputValidator(appContext);
        String ett = "test@test.com";
        EditText et = new EditText(appContext);
        et.setText(ett);

        assertTrue(iv.isValidEmail(et));
        //assertEquals("com.groupassignment2019.bartertraderappgithuballsetup", appContext.getPackageName());
    }

    }
