package fi.jussi.tf2.domain;

public enum ItemType {	
	GENUINE("Genuine"),
	VINTAGE("Vintage"),
	UNIQUE(""),
	STRANGE("Strange"),
	HAUNTED("Haunted"),
	COLLECTORS("Collector's");
	
	private final String description;
	
	private ItemType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}

