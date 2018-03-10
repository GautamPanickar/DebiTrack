/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author arun
 */

// This class maintain all tha database activities
public class DataBaseManager {
    
    private String szDriver          = null;
    private String szDomainAddress   = null;    // Hold domain address for connection
    private String szUserName        = null;
    private String szDomainPassword  = null;    // Hold database password
    
    // connection and query execution variables.
    private Connection          con  = null;
    private PreparedStatement   ps   = null;
    private ResultSet           rs   = null;

    // constructor which initialize 
    // database driver
    // domain address
    // user name of database
    // password
    public DataBaseManager() throws ClassNotFoundException, SQLException 
    {
        szDriver          = "com.mysql.jdbc.Driver";
        szDomainAddress   = "jdbc:mysql://localhost:3306/DebitManagerdb";
        szUserName        = "DebitManager";
        szDomainPassword  = "DebitManagerDroid";
        
        // function to init connection settings
        SetConection();
        
    }
    
    // Connection management
    /***************************************************************************************************/
    // function create a my sql connection
    private void  SetConection()
    {
        try
        {
            // creating a database connection.
            Class.forName( szDriver );
            con=DriverManager.getConnection( szDomainAddress, szUserName, szDomainPassword );
        }
        // database connection error
        catch( SQLException ex )
        {
            JOptionPane.showMessageDialog(null, " Please check the database connection ");
        }
        // driver error
        catch( ClassNotFoundException ex )
        {
            JOptionPane.showMessageDialog(null, " Database driver problem  ");
        }
        
    }
    
    // function to close the connection
    public void closeConection() throws SQLException
    {
        con.close();
    }
    
    
    // public functions to manage database operations
    /*******************************************************************************************************/
    
