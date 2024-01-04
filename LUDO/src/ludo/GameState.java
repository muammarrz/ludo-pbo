package ludo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ludo.Autoplay.AutoplayMode;
import ludo.ImgPath.*;
import static ludo.Player.GOAL;
import static ludo.Player.OUT_OF_BOARD;

/**
 * konstruksi permainan ludo
 */

public class GameState {
    
    //default game settings:
    public static final Theme DEFAULT_THEME = Theme.plain;
    public static final boolean DEFAULT_BOARD = true;
    public static final boolean DEFAULT_AUTOPLAYER= false;
    public static final AutoplayMode DEFAULT_AUTOMODE= AutoplayMode.customAI;
    private static List<ImgPath.Color> DEFAULT_PLAYERS = Arrays.asList(Color.blue, Color.red, Color.yellow, Color.green);
    
    private Board board;
    private Dice dice;
    private Player[] players;
    private Autoplay computerPlayer;
    private Theme theme;
    private int currentPlayer, turn; //index of current player, count of turns since game 
    private boolean diceRoller, playing, debug; // check if click is dice roller or token selector
    private ArrayList<Integer> xPlayers, xTokens, winners; //indexes of active players
    private String gameResults;
    /** 
    * Initializes an instance of GameState using default game settings. 
    */
    public GameState(){
        this.gameResults = "";
        initVars();
        this.theme = DEFAULT_THEME;
        this.board = new Board(DEFAULT_BOARD);
        createSetOfPlayers(DEFAULT_PLAYERS, DEFAULT_AUTOPLAYER, DEFAULT_AUTOMODE);
    } 
    

    public GameState(Theme theme, List<ImgPath.Color> plColors, boolean specialBoard){
        this.gameResults = "";
        initVars();
        this.theme = theme;
        this.board = new Board(specialBoard);
        createSetOfPlayers(plColors, DEFAULT_AUTOPLAYER, DEFAULT_AUTOMODE);
    } 
    

    public GameState(Theme theme, List<ImgPath.Color> plColors, List<Boolean> auto, List<AutoplayMode> autoMode, boolean specialBoard){
        initVars();
        this.theme = theme;
        this.board = new Board(specialBoard);
        createSetOfPlayers(plColors, auto, autoMode);
    } 
    
    /**
    * Initializes common variables to all GameState constructors.
    */
    private void initVars(){
        this.dice = new Dice();
        this.playing = true;
        this.currentPlayer = 0;
        this.turn = 0;
        this.xPlayers=new ArrayList<>(); 
        this.players = new Player[4];
        this.diceRoller=true;
        this.xTokens=new ArrayList<>();
        this.winners=new ArrayList<>();
        this.computerPlayer= new Autoplay();
        this.gameResults = "";
        this.debug = false;
    }


    public Board getBoard() {
        return this.board;
    }


    public void setBoard(Board board) {
        this.board = board;
    }


    public void setSpecial(String special) {
        if(special.equals("special"))
            this.board.setSpecial(true);
    }


    public Dice getDice() {
        return this.dice;
    }


    public void setDice(Dice dice) {
        this.dice = dice;
    }


    public Player[] getPlayers() {
        return this.players;
    }


    public void setPlayers(Player[] players) {
        this.players = players;
    }
    

    public Player getPlayer(int i) {
        return this.players[i];
    }
    

    public Player getPlayer(String color) {
        for (Player player: this.players)
            if(player.getColor().equalsIgnoreCase(color))
                return player;
        return null;
    }

    public ArrayList<Integer> getXPlayers() {
        return this.xPlayers;
    }


    public void addXPlayers(int playerIndex) {
        this.xPlayers.add(playerIndex);
        Collections.sort(this.xPlayers);
    }
    

    public void removeXPlayers(int playerIndex) {
        this.xPlayers.remove(this.xPlayers.indexOf(playerIndex));
    }
    

