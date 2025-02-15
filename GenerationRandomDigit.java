import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerationRandomDigit extends JFrame {
    // ================== CONSTANTS ==================
    private static final int STRING_LENGTH = 8; // Length of the random string
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Character pool for generating strings
    private static final Random random = new Random(); // Random object for generating strings
    private static final String RESTRICTED_CHARS = "I1S50O"; // Special characters restricted to one per string
    // ================================================

    private JTextField quantityField, addressField;
    private String saveDirectory = ""; // Directory to save files

    // Constructor: Initializes the frame and UI
    public GenerationRandomDigit() {
        setupFrame();
        setupUI();
    }

    // Sets up the main frame properties
    private void setupFrame() {
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
    }

    // Sets up the user interface components
    private void setupUI() {
        JLabel quantityLabel = new JLabel("Nhập số lượng:");
        quantityLabel.setBounds(50, 50, 100, 30);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(160, 50, 100, 30);
        add(quantityField);

        JLabel directoryLabel = new JLabel("Nhập vị trí lưu:");
        directoryLabel.setBounds(50, 100, 100, 30);
        add(directoryLabel);

        addressField = new JTextField();
        addressField.setBounds(160, 100, 200, 30);
        addressField.setEditable(false);
        add(addressField);

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBounds(370, 100, 80, 30);
        searchButton.addActionListener(e -> chooseSaveDirectory());
        add(searchButton);

        JButton generateButton = new JButton("Tạo code");
        generateButton.setBounds(160, 150, 100, 30);
        generateButton.addActionListener(e -> generateCodes());
        add(generateButton);
    }

    // Allows the user to choose a save directory
    private void chooseSaveDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            saveDirectory = selectedDirectory.getAbsolutePath() + "/";
            addressField.setText(saveDirectory);
        }
    }

    // Generates a random 8-character string ensuring at most one restricted character
    private String generateRandomString() {
        while (true) {
            StringBuilder sb = new StringBuilder(STRING_LENGTH);
            for (int i = 0; i < STRING_LENGTH; i++) {
                sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
            }

            int restrictedCount = 0;
            for (int i = 0; i < STRING_LENGTH; i++) {
                if (RESTRICTED_CHARS.indexOf(sb.charAt(i)) != -1) {
                    restrictedCount++;
                }
            }

            if (restrictedCount <= 1) {
                return sb.toString();
            }
        }
    }

    // Finds the next available file name (e.g., random_8_digit_1.txt, 2.txt, etc.)
    private File getNextFile() {
        int index = 1;
        while (true) {
            File f = new File(saveDirectory + "random_8_digit_" + index + ".txt");
            if (!f.exists()) {
                return f;
            }
            index++;
        }
    }

    // Saves the set of unique strings to a file
    private void saveToFile(Set<String> uniqueStrings) {
        File file = getNextFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String code : uniqueStrings) {
                writer.write(code);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tạo code thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Tạo code thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Generates random strings based on the entered quantity and saves them to a file
    private void generateCodes() {
        if (saveDirectory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Làm ơn hãy nhập địa chỉ lưu code.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Làm ơn nhập số lượnglượng.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Set<String> codes = new HashSet<>();
        while (codes.size() < quantity) {
            codes.add(generateRandomString());
        }

        saveToFile(codes);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GenerationRandomDigit app = new GenerationRandomDigit();
            app.setVisible(true);
        });
    }
}
