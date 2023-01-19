package syntax.pixelengine.engine;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;


public class Image {
    public static int height = 256;
    public static int width = 256;
    private int[][][] mapRaw = new int[height][width][3];
    public BufferedImage mapImage = null;

    public Image(File file) {
        try {
            mapImage = ImageIO.read(file); //Load the image data into mapImage

            //Loop through the image getting all RGb values
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(mapImage.getRGB(x, y)); //Convert raw color value into RGb color values
                    mapRaw[x][y][0] = color.getRed(); //Store the raw color value
                    mapRaw[x][y][1] = color.getGreen(); //Store the raw color value
                    mapRaw[x][y][2] = color.getBlue(); //Store the raw color value
                }
            }
        } catch (IOException e) {
            System.out.println("[ENGINE ERROR]:  " + e);
        }
    }

    public Image() {

    }

    public int[][][] getMap() { //Get the RGB values of a specific pixel
        return mapRaw;
    }

    public BufferedImage changeMap(BufferedImage mapImage, int[][][] rawMap) {

        //Loop through the image getting all RGb values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(rawMap[x][y][0], rawMap[x][y][1], rawMap[x][y][2]); //Convert raw color value into RGb color values
                mapImage.setRGB(x, y, color.getRGB()); //Store the raw color value
            }
        }
        return mapImage;

    }

    public void storeImage(BufferedImage mapImage, String path) {
        try {
            File outputfile = new File(path);
            ImageIO.write(mapImage, "png", outputfile);
        } catch (IOException e) {
            new EngineError(path + " could not be saved");
        }
    }
}
