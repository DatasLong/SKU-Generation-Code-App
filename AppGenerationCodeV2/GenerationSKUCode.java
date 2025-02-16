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

/**
 * GenerationSKUCode là lớp chính cho ứng dụng tạo SKU Code.
 * Lớp này xây dựng giao diện người dùng và tích hợp toàn bộ các hàm xử lý được lấy nguyên từ phiên bản V3.
 */
public class GenerationSKUCode extends JFrame {

    // --------------------
    // BIẾN GIAO DIỆN
    // --------------------
    private JTextField quantityField, blendingCountryField, yyField, targetCountryField, mmField, ddField, addressField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JTextField random8DigitField;

    // --------------------
    // BIẾN LOGIC
    // --------------------
    private String saveDirectory = "";
    private String randomFilePath = "";

    // --------------------
    // CONSTRUCTOR
    // --------------------
    /**
     * Constructor khởi tạo giao diện và gọi các hàm cài đặt.
     */
    public GenerationSKUCode() {
        setupFrame();
        setupInputFields();
        setupTable();
        setupButtons();
        setupLabels();
        setupDocumentListener();
    }

    // --------------------
    // HÀM CÀI ĐẶT FRAME
    // --------------------
    /**
     * Cài đặt cửa sổ chính của ứng dụng:
     * - Màu nền, tiêu đề, kích thước, chế độ đóng cửa sổ, layout, và vị trí hiển thị.
     */
    private void setupFrame() {
        getContentPane().setBackground(Color.WHITE);
        // setTitle("SKU Generation Application");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // --------------------
    // HÀM CÀI ĐẶT TRƯỜNG NHẬP
    // --------------------
    /**
     * Khởi tạo và cài đặt các trường nhập liệu cho ứng dụng, bao gồm:
     * - Số lượng, mã quốc gia pha trộn, năm (YY), mã quốc gia đích (tự động cập nhật),
     *   tháng (MM), ngày (DD), địa chỉ lưu file SKU, và file chứa mã 8 ký tự.
     */
    private void setupInputFields() {
        quantityField = createTextField(200, 107, 200, 30, "Quantity");
        blendingCountryField = createTextField(200, 148, 200, 30, "Blending Country/ Target Country");
        yyField = createTextField(200, 189, 200, 30, "YY");
        targetCountryField = createTextField(640, 107, 200, 30, "Blending Country/ Target Country");
        targetCountryField.setEditable(false); // Vẫn giữ non-editable nếu cần
        mmField = createTextField(640, 148, 200, 30, "MM");
        ddField = createTextField(640, 189, 200, 30, "DD");
        addressField = createTextField(200, 230, 200, 30, "");
        addressField.setEditable(false);
        random8DigitField = createTextField(200, 270, 200, 30, "");
        random8DigitField.setEditable(false); // Không cho phép nhập liệu
    }

    /**
     * Tạo một JTextField với placeholder.
     *
     * @param x          Tọa độ x của trường nhập.
     * @param y          Tọa độ y của trường nhập.
     * @param width      Chiều rộng của trường nhập.
     * @param height     Chiều cao của trường nhập.
     * @param placeholder Văn bản gợi ý khi chưa có dữ liệu.
     * @return Trả về JTextField đã được tạo.
     */
    private JTextField createTextField(int x, int y, int width, int height, String placeholder) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if(textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if(textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        getContentPane().add(textField);
        return textField;
    }

    // --------------------
    // HÀM CÀI ĐẶT BẢNG HIỂN THỊ
    // --------------------
    /**
     * Cài đặt bảng hiển thị SKU Code với tiêu đề cột và độ rộng cột.
     * Bảng được thêm vào một JScrollPane để có thanh cuộn.
     */
    private void setupTable() {
        tableModel = new DefaultTableModel(new Object[] {
            "No", "Blending Country", "YY [2]", "Target Country",
            "MM [2]", "Random digit [8]", "DD [2]", "SKU Code"
        }, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(40, 325, 800, 200);
        getContentPane().add(scrollPane);

        // Cài đặt độ rộng cột
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        resultTable.getColumnModel().getColumn(4).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        resultTable.getColumnModel().getColumn(6).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(7).setPreferredWidth(150);

        // Căn giữa tiêu đề cột
        JTableHeader header = resultTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // --------------------
    // HÀM CÀI ĐẶT NÚT BẤM
    // --------------------
    /**
     * Cài đặt các nút bấm trên giao diện:
     * - Nút "Search" (cho chọn thư mục lưu SKU Code)
     * - Nút "Generate SKU Codes" (tạo mã SKU)
     * - Nút "Search" (cho chọn file chứa mã 8 ký tự)
     */
    private void setupButtons() {
        JButton searchButton = new JButton("Tìm");
        searchButton.setBounds(434, 230, 100, 30);
        searchButton.addActionListener(e -> chooseSaveDirectory());
        getContentPane().add(searchButton);

        JButton generateButton = new JButton("tạo mã SKU");
        generateButton.setBounds(640, 230, 200, 30);
        generateButton.addActionListener(e -> generateSKUCodes());
        getContentPane().add(generateButton);

        JButton randomFileButton = new JButton("Tìm");
        randomFileButton.setBounds(434, 270, 100, 30);
        randomFileButton.addActionListener(e -> chooseRandom8DigitFile());
        getContentPane().add(randomFileButton);
    }

    // --------------------
    // HÀM CÀI ĐẶT NHÃN
    // --------------------
    /**
     * Cài đặt các nhãn hiển thị cho giao diện.
     */
    private void setupLabels() {
        addLabel("Nhập số lượng:", 40, 107, 150, 30);
        addLabel("Nhập mã quốc gia:", 40, 148, 150, 30);
        addLabel("Nhập năm:", 40, 189, 150, 30);
        addLabel("Mã quốc gia:", 480, 107, 150, 30);
        addLabel("Nhập năm:", 480, 148, 150, 30);
        addLabel("Nhập ngày:", 480, 189, 150, 30);
        addLabel("Lưu mã SKU:", 40, 230, 150, 30);

        JLabel titleLabel = new JLabel("CHƯƠNG TRÌNH TẠO MÃ SKU");
        titleLabel.setBounds(40, 20, 800, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        getContentPane().add(titleLabel);

        addLabel("Lưu mã random 8 ký tự:", 40, 270, 150, 30);
    }

    /**
     * Thêm một nhãn vào cửa sổ tại vị trí và kích thước xác định.
     *
     * @param text   Nội dung của nhãn.
     * @param x      Tọa độ x.
     * @param y      Tọa độ y.
     * @param width  Chiều rộng.
     * @param height Chiều cao.
     */
    private void addLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        getContentPane().add(label);
    }

    // --------------------
    // HÀM CÀI ĐẶT DOCUMENT LISTENER
    // --------------------
    /**
     * Cài đặt DocumentListener cho trường nhập blendingCountryField.
     * Mỗi khi giá trị thay đổi, trường targetCountryField sẽ được cập nhật.
     */
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

    // ----------------------------
    // HÀM XỬ LÝ (COPY NGUYÊN TỪ V3)
    // ----------------------------

    /**
     * Mở hộp thoại chọn thư mục để lưu file SKU Code.
     * Đường dẫn được lưu vào saveDirectory và hiển thị trên addressField.
     */
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

    /**
     * Mở hộp thoại chọn file chứa mã có 8 ký tự.
     * Đường dẫn file được lưu vào randomFilePath và hiển thị trên random8DigitField.
     */
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

    /**
     * Đọc các mã từ file được chỉ định.
     * Chỉ lấy những dòng có đúng 8 ký tự (không quan tâm nội dung).
     *
     * @param filePath Đường dẫn đến file chứa mã.
     * @return Danh sách các mã hợp lệ.
     */
    private List<String> readRandomCodesFromFile(String filePath) {
        List<String> codes = new ArrayList<>();
        if (filePath == null || filePath.trim().isEmpty()) {
            return codes;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Nếu dòng có đúng 8 ký tự thì thêm vào danh sách
                if (line.matches(".{8}")) {
                    codes.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy file random 8 ký tự.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return codes;
    }

    /**
     * Tạo SKU Code dựa trên dữ liệu nhập và danh sách mã 8 ký tự.
     * Hàm kiểm tra các giá trị nhập (quantity, blending country, YY, MM, DD),
     * sau đó kết hợp các giá trị này với mã từ file để tạo SKU Code.
     * Các SKU Code được hiển thị trong bảng, lưu vào file mới, và file chứa mã được ghi đè với các mã chưa sử dụng.
     */
    private void generateSKUCodes() {
        if (saveDirectory == null || saveDirectory.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Làm ơn chọn địa chỉ lưu mã SKU!.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (randomFilePath == null || randomFilePath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Làm ơn chọn địa chỉ lưu mã random 8 ký tự!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Số lượng phải lớn hơn 0!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Số lượng phải là số.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String blendingCountry = blendingCountryField.getText().trim();
        if (blendingCountry.isEmpty() || blendingCountry.length() > 9 || !blendingCountry.matches("[A-Z]*")) {
            JOptionPane.showMessageDialog(this,
                "Mã quốc gia phải là chữ hoa và tối đa là 9 ký tự.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String yy = yyField.getText().trim();
        try {
            int yyInt = Integer.parseInt(yy);
            if (yyInt < 25 || yyInt > 99) {
                JOptionPane.showMessageDialog(this,
                    "Năm phải từ 25 - 99.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Năm phải là số.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
                "Tháng phải là sốsố.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dd = ddField.getText().trim();
        try {
            int ddInt = Integer.parseInt(dd);
            if (ddInt < 1 || ddInt > 31) {
                JOptionPane.showMessageDialog(this,
                    "Ngày phải từ 1 - 31.",
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

        // Lấy giá trị target country từ targetCountryField (đã được cập nhật tự động)
        String targetCountry = targetCountryField.getText().trim();

        tableModel.setRowCount(0);

        List<String> randomCodes = readRandomCodesFromFile(randomFilePath);
        Set<String> uniqueStrings = new HashSet<>();

        int index = 1;
        int codeIndex = 0;

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

        if (index <= quantity) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy file random mã 8 ký tự!\n" +
                "Generated only " + (index - 1) + " codes.",
                "Warning", JOptionPane.WARNING_MESSAGE);
        }

        saveToFile(uniqueStrings);

        if (codeIndex > 0) {
            rewriteRemainingCodes(randomFilePath, randomCodes, codeIndex);
        }
    }

    /**
     * Định dạng chuỗi số sao cho có 2 ký tự (nếu cần, thêm số 0 ở đầu).
     *
     * @param input Chuỗi đầu vào.
     * @return Chuỗi có đúng 2 ký tự.
     */
    private String formatToTwoDigits(String input) {
        if (input.length() == 1) {
            return "0" + input;
        }
        return input;
    }

    /**
     * Ghi đè file chứa mã với các mã còn lại (sau khi đã sử dụng).
     *
     * @param filePath  Đường dẫn đến file chứa mã.
     * @param allCodes  Danh sách các mã đã đọc được.
     * @param usedCount Số lượng mã đã sử dụng.
     */
    private void rewriteRemainingCodes(String filePath, List<String> allCodes, int usedCount) {
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

    /**
     * Lưu các SKU Code đã tạo ra vào một file mới.
     * File được đặt tên là "SKU code.txt" (không thay đổi bất kỳ dòng nào khác).
     *
     * @param uniqueStrings Tập hợp các SKU Code duy nhất.
     */
    private void saveToFile(Set<String> uniqueStrings) {
        // Đổi từ "SKU_Codes_" + System.currentTimeMillis() + ".txt"
        // sang "SKU code.txt" theo yêu cầu, giữ nguyên các dòng khác.
        File file = new File(saveDirectory + "SKU code.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String code : uniqueStrings) {
                writer.write(code);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Mã SKU đã được tạo thành công.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Không thể tạo mã SKU.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --------------------
    // MAIN FUNCTION
    // --------------------
    /**
     * Phương thức main để khởi chạy ứng dụng.
     *
     * @param args Các đối số dòng lệnh (không sử dụng).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GenerationSKUCode gui = new GenerationSKUCode();
            gui.setVisible(true);
        });
    }
}
