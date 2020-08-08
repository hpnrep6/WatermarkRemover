import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainGui {
    private JPanel mainPanel;
    private JTextField watermarkPath;
    private JButton watermarkSelect;
    private JTextField watermarkedImagePath;
    private JButton watermarkedImageSelect;
    private JTextField savePathPath;
    private JButton savePathSelect;
    private JButton watermarkedImageFileSelect;
    private JTextArea notificationText;
    private JTextArea settingsText;
    private JTextField watermarkSettings;
    private JButton getWatermark;
    private JButton removeWatermark;
    private JButton getAndRemoveWatermark;
    private JButton getAndRemoveWatermarkBlack;
    private JTextField WatermarkBlackPath;
    private JButton WatermarkBlackSelect;
    private JTextField BWBias;

    private File finalWatermarkedFileDir, finalWatermarkFile, finalWatermarkBlackFile, finalSaveDirectory;
    private int settings[] = new int[2];
    private float bias = 1;

    public MainGui() {
        JFrame main = new JFrame("Watermark Remover");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(1680,600);
        main.add(mainPanel);
        main.setVisible(true);

        settingsText.setText("Enter which corner the watermark is on, or enter the x and y coordinates of the watermark. \n" +
        "(Corners correspond to the quadrant it would be in on a cartesian plane, i.e. top right is 1, top left is 2, bottom left is 3, bottom right is 4) \n" +
        "(0,0 of the coordinates is at the top left corner of the images) \n \n" +
        "Press enter to select.");
        settingsText.setEditable(false);
        notificationText.setEditable(false);

        // setting text in buttons
        watermarkSelect.setText("Select Watermark File [W]");
        watermarkedImageFileSelect.setText("Select Watermarked Image");
        watermarkedImageSelect.setText("Select Watermarked Directory");
        savePathSelect.setText("Select Save Directory");
        // setting text in text fields
        watermarkPath.setText("Watermark file on White background");
        watermarkedImagePath.setText("Watermarked image file or directory");
        savePathPath.setText("Save path");
        // setting text for watermark removing and getting
        getWatermark.setText("Extract Watermark from Image with White Background [Saves extracted watermark]");
        removeWatermark.setText("Use Already Extracted Watermark to remove Watermark [Saves watermarked image with watermark removed]");
        getAndRemoveWatermark.setText("Extract Watermark on White background and use the Extracted Watermark to Remove Watermark [Saves watermarked image with watermark removed]");
        getAndRemoveWatermarkBlack.setText("Extract Watermark on Black and White background and use the Extracted Watermark to Remove Watermark [Saves watermarked image with watermark removed]");
        WatermarkBlackPath.setText("Watermark File on Black background");
        WatermarkBlackSelect.setText("Select Watermark File [B]");
        BWBias.setText("Bias between deciding which extracted watermark to use. 0 is only using white and 2 is only using black.");

        // add action listener
        watermarkPath.addActionListener(this::actionPerformed);
        watermarkSelect.addActionListener(this::actionPerformed);
        watermarkedImagePath.addActionListener(this::actionPerformed);
        watermarkedImageSelect.addActionListener(this::actionPerformed);
        watermarkedImageFileSelect.addActionListener(this::actionPerformed);
        savePathPath.addActionListener(this::actionPerformed);
        savePathSelect.addActionListener(this::actionPerformed);
        watermarkSettings.addActionListener(this::actionPerformed);
        getWatermark.addActionListener(this::actionPerformed);
        removeWatermark.addActionListener(this::actionPerformed);
        getAndRemoveWatermark.addActionListener(this::actionPerformed);
        getAndRemoveWatermarkBlack.addActionListener(this::actionPerformed);
        WatermarkBlackPath.addActionListener(this::actionPerformed);
        WatermarkBlackSelect.addActionListener(this::actionPerformed);
        BWBias.addActionListener(this::actionPerformed);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == watermarkPath) {
            String path = watermarkPath.getText();
            File file = new File(path);
            if(file.isDirectory()) {
                finalWatermarkFile = null;
                notificationText.setText("Watermark file cannot be a directory.");
            }
            else if(!file.isFile()) {
                finalWatermarkFile = null;
                notificationText.setText("Watermark file selected does not exist.");
            }
            else if(path.contains(".png") || path.contains(".jpg") ) {
                finalWatermarkFile = file;
                notificationText.setText("Watermark file successfully selected.");
            }
        }
        if(e.getSource() ==  watermarkSelect) {
            File file = chooseFile();
            finalWatermarkFile = file;
            watermarkPath.setText(file.getAbsolutePath());
            notificationText.setText("Watermark file successfully selected.");

        }

        if(e.getSource() == WatermarkBlackPath) {
            String path = WatermarkBlackPath.getText();
            File file = new File(path);
            if(file.isDirectory()) {
                finalWatermarkBlackFile = null;
                notificationText.setText("Watermark file cannot be a directory.");
            }
            else if(!file.isFile()) {
                finalWatermarkBlackFile = null;
                notificationText.setText("Watermark file selected does not exist.");
            }
            else if(path.contains(".png") || path.contains(".jpg") ) {
                finalWatermarkBlackFile = file;
                notificationText.setText("Watermark file successfully selected.");
            }
        }
        if(e.getSource() ==  WatermarkBlackSelect) {
            File file = chooseFile();
            finalWatermarkBlackFile = file;
            WatermarkBlackPath.setText(file.getAbsolutePath());
            notificationText.setText("Watermark file successfully selected.");
        }

        if(e.getSource() == watermarkedImagePath) {
            String path = watermarkedImagePath.getText();
            File file = new File(path);
            if(file.isDirectory()) {
                finalWatermarkedFileDir = file;
                notificationText.setText("Watermarked image directory selected.");
            }
            else if(!file.isFile()) {
                finalWatermarkedFileDir = null;
                notificationText.setText("Watermarked image file selected does not exist.");
            }
            else if(path.contains(".png") || path.contains(".jpg") ) {
                finalWatermarkedFileDir = file;
                notificationText.setText("Watermarked image file successfully selected.");
            } else {
                finalWatermarkedFileDir = null;
                notificationText.setText("Invalid file type.");
            }
        }
        if(e.getSource() == watermarkedImageSelect) {
            File file = chooseDirectory();
            finalWatermarkedFileDir = file;
            watermarkedImagePath.setText(file.getAbsolutePath());
            notificationText.setText("Watermarked image file successfully selected.");
        }
        if(e.getSource() == watermarkedImageFileSelect) {
            File file = chooseFile();
            finalWatermarkedFileDir = file;
            watermarkedImagePath.setText(file.getAbsolutePath());
            notificationText.setText("Watermarked image file successfully selected.");
        }

        if(e.getSource() == savePathPath) {
            String path = savePathPath.getText();
            File file = new File(path);
            if(file.isDirectory()) {
                finalSaveDirectory = file;
                notificationText.setText("Save directory selected.");
            } else {
                finalSaveDirectory = null;
                notificationText.setText("Invalid directory");
            }

        }
        if(e.getSource() == savePathSelect) {
            File file = chooseDirectory();
            if (file.isDirectory()) {
                finalSaveDirectory = file;
                savePathPath.setText(file.getAbsolutePath());
                notificationText.setText("Save directory selected.");
            } else {
                finalWatermarkedFileDir = null;
                savePathPath.setText(file.getAbsolutePath());
                notificationText.setText("Invalid directory");
            }

        }
        if(e.getSource() == watermarkSettings) {
            String string = watermarkSettings.getText();
            String[] splitString = string.split(",");
            if(splitString.length == 1) {
                try {
                    int integer = Integer.parseInt(splitString[0]);
                    if(integer == 1 || integer == 2 || integer == 3 || integer == 4) {
                        settings[0] = integer;
                        settings[1] = 987654321; // used to check if only one int was given or two because length can't be used to compare as it's always 2
                        notificationText.setText("Setting saved.");
                    } else {
                        notificationText.setText("Setting invalid.");
                    }
                } catch (Exception exception) {
                    notificationText.setText("Invalid setting.");
                }
            } else if (splitString.length == 2) {
                try {
                    settings[0] = Integer.parseInt(splitString[0]);
                    settings[1] = Integer.parseInt(splitString[1]);
                    notificationText.setText("Setting saved.");
                } catch (Exception exception) {
                    notificationText.setText("Invalid setting.");
                }
            } else {
                notificationText.setText("Invalid setting.");
            }
        }

        if(e.getSource() == getWatermark) {
            if (!(finalWatermarkFile == null || finalSaveDirectory == null)) {
                try {
                    BufferedImage watermark = WatermarkImage.getWatermark(ImageIO.read(finalWatermarkFile));

                    DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                    LocalDateTime currentDate = LocalDateTime.now();
                    String saveName = "watermark-" + currentDate.format(date);

                    Main.saveImage(watermark, finalSaveDirectory.getAbsolutePath(), saveName);
                    notificationText.setText("Watermark saved at " + finalSaveDirectory.getAbsolutePath() + File.separator + saveName);
                } catch (Exception exception) {
                    notificationText.setText("An error occurred while extracting watermark.");
                }
            } else {
                notificationText.setText("The required inputs have not been set.");
            }
        }

        if(e.getSource() == removeWatermark) {
            if (!(finalWatermarkFile == null || finalSaveDirectory == null || finalWatermarkedFileDir == null)) {
                try {
                    if(finalWatermarkedFileDir.isDirectory()) {
                        File[] watermarked = getDirectoryFiles(finalWatermarkedFileDir);
                        BufferedImage[] listWatermarkRemoved = new BufferedImage[watermarked.length];
                        if(settings[1] == 987654321) {
                            for(int i = 0; i < watermarked.length; i++) {
                                listWatermarkRemoved[i] = WatermarkImage.removeWatermarkCorner(watermarked[i],ImageIO.read(finalWatermarkFile), settings[0]);
                            }
                        } else {
                            for(int i = 0; i < watermarked.length; i++) {
                                listWatermarkRemoved[i] = WatermarkImage.removeWatermarkCoords(watermarked[i],ImageIO.read(finalWatermarkFile), settings[0], settings[1]);
                            }
                        }
                        // saves list of files of original with watermark removed
                        for(int i = 0; i < listWatermarkRemoved.length; i++) {
                            String saveName = finalWatermarkedFileDir.getName().substring(0,finalWatermarkedFileDir.getName().indexOf("."));
                            Main.saveImage(listWatermarkRemoved[i], finalSaveDirectory.getAbsolutePath(), saveName);
                        }
                        notificationText.setText("Files with watermark removed saved at " + finalSaveDirectory.getAbsolutePath());
                    } else {
                        BufferedImage watermarkRemoved;
                        // removes watermark
                        if (settings[1] == 987654321) {
                            watermarkRemoved = WatermarkImage.removeWatermarkCorner(finalWatermarkedFileDir,ImageIO.read(finalWatermarkFile), settings[0]);
                        } else {
                            watermarkRemoved = WatermarkImage.removeWatermarkCoords(finalWatermarkedFileDir,ImageIO.read(finalWatermarkFile), settings[0], settings[1]);
                        }
                        // saves file of original with watermark removed
                        String saveName = finalWatermarkedFileDir.getName().substring(0,finalWatermarkedFileDir.getName().indexOf("."));
                        Main.saveImage(watermarkRemoved, finalSaveDirectory.getAbsolutePath(), saveName);
                        notificationText.setText("Files with watermark removed saved at " + finalSaveDirectory.getAbsolutePath() + File.separator + saveName);

                    }
                } catch (Exception exception) {
                    notificationText.setText("An error occurred while removing watermark.");
                }
            } else {
                notificationText.setText("The required inputs have not been set.");
            }
        }

        if(e.getSource() == getAndRemoveWatermark) {
            if (!(finalWatermarkFile == null || finalSaveDirectory == null || finalWatermarkedFileDir == null)) {
                try {
                    BufferedImage extractedWatermark = WatermarkImage.getWatermark(ImageIO.read(finalWatermarkFile));
                    if(finalWatermarkedFileDir.isDirectory()) {
                        File[] watermarked = getDirectoryFiles(finalWatermarkedFileDir);
                        BufferedImage[] listWatermarkRemoved = new BufferedImage[watermarked.length];
                        if(settings[1] == 987654321) {
                            for(int i = 0; i < watermarked.length; i++) {
                                listWatermarkRemoved[i] = WatermarkImage.removeWatermarkCorner(watermarked[i],extractedWatermark, settings[0]);
                            }
                        } else {
                            for(int i = 0; i < watermarked.length; i++) {
                                listWatermarkRemoved[i] = WatermarkImage.removeWatermarkCoords(watermarked[i],extractedWatermark, settings[0], settings[1]);
                            }
                        }
                        // saves list of files of original with watermark removed
                        for(int i = 0; i < listWatermarkRemoved.length; i++) {
                            String saveName = watermarked[i].getName().substring(0, watermarked[i].getName().indexOf("."));
                            Main.saveImage(listWatermarkRemoved[i], finalSaveDirectory.getAbsolutePath(), saveName);
                        }
                        notificationText.setText("Files with watermark removed saved at " + finalSaveDirectory.getAbsolutePath());
                    } else {
                        BufferedImage watermarkRemoved;
                        // removes watermark
                        if (settings[1] == 987654321) {
                            watermarkRemoved = WatermarkImage.removeWatermarkCorner(finalWatermarkedFileDir,extractedWatermark, settings[0]);
                        } else {
                            watermarkRemoved = WatermarkImage.removeWatermarkCoords(finalWatermarkedFileDir,extractedWatermark, settings[0], settings[1]);
                        }
                        // saves file of original with watermark removed
                        String saveName = finalWatermarkedFileDir.getName().substring(0,finalWatermarkedFileDir.getName().indexOf("."));
                        Main.saveImage(watermarkRemoved, finalSaveDirectory.getAbsolutePath(), saveName);
                        notificationText.setText("Files with watermark removed saved at " + finalSaveDirectory.getAbsolutePath() + File.separator + saveName);

                    }
                } catch (Exception exception) {
                    notificationText.setText("An error occurred while extracting watermark.");
                }
            } else {
                notificationText.setText("The required inputs have not been set.");
            }
        }

        if(e.getSource() == getAndRemoveWatermarkBlack) {
            if (!(finalWatermarkFile == null || finalSaveDirectory == null || finalWatermarkedFileDir == null || finalWatermarkBlackFile == null)) {
                try {
                    BufferedImage extractedWatermark = WatermarkImage.getWatermark(ImageIO.read(finalWatermarkFile));
                    BufferedImage extractedWatermarkB = WatermarkImage.getWatermarkBlack(ImageIO.read(finalWatermarkBlackFile));
                    if(finalWatermarkedFileDir.isDirectory()) {
                        File[] watermarked = getDirectoryFiles(finalWatermarkedFileDir);
                        BufferedImage[] listWatermarkRemoved = new BufferedImage[watermarked.length];
                        if(settings[1] == 987654321) {
                            for(int i = 0; i < watermarked.length; i++) {
                                listWatermarkRemoved[i] = WatermarkImage.removeWatermarkCornerFull(watermarked[i],extractedWatermark, extractedWatermarkB, settings[0], bias);
                            }
                        } else {
                            for(int i = 0; i < watermarked.length; i++) {
                                listWatermarkRemoved[i] = WatermarkImage.removeWatermarkCoords(watermarked[i],extractedWatermark, settings[0], settings[1]);
                            }
                        }
                        // saves list of files of original with watermark removed
                        for(int i = 0; i < listWatermarkRemoved.length; i++) {
                            String saveName = watermarked[i].getName().substring(0, watermarked[i].getName().indexOf("."));
                            Main.saveImage(listWatermarkRemoved[i], finalSaveDirectory.getAbsolutePath(), saveName);
                        }
                        notificationText.setText("Files with watermark removed saved at " + finalSaveDirectory.getAbsolutePath());
                    } else {
                        BufferedImage watermarkRemoved;
                        // removes watermark
                        if (settings[1] == 987654321) {
                            watermarkRemoved = WatermarkImage.removeWatermarkCornerFull(finalWatermarkedFileDir,extractedWatermark, extractedWatermarkB, settings[0], bias);
                        } else {
                            watermarkRemoved = WatermarkImage.removeWatermarkCoords(finalWatermarkedFileDir,extractedWatermark, settings[0], settings[1]);
                        }
                        // saves file of original with watermark removed
                        String saveName = finalWatermarkedFileDir.getName().substring(0,finalWatermarkedFileDir.getName().indexOf("."));
                        Main.saveImage(watermarkRemoved, finalSaveDirectory.getAbsolutePath(), saveName);
                        notificationText.setText("Files with watermark removed saved at " + finalSaveDirectory.getAbsolutePath() + File.separator + saveName);

                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    notificationText.setText("An error occurred while extracting watermark.");
                }
            } else {
                notificationText.setText("The required inputs have not been set.");
            }
        }

        if(e.getSource() == BWBias) {
            try{
                float biasSetting = Float.parseFloat(BWBias.getText());
                if(biasSetting >= 0 && biasSetting <= 2) {
                    bias = biasSetting;
                    notificationText.setText("Bias set");
                } else {
                    notificationText.setText("Invalid Bias Setting");
                }
            } catch (Exception exception) {
                notificationText.setText("Setting not recognised as an number");
            }
        }
    }

    public static File chooseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }

    }

    public static File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image File", "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        else{
            return null;
        }
    }

    public static File[] getDirectoryFiles(File f) {
        String[] stringFiles = f.list();
        File[] listFiles = new File[stringFiles.length];
        for(int i = 0; i < listFiles.length; i++) {
            listFiles[i] = new File(f.toString() + File.separator + stringFiles[i]);
        }
        return listFiles;
    }
}
