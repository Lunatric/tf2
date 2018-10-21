package fi.jussi.tf2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import fi.jussi.tf2.util.PriceUtil;

@Entity
@Table(name = "ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String name;
	
	private int priceGen, priceVin, priceUni, priceStr, priceHau, priceCol;
	
	public Item() {
		
	}
	
	public Item(String name, int priceGen, int priceVin, int priceUni, int priceStr, int priceHau, int priceCol) {
		this.name = name;
		this.priceGen = priceGen;
		this.priceVin = priceVin;
		this.priceUni = priceUni;
		this.priceStr = priceStr;
		this.priceHau = priceHau;
		this.priceCol = priceCol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPriceGen() {
		return priceGen;
	}

	public void setPriceGen(int priceGen) {
		this.priceGen = priceGen;
	}

	public int getPriceVin() {
		return priceVin;
	}

	public void setPriceVin(int priceVin) {
		this.priceVin = priceVin;
	}

	public int getPriceUni() {
		return priceUni;
	}

	public void setPriceUni(int priceUni) {
		this.priceUni = priceUni;
	}

	public int getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(int priceStr) {
		this.priceStr = priceStr;
	}

	public int getPriceHau() {
		return priceHau;
	}

	public void setPriceHau(int priceHau) {
		this.priceHau = priceHau;
	}

	public int getPriceCol() {
		return priceCol;
	}

	public void setPriceCol(int priceCol) {
		this.priceCol = priceCol;
	}
	
	

	@Override
	public String toString() {
		return "Item [id= " + id + ", name= " + name + ", priceGen= " + PriceUtil.weaponsToRef(priceGen) + ", priceVin= " + PriceUtil.weaponsToRef(priceVin)
		+ ", priceUni= " + PriceUtil.weaponsToRef(priceUni) + " , priceStr= " + PriceUtil.weaponsToRef(priceStr) + " , priceHau= " + PriceUtil.weaponsToRef(priceHau)
		+ ", priceCol= " + PriceUtil.weaponsToRef(priceCol) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}