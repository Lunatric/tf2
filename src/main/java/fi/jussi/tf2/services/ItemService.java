package fi.jussi.tf2.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import fi.jussi.tf2.domain.Item;

@Singleton
public class ItemService {

	private SessionFactory sessionFactory;

	@Inject
	public ItemService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void updateItems(List<Item> items) {
		System.out.println("Updating item info...\n");
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		for (Item item : items) {
			Query query = session.createQuery("From Item where name = :name");
			query.setParameter("name", item.getName());
			List itemsFromDb = query.list();
			if (itemsFromDb.isEmpty()) {
				session.save(item);
			} else {
				Item itemFromDb = (Item) itemsFromDb.get(0);
				itemFromDb.setPriceCol(item.getPriceCol());
				itemFromDb.setPriceUni(item.getPriceUni());
				itemFromDb.setPriceVin(item.getPriceVin());
				itemFromDb.setPriceStr(item.getPriceStr());
				itemFromDb.setPriceHau(item.getPriceHau());
				itemFromDb.setPriceGen(item.getPriceGen());
			}
		}

		session.getTransaction().commit();
		session.close();

	}

	public void searchForItems(String search) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createQuery("From Item i where lower(i.name) like lower(:name)");
		query.setParameter("name", "%" + search + "%");
		query.list().forEach(dbItem -> {
			Item itemFromDb = (Item) dbItem;
			System.out.println(itemFromDb);
		});
		
		session.getTransaction().commit();
		session.close();

	}

	public boolean isValidItemID(int itemID) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Item itemFromDb = session.get(Item.class, itemID);
		
		session.getTransaction().commit();
		session.close();
		
		if(itemFromDb == null ) {
			return false;
		}else {
			return true;
		}
	}

	public Item getItemById(int itemID) {
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Item itemFromDb = session.get(Item.class, itemID);
		
		session.getTransaction().commit();
		session.close();
		
		return itemFromDb;
	}

}
