/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author mn170387d
 */
public class mn170387_VehicleOperations implements VehicleOperations {

    @Override
    public boolean insertVehicle(String regbr, int tip, BigDecimal potrosnja) {
        boolean failure = true;
        int id=0;
        Connection conn = DB.getInstance().getConnection();
        String query = "INSERT INTO dbo.Vozilo(RegBr,TipGoriva,Potrosnja) VALUES (?,?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);){
            stmt.setString(1, regbr);
            stmt.setInt(2, tip);
            stmt.setBigDecimal(3, potrosnja);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                failure = false;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(failure)
            return false;
        else 
            return true;
    }

    @Override
    public int deleteVehicles(String... regBrojevi) {
        Connection conn = DB.getInstance().getConnection();
        String query = "DELETE FROM dbo.Vozilo WHERE RegBr=?";
        int cnt = 0;
        for (String regbr : regBrojevi){
            try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setString(1, regbr);
                int ret = stmt.executeUpdate();
                cnt+=ret;
            } catch (SQLException ex) {
               // Logger.getLogger(mn170387_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return cnt;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> ret = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT * from Vozilo";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String i = rs.getString("RegBr");
                ret.add(i);
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public boolean changeFuelType(String regbr, int tip) {
        boolean ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "UPDATE Vozilo SET TipGoriva=? WHERE RegBr=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, tip);
            stmt.setString(2, regbr);
            
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public boolean changeConsumption(String regbr, BigDecimal potrosnja) {
        boolean ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "UPDATE Vozilo SET Potrosnja=? WHERE RegBr=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setBigDecimal(1, potrosnja);
            stmt.setString(2, regbr);
            
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }
    
}
