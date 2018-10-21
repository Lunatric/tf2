package fi.jussi.tf2.parsing;

import org.jsoup.nodes.Document;

public interface Connector {

	public Document getSiteAsDocument(String url);
	
}
