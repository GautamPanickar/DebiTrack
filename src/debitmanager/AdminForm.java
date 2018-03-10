/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package debitmanager;

import Database.DataBaseManager;
// import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.print.PrinterException;
import java.beans.Visibility;
import static java.sql.JDBCType.NULL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;


/**
 *
 * @author arun
 */
public class AdminForm extends javax.swing.JFrame {

    /**
     * Creates new form AdminForm
     */
    public AdminForm() {
        initComponents();
        setLocationRelativeTo(this);
    }
    public boolean  validateInputLength()
    {
        boolean bStatus  = true;
        String  ErrorMsg = " ";
        
        // checking the length validation of name
        if( 3 > jTextFieldCustomerName.getText().length() || 
            30 < jTextFieldCustomerName.getText().length())
        {
            ErrorMsg += "Name length must be between 3 and 30 \n";
            bStatus  = false;
        }
        
        // checking the length validation of address
        if( 9  > jTextAreaAddress.getText().length() ||
            200 < jTextAreaAddress.getText().length())
        {
            ErrorMsg += "Address length must be between 10 and 200 \n";
            bStatus  = false;
        }
        
        //validation of phone number .
        if( jTextFieldPhone.getText().length() < 10 || 
            jTextFieldPhone.getText().length() > 10 )
        {
            ErrorMsg += " Phone number should contain 10 digits \n";
            bStatus  = false;
        }
        
        // customer remark validation
        if( jTextAreaRemark.getText().length() > 200 )
        {
           ErrorMsg += " Remark should be less than 100 \n"; 
           bStatus = false;
                   
        }
        
        if( jTextAreaRecomenter.getText().length() > 200 )
        {
           ErrorMsg += " Recommender text length should be less than 200 \n"; 
           bStatus = false;
        }
        
        if( false == bStatus )
        {
            JOptionPane.showMessageDialog(rootPane, ErrorMsg );
        }
        
        return bStatus; 
    }
    
    
    // function add new customer to the database.
    public int AddNewCustomer()
    {
        boolean validInputLength = false;   // user input validation flag.True -> user input is valid.
        Database.DataBaseManager dbManager; // Object of database manager.
        String autoId  = null;                        // Status variable show insertion sucessfull or not. 
        int nStatus = 0;
        
        validInputLength = validateInputLength();
        if( validInputLength )
        {
            try 
            {
                // creating object of databasemanager.
                dbManager = new DataBaseManager();
                // function to set the customer details to the database.    
                 autoId = dbManager.SetCustomerDetails( jTextFieldCustomerName.getText(),
                                                            jTextAreaAddress.getText(),
                                                            jTextFieldPhone.getText(),
                                                            jTextAreaRemark.getText(),
                                                            jTextAreaRecomenter.getText() );
                 if( null != autoId )
                 {
                     jTextFieldCustId.setText(autoId);
                     jButtonSearch.setVisible(false);
                     jTextFieldCustId.setVisible(false);
                     jLabelCustId.setVisible(false);
                     jTextAreaCustDetails.setText( jTextFieldCustomerName.getText() + "\n"
                                                +  jTextAreaAddress.getText() +"\n"
                                                +  jTextFieldPhone.getText() );
                     
                     nStatus = 1;
                     SetVisibility(Add_New_Credit);
                 }
                
                
                // memory deallocation
                dbManager.closeConection();
                ClearAddNewCustomerFields();
                dbManager = null;
            
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return nStatus;
        
    }
    
    // function to set the visibility of different panel 
    // The function first set the visibility of all panel to false.
    // Then set the visibility of the required window.
    public void SetVisibility( JPanel panel )
    {
        Add_New_Credit.setVisible(false);
        Show_All_Customers.setVisible(false);
        Add_Customer.setVisible(false);
        Add_transaction.setVisible(false);
        Add_amount.setVisible(false);
        Report.setVisible(false);
        Remove_Customer.setVisible(false);
        Edit_Login.setVisible(false);
        Print_Pane.setVisible(false);
        WelcomePane.setVisible(false);
        
        panel.setVisible(true);
        
        
    }
    
    // clear all text field values of add new customer GUI.
    public void ClearAddNewCustomerFields()
    {
        jTextFieldCustomerName.setText( null );
        jTextAreaAddress.setText( null );
        jTextFieldPhone.setText( null );
        jTextAreaRemark.setText( null );
        jTextAreaRecomenter.setText( null );
    }
    
    // function clear the values in the text field add new credit details.
    public void ClearAddNewCreditFields()
    {
        jTextFieldCustId.setText( null );
        jTextAreaCustDetails.setText( null );
        jTextAreaPurchaseRemark.setText( null );
        jXDatePicker_PurchaseDate.setDate( null );
        jXDatePicker_DueDate.setDate( null );
        jFormattedTextField_Amount.setText( null );
        jButtonSearch.setVisible(true);
        jTextFieldCustId.setVisible(true);
        jLabelCustId.setVisible(true);
        
    }
    public void clearAddAmountForm()
    {
        jTextFieldRemitedAmount.setText(null);
        jXDatePickerRemitedDate.setDate(null);
    }
    
    public void clearAddTransaction()
    {
        jTextFieldCustId1.setText(null);
        jTextAreaCustDetails1.setText(null);
        DefaultTableModel dm;
        dm = (DefaultTableModel)jTablePurchaseDetails.getModel();
        dm.setRowCount(0);
    }
    public void ClearChangePassword()
    {
        jPasswordFieldCurrentPassword.setText(null);
        jPasswordFieldNewPassword.setText(null);
        jPasswordFieldConfirmPassword.setText(null);
    }
    
    // function to set the search disalog box of search customer
    // dialog box poped up above -> Add new credit.
    
    public void setSearchDialog( JDialog dialogBox )
    {
        dialogBox.setSize(800, 500);
        dialogBox.setLocationRelativeTo(this);
        dialogBox.setVisible(rootPaneCheckingEnabled);
    }
    
    
    // set data to the amount transaction fields
    public void setAddAmountForm()
    {
        
        ResultSet rs            = null;
        DataBaseManager manager = null;
        double dTotalAmount     = 0;
        double dRemitedAmount   = 0;
        double dDueAmount       = 0;
                
        
        // read the customer id from the text field
        if( jTextAreaCustDetails1.getText() != null )
        {
            // read the purchase id from the table field.
            // The first column of table contain the purchase id.
            
                // set the purchase details to the purchase remark text field 
                             
                try {
                    // set all the transactions to the transaction table.
                    // customer id ,purchase id passed as arguments.
                    manager = new DataBaseManager();
                    rs = manager.getTransactions( jTextFieldCustId1.getText() );
                    if( null == rs )
                    {
                   
                    }
                    else
                    {
                        jTableTransactions.setModel( DbUtils.resultSetToTableModel(rs)); 
                    }
                    
                    // add total amount
                    rs = manager.getTotalAmount( jTextFieldCustId1.getText() );
                    if( rs.next() )
                    {
                        jTextFieldTotalAmount.setText( rs.getString("Amount"));
                        dTotalAmount = Double.valueOf(rs.getString("Amount"));
                        
                    }
                    else
                    {
                       jTextFieldTotalAmount.setText( "0"); 
                    }
                    
                    // calculate  balance due
                    rs = manager.getRemitedAmount( jTextFieldCustId1.getText() );
                    
                    if( rs.next() )
                    {
                        if(rs.getString(1) == null )
                        {
                            jTextFieldRemited.setText("0");
                            jTextFieldBalanceDue.setText(String.valueOf(dTotalAmount));
                        }
                        else
                        {
                            jTextFieldRemited.setText(rs.getString(1));
                            dRemitedAmount = Double.valueOf(rs.getString(1));
                            dDueAmount     = dTotalAmount - dRemitedAmount;
                            if( 0 <= dDueAmount )
                            {
                                jTextFieldBalanceDue.setText(String.valueOf(dDueAmount));
                            }
                            else
                            {
                                jTextFieldBalanceDue.setText("0");
                            }
                        }
                        
                    }
                
                }
                catch( Exception e )
                {
                    JOptionPane.showMessageDialog(rootPane, e);
                }
                    
                SetVisibility( Add_amount );
                
                
            
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "please select a customer" );
        }
        
        
    }
    
    public int setPrintOut( String szCustId )
    {
        
        ResultSet rs = null;
        int nStatus  = 0;
        DataBaseManager manager = null;
        DefaultTableModel dm;
        
        double dTotalAmount = 0;
        double dRemited     = 0;
        double dBalance     = 0;
        
        if( !szCustId.equals(null) )
        {
            
            try {
                dm = (DefaultTableModel)jTablePrintOut.getModel();
                manager = new DataBaseManager();
                dm.setRowCount(0);
                
                 // get personal details
                rs =  manager.getCustomerDetails( szCustId );
                dm.addRow(new Object[] {" "," " });
                if( rs == null  )
                {
                    
                } 
                else 
                {
                    dm.addRow(new Object[] {"<html><b><font color = 'red' >PERSONAL DETAILS</font></b></html>"," " });
                    dm.addRow(new Object[] {" "," " });
                    while( rs.next() )
                    {
                        
                        dm.addRow(new Object[] {"CUSTOMER ID ",rs.getString(1)});
                        dm.addRow(new Object[] {"NAME",rs.getString(2)});
                        dm.addRow(new Object[] {"ADDRESS",rs.getString(3)});
                        dm.addRow(new Object[] {"PHONE",rs.getString(4)});
                    }
                }
                
                // get purchase details
                rs = null;
                rs =  manager.getPurchaseDetails(szCustId );     
                if( rs == null  )
                {
                    
                } 
                else
                {
                    dm.addRow(new Object[] {" "," " });
                    dm.addRow(new Object[] {"<html><b><font color = 'red' >PURCHASE DETAILS</font></b></html>"," " });
                    dm.addRow(new Object[] {"<html><b><font color = 'green' >DATE(Y-M-D)</font></b></html>",
                                            "<html><b><font color = 'green' >AMOUNT</font></b></html>" });
                    
                  
                    while( rs.next() )
                    {
                     
                      dm.addRow(new Object[] {rs.getString("StartDate"),rs.getString("Amount")});
                      
                   }
                }
                
                // get purchase total
                rs = manager.getTotalAmount( szCustId );
                if( rs == null  )
                {
                    
                } 
                else
                {    
                    if( rs.next() )
                    {
                      
                      dm.addRow(new Object[] { "<html><b><font color = 'red' >TOTAL AMOUNT</font></b></html>",
                                 "<html><b><font color = 'red' >"+rs.getString("Amount")+"</font></b></html>"});
                      
                      if( rs.getString("Amount") == null ) 
                      {
                        dTotalAmount = 0;
                      } 
                      else
                      {
                          dTotalAmount = Double.valueOf( rs.getString("Amount") );
                      }
                    }
                   
                }
                
                // get all transactions
                rs =  manager.getTransactions( szCustId );     
                if( rs == null  )
                {
                    
                } 
                else
                {
                    dm.addRow(new Object[] {" "," " });
                    dm.addRow(new Object[] {"<html><b><font color = 'red' >AMOUNT REMITED DETAILS</font></b></html>"," " });
                    
                    dm.addRow(new Object[] {"<html><b><font color = 'green' >DATE(Y-M-D)</font></b></html>",
                                            "<html><b><font color = 'green' >AMOUNT</font></b></html>" });
                    while( rs.next() )
                    {
                      
                      dm.addRow(new Object[] {rs.getString("date"),rs.getString("Amount_remited")});
                      
                   }
                }
                
                // get remited total
                rs = manager.getRemitedAmount( szCustId );
                if( rs == null  )
                {
                    
                } 
                else
                {    
                    if( rs.next() )
                    {
                      
                      dm.addRow(new Object[] { "<html><b><font color = 'red' >TOTAL REMITED AMOUNT</font></b></html>",
                                 "<html><b><font color = 'red' >"+rs.getString("remited")+"</font></b></html>"});
                      
                      if( rs.getString("remited") == null )
                      {
                         dRemited = 0; 
                      }
                      else
                      {
                        dRemited = Double.valueOf( rs.getString("remited") );  
                      }
                      
                    }
                   
                }
                
                dm.addRow(new Object[] {" "," " });
                dBalance = dTotalAmount - dRemited;
                dm.addRow(new Object[] { "<html><b><font color = 'red' >BALANCE AMOUNT</font></b></html>",
                                         "<html><b><font color = 'red' >"+ dBalance +"</font></b></html>"});
                
                
                
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            SetVisibility(Print_Pane);
           
        }
        
        return nStatus;
    }
    
 
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogSearch = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jTextFieldSearchField = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableSearchResult = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jDialogTransactionSearch = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jTextFieldSearchField1 = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTableSearchResult1 = new javax.swing.JTable();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTextAreaCustDetails2 = new javax.swing.JTextArea();
        jDialogSearchForRemovingCustomer = new javax.swing.JDialog();
        jPanel16 = new javax.swing.JPanel();
        jTextFieldSearchField2 = new javax.swing.JTextField();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTableSearchResult2 = new javax.swing.JTable();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jDialogSearchCustomer = new javax.swing.JDialog();
        jPanel18 = new javax.swing.JPanel();
        jTextFieldSearchField3 = new javax.swing.JTextField();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTableSearchResult3 = new javax.swing.JTable();
        jDisalogSearchCustomer_close = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jDisalogSearchCustomer_close1 = new javax.swing.JButton();
        Head = new javax.swing.JPanel();
        Logolabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        SideMenu = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButtonReport1 = new javax.swing.JButton();
        jButtonReport = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        MiddlePane = new javax.swing.JPanel();
        WelcomePane = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        Add_Customer = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldCustomerName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaAddress = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldPhone = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaRemark = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaRecomenter = new javax.swing.JTextArea();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        Show_All_Customers = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableAllCustomer = new javax.swing.JTable();
        Add_New_Credit = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabelCustId = new javax.swing.JLabel();
        jTextFieldCustId = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextAreaCustDetails = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextAreaPurchaseRemark = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jFormattedTextField_Amount = new javax.swing.JFormattedTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        Add_transaction = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldCustId1 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextAreaCustDetails1 = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTablePurchaseDetails = new javax.swing.JTable();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        Add_amount = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTableTransactions = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldTotalAmount = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldRemited = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextFieldRemitedAmount = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldBalanceDue = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        Remove_Customer = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jTextFieldRemoveCustID = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextAreaRemovePurchaseDetails = new javax.swing.JTextArea();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextAreaRemoveCustDetails = new javax.swing.JTextArea();
        jButton25 = new javax.swing.JButton();
        RemoveCustomer_Button = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        Report = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTableReport = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        Edit_Login = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPasswordFieldCurrentPassword = new javax.swing.JPasswordField();
        jLabel34 = new javax.swing.JLabel();
        jPasswordFieldNewPassword = new javax.swing.JPasswordField();
        jLabel35 = new javax.swing.JLabel();
        jPasswordFieldConfirmPassword = new javax.swing.JPasswordField();
        jButton28 = new javax.swing.JButton();
        Print_Pane = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTablePrintOut = new javax.swing.JTable();
        PurchaseDues = new javax.swing.JPanel();
        Footer = new javax.swing.JPanel();
        CopyrightLabel = new javax.swing.JLabel();

        jDialogSearch.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel6.setBackground(new java.awt.Color(0, 51, 51));

        jTextFieldSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchFieldActionPerformed(evt);
            }
        });
        jTextFieldSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchFieldKeyReleased(evt);
            }
        });

        jScrollPane9.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane9.setBorder(null);

        jTableSearchResult.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableSearchResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null}
            },
            new String [] {
                ""
            }
        ));
        jTableSearchResult.setRowHeight(40);
        jScrollPane9.setViewportView(jTableSearchResult);

        jButton10.setBackground(new java.awt.Color(0, 102, 102));
        jButton10.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("OK");
        jButton10.setBorder(null);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(0, 102, 102));
        jButton13.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("CLOSE");
        jButton13.setBorder(null);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Search Customer Name");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldSearchField)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogSearchLayout = new javax.swing.GroupLayout(jDialogSearch.getContentPane());
        jDialogSearch.getContentPane().setLayout(jDialogSearchLayout);
        jDialogSearchLayout.setHorizontalGroup(
            jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jDialogSearchLayout.setVerticalGroup(
            jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel11.setBackground(new java.awt.Color(0, 51, 51));

        jTextFieldSearchField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchField1KeyReleased(evt);
            }
        });

        jTableSearchResult1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableSearchResult1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null}
            },
            new String [] {
                ""
            }
        ));
        jTableSearchResult1.setRowHeight(40);
        jScrollPane13.setViewportView(jTableSearchResult1);

        jButton16.setBackground(new java.awt.Color(0, 102, 102));
        jButton16.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("OK");
        jButton16.setBorder(null);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(0, 102, 102));
        jButton17.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("CLOSE");
        jButton17.setBorder(null);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Search Customer Name");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldSearchField1)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldSearchField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogTransactionSearchLayout = new javax.swing.GroupLayout(jDialogTransactionSearch.getContentPane());
        jDialogTransactionSearch.getContentPane().setLayout(jDialogTransactionSearchLayout);
        jDialogTransactionSearchLayout.setHorizontalGroup(
            jDialogTransactionSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogTransactionSearchLayout.setVerticalGroup(
            jDialogTransactionSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Customer Details");

        jTextAreaCustDetails2.setEditable(false);
        jTextAreaCustDetails2.setBackground(new java.awt.Color(204, 255, 204));
        jTextAreaCustDetails2.setColumns(20);
        jTextAreaCustDetails2.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        jTextAreaCustDetails2.setForeground(new java.awt.Color(255, 0, 51));
        jTextAreaCustDetails2.setRows(5);
        jTextAreaCustDetails2.setInheritsPopupMenu(true);
        jTextAreaCustDetails2.setMargin(new java.awt.Insets(10, 10, 2, 2));
        jScrollPane15.setViewportView(jTextAreaCustDetails2);

        jDialogSearchForRemovingCustomer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel16.setBackground(new java.awt.Color(0, 51, 51));

        jTextFieldSearchField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchField2KeyReleased(evt);
            }
        });

        jScrollPane20.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane20.setBorder(null);

        jTableSearchResult2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableSearchResult2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null}
            },
            new String [] {
                ""
            }
        ));
        jTableSearchResult2.setRowHeight(40);
        jScrollPane20.setViewportView(jTableSearchResult2);

        jButton26.setBackground(new java.awt.Color(0, 102, 102));
        jButton26.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton26.setForeground(new java.awt.Color(255, 255, 255));
        jButton26.setText("OK");
        jButton26.setBorder(null);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setBackground(new java.awt.Color(0, 102, 102));
        jButton27.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton27.setForeground(new java.awt.Color(255, 255, 255));
        jButton27.setText("CLOSE");
        jButton27.setBorder(null);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Search Customer Name");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldSearchField2)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldSearchField2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogSearchForRemovingCustomerLayout = new javax.swing.GroupLayout(jDialogSearchForRemovingCustomer.getContentPane());
        jDialogSearchForRemovingCustomer.getContentPane().setLayout(jDialogSearchForRemovingCustomerLayout);
        jDialogSearchForRemovingCustomerLayout.setHorizontalGroup(
            jDialogSearchForRemovingCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchForRemovingCustomerLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jDialogSearchForRemovingCustomerLayout.setVerticalGroup(
            jDialogSearchForRemovingCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchForRemovingCustomerLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jDialogSearchCustomer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel18.setBackground(new java.awt.Color(0, 51, 51));

        jTextFieldSearchField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchField3ActionPerformed(evt);
            }
        });
        jTextFieldSearchField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchField3KeyReleased(evt);
            }
        });

        jScrollPane21.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane21.setBorder(null);

        jTableSearchResult3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableSearchResult3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null}
            },
            new String [] {
                ""
            }
        ));
        jTableSearchResult3.setRowHeight(40);
        jScrollPane21.setViewportView(jTableSearchResult3);

        jDisalogSearchCustomer_close.setBackground(new java.awt.Color(0, 102, 102));
        jDisalogSearchCustomer_close.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jDisalogSearchCustomer_close.setForeground(new java.awt.Color(255, 255, 255));
        jDisalogSearchCustomer_close.setText("CLOSE");
        jDisalogSearchCustomer_close.setBorder(null);
        jDisalogSearchCustomer_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDisalogSearchCustomer_closeActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Search Customer Name");

        jDisalogSearchCustomer_close1.setBackground(new java.awt.Color(0, 102, 102));
        jDisalogSearchCustomer_close1.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jDisalogSearchCustomer_close1.setForeground(new java.awt.Color(255, 255, 255));
        jDisalogSearchCustomer_close1.setText("SHOW DETAILS");
        jDisalogSearchCustomer_close1.setBorder(null);
        jDisalogSearchCustomer_close1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDisalogSearchCustomer_close1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldSearchField3)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jDisalogSearchCustomer_close1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDisalogSearchCustomer_close, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldSearchField3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDisalogSearchCustomer_close, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDisalogSearchCustomer_close1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogSearchCustomerLayout = new javax.swing.GroupLayout(jDialogSearchCustomer.getContentPane());
        jDialogSearchCustomer.getContentPane().setLayout(jDialogSearchCustomerLayout);
        jDialogSearchCustomerLayout.setHorizontalGroup(
            jDialogSearchCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchCustomerLayout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jDialogSearchCustomerLayout.setVerticalGroup(
            jDialogSearchCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchCustomerLayout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        Head.setBackground(new java.awt.Color(0, 51, 51));

        Logolabel.setFont(new java.awt.Font("Miriam Fixed", 1, 36)); // NOI18N
        Logolabel.setForeground(new java.awt.Color(255, 255, 255));
        Logolabel.setText("DebiTRACK");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485643521_track_results.png"))); // NOI18N

        javax.swing.GroupLayout HeadLayout = new javax.swing.GroupLayout(Head);
        Head.setLayout(HeadLayout);
        HeadLayout.setHorizontalGroup(
            HeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeadLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Logolabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HeadLayout.setVerticalGroup(
            HeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logolabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        SideMenu.setBackground(new java.awt.Color(0, 102, 102));
        SideMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SideMenu.setLayout(new java.awt.GridLayout(10, 1));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485644515_Add-Male-User.png"))); // NOI18N
        jButton1.setText("Add New Customer");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton1);

        jButton5.setBackground(new java.awt.Color(51, 51, 51));
        jButton5.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485644689_magnifyingglass.png"))); // NOI18N
        jButton5.setText(" View Customers");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton5);

        jButton8.setBackground(new java.awt.Color(51, 51, 51));
        jButton8.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485644689_magnifyingglass.png"))); // NOI18N
        jButton8.setText("Search Customer");
        jButton8.setBorder(null);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton8);

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485644843_delete.png"))); // NOI18N
        jButton2.setText("Remove Customer");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton2);

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485645003_creditcard.png"))); // NOI18N
        jButton3.setText("Add New Credit");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton3);

        jButton14.setBackground(new java.awt.Color(51, 51, 51));
        jButton14.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485646223_Money.png"))); // NOI18N
        jButton14.setText("Add Transaction");
        jButton14.setBorder(null);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton14);

        jButtonReport1.setBackground(new java.awt.Color(51, 51, 51));
        jButtonReport1.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButtonReport1.setForeground(new java.awt.Color(255, 255, 255));
        jButtonReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485646419_stock_appointment-reminder.png"))); // NOI18N
        jButtonReport1.setText(" Purchase dues");
        jButtonReport1.setBorder(null);
        jButtonReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReport1ActionPerformed(evt);
            }
        });
        SideMenu.add(jButtonReport1);

        jButtonReport.setBackground(new java.awt.Color(51, 51, 51));
        jButtonReport.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButtonReport.setForeground(new java.awt.Color(255, 255, 255));
        jButtonReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485646510_6.png"))); // NOI18N
        jButtonReport.setText("Report       ");
        jButtonReport.setBorder(null);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });
        SideMenu.add(jButtonReport);

        jButton18.setBackground(new java.awt.Color(51, 51, 51));
        jButton18.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485646599_application-pgp-signature.png"))); // NOI18N
        jButton18.setText("Change Login ");
        jButton18.setBorder(null);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton18);

        jButton4.setBackground(new java.awt.Color(51, 51, 51));
        jButton4.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485646651_exit.png"))); // NOI18N
        jButton4.setText("Log out    ");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        SideMenu.add(jButton4);

        MiddlePane.setBackground(new java.awt.Color(30, 35, 35));
        MiddlePane.setLayout(new java.awt.CardLayout());

        WelcomePane.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485644054_constr_add_card.png"))); // NOI18N

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("A Complete debit tracker ");

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("debiTRACK");

        javax.swing.GroupLayout WelcomePaneLayout = new javax.swing.GroupLayout(WelcomePane);
        WelcomePane.setLayout(WelcomePaneLayout);
        WelcomePaneLayout.setHorizontalGroup(
            WelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WelcomePaneLayout.createSequentialGroup()
                .addGroup(WelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        WelcomePaneLayout.setVerticalGroup(
            WelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WelcomePaneLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        MiddlePane.add(WelcomePane, "card12");

        Add_Customer.setBackground(new java.awt.Color(30, 35, 35));
        Add_Customer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Add a new customer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane1.setBorder(null);
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Customer Name");

        jTextFieldCustomerName.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jTextFieldCustomerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustomerName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTextFieldCustomerName.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Customer Address");

        jTextAreaAddress.setColumns(20);
        jTextAreaAddress.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jTextAreaAddress.setLineWrap(true);
        jTextAreaAddress.setRows(5);
        jTextAreaAddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTextAreaAddress.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextAreaAddress.setMargin(new java.awt.Insets(20, 20, 2, 2));
        jScrollPane2.setViewportView(jTextAreaAddress);

        jLabel3.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Phone Number");

        jTextFieldPhone.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jTextFieldPhone.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldPhone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTextFieldPhone.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel4.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Remarks");

        jTextAreaRemark.setColumns(20);
        jTextAreaRemark.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jTextAreaRemark.setRows(5);
        jTextAreaRemark.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTextAreaRemark.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextAreaRemark.setMargin(new java.awt.Insets(20, 20, 2, 2));
        jScrollPane3.setViewportView(jTextAreaRemark);

        jLabel5.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Recommender Details");

        jTextAreaRecomenter.setColumns(20);
        jTextAreaRecomenter.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jTextAreaRecomenter.setRows(5);
        jTextAreaRecomenter.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTextAreaRecomenter.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextAreaRecomenter.setMargin(new java.awt.Insets(20, 20, 2, 2));
        jScrollPane4.setViewportView(jTextAreaRecomenter);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(87, 87, 87)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCustomerName)
                            .addComponent(jScrollPane2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                            .addComponent(jScrollPane4)
                            .addComponent(jTextFieldPhone))))
                .addGap(58, 58, 58))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel2))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel4)
                        .addGap(68, 68, 68))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(115, 115, 115))
        );

        jScrollPane1.setViewportView(jPanel8);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton6.setBackground(new java.awt.Color(0, 102, 102));
        jButton6.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("SAVE");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 102, 102));
        jButton7.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("CLEAR");
        jButton7.setBorder(null);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton29.setBackground(new java.awt.Color(0, 102, 102));
        jButton29.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton29.setForeground(new java.awt.Color(255, 255, 255));
        jButton29.setText("SAVE & ADD CREDIT");
        jButton29.setBorder(null);
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Add_CustomerLayout = new javax.swing.GroupLayout(Add_Customer);
        Add_Customer.setLayout(Add_CustomerLayout);
        Add_CustomerLayout.setHorizontalGroup(
            Add_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_CustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(Add_CustomerLayout.createSequentialGroup()
                .addContainerGap(240, Short.MAX_VALUE)
                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        Add_CustomerLayout.setVerticalGroup(
            Add_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_CustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Add_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        MiddlePane.add(Add_Customer, "card2");

        Show_All_Customers.setBackground(new java.awt.Color(30, 35, 35));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "View all customers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jScrollPane6.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane6.setBorder(null);
        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableAllCustomer.setAutoCreateRowSorter(true);
        jTableAllCustomer.setBackground(new java.awt.Color(240, 240, 240));
        jTableAllCustomer.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jTableAllCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null}
            },
            new String [] {
                ""
            }
        ));
        jTableAllCustomer.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableAllCustomer.setRowHeight(40);
        jTableAllCustomer.setRowMargin(10);
        jScrollPane6.setViewportView(jTableAllCustomer);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout Show_All_CustomersLayout = new javax.swing.GroupLayout(Show_All_Customers);
        Show_All_Customers.setLayout(Show_All_CustomersLayout);
        Show_All_CustomersLayout.setHorizontalGroup(
            Show_All_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Show_All_CustomersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Show_All_CustomersLayout.setVerticalGroup(
            Show_All_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Show_All_CustomersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePane.add(Show_All_Customers, "card3");

        Add_New_Credit.setBackground(new java.awt.Color(30, 35, 35));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Add new credit", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jScrollPane5.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane5.setBorder(null);

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jLabelCustId.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabelCustId.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCustId.setText("CUSTOMER ID");

        jTextFieldCustId.setEditable(false);
        jTextFieldCustId.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldCustId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextFieldCustId.setForeground(new java.awt.Color(0, 102, 102));
        jTextFieldCustId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustId.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextFieldCustId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldCustIdMouseClicked(evt);
            }
        });
        jTextFieldCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCustIdActionPerformed(evt);
            }
        });
        jTextFieldCustId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldCustIdKeyReleased(evt);
            }
        });

        jButtonSearch.setBackground(new java.awt.Color(0, 102, 102));
        jButtonSearch.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButtonSearch.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSearch.setText("Search");
        jButtonSearch.setBorder(null);
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Customer Details");

        jTextAreaCustDetails.setEditable(false);
        jTextAreaCustDetails.setBackground(new java.awt.Color(204, 204, 204));
        jTextAreaCustDetails.setColumns(20);
        jTextAreaCustDetails.setFont(new java.awt.Font("Miriam Fixed", 0, 18)); // NOI18N
        jTextAreaCustDetails.setForeground(new java.awt.Color(255, 0, 51));
        jTextAreaCustDetails.setRows(5);
        jTextAreaCustDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextAreaCustDetails.setInheritsPopupMenu(true);
        jTextAreaCustDetails.setMargin(new java.awt.Insets(10, 10, 2, 2));
        jScrollPane7.setViewportView(jTextAreaCustDetails);

        jLabel8.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Purchase Remark");

        jTextAreaPurchaseRemark.setBackground(new java.awt.Color(204, 204, 204));
        jTextAreaPurchaseRemark.setColumns(20);
        jTextAreaPurchaseRemark.setFont(new java.awt.Font("Miriam Fixed", 0, 18)); // NOI18N
        jTextAreaPurchaseRemark.setRows(5);
        jTextAreaPurchaseRemark.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextAreaPurchaseRemark.setMargin(new java.awt.Insets(10, 10, 2, 2));
        jTextAreaPurchaseRemark.setName(""); // NOI18N
        jScrollPane8.setViewportView(jTextAreaPurchaseRemark);

        jLabel9.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Purchase Date");

        jLabel10.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Due Date");

        jLabel11.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Amount");

        jLabel12.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 255, 255));
        jLabel12.setText("Rs");

        jFormattedTextField_Amount.setBackground(new java.awt.Color(204, 204, 204));
        jFormattedTextField_Amount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jFormattedTextField_Amount.setForeground(new java.awt.Color(0, 102, 102));
        jFormattedTextField_Amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        jFormattedTextField_Amount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField_Amount.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCustId, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTextFieldCustId, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGap(245, 245, 245)
                                    .addComponent(jLabel10))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jFormattedTextField_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(99, 99, 99))))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCustId)
                    .addComponent(jTextFieldCustId, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel7))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel8))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(42, 42, 42)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jFormattedTextField_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(84, 84, 84))
        );

        jScrollPane5.setViewportView(jPanel5);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
        );

        jButton11.setBackground(new java.awt.Color(0, 102, 102));
        jButton11.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("ADD");
        jButton11.setBorder(null);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 102, 102));
        jButton12.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("CLEAR");
        jButton12.setBorder(null);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Add_New_CreditLayout = new javax.swing.GroupLayout(Add_New_Credit);
        Add_New_Credit.setLayout(Add_New_CreditLayout);
        Add_New_CreditLayout.setHorizontalGroup(
            Add_New_CreditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_New_CreditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Add_New_CreditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Add_New_CreditLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)))
                .addContainerGap())
        );
        Add_New_CreditLayout.setVerticalGroup(
            Add_New_CreditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_New_CreditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addGroup(Add_New_CreditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );

        MiddlePane.add(Add_New_Credit, "card4");

        Add_transaction.setBackground(new java.awt.Color(30, 35, 35));

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Add a transaction", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jScrollPane10.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane10.setBorder(null);

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));

        jLabel14.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("CUSTOMER ID");

        jTextFieldCustId1.setEditable(false);
        jTextFieldCustId1.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldCustId1.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jTextFieldCustId1.setForeground(new java.awt.Color(0, 102, 102));
        jTextFieldCustId1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustId1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldCustId1MouseClicked(evt);
            }
        });
        jTextFieldCustId1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCustId1ActionPerformed(evt);
            }
        });
        jTextFieldCustId1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldCustId1KeyReleased(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(0, 102, 102));
        jButton15.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("Search");
        jButton15.setBorder(null);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Customer Details");

        jScrollPane11.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane11.setBorder(null);

        jTextAreaCustDetails1.setEditable(false);
        jTextAreaCustDetails1.setBackground(new java.awt.Color(204, 204, 204));
        jTextAreaCustDetails1.setColumns(20);
        jTextAreaCustDetails1.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jTextAreaCustDetails1.setRows(5);
        jTextAreaCustDetails1.setInheritsPopupMenu(true);
        jTextAreaCustDetails1.setMargin(new java.awt.Insets(10, 10, 2, 2));
        jScrollPane11.setViewportView(jTextAreaCustDetails1);

        jLabel16.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Purchase details");

        jScrollPane12.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane12.setBorder(null);

        jTablePurchaseDetails.setBackground(new java.awt.Color(204, 255, 255));
        jTablePurchaseDetails.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jTablePurchaseDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane12.setViewportView(jTablePurchaseDetails);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jTextFieldCustId1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                            .addComponent(jScrollPane11))
                        .addGap(98, 98, 98))))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldCustId1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel15))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)))
                .addGap(21, 21, 21)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        jScrollPane10.setViewportView(jPanel10);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
        );

        jButton19.setBackground(new java.awt.Color(0, 102, 102));
        jButton19.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.setText("View & Add amount");
        jButton19.setBorder(null);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(0, 102, 102));
        jButton20.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.setText("Clear");
        jButton20.setBorder(null);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Add_transactionLayout = new javax.swing.GroupLayout(Add_transaction);
        Add_transaction.setLayout(Add_transactionLayout);
        Add_transactionLayout.setHorizontalGroup(
            Add_transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_transactionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Add_transactionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        Add_transactionLayout.setVerticalGroup(
            Add_transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_transactionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Add_transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        MiddlePane.add(Add_transaction, "card5");

        Add_amount.setBackground(new java.awt.Color(30, 35, 35));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Add amount", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jScrollPane14.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane14.setBorder(null);

        jPanel13.setBackground(new java.awt.Color(51, 51, 51));
        jPanel13.setEnabled(false);

        jLabel20.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Transactions");

        jTableTransactions.setBackground(new java.awt.Color(204, 204, 204));
        jTableTransactions.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jTableTransactions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane17.setViewportView(jTableTransactions);

        jLabel21.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Total Remited");

        jTextFieldTotalAmount.setEditable(false);
        jTextFieldTotalAmount.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldTotalAmount.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jTextFieldTotalAmount.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldTotalAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel22.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Total amount");

        jTextFieldRemited.setEditable(false);
        jTextFieldRemited.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldRemited.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jTextFieldRemited.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldRemited.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jPanel14.setBackground(new java.awt.Color(0, 102, 102));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Add Amount", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel14.setAutoscrolls(true);

        jLabel23.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Select date");

        jLabel24.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Add Amount");

        jTextFieldRemitedAmount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextFieldRemitedAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton21.setBackground(new java.awt.Color(204, 204, 204));
        jButton21.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButton21.setText("CLOSE");
        jButton21.setBorder(null);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(204, 204, 204));
        jButton22.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButton22.setText("ADD AMOUNT");
        jButton22.setBorder(null);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setBackground(new java.awt.Color(204, 204, 204));
        jButton23.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButton23.setText("CLEAR");
        jButton23.setBorder(null);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jTextFieldRemitedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRemitedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel25.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(204, 255, 255));
        jLabel25.setText("Rs");

        jLabel26.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(204, 255, 255));
        jLabel26.setText("Rs");

        jLabel27.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Balance Due");

        jTextFieldBalanceDue.setEditable(false);
        jTextFieldBalanceDue.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldBalanceDue.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jTextFieldBalanceDue.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldBalanceDue.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel28.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(204, 255, 255));
        jLabel28.setText("Rs");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldRemited, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                    .addComponent(jTextFieldTotalAmount, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldBalanceDue))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane17))
                        .addGap(15, 15, 15))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 250, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel22))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jTextFieldRemited, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jTextFieldBalanceDue, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(221, 221, 221))
        );

        jScrollPane14.setViewportView(jPanel13);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Add_amountLayout = new javax.swing.GroupLayout(Add_amount);
        Add_amount.setLayout(Add_amountLayout);
        Add_amountLayout.setHorizontalGroup(
            Add_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Add_amountLayout.setVerticalGroup(
            Add_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Add_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePane.add(Add_amount, "card6");

        Remove_Customer.setBackground(new java.awt.Color(30, 35, 35));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Remove a customer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jPanel15.setBackground(new java.awt.Color(51, 51, 51));

        jLabel30.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("CUSTOMER ID");

        jTextFieldRemoveCustID.setEditable(false);
        jTextFieldRemoveCustID.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldRemoveCustID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextFieldRemoveCustID.setForeground(new java.awt.Color(0, 102, 102));
        jTextFieldRemoveCustID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldRemoveCustID.setBorder(null);
        jTextFieldRemoveCustID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldRemoveCustIDMouseClicked(evt);
            }
        });
        jTextFieldRemoveCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRemoveCustIDActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Customer Details");

        jLabel31.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Purchase Details");

        jTextAreaRemovePurchaseDetails.setColumns(20);
        jTextAreaRemovePurchaseDetails.setRows(5);
        jScrollPane18.setViewportView(jTextAreaRemovePurchaseDetails);

        jTextAreaRemoveCustDetails.setColumns(20);
        jTextAreaRemoveCustDetails.setRows(5);
        jScrollPane19.setViewportView(jTextAreaRemoveCustDetails);

        jButton25.setBackground(new java.awt.Color(0, 102, 102));
        jButton25.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jButton25.setForeground(new java.awt.Color(255, 255, 255));
        jButton25.setText("Search");
        jButton25.setBorder(null);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel31))
                .addGap(93, 93, 93)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane19)
                    .addComponent(jScrollPane18)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jTextFieldRemoveCustID, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)))
                .addContainerGap(185, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRemoveCustID, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                                .addComponent(jLabel31)
                                .addGap(114, 114, 114))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 823, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RemoveCustomer_Button.setBackground(new java.awt.Color(0, 102, 102));
        RemoveCustomer_Button.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        RemoveCustomer_Button.setForeground(new java.awt.Color(255, 255, 255));
        RemoveCustomer_Button.setText("REMOVE");
        RemoveCustomer_Button.setBorder(null);
        RemoveCustomer_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveCustomer_ButtonActionPerformed(evt);
            }
        });

        jButton24.setBackground(new java.awt.Color(0, 102, 102));
        jButton24.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jButton24.setForeground(new java.awt.Color(255, 255, 255));
        jButton24.setText("CANCEL");
        jButton24.setBorder(null);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Remove_CustomerLayout = new javax.swing.GroupLayout(Remove_Customer);
        Remove_Customer.setLayout(Remove_CustomerLayout);
        Remove_CustomerLayout.setHorizontalGroup(
            Remove_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Remove_CustomerLayout.createSequentialGroup()
                .addContainerGap(232, Short.MAX_VALUE)
                .addComponent(RemoveCustomer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(339, 339, 339))
            .addGroup(Remove_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Remove_CustomerLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        Remove_CustomerLayout.setVerticalGroup(
            Remove_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Remove_CustomerLayout.createSequentialGroup()
                .addContainerGap(469, Short.MAX_VALUE)
                .addGroup(Remove_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RemoveCustomer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
            .addGroup(Remove_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Remove_CustomerLayout.createSequentialGroup()
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 99, Short.MAX_VALUE)))
        );

        MiddlePane.add(Remove_Customer, "card7");

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        jTableReport.setAutoCreateRowSorter(true);
        jTableReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "CustId", "Name", "Address", "Phone", "Credit Amount", "Amount Remited", "Balance"
            }
        ));
        jTableReport.setRowHeight(40);
        jScrollPane16.setViewportView(jTableReport);
        if (jTableReport.getColumnModel().getColumnCount() > 0) {
            jTableReport.getColumnModel().getColumn(3).setResizable(false);
        }

        jButton9.setText("PRINT REPORT");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton30.setText("SHOW DETAILS");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jButton30))
                .addContainerGap())
        );

        javax.swing.GroupLayout ReportLayout = new javax.swing.GroupLayout(Report);
        Report.setLayout(ReportLayout);
        ReportLayout.setHorizontalGroup(
            ReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ReportLayout.setVerticalGroup(
            ReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        MiddlePane.add(Report, "card8");

        Edit_Login.setBackground(new java.awt.Color(51, 51, 51));

        jPanel19.setBackground(new java.awt.Color(51, 51, 51));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Edit login details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel19.setForeground(new java.awt.Color(51, 51, 51));

        jLabel33.setBackground(new java.awt.Color(255, 255, 255));
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Current Password");

        jLabel34.setBackground(new java.awt.Color(255, 255, 255));
        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("New password");

        jLabel35.setBackground(new java.awt.Color(255, 255, 255));
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Cofirm password");

        jPasswordFieldConfirmPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldConfirmPasswordActionPerformed(evt);
            }
        });

        jButton28.setText("Change password");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordFieldNewPassword))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordFieldCurrentPassword))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordFieldConfirmPassword)))
                .addGap(143, 143, 143))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(397, 397, 397)
                .addComponent(jButton28, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addGap(197, 197, 197))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Edit_LoginLayout = new javax.swing.GroupLayout(Edit_Login);
        Edit_Login.setLayout(Edit_LoginLayout);
        Edit_LoginLayout.setHorizontalGroup(
            Edit_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Edit_LoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Edit_LoginLayout.setVerticalGroup(
            Edit_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Edit_LoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePane.add(Edit_Login, "card9");

        jPanel20.setBackground(new java.awt.Color(51, 51, 51));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Print Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jScrollPane22.setBackground(new java.awt.Color(51, 51, 51));

        jPanel21.setBackground(new java.awt.Color(51, 51, 51));

        jScrollPane23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTablePrintOut.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTablePrintOut.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "", "", ""
            }
        ));
        jTablePrintOut.setRowHeight(30);
        jTablePrintOut.setRowMargin(5);
        jScrollPane23.setViewportView(jTablePrintOut);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        jScrollPane22.setViewportView(jPanel21);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 833, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Print_PaneLayout = new javax.swing.GroupLayout(Print_Pane);
        Print_Pane.setLayout(Print_PaneLayout);
        Print_PaneLayout.setHorizontalGroup(
            Print_PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Print_PaneLayout.setVerticalGroup(
            Print_PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        MiddlePane.add(Print_Pane, "card10");

        PurchaseDues.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout PurchaseDuesLayout = new javax.swing.GroupLayout(PurchaseDues);
        PurchaseDues.setLayout(PurchaseDuesLayout);
        PurchaseDuesLayout.setHorizontalGroup(
            PurchaseDuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 845, Short.MAX_VALUE)
        );
        PurchaseDuesLayout.setVerticalGroup(
            PurchaseDuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        MiddlePane.add(PurchaseDues, "card11");

        Footer.setBackground(new java.awt.Color(0, 51, 51));

        CopyrightLabel.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        CopyrightLabel.setForeground(new java.awt.Color(204, 204, 204));
        CopyrightLabel.setText(" © VAG's solution  2016 ");

        javax.swing.GroupLayout FooterLayout = new javax.swing.GroupLayout(Footer);
        Footer.setLayout(FooterLayout);
        FooterLayout.setHorizontalGroup(
            FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FooterLayout.createSequentialGroup()
                    .addContainerGap(461, Short.MAX_VALUE)
                    .addComponent(CopyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(372, Short.MAX_VALUE)))
        );
        FooterLayout.setVerticalGroup(
            FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
            .addGroup(FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FooterLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CopyrightLabel)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Head, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MiddlePane, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE))
            .addComponent(Footer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MiddlePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(Footer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int nStatus = 0;
        
        
        nStatus = AddNewCustomer();
        
        if( 1 == nStatus  )
        {
             JOptionPane.showMessageDialog(rootPane, " New Customer added " );
        }
        else
        {
             JOptionPane.showMessageDialog(rootPane, " Error in adding new customer " );
        }
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        
        DefaultTableModel dm;          // object of table
        DataBaseManager   dbManager;   // Object of databaseManager
        
        dm=(DefaultTableModel) jTableAllCustomer.getModel();
        try
        {
            // creating connection
            dbManager = new DataBaseManager();
            // get all customer details from database.
            ResultSet rs = dbManager.getAllCustomers();
            if( null == rs )
            {
                JOptionPane.showMessageDialog(rootPane, " \n\n No customer available " );
            }
            else
            {
                jTableAllCustomer.setModel( DbUtils.resultSetToTableModel(rs));
                SetVisibility( Show_All_Customers );
            }
            
            // deallocation of memory.
            dbManager.closeConection();
            dbManager = null;
        }
        catch( Exception ex )
        {
            
        }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        SetVisibility( Add_Customer );
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        SetVisibility( Add_New_Credit );
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jXDatePicker_PurchaseDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker_PurchaseDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jXDatePicker_PurchaseDateActionPerformed

    private void jXDatePicker_DueDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker_DueDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jXDatePicker_DueDateActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        
        // search dialog box
        setSearchDialog( jDialogSearch );    
        
        
        
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jTextFieldCustIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCustIdKeyReleased
        // TODO add your handling code here:
        // TextAutoCompleter complete=new TextAutoCompleter(jTextFieldCustId);
        
        
    }//GEN-LAST:event_jTextFieldCustIdKeyReleased

    private void jTextFieldSearchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchFieldKeyReleased
        // TODO add your handling code here:
        DataBaseManager    manager    = null; // Object of database
        ResultSet          rs         = null; // Result set object.
        DefaultTableModel  tableModel = null; // table model.
        
        tableModel = (DefaultTableModel) jTableSearchResult.getModel();
        if( jTextFieldSearchField.getText().length() > 0 )
        {
            try {
                manager = new DataBaseManager();
                rs = manager.GetCustomerDetails( jTextFieldSearchField.getText() );
                if ( null == rs )
                {
                    
                }
                else
                {
                   jTableSearchResult.setModel( DbUtils.resultSetToTableModel(rs));
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                tableModel = null;
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
        
    }//GEN-LAST:event_jTextFieldSearchFieldKeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        if( jTableSearchResult.getSelectedRow() >= 0)
        {
            // set customer id into customer id field
            jTextFieldCustId.setText( String.valueOf(jTableSearchResult.getValueAt( jTableSearchResult.getSelectedRow() ,0)) );
            
            // set customer name and address details to the text area.
            jTextAreaCustDetails.setText( String.valueOf(jTableSearchResult.getValueAt( jTableSearchResult.getSelectedRow() ,1))+"\n"
                                     +String.valueOf(jTableSearchResult.getValueAt( jTableSearchResult.getSelectedRow() ,2))+"\n"
            );
            
            // dispose the search dialog
            jDialogSearch.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(null, " Please select a row to continue..");
        }
        
        
        
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        
        // close butten in the dialog removed.
        jDialogSearch.dispose();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTextFieldCustIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldCustIdMouseClicked
        // TODO add your handling code here:
        jDialogSearch.setSize(800, 500);
        jDialogSearch.setLocationRelativeTo(this);
        jDialogSearch.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jTextFieldCustIdMouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        ClearAddNewCustomerFields();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        ClearAddNewCreditFields();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        
        DataBaseManager manager  = null;
        int         bStatus  = 0;     // Hold the status of the purchase insertion operation. 
        int         nConfirm = 0;     // Hold the status of the conformation dialog box
                                      // true -> add details to database
                                      // else -> allow user to edit the databefore insertin into database.
        
        // This add the details of purchase to the database.        
        // Validation of all the required fields. 
               
        if( jTextFieldCustId.getText()           !=  null &&
            jTextAreaPurchaseRemark.getText()    !=  null &&  
            jXDatePicker_PurchaseDate.getDate()  !=  null &&
            jXDatePicker_DueDate.getDate()       !=  null && 
            jFormattedTextField_Amount.getText() !=  null    )
        {
            
            try {
                
                // add purchase details to the database.
                // create object of database class then add the details to the database.
                manager = new DataBaseManager();
                
                nConfirm = JOptionPane.showConfirmDialog(rootPane, " Do you agree to add this purchase\n\n"
                                                        + "\n Amount Rs :  "+jFormattedTextField_Amount.getText() );
                if( 0 == nConfirm )
                {
                    bStatus = manager.SetPurchaseDetails(   jTextFieldCustId.getText(),
                                                        jTextAreaPurchaseRemark.getText(),
                                                        jXDatePicker_PurchaseDate.getDate(),
                                                        jXDatePicker_DueDate.getDate(),
                                                        jFormattedTextField_Amount.getText());
                
                    // status of the insertion operation.
                    if( 1 == bStatus )
                    {
                        JOptionPane.showMessageDialog( null ," New purchase details added sucessfully.");
                        setPrintOut( jTextFieldCustId.getText() );
                        ClearAddNewCreditFields();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null ," Some error occured during insertion operation.");
                    }
                    
                }
                
                
                // deallocation of object created
                manager.closeConection();
                manager = null;
                
                        
            
            
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // if validation of the input filed.
        else
        {
            JOptionPane.showMessageDialog(rootPane, " Please fill all the input fields " );
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTextFieldCustId1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldCustId1MouseClicked
        // TODO add your handling code here:
        setSearchDialog(jDialogTransactionSearch);
    }//GEN-LAST:event_jTextFieldCustId1MouseClicked

    private void jTextFieldCustId1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCustId1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCustId1KeyReleased

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        setSearchDialog(jDialogTransactionSearch);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTextFieldSearchField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchField1KeyReleased
        // TODO add your handling code here:
        DataBaseManager    manager    = null; // Object of database
        ResultSet          rs         = null; // Result set object.
        DefaultTableModel  tableModel = null; // table model.
        
        tableModel = (DefaultTableModel)jTableSearchResult1.getModel();
        if( jTextFieldSearchField1.getText().length() > 0 )
        {
            try {
                manager = new DataBaseManager();
                rs = manager.GetCustomerDetails( jTextFieldSearchField1.getText() );
                if ( null == rs )
                {
                    
                }
                else
                {
                   jTableSearchResult1.setModel( DbUtils.resultSetToTableModel(rs));
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                tableModel = null;
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
    }//GEN-LAST:event_jTextFieldSearchField1KeyReleased

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        DataBaseManager    manager    = null; // db connection object.
        //DefaultTableModel  tableModel = null; // table model.
        DefaultTableModel model ;
        ResultSet rs;
        rs = null;
        
        model = ( DefaultTableModel )jTablePurchaseDetails.getModel();
        
        if( jTableSearchResult1.getSelectedRow() >= 0)
        {
            // set customer id into customer id field
            jTextFieldCustId1.setText( String.valueOf(jTableSearchResult1.getValueAt( jTableSearchResult1.getSelectedRow() ,0)) );
            
            // set customer name and address details to the text area.
            jTextAreaCustDetails1.setText( String.valueOf(jTableSearchResult1.getValueAt( jTableSearchResult1.getSelectedRow() ,1))+"\n"
                                     +String.valueOf(jTableSearchResult1.getValueAt( jTableSearchResult1.getSelectedRow() ,2))+"\n");
            
            try {
                
                // open a database connection
                // search the purchase of customer id that selected
                // if purchase exists then show it on the table.
                manager = new DataBaseManager();
                rs = manager.getPurchaseDetails( jTextFieldCustId1.getText() );
                if( null == rs )
                {
                    JOptionPane.showMessageDialog(rootPane, " No purchase available for the selected customer." );
                }
                else
                {
                     jTablePurchaseDetails.setModel( DbUtils.resultSetToTableModel(rs));
                }
            
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
           
            
            // dispose the search dialog
            jDialogTransactionSearch.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(null, " Please select a row to continue..");
        }
        
        
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        jDialogTransactionSearch.dispose();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTextFieldCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCustIdActionPerformed
        // TODO add your handling code here:
        setSearchDialog( jDialogSearch );
    }//GEN-LAST:event_jTextFieldCustIdActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        clearAddTransaction();
        SetVisibility( Add_transaction );
        
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTextFieldCustId1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCustId1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCustId1ActionPerformed

    
    // This function set data to the GUI add_amount.
    
    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        setAddAmountForm();
        
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jXDatePickerRemitedDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerRemitedDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jXDatePickerRemitedDateActionPerformed

    // function to add transaction amount to the database.
    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        DataBaseManager manager = null;
        
        // Stores the return status after executing the "AddAmount" function
        int status = 0;
        try
        {
        manager = new DataBaseManager();
        
        if( Double.valueOf( jTextFieldRemitedAmount.getText()) <=
            Double.valueOf(    jTextFieldBalanceDue.getText())  &&
            Double.valueOf(    jTextFieldRemitedAmount.getText()) > 0)
        {
            status = manager.AddAmount(jTextFieldCustId1.getText(),
                              jXDatePickerRemitedDate.getDate(),
                              jTextFieldRemitedAmount.getText()
                              );
            if(1 == status)
            {
                JOptionPane.showMessageDialog(rootPane, "Your transaction has been updated!");
            }
        }
        else
        {
          JOptionPane.showMessageDialog(rootPane, " Please check the amount \n\n Amount is not valid..");
        }
        
        setAddAmountForm();
        
        if( Double.valueOf(jTextFieldBalanceDue.getText()) <= 0 )
        {
           manager.SetAccountState( jTextFieldCustId1.getText());
                   
        }
        
        clearAddAmountForm();
        manager.closeConection();
        manager = null;
        }
        catch( Exception e)
        {
            
        }
        
                   
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        clearAddAmountForm();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        clearAddTransaction();
        SetVisibility( Add_transaction );
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        clearAddTransaction();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jTextFieldSearchField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchField2KeyReleased
        // TODO add your handling code here:
         // TODO add your handling code here:
        DataBaseManager    manager    = null; // Object of database
        ResultSet          rs         = null; // Result set object.
        DefaultTableModel  tableModel = null; // table model.
        
        tableModel = (DefaultTableModel) jTableSearchResult2.getModel();
        if( jTextFieldSearchField2.getText().length() > 0 )
        {
            try {
                manager = new DataBaseManager();
                rs = manager.GetCustomerDetails( jTextFieldSearchField2.getText() );
                if ( null == rs )
                {
                    
                }
                else
                {
                   jTableSearchResult2.setModel( DbUtils.resultSetToTableModel(rs));
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                tableModel = null;
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }//GEN-LAST:event_jTextFieldSearchField2KeyReleased

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        DataBaseManager manager = null;
        ResultSet rs = null;
        if( jTableSearchResult2.getSelectedRow() >= 0)
        {
            // set customer id into customer id field
            jTextFieldRemoveCustID.setText( String.valueOf(jTableSearchResult2.getValueAt( jTableSearchResult2.getSelectedRow() ,0)) );
            
            // set customer name and address details to the text area.
            jTextAreaRemoveCustDetails.setText( String.valueOf(jTableSearchResult2.getValueAt( jTableSearchResult2.getSelectedRow() ,1))+"\n"
                                     +String.valueOf(jTableSearchResult2.getValueAt( jTableSearchResult2.getSelectedRow() ,2))+"\n"
            );
            
            try
            {
                // Getting the purchase details
                manager = new DataBaseManager();
                rs = manager.getPurchaseDetails( jTextFieldRemoveCustID.getText());
                
                // Setting the Purchase remark
                String remark = null;
                while(rs.next())
                {
                    remark = rs.getString("purchaseRemark");
                }
                jTextAreaRemovePurchaseDetails.setText(remark);
            }
            catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }         
            
            // dispose the search dialog
            jDialogSearchForRemovingCustomer.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(null, " Please select a row to continue..");
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        jDialogSearchForRemovingCustomer.dispose();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jTextFieldRemoveCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRemoveCustIDActionPerformed
        // TODO add your handling code here:
        setSearchDialog(jDialogSearchForRemovingCustomer);
    }//GEN-LAST:event_jTextFieldRemoveCustIDActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        setSearchDialog(jDialogSearchForRemovingCustomer);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        SetVisibility(Remove_Customer);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextFieldSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSearchFieldActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        jTextFieldRemoveCustID.setText( null );
        jTextAreaRemoveCustDetails.setText( null );
        jTextAreaRemovePurchaseDetails.setText( null );
    }//GEN-LAST:event_jButton24ActionPerformed

    private void RemoveCustomer_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveCustomer_ButtonActionPerformed
        // TODO add your handling code here:
        try
        {
            DataBaseManager manager = new DataBaseManager();
            if(jTextFieldRemoveCustID != null )
            {
                String deleteStatus = manager.DeleteRow(jTextFieldRemoveCustID.getText());
                if(deleteStatus == "DELETED")
                {
                    JOptionPane.showMessageDialog(null, "Customer has been deleted");
                    jTextFieldRemoveCustID.setText( null );
                    jTextAreaRemoveCustDetails.setText( null );
                    jTextAreaRemovePurchaseDetails.setText( null );
                }
                else if(deleteStatus == "NOROWS")
                {
                    JOptionPane.showMessageDialog(null, "Customer cannot be rmoved.");
                }
                else if(deleteStatus == "HASOPEN")
                {  
                    JOptionPane.showMessageDialog(null, "Customer has open transactions! \nHence, you cannot remove the customer!");
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Customer ID could not be found");
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_RemoveCustomer_ButtonActionPerformed

    private void jTextFieldRemoveCustIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldRemoveCustIDMouseClicked
        // TODO add your handling code here:
        jDialogSearchForRemovingCustomer.setSize(800, 500);
        jDialogSearchForRemovingCustomer.setLocationRelativeTo(this);
        jDialogSearchForRemovingCustomer.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jTextFieldRemoveCustIDMouseClicked

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportActionPerformed
        // TODO add your handling code here:
        ResultSet rsCust        = null;
        ResultSet rs            = null;
        DataBaseManager manager = null;
        DefaultTableModel tm; 
        String szTotal   = null;
        String szRemited = null;
        double dBalance = 0;
        
        try {
            tm = (DefaultTableModel)jTableReport.getModel();
            tm.setRowCount(0);
            manager = new DataBaseManager();
            rsCust = manager.getAllCustomers();
            if( null!= rsCust )
            {
                    
                    while ( rsCust.next() )
                    {


                        // add total amount
                        rs = manager.getTotalAmount( rsCust.getString(1) );

                        if( null!=rs  )
                        {
                            rs.next();
                            szTotal = rs.getString(1);

                        }

                        rs = manager.getRemitedAmount(rsCust.getString(1) );

                        if( null!=rs )
                        {
                            rs.next();
                            szRemited = rs.getString(1);

                        }
                        if( szTotal != null && szRemited != null )
                        {
                            dBalance = Double.valueOf(szTotal) - Double.valueOf(szRemited);
                        }
                        else if( szTotal != null )
                        {
                            dBalance = Double.valueOf(szTotal);
                        }

                        tm.addRow(new Object[]{rsCust.getString(1),
                                               rsCust.getString(2),
                                               rsCust.getString(3),
                                               rsCust.getString(4),
                                               szTotal,
                                               szRemited,
                                               dBalance                    




                        });

                        szTotal   = null;
                        szRemited = null;
                        dBalance  = 0;
                    }

                    double totalPurchase =  manager.getTotalPurchasedAmount();
                    double totalRemited  =  manager.getTotalRemitedAmount();
                    double balanceDue    = totalPurchase - totalRemited;
                    tm.addRow(new Object[]{" ",
                                        " ",
                                        " ",
                                        "<html><b><font color = 'red' >Total</font></b></html>",
                                        "<html><b><font color = 'red' >"+totalPurchase+"</font></b></html>",
                                        "<html><b><font color = 'red' >"+totalRemited+"</font></b></html>",
                                        "<html><b><font color = 'red' >"+balanceDue+"</font></b></html>"  
                    });  
            }
            
            
            
            SetVisibility(Report);
           
            
            
            manager = null;
            
        }
        catch( Exception e )
        {
             Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
        
        
        
        
    }//GEN-LAST:event_jButtonReportActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        LoginForm login;
        try {
            login = new LoginForm();
            login.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
                
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        setSearchDialog( jDialogSearchCustomer );
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextFieldSearchField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSearchField3ActionPerformed

    private void jTextFieldSearchField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchField3KeyReleased
        // TODO add your handling code here:
        DataBaseManager    manager    = null; // Object of database
        ResultSet          rs         = null; // Result set object.
        DefaultTableModel  tableModel = null; // table model.
        
        tableModel = (DefaultTableModel) jTableSearchResult3.getModel();
        if( jTextFieldSearchField3.getText().length() > 0 )
        {
            try {
                manager = new DataBaseManager();
                rs = manager.GetCustomerDetails( jTextFieldSearchField3.getText() );
                if ( null == rs )
                {
                    
                }
                else
                {
                   jTableSearchResult3.setModel( DbUtils.resultSetToTableModel(rs));
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                tableModel = null;
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }//GEN-LAST:event_jTextFieldSearchField3KeyReleased

    private void jDisalogSearchCustomer_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDisalogSearchCustomer_closeActionPerformed
        // TODO add your handling code here:
        jDialogSearchCustomer.dispose();
    }//GEN-LAST:event_jDisalogSearchCustomer_closeActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        SetVisibility( Edit_Login );
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jPasswordFieldConfirmPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldConfirmPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordFieldConfirmPasswordActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        
        DataBaseManager manager = null; // Hold tha object of database manager to establish the connection.
        String userType         = null; // Hold the type of the user 
                                        // if userType = null indicate that credential is not valid
        
        int bStatus = 0;        // Hold the status of password change.             
        
        // code check the input validation of text fields
        // check that all text field have some value - > Not null
        if( jPasswordFieldCurrentPassword.getPassword().length > 0 &&
            jPasswordFieldNewPassword.getPassword().length > 0     &&
            jPasswordFieldConfirmPassword.getPassword().length > 0    )
        {        
         
            
        try {
                // check that both new password and confirm password are equal ?
                if( new String( jPasswordFieldNewPassword.getPassword()).equals
                  ( new String( jPasswordFieldConfirmPassword.getPassword() ) ) )
                {
                    // open database connection.
                    manager = new DataBaseManager();
                    
                    // check that current password is valid
                    userType = manager.getUserType("Admin",
                                         new String(jPasswordFieldCurrentPassword.getPassword()));
                    // if current password is valid
                    if( null != userType )
                    {
                        bStatus = manager.ChangePassword( "Admin",
                                    new String( jPasswordFieldNewPassword.getPassword()) );
                        
                        if( 1 == bStatus )
                        {
                            JOptionPane.showMessageDialog(rootPane," Password changed.\n\n "
                                                                    + " Changes will affected from next login");
                            ClearChangePassword();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(rootPane," Password change failed. ");
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane," Current password is not valid. ");
                    }
                    
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane," The new password and confirm password should be equal..");
                }
                
                
                
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(AdminForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane," please enter value to all fields..");
                
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        int nStatus = 0;
        
        
        nStatus = AddNewCustomer();
        
        if( 1 == nStatus  )
        {
             
             
        }
        else
        {
             JOptionPane.showMessageDialog(rootPane, " Error in adding new customer " );
        }
        
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jDisalogSearchCustomer_close1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDisalogSearchCustomer_close1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = null;
        model = ( DefaultTableModel )jTableSearchResult3.getModel();
        
        if( jTableSearchResult3.getSelectedRow() >= 0)
        {
            // set customer id into customer id field
            
           
        setPrintOut( String.valueOf(jTableSearchResult3.getValueAt( jTableSearchResult3.getSelectedRow() ,0)) );
        jDialogSearchCustomer.setVisible(false);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"please select a row to continue" );
        }
        
    }//GEN-LAST:event_jDisalogSearchCustomer_close1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

            // TODO add your handling code here:
        try
        {
            MessageFormat header = new MessageFormat("REPORT");
            jTableReport.print(JTable.PrintMode.FIT_WIDTH,header,null);
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog(null, e.getMessage() );
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButtonReport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReport1ActionPerformed
        // TODO add your handling code here:
        PaymentDueCustomers dueustomers = new PaymentDueCustomers();
        dueustomers.SetTableDetails();
        dueustomers.setVisible(true);
    }//GEN-LAST:event_jButtonReport1ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        int row = jTableReport.getSelectedRow();
        if( -1 == row )
        {
            JOptionPane.showMessageDialog(rootPane," Please select a row from the list ");
        }
        else
        {
            ShowCustomerDetails details = new ShowCustomerDetails(this,true);
            try {
                String custId = jTableReport.getValueAt(row,0).toString();
                details.showDialoge(custId);
                custId = null;
                
            } catch (Exception ex) {
                Logger.getLogger(PaymentDueCustomers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_jButton30ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Add_Customer;
    private javax.swing.JPanel Add_New_Credit;
    private javax.swing.JPanel Add_amount;
    private javax.swing.JPanel Add_transaction;
    private javax.swing.JLabel CopyrightLabel;
    private javax.swing.JPanel Edit_Login;
    private javax.swing.JPanel Footer;
    private javax.swing.JPanel Head;
    private javax.swing.JLabel Logolabel;
    private javax.swing.JPanel MiddlePane;
    private javax.swing.JPanel Print_Pane;
    private javax.swing.JPanel PurchaseDues;
    private javax.swing.JButton RemoveCustomer_Button;
    private javax.swing.JPanel Remove_Customer;
    private javax.swing.JPanel Report;
    private javax.swing.JPanel Show_All_Customers;
    private javax.swing.JPanel SideMenu;
    private javax.swing.JPanel WelcomePane;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonReport;
    private javax.swing.JButton jButtonReport1;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JDialog jDialogSearch;
    private javax.swing.JDialog jDialogSearchCustomer;
    private javax.swing.JDialog jDialogSearchForRemovingCustomer;
    private javax.swing.JDialog jDialogTransactionSearch;
    private javax.swing.JButton jDisalogSearchCustomer_close;
    private javax.swing.JButton jDisalogSearchCustomer_close1;
    private javax.swing.JFormattedTextField jFormattedTextField_Amount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCustId;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordFieldConfirmPassword;
    private javax.swing.JPasswordField jPasswordFieldCurrentPassword;
    private javax.swing.JPasswordField jPasswordFieldNewPassword;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTableAllCustomer;
    private javax.swing.JTable jTablePrintOut;
    private javax.swing.JTable jTablePurchaseDetails;
    private javax.swing.JTable jTableReport;
    private javax.swing.JTable jTableSearchResult;
    private javax.swing.JTable jTableSearchResult1;
    private javax.swing.JTable jTableSearchResult2;
    private javax.swing.JTable jTableSearchResult3;
    private javax.swing.JTable jTableTransactions;
    private javax.swing.JTextArea jTextAreaAddress;
    private javax.swing.JTextArea jTextAreaCustDetails;
    private javax.swing.JTextArea jTextAreaCustDetails1;
    private javax.swing.JTextArea jTextAreaCustDetails2;
    private javax.swing.JTextArea jTextAreaPurchaseRemark;
    private javax.swing.JTextArea jTextAreaRecomenter;
    private javax.swing.JTextArea jTextAreaRemark;
    private javax.swing.JTextArea jTextAreaRemoveCustDetails;
    private javax.swing.JTextArea jTextAreaRemovePurchaseDetails;
    private javax.swing.JTextField jTextFieldBalanceDue;
    private javax.swing.JTextField jTextFieldCustId;
    private javax.swing.JTextField jTextFieldCustId1;
    private javax.swing.JTextField jTextFieldCustomerName;
    private javax.swing.JTextField jTextFieldPhone;
    private javax.swing.JTextField jTextFieldRemited;
    private javax.swing.JTextField jTextFieldRemitedAmount;
    private javax.swing.JTextField jTextFieldRemoveCustID;
    private javax.swing.JTextField jTextFieldSearchField;
    private javax.swing.JTextField jTextFieldSearchField1;
    private javax.swing.JTextField jTextFieldSearchField2;
    private javax.swing.JTextField jTextFieldSearchField3;
    private javax.swing.JTextField jTextFieldTotalAmount;
    // End of variables declaration//GEN-END:variables
}
