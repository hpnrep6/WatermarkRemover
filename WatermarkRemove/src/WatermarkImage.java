import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WatermarkImage {

    public WatermarkImage(File watermark) {
    }

    public static BufferedImage getWatermark(BufferedImage image) {
        // BufferedImage object for transparent watermark on white background
        BufferedImage watermarkOnlyImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int r, g, b, a, max; // red, green, blue, alpha/opacity/transparency
                Color curColour = new Color(image.getRGB(x, y));

                // Alpha value = 0 if all equals 0
                if(Math.max(curColour.getRed(),Math.max(curColour.getBlue(),curColour.getGreen())) == 255) {
                    a = 0; r = 0; g = 0; b = 0;
                }
                else {
                    // Alpha values equals max of values after they are subtracted from 255
                    a = Math.max(255 - curColour.getRed(),Math.max(255 - curColour.getBlue(),255 - curColour.getGreen()));
                    // Formula is C = Cs * a + Cd ( 1 - a )
                    // C = Final Colour | Cs = Colour with alpha added on top | Cd = Colour underneath / original colour | a = Alpha / opacity / transparency value from 0.0 to 1.0
                    r = (int)(((255 - (255.0*(255 - curColour.getRed())) / a)));
                    g = (int)(((255 - (255.0*(255 - curColour.getGreen())) / a)));
                    b = (int)(((255 - (255.0*(255 - curColour.getBlue())) / a)));
                }
                // creates a colour object using the RGB values and adds the integer/binary ARGB value into the BufferedImage object
                watermarkOnlyImage.setRGB(x,y,new Color(r,g,b,a).getRGB());
            }
        }
        return watermarkOnlyImage;
    }

    // Watermark extraction for image with black background
    public static BufferedImage getWatermarkBlack(BufferedImage image) {
        int backgroundColour = 0;
        // BufferedImage object for transparent watermark on white background
        BufferedImage watermarkOnlyImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int r, g, b, a, max; // red, green, blue, alpha/opacity/transparency
                Color curColour = new Color(image.getRGB(x, y));

                // Alpha value = 0 if all equals 0
                if(Math.max(curColour.getRed(),Math.max(curColour.getBlue(),curColour.getGreen())) == backgroundColour) {
                    a = 0; r = 0; g = 0; b = 0;
                }
                else {
                    // Alpha values equals max of values after they are subtracted from 255
                    a = Math.max(backgroundColour + curColour.getRed(),Math.max(backgroundColour + curColour.getBlue(),backgroundColour + curColour.getGreen()));
                    // Formula is C = Cs * a + Cd ( 1 - a )
                    // C = Final Colour | Cs = Colour with alpha added on top | Cd = Colour underneath / original colour | a = Alpha / opacity / transparency value from 0.0 to 1.0
                    r = (int)(((backgroundColour + (255.0*(backgroundColour + curColour.getRed())) / a)));
                    g = (int)(((backgroundColour + (255.0*(backgroundColour + curColour.getGreen())) / a)));
                    b = (int)(((backgroundColour + (255.0*(backgroundColour + curColour.getBlue())) / a)));
                }
                // creates a colour object using the RGB values and adds the integer/binary ARGB value into the BufferedImage object
                watermarkOnlyImage.setRGB(x,y,new Color(r,g,b,a).getRGB());
            }
        }
        return watermarkOnlyImage;
    }

    // Removes watermark with one of the "anchor" points being one of the corners of the watermarked image
    public static BufferedImage removeWatermarkCorner(File watermarked, BufferedImage watermark, int corner) {
        // BufferedImage object for watermarked image that will have watermark removed
        BufferedImage image;
        // x and y coordinates for where to start on watermarked image
        int newX = 0, newY = 0;
        // returns if image can't be read
        try {
            image = ImageIO.read(watermarked);
        } catch(IOException e) { return null; }
        // corner variable is the quadrant the corner would be in on a cartesian plane
        if(corner == 3 || corner == 4) {
            newY = image.getHeight() - watermark.getHeight();
        }
        if(corner == 1 || corner == 4) {
            newX = image.getWidth() - watermark.getWidth();
        }
        // loop setting new RGB values for watermarked image
        for(int x = 0, x2 = newX; x < watermark.getWidth(); x++, x2++) {
            for (int y = 0, y2 = newY; y < watermark.getHeight(); y++, y2++) {
                int r, g, b, or, og, ob, wr, wg, wb, bright; // red, green, blue values for new, original and watermark
                double a; // alpha/transparency/opacity value
                Color curColour = new Color(image.getRGB(x2, y2));
                Color watermarkColour = new Color(watermark.getRGB(x,y), true);
                // original / watermarked image RGB values
                or = curColour.getRed();
                og = curColour.getGreen();
                ob = curColour.getBlue();
                // separated watermark image RGB values
                wr = watermarkColour.getRed();
                wg = watermarkColour.getGreen();
                wb = watermarkColour.getBlue() ;
                // alpha value of watermark
                a = (watermarkColour.getAlpha()) / 255.0;
                // final RGB values after watermark has been removed
                r = (int) ((or - wr * a) / (1 - a));
                g = (int) ((og - wg * a) / (1 - a));
                b = (int) ((ob - wb * a) / (1 - a));
                // limits RGB values to be within 0 to 255 to ensure colour is set every time because the RGB calculations above will
                // sometimes return negative integers or integers greater than 255
                if(r >= 255) { r = 255; }
                if(g >= 255) { g = 255; }
                if(b >= 255) { b = 255; }
                if(r < 0) { r = 0; }
                if(g < 0) { g = 0; }
                if(b < 0) { b = 0; }
                // set RGB colour
                image.setRGB(x2, y2, new Color(r, g, b).getRGB());

            }
        }
        return image;
    }

    public static BufferedImage removeWatermarkCoords(File watermarked, BufferedImage watermark, int xCoord, int yCoord) {
        BufferedImage image;
        try {
            image = ImageIO.read(watermarked);
        } catch(IOException e) { return null; }
        for(int x = 0, x2 = xCoord; x < watermark.getWidth(); x++, x2++) {
            for (int y = 0, y2 = yCoord; y < watermark.getHeight(); y++, y2++) {
                int r, g, b, or, og, ob, wr, wg, wb, bright; // red, green, blue values for new, original and watermark
                double a; // alpha/transparency/opacity value
                Color curColour = new Color(image.getRGB(x2, y2));
                Color watermarkColour = new Color(watermark.getRGB(x,y), true);
                // original / watermarked image RGB values
                or = curColour.getRed();
                og = curColour.getGreen();
                ob = curColour.getBlue();
                // separated watermark image RGB values
                wr = watermarkColour.getRed();
                wg = watermarkColour.getGreen();
                wb = watermarkColour.getBlue() ;
                // alpha value of watermark
                a = (watermarkColour.getAlpha()) / 255.0;
                // final RGB values after watermark has been removed
                r = (int) ((or - wr * a) / (1 - a));
                g = (int) ((og - wg * a) / (1 - a));
                b = (int) ((ob - wb * a) / (1 - a));
                // limits RGB values to be within 0 to 255 to ensure colour is set every time because the RGB calculations above will
                // return negative integers or integers greater than 255
                if(r >= 255) { r = 255; }
                if(g >= 255) { g = 255; }
                if(b >= 255) { b = 255; }
                if(r < 0) { r = 0; }
                if(g < 0) { g = 0; }
                if(b < 0) { b = 0; }
                // set RGB colour
                image.setRGB(x2, y2, new Color(r, g, b).getRGB());
            }
        }
        return image;
    }

    public static BufferedImage removeWatermarkCornerFull(File watermarked, BufferedImage watermarkW, BufferedImage watermarkB, int corner, float bias) throws InterruptedException {
        // BufferedImage object for watermarked image that will have watermark removed
        BufferedImage image;
        int calculatedBias = (int) (128.0f * bias);
        // x and y coordinates for where to start on watermarked image
        int newXW = 0, newYW = 0;
        // returns if image can't be read
        try {
            image = ImageIO.read(watermarked);
        } catch(IOException e) { return null; }

        CombinedWatermark combWatermark = new CombinedWatermark(watermarkW, watermarkB, corner, calculatedBias);

        // corner variable is the quadrant the corner would be in on a cartesian plane
        if(corner == 3 || corner == 4) {
            newYW = image.getHeight() - combWatermark.getDimensions()[1];
        }
        if(corner == 1 || corner == 4) {
            newXW = image.getWidth() - combWatermark.getDimensions()[0];
        }

        // loop setting new RGB values for watermarked image
        for(int x = 0, x2 = newXW; x < combWatermark.getDimensions()[0]; x++, x2++) {
            for (int y = 0, y2 = newYW; y < combWatermark.getDimensions()[1]; y++, y2++) {
                //System.out.println("Unwatermarking: " + x + ", " + y);
                //System.out.println("dims " + combWatermark.getDimensions()[0] +  ", " + combWatermark.getDimensions()[1]);
                int r, g, b, or, og, ob, wr, wg, wb, bright; // red, green, blue values for new, original and watermark
                double a; // alpha/transparency/opacity value

                Color curColour = new Color(image.getRGB(x2, y2));

                Color watermarkColour = combWatermark.getColour(x,y,curColour);
                //Color watermarkColour = new Color(watermarkW.getRGB(x,y), true);

                // original / watermarked image RGB values
                or = curColour.getRed();
                og = curColour.getGreen();
                ob = curColour.getBlue();
                // separated watermark image RGB values
                wr = watermarkColour.getRed();
                wg = watermarkColour.getGreen();
                wb = watermarkColour.getBlue() ;
                // alpha value of watermark
                a = (watermarkColour.getAlpha()) / 255.0;
                // final RGB values after watermark has been removed
                r = (int) ((or - wr * a) / (1 - a));
                g = (int) ((og - wg * a) / (1 - a));
                b = (int) ((ob - wb * a) / (1 - a));
                // limits RGB values to be within 0 to 255 to ensure colour is set every time because the RGB calculations above will
                // sometimes return negative integers or integers greater than 255
                if(r >= 255) { r = 255; }
                if(g >= 255) { g = 255; }
                if(b >= 255) { b = 255; }
                if(r < 0) { r = 0; }
                if(g < 0) { g = 0; }
                if(b < 0) { b = 0; }
                // set RGB colour
                image.setRGB(x2, y2, new Color(r, g, b).getRGB());

            }
        }
        return image;
    }

    public static BufferedImage addWatermarkCorner(File addWatermark, BufferedImage watermark, int corner) {
        BufferedImage image;
        int newX = 0, newY = 0;
        try {
            image = ImageIO.read(addWatermark);
        } catch(IOException e) { return null; }
        if(corner == 3 || corner == 4) {
            newY = image.getHeight() - watermark.getHeight();
        }
        if(corner == 1 || corner == 4) {
            newX = image.getWidth() - watermark.getWidth();
        }
        for(int x = 0, x2 = newX; x < watermark.getWidth(); x++, x2++) {
            for (int y = 0, y2 = newY; y < watermark.getHeight(); y++, y2++) {
                int r, g, b, or, og, ob, wr, wg, wb, bright; // red, green, blue values for new, original and watermark
                double a; // alpha/transparency/opacity value
                Color curColour = new Color(image.getRGB(x2, y2));
                Color watermarkColour = new Color(watermark.getRGB(x,y), true);
                // original / watermarked image RGB values
                or = curColour.getRed();
                og = curColour.getGreen();
                ob = curColour.getBlue();
                // separated watermark image RGB values
                wr = watermarkColour.getRed();
                wg = watermarkColour.getGreen();
                wb = watermarkColour.getBlue() ;
                // alpha value of watermark
                a = (watermarkColour.getAlpha()) / 255.0;
                /*
                Adding the watermark uses the formula  C = Cs * a + Cd ( 1 - a ) where
                C = Final Colour
                Cs = Colour with alpha added on top of original colour
                Cd = Colour underneath / original colour
                a = Alpha / opacity / transparency value from 0.0 to 1.0
                */
                r = (int) ((wr * a) + or * (1 - a));
                g = (int) ((wg * a) + og * (1 - a));
                b = (int) ((wb * a) + ob * (1 - a));
                // limits RGB values to be within 0 to 255 to ensure colour is set every time because the RGB calculations above will
                // sometimes return negative integers or integers greater than 255
                if(r >= 255) { r = 255; }
                if(g >= 255) { g = 255; }
                if(b >= 255) { b = 255; }
                if(r < 0) { r = 0; }
                if(g < 0) { g = 0; }
                if(b < 0) { b = 0; }
                // set RGB colour
                image.setRGB(x2, y2, new Color(r, g, b).getRGB());
            }
        }
        return image;
    }

    public static BufferedImage addWatermarkCoords(File addWatermark, BufferedImage watermark, int xCoord, int yCoord) {
        BufferedImage image;
        try {
            image = ImageIO.read(addWatermark);
        } catch(IOException e) { return null; }
        for(int x = 0, x2 = xCoord; x < watermark.getWidth(); x++, x2++) {
            for (int y = 0, y2 = yCoord; y < watermark.getHeight(); y++, y2++) {
                int r, g, b, or, og, ob, wr, wg, wb, bright; // red, green, blue values for new, original and watermark
                double a; // alpha/transparency/opacity value
                Color curColour = new Color(image.getRGB(x2, y2));
                Color watermarkColour = new Color(watermark.getRGB(x,y), true);
                // original / watermarked image RGB values
                or = curColour.getRed();
                og = curColour.getGreen();
                ob = curColour.getBlue();
                // separated watermark image RGB values
                wr = watermarkColour.getRed();
                wg = watermarkColour.getGreen();
                wb = watermarkColour.getBlue() ;
                // alpha value of watermark
                a = (watermarkColour.getAlpha()) / 255.0;

                r = (int) ((wr * a) + or * (1 - a));
                g = (int) ((wg * a) + og * (1 - a));
                b = (int) ((wb * a) + ob * (1 - a));
                // limits RGB values to be within 0 to 255 to ensure colour is set every time because the RGB calculations above will
                // return negative integers or integers greater than 255
                if(r >= 255) { r = 255; }
                if(g >= 255) { g = 255; }
                if(b >= 255) { b = 255; }
                if(r < 0) { r = 0; }
                if(g < 0) { g = 0; }
                if(b < 0) { b = 0; }
                // set RGB colour
                image.setRGB(x2, y2, new Color(r, g, b).getRGB());
            }
        }
        return image;
    }
}
