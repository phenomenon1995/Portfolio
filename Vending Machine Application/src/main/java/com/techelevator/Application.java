package com.techelevator;

import com.techelevator.util.InvalidBalanceException;

import java.math.BigDecimal;
import java.util.*;

public class Application {
	private static final String MAIN_MENU_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_PURCHASE_ITEMS = "Purchase";
	private static final String MAIN_MENU_EXIT = "Exit";
	private static final String MAIN_MENU_GENERATE_SALES_REPORT = "!!Hidden Generation Menu";

	private static final String DISPLAY_MENU_RETURN_TO_MAIN = "Return to Main Screen";

	private static final String FEED_MONEY_DONE_ADDING_MONEY = "\nEnter whole dollar number amount to add to balance." +
			"\nEnter any Letter to finish adding Funds.";

	private static final String PURCHASE_MENU_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";

	private static final String [] MAIN_MENU_OPTIONS = {MAIN_MENU_DISPLAY_ITEMS, MAIN_MENU_PURCHASE_ITEMS, MAIN_MENU_EXIT,
			MAIN_MENU_GENERATE_SALES_REPORT};
	private static final String [] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_FEED_MONEY, PURCHASE_MENU_SELECT_PRODUCT,
			PURCHASE_MENU_FINISH_TRANSACTION};

	private static final String[] DISPLAY_MENUS_OPTIONS = {DISPLAY_MENU_RETURN_TO_MAIN};
	private static final String[] FEED_MONEY_MENUS_OPTIONS = {};
	private static final String[] SELECT_PRODUCT_MENUS_OPTIONS = {};


	private String [] activeMenu = MAIN_MENU_OPTIONS;
	private boolean running = false;

	public static void main(String[] args) {
		Application app = new Application();
		app.run();
	}

	private void run(){
		Scanner userInput = new Scanner(System.in);
		this.running = true;
		VendingMachine vm = new VendingMachine();
		while(running){
				System.out.println("\nCurrent Balance: ".toUpperCase() + vm.getBalance());
				System.out.println("MENU OPTIONS");
				displayMenu();
				System.out.print("Please make a selection: ".toUpperCase());
				int userEntry = 0;
				try {
					userEntry = Integer.parseInt(userInput.nextLine());
				} catch (NumberFormatException nfex) {
					userEntry = 0;
				}
				String choice = "";
				try {
					choice = activeMenu[userEntry - 1];
				} catch (ArrayIndexOutOfBoundsException ex) {
					System.out.println("\nInvalid selection, make another selection".toUpperCase());
					continue;
				}
				switch (choice) {
					case MAIN_MENU_DISPLAY_ITEMS:
						System.out.println(vm);
						break;
					case MAIN_MENU_PURCHASE_ITEMS:
						activeMenu = PURCHASE_MENU_OPTIONS;
						break;
					case MAIN_MENU_EXIT:
						this.running = false;
						break;
					case MAIN_MENU_GENERATE_SALES_REPORT:
						vm.generateSalesReport();
						break;
					case PURCHASE_MENU_FEED_MONEY:
						while (true) {
							System.out.println("Current Balance: " + vm.getBalance());
							System.out.println(FEED_MONEY_DONE_ADDING_MONEY);
							try {
								int amountOfMoney = Integer.parseInt(userInput.nextLine());
								vm.feedMoney(new BigDecimal(amountOfMoney));
							} catch (NumberFormatException numEx) {
								System.out.println("Not a valid number");
								break;
							} catch (RuntimeException runEx) {
								System.out.println("Must be greater than zero.");
								break;
							}
						}
						break;
					case DISPLAY_MENU_RETURN_TO_MAIN:
						activeMenu = MAIN_MENU_OPTIONS;
						break;
					case PURCHASE_MENU_SELECT_PRODUCT:
						System.out.println(vm);
						System.out.print("Please make a selection: ".toUpperCase());
						try {
							String itemChoice = userInput.nextLine().toUpperCase();
							VendItem dispensedItem = vm.getItem(itemChoice);
							System.out.println(dispensedItem.getMessage());
						} catch (NullPointerException e) {
							System.out.println("\nInvalid selection, make another selection".toUpperCase());
						} catch (IndexOutOfBoundsException e) {
							System.out.println("\nItem Out of Stock, make another selection".toUpperCase());
						} catch (InvalidBalanceException e) {
							System.out.println(e.getMessage());
						}
						break;
					case PURCHASE_MENU_FINISH_TRANSACTION:
						vm.giveChange();
						activeMenu = MAIN_MENU_OPTIONS;
						break;
					default:
						System.out.println("\nNo Choice Made".toUpperCase());
				}

		}
	}

	private void displayMenu() {

		for (int i = 0; i < activeMenu.length; i++) {

			String menuOptionNumber = (i + 1) + ") ";

			if (!activeMenu[i].startsWith("!!")) {
				System.out.println(menuOptionNumber + activeMenu[i]);
			}

		}
	}
	}

