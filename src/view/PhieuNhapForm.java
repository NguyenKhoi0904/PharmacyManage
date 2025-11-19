package view;

import BUS.LoHangBUS;
import BUS.NhanVienBUS;
import BUS.BUSManager;
import BUS.ChiTietPnBUS;
import BUS.PhieuNhapBUS;
import DTO.PhieuNhapDTO;
import DTO.ChiTietPnDTO;
import view.Dialog.SuaPhieuNhapDialog;
import view.Dialog.ThemPhieuNhapDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PhieuNhapForm extends JFrame {
    private JTable tblPhieuNhap;
    private JTable tblChiTiet;
    private DefaultTableModel modelPhieuNhap;
    private DefaultTableModel modelChiTiet;

    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    // private JTextField txtMaPn, txtDiaDiem;

    public PhieuNhapForm() {
        setTitle("Quản Lý Phiếu Nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 800);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 255, 255));
        JLabel lblTitle = new JLabel("QUẢN LÝ PHIẾU NHẬP");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

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
        // topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelPhieuNhap = new DefaultTableModel(
                new Object[] { "Mã PN", "Mã NV", "Thời gian nhập", "Địa điểm" }, 0);
        tblPhieuNhap = new JTable(modelPhieuNhap);
        tblPhieuNhap.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblPhieuNhap.setRowHeight(28);
        JScrollPane scrollPn = new JScrollPane(tblPhieuNhap);
        scrollPn.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));

        modelChiTiet = new DefaultTableModel(
                new Object[] { "Mã PN", "Mã Lô hàng", "Đơn giá", "Số lượng" }, 0);
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblChiTiet.setRowHeight(28);
        JScrollPane scrollCt = new JScrollPane(tblChiTiet);
        scrollCt.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        tablePanel.add(scrollPn);
        tablePanel.add(scrollCt);
        add(tablePanel, BorderLayout.CENTER);

        PhieuNhapBUS pnBUS = PhieuNhapBUS.getInstance();
        ChiTietPnBUS ctBUS = ChiTietPnBUS.getInstance();
        pnBUS.setChiTietPnBUS(ctBUS);
        pnBUS.setLoHangBUS(LoHangBUS.getInstance());
        pnBUS.setNhanVienBUS(NhanVienBUS.getInstance());
        ctBUS.setLoHangBUS(LoHangBUS.getInstance());

        loadPhieuNhapData();

        tblPhieuNhap.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuNhap.getSelectedRow();
                if (row >= 0) {
                    int maPn = (int) modelPhieuNhap.getValueAt(row, 0);
                    // txtMaPn.setText(String.valueOf(maPn));
                    // txtDiaDiem.setText(String.valueOf(modelPhieuNhap.getValueAt(row, 3)));
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

    public void loadPhieuNhapData() {
        modelPhieuNhap.setRowCount(0);
        ArrayList<PhieuNhapDTO> list = BUSManager.phieuNhapBUS.getListPhieuNhap();
        for (PhieuNhapDTO pn : list) {
            modelPhieuNhap.addRow(new Object[] {
                    pn.getMaPn(), pn.getMaNv(), pn.getThoiGianNhap(), pn.getDiaDiem()
            });
        }
    }

    private void loadChiTietData(int maPn) {
        modelChiTiet.setRowCount(0);
        ArrayList<ChiTietPnDTO> list = ChiTietPnBUS.getInstance().getListChiTietPnByMaPn(maPn);
        for (ChiTietPnDTO ct : list) {
            modelChiTiet.addRow(new Object[] {
                    ct.getMaPn(), ct.getMaLh(), ct.getDonGia(), ct.getSoLuong()
            });
        }
    }

    private void lamMoiForm() {
        // txtMaPn.setText("");
        // txtDiaDiem.setText("");
        modelChiTiet.setRowCount(0);
        loadPhieuNhapData();
    }

    private void themPhieuNhap() {
        ThemPhieuNhapDialog dialog = new ThemPhieuNhapDialog(this);
        dialog.setVisible(true);
    }

    private void suaPhieuNhap() {
        int row = tblPhieuNhap.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần sửa!");
            return;
        }
        int modelRow = tblPhieuNhap.convertRowIndexToModel(row);
        int maPn = (Integer) modelPhieuNhap.getValueAt(modelRow, 0);
        SuaPhieuNhapDialog dialog = new SuaPhieuNhapDialog(this, BUSManager.phieuNhapBUS.getPhieuNhapByMaPn(maPn));
        dialog.setVisible(true);
    }

    private void xoaPhieuNhap() {

        int rowCT = tblChiTiet.getSelectedRow();
        int rowPN = tblPhieuNhap.getSelectedRow();

        // --- TH1: Không chọn gì ---
        if (rowCT < 0 && rowPN < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xoá!");
            return;
        }

        // --- TH2: Ưu tiên xoá chi tiết phiếu nhập ---
        if (rowCT >= 0) {
            int maPn = (int) modelChiTiet.getValueAt(rowCT, 0);
            int maLh = (int) modelChiTiet.getValueAt(rowCT, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xoá chi tiết: Mã PN " + maPn + " - Lô hàng " + maLh + " ?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // boolean res = ChiTietPnBUS.getInstance().deleteChiTiet(maPn, maLo);
                boolean res = BUSManager.chiTietPnBUS.deleteChiTietPn(maPn, maLh);

                if (res) {
                    JOptionPane.showMessageDialog(this, "Đã xoá chi tiết phiếu nhập!");
                    loadChiTietData(maPn);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xoá chi tiết!");
                }
            }
            return;
        }

        // --- TH3: Nếu không chọn chi tiết → xoá phiếu nhập ---
        if (rowPN >= 0) {
            int maPn = (int) modelPhieuNhap.getValueAt(rowPN, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xoá phiếu nhập " + maPn + " ?\n(bao gồm tất cả chi tiết liên quan)",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean res = BUSManager.phieuNhapBUS.deletePhieuNhap(maPn);

                if (res) {
                    JOptionPane.showMessageDialog(this, "Đã xoá phiếu nhập!");
                    lamMoiForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xoá phiếu nhập!");
                }
            }
        }
    }

}
