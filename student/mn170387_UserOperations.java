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
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author mn170387d
 */
public class mn170387_UserOperations implements UserOperations {

    @Override
    public boolean insertUser(String korime, String ime, String prezime, String sifra) {
        boolean  ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "INSERT INTO dbo.Korisnik(KorIme,Ime,Prezime,Sifra ) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);){
            stmt.setString(1, korime);
            stmt.setString(2, ime);
            stmt.setString(3, prezime);
            stmt.setString(4, sifra);
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         return ret;
    }

    @Override
    public int declareAdmin(String korime) {
        int ret = 0;
        boolean postoji = false;
        boolean vecAdmin = false;

        Connection conn = DB.getInstance().getConnection();
        
        String query1 = "SELECT * from Korisnik where KorIme=?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1);){
            stmt1.setString(1, korime);
            ResultSet rs1 = stmt1.executeQuery();
            while(rs1.next()){
                String k = rs1.getString("KorIme");
                if(k.equals(korime)){
                    postoji = true;
                }
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(!postoji){
            ret = 2; //korisnik ne postoji
            return ret;
        }
        
        String query2 = "SELECT * from Admin where KorIme=?";
        try (PreparedStatement stmt2 = conn.prepareStatement(query2);){
            stmt2.setString(1, korime);
            ResultSet rs2 = stmt2.executeQuery();
            while(rs2.next()){
                String k = rs2.getString("KorIme");
                if(k.equals(korime)){
                    vecAdmin = true;
                }
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(vecAdmin){
            ret = 1; //vec admin
            return ret;
        }
        
        String query = "INSERT INTO dbo.Admin(KorIme) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, korime);
            if(stmt.executeUpdate() == 1){
                ret = 0; //uspeh
                return ret;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public Integer getSentPackages(String... korImena) {
        Connection conn = DB.getInstance().getConnection();
        int idGrad  = -1;
        int ret = 0;
        boolean postoji = false;
        
        for(String korime : korImena){
            String query1 = "SELECT * from Korisnik where KorIme=?";
            try (PreparedStatement stmt1 = conn.prepareStatement(query1);){
                stmt1.setString(1, korime);
                ResultSet rs1 = stmt1.executeQuery();
                while(rs1.next()){
                    String k = rs1.getString("KorIme");
                    if(k.equals(korime)){
                        postoji = true;
                    }
                }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
        if(!postoji){
            return null;
        }
        
        
        String query = "SELECT BrPoslatihPaketa from Korisnik where KorIme=?";
     
        for (String korime : korImena){
         
            try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setString(1, korime);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    ret += rs.getInt(1);
                }
            } catch (SQLException ex) {
               // Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        return ret;
    }

    @Override
    public int deleteUsers(String... korimena) {
        Connection conn = DB.getInstance().getConnection();
        String query = "DELETE FROM dbo.Korisnik WHERE KorIme=?";
        int cnt = 0;
        for (String korime : korimena){
            try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setString(1, korime);
                int ret = stmt.executeUpdate();
                cnt+=ret;
            } catch (SQLException ex) {
               // Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return cnt;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> ret = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT * from Korisnik";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String i = rs.getString("KorIme");
                ret.add(i);
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }
    
}
