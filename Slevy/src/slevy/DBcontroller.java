package slevy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trieda na prácu s databázou, pripojenie k nej, vyplnenie,..
 * @author martin
 */
public class DBcontroller {
    
    private final String URL = "jdbc:postgresql://ec2-54-197-251-18.compute-1.amazonaws.com:5432/d1gcms75s3ef0g?user=nxxclbdnrjiwcx&password=pz1YC2j_lfM4YBuV3iLFyV7c_p&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    private final Properties PROPS = new Properties();

    // Nastavenia pre pripojenie k databáze
    public boolean fillDB(List<Benefit> list) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = null;
        PROPS.setProperty("user", "nxxclbdnrjiwcx");
        PROPS.setProperty("password", "pz1YC2j_lfM4YBuV3iLFyV7c_p");
        PROPS.setProperty("ssl", "true");
        try {
            connection = DriverManager.getConnection(URL, PROPS);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
            return false;
        }
        
        // Spojenie sa nenašlo
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
        
        // Vymaže tabuľku pred vytvorením novej 
        List<String> querry = new ArrayList<>();
        String record;
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

        for (Iterator<Benefit> it = list.iterator(); it.hasNext();) {
            Benefit benefit = it.next();
            int i=1;
            record = "INSERT INTO isic VALUES (" + i++ + ", '" + benefit.getName() + "', '" + benefit.getAddress() + "', '" + benefit.getCardAsString() + "', " + benefit.getLatitude() + ", " + benefit.getLongitude() + ");";
            querry.add(record);
            //System.out.println(benefit.toString());
        }
        
        // Vyplnenie databáze, ak sú dostupné nejaké údaje 
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
    
     public static void main(String[] args) throws ClassNotFoundException {
        
        Benefits bens = (Benefits) UsefullTools.loadBenefitsFromXML("ISIC&EYCA&SPHERE.xml");
        
        UsefullTools.saveObjectToFile("ISIC&EYCA&SPHERE.data", bens);
        DBcontroller cnt = new DBcontroller();
        //cnt.fillDB(bens.getBenefits());
        /*EYCA.parserHTML();
        SphereCard.parserHTML(); */
    }
}
