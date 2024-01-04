package ludo;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import static ludo.GameState.DEFAULT_THEME;
import ludo.ImgPath.Color;
import ludo.ImgPath.Theme;

/**
 * Represents a select window.
 */

public class Selector extends JDialog implements MouseListener{
    public static final String IMG_PATH ="images\\";
    public static final String DIR_PATH ="\\";
    public static final String DEFAULT_IMG ="blue";
    public static final String UNSELECTED_IMG ="gray";
    public static final String IMG_FORMAT =".png";
    
    public static final String LABEL_FONT ="Arial Black";
    public static final int LABEL_FONT_SIZE =14;
    
    public static final int BUTTON_WIDTH =40;
    public static final int BUTTON_HEIGHT =30;
    
    private final List<String> PLAYER_OPTIONS = Arrays.asList("Yellow", "Red", "Green", "Blue"); 
    private final List<String> THEME_OPTIONS = Arrays.asList("Plain", "Solid", "Fruits");
    private final List<String> BOARD_OPTIONS = Arrays.asList("Regular", "Special");
    private final List<String> DICE_OPTIONS = Arrays.asList("Regular", "Special");
    private Map<String, List<String>> listOfOptions = new HashMap<>();
                      
    private JPanel selectWindow, options;
    private JLabel text;
    private JButton ok;
    private BufferedImage selected, unselected;
    private ArrayList<JRadioButton> buttons = new ArrayList<>();
    private ArrayList<String> selectedOptions = new ArrayList<>();
    
     /**
     * Initializes an instance of Selector according to the given type
     */
    public Selector(String type){
        initComponents(type, null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    /**
     * Initializes an instance of Selector with options according to the given type and theme
     */
    public Selector(String type, String theme) {
        initComponents(type, theme);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    /**
     * Initializes an instance of Selector specifying the index of the current player at the moment the selector windows is shown
     */
    public Selector(String type, String theme, String currentPlayer) {
        initComponents(type, theme);
        this.setLocationRelativeTo(null);
        for(JRadioButton b:this.buttons)
            if(b.getText().equalsIgnoreCase(currentPlayer)){
                b.setSelected(true);
                b.setEnabled(false);}
        this.setVisible(true);
    }
    
     /**
     * Creates the label, radioButtons (options) and confirmation button of the selector window
     */
    private void initComponents(String type, String theme) {       
        this.setTitle("Select "+type);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setModal(true);
        
        selectWindow = new JPanel();
        text = new JLabel();
        options = new JPanel();
        
        listOfOptions.put("player", PLAYER_OPTIONS);
        listOfOptions.put("theme", THEME_OPTIONS);
        listOfOptions.put("board", BOARD_OPTIONS);
        listOfOptions.put("dice", DICE_OPTIONS);
        
        ok = new JButton();        

        this.add(selectWindow);
        selectWindow.setLayout(new BoxLayout(selectWindow, BoxLayout.Y_AXIS));
        
        this.setPreferredSize(new Dimension(200, 350));
        selectWindow.add(Box.createVerticalGlue());
        
        text.setFont(new Font(LABEL_FONT, 1, LABEL_FONT_SIZE+4));
        text.setText("Select "+type+":");
        selectWindow.add(text);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectWindow.add(Box.createVerticalGlue());
        
        selectWindow.add(options);
        addOptions(options, type, theme);
        
        selectWindow.add(Box.createVerticalGlue());
        ok.setText("OK");
        selectWindow.add(ok);
        ok.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        ok.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectWindow.add(Box.createVerticalGlue());
        
        ok.addMouseListener(this);
        
        pack();
    }      
    
    /**
     * @return the path of the images to be used as radioButton icons
     */
    private String getPath(String type, String option, String theme, boolean selected){
        String path="";
        switch(type){
            case "theme":{
                if(selected)
                    path = IMG_PATH+option+DIR_PATH+DEFAULT_IMG+IMG_FORMAT;
                else{
                    if(option.equals("fruits"))
                        path = IMG_PATH+option+DIR_PATH+DEFAULT_IMG+UNSELECTED_IMG+IMG_FORMAT;
                    else
                        path = IMG_PATH+option+DIR_PATH+UNSELECTED_IMG+IMG_FORMAT;
                }
                break;
            }
            case "player":{
                if(selected)
                    path = IMG_PATH+theme+DIR_PATH+option+IMG_FORMAT;
                else
                    if(theme.equals("fruits"))
                        path = IMG_PATH+theme+DIR_PATH+option+UNSELECTED_IMG+IMG_FORMAT;
                    else
                        path = IMG_PATH+theme+DIR_PATH+UNSELECTED_IMG+IMG_FORMAT;
                
                break;
            }
            default:{
                if(selected)
                    path = IMG_PATH+theme+DIR_PATH+option+IMG_FORMAT;
                else
                    path = IMG_PATH+theme+DIR_PATH+option+UNSELECTED_IMG+IMG_FORMAT;
                break;
            }
        }
        return path;
    }
   
    /**
     * Adds a radio button to the selector window for each of the options retrieved from the hash table listOfOptions
     */
    private void addOptions(JPanel options, String type, String theme){
        
        options.setPreferredSize(new Dimension(200,120));
        options.setAlignmentX(Component.CENTER_ALIGNMENT);
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        ButtonGroup b = new ButtonGroup();

        for(String option: listOfOptions.get(type)){
            JRadioButton rButton =new JRadioButton();
            rButton.setFont(new Font(LABEL_FONT, 1, LABEL_FONT_SIZE));
            rButton.setName(option);
            rButton.setText(rButton.getName());
            
            System.out.println(getPath(type, option.toLowerCase(), theme, true));
            System.out.println(getPath(type, option.toLowerCase(), theme, false));

            try {
                selected = ImageIO.read(new File(getPath(type, option.toLowerCase(), theme, true)));
                unselected = ImageIO.read(new File(getPath(type, option.toLowerCase(), theme, false)));
                } 
            catch (IOException e) {
                }
            rButton.setIcon(new ImageIcon(unselected));
            rButton.setSelectedIcon(new ImageIcon(selected));
            rButton.setIconTextGap(15);
            rButton.setRolloverEnabled(false);
            Box.createVerticalGlue();
            options.add(rButton);
            if (!type.equals("player")){
                b.add(rButton);
            }
            buttons.add(rButton);
            options.add(Box.createVerticalGlue());
        }
        if(b.getButtonCount()>0)
            buttons.get(0).setSelected(true);
    }
    
    /**
     * Occurs when the confirmation button of the selector window is clicked. 
     */
    public void mouseClicked(MouseEvent evt) {
        for(JRadioButton button: buttons){
            if (button.isSelected())
                selectedOptions.add(button.getName().toLowerCase());
        }
        this.setVisible(false);
    }

    /**
     * Returns a list of objects of type Color, representing the players selected by the user
     */
    public ArrayList<Color> selectedPlayers() {
        ArrayList <Color> pColors = new ArrayList<>();
        for(String option: selectedOptions)
            for(Color color: Color.values())
                if(option.equals(color.name()))
                    pColors.add(color);
        return pColors;
    }

    public String selectedOption() {
        return selectedOptions.get(0);
    }


    public boolean selectedBoard() {
        return selectedOptions.get(0).equals("special");
    }

    public Theme selectedTheme() {
        Theme theme = DEFAULT_THEME;
        for(Theme t: Theme.values())
            if(selectedOptions.get(0).equals(t.name()))
                return t;
        return theme;
    } 

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}