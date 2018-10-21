package fi.jussi.tf2.parsing;

import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.junit.Test;

public class FakeConnectorTest {
	
	private FakeConnector connector = new FakeConnector();
	

	@Test
	public void test() {

		Document doc = connector.getSiteAsDocument("adasadads");
		assertTrue(!doc.getAllElements().isEmpty());
		
	}

}
