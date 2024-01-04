/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package authentication;

/**
 *
 * @author Azeren
 */
public abstract class ModelAbstract {
    public static void Input(String username, String password, String email, String username_db, String password_db){}
    
    public static boolean Validate(String username, String password, String username_db, String password_db) {
        // Implementation of validation logic
        return false;
    }
    
    public static void SendPassword(String email) {
        // Implementation of sending password logic
    }
}

