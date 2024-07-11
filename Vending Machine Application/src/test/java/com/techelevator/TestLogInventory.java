package com.techelevator;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import com.techelevator.InventoryLog;
import com.techelevator.VendItem;

import java.math.BigDecimal;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLogInventory {
    InventoryLog il;
    Map<String, VendItem> expectedMap;
    Set<Map.Entry<String, VendItem>> a;
    Set<Map.Entry<String, VendItem>> b;

    @Before
    public void init(){
         expectedMap = new HashMap<>();
         expectedMap.put("A1",new VendItem.Chip("Potato Crisps", new BigDecimal("3.05")));
         expectedMap.put("A2",new VendItem.Chip("Stackers", new BigDecimal("1.45")));
         expectedMap.put("A3",new VendItem.Chip("Grain Waves", new BigDecimal("2.75")));
         expectedMap.put("A4",new VendItem.Chip("Cloud Popcorn", new BigDecimal("3.65")));
    }
    private boolean compareMaps() {
        List<String> aKeys = new ArrayList<>();
        List<VendItem> aValues = new ArrayList<>();
        List<String> bKeys = new ArrayList<>();
        List<VendItem> bValues = new ArrayList<>();

        for (Map.Entry entry : il.records.entrySet()){
            aKeys.add(entry.getKey().toString());
            aValues.add((VendItem)entry.getValue());
        }
        for (Map.Entry entry : expectedMap.entrySet()){
            bKeys.add(entry.getKey().toString());
            bValues.add((VendItem)entry.getValue());
        }
        for (int i = 0 ; i < 4; i++){
            if((!aKeys.get(i).equals(bKeys.get(i)) && aKeys.get(i).equals(bKeys.get(i)))){
                return false;
            }
        }
        return true;
    }

    @Test
    public void t01_logInventory_creates_correct_map_when_given_valid_file(){
        il = new InventoryLog("test_vendingmachines.csv");
        assertTrue("The created map was incorrect",compareMaps());
    }
    @Test
    public void t02_logInventory_creates_correct_map_with_default_constructor(){
        il = new InventoryLog();
        assertTrue("The created map was incorrect",compareMaps());
    }

    @Test
    public void t03_logInventory_doesnt_create_map_when_given_invalid_file(){
        il = new InventoryLog("");
        assertFalse("The created map was incorrect", expectedMap.equals(il.records));

    }

}
