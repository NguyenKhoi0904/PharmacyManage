package test;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import DTO.ThuocDTO;
import BUS.BUSManager;

public class ThuocGUI extends JFrame {
    private JTable tblThuoc;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnRefresh;

    public ThuocGUI() {
        setTitle("🩺 Quản lý thuốc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // ===== TITLE =====
        JLabel title = new JLabel("DANH SÁCH THUỐC", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columnNames = {
                "Mã thuốc", "Tên thuốc", "Giá (VND)", "Đơn vị tính",
                "Mã danh mục", "Nhà sản xuất", "Xuất xứ", "Hình ảnh"
        };

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Không cho chỉnh trực tiếp
            }
        };

        tblThuoc = new JTable(model);
        tblThuoc.setRowHeight(80);
        tblThuoc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblThuoc.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Renderer để hiển thị ảnh
        tblThuoc.getColumnModel().getColumn(7).setCellRenderer(new ImageRenderer());

        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(Color.WHITE);

        btnThem = new JButton("➕ Thêm");
        btnSua = new JButton("✏️ Sửa");
        btnXoa = new JButton("🗑 Xóa");
        btnRefresh = new JButton("🔄 Làm mới");

        for (JButton btn : new JButton[] { btnThem, btnSua, btnXoa, btnRefresh }) {
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(0, 120, 215));
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(130, 40));
            pnlButtons.add(btn);
        }

        add(pnlButtons, BorderLayout.SOUTH);

        // ===== LOAD DATA =====
        loadData();

        // ===== EVENTS =====
        btnThem.addActionListener(e -> themThuoc());

        btnSua.addActionListener(e -> suaThuoc());
        btnXoa.addActionListener(e -> xoaThuoc());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        model.setRowCount(0);
        List<ThuocDTO> list = BUSManager.thuocBUS.getListThuoc();
        if (list == null)
            return;

        for (ThuocDTO t : list) {
            ImageIcon icon = null;
            if (t.getUrlAnh() != null && !t.getUrlAnh().isEmpty()) {
                Image img = new ImageIcon(t.getUrlAnh()).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }
            Object[] row = {
                    t.getMaThuoc(),
                    t.getTenThuoc(),
                    t.getGia(),
                    t.getDonViTinh(),
                    t.getMaDmt(),
                    t.getNhaSanXuat(),
                    t.getXuatXu(),
                    icon
            };
            model.addRow(row);
        }
    }

    private void themThuoc() {
        ThemThuocDialog dialog = new ThemThuocDialog(this);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            BUSManager.thuocBUS.addThuoc(dialog.getThuoc());
            loadData();
        }
    }

    private void suaThuoc() {
        int viewRow = tblThuoc.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 thuốc để sửa!");
            return;
        }

        int modelRow = tblThuoc.convertRowIndexToModel(viewRow);
        int maThuoc = (int) model.getValueAt(modelRow, 0);
        ThuocDTO thuoc = BUSManager.thuocBUS.getThuocByMaThuoc(maThuoc);

        if (thuoc == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thuốc này!");
            return;
        }

        SuaThuocDialog dialog = new SuaThuocDialog(this, thuoc);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            BUSManager.thuocBUS.updateThuoc(dialog.getThuoc());
            loadData();
        }
    }

    private void xoaThuoc() {
        int viewRow = tblThuoc.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn 1 thuốc để xoá!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá thuốc này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        int modelRow = tblThuoc.convertRowIndexToModel(viewRow);
        int maThuoc = (Integer) model.getValueAt(modelRow, 0);

        if (BUSManager.thuocBUS.deleteThuoc(maThuoc)) {
            JOptionPane.showMessageDialog(this, "Đã xoá thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể xoá!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThuocGUI().setVisible(true));
    }

    // Renderer để hiển thị ảnh trong bảng
    private static class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            if (value instanceof ImageIcon icon) {
                label.setIcon(icon);
            } else {
                label.setText("Không có ảnh");
            }
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setOpaque(true);
            }
            return label;
        }
    }
}
