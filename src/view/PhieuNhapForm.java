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
        
                // ================= PANEL TIÊU ĐỀ =================
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 255, 255));
        JLabel lblTitle = new JLabel("QUẢN LÝ PHIẾU NHẬP");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

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

        add(panelControl, BorderLayout.NORTH);

        // ===== NẠP DỮ LIỆU BAN ĐẦU =====
        loadPhieuNhapData();

        // ===== SỰ KIỆN =====
        tblPhieuNhap.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuNhap.getSelectedRow();
                if (row >= 0) {
                    int maPn = (int) modelPhieuNhap.getValueAt(row, 0);
                    loadChiTietData(maPn);
                }
            }
        });

        btnLamMoi.addActionListener(e -> loadPhieuNhapData());

        btnThem.addActionListener(e -> themPhieuNhap());
        btnSua.addActionListener(e -> suaPhieuNhap());
        btnXoa.addActionListener(e -> xoaPhieuNhap());
    }

    // ===== HÀM LOAD DỮ LIỆU =====
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

    // ===== CHỨC NĂNG =====
    private void themPhieuNhap() {
        try {
            int maPn = Integer.parseInt(txtMaPn.getText());
            int maNv = Integer.parseInt(txtMaNv.getText());
            String diaDiem = txtDiaDiem.getText();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            PhieuNhapDTO pn = new PhieuNhapDTO(maPn, maNv, now, diaDiem, 1);
            int result = PhieuNhapDAO.getInstance().insert(pn);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!");
                loadPhieuNhapData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu đầu vào!");
        }
    }

    private void suaPhieuNhap() {
        try {
            int row = tblPhieuNhap.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần sửa!");
                return;
            }
            int maPn = (int) modelPhieuNhap.getValueAt(row, 0);
            int maNv = Integer.parseInt(txtMaNv.getText());
            String diaDiem = txtDiaDiem.getText();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            PhieuNhapDTO pn = new PhieuNhapDTO(maPn, maNv, now, diaDiem, 1);
            int result = PhieuNhapDAO.getInstance().update(pn);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadPhieuNhapData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật dữ liệu!");
        }
    }

    private void xoaPhieuNhap() {
        int row = tblPhieuNhap.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần xóa!");
            return;
        }
        int maPn = (int) modelPhieuNhap.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa phiếu nhập " + maPn + " ?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int result = PhieuNhapDAO.getInstance().deleteById(String.valueOf(maPn));
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                loadPhieuNhapData();
            }
        }
    }

    // ===== MAIN TEST =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PhieuNhapForm().setVisible(true);
        });
    }
}
