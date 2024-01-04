/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package authentication;


import javax.swing.JDialog;
import setup_game.Homepage;
import setup_game.PlayOption;
import setup_game.Setting;

/**
 *
 * @author Azeren
 */
public class View extends Show implements ViewInterface {

    
    public static void ShowLogin(){
        Login login = new Login();
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
    }
    public static void ShowRegister(){
        Register register = new Register();
        register.setVisible(true);
        register.pack();
        register.setLocationRelativeTo(null);
    }
    public static void ShowHomepage(){
        Homepage StartMenu = new Homepage();
        StartMenu.setVisible(true);
        StartMenu.pack();
        StartMenu.setLocationRelativeTo(null);
    }
    
    public static void ShowPopup(){
        PlayOption popup = new PlayOption();
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        popup.setVisible(true);
        popup.setLocationRelativeTo(null);
    }
    public static void ShowSetting(){
        Setting setting = new Setting();
        setting.setVisible(true);
        setting.pack();
        setting.setLocationRelativeTo(null);
    }
    public static void ShowForgetPassword(){
        ForgetPassword popup = new ForgetPassword();
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        popup.setVisible(true);
        popup.setLocationRelativeTo(null);
    }
}
