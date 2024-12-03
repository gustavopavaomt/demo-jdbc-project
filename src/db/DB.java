package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    public static Connection con = null;


    public static Connection getConnection() {
        if(con == null) {
            try{
                Properties prop = loadProps();
                String url = prop.getProperty("burl");
                con = DriverManager.getConnection(url,prop);
                return con;
            }catch (SQLException e){
                throw new DBException(e.getMessage());
            }
        }
        return con;
    }

    public static void closeConnection() {
        if(con != null) {
            try{
                con.close();
            }catch (SQLException e){
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if(stmt != null) {
            try{
                stmt.close();
            }catch (SQLException e){
                throw new DBException(e.getMessage());
            }
        }
    }
    public static void closeResultSet(ResultSet rs) {
        if(rs != null) {
            try{
                rs.close();
            }catch (SQLException e){
                throw new DBException(e.getMessage());
            }
        }
    }

    private static Properties loadProps(){
        try(FileInputStream fs = new FileInputStream("db.properties")){
            Properties props = new Properties();
            props.load(fs);
            return props;
        }catch (IOException e){
            throw new DBException(e.getMessage());
        }
    }
}
