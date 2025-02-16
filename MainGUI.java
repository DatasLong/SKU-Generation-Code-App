import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private JPanel rightPanel;

    public MainGUI() {
        setTitle("Java Swing Demo");
        setSize(1080, 600); // Updated width to 1080
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        leftPanel.setPreferredSize(new Dimension(160, 600));

        JButton btnIntro = new JButton("Giới thiệu");
        btnIntro.setPreferredSize(new Dimension(140, 50));

        JButton btnGenerateCode = new JButton("Tạo code 8 ký tự");
        btnGenerateCode.setPreferredSize(new Dimension(140, 50));

        JButton btnGenerateSKU = new JButton("Tạo SKU code");
        btnGenerateSKU.setPreferredSize(new Dimension(140, 50));

        leftPanel.add(btnIntro);
        leftPanel.add(btnGenerateCode);
        leftPanel.add(btnGenerateSKU);

        rightPanel = new JPanel(new CardLayout());
        rightPanel.setPreferredSize(new Dimension(920, 600));

        JPanel introPanel = new JPanel();
        JLabel introLabel = new JLabel("Giới thiệu");
        introLabel.setFont(new Font("Arial", Font.BOLD, 50)); // Set font size to 50
        introLabel.setForeground(Color.RED); // Set text color to red
        introPanel.add(introLabel);

        GenerationRandomDigit codePanel = new GenerationRandomDigit();
        GenerationSKUCode skuPanel = new GenerationSKUCode();

        rightPanel.add(introPanel, "Giới thiệu");
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
