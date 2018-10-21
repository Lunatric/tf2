// 1 scrap = 2 weapons
// 1 rec = 6 weapons
// 1 ref = 18 weapons
package fi.jussi.tf2.util;

public class PriceUtil {
	
	private PriceUtil() {
		
	}

    /**
     * Returns
     *
     * @param string The price of the item in string format.
     * @return How many scrap metal the item costs
     */
	public static int refToWeapons(String string) {

        if (string == null) {
            return 0;
        }

        if (string.equals("0")) {
            return 0;
        }

        if (string.equals("0.05")) {
            return 1;
        }

        int priceInScrap = 0;

        String[] parts = string.split("\\.");

        try{
            priceInScrap += Integer.parseInt(parts[0]) * 9;
        }catch(Exception e){
        }
        

        try {
            if (parts[1].equals("5")) {
                parts[1] = "44";
            }
            priceInScrap += Integer.parseInt(parts[1]) / 11;

        } catch (Exception e) {
            
        }

        return priceInScrap * 2;
    }

    /**
     * @param weapons
     * @return
     */
    public static String weaponsToRef(int weapons) {

        if (weapons == 0) {
            return "0 ref";
        }

        if (weapons == 1) {
            return "0.05 ref";
        }

        StringBuilder returnValue = new StringBuilder();

        if (weapons >= 18) {
            returnValue.append(Integer.toString(weapons / 18));
        }

        if (weapons % 18 == 0) {
            return returnValue.toString();
        }

        if (weapons < 18) {
            returnValue.append("0");
        }

        returnValue.append(".");

        returnValue.append(Integer.toString(weapons % 18 * 55 / 10));
        
        returnValue.append(" ref");

        return returnValue.toString();
    }

    public static String reducedPrice(int weapons) {

        //jos hinta on < 1 ref niin hinta ei laske
        if (weapons < 18) {
            return weaponsToRef(weapons);
        }

        //jos hinta on >= 3 ref niin hinta laskee 2 rec
        if (weapons >= 18 * 3) {
            weapons -= 12;
            return weaponsToRef(weapons);
        }

        //muulloin hinta laskee 1 rec
        weapons -= 6;
        return weaponsToRef(weapons);
    }

}