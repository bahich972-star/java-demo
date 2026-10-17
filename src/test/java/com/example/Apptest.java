package com.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AppTest {
    @Test
    public void testGetGreeting() {
        assertEquals("Hello Jenkins!", App.getGreeting());
    }
}
