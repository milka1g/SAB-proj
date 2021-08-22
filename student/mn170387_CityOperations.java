/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author mn170387d
 */
public class mn170387_CityOperations implements CityOperations {

    @Override
    public int insertCity(String naziv, String posbr) {
        boolean failure = true;
        int id=0;
        Connection conn = DB.getInstance().getConnection();
        String query = "INSERT INTO dbo.Grad(PostanskiBr,Naziv) VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);){
            stmt.setString(1, posbr);
            stmt.setString(2, naziv);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                failure = false;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(failure==false)
            return id;
        else return -1;
    }

    @Override
    public int deleteCity(String... gradovi) {
        Connection conn = DB.getInstance().getConnection();
        String query = "DELETE FROM dbo.Grad WHERE Naziv=?";
        int cnt = 0;
        for (String grad : gradovi){
            try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setString(1, grad);
                int ret = stmt.executeUpdate();
                cnt+=ret;
            } catch (SQLException ex) {
               // Logger.getLogger(mn170387_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return cnt;
    }

    @Override
    public boolean deleteCity(int idGrad) {
        Connection conn = DB.getInstance().getConnection();
        int ret=-1;
        String query = "DELETE FROM dbo.Grad WHERE IdGrad=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, idGrad);
            ret = stmt.executeUpdate();
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(ret==0)
            return false;
        else return true;  
    }

    @Override
    public List<Integer> getAllCities() {
        List<Integer> ret = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT * from Grad";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Integer i = rs.getInt("IdGrad");
                ret.add(i);
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }
    
}
