package fi.jussi.tf2.di;

import java.util.Scanner;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.SessionFactory;

import dagger.Module;
import dagger.Provides;
import fi.jussi.tf2.parsing.Connector;
import fi.jussi.tf2.parsing.FakeConnector;
import fi.jussi.tf2.parsing.RealConnector;
import fi.jussi.tf2.util.DatabaseUtils;

@Module
public class AppModule {

	@Provides
	@Named("userinput")
	@Singleton
	public Scanner scanner() {
		return new Scanner(System.in);
	}
	
	@Singleton
	@Provides
	public SessionFactory sessionFactory() {
		return DatabaseUtils.getSessionFactory();
	}
	
	@Singleton
	@Provides
	@Named("real")
	public Connector realConnector() {
		return new RealConnector();
	}
	
	@Singleton
	@Provides
	@Named("fake")
	public Connector fakeConnector() {
		return new FakeConnector();
	}
}