    public ArrayList<Integer> getXTokens() {
        return this.xTokens;
    }


    public ArrayList<Integer> getWinners() {
        return this.winners;
    }


    public void addWinners(int playerIndex) {
        this.winners.add(playerIndex);
    }


    public Theme getTheme() {
        return this.theme;
    }


    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    

    public void setTheme(String theme) {
        for(Theme t: Theme.values())
            if(theme.equalsIgnoreCase(t.name()))
                this.theme = t;
    }


    public boolean getDiceRoller() {
        return this.diceRoller;
    }
    

    public boolean getPlaying() {
        return this.playing;
    }
    

    public void setPlaying(boolean playing) {
        this.playing=playing;
    }
    

    public boolean getDebug() {
        return this.debug;
    }
    

    public void setDebug(boolean debug) {
        this.debug=debug;
    }


    public int getCurrentPlayer(){
        return this.currentPlayer;
    }
    

    public void setCurrentPlayer(int index){
        this.currentPlayer=index%4;
    }
    

    public int getTurnCount(){
        return this.turn;
    }
    

    public String getGameResults(){
    	return this.gameResults;
    }

    /**
     * Initializes the attributes of each of the active players for this round.
     */
    private void createSetOfPlayers(List<ImgPath.Color> colors, List<Boolean> auto, List<AutoplayMode> autoMode) {
        int i = 0;
        for (ImgPath.Color c : ImgPath.Color.values()) {
            this.players[i] = new Player(c, auto.get(i), autoMode.get(i));
            this.players[i].setPIndex(i);
            //System.out.println("Player "+c.name()+" created.");
            for(int j=0; j<4; j++){
                this.players[i].setXY(j);
            }
            if (colors.contains(c)) {
                this.players[i].setActive(true);
                this.xPlayers.add(i);
            }
            i++;
        }
    }
   
    /**
     * Initializes the attributes of each of the active players for this round.
     */
    private void createSetOfPlayers(List<ImgPath.Color> colors, boolean auto, AutoplayMode autoMode) {
        int i = 0;
        for (ImgPath.Color c : ImgPath.Color.values()) {
            this.players[i] = new Player(c, auto, autoMode);
            this.players[i].setPIndex(i);
            //System.out.println("Player "+c.name()+" created.");
            for(int j=0; j<4; j++){
                this.players[i].setXY(j);
            }
            if (colors.contains(c)) {
                this.players[i].setActive(true);
                this.xPlayers.add(i);
            }
            i++;
        }
    }
    
    /**
     * Sends all the tokens of each active player to its starting position
     * and sets attributes turn, currentPlater and diceRoller to its original values.
     */
    public void restart(){
        for(Player p: players)
            if(p.getActive())
                p.reset();
        turn = 0;
        currentPlayer = 0;
        diceRoller=true;
    }
    
    /**
     * Initializes a player that was previously considered not-active in the game
     */
    public void addPlayer(String color){
        if(!this.getPlayer(color).getActive()){
            this.getPlayer(color).reset();
            this.getPlayer(color).setActive(true);
            addXPlayers(this.getPlayer(color).getPIndex());
            turn = this.xPlayers.indexOf(currentPlayer);}
    }
    
    /**
     * Removes an active player from the game given its color
     */
    public void removePlayer(String color){
        this.getPlayer(color).setActive(false);
        removeXPlayers(this.getPlayer(color).getPIndex());
        turn = this.xPlayers.indexOf(currentPlayer);
    }    

     /**
     * Compares the position of a given token with other players' tokens and if equal, sends the opponent's token to its home area
     */
    private void checkOtherTokens(int pIndex, int tIndex) {
        int tokenPosition = this.players[pIndex].getToken(tIndex).getPosition();
        for (int i = 0; i < this.xPlayers.size(); i++) {
            if (!this.players[pIndex].getColor().equals(players[xPlayers.get(i)].getColor())) {
                for (int j = 0; j < 4; j++) {
                    if (players[xPlayers.get(i)].getToken(j).getPosition() == tokenPosition && players[xPlayers.get(i)].getToken(j).getPosition() != OUT_OF_BOARD && !players[xPlayers.get(i)].getToken(j).getSafe()) {
                        players[xPlayers.get(i)].outOfBoard(j);
                    }
                }
            }
        }
    }

