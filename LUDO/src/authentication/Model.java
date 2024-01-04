/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package authentication;




import javax.swing.JSlider;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.mail.internet.*;
import javax.mail.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import setup_game.Setting;
 


/**
 *
 * @author Azeren
 */
public class Model extends ModelAbstract {

    public static Clip clip;

    public Model() {
    }
    
    public static boolean isValidEmail(String email) {
        String EMAIL_REGEX ="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static boolean isValidPassword(String password) {
        // Check if the password meets the length requirement (at least 8 characters)
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(null, "Password length need " + " " + "at least 6 characters");
            return false;
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            JOptionPane.showMessageDialog(null, "Password must contains" + " " + "at least one uppercase letter");
            return false;
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            JOptionPane.showMessageDialog(null, "Password must contains" + " " + "at least one lowercase letter");
            return false;
        }

        // Check if the password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(null, "Password must contains" + " " + "at least one digit");
            return false;
        }

        // If all criteria are met, the password is valid
        return true;
    }
    
    public static boolean isValidUsername(String username) {
        // Check if the username meets the length requirement (between 3 and 20 characters)
        if (username.length() < 3 || username.length() > 20) {
            JOptionPane.showMessageDialog(null, "username length need" + " " + "at least 3-20 characters");
            return false;
        }

        // If all criteria are met, the username is valid
        return true;
    }
    
    public static void Input(String username,String password,String email,String username_db,String password_db){
        String url = "jdbc:mysql://user-farhanabdullah52-afe7.a.aivencloud.com:23185/defaultdb?ssl-mode=REQUIRED";
        
        if (isValidEmail(email)) {
            if(CheckUsername(username,username_db,password_db)){
                JOptionPane.showMessageDialog(null, "Username" + " " + username + " " + "Already exist");         
            }else if(!isValidPassword(password)){
            }else if(!isValidUsername(username)){
            }else{
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection= DriverManager.getConnection(url,username_db,password_db);

                    PreparedStatement statement=connection.prepareStatement("INSERT INTO userdata (username,password,email) VALUES (?,?,?)");
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.setString(3, email);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Register Success");
                        
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "Register Failed");
                    }
                    connection.close();
                    
                } catch (Exception e) {
                    System.out.println(e);
                }               
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid email address");
        }
    }
    
    public static boolean Validate(String username,String password,String username_db,String password_db){
        String url = "jdbc:mysql://user-farhanabdullah52-afe7.a.aivencloud.com:23185/defaultdb?ssl-mode=REQUIRED";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection(url,username_db,password_db);
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select * from userdata");
            while(resultSet.next()){
                if(resultSet.getString(2).equals(username) && resultSet.getString(3).equals(password)){
                    return true;
                }
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    public static boolean CheckUsername(String username,String username_db,String password_db){
        String url = "jdbc:mysql://user-farhanabdullah52-afe7.a.aivencloud.com:23185/defaultdb?ssl-mode=REQUIRED";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection(url,username_db,password_db);
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select * from userdata");
            while(resultSet.next()){
                if(resultSet.getString(2).equals(username)){
                    return true;
                }
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    public static void Sendpassword(String Email) {
        // Sender's email address and password
        String senderEmail = "ludoonline52";
        String senderPassword = "ccikazijbzgatzld";

        // Recipient's email address
        String recipientEmail = Email;

        // Properties for the mail session
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        
        // Create a Session object with an Authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        
        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set the email subject and content
            message.setSubject("Hello from JavaMail");
            if(ForgetPassword(Email,"avnadmin","AVNS_0fgqDxdGHgxOLRkmnQj").equals("Your email not registered in LUDO")){
                JOptionPane.showMessageDialog(null, "Email not registered");
            }else{
                message.setText(ForgetPassword(Email,"avnadmin","AVNS_0fgqDxdGHgxOLRkmnQj"));
                
                Transport.send(message);
                JOptionPane.showMessageDialog(null, "Password Already sent to your email");
            }
            
            
            

            

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    //ForgetPassword(Email,"sql12672656","sql12672656","etUCGeSRaz")
    public static String ForgetPassword(String email,String username_db,String password_db){
        String url = "jdbc:mysql://user-farhanabdullah52-afe7.a.aivencloud.com:23185/defaultdb?ssl-mode=REQUIRED";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection(url,username_db,password_db);
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select * from userdata");
            while(resultSet.next()){
                if(resultSet.getString(4).equals(email)){
                    return "Username & Password: " + resultSet.getString(2) + " " + resultSet.getString(3);
                }
            }
            
            
            connection.close();
            return "Your email not registered in LUDO";
            
        } catch (Exception e) {
            System.out.println(e);
            return "Your email not registered in LUDO";
        }
        
    }
    public static void Play(){
        Setting setting = new Setting();
        initializeAudioClip();
        playMusic(); 
    }
    
    public static void initializeAudioClip() {
        try {
            File audioFile = new File("music/1.wav"); // Replace with your audio file path
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioInputStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playMusic() {
        if (clip != null) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            setVolume(Setting.Music.getValue());
        }
    }

    public static void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }
    public static void setVolume(int volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float gain = (float) (Math.log10(volume / 100.0) * 20.0);
            gainControl.setValue(gain);
        }
    }
}
