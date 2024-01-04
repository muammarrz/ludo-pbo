package ludo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import static javax.swing.JOptionPane.showMessageDialog;
import static ludo.Dice.DICE_SIZE;
import static ludo.ImgPath.*;
import ludo.ImgPath.Color;

/**
 * UI for the ludo game.
 */

public class LudoGUI extends JPanel implements ActionListener, MouseListener, KeyListener {
    public static final int IGNORE = 10;
    public static final int TILE_SIZE = 40;
    public static final int DICE_DELAY = 40;
    public static final int AUTOPLAYER_DELAY = 1000;
    
    private static final List<String> mGame = Arrays.asList("Restart", "Pause", "Exit");
    private static final List<String> mSettings = Arrays.asList("Players", "Theme", "Board", "Dice");
    private static final List<String> mPlayers = Arrays.asList("Yellow", "Red", "Green", "Blue");
    private static final List<String> mTheme = Arrays.asList("Plain", "Solid","Fruits");
    private static final List<String> mBoard = Arrays.asList("Regular", "Special");
    private static final List<String> mPSettings = Arrays.asList("Auto", "Manual", "Off");
    
    BufferedImage highlighter;


    Timer animation = new Timer(DICE_DELAY, this);
    Timer autoplayer = new Timer(AUTOPLAYER_DELAY, this);

    JMenuBar menuBar;
    GameState thisGame;
    static JDialog frame;
    
    int tileSize = TILE_SIZE;
    int frameSize = tileSize*15;
    int diceSize = (int)(tileSize*1.5);