    // function to check user name and password.
    // if both matches it returns user type otherwise return null.
    public String getUserType( String szUserName /*IN*/ , String szPassword /*IN*/ )
    {
        String szUserType = null; // user type admin / staff ? -> return from function
        String szQuery    = null; // sql query string
        
        // validation of empty username and password
        if( null != szUserName && null != szPassword )
        {
            try {
                
                // sql query to check user name and password
                szQuery = " select  userPrivilage from LoginTable where userName = ? and userPassword = ? ";
                // prepare sql query
                ps = con.prepareStatement( szQuery );
                ps.setString( 1, szUserName );
                ps.setString( 2, szPassword );
                // get result of query
                rs = ps.executeQuery();
                
                // user name and password match.
                // set szUser type as user privilage.
                if( rs.next() )
                {
                    szUserType = rs.getString( "userPrivilage" );
                }
            
            
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return szUserType;
    }
    
   //***********************************************************************************************
    
   // function to store new registered user details to the database
    public String SetCustomerDetails( String szName       /*IN*/,
                                   String szAddress    /*IN*/,
                                   String szPhone      /*IN*/,
                                   String szRemark     /*IN*/,
                                   String szRecomenter /*IN*/ ) 
    {
        
        
        String szQuery    = null; // sql query string
        String autoId     = null; // auto generated id return from database after insertion.
        int  nStatus      = 0;
        if( null != szName && null != szAddress && null!= szPhone )
        {
            try
            {
            // insert query
            szQuery = " insert into CustomerTable(CustName, CustAddress, CustPhone, Remark, Recomenter )"
                    + " values( ?, ?, ?, ?, ? )"; 
            
            // prepare sql query
            ps = con.prepareStatement( szQuery,Statement.RETURN_GENERATED_KEYS );
            // set arguments
            ps.setString( 1, szName );
            ps.setString( 2, szAddress );
            ps.setString( 3, szPhone );
            ps.setString( 4, szRemark );
            ps.setString( 5, szRecomenter );
            //execute query
            nStatus = ps.executeUpdate();
            
            if( 1 == nStatus )
            {
                rs = ps.getGeneratedKeys();
                if( rs.next() )
                {
                    autoId = rs.getString(1);
                }
            }
           
            }
            catch( SQLException ex )
            {
                JOptionPane.showMessageDialog( null,ex.getMessage() );
            }
            
            
            
        }
        
        return autoId;
        
    }
    
 //**************************************************************************************
    
    //function to select a customer details
    public ResultSet getCustomerDetails( String szCustId )
    {
        if( null != szCustId )
        {
            String szQuery = null;
            szQuery = " select CustId,"
                        + "CustName,"
                        + "CustAddress,"
                        + "CustPhone"
                
                + " from CustomerTable"
                + " where custId = ?";
       
            rs = null;
            try {
                ps=con.prepareStatement( szQuery );
                ps.setString( 1, szCustId );
                rs=ps.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

        return rs;
        
    }
    
 //*****************************************************************************************   

    public ResultSet getAllCustomers() 
    {
        
        
        String szQuery = null;
        szQuery = " select CustId      as CUSTOMER_ID,"
                        + "CustName    as CUSTOMER_NAME,"
                        + "CustAddress as ADDRESS,"
                        + "CustPhone   as PHONE,"
                        + "Remark      as REMARKS,"
                        + "Recomenter  as RECOMENTED_BY"
                
                + " from CustomerTable"
                + " order by CustName asc";
       
        rs = null;
        try {
            ps=con.prepareStatement( szQuery );
            rs=ps.executeQuery();
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return rs;
    }

//**************************************************************************************************

    public ResultSet GetCustomerDetails( String szSearString ) 
    {
        String szQuery = null;
        szQuery = "select CustId as CUSTOMER_ID,"
                       + "CustName as CUST_NAME,"
                       + "CustAddress as CUST_Address"
                + " from CustomerTable where CustName like ? ";
       
        rs = null;
        try {
            ps=con.prepareStatement( szQuery );
            ps.setString( 1, szSearString + '%' );
            
            rs=ps.executeQuery();
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return rs;
    
    
    }

 //***********************************************************************************************************   
    
    
    public int SetPurchaseDetails( String custId, 
                                       String Remark, 
                                       Date   PurchaseDate, 
                                       Date   DueDate, 
                                       String Amount ) 
    
    {
        
        int  nStatus    = 0;
        String  szQuery = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String szDate   = null;
        try
            {
            // insert query
            szQuery = " insert into purchasetable(Custid, StartDate, DueDate, Amount, PurchaseRemark )"
                    + " values( ?, ?, ?, ?, ? )"; 
            
            // prepare sql query
            ps = con.prepareStatement( szQuery );
            // set arguments
            ps.setString( 1, custId );
            
            szDate = format.format( PurchaseDate );
            ps.setString( 2, szDate );
            
            szDate = format.format( DueDate );
            ps.setString( 3, szDate  );
            ps.setString( 4, Amount );
            ps.setString( 5, Remark );
            //execute query
            nStatus = ps.executeUpdate();
           
            }
            catch( SQLException ex )
            {
                JOptionPane.showMessageDialog( null,ex.getMessage() );
            }
                 
        
    
    
       return nStatus;
    
    }
    
//*******************************************************************************************************
   
    public ResultSet getPurchaseDetails(String custId /*IN*/ ) 
    {
        
        String szQuery = " select purchaseId, StartDate,DueDate,Amount,purchaseRemark,status"
                       + " from purchasetable"
                       + " where custId = ? ";
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );
            ps.setString( 1, custId );
            rs = ps.executeQuery();
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return rs;
        
        
        
    }

 //*********************************************************************************************************   
    

    public ResultSet getTransactions(String custId ) 
    {
        String szQuery = " select Amount_remited,date"
                       + " from transactions"
                       + " where custId = ? ";
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );
            ps.setString( 1, custId );
            //ps.setString( 2, PurchaseId);
            rs = ps.executeQuery();
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return rs;
        
    
    }

//***************************************************************************************************
    
    public ResultSet getTotalAmount(String custId ) 
    {
        String szQuery = " select sum( Amount ) Amount"
                       + " from purchasetable"
                       + " where custId = ?";
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );
           
            ps.setString( 1, custId);
            rs = ps.executeQuery();
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return rs;
    }
    
 //**************************************************************************************************

    public ResultSet getRemitedAmount(String custId ) 
    { 
        String szQuery = " select sum( amount_remited ) as remited"
                       + " from transactions"
                       + " where custId = ?";
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );
           
            ps.setString( 1, custId);
            //ps.setString( 2, purchaseId);
            rs = ps.executeQuery();
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return rs;
    
    
    }
    
 //**************************************************************************************************

    public int AddAmount(String custId, Date date, String amount ) 
    { 
    
        int  nStatus    = 0;
        String  szQuery = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String szDate   = null;
        try
            {
            // insert query
            szQuery = " insert into transactions(Custid, Amount_remited,date )"
                    + " values( ?, ?, ? )"; 
            
            // prepare sql query
            ps = con.prepareStatement( szQuery );
            // set arguments
            ps.setString( 1, custId );
            //ps.setString( 2, purchaseId);
            ps.setString( 2, amount);
            
            szDate = format.format( date );
            ps.setString( 3, szDate );
            
            
            //execute query
            nStatus = ps.executeUpdate();
           
            }
            catch( SQLException ex )
            {
                JOptionPane.showMessageDialog( null,ex.getMessage() );
            }
                 
        
    
    
       return nStatus;
    }
    
  //*****************************************************************************************************  

    public int SetAccountState( String custId ) 
    {  
        int  nStatus    = 0;
        String  szQuery = null;
        
        try
            {
            // insert query
            szQuery = " update purchasetable set status = 'closed' "
                    + " where custId = ?"; 
            
            // prepare sql query
            ps = con.prepareStatement( szQuery );
            // set arguments
            ps.setString( 1, custId );
           
            //execute query
            nStatus = ps.executeUpdate();
           
            }
            catch( SQLException ex )
            {
                JOptionPane.showMessageDialog( null,ex.getMessage() );
            }             
       
        return nStatus;
        
    }    
    
  //********************************************************************************************************  
    
    //function return the all customer id
    public ResultSet getAllCustomerId()
    {
        String szQuery = " select custId "
                       + " from customertable";
                       
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );
            rs = ps.executeQuery();
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return rs;
    }
            
 //*******************************************************************************************************   
    
    // Deletes the mentioned row from the table    
    public String DeleteRow( String custId)     
    {
        
        int nStatus = 0;
        String  szQuery1 = null;
        String  szQuery2 = null;
        String  szQuery3 = null;
        String szQueryStatus = null;
        String openStatus = "open";
        String deleteStatus = null;
        try
        {
            // Delete query query
            szQuery1 = " delete from transactions "
                      +"where custid = ?";
            szQuery2 = " delete from purchasetable "
                      +"where custid = ?";
            szQuery3 = " delete from customertable "
                      +"where custid = ?";
            
            // Query to check the purchase status od the customer
            szQueryStatus = " select COUNT(*) as rowCount from purchasetable "
                            +"where custid = ? "
                            +"and status = ?";
            ps = con.prepareStatement( szQueryStatus );
            ps.setString( 1, custId );
            ps.setString( 2, openStatus );
            rs = ps.executeQuery();
            if( rs.next() )
            {
                // Validating that the customer to be deleted has no open purchase credits
            if(Integer.valueOf(rs.getString( "rowCount" )) == 0)
            {
                // prepare and execute sql query
                ps = con.prepareStatement( szQuery1 );
                ps.setString( 1, custId );
                ps.executeUpdate();
                ps = con.prepareStatement( szQuery2 );
                ps.setString( 1, custId );
                ps.executeUpdate();
                ps = con.prepareStatement( szQuery3 );
                ps.setString( 1, custId );
                nStatus = ps.executeUpdate();
                if(nStatus > 0)
                {
                    deleteStatus = "DELETED";
                }
                else
                {
                    deleteStatus = "NOROWS";
                }
            }
            else
            {
                deleteStatus = "HASOPEN";
            }
            }            
        }
        catch( SQLException ex )
        {
            JOptionPane.showMessageDialog( null,ex.getMessage() );
        }                      
       
       return deleteStatus;
    
    }

 //***********************************************************************************************
    
    // function chage the password of specified user
    
    public int ChangePassword(String szUserName, String szPassword ) 
    {
        int     nStatus = 0;
        String  szQuery = null;
        
        try
            {
            // insert query
            szQuery = " update loginTable set userPassword = ?"
                    + " where userName = ?"; 
            
            // prepare sql query
            ps = con.prepareStatement( szQuery );
            // set arguments
            ps.setString( 2, szUserName );
            ps.setString( 1, szPassword );
           
            //execute query
            nStatus = ps.executeUpdate();
           
            }
            catch( SQLException ex )
            {
                JOptionPane.showMessageDialog( null,ex.getMessage() );
            }
                 
        
    
    
       return  nStatus;
        
        
    }
    
     // The method retrieves the result set having details of customers who have exceeded payment due date
    public ResultSet getPurchaseDetails(Date dueDate) 
    {
        
        String szQuery = " select ct.custid as CustomerId, ct.custname as Name, ct.custaddress as Address, ct.custphone as Phone, pt.duedate as Duedate, pt.purchaseremark as Remark"
                       + " from customertable ct, purchasetable pt"
                       + " where ct.custid = pt.custid and pt.status = 'open' and pt.duedate <= ? order by pt.duedate asc";
        rs = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String szDate   = null;
        try
        {
            ps = con.prepareStatement( szQuery );
            szDate = format.format(dueDate);
            ps.setString( 1, szDate );
            rs = ps.executeQuery();            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }       
    
        return rs;                    
    }
    
    //*********************************************Total purchase amount***************************
    public double getTotalPurchasedAmount()
    {
        double dTotalPurchase = 0;
        String szQuery = " select sum( amount ) as purchased"
                       + " from purchasetable";
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );         

            rs = ps.executeQuery();
            if( rs.next() ){
                
                if( null!=rs.getString("purchased") )
                dTotalPurchase = Double.parseDouble(  rs.getString("purchased"));
            }
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return dTotalPurchase;
        
    }
    
