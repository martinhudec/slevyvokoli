/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slevy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Martin Hudec
 */
public class Places {

    private float lat1, lat2, lon1, lon2;
    private final String URL = "jdbc:postgresql://ec2-54-197-251-18.compute-1.amazonaws.com:5432/d1gcms75s3ef0g?user=nxxclbdnrjiwcx&password=pz1YC2j_lfM4YBuV3iLFyV7c_p&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    private final Properties PROPS = new Properties();

    /**
     * 48.581681, 18.868036 lon 40076 / 180 lat 40076 / 360
     *
     * @param lon Longitude
     * @param lat Latitude
     * @return
     */
    public List<GPSrecord> getPlaces(float lat3, float lon3) {
        List<GPSrecord> places = new ArrayList<>();
        //lat = lat2;
        //lon = lon2;
        lon1 = lon3 + (20f / (40076f / 180f));
        lat1 = lat3 + (16f / (40076f / 360f));
        lon2 = lon3 - (20f / (40076f / 180f));
        lat2 = lat3 - (16f / (40076f / 360f));
        // this.lon = lon+10/(40076/360) ;
        //lat = lat2-(20f/(40076f/360f));
        float[] adjusted = new float[4];
        adjusted[0] = this.lat1;
        adjusted[1] = this.lon1;
        adjusted[2] = this.lat2;
        adjusted[3] = this.lon2;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.toString());
        }
        Connection connection = null;
        PROPS.setProperty("user", "nxxclbdnrjiwcx");
        PROPS.setProperty("password", "pz1YC2j_lfM4YBuV3iLFyV7c_p");
        PROPS.setProperty("ssl", "true");
        try {
            // ========>     from heroku website
            //String url = "jdbc:postgresql://ec2-107-20-214-225.compute-1.amazonaws.com:5432/d6iv2kl28367ds?user=wkpftjetpstqic&password=F9pRGn6QWsgxWOXU6zf5asLqIS&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
            connection = DriverManager.getConnection(URL, PROPS);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
            return null;
        }

        if (connection == null) {
            System.out.println("Connection not found.");
            return null;
        }

        Statement stm = null;
        try {
            stm = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        String sql = "SELECT id,name,address,cardType,latitude,longitude FROM isic WHERE " + adjusted[0] + ">latitude AND " + adjusted[2] + "<latitude AND " + adjusted[3] + "<longitude AND " + adjusted[1] + ">longitude;";
        /*String sql = "SELECT e.id as 'ID'"
         + ", e.name as 'Name'"
         + ", e.address as 'Address'"
         + ", e.cardType as 'Cardtype'"
         + ", e.latitude as 'Latitude'"
         + ", e.longitude as 'Longitude'"
         + "FROM isic e WHERE "+adjusted[0]+">latitude AND "+adjusted[2]+"<latitude AND "+adjusted[3]+"<longitude AND "+adjusted[1]+">longitude;";
         */
        try {
            ResultSet rs = stm.executeQuery(sql);

            String tmp;
            int i = 0;
            while (rs.next()) {
                GPSrecord record = new GPSrecord();
                /*System.out.print(rs.getString("id"));
                 System.out.print(rs.getString("name"));
                 System.out.print(rs.getString("address"));
                 System.out.print(rs.getString("latitude"));
                 System.out.println(rs.getString("longitude"));
                 */
                tmp = rs.getString("id");
                record.setId(Integer.parseInt(tmp));
                tmp = rs.getString("name");
                record.setName(tmp);
                tmp = rs.getString("address");
                record.setAddress(tmp);
                tmp = rs.getString("latitude");
                record.setLatitude(Float.parseFloat((tmp)));
                tmp = rs.getString("longitude");
                record.setLongitude(Float.parseFloat(tmp));
                places.add(record);
                places.add(i, record);
                i++;
            }

        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return places;
    }
}
