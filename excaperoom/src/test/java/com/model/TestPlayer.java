package com.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

public class TestPlayer {

    @Test
    public void constructorWithoutUuid_generatesRandomUuid() {
        ArrayList<Progress> progressList = new ArrayList<>();
        Player p1 = new Player("Alice", progressList, "secret123");
        Player p2 = new Player("Bob", progressList, "secret456");

        assertNotNull("UUID should not be null", p1.getId());
        assertNotNull("UUID should not be null", p2.getId());

        assertNotEquals("Each Player should get a different UUID", p1.getId(), p2.getId());
    }

    @Test
    public void constructorWithUuid_usesProvidedUuid() {
        UUID fixedId = UUID.randomUUID();
        ArrayList<Progress> progressList = new ArrayList<>();
        Player p = new Player(fixedId, "Charlie", progressList, "p@ss");

        assertEquals("Player should keep the UUID that is passed in", fixedId, p.getId());
    }

    @Test
    public void constructorWithNullUuid_generatesNewUuid() {
        ArrayList<Progress> progressList = new ArrayList<>();
        Player p = new Player(null, "Dana", progressList, "hunter2");

        assertNotNull("UUID should not be null even if null was passed in", p.getId());
    }

    @Test
    public void checkPassword_returnsTrueForCorrectPassword() {
        Player p = new Player("Eve", new ArrayList<Progress>(), "letmein");

        assertTrue("checkPassword should return true for the correct password",
                p.checkPassword("letmein"));
    }

    @Test
    public void checkPassword_returnsFalseForIncorrectPassword() {
        Player p = new Player("Frank", new ArrayList<Progress>(), "abc123");

        assertFalse("checkPassword should return false for an incorrect password",
                p.checkPassword("wrongpass"));
    }

    @Test
    public void setPassword_updatesPasswordWhenNonEmpty() {
        Player p = new Player("Grace", new ArrayList<Progress>(), "oldPass");
        p.setPassword("newPass");

        assertEquals("Password should update if non-empty",
                "newPass", p.getPassword());
    }

    @Test
    public void setPassword_rejectsEmptyString() {
        Player p = new Player("Heidi", new ArrayList<Progress>(), "origPass");

        p.setPassword(""); 

        assertEquals("Password should remain unchanged if setPassword(\"\") is called",
                "origPass", p.getPassword());
    }

    @Test
    public void toString_includesDisplayNameAndUuid() {
        ArrayList<Progress> progressList = new ArrayList<>();
        Player p = new Player("Ivy", progressList, "pw");

        String result = p.toString();

        assertNotNull("toString() should not return null", result);
        assertTrue("toString() should include the displayName",
                result.contains("Ivy"));
        assertTrue("toString() should include the uuid",
                result.contains(p.getId().toString()));
    }


    @Test
    public void setPassword_nullPasswordShouldNotCrash() {
        Player p = new Player("Mallory", new ArrayList<Progress>(), "origPass");

        try {
            p.setPassword(null);
            // If we get here with no exception, cool.
            // We ALSO expect the password to stay the same because null is not valid.
            assertEquals("origPass", p.getPassword());
        } catch (NullPointerException npe) {
            fail("setPassword(null) should not throw NullPointerException");
        }
    }

    @Test
    public void checkPassword_nullArgumentShouldReturnFalseNotCrash() {
        Player p = new Player("Niaj", new ArrayList<Progress>(), "sekrit");

        try {
            Boolean result = p.checkPassword(null);

            // What SHOULD happen logically:
            // If the guess is null, that's not the right password.
            // So we expect false.
            assertFalse("checkPassword(null) should return false, not crash", result);
        } catch (NullPointerException npe) {
            fail("checkPassword(null) should not throw NullPointerException");
        }
    }

    @Test
    public void constructor_allowsNullProgressButPlayerStillExists() {
        Player p = new Player("Olivia", null, "pw123");

        assertNotNull("Player should still be constructed even if progress is null", p);
        assertEquals("Olivia", p.getDisplayName());
        assertEquals("pw123", p.getPassword());

        // This next assert is about design:
        // Do we EXPECT getProgress() to maybe be null?
        // If this fails, that's info for a bug report / model contract.
        assertNull("Progress was passed as null; getProgress() should reflect that (or code should defensively create an empty list).",
                p.getProgress());
    }
}