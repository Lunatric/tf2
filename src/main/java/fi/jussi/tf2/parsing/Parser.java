package fi.jussi.tf2.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fi.jussi.tf2.domain.Item;
import fi.jussi.tf2.util.PriceUtil;

@Singleton
public class Parser implements Runnable {

	private Connector connector;

	private CountDownLatch latch;

	private final String siteURL = "https://backpack.tf/spreadsheet";

	private List<Item> parsedItems = new ArrayList<>(2700);

	@Inject
	public Parser(@Named("real")Connector connector) {
		this.connector = connector;
	}

	/**
	 * Connects to the site and returns items.
	 * 
	 * @return List of items
	 */
	public List<Item> connectAndParseItems() {

		Element table = connector.getSiteAsDocument(siteURL).select("table").get(0);
		table = table.select("tbody").get(0);
		Elements rows = table.select("tr");

		if (rows.isEmpty()) {
			return null;
		}

		List<Item> returnList = new ArrayList<>();

		for (Element row : rows) {
			String name = null;
			int priceGen = 0, priceVin = 0, priceUni = 0, priceStr = 0, priceHau = 0, priceCol = 0;

			Elements tds = row.select("td");

			for (int i = 0; i < tds.size(); i++) {
				switch (i) {
				case 0:
					name = getItemName(tds.get(i).toString());
					break;
				case 1:
					break;
				case 2:
					priceGen = PriceUtil.refToWeapons(getPrice(tds.get(i).toString()));
					break;
				case 3:
					priceVin = PriceUtil.refToWeapons(getPrice(tds.get(i).toString()));
					break;
				case 4:
					priceUni = PriceUtil.refToWeapons(getPrice(tds.get(i).toString()));
					break;
				case 5:
					priceStr = PriceUtil.refToWeapons(getPrice(tds.get(i).toString()));
					break;
				case 6:
					priceHau = PriceUtil.refToWeapons(getPrice(tds.get(i).toString()));
					break;
				case 7:
					priceCol = PriceUtil.refToWeapons(getPrice(tds.get(i).toString()));
					break;
				}
			}

			returnList.add(new Item(name, priceGen, priceVin, priceUni, priceStr, priceHau, priceCol));
		}

		return returnList;
	}

	private String getItemName(String td) {
		StringBuilder returnString = new StringBuilder();

		Pattern p = Pattern.compile("<td>(.*?)<(?:.*(\\(.*\\)))?");
		Matcher m = p.matcher(td);

		if (m.find()) {
			returnString.append(m.group(1));

			if (m.group(2) != null) {
				returnString.append(m.group(2));
			}
		}

		return returnString.toString();
	}

	private String getPrice(String td) {

		Pattern p = Pattern.compile("(?s)abbr=\"(.*?)\"(?:.*?title=.*?>.*?([^\\s].*?)(?:–.*?)? ref)?");
		Matcher m = p.matcher(td);

		if (m.find()) {
			if (m.group(2) != null) {
				return m.group(2);
			}
			return m.group(1);
		}

		return "0";
	}

	/**
	 * Returns already parsed list of Items. Does not connect to the site.
	 * 
	 * @return List of items
	 */
	public List<Item> getItems() {
		return parsedItems;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		parsedItems = connectAndParseItems();
		latch.countDown();
	}

}
