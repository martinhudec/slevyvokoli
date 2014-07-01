package slevy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Benefit reprezentuje jednu konkrétnu zľavu
 * @author martin
 * 
 */
class Benefit implements Serializable
{
    /**
     * Trieda BenefitAdress slúži na uloženie adresy danej zľavy.
     * 
     * Je potomkom triedy Address. Oproti triede Address má navyše atribút "benefitOnAddress".
     * V tomto atribúte sú odkazy na benefity s rovnakou adresou.
     */
    public static class BenefitAddress extends Address implements Serializable
    {
        public HashSet<Benefit> benefitOnThisAddress;
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
        public BenefitAddress(String paCity, String paStreet,String paPremise, String paStreetNumber, String paPostalCode, float paLat, float paLng)
        {
            super(paCity, paStreet, paPremise, paStreetNumber, paPostalCode, paLat, paLng);
            benefitOnThisAddress = new HashSet<>();
        }
        
        /**
         * 
         * @param address 
         */
        public BenefitAddress(Address address)
        {
            super(address);
            benefitOnThisAddress = new HashSet<>();
        }
        
        /**
         * 
         * @param paAddressLine 
         */
        public BenefitAddress(String paAddressLine)
        {
            super(paAddressLine);
            benefitOnThisAddress = new HashSet<>();
        }
        
        /**
         * 
         * @param ben 
         */
        public void addBenefitOnThisAddress(Benefit ben)
        {
            if (benefitOnThisAddress == null)
            {
                benefitOnThisAddress = new HashSet<>();
                benefitOnThisAddress.add(ben);
            } else
            {
                benefitOnThisAddress.add(ben);
            }
        }
        
        /**
         * 
         * @return 
         */
        public String getCityB()
        {
            return city;
        }
        
        /**
         * 
         * @return 
         */
        public String getStreetB()
        {
            return street;
        }
        
        /**
         * 
         * @return 
         */
        public String getPremiseB()
        {
            return premise;
        }
        /**
         * 
         * @return 
         */
        public String getStreetNumberB()
        {
            return streetNumber;
        }
        
        /**
         * 
         * @return 
         */
        public String getPostalCodeB()
        {
            return postalCode;
        }
        
    }
    
    private String name, web, email, phone;
    private TreeSet<String> cardType;
    private BenefitAddress address;

    /**
     * 
     * @param paName
     * @param paWeb
     * @param paEmail
     * @param paPhone
     * @param paCardType
     * @param paAddress 
     */
    public Benefit(String paName, String paWeb, String paEmail, String paPhone, String paCardType, Address paAddress)
    {
        name = paName;
        web = paWeb;
        email = paEmail;
        phone = paPhone;
        this.addCard(paCardType);
        address = new BenefitAddress(paAddress);
    }

    /**
     * 
     * @param paName
     * @param paWeb
     * @param paEmail
     * @param paPhone
     * @param paCardType
     * @param paAddress 
     */
    public Benefit(String paName, String paWeb, String paEmail, String paPhone, TreeSet<String> paCardType, Address paAddress)
    {
        name = paName;
        web = paWeb;
        email = paEmail;
        phone = paPhone;
        this.addCard(paCardType); 
        address = new BenefitAddress(paAddress);
    }

    /**
     * 
     * @param paName
     * @param paWeb
     * @param paEmail
     * @param paPhone
     * @param paCardType
     * @param paAddress 
     */
    public Benefit(String paName, String paWeb, String paEmail, String paPhone, String paCardType, String paAddress)
    {
        name = paName;
        web = paWeb;
        email = paEmail;
        phone = paPhone;
        this.addCard(paCardType);
        address = new BenefitAddress(paAddress);
    }

    /**
     * 
     * @param paName
     * @param paWeb
     * @param paEmail
     * @param paPhone
     * @param paCardType
     * @param paAddress 
     */
    public Benefit(String paName, String paWeb, String paEmail, String paPhone, TreeSet<String> paCardType, String paAddress)
    {
        name = paName;
        web = paWeb;
        email = paEmail;
        phone = paPhone;
        this.addCard(paCardType); 
        address = new BenefitAddress(paAddress);
    }

