package fi.jussi.tf2.parsing;

import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RealConnector implements Connector{
	
	public RealConnector() {
		
	}

	public Document getSiteAsDocument(String url) {

		Document doc = null;

		System.out.println("Connecting to " + url);
		
			try {
				doc = Jsoup.connect(url)
						.maxBodySize(0)
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101   Firefox/45.0")
						.referrer("http://www.google.com")
						.get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to connect.");
				e.printStackTrace();
				System.out.println("Press any key to exit...");
				new Scanner(System.in).nextLine();
			}
			
		
		
		return doc;

	}
}
