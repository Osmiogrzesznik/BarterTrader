package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

//@RunWith(RobolectricTestRunner.class)
public class InputValidatorTest {

    private InputValidator iv = new InputValidator(null);

    @Test
    public void notEmpty() {
        assertTrue(iv.notEmpty("t"));
        assertTrue(iv.notEmpty(" t "));
        assertTrue(!iv.notEmpty("\n\t"));
        assertTrue(!iv.notEmpty("   "));
        assertTrue(!iv.notEmpty("  \n\t "));
    }

    @Test
    public void notEmptyAll() {
        String[] AllOk = {"f", "d", "x x"};
        String[] OneEmpty = {"f", " ", "x x"};
        String[] AllEmpty = {"", " ", "\n"};
        String[] EmptyAtEnd = {"rr", "rere", ""};
        String[] AllEmpty2 = {"",""};
        String[] AllNull = {null,null};
        assertTrue(iv.notEmptyAll(AllOk));
        assertFalse(iv.notEmptyAll(OneEmpty));
        assertFalse(iv.notEmptyAll(AllEmpty));
        assertFalse(iv.notEmptyAll(AllEmpty2));
        assertFalse(iv.notEmptyAll(AllNull));
        assertFalse(iv.notEmptyAll(EmptyAtEnd));
    }

    @Test
    public void notShorterThan() {
        assertTrue(iv.notShorterThan("aa", 1));
        assertTrue(iv.notShorterThan("a", 1));
        assertTrue(iv.notShorterThan("password ", 8));
        assertTrue(iv.notShorterThan("password", 8));
        assertFalse(iv.notShorterThan("    ", 4));
        assertFalse(iv.notShorterThan(" ", 1));

    }

    @Test
    public void isValidEmail() {
        /**
         * https://en.m.wikipedia.org/wiki/Email_address
         */
        assertTrue(iv.isValidEmail("simple@example.com"));
        assertTrue(iv.isValidEmail("very.common@example.com"));
        assertTrue(iv.isValidEmail("disposable.style.email.with+symbol@example.com"));
        assertTrue(iv.isValidEmail("other.email-with-hyphen@example.com"));
        assertTrue(iv.isValidEmail("fully-qualified-domain@example.com"));
        assertTrue(iv.isValidEmail("user.name+tag+sorting@example.com"));
        assertTrue(iv.isValidEmail("x@example.com"));
        assertTrue(iv.isValidEmail("example-indeed@strange-example.com"));
        assertTrue(iv.isValidEmail("example@s.example"));
        assertTrue(iv.isValidEmail( "user%example.com@example.or"));
//        assertTrue(iv.isValidEmail("\" \"@example.org")); invalid according to Patterns.EMAIL_ADDRESS
//        assertTrue(iv.isValidEmail("\"john..doe\"@example.org"));invalid according to Patterns.EMAIL_ADDRESS
//        assertTrue(iv.isValidEmail("mailhost!username@example.org"));invalid according to Patterns.EMAIL_ADDRESS
//        assertTrue(iv.isValidEmail("admin@mailserver1")); invalid according to Patterns.EMAIL_ADDRESS

        assertFalse(iv.isValidEmail( "Abc.example.com"));// (no @ character)
        assertFalse(iv.isValidEmail( "A @b@c@example.com"));// (only one @ is allowed outside quotation marks)
        assertFalse(iv.isValidEmail( "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com"));//(none of the special characters in this local-part are allowed outside quotation marks)
        assertFalse(iv.isValidEmail( "just\"not\"right@example.com"));// (quoted strings must be dot separated or the only element making up the local-part)
        assertFalse(iv.isValidEmail( "this is\"no_{}allowed@example.com"));//(spaces, quotes, and backslashes may only exist when within quoted strings and preceded by a backslash)
        assertFalse(iv.isValidEmail( "this\\ still\"notallowed@example.com"));//(even if escaped"));// (preceded by a backslash), spaces, quotes, and backslashes must still be contained by quotes)
        //assertFalse(iv.isValidEmail( "1234567890123456789012345678901234567890123456789012345678901234+x@example.com")); // according to Patterns this invalid email is valid

    }

    @Test
    public void isValidPhone() {

    }
}