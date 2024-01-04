package ludo;


import java.util.List;
import ludo.ImgPath.Color;
import ludo.ImgPath.Theme;

/**
* implementation
*/

public class Main {
    
    private static GameState game;
    private static Selector selectWindow;
    public static void run(){
        new ImgPath();

    	selectWindow= new Selector("theme");
    	Theme theme = selectWindow.selectedTheme();
        
    	selectWindow= new Selector("player", theme.name());
    	List<Color> plColors = selectWindow.selectedPlayers();
        
    	selectWindow= new Selector("board", theme.name());
    	boolean special = selectWindow.selectedBoard();

    	game = new GameState(theme, plColors, special);
        
        LudoGUI.drawGUI(game);
                
    }
    
}
