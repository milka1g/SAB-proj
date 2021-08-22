/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author mn170387d
 */
public class mn170387_CourierRequestOperation implements CourierRequestOperation {

    @Override
    public boolean insertCourierRequest(String korime, String regbr) {
        boolean  ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "INSERT INTO dbo.ZahtevZaKurira(KorIme,RegBr) VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, korime);
            stmt.setString(2, regbr);
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         return ret;
    }

    @Override
    public boolean deleteCourierRequest(String korime) {
        Connection conn = DB.getInstance().getConnection();
        String query = "DELETE FROM dbo.ZahtevZaKurira WHERE KorIme=?";
        boolean ret = false;
        try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setString(1, korime);
                if(stmt.executeUpdate() == 1){
                ret = true;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String korime, String regbr) {
         boolean ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "UPDATE ZahtevZaKurira SET RegBr=? WHERE KorIme=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, regbr);
            stmt.setString(2, korime);
            
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public List<String> getAllCourierRequests() {
        List<String> ret = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT * from ZahtevZaKurira";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String i = rs.getString("KorIme");
                ret.add(i);
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public boolean grantRequest(String korime) {
        boolean ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "{ call spOdobriZahtev(?) }";
        try (CallableStatement stmt = conn.prepareCall(query);){
            stmt.setString(1,korime);
            
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }
    
}
