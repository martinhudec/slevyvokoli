package slevy;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import slevy.GPSrecord;

/**
 *
 * @author martin
 */
public class DBcontroller {

    private List listOfRecords;
    private final String URL = "jdbc:postgresql://ec2-54-197-251-18.compute-1.amazonaws.com:5432/d1gcms75s3ef0g?user=nxxclbdnrjiwcx&password=pz1YC2j_lfM4YBuV3iLFyV7c_p&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    private final Properties PROPS = new Properties();

    public boolean fillDB(List<GPSrecord> list) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = null;
        PROPS.setProperty("user", "nxxclbdnrjiwcx");
        PROPS.setProperty("password", "pz1YC2j_lfM4YBuV3iLFyV7c_p");
        PROPS.setProperty("ssl", "true");
        try {
            // ========>     from heroku website
            //String url = "jdbc:postgresql://ec2-107-20-214-225.compute-1.amazonaws.com:5432/d6iv2kl28367ds?user=wkpftjetpstqic&password=F9pRGn6QWsgxWOXU6zf5asLqIS&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
            connection = DriverManager.getConnection(URL, PROPS);

        } catch (SQLException ex) {
            System.out.println("Chyba: " + ex.toString());
            return false;
        }

        if (connection == null) {
            System.out.println("Spojenie sa nenašlo");
            return false;
        }

        Statement stm = null;
        try {
            stm = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        List<String> querry = new ArrayList<>();
        String record = null;
        String dropAllTables = "DROP TABLE isic";
        String sql = "CREATE TABLE isic " + "(id INTEGER, name VARCHAR(255), address VARCHAR(255), cardtype VARCHAR(255), latitude FLOAT, longitude FLOAT, PRIMARY KEY (id))";

        Integer count = 0;
        try {
            stm.executeUpdate(dropAllTables);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        try {
            stm.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        for (Iterator<GPSrecord> it = list.iterator(); it.hasNext();) {
            GPSrecord gpsrecord = it.next();
            record = "INSERT INTO isic VALUES (" + gpsrecord.getId() + ", '" + gpsrecord.getName() + "', '" + gpsrecord.getAddress() + "', '" + Arrays.toString(gpsrecord.getCardType()) + "', " + gpsrecord.getLatitude() + ", " + gpsrecord.getLongitude() + ");";
            querry.add(record);
            //System.out.println(gpsrecord.toString());
        }

        count = 0;
        String tmp = null;
        PreparedStatement pstmt;
        String str ;
        Iterator<String> it = querry.iterator();
        try {
            while (it.hasNext()) {
                connection = DriverManager.getConnection(URL, PROPS);
                for (int i = 0; i < 1000; i++) {
                    try{
                    str = it.next();
                    tmp = str;
                    //stm.executeUpdate(string);
                    pstmt = connection.prepareStatement(str);
                    pstmt.executeUpdate();
                    }catch (Exception ex){
                        System.out.println(ex.toString());
                        System.out.println(tmp);
                    }
                    if (i==999){
                        System.out.println("1000!");
                    }
                }
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println("Chyba: " + ex.toString());
            System.out.println(tmp);
            return false;
        }
        try {
            /*
             //String sql = "CREATE TABLE ISIC2 " + "(id INTEGER, meno VARCHAR(255), PRIMARY KEY (id))";
             //stm.executeUpdate(sql);
            
             // String sql = "INSERT INTO isic VALUES (3,'martinko');";
             //stm.executeUpdate(sql);
             String sql = "DROP TABLE isic2";
             stm.executeUpdate(sql);
            
             String query = "SELECT * FROM isic";
             ResultSet rs = stm.executeQuery(query);
             while (rs.next()) {
             System.out.print(rs.getInt("id") + " ");
             System.out.println(rs.getString("meno"));
             }; */
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBcontroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }
}
