package fi.jussi.tf2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import fi.jussi.tf2.di.AppComponent;
import fi.jussi.tf2.di.DaggerAppComponent;
import fi.jussi.tf2.parsing.Parser;
import fi.jussi.tf2.util.DatabaseStarter;
import fi.jussi.tf2.util.DatabaseUtils;

public class App 
{
    public static void main( String[] args )
    {
    	java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
    	
    	AppComponent component = DaggerAppComponent.create();    	
        CountDownLatch latch = new CountDownLatch(2);
        
        DatabaseStarter databaseStarter = new DatabaseStarter(latch);
        Thread dbThread = new Thread(databaseStarter);
        
        Parser parser = component.provideParser();
        parser.setLatch(latch);
        Thread parserThread = new Thread(parser);
        
        dbThread.start();
        parserThread.start();   
        
        try {
			if(!latch.await(30, TimeUnit.SECONDS)){
				System.out.println("Could not load database or parse items.");
	        	DatabaseUtils.closeSessionFactory();
	        	System.exit(1);
			}
		} catch (InterruptedException e) {}
        
        component.provideUi().start();
    }
}
