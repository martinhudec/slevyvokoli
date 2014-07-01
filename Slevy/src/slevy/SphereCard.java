package slevy;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Trieda, ktorá obsahuje metódu na vyparsrovanie údajov o karte Sphere
 * @author martin
 */
public class SphereCard {
    
    public static void parserHTML()
    {
        Benefits benefits = (Benefits) UsefullTools.loadObjectFromFile("ISIC&EYCA.data");
        ArrayList<Benefit> badList = new ArrayList();
        for (int i= 1; i<135; i++)
        {
            String url = "http://www.sphere.sk/katalog/vysledky/?oblasti=A101,A102,A103,A104,A105,A106,A107,A108,A109,A110,A191,A192,A203,A204,A205,A206,A207,A208,A209,A210,A211,A212,A251,A252,A253,A254,A255,A256,A257,A301,A302,A303,A304,A305,A306,A307,A351,A352,A353,A401,A402,A403,A404,A405,A451,A452,A453,A454,A455,A471,A472,A473,A474,A501,A502,A503,A504,A505,A506,A551,A552,A553,A554,A555,A601,A602,A603,A604,A651,A652,A653,A654,A655,A656,A657,A701,A702,A703,A704,A705,A706,A707,A751,A752,A753,A754&zeme=cs&page="+i;
            Document doc;
            Elements elements; 
            
            String name = "", address = "", phone = "", email = "", web = "";
            
            try 
            {
                doc = Jsoup.connect(url).get();
                elements = doc.select("div[class=result]");
                for (Element el: elements)
                {
                    Elements td, left;
                    name = ""; address = ""; phone = ""; email = ""; web = "";
                    name = el.getElementsByTag("h3").text();
                    web = el.getElementsContainingOwnText("Web").attr("href");
                    td = el.getElementsByTag("tbody");
                    
                    try {
                        left = td.get(0).getElementsContainingOwnText("Adresa:");
                        if (left != null) address = left.parents().get(1).child(1).text();
                    } catch (Exception e) {
                        System.err.println("addressException: "+e.getLocalizedMessage());
                    }
                    
                    try {
                        left = td.get(0).getElementsContainingOwnText("Telefón:");
                        if (left != null) phone = left.parents().get(1).child(1).text();
                    } catch (Exception e) {
                        System.err.println("phoneException: "+e.getLocalizedMessage());
                    }
                    
                    Benefit bad = benefits.addBenefit(new Benefit(name, web, email, phone, "SPHERE", address));
                    if (bad != null) badList.add(bad);
                    //System.out.println(""+name+"\n"+address+"\n"+phone+"\n"+email+"\n"+web+"\n");  
                }
            } catch (Exception e) {
                System.err.println("Exception: "+e.getLocalizedMessage());
            }
            System.out.println(i+"/134");
        }
        UsefullTools.saveObjectToFile("ISIC&EYCA&SPHERE.data", benefits);
        UsefullTools.saveObjectToFile("badSPHERE.data", badList);
    }
    
}
