
package ludo;

import static ludo.LudoGUI.TILE_SIZE;
import static ludo.Player.GOAL;
import static ludo.Player.OUT_OF_BOARD;

/**
 * Represents one of the 4 tokens (pion) of a player.
 */
public class Token {
 
    private int index, position, coordinateX, coordinateY;
    private boolean out, finalTrack, safe;
   
    /**
     * Initializes an instance of Token given its position in the array of tokens from the Player object
     */
    public Token(int i) {
    this.index = i;
    this.position = OUT_OF_BOARD;
    this.out = false;
    this.finalTrack = false;
    this.coordinateX = TILE_SIZE*15;
    this.coordinateY = TILE_SIZE*15;
    }    
    

    public int getIndex() {
        return this.index;
    }


    public int getPosition() {
        return this.position;
    }
    

    public void setPosition(int pos) {
        this.position = pos;
    }
    

    public int getCoordinateX() {
        return this.coordinateX;
    }


    public int getCoordinateY() {
        return this.coordinateY;
    }
    

    public void setCoordinateX(int x) {
        this.coordinateX = x;
    }
    

    public void setCoordinateY(int y) {
        this.coordinateY = y;
    }
    

    public boolean getSafe() {
        return this.safe;
    }


    public void setSafe(boolean safe) {
        this.safe = safe;
    }
    
  
    public boolean getOut() {
        return this.out;
    }


    public void setOut(boolean out) {
        this.out = out;
    }
    

    public boolean getFinalTrack() {
        return this.finalTrack;
    }


    public void setFinalTrack(boolean ft) {
        this.finalTrack = ft;
    }
    

    public void sendHome(){
        this.position = OUT_OF_BOARD;
        this.out = false;
        this.finalTrack = false;
    }
    
    /**
     * Updates a token's position according to a given dice result.
     */ 
    public void moveToken(int diceResult) {
        if (!this.finalTrack){
        this.position = (this.position + diceResult) % 52;
        }
        else{
            int x = this.position + diceResult;
            this.position = x>GOAL? GOAL-(x-GOAL):x;
            //this.position = Math.abs((int) ((x % 10) / 5) * 5 - x % 5);
        }
    }
}