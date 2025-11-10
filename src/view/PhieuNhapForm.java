package view;

import BUS.LoHangBUS;
import BUS.NhanVienBUS;
import BUS.ChiTietPnBUS;
import BUS.PhieuNhapBUS;
import BUS.TaiKhoanBUS;
import DTO.PhieuNhapDTO;
import DTO.ChiTietPnDTO;
import DTO.TaiKhoanDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PhieuNhapForm extends JFrame {
    private JTable tblPhieuNhap;
    private JTable tblChiTiet;
    private DefaultTableModel modelPhieuNhap;
    private DefaultTableModel modelChiTiet;

    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTextField txtMaPn, txtDiaDiem;

    public PhieuNhapForm() {
        setTitle("Quản Lý Phiếu Nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 800);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 255, 255));
        JLabel lblTitle = new JLabel("QUẢN LÝ PHIẾU NHẬP");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // ================= FORM INPUT + BUTTON =================
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu nhập"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font lblFont = new Font("Segoe UI", Font.PLAIN, 18);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel lblMaPn = new JLabel("Mã PN:");
        lblMaPn.setFont(lblFont);
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(lblMaPn, gbc);

        txtMaPn = new JTextField(15);
        txtMaPn.setFont(txtFont);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(txtMaPn, gbc);

        JLabel lblDiaDiem = new JLabel("Địa điểm:");
        lblDiaDiem.setFont(lblFont);
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(lblDiaDiem, gbc);

        txtDiaDiem = new JTextField(20);
        txtDiaDiem.setFont(txtFont);
        gbc.gridx = 3; gbc.gridy = 0;
        inputPanel.add(txtDiaDiem, gbc);

        // Hiển thị mã NV hiện tại (chỉ đọc)
        JLabel lblNv = new JLabel("Nhân viên:");
        lblNv.setFont(lblFont);
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(lblNv, gbc);

        JTextField txtNhanVien = new JTextField(20);
        txtNhanVien.setFont(txtFont);
        txtNhanVien.setEditable(false);

        var nv = NhanVienBUS.getInstance().getNhanVienByMaTk(TaiKhoanBUS.getCurrentUser().getMaTk());
        if (nv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên cho tài khoản đăng nhập!");
            return;
        }
        int maNv = nv.getMaNv();
        txtNhanVien.setText("Mã NV: " + maNv);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 3;
        inputPanel.add(txtNhanVien, gbc);
        gbc.gridwidth = 1;

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnThem = createStyledButton("Thêm", new Color(76, 175, 80));
        btnXoa = createStyledButton("Xóa", new Color(244, 67, 54));
        btnSua = createStyledButton("Sửa", new Color(255, 152, 0));
        btnLamMoi = createStyledButton("Làm mới", new Color(25, 118, 210));

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnLamMoi);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ================= BẢNG SONG SONG =================
        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelPhieuNhap = new DefaultTableModel(
                new Object[]{"Mã PN", "Mã NV", "Thời gian nhập", "Địa điểm", "Trạng thái"}, 0);
        tblPhieuNhap = new JTable(modelPhieuNhap);
        tblPhieuNhap.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblPhieuNhap.setRowHeight(28);
        JScrollPane scrollPn = new JScrollPane(tblPhieuNhap);
        scrollPn.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));

        modelChiTiet = new DefaultTableModel(
                new Object[]{"Mã PN", "Mã Lô hàng", "Đơn giá", "Số lượng"}, 0);
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblChiTiet.setRowHeight(28);
        JScrollPane scrollCt = new JScrollPane(tblChiTiet);
        scrollCt.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        tablePanel.add(scrollPn);
        tablePanel.add(scrollCt);
        add(tablePanel, BorderLayout.CENTER);

        // ================= KẾT NỐI BUS =================
        PhieuNhapBUS pnBUS = PhieuNhapBUS.getInstance();
        ChiTietPnBUS ctBUS = ChiTietPnBUS.getInstance();
        pnBUS.setChiTietPnBUS(ctBUS);
        pnBUS.setLoHangBUS(LoHangBUS.getInstance());
        pnBUS.setNhanVienBUS(NhanVienBUS.getInstance());
        ctBUS.setLoHangBUS(LoHangBUS.getInstance());

        // ================= LOAD DỮ LIỆU BAN ĐẦU =================
        loadPhieuNhapData();

        // ================= SỰ KIỆN =================
        tblPhieuNhap.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuNhap.getSelectedRow();
                if (row >= 0) {
                    int maPn = (int) modelPhieuNhap.getValueAt(row, 0);
                    txtMaPn.setText(String.valueOf(maPn));
                    txtDiaDiem.setText(String.valueOf(modelPhieuNhap.getValueAt(row, 3)));
                    loadChiTietData(maPn);
                }
            }
        });

        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnThem.addActionListener(e -> themPhieuNhap());
        btnSua.addActionListener(e -> suaPhieuNhap());
        btnXoa.addActionListener(e -> xoaPhieuNhap());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setFocusPainted(false);
        return btn;
    }

    private void loadPhieuNhapData() {
        modelPhieuNhap.setRowCount(0);
        ArrayList<PhieuNhapDTO> list = PhieuNhapBUS.getInstance().getListPhieuNhap();
        for (PhieuNhapDTO pn : list) {
            modelPhieuNhap.addRow(new Object[]{
                    pn.getMaPn(), pn.getMaNv(), pn.getThoiGianNhap(), pn.getDiaDiem(), pn.getTrangThai()
            });
        }
    }

    private void loadChiTietData(int maPn) {
        modelChiTiet.setRowCount(0);
        ArrayList<ChiTietPnDTO> list = ChiTietPnBUS.getInstance().getListChiTietPnByMaPn(maPn);
        for (ChiTietPnDTO ct : list) {
            modelChiTiet.addRow(new Object[]{
                    ct.getMaPn(), ct.getMaLh(), ct.getDonGia(), ct.getSoLuong()
            });
        }
    }

    private void lamMoiForm() {
        txtMaPn.setText("");
        txtDiaDiem.setText("");
        modelChiTiet.setRowCount(0);
        loadPhieuNhapData();
    }

    private void themPhieuNhap() {
        try {
            if (txtDiaDiem.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập địa điểm!");
                return;
            }

            var nv = NhanVienBUS.getInstance().getNhanVienByMaTk(TaiKhoanBUS.getCurrentUser().getMaTk());
            if (nv == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên đăng nhập!");
                return;
            }
            int maNv = nv.getMaNv();
            String diaDiem = txtDiaDiem.getText().trim();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // Nếu mã PN rỗng → để DB tự sinh AUTO_INCREMENT
            int maPn = txtMaPn.getText().isEmpty() ? 0 : Integer.parseInt(txtMaPn.getText());

            PhieuNhapDTO pn = new PhieuNhapDTO(maPn, maNv, now, diaDiem, 1);
            boolean result = PhieuNhapBUS.getInstance().addPhieuNhap(pn, new ArrayList<>());

            if (result) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm! Có thể mã PN đã tồn tại hoặc lỗi dữ liệu!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phiếu nhập: " + ex.getMessage());
        }
    }

    private void suaPhieuNhap() {
        try {
            int row = tblPhieuNhap.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần sửa!");
                return;
            }

            int maPn = Integer.parseInt(txtMaPn.getText());
            var nv = NhanVienBUS.getInstance().getNhanVienByMaTk(TaiKhoanBUS.getCurrentUser().getMaTk());
            if (nv == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên cho tài khoản đăng nhập!");
                return;
            }
            int maNv = nv.getMaNv();
            String diaDiem = txtDiaDiem.getText().trim();

            // Giữ lại thời gian nhập cũ
            Timestamp thoiGianNhap = (Timestamp) modelPhieuNhap.getValueAt(row, 2);

            PhieuNhapDTO pn = new PhieuNhapDTO(maPn, maNv, thoiGianNhap, diaDiem, 1);
            boolean result = PhieuNhapBUS.getInstance().updatePhieuNhap(maPn, pn);

            if (result) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật phiếu nhập!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage());
        }
    }

    private void xoaPhieuNhap() {
        int row = tblPhieuNhap.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần xóa!");
            return;
        }

        int maPn = (int) modelPhieuNhap.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa phiếu nhập " + maPn + " ?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = PhieuNhapBUS.getInstance().deletePhieuNhap(maPn);
            if (result) {
                JOptionPane.showMessageDialog(this, "Đã xóa phiếu nhập!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa phiếu nhập!");
            }
        }
    }

    public static void main(String[] args) {
        TaiKhoanDTO fakeUser = new TaiKhoanDTO(
                3, "nhanvien1", "123456", "Nguyen Van A", "0123456789", "nhanvien", 1);
        TaiKhoanBUS.setCurrentUser(fakeUser);

        TaiKhoanBUS taiKhoanBUS = TaiKhoanBUS.getInstance();
        NhanVienBUS nhanVienBUS = NhanVienBUS.getInstance();
        taiKhoanBUS.setNhanVienBUS(nhanVienBUS);
        nhanVienBUS.setTaiKhoanBUS(taiKhoanBUS);

        SwingUtilities.invokeLater(() -> new PhieuNhapForm().setVisible(true));
    }
}

//nhanvienBUS,taikhoanBUS,Nhanvienbus,taikhoanDTO
