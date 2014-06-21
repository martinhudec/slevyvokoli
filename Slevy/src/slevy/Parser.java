/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slevy;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Parser {

    public static void main(String[] args) throws ClassNotFoundException {

        final File folder = new File("xmls");

        List<String> list = new ArrayList<>();
        list = listFilesForFolder(folder);

        List<GPSrecord> records = new ArrayList<>();

        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            String fileName = it.next();
            GPSrecord rec = jaxbXMLToObject("xmls/" + fileName);
            records.add(rec);
        }
        
        String[] parts = null;
        for (Iterator<GPSrecord> it = records.iterator(); it.hasNext();) {          //kontroluje stringy na riadiace charaktery
            GPSrecord gpsrecord = it.next();
            if (gpsrecord.getName().contains("'")) {
                parts = gpsrecord.getName().split("\\'");
                gpsrecord.setName(parts[0]+"´"+parts[1]);  
                
            }
        }

        DBcontroller cnt = new DBcontroller();
        cnt.fillDB(records);
    }

    private static GPSrecord jaxbXMLToObject(String fileName) {
        try {
            JAXBContext context = JAXBContext.newInstance(GPSrecord.class);
            Unmarshaller un = context.createUnmarshaller();
            GPSrecord rec = (GPSrecord) un.unmarshal(new File(fileName));
            return rec;
        } catch (JAXBException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    private static List<String> listFilesForFolder(final File folder) {
        List<String> list = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                list.add(fileEntry.getName());
                //System.out.println(fileEntry.getName());
            }
        }
        return list;
    }

}
