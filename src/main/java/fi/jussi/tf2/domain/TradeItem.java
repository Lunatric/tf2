package fi.jussi.tf2.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fi.jussi.tf2.util.PriceUtil;

@Entity
@Table(name = "TRADE_ITEM")
public class TradeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private ItemType itemType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Item item;
	
	@ManyToOne
	private Trade trade;

	public TradeItem() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Trade getTrade() {
		return trade;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}	

	public String getSellString() {
		switch(itemType) {
			case UNIQUE:
				return item.getName() + " " + PriceUtil.weaponsToRef(item.getPriceUni());
			case COLLECTORS:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.weaponsToRef(item.getPriceCol());
			case GENUINE:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.weaponsToRef(item.getPriceGen());
			case HAUNTED:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.weaponsToRef(item.getPriceHau());
			case STRANGE:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.weaponsToRef(item.getPriceStr());
			case VINTAGE:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.weaponsToRef(item.getPriceVin());
			default:
				return "Something went wrong getting item info";
		}
	}
	
	public String getBuyString() {
		switch(itemType) {
			case UNIQUE:
				return item.getName() + " " + PriceUtil.weaponsToRef(item.getPriceUni());
			case COLLECTORS:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.reducedPrice(item.getPriceCol());
			case GENUINE:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.reducedPrice(item.getPriceGen());
			case HAUNTED:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.reducedPrice(item.getPriceHau());
			case STRANGE:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.reducedPrice(item.getPriceStr());
			case VINTAGE:
				return itemType.getDescription() + " " + item.getName() + " - " + PriceUtil.reducedPrice(item.getPriceVin());
			default:
				return "Something went wrong getting item info";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.getId());
		result = prime * result + ((trade == null) ? 0 : trade.getId());
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
		TradeItem other = (TradeItem) obj;
		if(item.getId() != other.item.getId() || trade.getId() != other.trade.getId()) {
			return false;
		}
		return true;
	}
	
	
	
}