    /**
     * 
     * @param paCard 
     */
    public void addCard(String paCard)
    {
        if (cardType== null)
        {
             cardType = new TreeSet<>();
        }
        cardType.add(paCard);
    }

    /**
     * 
     * @param paCards 
     */
    public void addCard(TreeSet<String> paCards)
    {
       if (cardType== null)
        {
             cardType = new TreeSet<>();
        }
        for (String card: paCards) this.addCard(card);
    }

    public void changeAddress(Address add)
    {
        address = new BenefitAddress(add);
    }

    public void changeAddress(BenefitAddress add)
    {
        address = add;
    }

    public TreeSet<String> getCard()
    {
        return cardType;
    }
    
    /**
     * Vráti karty ako string, bez zátvoriek
     * @return zoznam kariet v stringu
     */
    public String getCardAsString()
    {
        String card = "";
        int i =0;
        for(String s:cardType){
            if (i == 0)
            {
                card+=s;
                i++;
            } else 
            {
                card+=", "+s;
            }    
        }
        return card;
    }

    public String getName()
    {
        return name;
    }

    public String getWeb()
    {
        return web;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public void setPhone(String newPhone)
    {
        phone = newPhone;
    }
    
    public void setEmail(String newEmail)
    {
        name = newEmail;
    }
    
    public void setWeb(String newEmail)
    {
        phone = newEmail;
    }

    @Override
    public String toString()
    {
        return "NAME=" + name + " ADDRESS=" + address.getAddressOneLine() + " CARDTYPE=" + cardType.toString() + " LONGITUDE="
                + address.getLongitude() + " LATITUDE=" + address.getLatitude();
    }

    /**
     * Metóda, ktorá porovnáva, či sú dva benefity od toho istého poskytovateľa
     * @param otherBenefit
     * @return Byte, čím vyššie číslo, tým väčšia šanca na zhodu 
     */
    public byte compareTo(Benefit otherBenefit)
    {
        byte c = 0;
        if (this.getName().equals(otherBenefit.getName()))
        {
            if (otherBenefit.getName().contains(this.getName())) c++;
        }
        
        if (5 <this.getWeb().length() && 5<otherBenefit.getWeb().length())
        if (this.getWeb().length()<otherBenefit.getWeb().length())
        {
            if (otherBenefit.getWeb().contains(this.getWeb())) c+=2;
        } else
        {
            if (this.getWeb().contains(otherBenefit.getWeb())) c+=2;
        }

        if (8 < this.getEmail().length() && 8 < otherBenefit.getEmail().length())
        if (this.getEmail().length()<otherBenefit.getEmail().length())
        {
            if (otherBenefit.getEmail().contains(this.getEmail())) c+=2;
        } else
        {
            if (this.getEmail().contains(otherBenefit.getEmail())) c+=2;
        }
        String thisPhone = this.getPhone().replace(" ", "");
        String otherPhone = otherBenefit.getPhone().replace(" ", "");
        
        if (8 < this.getPhone().length() && 8 < otherBenefit.getPhone().length())
        if (this.getPhone().length()<otherBenefit.getPhone().length())
        {
            if (otherPhone.contains(thisPhone)) c++;
        } else
        {
            if (thisPhone.contains(otherPhone)) c++;
        }

        return c;
    }

    public BenefitAddress getAddress()
    {
        return address;
    }

    public Float getLatitude()
    {
        return address.getLatitude();
    }

    public float getLongitude()
    {
        return address.getLongitude();
    }
    
    public Float getLatitudeF()
    {
        if (address.haveCoordinates()) return address.getLatitude();
        return null;
    }

    public Float getLongitudeF()
    {
        if (address.haveCoordinates()) return address.getLongitude();
        return null;
    }
    
    public String getFullInfo()
    {
        return name+"\n"+cardType.toString()+"\n"+web+"\n"+email+"\n"+phone+"\n"+address.getAddressMultiLineWithGPS();
    }
    
}
