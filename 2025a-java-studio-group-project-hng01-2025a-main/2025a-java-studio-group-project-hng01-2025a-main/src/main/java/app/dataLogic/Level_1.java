package app.dataLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// 1. getMember 12-52

public class Level_1 extends JDBCmain {
  ///////////////////////////////////////// For member
 public ArrayList<memberClass> getMember(){
    ArrayList<memberClass> members = new ArrayList<memberClass>();
    try {
        // Connect to JDBC data base
        Connection connection = DriverManager.getConnection(DATABASE_Member);

        // Prepare a new SQL Query & Set a timeout
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        // The Query - Updated to include Mem_img
        String query = "SELECT * FROM Members";
        
        // Get Result
        ResultSet results = statement.executeQuery(query);

        // Process all of the results
        while (results.next()) {
            // Lookup the columns we need
            String sID = results.getString("Sid");
            String mem_img = results.getString("Mem_img");
            String name = results.getString("Full_name");
            String gender = results.getString("gender");
            
            // Create a member Object with all fields
            memberClass member = new memberClass(sID, mem_img, name, gender);

            // Add the member object to the array
            members.add(member);
        }

        // Close the statement because we are done with it
        statement.close();
    } catch (SQLException e) {
        // If there is an error, lets just print the error
        System.err.println(e.getMessage());
    }
    return members;
}

    ///////////////////////////////////////// For persona
    public ArrayList<personaClass> getPersona(){
        ArrayList<personaClass> personasin4 = new ArrayList<personaClass>();
        try {
            // Connect to JDBC data base
            Connection connection = DriverManager.getConnection(DATABASE_Member);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select * from personas";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String psname = results.getString("ps_name");
                String psimg = results.getString("ps_img");
                String psquote = results.getString("ps_quote");
                String psage = results.getString("ps_age");
                String psdescription = results.getString("ps_des");
                String psbackground = results.getString("ps_background");
                String psneed1 = results.getString("ps_needs1");
                String psneed2 = results.getString("ps_needs2");
                String pspain1 = results.getString("ps_pain1");
                String pspain2 = results.getString("ps_pain2");
                String psskills = results.getString("ps_skills");
            
                // Create a temperature Object
                personaClass psn = new personaClass(psname, psimg, psquote, psage, psdescription, psbackground, psneed1, psneed2, pspain1, pspain2, psskills);
                // Add the lga object to the array
                personasin4.add(psn);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        }
        return personasin4;
    }
    ///////////////////////////////////////// For population
    public ArrayList<populationClass> getPopulation() throws SQLException {
        ArrayList<populationClass> populations = new ArrayList<>();
        try {
            // Connect to JDBC data base
            Connection connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select * from population";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String lga_code = results.getString("lga_code");
                String lga_year = results.getString("lga_year");
                String indi_status = results.getString("indigenous_status");
                String sex = results.getString("sex");
                String age_category = results.getString("age_category");
                String count = results.getString("count");
                String age_min = results.getString("age_min");
                String age_max = results.getString("age_max");

                // Create a population Object
                populationClass pop = new populationClass(lga_code, lga_year, indi_status, sex, age_category, count, age_min, age_max);
                populations.add(pop);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just print the error
            System.err.println(e.getMessage());
            throw e;
        }
        return populations;
    }
    ///////////////////////////////////////// For lga population
    public ArrayList<populationClass> getPopulation(String lgacode){
        ArrayList<populationClass> populations = new ArrayList<>();
        try {
            // Connect to JDBC data base
            Connection connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select * from population where lga_code = ?1";
            query = query.replace("?1",lgacode);            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String lga_code = results.getString("lga_code");
                String lga_year = results.getString("lga_year");
                String indi_status = results.getString("indigenous_status");
                String sex = results.getString("sex");
                String age_category = results.getString("age_category");
                String count = results.getString("count");
                String age_min = results.getString("age_min");
                String age_max = results.getString("age_max");

                populationClass pop = new populationClass(lga_code, lga_year, indi_status, sex, age_category, count, age_min, age_max);
                populations.add(pop);
            }
        }catch (SQLException e) {
            // If there is an error, lets just print the error
            System.err.println(e.getMessage());
        }
        return populations;
    }

    


    /* 
    public static void main(String[] args) {   ////////////// file nay dung de test xem database co connect duoc hay ko
        Level_1 level1 = new Level_1();
        ArrayList<personaClass> personas = level1.getPersona();
        for (personaClass persona : personas) {
            System.out.println("Name: " + persona.getPersonaName());
            System.out.println("Quote: " + persona.getPersonaQuote());
            System.out.println("Age: " + persona.getPersonaAge());
            System.out.println("Description: " + persona.getPersonaBackground());
            System.out.println("Background: " + persona.getPersonaDescription());
            System.out.println("Need1: " + persona.getPersonaNeed1());
            System.out.println("Need2: " + persona.getPersonaNeed2());
            System.out.println("Pain1: " + persona.getPersonaPain1());
            System.out.println("Pain2: " + persona.getPersonaPain2());
            System.out.println("Skills: " + persona.getPersonaSkills());
            System.out.println("-----------------------------");
        }
    }
    */
    public static void main(String[] args) {   ////////////// file nay dung de test xem database co connect duoc hay ko
        Level_1 level1 = new Level_1();
        ArrayList<memberClass> members = level1.getMember();
        for (memberClass member : members) {
            System.out.println("ID: " + member.getsID());
            System.out.println("Name: " + member.getFull_name());
            System.out.println("Gender: " + member.getGender());
            System.out.println("-----------------------------");
        }
    }
    
    
}
