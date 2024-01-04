package ludo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import ludo.ImgPath.Theme;

/**
 * papan ludo.
 */
public class Board {

    public static final List<Integer> START_TILES = Arrays.asList(0, 13, 26, 39);
    public static final List<Integer> GLOBE_TILES = Arrays.asList(8, 21, 34, 47);
    public static final List<Integer> STAR_TILES = Arrays.asList(5, 11, 18, 24, 31, 37, 44, 50);

    private boolean special;
    private final Map<Theme, BufferedImage> img = new HashMap<>();
    private final Map<Theme, BufferedImage> imgsp = new HashMap<>();

   
    public Board(boolean special) {
        this.special = special;
        for (Theme t : Theme.values()) {
            ImgPath.setBoardPath(t);
            try {
                img.put(t, ImageIO.read(new File(ImgPath.getBoardPath(ImgPath.Board.board))));
                imgsp.put(t, ImageIO.read(new File(ImgPath.getBoardPath(ImgPath.Board.specialboard))));
            } catch (IOException ex) {
                System.out.println("Image not found.");
            }
        }
    }


    public BufferedImage getImg(Theme theme) {
        return img.get(theme);
    }


    public BufferedImage getImgSp(Theme theme) {
        return imgsp.get(theme);
    }


    public boolean getSpecial() {
        return this.special;
    }


    public void setSpecial(boolean special) {
        this.special = special;
    }


    public void setSpecial(String special) {
        this.special = special.equalsIgnoreCase("special");
    }
}
