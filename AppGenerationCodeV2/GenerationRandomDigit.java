import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * GenerationRandomDigit là lớp chính cho ứng dụng tạo code 8 ký tự.
 * Lớp này xây dựng giao diện người dùng và xử lý tạo code theo yêu cầu.
 */
public class GenerationRandomDigit extends JFrame {
    // ================== HẰNG SỐ ==================
    private static final int STRING_LENGTH = 8; // Độ dài chuỗi ngẫu nhiên
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Bộ ký tự dùng để tạo chuỗi
    private static final Random random = new Random(); // Đối tượng Random để tạo chuỗi
    private static final String RESTRICTED_CHARS = "I1S50O"; // Ký tự đặc biệt bị giới hạn 1 lần trong chuỗi
    // ==============================================
    
    private JTextField quantityField, addressField;
    private String saveDirectory = ""; // Thư mục lưu file

    // --- THÊM: Các thành phần JTable để hiển thị các code đã tạo ---
    private JTable codeTable;
    private javax.swing.table.DefaultTableModel codeTableModel;
    
    // Constructor: Khởi tạo frame và giao diện người dùng
    public GenerationRandomDigit() {
    	getContentPane().setBackground(new Color(255, 255, 255));
        setupFrame();
        setupUI();
    }
    
    // Cài đặt thuộc tính chính của frame (theo file SKU: 900×600)
    private void setupFrame() {
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
    }
    
    // Cài đặt các thành phần giao diện người dùng theo phong cách của GenerationSKUCode.java
    private void setupUI() {
        // Title Label
        JLabel titleLabel = new JLabel("CHƯƠNG TRÌNH TẠO CODE 8 KÝ TỰ");
        titleLabel.setBounds(40, 20, 800, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        getContentPane().add(titleLabel);
        
        // Label "Nhập số lượng:" và trường nhập số lượng
        JLabel quantityLabel = new JLabel("Nhập số lượng:");
        quantityLabel.setBounds(40, 107, 150, 30);
        getContentPane().add(quantityLabel);
        
        quantityField = new JTextField();
        quantityField.setBounds(200, 107, 200, 30);
        // Thiết lập placeholder "Quantity" sử dụng FocusListener
        quantityField.setForeground(Color.GRAY);
        quantityField.setText("Quantity");
        quantityField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if(quantityField.getText().equals("Quantity")) {
                    quantityField.setText("");
                    quantityField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if(quantityField.getText().trim().isEmpty()) {
                    quantityField.setText("Quantity");
                    quantityField.setForeground(Color.GRAY);
                }
            }
        });
        getContentPane().add(quantityField);
        
        // Label "Nhập địa chỉ lưu:" và trường nhập địa chỉ lưu
        JLabel directoryLabel = new JLabel("Nhập địa chỉ lưu:");
        directoryLabel.setBounds(40, 148, 150, 30);
        getContentPane().add(directoryLabel);
        
        addressField = new JTextField();
        addressField.setBounds(200, 148, 200, 30);
        addressField.setEditable(false);
        getContentPane().add(addressField);
        
        // Nút "Tìm kiếm" để chọn thư mục lưu, bên cạnh trường địa chỉ
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBounds(434, 148, 100, 30);
        searchButton.addActionListener(e -> chooseSaveDirectory());
        getContentPane().add(searchButton);
        
        // Nút "Tạo code" đặt theo file SKU (ở cùng hàng với trường địa chỉ lưu)
        JButton generateButton = new JButton("Tạo code");
        generateButton.setBounds(640, 148, 200, 30);
        generateButton.addActionListener(e -> generateCodes());
        getContentPane().add(generateButton);
        
        // JTable hiển thị các code đã tạo, đặt dưới khoảng cách phù hợp như file SKU
        codeTableModel = new javax.swing.table.DefaultTableModel(new Object[]{"No", "Random digit [8]"}, 0);
        codeTable = new JTable(codeTableModel);
        JScrollPane scrollPane = new JScrollPane(codeTable);
        scrollPane.setBounds(40, 203, 800, 325);
        getContentPane().add(scrollPane);
    }
    
    // Cho phép người dùng chọn thư mục lưu
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
    
    // Tạo chuỗi ngẫu nhiên 8 ký tự với tối đa 1 ký tự bị hạn chế
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
    
    // Tìm tên file tiếp theo khả dụng (ví dụ: random_8_digit_1.txt, 2.txt, v.v.)
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
    
    // Lưu tập hợp các chuỗi duy nhất vào file
    private void saveToFile(Set<String> uniqueStrings) {
        File file = getNextFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String code : uniqueStrings) {
                writer.write(code);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tạo mã thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Tạo mã thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Tạo các chuỗi ngẫu nhiên dựa trên số lượng nhập và lưu chúng vào file
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
        
        // Validation: số lượng phải lớn hơn 0
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Set<String> codes = new HashSet<>();
        while (codes.size() < quantity) {
            codes.add(generateRandomString());
        }
        
        // Cập nhật JTable hiển thị các code với cột "No"
        codeTableModel.setRowCount(0);
        int no = 1;
        for (String code : codes) {
            codeTableModel.addRow(new Object[]{no++, code});
        }
        
        saveToFile(codes);
    }
    
    // Phương thức chính để khởi chạy ứng dụng
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GenerationRandomDigit app = new GenerationRandomDigit();
            app.setVisible(true);
        });
    }
}
