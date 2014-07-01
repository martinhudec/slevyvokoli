package slevy;

import org.w3c.dom.Document;

/**
 * Trieda, ktorá slúži na priradenie GPS k danej adrese, resp. overenie adresy 
 * @author martin
 */
public class GPS {
    

    /**
     *
     * @param paAddress
     * @return
     */
    public Address getCoordinates(String paAddress)
    {
        String  url;
        String key = "AIzaSyCR0s8KmcyMmItLlFVKDEEMYrIKi-_EVYo";
        url = "https://maps.googleapis.com/maps/api/geocode/xml?address="+paAddress.replace(' ', '+')+"&key=" + key;
        return getCoordinates(paAddress,url);
    }
    
    /**
     *
     * @param paAddress
     * @param url
     * @return
     */
    public Address getCoordinates(String paAddress,String url)
    {
        Address address;
        String status, city ="", street ="", premise ="", streetNumber = "", postalCode="", latitude="", longitude="";
        
        Document geo = UsefullTools.loadXMLFromURI(url);
        status = UsefullTools.getFromXMLAsString(geo, "//status");
        System.out.println("status: "+status);
        if (!status.equalsIgnoreCase("OK"))
        {
            address = new Address(paAddress);
            address.setGPSStatus(status);
        }
        city = UsefullTools.getFromXMLAsString(geo, "//address_component[type='locality']/short_name");
        street = UsefullTools.getFromXMLAsString(geo, "//address_component[type='route']/short_name");
        premise = UsefullTools.getFromXMLAsString(geo, "//address_component[type='premise']/short_name");
        postalCode = UsefullTools.getFromXMLAsString(geo, "//address_component[type='postal_code']/short_name");
        streetNumber = UsefullTools.getFromXMLAsString(geo, "//address_component[type='street_number']/short_name");
        latitude = UsefullTools.getFromXMLAsString(geo, "//location/lat");
        longitude = UsefullTools.getFromXMLAsString(geo, "//location/lng");
        
        float lat, lng;
        
        try {
            lat = Float.valueOf(latitude);
            lng = Float.valueOf(longitude);
        } catch (NumberFormatException e) {
            lat = 0;
            lng = 0;
        }
        
        address = new Address(city, street, premise, streetNumber, postalCode, lat, lng);
        address.setGPSStatus(status);
        return address;
    }
    
    /**
     *
     * @param paAddress
     * @return
     */
    public static Address getAddressWithCoordinates(String paAddress)
     {
         return new GPS().getCoordinates(paAddress);
     }
             
    /**
     *
     * @param paAddress
     * @param url
     * @return
     */
    public static Address getAddressWithCoordinates(String paAddress, String url)
     {
         return new GPS().getCoordinates(paAddress, url);
     }
}
