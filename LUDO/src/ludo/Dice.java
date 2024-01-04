package ludo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import static javax.swing.JOptionPane.showInputDialog;
import static ludo.ImgPath.DICE_PATH;
import static ludo.ImgPath.FILE_EXTENSION;
import static ludo.LudoGUI.TILE_SIZE;

/**
 * dadu dengan 6 values

 */
public class Dice{

    public static final int DICE_SIZE=(int)(1.5*TILE_SIZE);
    private final Random random = new Random();
    private Scanner scanner;
    private final int ACC=0;
    private int result = 3;
    private int pIndex; //player holding the dice
    private boolean isSix = false,
                    animated = false,
                    debug = false;
    private int[] coordinates = new int[2];
    private int tickCounter;
    private int pos, vel;
    private int diceRollCount;
    
    private BufferedImage dice[] = new BufferedImage[6];
    private BufferedImage diceAnimation[] = new BufferedImage[25];
    private BufferedImage diceImg;
  
    /**
     * Initializes an instance of Dice.
     */
        public Dice(){  
        try {
            for (int i=0; i<dice.length; i++) 
                    dice[i]=ImageIO.read(new File(DICE_PATH+"result"+(i+1)+FILE_EXTENSION));
            for (int i=0; i<diceAnimation.length; i++) 
                    diceAnimation[i]=ImageIO.read(new File(DICE_PATH+"animateddice"+(i+1)+FILE_EXTENSION));}
        catch (IOException ex) {
                System.out.println("Image not found.");}
        diceImg = dice[result-1];
        this.coordinates[0]=(TILE_SIZE*15-DICE_SIZE)/2;
        this.coordinates[1]=(TILE_SIZE*15-DICE_SIZE)/2;
    }
    
    /**
     * Gets the integer number in the range [1-6] obtained after casting the dice.
     */
    public int getResult() {
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        this.isSix = this.result == 6;
    }
    

    public int getDiceRollCount() {
        return this.diceRollCount;
    }


    public void reRoll() {
        this.result = this.roll();
        this.isSix = this.result == 6;
    }


    public int getHolder() {
        return this.pIndex;
    }


    public void setHolder(int pIndex) {
        this.pIndex = pIndex;
    }


    public boolean getIsSix() {
        return this.isSix;
    }
    

    public void setIsSix(boolean isSix) {
        this.isSix = isSix;
    }
    

    public boolean getDebug() {
        return this.debug;
    }
    

    public void setDebug(boolean debug) {
        this.debug = debug;
    }


    public int getCoordinates(int i) {
        return this.coordinates[i];
    }
    

    public BufferedImage getDiceImg() {
        return this.diceImg;
    }
    

    public void rollDice(int pIndex) {
        this.pIndex = pIndex;
        this.result = this.roll();
        this.isSix = this.result == 6;
        resetCoordinates();
        this.tickCounter=0;
        this.vel=1;
        this.diceRollCount++;
    }


    public int roll() {
        if(!debug)
            this.result = random.nextInt(6) + 1;
        else{
            scanner = new Scanner(showInputDialog("Enter dice value (1-6):"));
            try{int res = scanner.nextInt()%7;
                this.result = res!=0? res:6;
            }
            catch(NoSuchElementException ne){this.result=6;}     
            }
        return this.result;
    }
    
    /**
     * Sets the dice coordinates to the corner of the player holding the dice.
     */
    public void resetCoordinates(){
        pos=0;
        if(this.pIndex<2){
            this.coordinates[0]=TILE_SIZE*15-DICE_SIZE;}
        else{
            this.coordinates[0]=0;}
        if(this.pIndex%3==0){
            this.coordinates[1]=0;}
        else{
            this.coordinates[1]=TILE_SIZE*15-DICE_SIZE;}   
    }
    

    public void setCoordinates(int pos){
        if(this.pIndex<2){
            this.coordinates[0]=TILE_SIZE*15-DICE_SIZE-pos;}
        else{
            this.coordinates[0]=pos;}
        if(this.pIndex%3==0){
            this.coordinates[1]=pos;}
        else{
            this.coordinates[1]=TILE_SIZE*15-DICE_SIZE-pos;}
    }

    /**
     * Select a new image to represent the dice with every tick of the timer that controls the dice animation from the GUI.
     */
    public void animateDice() {
        pos += vel*tickCounter + ACC*tickCounter*tickCounter/2;
        if(pos<(TILE_SIZE*15-DICE_SIZE)/2){
            if(this.pIndex%3==0)
                diceImg = diceAnimation[tickCounter%diceAnimation.length];
            else
                diceImg = diceAnimation[diceAnimation.length-1-(tickCounter%diceAnimation.length)];
            tickCounter++;
            vel += ACC;}
        else{
            diceImg = dice[result-1];
            pos=(TILE_SIZE*15-DICE_SIZE)/2;}
        setCoordinates(pos);
        //System.out.println("dice pos "+pos);
    }
}
    