    /**
     * Initializes the event listeners and starts the timer for the computer-operated players.
     */
    public LudoGUI(GameState game) {
        thisGame = game;
        autoplayer.start();
        addMenu();
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(frameSize, frameSize));
        //System.out.println(thisGame.getTheme());
        try {
            highlighter = ImageIO.read(new File(GEN_PATH+DIR+"highlight.png"));} 
        catch (IOException e) {}
    }
    

    public JMenuBar getMenu(){
        return this.menuBar;
    }

    private void render(Graphics2D g2) {
        
        g2.drawImage(thisGame.getBoard().getImg(thisGame.getTheme()), 0, 0, frameSize, frameSize, null);
        if (thisGame.getBoard().getSpecial()) {
            g2.drawImage(thisGame.getBoard().getImgSp(thisGame.getTheme()), 0, 0, frameSize, frameSize, null);
        }
        Player[] players = thisGame.getPlayers();
        for (Player player : players) {
            if (player.getActive()) {
                //int[] coordXY;
                for (Token token : player.getTokens()) {
                    g2.drawImage(player.getImage(thisGame.getTheme()), token.getCoordinateX(), token.getCoordinateY(), tileSize, tileSize, null);
                    if (!thisGame.getDiceRoller()) {
                        if (!animation.isRunning()) {
                            //if (thisGame.getPlayers()[thisGame.getDice().getHolder()].getColor().equals(player.getColor())) {
                            if (thisGame.getCurrentPlayer() == player.getPIndex()) {    
                                if (thisGame.getDice().getIsSix() & !token.getFinalTrack()) {
                                    g2.drawImage(highlighter, token.getCoordinateX(), token.getCoordinateY(), tileSize, tileSize, null);
                                } else {
                                    if (player.getTokensOut().contains(token.getIndex())) {
                                        g2.drawImage(highlighter, token.getCoordinateX(), token.getCoordinateY(), tileSize, tileSize, null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            g2.drawImage(thisGame.getDice().getDiceImg(), thisGame.getDice().getCoordinates(0), thisGame.getDice().getCoordinates(1), DICE_SIZE, DICE_SIZE, null);
    }

    /**
     * Draws the images of each ludo game element in the canvas.

     */
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        render(g2);
    }
    
     /**
     * Creates the menu bar and each of the sub-menus and menu items on it.
     */
    private void addMenu(){
        JMenuBar menu = new JMenuBar();
        menu.setPreferredSize(new Dimension(15*TILE_SIZE,25));
        JMenu gameMenu = createSubMenu("Game",mGame,true);
        JMenu settingsMenu = new JMenu("Settings");
        JMenu playerMenu = new JMenu("Players");
        JMenu themeMenu = createSubMenu("Theme",mTheme,true);
        JMenu boardMenu = createSubMenu("Board",mBoard,true);
        for(String player: mPlayers){
            playerMenu.add(createSubMenu(player,mPSettings,false));} 
        menu.add(gameMenu);
        menu.add(settingsMenu);
        settingsMenu.add(playerMenu);
        settingsMenu.add(themeMenu);
        settingsMenu.add(boardMenu);
        this.menuBar = menu;
    }
    
    //itemName: true for name same as label, false for name same as text

    private JMenu createSubMenu(String label, List<String> menuList, boolean itemName){
        JMenu menu = new JMenu(label);
        for(String option: menuList){
            JMenuItem item = new JMenuItem(option);
            item.setName(itemName? option: label);
            item.addActionListener(this);
            menu.add(item);
        }
        return menu;
    }

    /**
     * Event handler for the mouse events.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!thisGame.getPlaying()) {
            showMessageDialog(frame, thisGame.getGameResults()+"\n", "Game Over", JOptionPane.PLAIN_MESSAGE); 
            closeGUI();
        } 
        else {
        if (!thisGame.getPlayer(thisGame.getCurrentPlayer()).getAuto()){
            if (thisGame.getDiceRoller()) {
                    thisGame.rollAndCheckActiveTokens();
                    animation.start();} 
            else {
                int[] clickXY = new int[2];
                clickXY[0] = e.getX();
                clickXY[1] = e.getY();
                int selectedToken = thisGame.getPlayer(thisGame.getCurrentPlayer()).getTokenbyCoord(clickXY);
                System.out.println("SelectedToken: "+selectedToken);
                if (thisGame.getDice().getIsSix() && selectedToken != IGNORE) {
                    thisGame.selectAndMove(selectedToken);} 
                else {
                    if (thisGame.getPlayer(thisGame.getCurrentPlayer()).getTokensOut().contains(selectedToken)) {
                        thisGame.selectAndMove(selectedToken);}}
                autoplayer.start();
            }
        }
        repaint();
        }
    }
    
    /**
     * Event handler for the keyboard events.
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.isControlDown()){
            switch(ke.getKeyCode()){
                case KeyEvent.VK_D:{
                    boolean debugMode=thisGame.getDice().getDebug();
                    System.out.printf("Debug switched %s\n",debugMode? "off":"on");
                    thisGame.getDice().setDebug(!debugMode);
                    break;}
                case KeyEvent.VK_A:{
                    for(Player player: thisGame.getPlayers())
                        player.setAuto(true);
                    autoplayer.start();
                    break;}
                case KeyEvent.VK_M:{
                    for(Player player: thisGame.getPlayers())
                        player.setAuto(false);
                    break;}
                case KeyEvent.VK_T:{
                    Selector s = new Selector("theme");
                    thisGame.setTheme(s.selectedTheme());
                    break;}
                case KeyEvent.VK_P:{
                    Selector s = new Selector("player", thisGame.getTheme().name(),thisGame.getPlayer(thisGame.getCurrentPlayer()).getColor());
                        for(Color color: Color.values())
                            if(s.selectedPlayers().contains(color))
                                thisGame.addPlayer(color.name());
                            else
                                if(thisGame.getPlayer(color.name()).getActive())
                                    thisGame.removePlayer(color.name());
                    break;}
                case KeyEvent.VK_B:{
                    Selector s = new Selector("board", thisGame.getTheme().name());
                    thisGame.getBoard().setSpecial(s.selectedBoard());
                    break;}                
            }
            repaint();
        }
    }

    /**
     * Event handler for the timers and menu events.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object event = ae.getSource();
        switch(event.getClass().getSimpleName()){
            case ("Timer"):{
                timerEvent((Timer)event);
                break;}
            case ("JMenuItem"):{
                menuEvent((JMenuItem)event);
                break;}}
        repaint();
    }
    
    /**
     * Identifies the timer that triggered the event and:
     */
    public void timerEvent(Timer timer) {
        if (timer == animation) {
            playAnimation();} 
        else {
            if (timer == autoplayer) {
                if(!thisGame.getDebug())
                    blockCurrentPlayerMenu();
                autoPlay();}}
    }
    
    /**
     * Blocks the current player from the menu bar so it can not be modified while playing a turn, in order to avoid null pointer exceptions during execution.
     */
    private void blockCurrentPlayerMenu(){ 
        JMenu menu = (JMenu)menuBar.getMenu(1).getPopupMenu().getComponent(0);
        for (int i=0; i<4; i++){
            if(thisGame.getPlayer(thisGame.getCurrentPlayer()).getColor().equalsIgnoreCase(menu.getItem(i).getText()))
                menu.getItem(i).setEnabled(false);
            else
                menu.getItem(i).setEnabled(true);}
    }
    
    /**
     * Updates the dice image with every tick of the timer and moves the dice position from the player's corner to the center of the board.
     */
    public void playAnimation(){
       thisGame.getDice().animateDice();
       if (thisGame.getDice().getCoordinates(0) == (TILE_SIZE * 15 - DICE_SIZE) / 2) {
           animation.stop();
           thisGame.checkMoveOrPass();
           autoplayer.start();} 
    }
    
    /**
     * Checks if it is the computer player's turn to roll the dice or to move a token, and acts accordingly.
     */
    public void autoPlay(){
        if (!thisGame.getPlaying()) {
            showMessageDialog(frame, thisGame.getGameResults()+"\n", "Game Over", JOptionPane.PLAIN_MESSAGE); 
            closeGUI();
        } 
        else {
            autoplayer.stop();
            if (thisGame.getPlayer(thisGame.getCurrentPlayer()).getAuto()) {
                if (thisGame.getDiceRoller()) {
                    thisGame.rollAndCheckActiveTokens();
                    animation.start();} 
                else {
                thisGame.autoMove();
                autoplayer.start();}
            }
        }
    }
    
    /**
     * Identifies the menu item that triggered the event and acts accordingly.
     */
    public void menuEvent(JMenuItem item){
        if(mGame.contains(item.getName()))                    
                    switch(item.getName()){
                        case "Restart":{
                            restartGUI();
                            break;}
                        case "Pause":{
                            pause(item.getText().equals("Pause"));   
                            break;}
                        case "Exit":{
                            closeGUI();
                            break;}}
                else{
                    if(mTheme.contains(item.getText())){
                        thisGame.setTheme(item.getText());}
                    else{
                        if(mBoard.contains(item.getText())){
                            thisGame.getBoard().setSpecial(item.getText());
                            System.out.println("Item set to "+item.getText());}
                        else{
                            if(mPSettings.contains(item.getText())){
                                switch(item.getText()){
                                    case "Auto":{
                                        thisGame.addPlayer(item.getName());
                                        thisGame.getPlayer(item.getName()).setAuto(true);
                                        break;}
                                    case "Manual":{
                                        thisGame.addPlayer(item.getName());
                                        thisGame.getPlayer(item.getName()).setAuto(false);
                                        break;}
                                    case "Off":{
                                        thisGame.removePlayer(item.getName());
                                        break;}}}}}}
    }
    
    /**
     * Sends all the tokens of the active players to their home area.
     */
    public void restartGUI(){
        animation.stop();
        autoplayer.restart();
        thisGame.restart();
    }
    
    /**
     * Stops/restarts the timers of the GUI.
     */
    public void pause(boolean playing){
        if(playing){
            animation.stop();
            autoplayer.stop();
            menuBar.getMenu(0).getItem(1).setText("Resume");}
        else{
            animation.start();
            autoplayer.start();
            menuBar.getMenu(0).getItem(1).setText("Pause");}  
    }
            
    /**
     * Stops the timers and disposes of the GUI.
     */
    public void closeGUI(){
        animation.stop();
        autoplayer.stop();
        //setFocusable(false);
        try{frame.dispose();}
        catch(NullPointerException e){}
    }
    
    /**
     * Creates and initializes the container of the ludoGUI and shows it on screen.

     */
    public static void drawGUI(GameState game) {
        frame = new JDialog();
        frame.setTitle("LUDO");
        frame.setModal(true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        game.setPlaying(true);
        LudoGUI ludoPanel=new LudoGUI(game);  
        frame.setJMenuBar(ludoPanel.getMenu());
        frame.add(ludoPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null); //center frame on screen
        frame.setVisible(true);
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

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }  
}
