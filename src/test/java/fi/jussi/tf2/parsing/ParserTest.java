package fi.jussi.tf2.parsing;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fi.jussi.tf2.domain.Item;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

	private Parser parser;
	
	@Mock
	private Connector mockConnector;
	
	@Before
	public void init() {
		parser = new Parser(mockConnector);
	}

	@Test
	public void test() {		
		Document exampleDoc = Jsoup.parse(getExampleSiteAsString());
		when(mockConnector.getSiteAsDocument(anyString())).thenReturn(exampleDoc);
		List<Item> items = parser.connectAndParseItems();
		assertTrue(items.size() > 2000 && items.size() < 3000);
	}
	
//	@Test
	public void test2() {		
		Document exampleDoc = Jsoup.parse(getExampleSiteAsString());
		when(mockConnector.getSiteAsDocument(anyString())).thenReturn(exampleDoc);
		for(Item item : parser.connectAndParseItems()) {
			System.out.println(item);
		}
	}

	private String getExampleSiteAsString() {
		File file = new File("test_files/examplesite.html");
		BufferedReader in = null;
		StringBuilder sb;
		
		try {
			in = new BufferedReader(new FileReader(file));
			sb = new StringBuilder(2691481);
			
			in.lines()
				.forEach(s -> sb.append(s + "\n"));
			
		}catch (Exception e) {
			throw new RuntimeException("asd");
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return sb.toString();			
	}
}
