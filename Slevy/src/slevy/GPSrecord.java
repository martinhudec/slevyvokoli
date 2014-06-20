/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slevy;

/**
 *
 * @author martin
 */
import java.util.Arrays;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "benefit")
@XmlType(propOrder = {"name", "cardType", "address", "latitude", "longitude"})

public class GPSrecord {

    private String address, name;
    private Float latitude, longitude;
    private String[] cardType;

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String[] getCardType() {
        return cardType;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setCardType(String[] cardType) {
        this.cardType = cardType;
    }

    public String toString() {
        return  "NAME=" + name + " ADDRESS=" + address + " CARDTYPE=" + Arrays.toString(cardType) + " LONGITUDE="
                + longitude + " LATITUDE=" + latitude;
    }
}
