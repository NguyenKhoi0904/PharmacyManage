package view.Dialog;

import java.awt.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import BUS.BUSManager;
import BUS.TaiKhoanBUS;
import DTO.ChiTietPnDTO;
import DTO.PhieuNhapDTO;
import helper.IDGenerator;
import view.PhieuNhapForm;

public class ThemPhieuNhapDialog extends JDialog {
    private int maPn = IDGenerator.generateUniqueID();

    private JTextField txtDiadiem;
    private JLabel lblNhanVien;
    private JTable tblChiTiet;
    private DefaultTableModel model;

    private ArrayList<ChiTietPnDTO> dsChiTiet;
    private Timestamp timestamp;

    public ThemPhieuNhapDialog(PhieuNhapForm parent) {
        super(parent, "Thêm Phiếu Nhập Mới", true);
        setSize(1280, 720);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));
        setResizable(false);

        dsChiTiet = new ArrayList<ChiTietPnDTO>();

        // PANEL CHÍNH
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        Font font = new Font("Segoe UI", Font.PLAIN, 18);

        // ========================
        // PANEL TRÁI – THÔNG TIN PHIẾU NHẬP
        // ========================
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        "Thông tin phiếu nhập",
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 10, 25, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ====== DÒNG 1: Mã phiếu nhập ======
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleMaPn = new JLabel("Mã phiếu nhập:");
        titleMaPn.setFont(font);
        infoPanel.add(titleMaPn, gbc);

        gbc.gridx = 1;
        JLabel lblMaPn = new JLabel(String.valueOf(maPn));
        lblMaPn.setFont(font);
        infoPanel.add(lblMaPn, gbc);

        // ====== DÒNG 2: Nhân viên nhập ======
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel titleNhanVien = new JLabel("Nhân viên nhập:");
        titleNhanVien.setFont(font);
        infoPanel.add(titleNhanVien, gbc);

        gbc.gridx = 1;
        lblNhanVien = new JLabel(TaiKhoanBUS.getCurrentUser().toString());
        lblNhanVien.setFont(font);
        infoPanel.add(lblNhanVien, gbc);

        // ====== DÒNG 3: Thời gian nhập ======
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel titleThoiGianNhap = new JLabel("Thời gian nhập:");
        titleThoiGianNhap.setFont(font);
        infoPanel.add(titleThoiGianNhap, gbc);

        gbc.gridx = 1;
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        timestamp = Timestamp.from(instant);
        JLabel lblThoiGian = new JLabel();
        lblThoiGian.setFont(font);
        infoPanel.add(lblThoiGian, gbc);

        // ====== DÒNG 4: Địa điểm ======
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel titleDiaDiem = new JLabel("Địa điểm:");
        titleDiaDiem.setFont(font);
        infoPanel.add(titleDiaDiem, gbc);

        gbc.gridx = 1;
        txtDiadiem = new JTextField();
        txtDiadiem.setFont(font);
        infoPanel.add(txtDiadiem, gbc);

        mainPanel.add(infoPanel);

        // ========================
        // PANEL PHẢI – CHI TIẾT PHIẾU NHẬP
        // ========================
        JPanel pnRight = new JPanel(new BorderLayout(10, 10));

        pnRight.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        "Chi tiết phiếu nhập",
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 15)));

        model = new DefaultTableModel(new String[] {
                "Mã Phiếu Nhập", "Mã Lô Hàng", "Đơn Giá", "Số Lượng"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tblChiTiet = new JTable(model);
        tblChiTiet.setFont(font);
        tblChiTiet.setRowHeight(28);
        tblChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        pnRight.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        // Nút thêm/xóa/refresh chi tiết
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnButtons.setBackground(Color.WHITE);

        JButton btnThem = new JButton("Thêm");
        JButton btnXoa = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");

        // ===== THÊM CHI TIẾT =====
        btnThem.addActionListener(e -> {
            // Lấy danh sách mã PN và mã lô hàng (bạn tự truyền từ BUS)
            ThemChiTietPhieuNhapDialog dialog = new ThemChiTietPhieuNhapDialog(
                    this,
                    String.valueOf(maPn));

            dialog.setVisible(true);

            ChiTietPnDTO ct = dialog.getResult();

            if (ct != null) {
                dsChiTiet.add(ct); // Thêm vào danh sách chi tiết
                loadTableChiTiet(); // Cập nhật bảng
            }
        });

        // ===== XÓA CHI TIẾT =====
        btnXoa.addActionListener(e -> {
            int row = tblChiTiet.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xoá!");
                return;
            }

            int modelRow = tblChiTiet.convertRowIndexToModel(row);

            int maPn = (Integer) model.getValueAt(modelRow, 0);
            int maLh = (Integer) model.getValueAt(modelRow, 1);

            dsChiTiet.removeIf(ct -> ct.getMaPn() == maPn && ct.getMaLh() == maLh);

            loadTableChiTiet(); // load lại bảng
        });

        // ===== NÚT REFRESH =====
        btnRefresh.addActionListener(e -> loadTableChiTiet());

        pnButtons.add(btnThem);
        pnButtons.add(btnXoa);
        pnButtons.add(btnRefresh);

        pnRight.add(pnButtons, BorderLayout.SOUTH);

        pnButtons.add(btnThem);
        pnButtons.add(btnXoa);

        pnRight.add(pnButtons, BorderLayout.SOUTH);

        mainPanel.add(pnRight);

        // ========================
        // PANEL DƯỚI – NÚT XÁC NHẬN + HỦY
        // ========================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnCancel = new JButton("Hủy");
        JButton btnOK = new JButton("Xác nhận");

        btnCancel.addActionListener(e -> setVisible(false));

        btnOK.addActionListener(e -> {
            if (!validateInput())
                return;

            // thêm phiếu nhập vào db
            int maNv = BUSManager.nhanVienBUS.getNhanVienByMaTk(TaiKhoanBUS.getCurrentUser().getMaTk()).getMaNv();
            if (BUSManager.phieuNhapBUS.addPhieuNhap(new PhieuNhapDTO(maPn, maNv, timestamp, txtDiadiem.getText(), 1),
                    dsChiTiet)) {
                JOptionPane.showMessageDialog(parent, "Thêm Phiếu Nhập Thành Công");
                parent.loadPhieuNhapData();
                setVisible(false);
            }
        });

        btnOK.setBackground(new Color(0, 120, 215));
        btnOK.setForeground(Color.WHITE);
        btnOK.setFont(new Font("Segoe UI", Font.BOLD, 14));

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnOK);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private boolean validateInput() {
        if (txtDiadiem.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập địa điểm!",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public ArrayList<ChiTietPnDTO> getDsChiTiet() {
        return dsChiTiet;
    }

    private void loadTableChiTiet() {
        model.setRowCount(0); // Xóa toàn bộ dữ liệu cũ

        for (ChiTietPnDTO ct : dsChiTiet) {
            model.addRow(new Object[] {
                    ct.getMaPn(),
                    ct.getMaLh(),
                    ct.getDonGia(),
                    ct.getSoLuong()
            });
        }
    }

}
