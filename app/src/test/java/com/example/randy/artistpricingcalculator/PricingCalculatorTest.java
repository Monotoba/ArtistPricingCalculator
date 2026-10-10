package com.example.randy.artistpricingcalculator;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import static com.example.randy.artistpricingcalculator.PricingCalculator.Category.*;

public class PricingCalculatorTest {
    private PricingCalculator.Line line(PricingCalculator.Category category, String name, double value) {
        return new PricingCalculator.Line(category, name, value);
    }
    private void rejects(List<PricingCalculator.Line> lines, String multiplier) {
        try {
            PricingCalculator.calculate(lines, multiplier);
            fail("Expected invalid input to be rejected");
        } catch (IllegalArgumentException expected) {
            assertFalse(expected.getMessage().isEmpty());
        }
    }
    @Test public void workedExampleIncludesEveryCostCategoryAndFractionalLabor() {
        PricingCalculator.Result r = PricingCalculator.calculate(Arrays.asList(
                line(OVERHEAD,"Studio allocation",10), line(MARKETING,"Ads allocation",5),
                line(LABOR,"Hourly Rate",20), line(LABOR,"Hours",1.5),
                line(MATERIALS,"Canvas",12.5), line(PACKAGING,"Box",2.5), line(MISC,"Fees",1)), "3.5");
        assertEquals("61.00", r.cost.toPlainString());
        assertEquals("30.00", r.labor.toPlainString());
        assertEquals("213.50", r.retail.toPlainString());
        assertEquals("106.75", r.wholesale.toPlainString());
    }
    @Test public void zeroCostsAndAbsentLaborAreAllowed() {
        PricingCalculator.Result r = PricingCalculator.calculate(Collections.<PricingCalculator.Line>emptyList(), "1");
        assertEquals("0.00", r.retail.toPlainString());
        assertEquals("0.00", r.wholesale.toPlainString());
    }
    @Test public void decimalAdditionDoesNotAccumulateBinaryRoundingError() {
        PricingCalculator.Result r = PricingCalculator.calculate(Arrays.asList(
                line(MATERIALS,"A",0.1),line(MATERIALS,"B",0.2)),"1");
        assertEquals("0.3", r.cost.toPlainString());
        assertEquals("0.30", r.retail.toPlainString());
    }
    @Test public void halfUpPricesAreRoundedIndependently() {
        PricingCalculator.Result r = PricingCalculator.calculate(Arrays.asList(line(MATERIALS,"A",0.005)),"1");
        assertEquals("0.01", r.retail.toPlainString());
        assertEquals("0.00", r.wholesale.toPlainString());
    }
    @Test public void laborNamesIgnoreCaseAndSurroundingWhitespace() {
        PricingCalculator.Result r = PricingCalculator.calculate(Arrays.asList(
                line(LABOR," hourly rate ",12.5),line(LABOR,"HOURS",0.25)),"4");
        assertEquals("12.50",r.retail.toPlainString());
        assertEquals("6.25",r.wholesale.toPlainString());
    }
    @Test public void incompleteLaborIsRejectedEvenWhenZero() {
        rejects(Arrays.asList(line(LABOR,"Hours",0)),"3");
        rejects(Arrays.asList(line(LABOR,"Hourly Rate",0)),"3");
    }
    @Test public void ambiguousAndUnknownLaborAreRejected() {
        rejects(Arrays.asList(line(LABOR,"Hours",1),line(LABOR,"Hours",2)),"3");
        rejects(Arrays.asList(line(LABOR,"Hourly Rate",1),line(LABOR,"Hourly Rate",2)),"3");
        rejects(Arrays.asList(line(LABOR,"Painting",1)),"3");
        rejects(Arrays.asList(line(LABOR,null,1)),"3");
    }
    @Test public void negativeAndNonfiniteValuesAreRejectedInAllCategories() {
        for (PricingCalculator.Category category : PricingCalculator.Category.values()) {
            for (double value : new double[]{-1,Double.NaN,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY}) {
                rejects(Arrays.asList(line(category,"Hours",value)),"3");
            }
        }
    }
    @Test public void invalidMultipliersAreRejected() {
        for (String multiplier : new String[]{null,""," ","0.99","-3","NaN","Infinity","1e2","1,5","3.5.6"}) {
            rejects(Collections.<PricingCalculator.Line>emptyList(),multiplier);
        }
        rejects(Collections.<PricingCalculator.Line>emptyList(),new String(new char[65]).replace('\0','9'));
    }
    @Test public void malformedRecordsAreRejected() {
        rejects(null,"3");
        rejects(Arrays.asList((PricingCalculator.Line)null),"3");
        rejects(Arrays.asList(line(null,"A",1)),"3");
    }
    @Test public void customMultiplierHasNoPresetRestriction() {
        PricingCalculator.Result r = PricingCalculator.calculate(Arrays.asList(line(MATERIALS,"A",2))," 1.125 ");
        assertEquals("2.25",r.retail.toPlainString());
        assertEquals("1.13",r.wholesale.toPlainString());
    }
    @Test public void zeroLaborPairIsAllowed() {
        PricingCalculator.Result r = PricingCalculator.calculate(Arrays.asList(
                line(LABOR,"Hourly Rate",0),line(LABOR,"Hours",0)),"3");
        assertEquals("0.00",r.retail.toPlainString());
    }
}
