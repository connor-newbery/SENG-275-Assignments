package org.jfree.part_2;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.*;

//This part was written by Charlie, other group members provided input for making this portion more testable if it was needed
public class createBarChartDataset {

    static String query;

    /*
    This acts as a main when the dataset is asked to be created in the appGroup 10 file
     */
    public static CategoryDataset createBarChartDataset(String q){
        query = q;
        Connection c = null;
        DefaultCategoryDataset dataset = null; //since the method create dataset creates the new dataset
                                                //there is no need to create a new one here therefore initialize to null
        try {

            ResultSet set = createBarResultSet(c);
            dataset = createDataset(set);

            if(c != null){
                c.close(); // close the connection established by the createResultSet method
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dataset;
    }

    //helper method to establish databse connection and return a result set - written by Charlie
    static ResultSet createBarResultSet(Connection con) throws SQLException {
        //create database connection
        con = DriverManager.getConnection("jdbc:sqlite:chinook.db");
        Statement stmnt = con.createStatement( );

        //get query and return as a result set
        return stmnt.executeQuery(query);
    }

    //Helper method to filter the result set to extract data we want to chart, without this testing this apps logic
    //would be impossible due to the database dependancy. This dependancy could be moved farther outside this class
    //in order to more closely follow the dependancy injection examples shown in class but I deemed that creating a method
    //within this class was going to be enough to enable proper testing, I was right. - Charlie
    public static DefaultCategoryDataset createDataset(ResultSet set) throws SQLException {
        DefaultCategoryDataset data = new DefaultCategoryDataset();

        int lenThreshold = 40000; //filter out songs longer than this
        while(set.next()){

            String tname = set.getString("Name");
            int length = set.getInt("Milliseconds");

            assert(length > 0): "Song Length must be greater than zero";
            assert(tname != null):"The song must have a name";
            if(length <= lenThreshold){
                data.addValue(length, tname, "Songs");
            }
        }

        return data;
    }


}
