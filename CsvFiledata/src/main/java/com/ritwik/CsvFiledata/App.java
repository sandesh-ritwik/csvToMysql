package com.ritwik.CsvFiledata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	studentdata();
//    	Properties p = new Properties();
//    	InputStream is = new FileOutputStream("dataconfig.properties");
//    	p.is()
//    	System.out.println("done");
    	
	}
    public static void studentdata() {

        String csvFilePath = "C:\\Users\\RSTPL081\\Desktop\\Student.csv";
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            CSVParser records = CSVParser.parse(lineReader, CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            ArrayList<Student> students = new ArrayList<Student>();
            for (CSVRecord record : records) {
                Student student = new Student();
                student.setStudentId(Integer.parseInt(record.get(0)));
                student.setStudentName(record.get(1));
                students.add(student);
            }
            PreparedStatement statement = null;
            Connection con = dbconnection();
            String sql = "INSERT INTO student(STUDENTID, STUDENTNAME) VALUES (?, ?)";
            statement = con.prepareStatement(sql);
            for (Student record : students) {
            statement.setInt(1, record.getStudentId());
            statement.setString(2, record.getStudentName());

            statement.addBatch();
        }
        statement.executeBatch();
        con.commit();
        con.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    }

}
public static Connection dbconnection() {

    Connection connection = null;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdata?", "root", "ritwik");
        System.out.println("connection sucessfull");
        connection.setAutoCommit(false);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return connection;
}
}
