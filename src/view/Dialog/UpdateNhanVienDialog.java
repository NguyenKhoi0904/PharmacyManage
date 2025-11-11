package view.Dialog;

import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.function.BiConsumer;
import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import BUS.BUSManager;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import utils.NumberOnlyField;
import view.NhanVienForm;

public class UpdateNhanVienDialog extends JDialog {
    private JTextField Luong, email, diaChi, viTri;
    private JTextField taiKhoan, matKhau, ten, sdt;
    private JDateChooser ngayVaoLam, ngaySinh;
    private JComboBox<String> comboGioiTinh;
    private NhanVienForm parent;
    private JLabel lblMaNv, lblMaTk;
    private String matKhauCu = "", matKhauMoi = "";
    private static SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

    public UpdateNhanVienDialog(NhanVienForm parent, NhanVienDTO nhanVienDTO) {
        super(parent, "Cập nhật nhân viên", true);
        this.parent = parent;
        TaiKhoanDTO taiKhoanDTO = BUSManager.taiKhoanBUS.getTaiKhoanByMaTk(nhanVienDTO.getMaTk());
        matKhauCu = taiKhoanDTO.getMatKhau();
        setSize(1080, 800);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        // ======= PANEL CHÍNH =======
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // =========================
        // 🔹 PANEL CHÍNH: FORM THÔNG TIN
        // =========================
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        infoPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        int[] row = { 0 };

        BiConsumer<String, JComponent> addRow = (label, field) -> {
            gbc.gridx = 0;
            gbc.gridy = row[0];
            gbc.weightx = 0.3;
            JLabel lbl = new JLabel(label, SwingConstants.RIGHT);
            lbl.setFont(font);
            infoPanel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            infoPanel.add(field, gbc);

            row[0]++;
        };

        // ======= Các field =======
        lblMaTk = new JLabel();
        lblMaTk.setText(String.valueOf(nhanVienDTO.getMaTk()));
        lblMaTk.setFont(font);
        addRow.accept("Mã Tài Khoản:", lblMaTk);

        lblMaNv = new JLabel();
        lblMaNv.setText(String.valueOf(nhanVienDTO.getMaNv()));
        lblMaNv.setFont(font);
        addRow.accept("Mã Nhân Viên:", lblMaNv);

        ngayVaoLam = new JDateChooser();
        ngayVaoLam.setDateFormatString("yyyy-MM-dd");
        ngayVaoLam.setDate(nhanVienDTO.getNgayVaoLam());
        ngayVaoLam.setFont(font);
        addRow.accept("Ngày vào làm:", ngayVaoLam);

        Luong = new NumberOnlyField();
        Luong.setText(String.valueOf(nhanVienDTO.getLuong()));
        Luong.setFont(font);
        addRow.accept("Lương:", Luong);

        email = new JTextField();
        email.setText(nhanVienDTO.getEmail());
        email.setFont(font);
        addRow.accept("Email:", email);

        diaChi = new JTextField();
        diaChi.setText(nhanVienDTO.getDiaChi());
        diaChi.setFont(font);
        addRow.accept("Địa chỉ:", diaChi);

        comboGioiTinh = new JComboBox<>();
        comboGioiTinh.addItem("Nam");
        comboGioiTinh.addItem("Nữ");
        comboGioiTinh.setSelectedItem(nhanVienDTO.getGioiTinh());
        comboGioiTinh.setFont(font);
        addRow.accept("Giới tính:", comboGioiTinh);

        ngaySinh = new JDateChooser();
        ngaySinh.setDateFormatString("yyyy-MM-dd");
        ngaySinh.setDate(nhanVienDTO.getNgaySinh());
        ngaySinh.setFont(font);
        addRow.accept("Ngày sinh:", ngaySinh);

        viTri = new JTextField();
        viTri.setText(nhanVienDTO.getViTri());
        viTri.setFont(font);
        addRow.accept("Vị trí:", viTri);

        taiKhoan = new JTextField();
        taiKhoan.setText(taiKhoanDTO.getTaiKhoan());
        taiKhoan.setFont(font);
        addRow.accept("Tài Khoản", taiKhoan);

        matKhau = new JTextField();
        matKhau.setText(matKhauMoi);
        matKhau.setFont(font);
        addRow.accept("Mật Khẩu(Để trống xem như không thay đổi)", matKhau);

        ten = new JTextField();
        ten.setText(taiKhoanDTO.getTen());
        ten.setFont(font);
        addRow.accept("Tên", ten);

        sdt = new JTextField();
        sdt.setText(taiKhoanDTO.getSdt());
        sdt.setFont(font);
        addRow.accept("Số Điện Thoại", sdt);

        mainPanel.add(infoPanel);

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
                String gioiTinh = (String) comboGioiTinh.getSelectedItem();
                String ngayVaoLamStr = sdf.format(ngayVaoLam.getDate());
                String ngaySinhStr = sdf.format(ngaySinh.getDate());
                NhanVienDTO tempNhanVien = new NhanVienDTO(Integer.parseInt(lblMaNv.getText()),
                        Integer.parseInt(lblMaTk.getText()),
                        java.sql.Date.valueOf(ngayVaoLamStr),
                        new BigDecimal(Luong.getText()), email.getText(), diaChi.getText(),
                        gioiTinh, java.sql.Date.valueOf(ngaySinhStr), viTri.getText(), 1);

                // cập nhật tài khoản trên db
                matKhauMoi = matKhau.getText().trim();
                if (matKhauMoi.equals("")) {
                    matKhauMoi = matKhauCu;
                }
                if (!BUSManager.taiKhoanBUS
                        .updateTaiKhoan(new TaiKhoanDTO(Integer.parseInt(lblMaTk.getText()), taiKhoan.getText(),
                                matKhauMoi, ten.getText(), sdt.getText(), "nhanvien", 1))) {
                    JOptionPane.showMessageDialog(this, "Không thể cập nhật tài khoản");
                    return;
                }

                // cập nhật nhân viên trên db
                if (BUSManager.nhanVienBUS.updateNhanVien(tempNhanVien)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể cập nhật nhân viên");
                    return;
                }

                // load danh sách nhân viên
                this.parent.loadData();

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

    private boolean validateInput() {
        if (ngayVaoLam.getDate() == null || ngaySinh.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống ngày sinh hoặc ngày vào làm");
            return false;
        }

        if (Luong.getText().trim().isEmpty() ||
                viTri.getText().trim().isEmpty() ||
                email.getText().trim().isEmpty() ||
                diaChi.getText().trim().isEmpty() ||
                taiKhoan.getText().trim().isEmpty() ||
                ten.getText().trim().isEmpty() ||
                sdt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }

        try {
            new BigDecimal(Luong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền không hợp lệ!");
            return false;
        }

        // 🔹 Kiểm tra ngày sinh < ngày vào làm
        java.util.Date birthDate = ngaySinh.getDate();
        java.util.Date workDate = ngayVaoLam.getDate();

        if (!birthDate.before(workDate)) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải nhỏ hơn ngày vào làm!");
            return false;
        }

        // 🔹 Kiểm tra đủ 18 tuổi tại thời điểm vào làm
        long ageInMillis = workDate.getTime() - birthDate.getTime();
        double years = ageInMillis / (1000.0 * 60 * 60 * 24 * 365.25); // xấp xỉ năm

        if (years < 18) {
            JOptionPane.showMessageDialog(this, "Nhân viên phải đủ 18 tuổi trở lên tại thời điểm vào làm!");
            return false;
        }

        return true;
    }
}
