package slevy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class DBcontroller {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    System.out.println("-------- PostgreSQL "
            + "JDBC Connection Testing ------------");
    try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        System.out.println("Where is your PostgreSQL JDBC Driver? "
                + "Include in your library path!");
        e.printStackTrace();
        return;
    }
    System.out.println("PostgreSQL JDBC Driver Registered!");
    Connection connection = null;
    try {
        // ========>     from heroku website
        String url = "jdbc:postgresql://ec2-54-197-251-18.compute-1.amazonaws.com:5432/d1gcms75s3ef0g?user=nxxclbdnrjiwcx&password=pz1YC2j_lfM4YBuV3iLFyV7c_p&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        //String url = "jdbc:postgresql://ec2-107-20-214-225.compute-1.amazonaws.com:5432/d6iv2kl28367ds?user=wkpftjetpstqic&password=F9pRGn6QWsgxWOXU6zf5asLqIS&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        Properties props = new Properties();
        props.setProperty("user", "nxxclbdnrjiwcx");
        props.setProperty("password", "pz1YC2j_lfM4YBuV3iLFyV7c_p");
        props.setProperty("ssl", "true");
        connection = DriverManager.getConnection(url, props);
        
        Statement stm = connection.createStatement(); 
        //String sql = "CREATE TABLE ISIC2 " + "(id INTEGER, meno VARCHAR(255), PRIMARY KEY (id))";
        //stm.executeUpdate(sql);
        
       // String sql = "INSERT INTO isic VALUES (3,'martinko');";
        //stm.executeUpdate(sql);
        
        String sql = "DROP TABLE isic2";
        stm.executeUpdate(sql);

        
        String query = "SELECT * FROM isic";
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()){
            System.out.print(rs.getInt("id")+" ");
            System.out.println(rs.getString("meno"));
        };
        
        
        
    } catch (SQLException e) {

        System.out.println("Connection Failed! Check output console");
        e.printStackTrace();
        return;
    }
    }
}
