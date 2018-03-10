/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package debitmanager;

import Database.DataBaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author arun
 */
public class ShowCustomerDetails extends javax.swing.JDialog {

    /**
     * Creates new form ShowCustomerDetails
     */
    
    String szcustId = null;
    public ShowCustomerDetails(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    public void showDialoge( String custId ){
        
        szcustId = custId;
        if( szcustId != null ){
            setPrintOut(szcustId);
            setVisible(true);
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
                     
                      dm.addRow(new Object[] {rs.getString("StartDate"),rs.getString("Amount")+"   (  "+rs.getString("purchaseRemark")+"  ) DUE DATE : "+rs.getString("DueDate")});
                      
                   }
                }
                
                // get purchase totaladmin  
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePrintOut = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/1485640864_purchase.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("PURCHASE DETAILS");
        jLabel3.setToolTipText("");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 51, 153));
        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTablePrintOut.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "", ""
            }
        ));
        jTablePrintOut.setRowHeight(25);
        jScrollPane1.setViewportView(jTablePrintOut);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
         try
        {
            MessageFormat header = new MessageFormat("CUSTOMER PURCHASE REPORT");
            jTablePrintOut.print(JTable.PrintMode.FIT_WIDTH,header,null);
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog(null, e.getMessage() );
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(ShowCustomerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShowCustomerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShowCustomerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShowCustomerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ShowCustomerDetails dialog = new ShowCustomerDetails(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePrintOut;
    // End of variables declaration//GEN-END:variables
}
