package slevy;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Pomocná trieda, ktorá obsahuje pomocné metódy, na rôzne účely 
 * @author martin
 */
public class UsefullTools {
    
    private static final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    
    private static Object get(Document doc,String xPathExp, QName returnType)
    {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        try {
            return xPath.compile(xPathExp).evaluate(doc, returnType);
        } catch (XPathExpressionException ex)
        {
            
        }
        return null;
    }
    
    /**
     * Zo zadaného dokumentu, ktorý je zadaný ako parameter sa vyberú informácie na základe zadaného Xpath výrazu
     * @param doc dokument 
     * @param xPathExp Xpath výraz
     * @return String
     */
    public static String getFromXMLAsString(Document doc,String xPathExp)
    {
        return (String)get(doc, xPathExp, XPathConstants.STRING);
    }
    
    /**
     * Zo zadaného dokumentu, ktorý je zadaný ako parameter sa vyberú informácie na základe zadaného XPath výrazu
     * @param doc
     * @param xPathExp
     * @return Node
     */
    public static Node getFromXMLAsNode(Document doc,String xPathExp)
    {
        return (Node)get(doc, xPathExp, XPathConstants.NODE);
    }
    
    /**
     * Zo zadaného dokumentu vráti zoznam uzlov, ktoré vyhovujú XPath výrazu 
     * @param doc
     * @param xPathExp
     * @return List of nodes
     */
    public static NodeList getFromXMLAsNodeList(Document doc,String xPathExp)
    {
        return (NodeList)get(doc, xPathExp, XPathConstants.NODESET);
    }
    
    /**
     * Načítanie XML zo súboru 
     * @param uri
     * @return objekt typu dokument, ktorý reprezentuje načítané XML
     */
    public static Document loadXMLFromURI(String uri) {
        Document doc = null;
        try {
            doc = documentBuilder().parse(uri);
        } catch (IOException e) {
            System.err.println("File is not found: " + uri);
        } catch (SAXException e) {
            System.err.println("Error: " + e.getLocalizedMessage());
        }
        return doc;
    }
    
