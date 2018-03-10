/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package debitmanager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author arun
 */
public class DebitManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            // Setting the look and feel for the application
            try {
                // Set cross-platform Java L&F (also called "Metal")
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            }
            catch (UnsupportedLookAndFeelException e) {
                // handle exception
            }
            catch (ClassNotFoundException e) {
                // handle exception
            }
            catch (InstantiationException e) {
                // handle exception
            }
            catch (IllegalAccessException e) {
                // handle exception
            }
            // creating object of login form
            // user login page is the first page of thisapplication
            // if user is a staff then the login widow direct the control to staff page.
            // if user ia an admin then directed the page to admin window.
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible( true );
        } 
    catch (ClassNotFoundException ex) {
            Logger.getLogger(DebitManager.class.getName()).log(Level.SEVERE, null, ex);
       // handle exception
    }
    }
    
}
