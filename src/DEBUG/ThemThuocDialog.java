import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import BUS.BUSManager;
import DTO.DanhMucThuocDTO;
import DTO.ThuocDTO;
import helper.IDGenerator;
import helper.ImageSelector;

public class ThemThuocDialog extends JDialog {
    private int maThuoc = IDGenerator.generateUniqueID();
    private JTextField txtTenThuoc, txtDonViTinh, txtNhaSanXuat, txtXuatXu;
    private NumberOnlyField txtGia;
    private JComboBox<DanhMucThuocDTO> comboDanhMuc;
    private JLabel imageLabel;
    private String imagePath = "image/placeholder_them_thuoc.jpg";
    private boolean confirmed = false;

    public ThemThuocDialog(JFrame parent) {
        super(parent, "🩺 Thêm thuốc mới", true);
        setSize(1280, 720);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        // ======= PANEL CHÍNH =======
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // =========================
        // 🔹 PANEL TRÁI: FORM THÔNG TIN
        // =========================
        JPanel infoPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        infoPanel.setBorder(new TitledBorder("Thông tin thuốc"));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        // Mã thuốc
        infoPanel.add(new JLabel("Mã thuốc:", SwingConstants.RIGHT));
        JLabel lblMaThuoc = new JLabel(String.valueOf(maThuoc));
        lblMaThuoc.setFont(font);
        infoPanel.add(lblMaThuoc);

        // Danh mục thuốc
        infoPanel.add(new JLabel("Danh mục:", SwingConstants.RIGHT));
        comboDanhMuc = new JComboBox<>();
        comboDanhMuc.setFont(font);
        loadDanhMuc();
        infoPanel.add(comboDanhMuc);

        // Tên thuốc
        infoPanel.add(new JLabel("Tên thuốc:", SwingConstants.RIGHT));
        txtTenThuoc = new JTextField();
        txtTenThuoc.setFont(font);
        infoPanel.add(txtTenThuoc);

        // Giá
        infoPanel.add(new JLabel("Giá tiền (VND):", SwingConstants.RIGHT));
        txtGia = new NumberOnlyField();
        txtGia.setFont(font);
        infoPanel.add(txtGia);

        // Đơn vị tính
        infoPanel.add(new JLabel("Đơn vị tính:", SwingConstants.RIGHT));
        txtDonViTinh = new JTextField();
        txtDonViTinh.setFont(font);
        infoPanel.add(txtDonViTinh);

        // Nhà sản xuất
        infoPanel.add(new JLabel("Nhà sản xuất:", SwingConstants.RIGHT));
        txtNhaSanXuat = new JTextField();
        txtNhaSanXuat.setFont(font);
        infoPanel.add(txtNhaSanXuat);

        // Xuất xứ
        infoPanel.add(new JLabel("Xuất xứ:", SwingConstants.RIGHT));
        txtXuatXu = new JTextField();
        txtXuatXu.setFont(font);
        infoPanel.add(txtXuatXu);

        mainPanel.add(infoPanel);

        // =========================
        // 🔹 PANEL PHẢI: ẢNH THUỐC
        // =========================
        JPanel imagePanel = new JPanel(new BorderLayout(10, 10));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(new TitledBorder("Hình ảnh thuốc"));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 250));
        updateImagePreview(imagePath);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JButton chooseImage = new JButton("Chọn ảnh");
        chooseImage.setFocusPainted(false);
        chooseImage.addActionListener(e -> {
            String selectedPath = ImageSelector.chooseAndCopyImage(parent);
            if (selectedPath != null) {
                imagePath = selectedPath;
                updateImagePreview(imagePath);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi chọn hình ảnh!");
            }
        });
        imagePanel.add(chooseImage, BorderLayout.SOUTH);

        mainPanel.add(imagePanel);

        // =========================
        // 🔹 PANEL DƯỚI: NÚT HÀNH ĐỘNG
        // =========================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnCancel = new JButton("Hủy");
        JButton btnOK = new JButton("Xác nhận");

        btnCancel.addActionListener(e -> setVisible(false));
        btnOK.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                setVisible(false);
            }
        });

        btnOK.setBackground(new Color(0, 120, 215));
        btnOK.setForeground(Color.WHITE);
        btnOK.setFocusPainted(false);
        btnOK.setFont(new Font("Segoe UI", Font.BOLD, 14));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnOK);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDanhMuc() {
        ArrayList<DanhMucThuocDTO> dsDanhMuc = BUSManager.danhMucThuocBUS.getListDanhMucThuoc();
        comboDanhMuc.removeAllItems();
        for (DanhMucThuocDTO dm : dsDanhMuc) {
            comboDanhMuc.addItem(dm);
        }
    }

    private void updateImagePreview(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
    }

    private boolean validateInput() {
        if (txtTenThuoc.getText().trim().isEmpty() ||
                txtGia.getText().trim().isEmpty() ||
                txtDonViTinh.getText().trim().isEmpty() ||
                txtNhaSanXuat.getText().trim().isEmpty() ||
                txtXuatXu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }

        try {
            new BigDecimal(txtGia.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền không hợp lệ!");
            return false;
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public ThuocDTO getThuoc() {
        DanhMucThuocDTO dmt = (DanhMucThuocDTO) comboDanhMuc.getSelectedItem();
        return new ThuocDTO(
                maThuoc,
                dmt != null ? dmt.getMaDmt() : 0,
                txtTenThuoc.getText(),
                new BigDecimal(txtGia.getText()),
                txtDonViTinh.getText(),
                txtNhaSanXuat.getText(),
                txtXuatXu.getText(),
                imagePath,
                1);
    }
}
