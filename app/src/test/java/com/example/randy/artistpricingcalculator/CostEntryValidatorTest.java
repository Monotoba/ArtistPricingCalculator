package com.example.randy.artistpricingcalculator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CostEntryValidatorTest {
    @Test public void acceptsValidAmountsIncludingZeroAndFractions() {
        assertEquals(0.0, CostEntryValidator.parseAmount("0"), 0.0);
        assertEquals(1.25, CostEntryValidator.parseAmount(" 1.25 "), 0.0);
    }
    @Test public void rejectsEmptyInvalidNegativeAndNonfiniteAmounts() {
        for (String input : new String[]{"", " ", "not a number", "-1", "NaN", "Infinity", "1e999"}) {
            try {
                CostEntryValidator.parseAmount(input);
                fail("Expected rejection of: " + input);
            } catch (IllegalArgumentException expected) { }
        }
    }
    @Test public void validatesNames() {
        assertEquals("Canvas", CostEntryValidator.requireName(" Canvas "));
        for (String input : new String[]{ "", "  " }) {
            try {
                CostEntryValidator.requireName(input);
                fail("Expected rejection");
            } catch (IllegalArgumentException expected) { }
        }
    }
}
