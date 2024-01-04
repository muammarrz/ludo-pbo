/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package authentication;


import javax.swing.JOptionPane;

/**
 *
 * @author Azeren
 */
public class Controller {

    public static void view(javax.swing.JFrame frame,String GUI){
        if (GUI.equals("Register")){
            View.ShowRegister();
            frame.dispose();
        }else if (GUI.equals("Homepage")){
            View.ShowHomepage();
            frame.dispose();
        }else if (GUI.equals("Popup")){
            View.ShowPopup();
            
        }else if (GUI.equals("Login")){
            View.ShowLogin();
            frame.dispose();
        }else if (GUI.equals("Setting")){
            View.ShowSetting();
            frame.dispose();
        }else if (GUI.equals("ForgetPassword")){
            View.ShowForgetPassword();
        }else{
            JOptionPane.showMessageDialog(null, "GUI not Exist");
        }
    }
    public static boolean Validate(String username,String password,String username_db,String password_db){
        return  Model.Validate(username,password,username_db,password_db);
    }
    public static void Input(String username,String password,String email,String Db_name,String username_db,String password_db){
        Model.Input(username, password, email, username_db, password_db);
    }
    public static void Sendpassword(String email){
        Model.Sendpassword(email);
    }
    public static boolean isValidEmail(String email){
        return Model.isValidEmail(email);
    }
    public static void PlayMusic(){
        Model.Play();
    }
    public static void StopMusic(){
        Model.stopMusic();
    }
}
