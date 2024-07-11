package com.techelevator;
import com.techelevator.util.InvalidBalanceException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import com.techelevator.VendingMachine;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGenerateSalesReport {
    VendingMachine vm;
    List<String> expected = new ArrayList<>();
    Map<String, List<VendItem>> ms;
    List<String> keys;
    @Before
    public void init(){
        vm = new VendingMachine();
        vm.feedMoney(new BigDecimal("1000.00"));
        ms = vm.getMachineStock();
        keys = new ArrayList<>();
        keys.addAll(ms.keySet());
        
    }
    @Test
    public void t01_generateSalesReport_returns_correct_list_when_nothing_sold(){
        String[] values = {
                "U-Chews | 0",
                "Cola | 0",
                "Little League Chew | 0",
                "Moonpie | 0",
                "Dr. Salt | 0",
                "Chiclets | 0",
                "Potato Crisps | 0",
                "Cowtales | 0",
                "Mountain Melter | 0",
                "Triplemint | 0",
                "Stackers | 0",
                "Wonka Bar | 0",
                "Heavy | 0",
                "Grain Waves | 0",
                "Crunchie | 0",
                "Cloud Popcorn | 0",
                "0.00"
        };
        for (String line : values){
            expected.add(line);
        }
        String[] actual = vm.generateSalesReport().toArray(new String[0]);
        assertArrayEquals("Test Failed: Sales Report not correct.", values, actual);
    }
    @Test
    public void t02_generateSalesReport_returns_correct_list_when_some_items_sold(){
        String[] values = {
                "U-Chews | 3",
                "Cola | 3",
                "Little League Chew | 3",
                "Moonpie | 3",
                "Dr. Salt | 3",
                "Chiclets | 3",
                "Potato Crisps | 3",
                "Cowtales | 3",
                "Mountain Melter | 3",
                "Triplemint | 3",
                "Stackers | 3",
                "Wonka Bar | 3",
                "Heavy | 3",
                "Grain Waves | 3",
                "Crunchie | 3",
                "Cloud Popcorn | 3",
                "79.50"
        };

        buy(3);
        List<String> actual = vm.generateSalesReport();
        assertArrayEquals("Test Failed: Sales Report not correct.", values,actual.toArray(new String[0]) );
    }
    @Test
    public void t03_generateSalesReport_returns_correct_list_when_all_items_sold_out(){
        String[] values = {
                "U-Chews | 5",
                "Cola | 5",
                "Little League Chew | 5",
                "Moonpie | 5",
                "Dr. Salt | 5",
                "Chiclets | 5",
                "Potato Crisps | 5",
                "Cowtales | 5",
                "Mountain Melter | 5",
                "Triplemint | 5",
                "Stackers | 5",
                "Wonka Bar | 5",
                "Heavy | 5",
                "Grain Waves | 5",
                "Crunchie | 5",
                "Cloud Popcorn | 5",
                "132.50"
        };
        buy(5);
        List<String> actual = vm.generateSalesReport();
        assertArrayEquals("Test Failed: Sales Report not correct.", values,actual.toArray(new String[0]) );
    }
    private void buy(int num) {
        for (String key : keys){
            System.out.println(key);
            for (int i = 0; i < num; i++){
                try{
                    VendItem item = vm.getItem(key.toString());
                    //System.out.println(item);
                }
                catch (InvalidBalanceException e){
                    break;
                }
            }
        }
    }


}
