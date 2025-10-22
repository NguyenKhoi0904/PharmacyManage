package view;

import javax.swing.*;
import java.awt.*;

public class NhaCungCapForm extends JFrame {

    private JTextField txtMaNCC, txtTenNCC, txtDiaChi, txtSoDienThoai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTable tblNCC;

    public NhaCungCapForm() {
        setTitle("Quản lý Nhà Cung Cấp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // ===== TIÊU ĐỀ =====
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP", SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(0, 102, 204)); // xanh lam đậm
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== PANEL NHẬP LIỆU =====
        JPanel pnlInput = new JPanel(new GridLayout(4, 2, 15, 15));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblMa = new JLabel("Mã NCC:");
        JLabel lblTen = new JLabel("Tên NCC:");
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        JLabel lblSDT = new JLabel("Số điện thoại:");

        Font fontLabel = new Font("Segoe UI", Font.PLAIN, 18);
        lblMa.setFont(fontLabel);
        lblTen.setFont(fontLabel);
        lblDiaChi.setFont(fontLabel);
        lblSDT.setFont(fontLabel);

        txtMaNCC = new JTextField();
        txtTenNCC = new JTextField();
        txtDiaChi = new JTextField();
        txtSoDienThoai = new JTextField();

        Font fontField = new Font("Segoe UI", Font.PLAIN, 18);
        txtMaNCC.setFont(fontField);
        txtTenNCC.setFont(fontField);
        txtDiaChi.setFont(fontField);
        txtSoDienThoai.setFont(fontField);

        pnlInput.add(lblMa); pnlInput.add(txtMaNCC);
        pnlInput.add(lblTen); pnlInput.add(txtTenNCC);
        pnlInput.add(lblDiaChi); pnlInput.add(txtDiaChi);
        pnlInput.add(lblSDT); pnlInput.add(txtSoDienThoai);

        add(pnlInput, BorderLayout.CENTER);

        // ===== NÚT CHỨC NĂNG =====
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        JButton[] buttons = {btnThem, btnSua, btnXoa, btnLamMoi};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btn.setBackground(new Color(30, 144, 255));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(130, 45));
        }

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        add(pnlButtons, BorderLayout.SOUTH);

        // ===== BẢNG HIỂN THỊ =====
        String[] columnNames = {"Mã NCC", "Tên NCC", "Địa chỉ", "Số điện thoại"};
        Object[][] data = {
            {"NCC001", "Công ty Dược Phẩm ABC", "TP. HCM", "0909123456"},
            {"NCC002", "Nhà Cung Cấp Thuốc Bình Minh", "Hà Nội", "0988123456"}
        };
        tblNCC = new JTable(data, columnNames);
        tblNCC.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tblNCC.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(tblNCC);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách Nhà Cung Cấp"));
        add(scrollPane, BorderLayout.EAST);

        // ===== CÀI KÍCH THƯỚC KHUNG =====
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.65);
        int height = (int) (screenSize.height * 0.65);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NhaCungCapForm::new);
    }
}
