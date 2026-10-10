package com.example.randy.artistpricingcalculator;

/** Input validation shared by cost entry and editing. */
public final class CostEntryValidator {
    private CostEntryValidator() { }

    public static double parseAmount(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter an amount.");
        }
        final double amount;
        try {
            amount = Double.parseDouble(input.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a valid numeric amount.");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Amount must be finite and nonnegative.");
        }
        return amount;
    }

    public static String requireName(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter an item name.");
        }
        return input.trim();
    }
}