    /**
     * Z objektu typu dokument vytvorí XML súbor
     * @param doc
     * @param file 
     */
    public static void createXMLFile(Document doc, String file) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(file));
            transformer.transform(domSource, streamResult);
        } catch (IllegalArgumentException | TransformerException e) {
            System.err.println("Error: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * 
     * @return objekt typu DocumentBuilder 
     */
    public static DocumentBuilder documentBuilder()
    {
        try {
            return documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UsefullTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Metoda, ktorá slúži na odstránenie diakritiky z textu
     * @param string
     * @return String bez diakritiky
     */
    public static String removeDiak(String string){
       String stringBD="";
       String sdiak="áäčďěéíĺžňóöôŕřšťúüýžźÁÄČĎĚÉÍĹŇÓÖÔŔŘÚÜÝŽŐőÖöŰűÜü";
       String bdiak="aacdeeillnooorrstuuyzzAACDEEILLNOOORRTUUYZOoOoUuUu";
       for (int l=0;l<string.length();l++){
           if (sdiak.indexOf(string.charAt(l))!=-1)
               stringBD+=bdiak.charAt(sdiak.indexOf(string.charAt(l)));
           else
               stringBD+=string.charAt(l);

       }
       return stringBD;
   }
    
    /**
     * Uloží zadaný objekt do súboru 
     * @param file
     * @param object 
     */
    public static void saveObjectToFile(String file, Object object)
    {
        FileOutputStream out;
        ObjectOutputStream s;
        try 
        {
            out = new FileOutputStream(file);
            s = new ObjectOutputStream(out);
            s.writeObject(object);
            s.flush();
        }
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(UsefullTools.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(UsefullTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Načítanie objektu zo súboru 
     * @param file
     * @return vráti daný objekt zo súboru 
     */
    public static Object loadObjectFromFile(String file)
    {
        FileInputStream in;
	ObjectInputStream s;
        try 
        {
            in = new FileInputStream(file);
            s = new ObjectInputStream(in);
            return s.readObject();
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.err.println("Error: "+e.getLocalizedMessage());
        }
            
       
        return null;
    }
    
    /**
     * Z objektu Benefit vygeneruje XML súbor, aby ho bolo možne v prípade potreby obnoviť 
     * @param benefits
     * @param file 
     */
    public static void saveBenefitsToXML(Benefits benefits, String file)
    {
        String gpsStatus="", coor ="", city ="", street ="", premise ="", streetNumber = "", postalCode="", latitude="", longitude="";
        String name = "", phone = "", email = "", web = "";
        
        org.w3c.dom.Document xml = UsefullTools.documentBuilder().newDocument();
        org.w3c.dom.Element benefitsE = xml.createElement("benefits");
        xml.appendChild(benefitsE);
        
        for (int i = 0; i < benefits.numbersBenefits(); i++)
        {
            gpsStatus = benefits.getBenefit(i).getAddress().getGPSStatus();
            coor =""+ benefits.getBenefit(i).getAddress().haveCoordinates();
            city =benefits.getBenefit(i).getAddress().getCityB();
            street =benefits.getBenefit(i).getAddress().getStreetB();
            premise = benefits.getBenefit(i).getAddress().getPremiseB();
            streetNumber = benefits.getBenefit(i).getAddress().getStreetNumberB();
            postalCode = benefits.getBenefit(i).getAddress().getPostalCodeB();
            latitude = ""+benefits.getBenefit(i).getAddress().getLatitude();
            longitude = ""+benefits.getBenefit(i).getAddress().getLongitude();
            name = benefits.getBenefit(i).getName();
            web = benefits.getBenefit(i).getWeb();
            email = benefits.getBenefit(i).getEmail();
            phone = benefits.getBenefit(i).getPhone();
            
            org.w3c.dom.Element benefit = xml.createElement("benefit");
            org.w3c.dom.Element pom;
            benefit.setAttribute("id", ""+(i+1));
            
            pom = xml.createElement("name");
            pom.appendChild(xml.createTextNode(name));
            benefit.appendChild(pom);
            
            for (String card: benefits.getBenefit(i).getCard())
            {
                pom = xml.createElement("cardType");
                pom.appendChild(xml.createTextNode(card));
                benefit.appendChild(pom);
            }
            
            pom = xml.createElement("web");
            pom.appendChild(xml.createTextNode(web));
            benefit.appendChild(pom);
            
            pom = xml.createElement("email");
            pom.appendChild(xml.createTextNode(email));
            benefit.appendChild(pom);
            
            pom = xml.createElement("phone");
            pom.appendChild(xml.createTextNode(phone));
            benefit.appendChild(pom);
            
            pom = xml.createElement("address");
            
            org.w3c.dom.Element pom2 = xml.createElement("city");
            pom2.appendChild(xml.createTextNode(city));
            pom.appendChild(pom2);
            pom2 = xml.createElement("street");
            pom2.appendChild(xml.createTextNode(street));
            pom.appendChild(pom2);
            pom2 = xml.createElement("premise");
            pom2.appendChild(xml.createTextNode(premise));
            pom.appendChild(pom2);
            pom2 = xml.createElement("streetNumber");
            pom2.appendChild(xml.createTextNode(streetNumber));
            pom.appendChild(pom2);
            pom2 = xml.createElement("latitude");
            pom2.appendChild(xml.createTextNode(latitude));
            pom.appendChild(pom2);
            pom2 = xml.createElement("longtitude");
            pom2.appendChild(xml.createTextNode(longitude));
            pom.appendChild(pom2);
            pom2 = xml.createElement("gpsStatus");
            pom2.appendChild(xml.createTextNode(gpsStatus));
            pom.appendChild(pom2);
            pom2 = xml.createElement("coor");
            pom2.appendChild(xml.createTextNode(coor));
            pom.appendChild(pom2);
            
            benefit.appendChild(pom);
            benefitsE.appendChild(benefit);
        }
        
        UsefullTools.createXMLFile(xml, file);
    }
    
    /**
     * Zo zadaného XMLka vytvorí objekt benefits, použije sa len v prípade, že sa stane
     * chyba pri vytváraní objektu
     * @param uri
     * @return 
     */
    public static Benefits loadBenefitsFromXML(String uri)
    {
        String gpsStatus="", coor ="", city ="", street ="", premise ="", streetNumber = "", postalCode="", latitude="", longitude="";
        String name = "", phone = "", email = "", web = "";
        Benefits benefits = new Benefits();
        try {
        org.w3c.dom.Document xml = UsefullTools.documentBuilder().parse(uri);
        int pocet = Integer.valueOf(UsefullTools.getFromXMLAsString(xml, "/benefits/benefit[last()]/@id"));
        for (int i = 1; i < (pocet + 1); i++)
        {
            gpsStatus = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/gpsStatus");
            coor =""+ UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/coor");
            city =UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/city");
            street =UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/street");
            premise = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/premise");
            streetNumber = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/streetNumber");
            postalCode = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/postalCode");
            latitude = ""+UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/latitude");
            longitude = ""+UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/address/longtitude");
            name = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/name");
            web = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/web");
            email = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/email");
            phone = UsefullTools.getFromXMLAsString(xml, "//benefit[@id='"+i+"']/phone");
            
            NodeList cl = UsefullTools.getFromXMLAsNodeList(xml, "//benefit[@id='"+i+"']/cardType");
            TreeSet<String> card = new TreeSet<>();
            for(int j = 0;j< cl.getLength(); j++)
            {
                card.add(cl.item(j).getTextContent());
            }
            Address adr = new Address(city, street, premise, streetNumber, postalCode, Float.valueOf(latitude), Float.valueOf(longitude), Boolean.valueOf(coor));
            adr.setGPSStatus(gpsStatus);
            Benefit benefit = new Benefit(name, web, email, phone, card, adr);
            benefits.addBenefit(benefit);
            System.out.println(i+"/"+pocet);
        }
        } catch (IOException | NumberFormatException | DOMException | SAXException e)
        {
            
        }
        return benefits;
    }
}

