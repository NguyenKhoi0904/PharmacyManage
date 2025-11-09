package view;

import BUS.LoHangBUS;
import BUS.NhanVienBUS;
import BUS.ChiTietPnBUS;
import BUS.PhieuNhapBUS;
import DTO.PhieuNhapDTO;
import DTO.ChiTietPnDTO;

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
    private JTextField txtMaPn, txtMaNv, txtDiaDiem;

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

        JLabel lblMaNv = new JLabel("Mã NV:");
        lblMaNv.setFont(lblFont);
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(lblMaNv, gbc);

        txtMaNv = new JTextField(15);
        txtMaNv.setFont(txtFont);
        gbc.gridx = 3; gbc.gridy = 0;
        inputPanel.add(txtMaNv, gbc);

        JLabel lblDiaDiem = new JLabel("Địa điểm:");
        lblDiaDiem.setFont(lblFont);
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(lblDiaDiem, gbc);

        txtDiaDiem = new JTextField(35);
        txtDiaDiem.setFont(txtFont);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 3;
        inputPanel.add(txtDiaDiem, gbc);
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

        // --- Gộp form + nút ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ================= VÙNG BẢNG (2 ô song song) =================
        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Bảng DANH SÁCH PHIẾU NHẬP (trái) ---
        modelPhieuNhap = new DefaultTableModel(
                new Object[]{"Mã PN", "Mã NV", "Thời gian nhập", "Địa điểm", "Trạng thái"}, 0);
        tblPhieuNhap = new JTable(modelPhieuNhap);
        tblPhieuNhap.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblPhieuNhap.setRowHeight(28);
        JScrollPane scrollPn = new JScrollPane(tblPhieuNhap);
        scrollPn.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));

        // --- Bảng CHI TIẾT PHIẾU NHẬP (phải) ---
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
                    txtMaNv.setText(String.valueOf(modelPhieuNhap.getValueAt(row, 1)));
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

    // ================= LOAD DỮ LIỆU =================
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
        txtMaNv.setText("");
        txtDiaDiem.setText("");
        modelChiTiet.setRowCount(0);
        loadPhieuNhapData();
    }

    // ================= CHỨC NĂNG =================
    private void themPhieuNhap() {
        try {
            if (txtMaPn.getText().isEmpty() || txtMaNv.getText().isEmpty() || txtDiaDiem.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            int maPn = Integer.parseInt(txtMaPn.getText());
            int maNv = Integer.parseInt(txtMaNv.getText());
            String diaDiem = txtDiaDiem.getText().trim();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            PhieuNhapDTO pn = new PhieuNhapDTO(maPn, maNv, now, diaDiem, 1);
            boolean result = PhieuNhapBUS.getInstance().addPhieuNhap(pn, new ArrayList<>());

            if (result) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm! Có thể mã PN đã tồn tại hoặc lỗi dữ liệu!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã PN và Mã NV phải là số!");
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
            int maNv = Integer.parseInt(txtMaNv.getText());
            String diaDiem = txtDiaDiem.getText().trim();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            PhieuNhapDTO pn = new PhieuNhapDTO(maPn, maNv, now, diaDiem, 1);
            boolean result = PhieuNhapBUS.getInstance().updatePhieuNhap(maPn, pn);

            if (result) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật phiếu nhập!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã PN và Mã NV phải là số!");
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

    // ================= MAIN TEST =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PhieuNhapForm().setVisible(true));
    }
}
