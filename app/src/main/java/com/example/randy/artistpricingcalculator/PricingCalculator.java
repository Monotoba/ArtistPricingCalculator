package com.example.randy.artistpricingcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** Decimal pricing for one artwork; no Android or database dependencies. */
public final class PricingCalculator {
    private PricingCalculator() { }

    public enum Category { OVERHEAD, MARKETING, LABOR, MATERIALS, PACKAGING, MISC }

    public static final class Line {
        public final Category category;
        public final String name;
        public final double value;

        public Line(Category category, String name, double value) {
            this.category = category;
            this.name = name;
            this.value = value;
        }
    }

    public static final class Result {
        public final BigDecimal cost;
        public final BigDecimal labor;
        public final BigDecimal multiplier;
        public final BigDecimal retail;
        public final BigDecimal wholesale;

        private Result(BigDecimal cost, BigDecimal labor, BigDecimal multiplier) {
            this.cost = cost;
            this.labor = labor;
            this.multiplier = multiplier;
            BigDecimal unroundedRetail = cost.multiply(multiplier);
            retail = money(unroundedRetail);
            // Round each displayed price independently from the exact calculation.
            wholesale = money(unroundedRetail.divide(new BigDecimal("2")));
        }
    }

    public static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Result calculate(List<Line> lines, String multiplierText) {
        if (multiplierText == null || multiplierText.trim().length() > 64
                || !multiplierText.trim().matches("[0-9]+(?:\\.[0-9]+)?")) {
            throw new IllegalArgumentException("Enter a decimal multiplier of at least 1 (use a decimal point).");
        }
        BigDecimal multiplier = new BigDecimal(multiplierText.trim());
        if (multiplier.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("The multiplier must be at least 1.");
        }
        if (lines == null) throw new IllegalArgumentException("Cost records are missing.");
        BigDecimal cost = BigDecimal.ZERO;
        BigDecimal rate = null;
        BigDecimal hours = null;
        for (Line line : lines) {
            if (line == null || line.category == null || Double.isNaN(line.value)
                    || Double.isInfinite(line.value) || line.value < 0) {
                throw new IllegalArgumentException("All cost records must contain finite, nonnegative values.");
            }
            BigDecimal value = BigDecimal.valueOf(line.value);
            if (line.category == Category.LABOR) {
                String name = line.name == null ? "" : line.name.trim();
                if ("Hourly Rate".equalsIgnoreCase(name)) {
                    if (rate != null) throw new IllegalArgumentException("Keep only one labor record named Hourly Rate.");
                    rate = value;
                } else if ("Hours".equalsIgnoreCase(name)) {
                    if (hours != null) throw new IllegalArgumentException("Keep only one labor record named Hours.");
                    hours = value;
                } else {
                    throw new IllegalArgumentException("Name labor records Hourly Rate and Hours; other labor names cannot be calculated.");
                }
            } else {
                cost = cost.add(value);
            }
        }
        if ((rate == null) != (hours == null)) {
            throw new IllegalArgumentException("Labor requires both an Hourly Rate record and an Hours record.");
        }
        BigDecimal labor = rate == null ? BigDecimal.ZERO : rate.multiply(hours);
        return new Result(cost.add(labor), labor, multiplier);
    }
}
