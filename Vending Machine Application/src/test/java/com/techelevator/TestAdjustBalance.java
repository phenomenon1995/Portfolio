package com.techelevator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import com.techelevator.VendingMachine;

import java.math.BigDecimal;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAdjustBalance {
    VendingMachine vm;
    BigDecimal amount;


    @Before
    public void init(){
        vm = new VendingMachine();
    }
    @Test
    public void t01_adjustBalance_result_if_feeded_money(){
        amount = new BigDecimal("10");
        BigDecimal [] actual = vm.adjustBalance(amount);
        BigDecimal [] expected = {new BigDecimal("10.0"), new BigDecimal("10.0")};
        assertEquals("Assertion failed", expected[0].compareTo(actual[0]), 0);
        assertEquals("Assertion failed", expected[1].compareTo(actual[1]), 0);
    }

}
