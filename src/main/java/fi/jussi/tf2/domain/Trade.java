package fi.jussi.tf2.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
//import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TRADE")
public class Trade {

	@Transient
	private static final int MAX_ITEMS = 7;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String buyUrl, sellUrl;
	
	@Transient
	private String originalSellString = "";
	
	@Transient
	private boolean needsUpdating = false;

	@OneToMany(
			cascade = CascadeType.ALL,
			mappedBy = "trade",
			fetch = FetchType.EAGER,
			orphanRemoval= true
	)
	private List<TradeItem> items = new ArrayList<>(MAX_ITEMS);

	public Trade() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String url) {
		this.buyUrl = url;
	}	

	public String getSellUrl() {
		return sellUrl;
	}

	public void setSellUrl(String sellUrl) {
		this.sellUrl = sellUrl;
	}

	public void addItem(Item item, ItemType itemType) {
		if(items.size() >= MAX_ITEMS) {
			System.out.println("Too many items");
		}else {
			TradeItem tradeItem = new TradeItem();
			tradeItem.setItem(item);
			tradeItem.setItemType(itemType);
			tradeItem.setTrade(this);
			items.add(tradeItem);
			System.out.println("Item added to trade\n");
		}
	}
	
	public String getSellString() {
		
		StringBuilder sb = new StringBuilder();
		for(TradeItem tradeItem : items) {
			sb.append(tradeItem.getSellString() + "\n");
		}
		return sb.toString();
	}
	
	public String getBuyString() {
		
		StringBuilder sb = new StringBuilder();
		for(TradeItem tradeItem : items) {
			sb.append(tradeItem.getBuyString() + "\n");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buyUrl == null) ? 0 : buyUrl.hashCode());
		result = prime * result + ((sellUrl == null) ? 0 : sellUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trade other = (Trade) obj;
		if(buyUrl != other.buyUrl || sellUrl != other.sellUrl) {
			return false;
		}
		return true;
	}

	public void saveOriginal() {
		originalSellString = getSellString();
	}
	
	public void updatePrices(Trade tradeFromDb) {
		items = tradeFromDb.items;
		
		if(!originalSellString.equals(getSellString())) {
			needsUpdating = true;
		}
		
		originalSellString = getSellString();
	}
	
	public boolean needsUpdating() {
		return needsUpdating;
	}

	public String getInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Trade " + id);
		if(needsUpdating) {
			sb.append(" needs updating");
		}
		sb.append("\n");
		sb.append(getSellString());
		sb.append("Sell Url: " + sellUrl + "\n");
		sb.append("Buy Url " + buyUrl + "\n");
		return sb.toString();
	}

	public boolean isFull() {
		return items.size() == MAX_ITEMS;
	}

	
	public void removeItem(String itemToRemove) {
		
		boolean itemRemoved = false;		
		Iterator<TradeItem> iterator = items.iterator();
		
		while(iterator.hasNext()) {
			TradeItem tradeItem = iterator.next();
			if(tradeItem.getItem().getName().toLowerCase().startsWith(itemToRemove.toLowerCase())) {
				System.out.println("Removed " + tradeItem.getItem().getName());
				iterator.remove();
				itemRemoved = true;
			}
		}
		
		if(itemRemoved)
			System.out.println("Succesfully removed item.");		
		
	}

	public void setNeedsUpdating(boolean b) {
		needsUpdating = b;		
	}
	
}
