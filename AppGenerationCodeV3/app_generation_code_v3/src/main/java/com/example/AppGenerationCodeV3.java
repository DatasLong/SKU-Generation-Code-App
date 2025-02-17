package com.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AppGenerationCodeV3 extends JFrame { // Đổi tên class thành AppGenerationCodeV3
    private static final int STRING_LENGTH = 8; // Độ dài của chuỗi ngẫu nhiên
    private static final String CHAR_POOL = "0123456789"; // Các ký tự có thể sử dụng
    private static final Random random = new Random(); // Đối tượng Random để tạo chuỗi ngẫu nhiên

    private JTextField quantityField, blendingCountryField, yyField, targetCountryField, mmField, ddField, addressField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private String saveDirectory = ""; // Thư mục lưu file

    public AppGenerationCodeV3() {
        setupFrame();
        setupInputFields();
        setupTable();
        setupButtons();
        setupLabels();
        setupDocumentListener();
    }

    private void setupFrame() {
        getContentPane().setBackground(new Color(255, 255, 255));
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
    }

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
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        getContentPane().add(textField);
        return textField;
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(new Object[] { "No", "Blending Country", "YY [2]", "Target Country", "MM [2]", "Random digit [8]", "DD [2]", "SKU Code" }, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(40, 285, 800, 200);
        getContentPane().add(scrollPane);

        JTableHeader header = resultTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupButtons() {
        JButton searchButton = new JButton("Tìm");
        searchButton.setBounds(434, 230, 100, 30);
        searchButton.addActionListener(e -> chooseSaveDirectory());
        getContentPane().add(searchButton);

        JButton generateButton = new JButton("Tạo mã SKU");
        generateButton.setBounds(640, 230, 200, 30);
        generateButton.addActionListener(e -> generateSKUCodes());
        getContentPane().add(generateButton);
    }

    private void setupLabels() {
        addLabel("Nhập số lượng:", 40, 107, 150, 30);
        addLabel("Nhập mã quốc gia:", 40, 148, 150, 30);
        addLabel("Nhập năm:", 40, 189, 150, 30);
        addLabel("Mã quốc gia:", 480, 107, 150, 30);
        addLabel("Nhập tháng:", 480, 148, 150, 30);
        addLabel("Nhập ngày:", 480, 189, 150, 30);
        addLabel("Chọn địa chỉ lưu:", 40, 230, 150, 30);

        JLabel titleLabel = new JLabel("PHẦN MỀM TẠO MÃ SKU");
        titleLabel.setBounds(40, 20, 800, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        getContentPane().add(titleLabel);
    }

    private void addLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        getContentPane().add(label);
    }

    private void setupDocumentListener() {
        blendingCountryField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateTargetCountry(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateTargetCountry(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateTargetCountry(); }
            private void updateTargetCountry() {
                targetCountryField.setText(blendingCountryField.getText());
            }
        });
    }

    private Set<String> generatedStrings = new HashSet<>(); // Tập hợp để lưu tất cả các chuỗi đã tạo

    private String generateRandomString() {
        String randomString;
        do {
            StringBuilder sb = new StringBuilder(STRING_LENGTH);
            for (int i = 0; i < STRING_LENGTH; i++) {
                sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
            }
            randomString = sb.toString();
        } while (generatedStrings.contains(randomString)); // Lặp lại nếu mã đã tồn tại

        // Thêm mã vào tập hợp các mã đã tạo
        generatedStrings.add(randomString);
        return randomString;
    }

    public void chooseSaveDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            saveDirectory = selectedDirectory.getAbsolutePath() + "/";
            addressField.setText(saveDirectory);
        }
    }

    private void generateSKUCodes() {
        // Kiểm tra thư mục lưu
        if (saveDirectory == null || saveDirectory.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "làm ơn chọn ổ đĩa lưu mã SKU.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra số lượng
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Số lượng phải > 0!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Số lượng phải là số.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra Blending Country
        String blendingCountry = blendingCountryField.getText().trim();
        if (blendingCountry.isEmpty() || blendingCountry.length() > 9 || !blendingCountry.matches("[A-Z]*")) {
            JOptionPane.showMessageDialog(this,
                "mã quốc gia phải là chữ in hoa và <=9 ký tự.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra YY
        String yy = yyField.getText().trim();
        try {
            int yyInt = Integer.parseInt(yy);
            if (yyInt < 25 || yyInt > 99) {
                JOptionPane.showMessageDialog(this,
                    "Năm phải nằm trong 25 - 99.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Năm phải là số.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra MM
        String mm = mmField.getText().trim();
        try {
            int mmInt = Integer.parseInt(mm);
            if (mmInt < 1 || mmInt > 12) {
                JOptionPane.showMessageDialog(this,
                    "Tháng phải từ 1 - 12.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mm = formatToTwoDigits(mm);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Tháng phải là số.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra DD
        String dd = ddField.getText().trim();
        try {
            int ddInt = Integer.parseInt(dd);
            if (ddInt < 1 || ddInt > 31) {
                JOptionPane.showMessageDialog(this,
                    "Ngày phải là số nằm trong 1 -31.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dd = formatToTwoDigits(dd);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Ngày phải là số.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Lấy Target Country (được cập nhật tự động từ blendingCountryField)
        String targetCountry = targetCountryField.getText().trim();

        tableModel.setRowCount(0);
        Set<String> uniqueStrings = new HashSet<>();

        int index = 1;
        while (index <= quantity) {
            String randomCode = generateRandomString();
            String formattedCode = blendingCountry + yy + targetCountry + mm + randomCode + dd;
            if (uniqueStrings.add(formattedCode)) {
                tableModel.addRow(new Object[] { index++, blendingCountry, yy, targetCountry, mm, randomCode, dd, formattedCode });
            }
        }
        saveToFile(uniqueStrings);
        exportToExcel();
    }

    // Phương thức xuất mã SKU ra file Excel (mã SKU sẽ nằm ở cột đầu tiên)
private void exportToExcel() {
    if (tableModel.getRowCount() == 0) {
        return;
    }

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("SKU Codes");

    // Ghi tiêu đề cột
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("SKU Code");
    headerRow.createCell(1).setCellValue("Fixed Code");

    // Danh sách 4 chuỗi cố định
    String[] fixedCodes = {"219466", "219487", "233175", "217143"};
    int fixedCodeCount = fixedCodes.length;

    // Tính toán số lượng SKU Code cho mỗi chuỗi cố định
    int quantity = tableModel.getRowCount();
    int itemsPerFixedCode = quantity / fixedCodeCount;
    int remainder = quantity % fixedCodeCount; // Nếu không chia hết, nhóm đầu tiên nhận thêm phần dư

    int fixedIndex = 0;
    int countForCurrentFixed = 0;

    for (int i = 0; i < quantity; i++) {
        Row row = sheet.createRow(i + 1);

        // Cột 1: SKU Code (Lấy từ cột SKU hiện tại)
        Object skuValue = tableModel.getValueAt(i, 7);
        String skuCode = (skuValue != null) ? skuValue.toString() : "LỖI DỮ LIỆU";
        row.createCell(0).setCellValue(skuCode);

        // Cột 2: Fixed Code (Chia đều)
        String fixedCode = fixedCodes[fixedIndex];
        row.createCell(1).setCellValue(fixedCode);

        countForCurrentFixed++;

        // Kiểm soát việc chia đều chuỗi cố định
        if (countForCurrentFixed >= itemsPerFixedCode + (fixedIndex < remainder ? 1 : 0)) {
            fixedIndex++;
            countForCurrentFixed = 0;
        }
    }

    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);

    File file = new File(saveDirectory + "SKU code V3.xlsx");
    try (FileOutputStream fos = new FileOutputStream(file)) {
        workbook.write(fos);
        workbook.close();
        JOptionPane.showMessageDialog(this, "Xuất mã SKU ra file Excel thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Xuất mã SKU ra file Excel thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}



    private String formatToTwoDigits(String input) {
        if (input.length() == 1) {
            return "0" + input;
        }
        return input;
    }

    private void saveToFile(Set<String> uniqueStrings) {
        File file = new File(saveDirectory + "SKU code V3.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String code : uniqueStrings) {
                writer.write(code);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tạo mã SKU thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Tạo mã SKU thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppGenerationCodeV3 gui = new AppGenerationCodeV3();
            gui.setVisible(true);
        });
    }
}
