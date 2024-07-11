package com.techelevator;

import com.techelevator.util.InvalidBalanceException;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class VendingMachine {
    private BigDecimal balance = new BigDecimal("0.00");
    private Map<String, List<VendItem>> machineStock;
    private Date timeTurnedOn;
    private File transactionLogFile;
    private File salesReportFile;
    private final int MAX_CAPACITY = 5;
    private static final BigDecimal QUARTER = new BigDecimal("0.25");
    private static final BigDecimal DIME = new BigDecimal("0.1");
    private static final BigDecimal NICKEL = new BigDecimal("0.05");
    private static final BigDecimal PENNY = new BigDecimal("0.01");
    private InventoryLog logToUse;
    public static Date getDate(){
        return new Date();
    }
    //Constructors
    public VendingMachine(){

        machineStock = new HashMap<>();
        logToUse = new InventoryLog();
        this.timeTurnedOn = new Date();
        transactionLogFile = new File ("Log.txt");
        loadStock(logToUse);

    }
    public VendingMachine (String pathToLogs, InventoryLog logToUse){
        machineStock = new HashMap<>();

        this.timeTurnedOn = new Date();
        System.out.println(timeTurnedOn);
        File logDirectory = new File(pathToLogs);
        transactionLogFile = new File ("Log.txt");
        this.logToUse = logToUse;
        loadStock(logToUse);
    }

    //Getters
    public VendItem getItem(String loc) throws NullPointerException, IndexOutOfBoundsException, InvalidBalanceException {
        VendItem item = null;
        if(this.balance.compareTo(this.logToUse.records.get(loc).getPrice()) != -1) {
            item = machineStock.get(loc).remove(0);
            logTransaction(
                    getDate(),
                    item.getName() + " " + loc,
                    adjustBalance(item.getPrice().negate()));
        } else {
            throw new InvalidBalanceException("YOU DON'T HAVE ENOUGH MONEY TO MAKE THIS PURCHASE...");
        }
        return item;
    }
    public BigDecimal getBalance(){
        return this.balance;
    }
    public Map<String, List<VendItem>> getMachineStock(){
        return this.machineStock;
    }

    //Methods
    public BigDecimal feedMoney(BigDecimal moneyAdded) throws RuntimeException {
        if (moneyAdded.compareTo(new BigDecimal("0")) == 1) {
            logTransaction(getDate(), "FEED MONEY", adjustBalance(moneyAdded));
        } else {
            throw new RuntimeException();
        }
        return this.balance;
    }
    public BigDecimal[] adjustBalance(BigDecimal amountToAdjust){
        BigDecimal transactionValue = amountToAdjust;
        this.balance = this.balance.add(amountToAdjust);
        BigDecimal[] update = {transactionValue.abs(), this.balance};
        return update;
    }
    private boolean logTransaction(Date date, String action, BigDecimal[] adjustment){
        boolean logged = false;

        try (PrintWriter transactionWriter =
                     new PrintWriter(new FileOutputStream(transactionLogFile,true))
        ){
            transactionWriter.println(
                    String.format("%1$s %2$s $%3$s $%4$s",
                            date, action, adjustment[0], adjustment[1])
            );
            logged = true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ;
        return logged;
    }
    private Map<String, List<VendItem>> loadStock(InventoryLog inventoryLogToUse){
        for (Map.Entry loc : inventoryLogToUse.records.entrySet()){
            List<VendItem> itemSupply = new ArrayList<>();
            for (int i = 0; i < MAX_CAPACITY ; i++){
                itemSupply.add(inventoryLogToUse.records.get(loc.getKey()));
            }
            machineStock.put(loc.getKey().toString(), itemSupply);
        }
        return machineStock;
    }
    public List<String> generateSalesReport(){
        System.out.println("Generating Sales report");
        Date date = this.getDate();
        String dateText = String.format("%1$tb%1$td%1$tY_%1$tl%1$tM%1$tp_", date);
        salesReportFile = new File(dateText + "SalesReport.txt");
        List<String> salesReport = new ArrayList<>();
        BigDecimal totalSales = new BigDecimal("0.00");
        try(PrintWriter writerSalesReport = new PrintWriter(salesReportFile)){
            for (Map.Entry loc : machineStock.entrySet()){
                String key = loc.getKey().toString();
                VendItem item = logToUse.records.get(key);
                String itemName = item.getName();
                BigDecimal itemPrice = item.getPrice();
                int numSold = MAX_CAPACITY - machineStock.get(key).size();
                salesReport.add(String.format("%s | %d", itemName, numSold));
                totalSales = totalSales.add(itemPrice.multiply(new BigDecimal(numSold)));
            }
            for (String line : salesReport){
                writerSalesReport.println(line);
                System.out.println(line);
            }
            writerSalesReport.println(String.format("\n**TOTAL SALES: $%s", totalSales));
            System.out.println(String.format("\n**TOTAL SALES: $%s", totalSales));
            salesReport.add(totalSales.toString());

        } catch (IOException e){
            System.out.println(e);
            return null;
        }
        return salesReport;
    }
    public int[] giveChange() {
        System.out.println("\nTransaction Finished. Dispensing Change.");
        int[] coinsQDNP = new int[4];

        Map<String, Integer> changeDue = new HashMap<>();

        BigDecimal[] bigDecimalArray1 = this.balance.divideAndRemainder(QUARTER);

        int quarterNum = bigDecimalArray1[0].intValue();
        changeDue.put("Quarters: ", quarterNum);
        coinsQDNP[0] = quarterNum;

        BigDecimal[] bigDecimalArray2 = bigDecimalArray1[1].divideAndRemainder(DIME);
        int dimeNum = bigDecimalArray2[0].intValue();
        changeDue.put("Dimes: ", dimeNum);
        coinsQDNP[1] = dimeNum;

        BigDecimal[] bigDecimalArray3 = bigDecimalArray2[1].divideAndRemainder(NICKEL);
        int nickelNum = bigDecimalArray3[0].intValue();
        changeDue.put("Nickels: ", nickelNum);
        coinsQDNP[2] = nickelNum;

        BigDecimal[] bigDecimalArray4 = bigDecimalArray3[1].divideAndRemainder(PENNY);
        int pennyNum = bigDecimalArray4[0].intValue();
        changeDue.put("Pennies: ", pennyNum);
        coinsQDNP[3] = pennyNum;

//        System.out.println(String.format("\nChange Due: \nQuarters: %1$s \nDimes: %2$s \nNickels: %3$s \nPennies: %4$s\n",
//                quarterNum, dimeNum, nickelNum, pennyNum));
       for (Map.Entry coin : changeDue.entrySet()){
           if(changeDue.get(coin.getKey().toString()) != 0 ){
               System.out.println(coin.getKey().toString() + coin.getValue());
           }
       }
        logTransaction(getDate(), "GIVE CHANGE", adjustBalance(this.balance.negate()));
       return coinsQDNP;
    }


    //Overrides
    @Override
    public String toString(){
        String result ="";
        System.out.println("\nCurrent Balance:" + this.balance);
        List<String> locations = new ArrayList<>();
        locations.addAll(logToUse.records.keySet());
        locations.sort(null);
        for(String loc : locations){
            VendItem currentItem = this.logToUse.records.get(loc);
            String itemName = currentItem.getName();
            String itemPrice = currentItem.getPrice().toString();
            List<VendItem> stock = machineStock.get(loc);
            int stockLevel = stock.size();
            String outOfStock = stockLevel == 0?"OUT OF STOCK":"";
            result += String.format("%1$s | %2$s | $%3$s | %4$s | %5$s\n",
                    loc, itemName, itemPrice, stockLevel, outOfStock);
        }

        return result;
    }


}




