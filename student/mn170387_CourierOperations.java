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
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author mn170387d
 */
public class mn170387_CourierOperations implements CourierOperations {

    @Override
    public boolean insertCourier(String korime, String regbr) {
        boolean  ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into Kurir(KorIme,RegBr,BrIsporucenihPaketa,Status) values(?,?,?,?);";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, korime);
            stmt.setString(2, regbr);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         return ret;
    }

    @Override
    public boolean deleteCourier(String korime) {
        Connection conn = DB.getInstance().getConnection();
        String query = "DELETE FROM dbo.Kurir WHERE KorIme=?";
        boolean ret = false;
        try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setString(1, korime);
                if(stmt.executeUpdate() == 1){
                ret = true;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<String> getCouriersWithStatus(int status) {
        List<String> ret = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        String query2 = "SELECT KorIme from Kurir WHERE Status=?";
        try (PreparedStatement stmt = conn.prepareStatement(query2);){
            stmt.setInt(1, status);
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getString(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> ret = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        String query2 = "SELECT KorIme from Kurir";
        try (PreparedStatement stmt = conn.prepareStatement(query2);){
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getString(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int brIsporuka) {
        BigDecimal ret= BigDecimal.ZERO;
        int cnt = 0;
        Connection conn = DB.getInstance().getConnection();
        String query2 = "SELECT Profit from Kurir where BrIsporucenihPaketa>?";
        try (PreparedStatement stmt = conn.prepareStatement(query2);){
            stmt.setInt(1, brIsporuka);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                cnt++;
                ret = ret.add(rs.getBigDecimal(1));
            }
        } catch (SQLException ex) {
            //Logger.getLogger(mn170387_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ret = ret.divide(BigDecimal.valueOf(cnt));
        return ret;
        


    }
    
}
