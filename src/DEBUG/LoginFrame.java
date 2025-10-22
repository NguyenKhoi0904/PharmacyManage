import javax.swing.*;

import BUS.BUSManager;
import BUS.TaiKhoanBUS;
import DTO.KhachHangDTO;
import DTO.NhanVienDTO;

import java.awt.*;
// import java.awt.event.*;

public class LoginFrame extends JFrame {
    // private final NhanVienBUS nhanVienBUS = NhanVienBUS.getInstance();
    // private final KhachHangBUS khachHangBUS = KhachHangBUS.getInstance();
    private final TaiKhoanBUS taiKhoanBUS = TaiKhoanBUS.getInstance();

    public LoginFrame() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel trái (chứa hình)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        // ⚠️ Thay đường dẫn ảnh của bạn tại đây
        ImageIcon icon = new ImageIcon("image.png");
        Image scaled = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        // Panel phải (chứa form)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(80, 40, 350, 30);
        rightPanel.add(titleLabel);

        JLabel userLabel = new JLabel("Tên đăng nhập");
        userLabel.setBounds(80, 100, 200, 25);
        rightPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(80, 130, 300, 35);
        rightPanel.add(userField);

        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setBounds(80, 180, 200, 25);
        rightPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(80, 210, 300, 35);
        rightPanel.add(passField);

        JCheckBox showPass = new JCheckBox("Hiện mật khẩu");
        showPass.setBackground(Color.WHITE);
        showPass.setBounds(80, 250, 150, 25);
        showPass.addActionListener(e -> {
            if (showPass.isSelected())
                passField.setEchoChar((char) 0);
            else
                passField.setEchoChar('•');
        });
        rightPanel.add(showPass);

        JButton loginBtn = new JButton("ĐĂNG NHẬP");
        loginBtn.setBounds(80, 300, 300, 40);
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        rightPanel.add(loginBtn);

        JLabel forgotLabel = new JLabel("<HTML><U>Quên mật khẩu</U></HTML>");
        forgotLabel.setForeground(Color.DARK_GRAY);
        forgotLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotLabel.setBounds(280, 350, 150, 25);
        rightPanel.add(forgotLabel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // login button logic
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            Object user = this.taiKhoanBUS.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Sai Mật Khẩu Hoặc Tài Khoản", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            // Phân biệt loại người dùng
            if (user instanceof NhanVienDTO nv) {
                JOptionPane.showMessageDialog(this, "Chào nhân viên: " + username);

                // chuyển trang nhân viên

                this.dispose();
            } else if (user instanceof KhachHangDTO kh) {
                JOptionPane.showMessageDialog(this, "Chào khách hàng: " + username);

                // chuyển trang khách hàng

                this.dispose();
            }
        });
    }

    public static void main(String[] args) {
        // BUS init
        BUSManager.initAllBUS();

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