    /**
     * The current player rolls the dice and updates the list of active tokens
     */
    public void rollAndCheckActiveTokens() {
        this.currentPlayer = this.xPlayers.get(this.turn % this.xPlayers.size());
        this.dice.rollDice(this.currentPlayer);
        //System.out.printf("%s player rolls the dice: %d\n", this.players[currentPlayer].getColor(), this.dice.getResult());
        this.xTokens.clear();

        if (this.dice.getIsSix()) {
            this.players[currentPlayer].setTurn(true);// flag for throwing the dice again if a token is moved
            for (Token token : this.players[currentPlayer].getTokens()) {
                if (!(token.getFinalTrack() && token.getPosition() == GOAL)) {
                    this.xTokens.add(token.getIndex());
                }
            }
        } else {
            this.players[currentPlayer].setTurn(false);
            for (int index : this.players[currentPlayer].getTokensOut()) {
                this.xTokens.add(index);
            }
        }
    }
    
    /**
     * If there are no possible moves, the current player passes the dice to the next player 
     */
    public void checkMoveOrPass(){
        if (this.xTokens.size() > 0) {
            this.diceRoller = false;} 
        else { //if no tokens to move, pass and let player roll dice
            this.turn++;
            //System.out.println("next turn player " + this.players[currentPlayer].getColor());
        }
        this.currentPlayer = this.xPlayers.get(this.turn % this.xPlayers.size());
    }
    
    /**
     * Moves the selected token to a new position, checks for other tokens and special tiles (in case of special board), 
     */
    public void selectAndMove(int tokenIndex) {
        Token thisToken = this.players[currentPlayer].getTokens()[tokenIndex];
        //System.out.println((thisToken.getFinalTrack()&&!thisToken.getOut()));
        if(!(thisToken.getFinalTrack()&&!thisToken.getOut())){
        this.players[currentPlayer].moveSelectedToken(tokenIndex,this.dice.getResult());
        if (!thisToken.getFinalTrack()) {
            this.checkOtherTokens(this.players[currentPlayer].getPIndex(), tokenIndex);
            if (this.board.getSpecial()) {
                this.players[currentPlayer].checkSpecial(tokenIndex, this.board);
            }
        }
        if (this.players[currentPlayer].getGoal() == 4) {
            this.addWinners(this.players[currentPlayer].getPIndex());
            this.removeXPlayers(this.players[currentPlayer].getPIndex());
            if (this.getXPlayers().isEmpty()) {
                this.playing = false;
                this.gameResults = "\nResults:\n\n";
                //System.out.println("-----GAME OVER!-----"+gameResults);
                for (int i = 0; i < this.getWinners().size(); i++) {
                    //System.out.printf("%d place - %s player\n", i + 1, this.getPlayers()[this.getWinners().get(i)].getColor());
                    this.gameResults += (i + 1)+" place - "+this.getPlayers()[this.getWinners().get(i)].getColor()+" player\n";
                }
            }
        }
        //System.out.println("Player turn:" + this.players[currentPlayer].getTurn());
        if (!this.players[currentPlayer].getTurn()) {
            this.turn++;
            //System.out.println("next turn player " + this.players[currentPlayer].getColor());
        }
        this.diceRoller = true;
        if (playing)
            this.currentPlayer = this.xPlayers.get(this.turn % this.xPlayers.size());
        }
    }
     
    /**
     * Calls the method SelectAndMove with the token selected by the computer player as the argument
     */
    public void autoMove() {
        selectAndMove(computerPlayer.selectToken(this));
    }
}
