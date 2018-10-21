package fi.jussi.tf2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseUtils {

	private static SessionFactory sessionFactory = null;

	private DatabaseUtils() {

	}

	public static SessionFactory getSessionFactory() {
		if(sessionFactory != null) {
			return sessionFactory;
		}else {
			System.out.println("Starting hibernate...");
			sessionFactory = new Configuration().configure().buildSessionFactory();
			return sessionFactory;
		}
	}

	public static void closeSessionFactory() {
		if(sessionFactory != null) {
			sessionFactory.close();
		}		
	}
}
