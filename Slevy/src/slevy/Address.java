package slevy;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Trieda Adress slúži na uloženie adresy.
 * 
 * Okrem atribútov, v ktorých sú uložené informácie o adrese, ako mesto, ulica, súradnice, atď. 
 * je tam pár ďalších atribútov. V atribúte nationalNameOfCity sú uložené prípadné anglické názvy miest.
 * @author martin
 * 
 */
class Address implements Serializable
{
    private static HashMap<String, String> nationalNameOfCity;
    String city, street, premise, streetNumber, postalCode;
    private float latitude, longitude;
    private boolean haveCoordinates;
    private String GPSStatus = "###";

    /**
     * 
     * @param address 
     */
    public Address(Address address)
    {
        this(address.city, address.street, address.premise, address.streetNumber, address.postalCode, address.latitude, address.longitude, address.haveCoordinates);
        GPSStatus = address.GPSStatus;
    }

    /**
     * 
     * @param paCity
     * @param paStreet
     * @param paPremise
     * @param paStreetNumber
     * @param paPostalCode
     * @param paLat
     * @param paLng
     * @param haveCoord 
     */
    public Address(String paCity, String paStreet,String paPremise, String paStreetNumber, String paPostalCode, float paLat, float paLng, boolean haveCoord)
    {
        nationalCityName();

        if (nationalNameOfCity.containsKey(paCity)) city = nationalNameOfCity.get(paCity);else city = paCity;
        if (paStreet.equals("") ||paStreet.equals(" ")) street = "###"; else street = paStreet;
        if (paPremise.equals("") ||paPremise.equals(" ")) premise = "###"; else premise = paPremise;
        if (paPostalCode.equals("") ||paPostalCode.equals(" ")) postalCode = "###"; else postalCode = paPostalCode;
        if (paStreetNumber.equals("") ||paStreetNumber.equals(" ")) streetNumber = "###"; else streetNumber = paStreetNumber;
        latitude = paLat;
        longitude = paLng;
        haveCoordinates = haveCoord;
    }
    /**
     * 
     * @param paCity
     * @param paStreet
     * @param paPremise
     * @param paStreetNumber
     * @param paPostalCode
     * @param paLat
     * @param paLng 
     */
    public Address(String paCity, String paStreet,String paPremise, String paStreetNumber, String paPostalCode, float paLat, float paLng)
    {
        this(paCity, paStreet, paPremise, paStreetNumber, paPostalCode, paLat, paLng, true);
    }

    /**
     * 
     * @param paCity 
     */
    public Address(String paCity)
    {
        this(paCity, "", "", "", "", 0, 0, false);
    }

    /**
     * 
     * @return vracia false, ak adresa nemá súradnice
     */
    public boolean haveCoordinates()
    {
        return haveCoordinates;
    }

    /**
     * Vracia názov mesta.
     * @return názov mesta
     */
    public String getCity()
    {
        return city.replace("###","");
    }

    /**
     *  Vracia názov ulice
     * @return názov ulce
     */
    public String getStreet()
    {
        return street.replace("###","");
    }

    public String getPremise()
    {
        return premise.replace("###","");
    }

     /**
     * Vracia číslo ulice
     * @return čislo ulce ako string
     */
    public String getStreetNumber()
    {
        return streetNumber.replace("###","");
    }

    /**
     * Vracia PSČ
     * @return PSČ string
     */
    public String getPostalCode()
    {
        return postalCode.replace("###","");
    }

    /**
     * Vracia zemepisnú širku
     * @return zemepisnú širku 
     */
    public float getLatitude()
    {
        return latitude;
    }

    /**
     * Vracia zemepisnú dĺžku
     * @return zemepisnú dľžku 
     */
    public float getLongitude()
    {
        return longitude;
    }

    /**
     * Metóda, ktorá vracia string s adresou, ktorá je vo viacerých riadkoch, bez súradníc
     * @return String
     */
    public String getAddressMultiLine()
    {
        String address = "", prem="", strNumber="", postCode="";
        if (!haveCoordinates) return city;
        else
        {
            if (!premise.equals("###")) 
            {
                 prem = premise;
                 if (!streetNumber.equals("###")) strNumber = "/";
            }
            if (!streetNumber.equals("###")) strNumber +=streetNumber;
            if (!postalCode.equals("###")) postCode = postalCode ;
            address += street+" "+prem+strNumber+"\n"+postCode+" "+city;
        }
        return address;
    }

    /**
     * Metóda, ktorá vracia string s adresou, ktorá je vo viacerých riadkoch, so súradnicami GPS
     * @return String
     */
     public String getAddressMultiLineWithGPS()
    {
        String address = "", prem="", strNumber="", postCode="";
        if (!haveCoordinates) return city;
        else
        {
            if (!premise.equals("###")) 
            {
                 prem = premise;
                 if (!streetNumber.equals("###")) strNumber = "/";
            }
            if (!streetNumber.equals("###")) strNumber +=streetNumber;
            if (!postalCode.equals("###")) postCode = postalCode ;
            address += getStreet()+" "+prem+strNumber+"\n"+postCode+" "+city+"\nlat: "+latitude+"\nlng: "+longitude;
        }
        return address;
    }

     /**
      * Metóda, ktorá vracia string s adresou v jednom riadku, bez GPS  
      * @return String
      */
    public String getAddressOneLine()
    {
        String address = "", prem="", strNumber="", postCode="";
        if (!haveCoordinates) return city;
        else
        {
            if (!premise.equals("###")) 
            {
                 prem = premise;
                 if (!streetNumber.equals("###")) strNumber = "/";
            }
            if (!streetNumber.equals("###")) strNumber +=streetNumber;
            if (!postalCode.equals("###")) postCode = postalCode ;
            address += getStreet()+" "+prem+strNumber+" "+postCode+" "+city;
        }
        return address;
    }

    /**
      * Metóda, ktorá vracia string s adresou v jednom riadku, s GPS  
      * @return String
      */
    public String getAddressOneLineWithGPS()
    {
        String address = "", prem="", strNumber="", postCode="";
        if (!haveCoordinates) return city;
        else
        {
            if (!premise.equals("###")) 
            {
                 prem = premise;
                 if (!streetNumber.equals("###")) strNumber = "/";
            }
            if (!streetNumber.equals("###")) strNumber +=streetNumber;
            if (!postalCode.equals("###")) postCode = postalCode ;
            address += getStreet()+" "+prem+strNumber+" "+postCode+" "+city+", lat: "+latitude+", lng: "+longitude;
        }
        return address;
    }

    /**
     *  Vytvára zoznam medzinárodných názvov miest.
     */
    private static void nationalCityName()
    {
        if (nationalNameOfCity == null)
        {
            nationalNameOfCity = new HashMap<>();
            nationalNameOfCity.put("Prague", "Praha");
        }
    }

    /**
     * Nastaví gps statatus
     * @param status 
     */
    public void setGPSStatus(String status)
    {
        GPSStatus = status;
    }

    /**
     * Vráti gps status
     * @return 
     */
    public String getGPSStatus()
    {
        return GPSStatus;
    }
}