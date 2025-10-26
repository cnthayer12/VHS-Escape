package com.model;

public class SpeakTest {
    public static void main(String[] args) {
        // Test text
        String testText = "Text to speech test";
        
        // Call the speak method
        Speak.speak(testText);
        
        System.out.println("✅ Text to speech executed successfully.");
    }
    
}
