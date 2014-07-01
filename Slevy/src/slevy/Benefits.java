package slevy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import slevy.Benefit.BenefitAddress;

/**
 * Trieda, ktorá zľučuje všetky zľavy
 *
 * @author martin
 */
class Benefits implements Serializable {

    private final List<Benefit> benefits;
    private final HashMap<String, HashMap<String, HashMap<String, BenefitAddress>>> addresses;

    Benefits() {
        benefits = new ArrayList<>();
        addresses = new HashMap<>();
    }

    /**
     * Prida benefit, nepridava benefity, ktore nemaju suradnice
     *
     * @param benefit
     * @return benefit, ktory sa nepodarilo pridat z nejakeho dôvodu, napr. nema
     * suradnice
     */
    public Benefit addBenefit(Benefit benefit) {
        if (benefit.getAddress().getGPSStatus().equals("OK")) {
            addBenefitWithAddress(benefit);
            return null;
        } else if (benefit.getAddress().getGPSStatus().equals("###")) {
            if (addBenefitWithOutAddress(benefit)) {
                return null;
            } else {
                benefit.changeAddress(GPS.getAddressWithCoordinates(benefit.getAddress().city));
                return addBenefit(benefit);
            }
        } else {
            return benefit;
        }
    }

    public void addBenefit(List<Benefit> benefitList) {
        for (Benefit benefit : benefitList) {
            addBenefit(benefit);
        }
    }

    /**
     * Pokusi sa pridat benefit, ktory ešte nema potvrdenu adresu
     *
     * @param benefit
     * @return false, ak sa nepodarilo pridať adresu
     */
    private boolean addBenefitWithOutAddress(Benefit benefit) {
        String s = UsefullTools.removeDiak(benefit.getAddress().city);
        s = s.toLowerCase();
        for (String keyCity : addresses.keySet()) {
            if (s.contains(keyCity)) {
                s = s.replace(keyCity, "");
                for (String keyStreet : addresses.get(keyCity).keySet()) {
                    if (s.contains(keyStreet)) {
                        s = s.replace(keyStreet, "");
                        Benefit tmp = Benefits.this.searchTheSameBenefit(benefit, addresses.get(keyCity).get(keyStreet));
                        if (tmp != null) {
                            if (tmp.getName().length() < benefit.getName().length()) {
                                tmp.setName(benefit.getName());
                            }
                            if (tmp.getPhone().length() < benefit.getPhone().length()) {
                                tmp.setPhone(benefit.getPhone());
                            }
                            tmp.addCard(benefit.getCard());
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Hľadá, zhodu predajcu na danej ulici
     *
     * @param benefit, pri ktorom chceme zistiť, či je na danej ulici
     * @param streets, ulica, ktorej benefity sledujeme
     * @return benefit, ktorý sa tam už nachádza
     */
    private Benefit searchTheSameBenefit(Benefit benefit, HashMap<String, BenefitAddress> streets) {
        for (String number : streets.keySet()) {
            Benefit tmp = searchTheSameBenefit(benefit, streets.get(number));
            if (tmp != null) {
                return tmp;
            }
        }
        return null;
    }

    private Benefit searchTheSameBenefit(Benefit benefit, BenefitAddress address) {
        byte c = 1;
        Benefit tmp = null;
        for (Benefit b : address.benefitOnThisAddress) {
            byte d = benefit.compareTo(b);
            if (c < d) {
                c = d;
                tmp = b;
            }
        }

        if (tmp != null) {
            return tmp;
        }
        return null;
    }
/** Pridáva benefit, ktorý má potvrdenú adresu, so súradnicami 
 *
 * @param ben, benefit, ktorý chceme pridať 
 */
    private void addBenefitWithAddress(Benefit ben) {
        BenefitAddress bAdr = ben.getAddress();
        String keyCity = "", keyStreet = "", premiseS = "", streetNS = "", keyFinal = "";
        keyCity = UsefullTools.removeDiak(bAdr.getCityB()).toLowerCase();
        keyStreet = UsefullTools.removeDiak(bAdr.getStreetB()).toLowerCase();
        premiseS = UsefullTools.removeDiak(bAdr.getPremiseB()).toLowerCase();
        streetNS = UsefullTools.removeDiak(bAdr.getStreetNumberB()).toLowerCase();
        keyFinal = premiseS + "/" + streetNS;

        if (addresses.containsKey(keyCity)) {
            if (addresses.get(keyCity).containsKey(keyStreet)) {
                if (addresses.get(keyCity).get(keyStreet).containsKey(keyFinal)) {
                    Benefit tmp = searchTheSameBenefit(ben, addresses.get(keyCity).get(keyStreet).get(keyFinal));
                    if (tmp == null) {
                        ben.changeAddress(addresses.get(keyCity).get(keyStreet).get(keyFinal));
                        ben.getAddress().addBenefitOnThisAddress(ben);
                        benefits.add(ben);
                    } else {
                        if (tmp.getName().length() < ben.getName().length()) {
                            tmp.setName(ben.getName());
                        }
                        if (tmp.getPhone().length() < ben.getPhone().length()) {
                            tmp.setPhone(ben.getPhone());
                        }

                        tmp.addCard(ben.getCard());
                    }
                } else {
                    addresses.get(keyCity).get(keyStreet).put(keyFinal, bAdr);
                    addresses.get(keyCity).get(keyStreet).get(keyFinal).addBenefitOnThisAddress(ben);
                    benefits.add(ben);
                }
            } else {
                addresses.get(keyCity).put(keyStreet, new HashMap<String, BenefitAddress>());
                addresses.get(keyCity).get(keyStreet).put(keyFinal, bAdr);
                addresses.get(keyCity).get(keyStreet).get(keyFinal).addBenefitOnThisAddress(ben);
                benefits.add(ben);
            }
        } else {
            addresses.put(keyCity, new HashMap<String, HashMap<String, BenefitAddress>>());
            addresses.get(keyCity).put(keyStreet, new HashMap<String, BenefitAddress>());
            addresses.get(keyCity).get(keyStreet).put(keyFinal, bAdr);
            addresses.get(keyCity).get(keyStreet).get(keyFinal).addBenefitOnThisAddress(ben);
            benefits.add(ben);
        }

    }

    public Benefit getBenefit(int index) {
        if (index < 0 || benefits.size() < (index - 1)) {
            return null;
        } else {
            return benefits.get(index);
        }
    }

    public List<Benefit> getBenefits() {
        return benefits;
    }

    public int numbersBenefits() {
        return benefits.size();
    }

    public List<Benefit> getPlaces(float lat3, float lon3) {
        List<Benefit> places = new ArrayList<>();
        float lat1, lat2, lon1, lon2;
        lon1 = lon3 + (20f / (40076f / 180f));
        lat1 = lat3 + (16f / (40076f / 360f));
        lon2 = lon3 - (20f / (40076f / 180f));
        lat2 = lat3 - (16f / (40076f / 360f));

        for (Benefit benefit : benefits) {
            if (lat1 > benefit.getLatitude() && lat2 < benefit.getLatitude() && lon2 < benefit.getLongitude() && lon1 > benefit.getLongitude()) {
                places.add(benefit);
                System.out.println(benefit.getFullInfo());
            }
        }
        return places;
    }
}
