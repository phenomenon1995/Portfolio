package com.techelevator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InventoryLog {
    public Map<String, VendItem> records;
    final String DEFAULT_PATH_TO_LOG = "vendingmachine.csv";
    private String pathToLog;
    private File inventoryFile;


    public InventoryLog() {
        this.pathToLog = DEFAULT_PATH_TO_LOG;
        ;
        inventoryFile = new File(pathToLog);
        logInventory();
    }
    public InventoryLog(String pathName){ //TO-DO exception handle if file doesn't exist.
        this.pathToLog = pathName;
        inventoryFile = new File(pathToLog);
        logInventory();
    }
    private Map<String, VendItem> logInventory() {
        records = new HashMap<>();
        try (Scanner fileScanner = new Scanner(inventoryFile)) {
            while (fileScanner.hasNextLine()) {
                VendItem item = null;
                String[] currentItem = fileScanner.nextLine().split("\\|");

                switch (currentItem[3]) {
                    case "Candy":
                        item = new VendItem.Candy(currentItem[1], new BigDecimal(currentItem[2]));
                        break;
                    case "Chip":
                        item = new VendItem.Chip(currentItem[1], new BigDecimal(currentItem[2]));
                        break;
                    case "Drink":
                        item = new VendItem.Drink(currentItem[1], new BigDecimal(currentItem[2]));
                        break;
                    case "Gum":
                        item = new VendItem.Gum(currentItem[1], new BigDecimal(currentItem[2]));
                        break;
                }
                records.put(currentItem[0], item);


            }
        } catch (IOException e) {
            System.out.println("File Error:" + e.getMessage());
        }
        return records;
    }





}
