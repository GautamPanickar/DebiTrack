/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package debitmanager;

import Database.DataBaseManager;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author arun
 */
public class LoginForm extends javax.swing.JFrame {

    /**
     * Creates new form LoginForm
     */
    public LoginForm() throws ClassNotFoundException {
        
        initComponents();
        setLocationRelativeTo(null);
        DataBaseManager manager;
        try {
            manager = new DataBaseManager();
            manager.initTable();
        } catch (SQLException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage() );
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMain = new javax.swing.JPanel();
        Logolabel = new javax.swing.JLabel();
        jPanelLogin = new javax.swing.JPanel();
        jPanelLoginContainer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldUserName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButtonLogin = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jPasswordFieldUserPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        Logopic = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        CopyrightLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        jPanelMain.setBackground(new java.awt.Color(0, 51, 51));

        Logolabel.setFont(new java.awt.Font("Miriam Fixed", 0, 24)); // NOI18N
        Logolabel.setForeground(new java.awt.Color(204, 204, 204));
        Logolabel.setText("DebiTRACK");

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logolabel, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logolabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanelLogin.setBackground(new java.awt.Color(30, 35, 35));

        jPanelLoginContainer.setBackground(new java.awt.Color(0, 102, 102));
        jPanelLoginContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelLoginContainer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Username");
        jLabel1.setToolTipText("");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jTextFieldUserName.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldUserName.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jTextFieldUserName.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Password");

        jButtonLogin.setBackground(new java.awt.Color(51, 51, 51));
        jButtonLogin.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButtonLogin.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogin.setText("Log in");
        jButtonLogin.setBorder(null);
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        jButtonClear.setBackground(new java.awt.Color(51, 51, 51));
        jButtonClear.setFont(new java.awt.Font("Miriam Fixed", 1, 12)); // NOI18N
        jButtonClear.setForeground(new java.awt.Color(255, 255, 255));
        jButtonClear.setText("Clear");
        jButtonClear.setBorder(null);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jPasswordFieldUserPassword.setBackground(new java.awt.Color(204, 204, 204));
        jPasswordFieldUserPassword.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        jPasswordFieldUserPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPasswordFieldUserPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordFieldUserPasswordKeyPressed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        jLabel4.setText("!!!Acessing this system without the permission of administrator is prohibited!!!");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Logopic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/debitmanager/images/Logo.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanelLoginContainerLayout = new javax.swing.GroupLayout(jPanelLoginContainer);
        jPanelLoginContainer.setLayout(jPanelLoginContainerLayout);
        jPanelLoginContainerLayout.setHorizontalGroup(
            jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logopic, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLoginContainerLayout.createSequentialGroup()
                        .addGroup(jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelLoginContainerLayout.createSequentialGroup()
                                .addComponent(jButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPasswordFieldUserPassword))
                        .addGap(56, 56, 56))
                    .addGroup(jPanelLoginContainerLayout.createSequentialGroup()
                        .addComponent(jTextFieldUserName)
                        .addGap(55, 55, 55))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLoginContainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(38, 38, 38))
        );
        jPanelLoginContainerLayout.setVerticalGroup(
            jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginContainerLayout.createSequentialGroup()
                .addGroup(jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLoginContainerLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jPasswordFieldUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLoginContainerLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(Logopic)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelLogin.add(jPanelLoginContainer);

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));

        CopyrightLabel.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        CopyrightLabel.setForeground(new java.awt.Color(204, 204, 204));
        CopyrightLabel.setText(" © VAG's solution  2016 ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1043, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(461, Short.MAX_VALUE)
                    .addComponent(CopyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(372, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CopyrightLabel)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 1043, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        // TODO add your handling code here:
        jTextFieldUserName.setText( null );
        jPasswordFieldUserPassword.setText( null );
    }//GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        try 
        {
            String szUserType = null;
            //String szUserId   = null; // Hold the user id that recieved from text box
            //String szPassword = null; // Hold the password recieved from password box.
            // TODO add your handling code here:
            
            // creating object of database
            DataBaseManager dbManager = new DataBaseManager();
            
            // Initializing  date format and getting the current date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");       
            String date= dateFormat.format(new Date());
            Dimension screenSize      = Toolkit.getDefaultToolkit().getScreenSize();
            
            // recieved data from text boxes.
            if( jTextFieldUserName.getText().length() < 30 &&
                jPasswordFieldUserPassword.getPassword().length < 30 )
            {
                
            
                // function return the user type if the user name and password match..
                // user name and password that user entered passed as argument.
                szUserType = dbManager.getUserType(jTextFieldUserName.getText(), 
                                        new String( jPasswordFieldUserPassword.getPassword() ));

                // user type is null show username and password does not match
                if( null == szUserType )
                {
                   JOptionPane.showMessageDialog(rootPane, " Please check your login credentials ..");
                }
                // user used this system is admin
                else if( szUserType.equals( "admin" ))
                {
                   AdminForm admin = new AdminForm();
                   admin.setBounds(1,1,(int)screenSize.getWidth(),(int)screenSize.getHeight());
                   admin.setVisible(true);
                   
                   this.hide();
                   
                   // For handling the due date scenarios 
                   try
                   {
                       Date currentDate = dateFormat.parse(date);
                       ResultSet rsPay = dbManager.getPurchaseDetails(currentDate);
                       if(!rsPay.next())
                       {                
                           // Dont do anything nothing to handle here
                       }
                       else
                       {
                           PaymentDueCustomers payment = new PaymentDueCustomers();
                           payment.setVisible(true);
                           payment.SetTableDetails();
                       }
                   }
                   catch(ParseException e)
                   {
                       JOptionPane.showMessageDialog( rootPane, e.getMessage());
                   }
                }
                // user is staff
                else if( szUserType.equals( "staff" ) )
                {
                   JOptionPane.showMessageDialog( rootPane, "staff" );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( rootPane, " user name / password too long" );
            }
                    
                   
                    
        }
        catch( ClassNotFoundException | SQLException ex )
        {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage() );
        }
           
    }//GEN-LAST:event_jButtonLoginActionPerformed

    private void jPasswordFieldUserPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordFieldUserPasswordKeyPressed
        // Handling login by hitting Enter key
        if (evt.getKeyCode()==KeyEvent.VK_ENTER)
        {        
            
        try 
        {
            String szUserType = null;
            //String szUserId   = null; // Hold the user id that recieved from text box
            //String szPassword = null; // Hold the password recieved from password box.
            // TODO add your handling code here:
            
            // creating object of database
            DataBaseManager dbManager = new DataBaseManager();
            // Initializing  date format and getting the current date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");       
            String date= dateFormat.format(new Date());
            // recieved data from text boxes.
            if( jTextFieldUserName.getText().length() < 30 &&
                jPasswordFieldUserPassword.getPassword().length < 30 )
            {
                
            
                // function return the user type if the user name and password match..
                // user name and password that user entered passed as argument.
                szUserType = dbManager.getUserType(jTextFieldUserName.getText(), 
                                        new String( jPasswordFieldUserPassword.getPassword() ));

                // user type is null show username and password does not match
                if( null == szUserType )
                {
                   JOptionPane.showMessageDialog(rootPane, " Please check your login credentials ..");
                }
                // user used this system is admin
                else if( szUserType.equals( "admin" ))
                {
                   AdminForm admin = new AdminForm();
                   admin.setVisible(true);
                   this.hide();
                   
                   // For handling the due date scenarios 
                   try
                   {
                       Date currentDate = dateFormat.parse(date);
                       ResultSet rsPay = dbManager.getPurchaseDetails(currentDate);
                       if(!rsPay.next())
                       {
                           // Dont do anything nothing to handle here
                       }
                       else
                       {
                           PaymentDueCustomers payment = new PaymentDueCustomers();
                           payment.setVisible(true);
                           payment.SetTableDetails();
                       }
                   }
                   catch(ParseException e)
                   {
                       JOptionPane.showMessageDialog( rootPane, e.getMessage());
                   }
                }
                // user is staff
                else if( szUserType.equals( "admin" ) )
                {
                   JOptionPane.showMessageDialog( rootPane, "staff" );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( rootPane, " user name / password too long" );
            }
                    
                   
                    
        }
        catch( ClassNotFoundException | SQLException ex )
        {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage() );
        }
        }
    }//GEN-LAST:event_jPasswordFieldUserPasswordKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CopyrightLabel;
    private javax.swing.JLabel Logolabel;
    private javax.swing.JLabel Logopic;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelLogin;
    private javax.swing.JPanel jPanelLoginContainer;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPasswordField jPasswordFieldUserPassword;
    private javax.swing.JTextField jTextFieldUserName;
    // End of variables declaration//GEN-END:variables
}
