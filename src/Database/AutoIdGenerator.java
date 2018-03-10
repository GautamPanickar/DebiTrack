/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arun
 */
public class AutoIdGenerator {
    
    private String szCustId     = null; // Hold the customer id generated.
    private String szPurchaseId = null; // Hold the purchase id .
    
    
    public AutoIdGenerator()
    {
        szCustId      = "c";
        szPurchaseId  = "p";
    }
    
    public String getCustomerId()
    {
        /*boolean custUpdateStatus      = false;
        boolean purchaseUpdateStatus  = false;
        
        try {
            DataBaseManager manager = new DataBaseManager(); 
            
            szCustId = szCustId + manager.getCustCode() + manager.getCustNumber();
            custUpdateStatus     = manager.updateCustCode();
            purchaseUpdateStatus = manager.updateCustNumber();
            
            if(szCustId.length() <= 1)
            {
                szCustId = null;
            }
            
                    
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(AutoIdGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AutoIdGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        return szCustId;
        
    }
    
}
