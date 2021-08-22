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
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author mn170387d
 */
public class mn170387_DistrictOperations implements DistrictOperations {

    @Override
    public int insertDistrict(String naziv, int idGrad, int xkoor, int ykoor) {
         boolean failure = true;
        int id=-1;
        Connection conn = DB.getInstance().getConnection();
        String query = "INSERT INTO dbo.Opstina(Naziv, idGrad, Xkoor, Ykoor) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);){
            stmt.setString(1, naziv);
            stmt.setInt(2, idGrad);
            stmt.setInt(3, xkoor);
            stmt.setInt(4, ykoor);
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
    public int deleteDistricts(String... nazivi) {
        Connection conn = DB.getInstance().getConnection();
        String query = "DELETE FROM dbo.Opstina WHERE Naziv=?";
        int cnt = 0;
        for (String naziv : nazivi){
            try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setString(1, naziv);
                int ret = stmt.executeUpdate();
                cnt+=ret;
            } catch (SQLException ex) {
               // Logger.getLogger(mn170387_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return cnt; 
    }

    @Override
    public boolean deleteDistrict(int idOpstina) {
        Connection conn = DB.getInstance().getConnection();
        int ret=-1;
        String query = "DELETE FROM dbo.Opstina WHERE IdOpstina=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, idOpstina);
            ret = stmt.executeUpdate();
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(ret==1)
            return true;
        else return false;
    }

    @Override
    public int deleteAllDistrictsFromCity(String nazivGrada) {
        Connection conn = DB.getInstance().getConnection();
        int idGrad  = -1;
        int ret = 0;
        String query1 = "SELECT IdGrad from Grad where Naziv='"+nazivGrada+"'";
        
        try (PreparedStatement stmt = conn.prepareStatement(query1);){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                idGrad = rs.getInt(1);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(mn170387_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String query2 = "DELETE FROM Opstina WHERE IdGrad="+idGrad;
        try (PreparedStatement stmt = conn.prepareStatement(query2);){
            ret = stmt.executeUpdate();
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int idGrad) {
        List<Integer> ret = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String query2 = "SELECT IdOpstina from Opstina where IdGrad="+idGrad;
        try (PreparedStatement stmt = conn.prepareStatement(query2);){
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getInt(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> ret = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String query2 = "SELECT IdOpstina from Opstina";
        try (PreparedStatement stmt = conn.prepareStatement(query2);){
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getInt(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    
}
