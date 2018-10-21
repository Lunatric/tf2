package fi.jussi.tf2.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FakeConnector implements Connector {

	@Override
	public Document getSiteAsDocument(String url) {
		return Jsoup.parse(getExampleSiteAsString());
	}

	private String getExampleSiteAsString() {
		File file = new File("test_files/examplesite.html");
		BufferedReader in = null;
		StringBuilder sb = new StringBuilder(2691481);

		try {
			in = new BufferedReader(new FileReader(file));

			in.lines().forEach(s -> sb.append(s + "\n"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(in != null) {
					in.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
