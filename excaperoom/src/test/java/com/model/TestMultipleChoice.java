package com.model;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestMultipleChoice {

/*  
    when making this tester, I realized there are no existing getter methods
    in the multiplechoice class, meaning there's not an easy way for me to
    check if some things actually work, and I figured that it would go
    against the nature of the tests to just add getter methods, so I'm
    reporting the lack of getter methods as an issue in github
*/

    private MultipleChoice mc;

    @Before
    public void setUp() {
        mc = new MultipleChoice();
    }

    @Test
    public void testConstructorCreatesNonNullObject() {
        assertNotNull("MultipleChoice object should not be null after construction", mc);
        assertNotNull("toString() should not return null", mc.toString());
        assertTrue("toString() should start with 'Multiple Choice:'", mc.toString().startsWith("Multiple Choice:"));
    }

    @Test
    public void testSetQuestionAffectsOutput() {
        mc.setQuestion("What is 2+2?");
        ArrayList<String> options = new ArrayList<String>();
        options.add("3"); options.add("4"); options.add("5");
        mc.setOptions(options);
        String result = mc.toString();
        assertNotNull("toString() should work after setting question", result);
    }

    @Test
    public void testSetOptionsAffectsToString() {
        ArrayList<String> options = new ArrayList<>();
        options.add("A"); options.add("B"); options.add("C"); options.add("D");
        mc.setOptions(options);

        String text = mc.toString();
        for (String opt : options) {
            assertTrue("toString() should include option " + opt, text.contains(opt));
        }
    }

    @Test
    public void testSetCorrectAnswerDoesNotCrash() {
        mc.setCorrectAnswer("B");
        assertNotNull(mc.toString());
    }

    @Test
    public void testToStringIncludesAllOptions() {
        ArrayList<String> opts = new ArrayList<>();
        opts.add("Option 1");
        opts.add("Option 2");
        mc.setOptions(opts);

        String output = mc.toString();
        assertTrue("toString() should include 'Option 1'", output.contains("Option 1"));
        assertTrue("toString() should include 'Option 2'", output.contains("Option 2"));
    }
}