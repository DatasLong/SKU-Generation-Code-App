import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueStringGeneratorGUI_V3 extends JFrame {

    // Các thành phần giao diện
    private JTextField quantityField, blendingCountryField, yyField, targetCountryField, mmField, ddField, addressField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private String saveDirectory = ""; // Thư mục lưu file

    // Thêm trường nhập địa chỉ file random_8_digit
    private JTextField random8DigitField; 
    private String randomFilePath = "";

    public UniqueStringGeneratorGUI_V3() {
        setupFrame();
        setupInputFields();
        setupTable();
        setupButtons();
        setupLabels();
        setupDocumentListener();
    }

    // Thiết lập cửa sổ chính
    private void setupFrame() {
        getContentPane().setBackground(new Color(255, 255, 255));
        setTitle("SKU Generation Application");
        setSize(900, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null); // Hiển thị cửa sổ ở giữa màn hình
    }

    // Thiết lập các trường nhập liệu
    private void setupInputFields() {
        quantityField = createTextField(200, 107, 200, 30);
        blendingCountryField = createTextField(200, 148, 200, 30);
        yyField = createTextField(200, 189, 200, 30);
        targetCountryField = createTextField(640, 107, 200, 30);
        targetCountryField.setEditable(false);
        mmField = createTextField(640, 148, 200, 30);
        ddField = createTextField(640, 189, 200, 30);
        addressField = createTextField(200, 230, 200, 30);
        addressField.setEditable(false);

        // Ô nhập địa chỉ file random_8_digit
        random8DigitField = createTextField(200, 270, 200, 30);
        random8DigitField.setEditable(false); 
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        getContentPane().add(textField);
        return textField;
    }

    // Thiết lập bảng kết quả
    private void setupTable() {
        tableModel = new DefaultTableModel(new Object[] {
            "No", "Blending Country", "YY [2]", "Target Country",
            "MM [2]", "Random digit [8]", "DD [2]", "SKU Code"
        }, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        // Dời bảng xuống (từ 285 -> 325)
        scrollPane.setBounds(40, 325, 800, 200);
        getContentPane().add(scrollPane);

        // Thiết lập độ rộng của các cột
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        resultTable.getColumnModel().getColumn(4).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        resultTable.getColumnModel().getColumn(6).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(7).setPreferredWidth(150);

        // Căn giữa tiêu đề của bảng
        JTableHeader header = resultTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer =
                (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // Thiết lập các nút bấm
    private void setupButtons() {
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(434, 230, 100, 30);
        searchButton.addActionListener(e -> chooseSaveDirectory());
        getContentPane().add(searchButton);

        JButton generateButton = new JButton("Generate SKU Codes");
        generateButton.setBounds(640, 230, 200, 30);
        generateButton.addActionListener(e -> generateSKUCodes());
        getContentPane().add(generateButton);

        // Nút tìm file random_8_digit
        JButton randomFileButton = new JButton("Search");
        randomFileButton.setBounds(434, 270, 100, 30);
        randomFileButton.addActionListener(e -> chooseRandom8DigitFile());
        getContentPane().add(randomFileButton);
    }

    // Thiết lập các nhãn
    private void setupLabels() {
        addLabel("Enter Quantity:", 40, 107, 150, 30); 
        addLabel("Enter Blending Country:", 40, 148, 150, 30);
        addLabel("Enter YY:", 40, 189, 150, 30);
        addLabel("Target Country:", 480, 107, 150, 30);
        addLabel("Enter MM:", 480, 148, 150, 30);
        addLabel("Enter DD:", 480, 189, 150, 30);
        addLabel("Save Directory:", 40, 230, 150, 30);

        JLabel titleLabel = new JLabel("GENERATION SKU CODE APP");
        titleLabel.setBounds(40, 20, 800, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        getContentPane().add(titleLabel);

        addLabel("8-digit File:", 40, 270, 150, 30);
    }

    private void addLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        getContentPane().add(label);
    }

    // DocumentListener để tự động cập nhật targetCountry
    private void setupDocumentListener() {
        blendingCountryField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTargetCountry();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTargetCountry();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTargetCountry();
            }
            private void updateTargetCountry() {
                targetCountryField.setText(blendingCountryField.getText());
            }
        });
    }

    // Chọn thư mục lưu file
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

    // Chọn file random_8_digit.txt
    private void chooseRandom8DigitFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            randomFilePath = selectedFile.getAbsolutePath();
            random8DigitField.setText(randomFilePath); 
        }
    }

    // Đọc tất cả mã 8 ký tự từ file
    private List<String> readRandomCodesFromFile(String filePath) {
        List<String> codes = new ArrayList<>();
        if (filePath == null || filePath.trim().isEmpty()) {
            return codes;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                codes.add(line.trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error reading random_8_digit file.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return codes;
    }

    // Hàm tạo SKU Codes
    private void generateSKUCodes() {
        // Kiểm tra saveDirectory
        if (saveDirectory.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a save directory before generating SKU codes.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra file random_8_digit
        if (randomFilePath == null || randomFilePath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select 8-digit file!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra quantity
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Quantity must be greater than 0!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid quantity (number).",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy các trường còn lại
        String blendingCountry = blendingCountryField.getText().trim();
        String yy = yyField.getText().trim();
        String targetCountry = blendingCountry; 
        String mm = mmField.getText().trim();
        String dd = ddField.getText().trim();

        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);

        // Đọc file gốc
        List<String> randomCodes = readRandomCodesFromFile(randomFilePath);
        Set<String> uniqueStrings = new HashSet<>();

        int index = 1;
        int codeIndex = 0;

        // Tạo SKU code
        while (index <= quantity && codeIndex < randomCodes.size()) {
            String randomCode = randomCodes.get(codeIndex);
            codeIndex++;

            String formattedCode = blendingCountry + yy + targetCountry + mm + randomCode + dd;

            if (uniqueStrings.add(formattedCode)) {
                tableModel.addRow(new Object[] {
                    index++,
                    blendingCountry,
                    yy,
                    targetCountry,
                    mm,
                    randomCode,
                    dd,
                    formattedCode
                });
            }
        }

        // Nếu file không đủ code
        if (index <= quantity) {
            JOptionPane.showMessageDialog(this,
                "Not enough 8-digit codes in the selected file!\n" +
                "Generated only " + (index - 1) + " codes.",
                "Warning", JOptionPane.WARNING_MESSAGE);
        }

        // Lưu các SKU đã tạo ra file SKU_Codes.txt
        saveToFile(uniqueStrings);

        // Tự động xóa số lượng code đã lấy (codeIndex) khỏi file gốc
        if (codeIndex > 0) {
            rewriteRemainingCodes(randomFilePath, randomCodes, codeIndex);
        }
    }

    // Hàm ghi đè file gốc, chỉ giữ lại các code từ codeIndex trở đi
    private void rewriteRemainingCodes(String filePath, List<String> allCodes, int usedCount) {
        // usedCount = số code đã dùng
        // leftover = các code còn lại
        List<String> leftover = allCodes.subList(usedCount, allCodes.size());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String c : leftover) {
                writer.write(c);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error rewriting the remaining codes to file.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Lưu SKU Codes vào file
    private void saveToFile(Set<String> uniqueStrings) {
        File file = new File(saveDirectory + "SKU_Codes.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String code : uniqueStrings) {
                writer.write(code);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "SKU codes saved successfully.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving SKU codes to file.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UniqueStringGeneratorGUI_V3 gui = new UniqueStringGeneratorGUI_V3();
            gui.setVisible(true);
        });
    }
}
