/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slevy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Martin Hudec
 *
 */
public class test {
    //49.198689, 16.607815

    private static float lat = 49.198689f;
    private static float lon = 16.607815f;

    public static void main(String[] args) {
        Places places = new Places();
        //places.getPlaces(lat,lon);
        List<GPSrecord> list = new ArrayList<>();
        list = places.getPlaces(lat, lon);
        int i = 0;
       /* for (Iterator<GPSrecord> it = list.iterator(); it.hasNext();) {
            GPSrecord gPSrecord = it.next();
            System.out.println(gPSrecord.toString());
            i++;

        } */
        System.out.println(i);

        JSONObject obj = new JSONObject();

        obj.put("list", list);

        System.out.print(obj);
      
    }

}
