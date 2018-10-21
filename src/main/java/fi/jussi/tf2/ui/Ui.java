package fi.jussi.tf2.ui;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import fi.jussi.tf2.domain.Item;
import fi.jussi.tf2.domain.ItemType;
import fi.jussi.tf2.domain.Trade;
import fi.jussi.tf2.parsing.Parser;
import fi.jussi.tf2.services.ItemService;
import fi.jussi.tf2.services.TradeService;
import fi.jussi.tf2.util.DatabaseUtils;

@Singleton
public class Ui {
	private List<Trade> trades;
	private Scanner scanner;
	private Parser parser;
	private TradeService tradeService;
	private ItemService itemService;

	@Inject
	public Ui(Parser parser, @Named("userinput") Scanner scanner, TradeService tradeService, ItemService itemService) {
		this.parser = parser;
		this.scanner = scanner;
		this.tradeService = tradeService;
		this.itemService = itemService;
		init();
	}

	public void start() {

		boolean keepGoing = true;

		while (keepGoing) {
			System.out.println("\nOptions:\n" + "1: Show trades\n" + "2: Edit trade\n" + "3: Add trade\n"
					+ "4: Remove trade\n" + "5: Quit");

			String input = getUserString();

			switch (input) {
			case "1":
				showTrades();
				break;
			case "2":
				editTrades();
				break;
			case "3":
				addTrade();
				break;
			case "4":
				removeTrade();
				break;
			case "5":
				DatabaseUtils.closeSessionFactory();
				System.exit(0);
			}

		}
	}

	private void init() {
		// Load all trades from databse
		getTrades();

		// Save originals
		trades.forEach(Trade::saveOriginal);

		// update item prices in db
		itemService.updateItems(parser.getItems());

		// update trades
		trades.forEach(t -> {
			Trade fromDb = tradeService.getTradeById(t.getId());
			t.updatePrices(fromDb);
		});

		showTrades();

		start();
	}

	private void showTrades() {

		if (trades.isEmpty()) {
			System.out.println("No active trades");
			return;
		}

		trades.forEach(t -> {
			System.out.println(t.getInfo());
		});
	}

	private void getTrades() {		
		trades = tradeService.getAllTrades();
	}

	private String getUserString() {
		String returnString = scanner.nextLine();
		System.out.println("--------");
		return returnString;
	}

	private void editTrades() {

		if (trades.isEmpty()) {
			System.out.println("No trades to edit");
			return;
		}

		StringBuilder sb = new StringBuilder();

		sb.append("Edit trade: (");
		for(int i = 0; i <= trades.size() -1; i++) {
			if(i != trades.size() -1) {
				sb.append(trades.get(i).getId() + ", ");
			}else {
				sb.append(trades.get(i).getId() + ")");
			}
		}

		System.out.println(sb.toString());

		int tradeToEdit;

		try {
			tradeToEdit = (Integer.parseInt(getUserString()));
		} catch (NumberFormatException e) {
			System.out.println("Not a number");
			return;
		}

		boolean tradeFound = false;

		for (Trade trade : trades) {
			if (trade.getId() == tradeToEdit) {
				tradeFound = true;
			}
		}

		if (!tradeFound) {
			System.out.println("Trade not found");
			return;
		} else {
			editTrade(tradeToEdit);
		}

	}

	private void editTrade(int tradeId) {
		Trade tradeToEdit = getTradeById(tradeId);
		System.out.println("Editing trade :");
		System.out.println(tradeToEdit.getInfo());

		boolean keepGoing = true;

		while (keepGoing) {
			System.out.println("Editing trade " + tradeId + "\nOptions:\n" + "1: Add item\n"
					+ "2: Remove item\n" + "3: Change buy URL\n" + "4: Change sell URL\n"
					+ "5: Update prices on Outpost\n" + "6: Back");

			String input = getUserString();

			switch (input) {
			case "1":
				addItem(tradeToEdit);
				break;
			case "2":
				removeItem(tradeToEdit);
				break;
			case "3":
				changeBuyUrl(tradeToEdit);
				break;
			case "4":
				changeSellUrl(tradeToEdit);
				break;
			case "5":
				updateTradeOnSite(tradeToEdit);
				break;
			case "6":
				keepGoing = false;
				break;
			}
		}

	}

	private Trade getTradeById(int id) {
		for (Trade trade : trades) {
			if (trade.getId() == id) {
				return trade;
			}
		}
		return null;
	}
	
