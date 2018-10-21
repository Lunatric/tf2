package fi.jussi.tf2.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import fi.jussi.tf2.domain.Trade;

@Singleton
public class TradeService {
	
	private SessionFactory sessionFactory;
	
	@Inject
	public TradeService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Trade getTradeById(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Trade trade = session.get(Trade.class, id);
		session.getTransaction().commit();
		session.close();
		
		return trade;
	}

	public List<Trade> getAllTrades() {
		List<Trade> trades = new ArrayList<>();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query query = session.createQuery("From Trade");
		List tradesFromDb = query.list();
		for(Object o : tradesFromDb) {
			trades.add((Trade) o);
		}
		session.getTransaction().commit();
		session.close();
		return trades;
	}

	public void updateTrade(Trade tradeToEdit) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(tradeToEdit);
		session.getTransaction().commit();
		session.close();		
	}

	public void saveTrade(Trade tradeToEdit) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(tradeToEdit);
		session.getTransaction().commit();
		session.close();		
	}

	public void removeTradeById(int tradeToRemove) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Trade trade = session.get(Trade.class, tradeToRemove);
		session.remove(trade);
		session.getTransaction().commit();
		session.close();
	}
}






















