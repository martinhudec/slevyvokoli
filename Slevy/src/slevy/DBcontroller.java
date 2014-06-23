package slevy;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class DBcontroller {

    private final String URL = "jdbc:postgresql://ec2-54-197-251-18.compute-1.amazonaws.com:5432/d1gcms75s3ef0g?user=nxxclbdnrjiwcx&password=pz1YC2j_lfM4YBuV3iLFyV7c_p&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    private final Properties PROPS = new Properties();

    //Settings for connection to the DB 
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
            System.out.println("Error: " + ex.toString());
            return false;
        }
        
        //connection was not found
        if (connection == null) {
            System.out.println("Connection not found.");
            return false;
        }

        Statement stm = null;
        try {
            stm = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        
        //Drop table at the beginning of the connection, create new one 
        List<String> querry = new ArrayList<>();
        String record = null;
        String dropAllTables = "DROP TABLE isic";
        String sql = "CREATE TABLE isic " + "(id INTEGER, name VARCHAR(255), address VARCHAR(255), cardtype VARCHAR(255), latitude FLOAT, longitude FLOAT, PRIMARY KEY (id))";

        
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
        
        //filling the DB, if there are some files
        PreparedStatement pstmt;
        String str= "";
        Iterator<String> it = querry.iterator();
        int i = 1;
        while (it.hasNext()) {
            try {
            str = it.next();
            
            pstmt = connection.prepareStatement(str);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            System.out.println(str);
        }
        System.out.println(i);
        i++;

        }
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBcontroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }
}