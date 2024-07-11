package com.techelevator;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import com.techelevator.VendItem;


import java.math.BigDecimal;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestVendItemEquals {
    VendItem item1;
    VendItem item2;
    @Before
    public void init(){
        item1 = new VendItem.Chip("Chip1", new BigDecimal("1.00"));
    }
    @Test
    public void t01_equals_returns_true_when_two_VendItems_are_the_same_subclass_and_memory_address(){
        item2 = item1;
        assertTrue("The two items were not the same.", item1.equals(item2));
    }
    @Test
    public void t02_equals_returns_true_when_two_VendItems_are_the_same_subclass_and__different_memory_address(){
        item2 = new VendItem.Chip("Chip1", new BigDecimal("1.00"));
        assertTrue("The two items were not the same.", item1.equals(item2));
    }
    @Test
    public void t03_equals_returns_false_when_two_VendItems_are_the_same_subclass_and_memory_address(){
        item2 = new VendItem.Candy("Candy1", new BigDecimal("2.00"));
        assertFalse("The two items were not the same.", item1.equals(item2));

        
    }

}
