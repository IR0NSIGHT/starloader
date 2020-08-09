import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class Downscale {
    public static final float SCALE = 1.5F;
    public static void main(String[] args) throws IOException {
        Collection<File> files = FileUtils.listFiles(new File("/home/jake/CSE2/game_english/data/"), new String[]{"png"}, true);
        for (File file : files) {
            BufferedImage img = ImageIO.read(file);
            BufferedImage newImage = new BufferedImage(img.getWidth(),img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            int halfWidth = (int) (img.getWidth()/SCALE);
            int halfHeight = (int) (img.getHeight()/SCALE);
            Graphics2D g = (Graphics2D) newImage.getGraphics();
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.setComposite(AlphaComposite.Src);
            for (int x = 0; x < halfWidth; x++) {
                for (int y = 0; y < halfHeight; y++) {
                    Color cTL = new Color(img.getRGB((int)(x*SCALE), (int)(y*SCALE)));
                    Color cTR = new Color(img.getRGB((int)(x*SCALE + 1), (int)(y*SCALE)));
                    Color cBL = new Color(img.getRGB((int)(x*SCALE), (int)(y*SCALE + 1)));
                    Color cBR = new Color(img.getRGB((int)(x*SCALE + 1), (int)(y*SCALE + 1)));
                    Color color = getScaledColor(cTL, cTR, cBR, cBL);
                    if(!isZero(color)) {
                        g.setColor(color);
                        g.fillRect((int)(x * SCALE), (int)(y * SCALE), (int)SCALE, (int)SCALE);
                    }
                }
            }
            ImageIO.write(newImage, "png", file);
        }
//        ImageIO.read(new File("/home/")
    }
    public static boolean isZero(Color c){
        return c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 0;
    }
    public static Color getScaledColor(Color... arr){
        //Primary method: if 2 pixels are the same color use that
        for (Color color : arr) {
            for (Color color1 : arr) {
                if(color != color1) {
                    if (color.equals(color1)) return color;
                }
            }
        }
        //Secondary: get average
        int rx = 0, ry = 0, rz = 0;
        for (Color color : arr) {
            rx += color.getRed();
            ry += color.getGreen();
            rz += color.getBlue();
        }
        int len = arr.length;
        return new Color(rx/ len, ry / len, rz/len);
    }
}
