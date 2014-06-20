/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package slevy;

import java.io.File;
 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
 
 
public class JAXBExample {
 
    private static final String FILE_NAME = "jaxb-emp.xml";
 
    public static void main(String[] args) {
        Employee emp = jaxbXMLToObject();
        
        System.out.println(emp.getName().toString());
 
    }
 
 
    private static Employee jaxbXMLToObject() {
        try {
            JAXBContext context = JAXBContext.newInstance(Employee.class);
            Unmarshaller un = context.createUnmarshaller();
            Employee emp = (Employee) un.unmarshal(new File(FILE_NAME));
            return emp;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
 
}