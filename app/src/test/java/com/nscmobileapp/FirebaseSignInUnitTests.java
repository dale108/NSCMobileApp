package com.nscmobileapp;

import android.app.Application;
import android.content.SharedPreferences;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FirebaseSignInUnitTests {

    @Test
    public void validateInputsTest() {
        assertFalse(InputValidator.validateInputs(null, null, null).isValid);
        assertFalse(InputValidator.validateInputs("badEmail", "okpass", "name").isValid);
        assertTrue(InputValidator.validateInputs("goodEmail@email.com", "okpass", "name").isValid);
    }
}

