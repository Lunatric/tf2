package fi.jussi.tf2.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PriceUtilTest {

	@Test
	public void testRefToWeapons() {
		assertEquals(0, PriceUtil.refToWeapons("0"));
		assertEquals(0, PriceUtil.refToWeapons(null));
		assertEquals(1, PriceUtil.refToWeapons("0.05"));
		assertEquals(2, PriceUtil.refToWeapons("0.11"));
		assertEquals(4, PriceUtil.refToWeapons("0.22"));
		assertEquals(6, PriceUtil.refToWeapons("0.33"));
		assertEquals(8, PriceUtil.refToWeapons("0.44"));
		assertEquals(10, PriceUtil.refToWeapons("0.55"));
		assertEquals(12, PriceUtil.refToWeapons("0.66"));
		assertEquals(14, PriceUtil.refToWeapons("0.77"));
		assertEquals(16, PriceUtil.refToWeapons("0.88"));
		assertEquals(18, PriceUtil.refToWeapons("1"));
		assertEquals(20, PriceUtil.refToWeapons("1.11"));
		assertEquals(20, PriceUtil.refToWeapons("1.12"));		
	}
	
	@Test
	public void testTwoWay() {
		int weapons = PriceUtil.refToWeapons("2.15");
		assertEquals("2.11 ref", PriceUtil.weaponsToRef(weapons));		
	}

	
}
