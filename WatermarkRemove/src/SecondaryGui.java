import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SecondaryGui {
    private JPanel mainPanel;
    private JTextField watermarkPath;
    private JButton watermarkSelect;
    private JTextField addWatermarkImagePath;
    private JButton addWatermarkDirectorySelect;
    private JTextField savePathPath;
    private JButton savePathSelect;
    private JButton addWatermarkFileSelect;
    private JTextArea notificationText;
    private JTextArea settingsText;
    private JTextField watermarkSettings;
    private JButton addWatermark;

    private File finalAddWatermarkFileDir, finalWatermarkFile, finalSaveDirectory;
    private int settings[] = new int[2];

    public SecondaryGui() {
        JFrame main = new JFrame("Watermark Adder");
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
        watermarkSelect.setText("Select Watermark File");
        addWatermarkFileSelect.setText("Select Image to Watermark");
        addWatermarkDirectorySelect.setText("Select Image Directory");
        savePathSelect.setText("Select Save Directory");
        // setting text in text fields
        watermarkPath.setText("Watermark file");
        addWatermarkImagePath.setText("Image to Watermark file or directory");
        savePathPath.setText("Save path");
        // setting text for watermark removing and getting
        addWatermark.setText("Add Watermark to Image [Saves Watermarked Image]");
        // add action listener
        watermarkPath.addActionListener(this::actionPerformed);
        watermarkSelect.addActionListener(this::actionPerformed);
        addWatermarkImagePath.addActionListener(this::actionPerformed);
        addWatermarkDirectorySelect.addActionListener(this::actionPerformed);
        addWatermarkFileSelect.addActionListener(this::actionPerformed);
        savePathPath.addActionListener(this::actionPerformed);
        savePathSelect.addActionListener(this::actionPerformed);
        watermarkSettings.addActionListener(this::actionPerformed);
        addWatermark.addActionListener(this::actionPerformed);
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
            File file = MainGui.chooseFile();
            finalWatermarkFile = file;
            watermarkPath.setText(file.getAbsolutePath());
            notificationText.setText("Watermark file successfully selected.");

        }

        if(e.getSource() == addWatermarkImagePath) {
            String path = addWatermarkImagePath.getText();
            File file = new File(path);
            if(file.isDirectory()) {
                finalAddWatermarkFileDir = file;
                notificationText.setText("Watermarked image directory selected.");
            }
            else if(!file.isFile()) {
                finalAddWatermarkFileDir = null;
                notificationText.setText("Watermarked image file selected does not exist.");
            }
            else if(path.contains(".png") || path.contains(".jpg") ) {
                finalAddWatermarkFileDir = file;
                notificationText.setText("Watermarked image file successfully selected.");
            } else {
                finalAddWatermarkFileDir = null;
                notificationText.setText("Invalid file type.");
            }
        }
        if(e.getSource() == addWatermarkDirectorySelect) {
            File file = MainGui.chooseDirectory();
            finalAddWatermarkFileDir = file;
            addWatermarkImagePath.setText(file.getAbsolutePath());
            notificationText.setText("Watermarked image file successfully selected.");
        }
        if(e.getSource() == addWatermarkFileSelect) {
            File file = MainGui.chooseFile();
            finalAddWatermarkFileDir = file;
            addWatermarkImagePath.setText(file.getAbsolutePath());
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
            File file = MainGui.chooseDirectory();
            if (file.isDirectory()) {
                finalSaveDirectory = file;
                savePathPath.setText(file.getAbsolutePath());
                notificationText.setText("Save directory selected.");
            } else {
                finalAddWatermarkFileDir = null;
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

        if(e.getSource() == addWatermark) {
            if (!(finalWatermarkFile == null || finalAddWatermarkFileDir == null || finalSaveDirectory == null)) {
                DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                LocalDateTime currentDate = LocalDateTime.now();
                try {
                    if(finalAddWatermarkFileDir.isDirectory()) {
                        File[] watermarked = MainGui.getDirectoryFiles(finalAddWatermarkFileDir);
                        BufferedImage[] listWatermarkAdded = new BufferedImage[watermarked.length];
                        if(settings[1] == 987654321) {
                            for(int i = 0; i < watermarked.length; i++) {
                                System.out.println(i);
                                listWatermarkAdded[i] = WatermarkImage.addWatermarkCorner(watermarked[i],ImageIO.read(finalWatermarkFile), settings[0]);
                            }
                        } else {
                            for(int i = 0; i < watermarked.length; i++) {
                                listWatermarkAdded[i] = WatermarkImage.addWatermarkCoords(watermarked[i],ImageIO.read(finalWatermarkFile), settings[0], settings[1]);
                            }
                        }
                        // saves list of files of original with watermark added
                        String curDate = currentDate.format(date);
                        for(int i = 0; i < listWatermarkAdded.length; i++) {
                            String saveName = watermarked[i].getName() + "-" + curDate;
                            Main.saveImage(listWatermarkAdded[i], finalSaveDirectory.getAbsolutePath(), saveName);
                        }
                        notificationText.setText("Files with watermark added saved at " + finalSaveDirectory.getAbsolutePath());
                    } else {
                        BufferedImage watermarkAdded;
                        // adds watermark
                        if (settings[1] == 987654321) {
                            watermarkAdded = WatermarkImage.addWatermarkCorner(finalAddWatermarkFileDir,ImageIO.read(finalWatermarkFile), settings[0]);
                        } else {
                            watermarkAdded = WatermarkImage.addWatermarkCoords(finalAddWatermarkFileDir,ImageIO.read(finalWatermarkFile), settings[0], settings[1]);
                        }
                        // saves file of original with watermark added
                        String saveName = finalAddWatermarkFileDir.getName() + "-" +  currentDate.format(date);
                        Main.saveImage(watermarkAdded, finalSaveDirectory.getAbsolutePath(), saveName);
                        notificationText.setText("Files with watermark added saved at " + finalSaveDirectory.getAbsolutePath() + File.separator + saveName);
                    }
                } catch (Exception exception) {
                    notificationText.setText("An error occurred while removing watermark.");
                    exception.printStackTrace();
                }
            } else {
                notificationText.setText("The required inputs have not been set.");
            }
        }
    }

    public void print(String p) {
        System.out.println(p);
    }
}
