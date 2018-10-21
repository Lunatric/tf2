package fi.jussi.tf2.util;

import java.util.concurrent.CountDownLatch;

public class DatabaseStarter implements Runnable {
	
	private CountDownLatch latch;
	
	public DatabaseStarter(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
			DatabaseUtils.getSessionFactory();
			latch.countDown();
	}

}
