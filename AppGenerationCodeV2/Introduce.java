import javax.swing.*;
import java.awt.*;

public class Introduce extends JFrame {
    public Introduce() {
        setTitle("Giới thiệu");
        setSize(900, 600); // Kích thước giống MainGUI
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);         // Không cho phóng to
        setLocationRelativeTo(null); // Luôn xuất hiện giữa màn hình

        // Sử dụng null layout để thiết lập vị trí chính xác (giống GenerationSKUCode)
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);

        // Label tiêu đề được đặt như trong GenerationSKUCode
        JLabel titleLabel = new JLabel("CHƯƠNG TRÌNH TẠO MÃ SKU");
        titleLabel.setBounds(40, 20, 800, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        mainPanel.add(titleLabel);

        // Thêm mô tả phần mềm với nội dung đã có, đặt dưới tiêu đề (với khoảng cách và kích thước gần giống)
        String description = "<html>"
                + "<p><b>I. Chức năng Tạo code 8 ký tự</b></p>"
                + "<ul>"
                + "<li>Tạo ngẫu nhiên code gồm 8 chữ số</li>"
                + "<li>Mỗi code chỉ xuất hiện 6 ký tự đặc biệt 1 lần duy nhất (S,5,I,1,0,O)</li>"
                + "</ul>"
                + "<p><b>II. Chức năng tạo code SKU</b></p>"
                + "<ul>"
                + "<li>Tạo code SKU</li>"
                + "<li>Sau khi lấy code từ ngân hàng code sẽ tự động xóa code trong ngân hàng code</li>"
                + "<li>Ràng buộc dữ liệu:</li>"
                + "<ul>"
                + "<li>Số lượng > 0, phải là số</li>"
                + "<li>Mã quốc gia: phải là ký tự in hoa và < 9 ký tự</li>"
                + "<li>Năm: phải là số và từ 25 đến 99</li>"
                + "<li>MM: phải là số từ 1 đến 12</li>"
                + "<li>YY: phải là số từ 1 đến 31</li>"
                + "<li>Không được để trống dữ liệu</li>"
                + "</ul>"
                + "</ul>"
                + "</html>";
        JLabel descLabel = new JLabel(description);
        descLabel.setBounds(-60, 80, 800, 400); // Đặt vị trí và kích thước theo yêu cầu
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel.setForeground(Color.BLACK);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setOpaque(true);
        descLabel.setBackground(Color.WHITE);
        mainPanel.add(descLabel);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Introduce frame = new Introduce();
            frame.setVisible(true);
        });
    }
}
