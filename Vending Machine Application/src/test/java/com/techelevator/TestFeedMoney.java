package com.techelevator;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import com.techelevator.VendingMachine;

import java.math.BigDecimal;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFeedMoney {

    VendingMachine vm;
    BigDecimal addedMoney;

    @Before
    public void init(){
        vm = new VendingMachine();
    }
    @Test
    public void t01_feedMoney_testing_adding_money_getting_positive_result(){
        addedMoney = new BigDecimal("10");
        BigDecimal actual = vm.feedMoney(addedMoney);
        BigDecimal expected = new BigDecimal("0");
        assertEquals("Assertion failed", actual.compareTo(expected), 1);
    }
    @Test
    public void t02_feedMoney_testing_exception_when_balance_negative(){
        addedMoney = new BigDecimal("-10");
        assertThrows(RuntimeException.class, () -> {vm.feedMoney(addedMoney);});
    }
}
