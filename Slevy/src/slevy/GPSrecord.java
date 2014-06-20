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


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "benefit")
@XmlType(propOrder = {"name", "cardType", "address"})

public class GPSrecord {
private String address, name;
private Float latitude, longitude;
private String cardType;

    public GPSrecord(String name, String adress, Float lattitude, Float longitude, String cardType) {
        this.address = adress;
        this.name = name;
        this.latitude = lattitude;
        this.longitude = longitude;
        this.cardType = cardType;
    }

    public String getAdress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Float getLattitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getCardType() {
        return cardType;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLattitude(Float lattitude) {
        this.latitude = lattitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


}
