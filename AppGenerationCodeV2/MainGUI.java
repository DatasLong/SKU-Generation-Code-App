import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private JPanel rightPanel;

    public MainGUI() {
        setTitle("Java Swing Demo");
        setSize(1080, 600); // Kích thước frame không thay đổi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(null);
        leftPanel.setPreferredSize(new Dimension(160, 600));
        leftPanel.setBackground(new Color(173, 255, 47));
        leftPanel.setOpaque(true);

        int buttonWidth = 140;
        int buttonHeight = (int) Math.round(buttonWidth / 3.0); // ≈ 47

        JPanel emptySquare = new JPanel();
        emptySquare.setBounds(10, 10, buttonWidth, buttonWidth);
        String imagePath = "./Pictures/download.png";
        ImageIcon icon = new ImageIcon(imagePath);
        System.out.println("Đường dẫn ảnh: " + imagePath);
        if (icon.getIconWidth() <= 0) {
            System.out.println("Ảnh chưa được load. Vui lòng kiểm tra lại đường dẫn hoặc tên file!");
        } else {
            Image scaledImage = icon.getImage().getScaledInstance(buttonWidth, buttonWidth, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
        }
        JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        emptySquare.setLayout(new BorderLayout());
        emptySquare.add(imageLabel, BorderLayout.CENTER);
        leftPanel.add(emptySquare);

        int margin = 10;
        JButton btnIntro = new JButton("Giới thiệu");
        btnIntro.setBounds(10, 10 + buttonWidth + margin, buttonWidth, buttonHeight);
        JButton btnGenerateCode = new JButton("Tạo code 8 ký tự");
        btnGenerateCode.setBounds(10, 10 + buttonWidth + margin + buttonHeight + margin, buttonWidth, buttonHeight);
        JButton btnGenerateSKU = new JButton("Tạo SKU code");
        btnGenerateSKU.setBounds(10, 10 + buttonWidth + margin + 2 * (buttonHeight + margin), buttonWidth, buttonHeight);
        leftPanel.add(btnIntro);
        leftPanel.add(btnGenerateCode);
        leftPanel.add(btnGenerateSKU);

        rightPanel = new JPanel(new CardLayout());
        rightPanel.setPreferredSize(new Dimension(920, 600));

        // Thay thế card "Giới thiệu" bằng nội dung của Introduce.java
        Introduce introduceFrame = new Introduce();
        JPanel introducePanel = new JPanel(new BorderLayout());
        introducePanel.add(introduceFrame.getContentPane(), BorderLayout.CENTER);
        // Thêm left padding 40px
        introducePanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        rightPanel.add(introducePanel, "Giới thiệu");

        GenerationRandomDigit codePanel = new GenerationRandomDigit();
        GenerationSKUCode skuPanel = new GenerationSKUCode();
        rightPanel.add(codePanel.getContentPane(), "Tạo code 8 ký tự");
        rightPanel.add(skuPanel.getContentPane(), "Tạo SKU code");

        btnIntro.addActionListener(e -> showPanel("Giới thiệu"));
        btnGenerateCode.addActionListener(e -> showPanel("Tạo code 8 ký tự"));
        btnGenerateSKU.addActionListener(e -> showPanel("Tạo SKU code"));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void showPanel(String panelName) {
        ((CardLayout) rightPanel.getLayout()).show(rightPanel, panelName);
    }

    public static void main(String[] args) {
        MainGUI frame = new MainGUI();
        frame.setVisible(true);
    }
}
