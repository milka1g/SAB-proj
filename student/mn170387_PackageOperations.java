/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author mn170387d
 */
public class mn170387_PackageOperations implements PackageOperations {
    

    private static HashMap<String, MyPair<Integer, Integer>> poslObidjeno = new HashMap<>();
    
    static double euclidean(final int x1, final int y1, final int x2, final int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
    static BigDecimal getPackagePrice(final int type, final BigDecimal weight, final double distance, BigDecimal percentage) {
        percentage = percentage.divide(new BigDecimal(100));
        switch (type) {
            case 0: {
                return new BigDecimal(10.0 * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 1: {
                return new BigDecimal((25.0 + weight.doubleValue() * 100.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 2: {
                return new BigDecimal((75.0 + weight.doubleValue() * 300.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
        boolean failure = true;
        int id=0;
        Connection conn = DB.getInstance().getConnection();
        String query = "INSERT INTO dbo.Paket(OpstinaOd,OpstinaDo,Korisnik,TipPak,Tezina) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);){
            stmt.setInt(1, districtFrom);
            stmt.setInt(2, districtTo);
            stmt.setString(3, userName);
            stmt.setInt(4, packageType);
            stmt.setBigDecimal(5, weight);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                failure = false;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        if(failure==false){
                String query2 = "UPDATE Korisnik SET BrPoslatihPaketa=BrPoslatihPaketa+1 where KorIme=?";
            try (PreparedStatement stmt = conn.prepareStatement(query2);){
                stmt.setString(1, userName);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(failure==false)
            return id;
        else return -1;
    }

    @Override
    public int insertTransportOffer(java.lang.String couriersUserName, int packageId, java.math.BigDecimal pricePercentage) {
        boolean failure = true;
        int id=0;
        Connection conn = DB.getInstance().getConnection();
        String query = "INSERT INTO dbo.PonudaZaVoznju(KurirKorime,IdPaket,Procenat) VALUES (?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);){
            stmt.setString(1, couriersUserName);
            stmt.setInt(2, packageId);
            stmt.setBigDecimal(3, pricePercentage);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                failure = false;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(failure==false)
            return id;
        else return -1;
    }

    @Override
    public boolean acceptAnOffer(int idOffer) {
        BigDecimal procenat=BigDecimal.ZERO, tezina=BigDecimal.ZERO, cena=BigDecimal.ZERO;
        int idpaket=-1, tip=-1;
        String korime="";
        double distanca=0;
        int opsOd=-1, opsDo=-1, x1=0,x2=0,y1=0,y2=0;

        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT Procenat,KurirKorime,IdPaket from PonudaZaVoznju where IdPonuda=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, idOffer);
           ResultSet rs = stmt.executeQuery();
           if(rs.next()){
              procenat = rs.getBigDecimal(1);
              korime = rs.getString(2);
              idpaket = rs.getInt(3);
           } else {
               return false;
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //dohvati za taj paket tezinu, od, do, tip
        String query2 = "SELECT Tezina,OpstinaOd,OpstinaDo,TipPak from Paket where IdPaket=?";
        try (PreparedStatement stmt = conn.prepareStatement(query2);){
            stmt.setInt(1, idpaket);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                tezina = rs.getBigDecimal(1);
                opsOd = rs.getInt(2);
                opsDo = rs.getInt(3);
                tip = rs.getInt(4);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String query3 = "SELECT Xkoor, Ykoor from Opstina where IdOpstina=?";
        try (PreparedStatement stmt = conn.prepareStatement(query3);){
            stmt.setInt(1, opsOd);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                x1 = rs.getInt(1);
                y1 = rs.getInt(2);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String query4 = "SELECT Xkoor, Ykoor from Opstina where IdOpstina=?";
        try (PreparedStatement stmt = conn.prepareStatement(query4);){
            stmt.setInt(1, opsDo);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                x2 = rs.getInt(1);
                y2 = rs.getInt(2);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        distanca = euclidean(x1, y1, x2, y2);

        cena = getPackagePrice(tip, tezina, distanca, procenat);
        
        //delay
        double j = 1;
        for(long i = 0; i < 10000000;i++){
             j += i*Math.PI/3;
        }
        
        String query5 = "UPDATE Paket SET Kurir=?, Cena=?, StatusIsporuke=1, VremePrihv=CURRENT_TIMESTAMP where IdPaket=?";
        try (PreparedStatement stmt = conn.prepareStatement(query5);){
            stmt.setString(1, korime);
            stmt.setBigDecimal(2,cena);
            stmt.setInt(3,idpaket);
            int ret = stmt.executeUpdate();
            return ret==1;
        } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }


    @Override
    public List<Integer> getAllOffers() {
        List<Integer> ret = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT IdPonuda from PonudaZaVoznju";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getInt(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
        List<Pair<Integer, BigDecimal>> ret = new ArrayList<Pair<Integer, BigDecimal>>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT IdPonuda,Procenat from PonudaZaVoznju where IdPaket=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
           stmt.setInt(1, packageId);
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               Pair p = new MyPair<Integer, BigDecimal>(rs.getInt(1),rs.getBigDecimal(2));
               ret.add(p);
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public boolean deletePackage(int packageId) {
        Connection conn = DB.getInstance().getConnection();
        int ret=-1;
        String query = "DELETE FROM dbo.Paket WHERE IdPaket=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, packageId);
            ret = stmt.executeUpdate();
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(ret==1)
            return true;
        else return false;
    }

    @Override
    public boolean changeWeight(int packageId, java.math.BigDecimal newWeight) {
        boolean ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "UPDATE Paket SET Tezina=? WHERE IdPaket=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setBigDecimal(1, newWeight);
            stmt.setInt(2, packageId);
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret; 
    }

    @Override
    public boolean changeType(int packageId, int newType) {
        boolean ret = false;
        Connection conn = DB.getInstance().getConnection();
        String query = "UPDATE Paket SET TipPak=? WHERE IdPaket=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, newType);
            stmt.setInt(2, packageId);
            if(stmt.executeUpdate() == 1){
                ret = true;
            }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret; 
    }

    @Override
    public Integer getDeliveryStatus(int packageId) {
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT StatusIsporuke from Paket where IdPaket=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, packageId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1); //PROVERI OVO KAKO RADI 
            } else {
                return null;
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT Cena from Paket";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getBigDecimal(1);  //PROVERI OVO KAKO RADI 
            } else {
                return null;
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT VremePrihv from Paket";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getDate(1);  //PROVERI OVO KAKO RADI 
            } else {
                return null;
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(mn170387_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int i) {
        List<Integer> ret = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT IdPaket from Paket where TipPak=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, i);
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getInt(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<Integer> getAllPackages() {
        List<Integer> ret = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT IdPaket from Paket";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getInt(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<Integer> getDrive(String courierUsername) {
        List<Integer> ret = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT IdPaket from Paket where Kurir=? and StatusIsporuke=2";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, courierUsername);
           ResultSet rs = stmt.executeQuery();
           while(rs.next()){
               ret.add(rs.getInt(1));
           }
        } catch (SQLException ex) {
           // Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public int driveNextPackage(String courierUsername) {
        int statusKurira = -1;
        String regbr = "";
        int cntKurira = -1;
        
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT Status,RegBr from Kurir where KorIme=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1,courierUsername);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                statusKurira = rs.getInt(1);
                regbr = rs.getString(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(statusKurira==0){
            //proveravamo da li neko vozi nasa kola
            String query2 = "SELECT COUNT(*) from Kurir where Status=1 and RegBr=?";
            try (PreparedStatement stmt = conn.prepareStatement(query2);){
                stmt.setString(1,regbr);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    cntKurira = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(cntKurira>0){
                return -2;
            }
            //mozemo da vozimo, nas status se stavi na 1
            String query3 = "UPDATE Kurir SET Status=1 where KorIme=?";
            try (PreparedStatement stmt = conn.prepareStatement(query3);){
                stmt.setString(1,courierUsername);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            //sve nase pakete sa 1 na 2 statusisporuke paketa
            String query4 = "UPDATE Paket SET StatusIsporuke=2 where Kurir=? and StatusIsporuke=1";
            try (PreparedStatement stmt = conn.prepareStatement(query4);){
                stmt.setString(1,courierUsername);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            poslObidjeno.put(courierUsername, new MyPair(-1,-1));

        } 
                //dostaviti najstariji VremePrihv paket
               
        int top1idpaket = -5;
        String query5 = "SELECT TOP(1) IdPaket from Paket where Kurir=? and StatusIsporuke=2 ORDER BY VremePrihv ASC";
            try (PreparedStatement stmt = conn.prepareStatement(query5);){
                stmt.setString(1, courierUsername);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    top1idpaket = rs.getInt(1);
                   // System.out.println("DOSTAVA "+top1idpaket);
                } else {
                     //proveriti da li ima jos paketa za isporuku (status =2 )
                    //ako nema vratiti status kurira na 0
                    String query7 = "UPDATE Kurir SET Status=0 where KorIme=?";
                       try (PreparedStatement stmt2 = conn.prepareStatement(query7);){
                           stmt2.setString(1, courierUsername);
                           stmt2.executeUpdate();
                       }catch (SQLException ex) {
                            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    return -1;
                }
            } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            //isporucujemo paket status 2->3
        String query6 = "UPDATE Paket SET StatusIsporuke=3 where IdPaket=?";
            try (PreparedStatement stmt = conn.prepareStatement(query6);){
                stmt.setInt(1, top1idpaket);
                stmt.executeUpdate();
            } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            //povecamo broj isporucenih paketa

            String query8 = "UPDATE Kurir SET BrIsporucenihPaketa=BrIsporucenihPaketa+1 where KorIme=?";
                       try (PreparedStatement stmt2 = conn.prepareStatement(query8);){
                           stmt2.setString(1, courierUsername);
                           stmt2.executeUpdate();
                       }catch (SQLException ex) {
                            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
            //SRACUNAJ PROFIT
           // System.out.println("ID PAK " + top1idpaket);
            int x1=-1, y1=-1, x2=-1, y2=-1, opsOd=-1, opsDo=-1;
            String query9 = "SELECT OpstinaOd, OpstinaDo FROM Paket where IdPaket=?";
            try (PreparedStatement stmt = conn.prepareStatement(query9);){
                stmt.setInt(1, top1idpaket);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){ 
                    opsOd = rs.getInt(1);
                    opsDo = rs.getInt(2);
                }
             } catch (SQLException ex) {
                    Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
             }
            
            x1 = koorOps(true, opsOd);
            y1 = koorOps(false, opsOd);
            x2 = koorOps(true, opsDo);
            y2 = koorOps(false, opsDo);
            
            BigDecimal profit = BigDecimal.ZERO;

            MyPair posl = poslObidjeno.get(courierUsername);
            if((int)posl.getFirstParam()==-1 && (int)posl.getSecondParam()==-1){
                //kad smo -1 -1
                double distanca = euclidean(x1, y1, x2, y2);
                profit = profit(distanca,courierUsername,regbr, top1idpaket);
                
            } else {
                //dohvati poslenje iz mape pa sracunaj od poslednjeg do 
                int xp = (int)posl.getFirstParam();
                int yp  = (int)posl.getSecondParam();
                
                double distanca = euclidean(xp, yp, x1, y1);
                distanca += euclidean(x1, y1, x2, y2);
                profit = profit(distanca,courierUsername,regbr, top1idpaket);


            }
            
        String query12 = "UPDATE Kurir SET Profit=Profit+? where KorIme=?";
            try (PreparedStatement stmt = conn.prepareStatement(query12);){
                stmt.setBigDecimal(1, profit);
                stmt.setString(2, courierUsername);
                stmt.executeUpdate();
             } catch (SQLException ex) {
                    Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
             }
            
            
            String query15 = "SELECT TOP(1) IdPaket from Paket where Kurir=? and StatusIsporuke=2 ORDER BY VremePrihv ASC";
            try (PreparedStatement stmt = conn.prepareStatement(query15);){
                stmt.setString(1, courierUsername);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    
                } else {
                     //proveriti da li ima jos paketa za isporuku (status =2 )
                    //ako nema vratiti status kurira na 0
                    String query17 = "UPDATE Kurir SET Status=0 where KorIme=?";
                       try (PreparedStatement stmt2 = conn.prepareStatement(query17);){
                           stmt2.setString(1, courierUsername);
                           stmt2.executeUpdate();
                       }catch (SQLException ex) {
                            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                   
                }
            } catch (SQLException ex) {
            Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

            poslObidjeno.put(courierUsername, new MyPair(x2, y2));
            
        return top1idpaket;
    }
    
    BigDecimal profit(double distanca, String kurir, String regbr, int idpak){
        //izvuci cenu za paket
        int tipGoriva=-1, cenaGoriva=-1;
        BigDecimal cenaPuta = BigDecimal.ZERO;
        BigDecimal potrosnja = BigDecimal.ZERO, cenaPaketa = BigDecimal.ZERO;
        Connection conn = DB.getInstance().getConnection();
        String query = "SELECT Cena FROM Paket where IdPaket=?";
            try (PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setInt(1, idpak);
                ResultSet rs = stmt.executeQuery();
                if(rs.next())
                cenaPaketa = rs.getBigDecimal(1);
                else 
                    return BigDecimal.ZERO;
             } catch (SQLException ex) {
                    Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
             }
        
         //potrosnja za nase auto
         String query2 = "SELECT Potrosnja, TipGoriva FROM Vozilo where RegBr=?";
            try (PreparedStatement stmt = conn.prepareStatement(query2);){
                stmt.setString(1, regbr);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    potrosnja = rs.getBigDecimal(1);
                    tipGoriva = rs.getInt(2);
                } else return BigDecimal.ZERO;
             } catch (SQLException ex) {
                    Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
             }
        
          if(tipGoriva==0){
              cenaGoriva = 15;
          } else if(tipGoriva==1){
              cenaGoriva = 32;
          } else if(tipGoriva==2){
              cenaGoriva = 36;
          }
        cenaPuta = cenaPaketa.subtract(BigDecimal.valueOf(distanca).multiply(potrosnja).multiply(BigDecimal.valueOf(cenaGoriva)));
         
        return cenaPuta;
    }
    
    int koorOps(boolean isX, int ops){
        int x=-1, y=-1;
        Connection conn = DB.getInstance().getConnection();
        String query11 = "SELECT Xkoor, Ykoor from Opstina where IdOpstina=?";
            try (PreparedStatement stmt = conn.prepareStatement(query11);){
                stmt.setInt(1, ops);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    x = rs.getInt(1);
                    y = rs.getInt(2);
                } 
            } catch (SQLException ex) {
                Logger.getLogger(mn170387_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        if(isX)
            return x;
        else 
            return y;
    }
    
}