    public double getTotalRemitedAmount()
    {
        double dTotalRemited = 0;
        String szQuery = " select sum( amount_remited ) as remited"
                       + " from transactions";
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );         

            rs = ps.executeQuery();
            if( rs.next() ){
                
                if( null!=rs.getString("remited") )
                dTotalRemited = Double.parseDouble(  rs.getString("remited"));
            }
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return dTotalRemited;
        
    }
    
    
    
    
    //******************************************Save last called date********************************
    public int saveLastCalledDate(int custId, Date lastCalledDate)
    {
        int bStatus = 0;
        String szQuery;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String szDate   = null;
        
        
        try{
          
            szQuery = "INSERT INTO lastcalled (custId, date) VALUES(?,?) ON DUPLICATE KEY UPDATE custId = ?, date = ?";
            ps = con.prepareStatement( szQuery );
            szDate = format.format(lastCalledDate);
            ps.setInt(1,custId);
            ps.setString( 2, szDate );
            ps.setInt(3,custId);
            ps.setString(4, szDate );
            bStatus = ps.executeUpdate();          
        
         }catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        return bStatus;
        
    }
    
    //****************************************return last called date*******************************
    public String getLastCalledDate( String custId )
    {
        
        String lastCalledDate = "";
        String szQuery = " select date as lastCalledDate"
                       + " from lastcalled"
                       + " where custId = ?";
        rs = null;
       
        try {
            ps = con.prepareStatement( szQuery );
           
            ps.setString( 1, custId);
            //ps.setString( 2, purchaseId);
            rs = ps.executeQuery();
            if( rs.next() )
            {
                lastCalledDate = rs.getString("lastCalledDate");
            }
            
       
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return lastCalledDate;
        
    }
    
    //**********************************************Init table*****************************
    public void initTable()
    {
        int bStatus = 0;
        try {
            String query = "create table if not exists lastcalled( custId bigint(20) unsigned,date date)";
            ps = con.prepareStatement( query );
            bStatus = ps.executeUpdate();
            System.out.println(bStatus);
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}