	private void addItem(Trade tradeToEdit) {

		if (tradeToEdit.isFull()) {
			System.out.println("Trade is full");
			return;
		}

		System.out.println("Search for items:");

		String search = getUserString();

		itemService.searchForItems(search);

		System.out.println("Item ID: ");

		int itemID = 0;

		try {
			itemID = Integer.parseInt(getUserString());
		} catch (NumberFormatException e) {
			System.out.println("Not a number\n");
			return;
		}

		if (!itemService.isValidItemID(itemID)) {
			System.out.println("Not a valid item ID\n");
			return;
		}

		System.out.println("Type (gen, vin, uni, str, hau, col) :");
		String type = getUserString();

		if (!isValidType(type)) {
			System.out.println("Not a valid type");
			return;
		}

		Item itemToAdd = itemService.getItemById(itemID);

		ItemType itemType;
		
		
		switch (type) {
			case "gen":
				itemType = ItemType.GENUINE;
				break;
			case "vin":
				itemType = ItemType.VINTAGE;
				break;
			case "uni":
				itemType = ItemType.UNIQUE;
				break;
			case "str":
				itemType = ItemType.STRANGE;
				break;
			case "col":
				itemType = ItemType.COLLECTORS;
				break;
			case "hau":
				itemType = ItemType.HAUNTED;
				break;
			default:
				itemType = ItemType.UNIQUE;
				break;
		}

		tradeToEdit.addItem(itemToAdd, itemType);
		
		tradeService.updateTrade(tradeToEdit);

		tradeToEdit.updatePrices(tradeService.getTradeById(tradeToEdit.getId()));

	}

	private boolean isValidType(String type) {
		String[] validTypes = { "gen", "vin", "uni", "str", "hau", "col" };

		for (int i = 0; i < validTypes.length; i++) {
			if (type.equals(validTypes[i])) {
				return true;
			}
		}

		return false;
	}

	private void removeItem(Trade tradeToEdit) {
		System.out.println("Item to remove");

		String itemToRemove = getUserString();
		
		if(itemToRemove.length() < 3) {
			System.out.println("Give at least 4 characters");
			return;
		}

		tradeToEdit.removeItem(itemToRemove);
		
		tradeService.updateTrade(tradeToEdit);
		
		tradeToEdit.updatePrices(tradeService.getTradeById(tradeToEdit.getId()));
		
	}

	private void changeBuyUrl(Trade tradeToEdit) {
		System.out.println("Give new url");

		String newUrl = getUserString();

		tradeToEdit.setBuyUrl(newUrl);
		
		tradeService.updateTrade(tradeToEdit);		
	}

	private void changeSellUrl(Trade tradeToEdit) {
		System.out.println("Give new url");

		String newUrl = getUserString();

		tradeToEdit.setBuyUrl(newUrl);
		
		tradeService.updateTrade(tradeToEdit);
	}

	private void addTrade() {

		System.out.println("Adding new trade\n" + "Give new Buy URL:");

		String buyUrl = getUserString();

		System.out.println("Give new Sell URL:");

		String sellUrl = getUserString();

		Trade newTrade = new Trade();
		newTrade.setBuyUrl(buyUrl);
		newTrade.setSellUrl(sellUrl);
		
		tradeService.saveTrade(newTrade);

		System.out.println("Added new trade");
		getTrades();
	}

	private void removeTrade() {
		
		if(trades.isEmpty()) {
			System.out.println("No trades to remove");
			return;
		}			
		
		System.out.println("Trade to remove: ");
		int tradeToRemove = 0;

		try {
			tradeToRemove = Integer.parseInt(getUserString());
		} catch (NumberFormatException e) {
			System.out.println("Not a number");
			return;
		}
		
		if(tradeService.getTradeById(tradeToRemove) == null) {
			System.out.println("Not a valid trade id");
			return;
		}
		
		tradeService.removeTradeById(tradeToRemove);

		System.out.println("Succesfully removed trade");
		getTrades();
	}

	private void updateTradeOnSite(Trade trade) {
		
		System.out.println("Press any key to update sell on site");
		getUserString();
		copyTextToClipBoard(trade.getSellString());
		openURL(trade.getSellUrl());
		
		System.out.println("Press any key to update buy on site");
		getUserString();
		copyTextToClipBoard(trade.getBuyString());
		openURL(trade.getBuyUrl());
		
		trade.setNeedsUpdating(false);
	}

	private void openURL(String URL) {

		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(URL));
			}
		} catch (Exception e) {
			System.out.println("Problem with Ui.openURL");
			System.out.println("URL: " + URL);
			System.out.println(e.getClass() + ": " + e.getMessage());
		}

	}

	private void copyTextToClipBoard(String stringToClipboard) {
		StringSelection selection = new StringSelection(stringToClipboard);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
}
