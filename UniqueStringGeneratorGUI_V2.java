import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UniqueStringGeneratorGUI_V2 extends JFrame {
    // ================== (GIỮ NGUYÊN) ==================
    private static final int STRING_LENGTH = 8; // Độ dài của chuỗi ngẫu nhiên
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Các ký tự có thể sử dụng
    private static final Random random = new Random(); // Đối tượng Random để tạo chuỗi ngẫu nhiên

    // Thêm hằng chứa 6 ký tự đặc biệt (để đếm số lần xuất hiện)
    private static final String RESTRICTED_CHARS = "I1S50O";
    // ==================================================

    private JTextField quantityField, addressField;
    private String saveDirectory = ""; // Thư mục lưu file

    public UniqueStringGeneratorGUI_V2() {
        setupFrame();
        setupUI();
    }

    // Tạo cửa sổ chính
    private void setupFrame() {
        setTitle("Two Function App");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
    }

    // Tạo giao diện đơn giản
    private void setupUI() {
        JLabel quantityLabel = new JLabel("Enter Quantity:");
        quantityLabel.setBounds(50, 50, 100, 30);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(160, 50, 100, 30);
        add(quantityField);

        JLabel directoryLabel = new JLabel("Save Directory:");
        directoryLabel.setBounds(50, 100, 100, 30);
        add(directoryLabel);

        addressField = new JTextField();
        addressField.setBounds(160, 100, 200, 30);
        addressField.setEditable(false);
        add(addressField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(370, 100, 80, 30);
        // ================== (GIỮ NGUYÊN) ==================
        searchButton.addActionListener(e -> chooseSaveDirectory());
        // ==================================================
        add(searchButton);

        JButton generateButton = new JButton("Generate");
        generateButton.setBounds(160, 150, 100, 30);
        generateButton.addActionListener(e -> generateCodes());
        add(generateButton);
    }

    // ================== (GIỮ NGUYÊN) ==================
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
    // ==================================================

    // Đảm bảo mỗi code chỉ chứa tối đa 1 ký tự trong RESTRICTED_CHARS
    private String generateRandomString() {
        while (true) {
            StringBuilder sb = new StringBuilder(STRING_LENGTH);
            // Sinh ngẫu nhiên 8 ký tự từ CHAR_POOL
            for (int i = 0; i < STRING_LENGTH; i++) {
                sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
            }
            
            // Đếm số ký tự đặc biệt xuất hiện trong mã
            int restrictedCount = 0;
            for (int i = 0; i < STRING_LENGTH; i++) {
                if (RESTRICTED_CHARS.indexOf(sb.charAt(i)) != -1) {
                    restrictedCount++;
                }
            }
            
            // Chấp nhận mã nếu có tối đa 1 ký tự đặc biệt (0 hoặc 1)
            if (restrictedCount <= 1) {
                return sb.toString();
            }
            // Nếu vượt quá 1 ký tự đặc biệt, tiếp tục sinh mã mới
        }
    }
    

    // Tìm file random_8_digit_1.txt, random_8_digit_2.txt, ... chưa tồn tại
    private File getNextFile() {
        int index = 1;
        while (true) {
            // Thêm đuôi .txt vào tên file
            File f = new File(saveDirectory + "random_8_digit_" + index + ".txt");
            if (!f.exists()) {
                return f;
            }
            index++;
        }
    }

    private void saveToFile(Set<String> uniqueStrings) {
        // Lấy file random_8_digit_1.txt, 2.txt,... chưa tồn tại
        File file = getNextFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String code : uniqueStrings) {
                writer.write(code);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "SKU codes saved successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving SKU codes to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm tạo mã dựa trên số lượng nhập
    private void generateCodes() {
        if (saveDirectory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a save directory before generating codes.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Set<String> codes = new HashSet<>();
        // Tạo đúng 'quantity' mã 8 ký tự (mỗi mã tối đa 1 ký tự đặc biệt)
        while (codes.size() < quantity) {
            codes.add(generateRandomString());
        }

        // Lưu các mã này ra file (random_8_digit_1.txt, 2.txt, 3.txt, ...)
        saveToFile(codes);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UniqueStringGeneratorGUI_V2 app = new UniqueStringGeneratorGUI_V2();
            app.setVisible(true);
        });
    }
}
