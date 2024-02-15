import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The PiEncoder class is a Java application for encoding and decoding files using the digits of Pi.
 * It allows users to select files and either encode or decode them based on the Pi digits.
 */

public class PiEncoder extends JFrame {

	/**
     * Array of digits of Pi used for the encoding/decoding process.
     */
    private byte[] piDigits = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5, 0, 2, 8, 8, 4, 1, 9, 7, 1, 6, 9, 3, 9, 9, 3, 7, 5, 1, 0, 5, 8, 2, 0, 9, 7, 4, 9, 4, 4, 5, 9, 2, 3, 0, 7, 8, 1, 6, 4, 0, 6, 2, 8, 6, 2, 0};
    private JTextField inputText, outputText;
    private JToggleButton encodeDecodeToggle;
    private JButton openFileButton;

    /**
     * Constructor for PiEncoder. Sets up the GUI layout and initializes the application.
     */
    
    public PiEncoder() {
        // Setting up the basic frame of the application
        setTitle("Pi Encoder");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    /**
     * Places and initializes GUI components on the panel.
     * @param panel The JPanel on which components are added.
     */
    
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Initializing and placing text fields and labels on the panel
        inputText = new JTextField(20);
        inputText.setBounds(100, 20, 165, 25);
        panel.add(inputText);

        outputText = new JTextField(20);
        outputText.setBounds(100, 60, 165, 25);
        panel.add(outputText);

        JLabel inputLabel = new JLabel("Input File:");
        inputLabel.setBounds(10, 20, 80, 25);
        panel.add(inputLabel);

        // Initializing and placing buttons on the panel
        JButton browseInputButton = new JButton("Browse");
        browseInputButton.setBounds(280, 20, 100, 25);
        panel.add(browseInputButton);

        JLabel outputLabel = new JLabel("Output File:");
        outputLabel.setBounds(10, 60, 80, 25);
        panel.add(outputLabel);

        JButton browseOutputButton = new JButton("Browse");
        browseOutputButton.setBounds(280, 60, 100, 25);
        panel.add(browseOutputButton);

        JButton encodeButton = new JButton("Process");
        encodeButton.setBounds(150, 130, 150, 25);
        panel.add(encodeButton);

        encodeDecodeToggle = new JToggleButton("Encode", true);
        encodeDecodeToggle.setBounds(10, 130, 120, 25);
        panel.add(encodeDecodeToggle);

        openFileButton = new JButton("Open File");
        openFileButton.setBounds(310, 130, 100, 25);
        panel.add(openFileButton);

        // Adding functionality to toggle between encode and decode
        encodeDecodeToggle.addActionListener(e -> {
            if (encodeDecodeToggle.isSelected()) {
                encodeDecodeToggle.setText("Encode");
            } else {
                encodeDecodeToggle.setText("Decode");
            }
        });

        // File chooser for selecting the input file
        browseInputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text, RTF, PDF, Word Documents", "txt", "rtf", "pdf", "doc", "docx");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(true);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    inputText.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        // File chooser for selecting the output file
        browseOutputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    outputText.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        // Handling the encoding or decoding process
        encodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputFilePath = inputText.getText();
                boolean isEncode = encodeDecodeToggle.isSelected();
                String suffix = isEncode ? "_encoded" : "_decoded";
                String outputFilePath = inputFilePath + suffix;
                outputText.setText(outputFilePath);
                new Thread(() -> {
                    try {
                        processFile(inputFilePath, outputFilePath, isEncode);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
                    }
                }).start();
            }
        });

        // Opening the processed file
        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String outputFilePath = outputText.getText();
                openFile(outputFilePath);
            }
        });
    }
    /**
     * Processes (encodes or decodes) the selected file.
     * @param inputFilePath  Path of the input file.
     * @param outputFilePath Path where the processed file will be saved.
     * @param isEncode       Flag to determine whether to encode or decode.
     */
    // Method for encoding or decoding a file
    private void processFile(String inputFilePath, String outputFilePath, boolean isEncode) {
        try (FileInputStream inputStream = new FileInputStream(inputFilePath);
             FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {

            int byteRead;
            int piIndex = 0;

            while ((byteRead = inputStream.read()) != -1) {
                byte processedByte = (byte) (byteRead ^ piDigits[piIndex]);
                outputStream.write(processedByte);
                piIndex = (piIndex + 1) % piDigits.length;
            }

            JOptionPane.showMessageDialog(null, "File " + (isEncode ? "encoded" : "decoded") + " successfully!");
            outputText.setText(outputFilePath);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
        }
    }
    /**
     * Opens the specified file using the desktop's default application.
     * @param filePath Path of the file to be opened.
     */
    // Method to open the file with the default application
    private void openFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No file path specified.");
            return;
        }

        try {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(filePath);
            if (file.exists()) {
                desktop.open(file);
            } else {
                JOptionPane.showMessageDialog(null, "File does not exist: " + filePath);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not open the file: " + ex.getMessage());
        }
    }
    /**
     * Main method to launch the PiEncoder application.
     * @param args Command-line arguments, not used in this application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PiEncoder());
    }
}

