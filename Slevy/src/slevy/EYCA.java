package slevy;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Trieda, ktorá obsahuje metódu na vyparsovanie údajov o karte EYCA
 * @author martin
 */
public class EYCA 
{
    
    /**
     * Metóda ziska údaje o partneroch zo stránky prislušnej karty.
     */
    public static void parserHTML()
    {
        Benefits benefits = (Benefits) UsefullTools.loadObjectFromFile("benefits.data");
        ArrayList<Benefit> badList = new ArrayList();
        for (int i = 0; i<64;i++)
        {
            String url = "http://www.eyca.org/discounts/search/"+i+"?q=&country_id2=CZ&city2=&country_id=CZ&city=&x=121&y=22";
            Document doc;
            Elements element; 
            
            String name = "", address = "", phone = "", email = "", web = "";
            try {
                doc = Jsoup.connect(url).get();
                element = doc.select("div[class=item-inner]");
                for (Element el : element) 
                {
                    name = el.getElementsByTag("h3").text();
                    Elements left = el.getElementsContainingOwnText("Address:");
                    if (left.first() != null) address = left.first().nextElementSibling().text();
                    left = el.getElementsContainingOwnText("URL:");
                    if (left.first() != null) web = left.first().nextElementSibling().child(0).text();
                    left = el.getElementsContainingOwnText("Phone:");
                    if (left.first() != null) phone = left.first().nextElementSibling().html().replace("\n<br /> ", ", ");
                    
                    Benefit bad = benefits.addBenefit(new Benefit(name, web, email, phone, "EYCA", address));
                    if (bad != null) badList.add(bad);
                    //System.out.println(name+"\n"+address+"\n"+phone+"\n"+email+"\n"+web+"\n");
                }
            } catch (IOException e) {
            }
            System.out.println(i+"/62");
        }
        UsefullTools.saveObjectToFile("bISIC_EYCA.data", benefits);
        UsefullTools.saveObjectToFile("bad.data", badList);
    }

}
