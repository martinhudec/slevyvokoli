package slevy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.DOMException;

/**
 * Trieda obsahuje metódu na vyparsovanie údajov zo stránok ISICu
 * @author martin
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class IsicCard {

    /**
     *
     * @throws IOException
     */
    public static void createLinksToBenefitsPage() throws IOException {
        Document doc = Jsoup.connect("http://localhost/lenzlavy.html").get();
        Elements e = doc.select("a[href]");
        String predchadzjuci = "", links = "";
        for (Element el : e) {
            if (!el.attr("href").equals(predchadzjuci)) {
                links+=(el.attr("href")+"\n");
            }
            predchadzjuci = el.attr("href");
        }
        BufferedWriter bwFile = new BufferedWriter(new FileWriter("isicCard/linksToBenefitsPage.txt"));
        bwFile.write(links);
        bwFile.close();     
    }

    private static void vytvorXMLZliavZLinkov() {
        Scanner scanerZoSuboru = null;
        try {
            File subor = new File("isicCard/linksToBenefitsPage.txt");
            scanerZoSuboru = new Scanner(subor);
            org.w3c.dom.Document xml = UsefullTools.documentBuilder().newDocument();
            org.w3c.dom.Element zlavy = xml.createElement("zlavy");
            xml.appendChild(zlavy);
            int id = 1;
            while (scanerZoSuboru.hasNextLine()) {
                zlavy.appendChild(spracujZlavu(scanerZoSuboru.nextLine(), "" + id++, xml));
            }
            UsefullTools.createXMLFile(xml, "isiCzlavy.xml");
        } catch (IOException | DOMException e) {
        } finally {
            if (scanerZoSuboru != null) {
                scanerZoSuboru.close();
            }
        }
    }

    private static org.w3c.dom.Element spracujZlavu(String url, String id, org.w3c.dom.Document document) throws IOException {
        org.w3c.dom.Element zlava = document.createElement("zlava");
        org.w3c.dom.Element pom;
        zlava.setAttribute("id", id);
        Document doc = Jsoup.connect(url).get();
        pom = document.createElement("meno");
        pom.appendChild(document.createTextNode(doc.getElementsByTag("h2").text()));
        zlava.appendChild(pom);
        Elements e = doc.select("img[class]");
        for (Element el : e) {
            if (el.attr("class").equals("minikarta")) {
                pom = document.createElement("plati-pre");
                pom.appendChild(document.createTextNode(el.attr("title")));
                zlava.appendChild(pom);
            }
        }
        e = doc.select("td");
        String predchadzjuciElement = "";
        for (Element el : e) {
            if (predchadzjuciElement.equals("adresa")) {
                pom = document.createElement("adresa");
                pom.appendChild(document.createTextNode(el.text()));
                zlava.appendChild(pom);
            }
            if (predchadzjuciElement.equals("telefon")) {
                pom = document.createElement("telefon");
                pom.appendChild(document.createTextNode(el.text()));
                zlava.appendChild(pom);
            }
            if (predchadzjuciElement.equals("email")) {
                pom = document.createElement("email");
                pom.appendChild(document.createTextNode(el.text()));
                zlava.appendChild(pom);
            }
            if (predchadzjuciElement.equals("str\u00e1nky")) {
                pom = document.createElement("web");
                pom.appendChild(document.createTextNode(el.text()));
                zlava.appendChild(pom);
            }
            predchadzjuciElement = el.text();
        }
        pom = document.createElement("info");
        pom.appendChild(document.createTextNode(doc.getElementsByAttributeValue("id", "note").text()));
        zlava.appendChild(pom);
        return zlava;
    }
}
