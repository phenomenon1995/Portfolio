package com.techelevator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import com.techelevator.VendingMachine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGiveChange {

    VendingMachine vm;
    int[] expected;

    @Before
    public void init(){
        vm = new VendingMachine();
        expected = new int[4];

    }
    @Test
    public void t01_giveChange_dispenses_correct_change_when_no_balance_due(){
        expected[0] = 0;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        vm.adjustBalance(new BigDecimal("0.00"));
        assertArrayEquals("The correct change was not dispensed", expected, vm.giveChange());
    }
    @Test
    public void t02_giveChange_dispenses_correct_change_when_some_coins_required_due(){
        expected[0] = 4;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        vm.adjustBalance(new BigDecimal("1.00"));
        assertArrayEquals("The correct change was not dispensed", expected, vm.giveChange());
    }
    @Test
    public void t03_giveChange_dispenses_correct_change_when_all_coins_required(){
        expected[0] = 3;
        expected[1] = 1;
        expected[2] = 1;
        expected[3] = 1;
        vm.adjustBalance(new BigDecimal("0.91"));
        assertArrayEquals("The correct change was not dispensed", expected, vm.giveChange());
    }

}
