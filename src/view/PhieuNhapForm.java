package view;

import DAO.PhieuNhapDAO;
import DAO.ChiTietPnDAO;
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
    private JLabel lblChiTiet;

    public PhieuNhapForm() {
        setTitle("Quản lý Phiếu Nhập");
        setSize(1350, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== PANEL TIÊU ĐỀ =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel lblTitle = new JLabel("QUẢN LÝ PHIẾU NHẬP");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // ===== THANH CHỨC NĂNG =====
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        txtMaPn = new JTextField(6);
        txtMaNv = new JTextField(6);
        txtDiaDiem = new JTextField(10);
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        panelControl.add(new JLabel("Mã PN:"));
        panelControl.add(txtMaPn);
        panelControl.add(new JLabel("Mã NV:"));
        panelControl.add(txtMaNv);
        panelControl.add(new JLabel("Địa điểm:"));
        panelControl.add(txtDiaDiem);
        panelControl.add(btnThem);
        panelControl.add(btnSua);
        panelControl.add(btnXoa);
        panelControl.add(btnLamMoi);

        add(panelControl, BorderLayout.BEFORE_FIRST_LINE);

        // ===== PHẦN TRÊN: DANH SÁCH PHIẾU NHẬP =====
        modelPhieuNhap = new DefaultTableModel(
                new Object[]{"Mã PN", "Mã NV", "Thời gian nhập", "Địa điểm", "Trạng thái"}, 0);
        tblPhieuNhap = new JTable(modelPhieuNhap);
        JScrollPane scrollPn = new JScrollPane(tblPhieuNhap);
        scrollPn.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));
        add(scrollPn, BorderLayout.CENTER);

        // ===== PHẦN DƯỚI: CHI TIẾT PHIẾU NHẬP =====
        modelChiTiet = new DefaultTableModel(
                new Object[]{"Mã PN", "Mã Lô hàng", "Đơn giá", "Số lượng"}, 0);
        tblChiTiet = new JTable(modelChiTiet);
        JScrollPane scrollCt = new JScrollPane(tblChiTiet);
        scrollCt.setPreferredSize(new Dimension(1300, 200));
        lblChiTiet = new JLabel("Chi tiết phiếu nhập:");
        lblChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(lblChiTiet, BorderLayout.NORTH);
        bottomPanel.add(scrollCt, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== NẠP DỮ LIỆU BAN ĐẦU =====
        loadPhieuNhapData();

        // ===== SỰ KIỆN =====
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

    // ===== LOAD DỮ LIỆU =====
    private void loadPhieuNhapData() {
        modelPhieuNhap.setRowCount(0);
        ArrayList<PhieuNhapDTO> list = PhieuNhapDAO.getInstance().selectAll();
        for (PhieuNhapDTO pn : list) {
            modelPhieuNhap.addRow(new Object[]{
                    pn.getMaPn(), pn.getMaNv(), pn.getThoiGianNhap(), pn.getDiaDiem(), pn.getTrangThai()
            });
        }
    }

    private void loadChiTietData(int maPn) {
        modelChiTiet.setRowCount(0);
        ArrayList<ChiTietPnDTO> list = ChiTietPnDAO.getInstance().selectAllByMaPn(maPn);
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

    // ===== CHỨC NĂNG =====
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
            int result = PhieuNhapDAO.getInstance().insert(pn);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm! Có thể mã PN đã tồn tại!");
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
            int result = PhieuNhapDAO.getInstance().update(pn);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật!");
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
            try {
                int result = PhieuNhapDAO.getInstance().deleteById(String.valueOf(maPn));
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Đã xóa phiếu nhập!");
                    lamMoiForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa phiếu nhập!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
            }
        }
    }

    // ===== MAIN TEST =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PhieuNhapForm().setVisible(true));
    }
}
