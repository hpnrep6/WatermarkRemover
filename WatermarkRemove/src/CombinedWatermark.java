import java.awt.*;
import java.awt.image.BufferedImage;

public class CombinedWatermark {
    private BufferedImage wImage, bImage;
    private int height, width, bias;

    public CombinedWatermark(BufferedImage white, BufferedImage black, int corner, int calculatedBias) {
        int lowestX, lowestY;
        if(white.getWidth() >= black.getWidth()) {
            lowestX = black.getWidth();
        } else {
            lowestX = white.getWidth();
        }
        if(white.getHeight() >= black.getHeight()) {
            lowestY = black.getHeight();
        } else {
            lowestY = white.getHeight();
        }

        int wWidth = white.getWidth(), wHeight = white.getHeight();
        int bWidth = black.getWidth(), bHeight = black.getHeight();

        // corner values correspond to the location it "would be in" on a cartesian plane (top right is 1, top left is 2 etc.)
        switch (corner){
            case 1:
                wImage = imgCrop(white,wWidth - lowestX, 0, wWidth,lowestY);
                bImage = imgCrop(black,bWidth - lowestX, 0, bWidth,lowestY);
                break;
            case 2:
                wImage = imgCrop(white, 0, 0, lowestX, lowestY);
                bImage = imgCrop(black, 0, 0, lowestX, lowestY);
                break;
            case 3:
                wImage = imgCrop(white, 0,wHeight - lowestY, lowestX, wHeight);
                bImage = imgCrop(black, 0,bHeight - lowestY, lowestX, bHeight);
                break;
            case 4:
                wImage = imgCrop(white, wWidth - lowestX, wHeight - lowestY, wWidth, wHeight);
                bImage = imgCrop(black, bWidth - lowestX, bHeight - lowestY, bWidth, bHeight);
                break;
        }
        height = lowestY;
        width = lowestX;
        bias = calculatedBias;
    }

    public int[] getDimensions() {
        int[] dimensions = {width, height};
        return dimensions;
    }

    public Color getColour(int x, int y, Color bkgColour) {
         int maxCol = Math.max(bkgColour.getBlue(), Math.max(bkgColour.getRed(), bkgColour.getGreen()));
         if(maxCol < bias) {
             return new Color(bImage.getRGB(x, y), true);
         } else {
             return new Color(wImage.getRGB(x, y), true);
         }
    }

    private static BufferedImage imgCrop(BufferedImage original, int xPosOne, int yPosOne, int xPosTwo, int yPosTwo){
        if(xPosOne > xPosTwo) {
            int temp = xPosOne;
            xPosTwo = xPosOne;
            xPosOne = temp;
        }
        if(yPosOne > yPosTwo) {
            int temp = yPosOne;
            yPosTwo = yPosOne;
            yPosOne = temp;
        }
        BufferedImage cropImage = new BufferedImage((xPosTwo-xPosOne), (yPosTwo-yPosOne), BufferedImage.TYPE_INT_ARGB);
        for(int y = yPosOne, yt = 0; y < yPosTwo; y++, yt++) {
            for(int x = xPosOne, xt = 0; x < xPosTwo; x++, xt++) {
                int curColour = original.getRGB(x, y);
                cropImage.setRGB(xt, yt, curColour);
            }
        }
        return cropImage;
    }
}
