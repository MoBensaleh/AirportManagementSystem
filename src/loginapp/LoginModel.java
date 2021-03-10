package loginapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbUtil.dbConnection;

public class LoginModel {
    Connection connection;

    public static LoginModel user;

    public LoginModel(){
        try{
            this.connection = dbConnection.getConnection();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        if(this.connection == null){
            System.exit(1);
        }
    }


    public boolean isDatabaseConnected(){
        return this.connection != null;
    }

    public boolean isLogin(String id, String pass, String opt) throws Exception{
        PreparedStatement pr = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM login where id = ? and password = ? and representation = ?";

        try {
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, id);
            pr.setString(2, pass);
            pr.setString(3, opt);

            rs = pr.executeQuery();

            boolean boll1;

            return rs.next();
        }
        catch (SQLException ex) {
            return false;
        }
        finally {
            pr.close();
            rs.close();

        }
    }
    public String getID(){
        return user.getID();
    }

}
