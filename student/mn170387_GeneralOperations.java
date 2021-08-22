/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author mn170387d
 */
public class mn170387_GeneralOperations implements GeneralOperations {

    @Override
    public void eraseAll() {
        Connection conn=DB.getInstance().getConnection();
        String [] tables = {"dbo.PonudaZaVoznju", "dbo.ZahtevZaKurira","dbo.Paket",   "dbo.Kurir", "dbo.Admin","dbo.Vozilo", "dbo.Korisnik","dbo.Opstina","dbo.Grad"};
        for (String table : tables) {
            try (PreparedStatement stmt=conn.prepareStatement("delete from " + table);){                
                stmt.executeUpdate();
            }catch (SQLException ex) {
                Logger.getLogger(mn170387_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
    }
    
}
