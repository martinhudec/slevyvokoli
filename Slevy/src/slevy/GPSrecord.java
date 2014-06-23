package slevy;

/**
 *
 * @author martin
 */
import java.util.Arrays;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "benefit")
@XmlType(propOrder = {"name", "cardType", "address", "latitude", "longitude"})

public class GPSrecord {

    private Integer id;

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

    @XmlAttribute
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "NAME=" + name + " ADDRESS=" + address + " CARDTYPE=" + Arrays.toString(cardType) + " LONGITUDE="
                + longitude + " LATITUDE=" + latitude;
    }
}
