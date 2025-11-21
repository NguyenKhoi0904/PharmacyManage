/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import BUS.BUSManager;
import BUS.TaiKhoanBUS;

// import static BUS.BUSManager.hoaDonBUS;
import DTO.ChiTietHdDTO;
import DTO.DanhMucThuocDTO;
import DTO.HoaDonDTO;
import DTO.KhachHangDTO;
import DTO.KhuyenMaiDTO;
import DTO.TaiKhoanDTO;
import DTO.ThuocDTO;
import test.NumberOnlyField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.BigDecimalUtils;
import utils.IconUtils;
import utils.ValidationUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author admin
 */
public class HoaDonForm extends javax.swing.JFrame {

        private ThuocDTO selectedThuoc;
        private ArrayList<ChiTietHdDTO> listCTHD = new ArrayList<ChiTietHdDTO>();
        private ChiTietHdDTO selectedCTHD;
        // private ArrayList<KhuyenMaiDTO> listKM;
        private ArrayList<ThuocDTO> listThuoc;
        // private int maNV = 12; // default
        // private int maKH = -1;

        private JTable thuocTable;
        private DefaultTableModel thuocTableModel;
        private JTable cartTable;
        private DefaultTableModel cartTableModel;

        /**
         * Creates new form HoaDonGUI
         */
        public HoaDonForm(int maNV) {
                initComponents();
                cbMaKH.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                KhachHangDTO kh = (KhachHangDTO) cbMaKH.getSelectedItem();

                                // set text cho sđt và tên khách hàng
                                tfTenKH.setText(BUSManager.taiKhoanBUS.getTaiKhoanByMaTk(kh.getMaTk()).getTen());
                                tfSDT.setText(BUSManager.taiKhoanBUS.getTaiKhoanByMaTk(kh.getMaTk()).getSdt());
                        }
                });
                cbMaKM.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                refreshTongTien();
                        }
                });
                btnHuy.addActionListener(e -> {
                        tfSDT.setText("");
                        tfTenKH.setText("");
                        tfTongTien.setText("");
                        tfTienKhachDua.setText("");
                        tfTienThua.setText("");
                });
                tfMaKH.setVisible(false);
                tfMaKM.setVisible(false);
                // this.maNV = maNV;

                // DEBUG ONLY !!!
                // BUSManager.initAllBUS();
                // listKM = BUSManager.khuyenMaiBUS.getListKhuyenMai();
                listThuoc = BUSManager.thuocBUS.getListThuoc();

                setupMainLayout();

                setIcons();
                setupListProduct();
                setupCartProducts();
                setupCBMaKH();
                setupCBMaKM();

                addEventForInvoicePanel();
                addEventForSearching(tfTimKiemThuoc, thuocTable, listThuoc);
                addEventForTFMaKH();
        }

        private void setupMainLayout() {
                Container cp = getContentPane();

                // Remove all components (vì initComponents đã add chúng vào content pane)
                cp.removeAll();

                // Left panel: infoPanel (top) + productsPanel (bottom)
                JPanel leftPanel = new JPanel(new GridLayout(2, 1));
                leftPanel.add(productInfoPanel);
                leftPanel.add(productsPanel);

                // Right panel: cartPanel (top) + invoicePanel (bottom)
                JPanel rightPanel = new JPanel(new GridLayout(2, 1));
                rightPanel.add(cartPanel);
                rightPanel.add(invoicePanel);

                // Sử dụng GridBagLayout để điều khiển tỷ lệ
                GridBagLayout gbl = new GridBagLayout();
                cp.setLayout(gbl);
                GridBagConstraints gbc = new GridBagConstraints();

                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(6, 6, 6, 6); // tùy chỉnh khoảng cách nếu muốn
                gbc.weighty = 1.0; // chiếm toàn chiều cao

                // Cột trái (65%)
                gbc.gridx = 0;
                gbc.weightx = 0.65;
                gbc.weighty = 1.0;
                cp.add(leftPanel, gbc);

                // Cột phải (35%)
                gbc.gridx = 1;
                gbc.weightx = 0.35;
                cp.add(rightPanel, gbc);

                // Cập nhật layout
                cp.revalidate();
                cp.repaint();
        }

        private void setIcons() {
                // IconUtils.setIcon(productIconLabel,
                // "/product-image/placeholder_them_thuoc.jpg", true);
                ImageIcon icon = loadImage("/image/product-image/placeholder_them_thuoc.jpg");
                Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                productIconLabel.setIcon(new ImageIcon(img));
                IconUtils.setIcon(magnifyingGlassLabel, "magnifying-glass.png", true);
                IconUtils.setIcon(refreshLabel, "refresh.png", true);
                IconUtils.setIcon(addButton, "cart.png", true);
                IconUtils.setIcon(deleteButton, "trash.png", true);
        }

        private void setupCBMaKH() {
                cbMaKH.removeAllItems();
                for (KhachHangDTO kh : BUSManager.khachHangBUS.getListKhachHang()) {
                        cbMaKH.addItem(kh);
                }

                cbMaKH.setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value,
                                        int index, boolean isSelected, boolean cellHasFocus) {

                                Component c = super.getListCellRendererComponent(list, value, index, isSelected,
                                                cellHasFocus);

                                if (value instanceof KhachHangDTO) {
                                        KhachHangDTO khDTO = (KhachHangDTO) value;

                                        // Lấy tài khoản tương ứng
                                        TaiKhoanDTO tk = BUSManager.taiKhoanBUS.getTaiKhoanByMaTk(khDTO.getMaTk());

                                        if (tk != null) {
                                                setText("Mã KH: " + khDTO.getMaKh()
                                                                + " - Tên: " + tk.getTen()
                                                                + " - SĐT: " + tk.getSdt()
                                                                + " - Điểm TL: " + khDTO.getDiemTichLuy());
                                        } else {
                                                setText("Mã KH: " + khDTO.getMaKh() + " (Dữ liệu tài khoản thiếu)");
                                        }
                                }

                                return c;
                        }
                });
        }

        private void setupCBMaKM() {
                cbMaKM.removeAllItems();
                for (KhuyenMaiDTO km : BUSManager.khuyenMaiBUS.getListKhuyenMai()) {
                        cbMaKM.addItem(km);
                }
        }

        private JTable createThuocTable() {
                String[] columnNames = {
                                "Mã thuốc", "Tên thuốc", "Danh mục", "Xuất xứ",
                                "Đơn vị tính", "Nhà sản xuất", "Đơn giá"
                };

                thuocTableModel = new DefaultTableModel(columnNames, 0); // tạo model rỗng
                thuocTable = new JTable(thuocTableModel);
                thuocTable.setFillsViewportHeight(true);
                thuocTable.setRowHeight(28);
                thuocTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                thuocTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
                thuocTable.getTableHeader().setBackground(new Color(240, 240, 240));
                thuocTable.getTableHeader().setForeground(Color.BLACK);

                // Căn giữa, căn phải
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                thuocTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
                thuocTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
                thuocTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);

                // Hiệu ứng nền xen kẽ
                thuocTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                        boolean hasFocus, int row, int column) {
                                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row,
                                                column);
                                if (!isSelected) {
                                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                                }
                                return c;
                        }
                });

                // Sự kiện click
                thuocTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                int selectedRowIndex = thuocTable.getSelectedRow();
                                if (selectedRowIndex != -1) {
                                        int selectedThuocID = (int) thuocTable.getValueAt(selectedRowIndex, 0);
                                        selectedThuoc = BUSManager.thuocBUS.getThuocByMaThuoc(selectedThuocID);

                                        // Cập nhật UI chi tiết
                                        tfMaThuoc.setText(String.valueOf(selectedThuoc.getMaThuoc()));
                                        tfTenThuoc.setText(selectedThuoc.getTenThuoc());
                                        tfDonGia.setText(String.format("%,.0f", selectedThuoc.getGia()));
                                        ImageIcon icon = loadImage(selectedThuoc.getUrlAnh());
                                        Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                                        productIconLabel.setIcon(new ImageIcon(img));
                                        // IconUtils.setIcon(productIconLabel, selectedThuoc.getUrlAnh(), true);
                                }
                        }
                });

                return thuocTable;
        }

        // goi khi du lieu thay doi tren DB
        private void loadThuocData() {
                ArrayList<ThuocDTO> listThuoc = BUSManager.thuocBUS.getListThuoc();

                // Xóa dữ liệu cũ
                thuocTableModel.setRowCount(0);

                for (ThuocDTO thuoc : listThuoc) {
                        DanhMucThuocDTO dmt = BUSManager.danhMucThuocBUS.getDmtByMaDmt(thuoc.getMaDmt());
                        String tenDanhMuc = (dmt != null) ? dmt.getTenDmt() : "Không xác định";

                        Object[] row = {
                                        thuoc.getMaThuoc(),
                                        thuoc.getTenThuoc(),
                                        tenDanhMuc,
                                        thuoc.getXuatXu(),
                                        thuoc.getDonViTinh(),
                                        thuoc.getNhaSanXuat(),
                                        String.format("%,.0f", thuoc.getGia())
                        };
                        thuocTableModel.addRow(row);
                }
        }

        private void setupListProduct() {
                JTable table = createThuocTable(); // tạo bảng
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                scrollPane.getViewport().setBackground(Color.WHITE);

                listProductPanel.setLayout(new BorderLayout());
                listProductPanel.add(scrollPane, BorderLayout.CENTER);

                // Lần đầu load dữ liệu
                loadThuocData();
        }

        private JTable createCartTable() {
                String[] columnNames = { "STT", "Tên thuốc", "Số lượng", "Đơn giá" };
                cartTableModel = new DefaultTableModel(columnNames, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false; // Không cho chỉnh trực tiếp
                        }
                };

                cartTable = new JTable(cartTableModel);
                cartTable.setFillsViewportHeight(true);
                cartTable.setRowHeight(28);
                cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
                cartTable.getTableHeader().setBackground(new Color(240, 240, 240));
                cartTable.getTableHeader().setForeground(Color.BLACK);
                cartTable.setGridColor(new Color(230, 230, 230));
                cartTable.setIntercellSpacing(new Dimension(0, 1));
                cartTable.setShowHorizontalLines(true);
                cartTable.setShowVerticalLines(false);

                // Renderer căn giữa và căn phải
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                cartTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
                cartTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
                cartTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

                // Hiệu ứng nền xen kẽ
                cartTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                        boolean hasFocus, int row, int column) {
                                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row,
                                                column);
                                if (!isSelected) {
                                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                                }
                                return c;
                        }
                });

                // Sự kiện click
                cartTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                int selectedRowIndex = cartTable.getSelectedRow();
                                if (selectedRowIndex != -1) {
                                        selectedCTHD = listCTHD.get(selectedRowIndex);
                                }
                        }
                });

                return cartTable;
        }

        private void loadCartData() {
                // Xóa dữ liệu cũ
                cartTableModel.setRowCount(0);
                int stt = 1;
                // Thêm dữ liệu mới
                for (ChiTietHdDTO ct : listCTHD) {
                        ThuocDTO thuoc = BUSManager.thuocBUS.getThuocByMaThuoc(ct.getMaThuoc());
                        Object[] row = {
                                        stt,
                                        thuoc.getTenThuoc(),
                                        ct.getSoLuong(),
                                        String.format("%,.0f", thuoc.getGia())
                        };
                        cartTableModel.addRow(row);
                        stt++;
                }
        }

        private void setupCartProducts() {
                cartTable = createCartTable(); // Tạo bảng
                if (listCTHD != null)
                        loadCartData(); // Nạp dữ liệu ban đầu

                JScrollPane scrollPane = new JScrollPane(cartTable);
                scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
                scrollPane.getViewport().setBackground(Color.WHITE);

                cartProductsPanel.setLayout(new BorderLayout());
                cartProductsPanel.add(scrollPane, BorderLayout.CENTER);
        }

        private BigDecimal getCartSum() {
                BigDecimal sum = BigDecimal.ZERO; // Khởi tạo giá trị 0 dạng BigDecimal

                if (!listCTHD.isEmpty()) {
                        for (ChiTietHdDTO cthd : listCTHD) {
                                BigDecimal soLuong = BigDecimal.valueOf(cthd.getSoLuong()); // ép kiểu từ int →
                                                                                            // BigDecimal
                                BigDecimal thanhTien = cthd.getDonGia().multiply(soLuong); // đơn giá * số lượng
                                sum = sum.add(thanhTien); // cộng dồn
                        }
                }

                return sum;
        }

        /**
         * Lấy giá trị giảm (%) từ mã khuyến mãi
         * 
         * @return Giá trị giảm từ 0 đến 1 (vd: 10% -> 0.1). Trả về BigDecimal.ZERO nếu
         *         mã không hợp lệ.
         */

        private void addEventForInvoicePanel() {
                // Ma KM
                tfMaKM.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                                refreshTongTien();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                                refreshTongTien();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                                refreshTongTien();
                        }
                });

                // Tien Khach Dua
                tfTienKhachDua.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                                refreshTienThua(BigDecimalUtils.toBigDecimal(tfTongTien.getText()));
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                                refreshTienThua(BigDecimalUtils.toBigDecimal(tfTongTien.getText()));
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                                refreshTienThua(BigDecimalUtils.toBigDecimal(tfTongTien.getText()));
                        }
                });

        }

        private void addEventForSearching(JTextField txtSearch, JTable thuocTable, ArrayList<ThuocDTO> originalList) {
                // Lắng nghe khi nội dung trong ô tìm kiếm thay đổi
                txtSearch.getDocument().addDocumentListener(new DocumentListener() {
                        private void search() {
                                String keyword = txtSearch.getText().trim().toLowerCase();

                                // Nếu không nhập gì thì hiển thị toàn bộ danh sách
                                if (keyword.isEmpty()) {
                                        updateThuocTable(originalList, thuocTable);
                                        return;
                                }

                                // Lọc danh sách theo từ khóa (ví dụ tìm trong tên hoặc mã thuốc)
                                ArrayList<ThuocDTO> filteredList = new ArrayList<>();
                                for (ThuocDTO thuoc : originalList) {
                                        if (thuoc.getTenThuoc().toLowerCase().contains(keyword) ||
                                                        String.valueOf(thuoc.getMaThuoc()).contains(keyword)) {
                                                filteredList.add(thuoc);
                                        }
                                }

                                // Cập nhật lại bảng
                                updateThuocTable(filteredList, thuocTable);
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                                search();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                                search();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                                search();
                        }
                });
        }

        private void updateThuocTable(ArrayList<ThuocDTO> list, JTable thuocTable) {
                DefaultTableModel model = (DefaultTableModel) thuocTable.getModel();
                model.setRowCount(0); // Xóa hết dữ liệu cũ
                for (ThuocDTO thuoc : list) {
                        DanhMucThuocDTO dmt = BUSManager.danhMucThuocBUS.getDmtByMaDmt(thuoc.getMaDmt());

                        model.addRow(new Object[] {
                                        thuoc.getMaThuoc(),
                                        thuoc.getTenThuoc(),
                                        dmt.getTenDmt(),
                                        thuoc.getXuatXu(),
                                        thuoc.getDonViTinh(),
                                        thuoc.getNhaSanXuat(),
                                        thuoc.getGia()
                        });
                }
        }

        private void refreshTongTien() {
                // Lấy tổng tiền hiện tại của giỏ hàng
                BigDecimal tongTienHienTai = getCartSum();
                BigDecimal tongTienMoi = tongTienHienTai;
                BigDecimal giaTriKM = BigDecimal.ZERO;
                try {

                        // Ma KM trong UI la so thi parse
                        // if (ValidationUtils.isValidIntBiggerThanZero(tfMaKM.getText())) {
                        // int maKM = Integer.parseInt(tfMaKM.getText());
                        // KhuyenMaiDTO km = BUSManager.khuyenMaiBUS.getKhuyenMaiByMaKm(maKM);
                        KhuyenMaiDTO km = (KhuyenMaiDTO) cbMaKM.getSelectedItem();

                        if (km != null & BUSManager.khuyenMaiBUS.isKMValid(km)) {
//                            if (km.getELoaiKM() == KhuyenMaiDTO.LOAI_KM.PHAN_TRAM)
                            {
                                // Lấy mã KM và tính giảm giá nếu có
                                giaTriKM = km.getGiaTriKm();
                                // tongTienMoi =
                                // tongTienHienTai.subtract(tongTienHienTai.multiply(phanTramGiam));
                                // Đảm bảo chia an toàn bằng cách sử dụng RoundingMode
                                tongTienMoi = tongTienHienTai.subtract(
                                                tongTienHienTai.multiply(giaTriKM).divide(new BigDecimal("100"), 2,
                                                                RoundingMode.HALF_UP));
                            }
                                
                        }
                        // }

                        tfTongTien.setText(tongTienMoi.toString());

                        // Đồng thời refresh luôn tiền thừa
                        refreshTienThua(tongTienMoi);

                } catch (NumberFormatException e) {

                }

        }

        private void refreshTienThua(BigDecimal tongTien) {
                BigDecimal tienKhachDua = BigDecimalUtils.toBigDecimal(tfTienKhachDua.getText(), BigDecimal.ZERO);
                BigDecimal tienThuaMoi = tienKhachDua.subtract(tongTien);
                tfTienThua.setText(tienThuaMoi.toString());
        }

        private void addEventForTFMaKH() {
                // Ma KM
                tfMaKH.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                                refreshInfoKH();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                                refreshInfoKH();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                                refreshInfoKH();
                        }
                });
        }

        // Tam thoi chua co field tenKH
        private void refreshInfoKH() {
                if (ValidationUtils.isValidInt(tfMaKH.getText())) {
                        int maKH = Integer.parseInt(tfMaKH.getText());
                        KhachHangDTO kh = BUSManager.khachHangBUS.getKhachHangByMaKh(maKH);
                        if (kh != null) {
                                TaiKhoanDTO tk = BUSManager.taiKhoanBUS.getTaiKhoanByMaTk(kh.getMaTk());
                                tfTenKH.setText(tk.getTen() + "");
                                tfSDT.setText(tk.getSdt());
                                // this.maKH = kh.getMaKh();
                        } else {
                                tfTenKH.setText("");
                                tfSDT.setText("");
                                // this.maKH = -1;
                        }

                }
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                productInfoPanel = new javax.swing.JPanel();
                productIconLabel = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                lblMaThuoc = new javax.swing.JLabel();
                lblThanhPhan = new javax.swing.JLabel();
                lblDonGia = new javax.swing.JLabel();
                lblTenThuoc = new javax.swing.JLabel();
                tfTenThuoc = new javax.swing.JTextField();
                tfMaThuoc = new javax.swing.JTextField();
                tfDonGia = new javax.swing.JTextField();
                jScrollPane1 = new javax.swing.JScrollPane();
                taThanPhan = new javax.swing.JTextArea();
                productsPanel = new javax.swing.JPanel();
                actionPanel = new javax.swing.JPanel();
                jComboBox1 = new javax.swing.JComboBox<>();
                jPanel1 = new javax.swing.JPanel();
                magnifyingGlassLabel = new javax.swing.JLabel();
                tfTimKiemThuoc = new javax.swing.JTextField();
                refreshLabel = new javax.swing.JLabel();
                jPanel2 = new javax.swing.JPanel();
                jTextField6 = new javax.swing.JTextField();
                jPanel3 = new javax.swing.JPanel();
                addButton = new javax.swing.JButton();
                listProductPanel = new javax.swing.JPanel();
                cartPanel = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                cartProductsPanel = new javax.swing.JPanel();
                deleteButton = new javax.swing.JButton();
                invoicePanel = new javax.swing.JPanel();
                jLabel4 = new javax.swing.JLabel();
                jLabel10 = new javax.swing.JLabel();
                tfSDT = new javax.swing.JTextField();
                jLabel11 = new javax.swing.JLabel();
                tfTenKH = new javax.swing.JTextField();
                jLabel12 = new javax.swing.JLabel();
                tfTongTien = new javax.swing.JTextField();
                jLabel13 = new javax.swing.JLabel();
                tfTienKhachDua = new NumberOnlyField();
                jLabel14 = new javax.swing.JLabel();
                tfTienThua = new javax.swing.JTextField();
                cbGender = new javax.swing.JComboBox<>();
                btnIn = new javax.swing.JButton();
                btnHuy = new javax.swing.JButton();
                tfMaKM = new javax.swing.JTextField();
                jLabel15 = new javax.swing.JLabel();
                paymentComboBox = new javax.swing.JComboBox<>();
                tfMaKH = new javax.swing.JTextField();
                cbMaKH = new javax.swing.JComboBox<KhachHangDTO>();
                cbMaKM = new javax.swing.JComboBox<KhuyenMaiDTO>();
                // tfDanhMuc = new javax.swing.JTextField();

                // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setSize(new java.awt.Dimension(1350, 800));

                productInfoPanel.setMaximumSize(new java.awt.Dimension(32767, 309));
                productInfoPanel.setName(""); // NOI18N

                productIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                productIconLabel.setText("img");
                productIconLabel.setToolTipText("");
                productIconLabel.setMaximumSize(new java.awt.Dimension(300, 300));
                productIconLabel.setPreferredSize(new java.awt.Dimension(250, 250));

                jLabel2.setBackground(new java.awt.Color(0, 255, 255));
                jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabel2.setForeground(new java.awt.Color(255, 255, 255));
                jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel2.setText("THÔNG TIN THUỐC");
                jLabel2.setOpaque(true);

                lblMaThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                lblMaThuoc.setText("Mã thuốc:");

                lblThanhPhan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                lblThanhPhan.setText("Thành phần:");

                lblDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                lblDonGia.setText("Đơn giá");

                lblTenThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                lblTenThuoc.setText("Tên thuốc:");

                tfTenThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                tfTenThuoc.setEnabled(false);

                tfMaThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                tfMaThuoc.setEnabled(false);

                tfDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                tfDonGia.setEnabled(false);

                taThanPhan.setColumns(20);
                taThanPhan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                taThanPhan.setRows(5);
                taThanPhan.setEnabled(false);
                jScrollPane1.setViewportView(taThanPhan);

                javax.swing.GroupLayout productInfoPanelLayout = new javax.swing.GroupLayout(productInfoPanel);
                productInfoPanel.setLayout(productInfoPanelLayout);
                productInfoPanelLayout.setHorizontalGroup(
                                productInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(productInfoPanelLayout.createSequentialGroup()
                                                                .addGap(17, 17, 17)
                                                                .addComponent(productIconLabel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                205,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(32, 32, 32)
                                                                .addGroup(productInfoPanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addGroup(productInfoPanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblMaThuoc,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                90,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(tfMaThuoc,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                120,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(productInfoPanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblTenThuoc,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                90,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(tfTenThuoc,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                315,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(productInfoPanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(productInfoPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lblThanhPhan,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                90,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(lblDonGia,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                90,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(productInfoPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jScrollPane1)
                                                                                                                .addGroup(productInfoPanelLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(tfDonGia,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                162,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                                                .addContainerGap(67, Short.MAX_VALUE))
                                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE));
                productInfoPanelLayout.setVerticalGroup(
                                productInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(productInfoPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                28,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(productInfoPanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(productInfoPanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(productInfoPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(lblMaThuoc,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                30,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(tfMaThuoc,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                30,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addGroup(productInfoPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(lblTenThuoc,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                30,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(tfTenThuoc,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                30,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addGap(11, 11, 11)
                                                                                                .addGroup(productInfoPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lblThanhPhan,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                30,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(jScrollPane1,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(productInfoPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(lblDonGia,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                30,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(tfDonGia,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                30,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                productInfoPanelLayout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(12, 12, 12)
                                                                                                                .addComponent(productIconLabel,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                231,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(38, Short.MAX_VALUE)));

                jComboBox1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
                jComboBox1.setModel(
                                new javax.swing.DefaultComboBoxModel<>(
                                                new String[] { "Tất cả", "A - Z", "Z - A", "Giá tăng" }));
                jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBox1ActionPerformed(evt);
                        }
                });

                jPanel1.setBackground(new java.awt.Color(255, 255, 255));
                jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

                magnifyingGlassLabel.setText("jLabel8");

                tfTimKiemThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                tfTimKiemThuoc.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(magnifyingGlassLabel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                31,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tfTimKiemThuoc,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                224,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(magnifyingGlassLabel,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                22,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(tfTimKiemThuoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                refreshLabel.setText("jLabel8");
                refreshLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                refreshLabelMouseClicked(evt);
                        }
                });

                jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                jTextField6.setText("Số lượng");
                jTextField6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

                jPanel3.setBackground(new java.awt.Color(0, 255, 0));
                jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

                addButton.setText("jButton3");
                addButton.setMinimumSize(new java.awt.Dimension(23, 23));
                addButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                addButtonActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(addButton,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(addButton,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                24, Short.MAX_VALUE)
                                                                .addContainerGap()));

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jTextField6,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                90, Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextField6)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

                javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
                actionPanel.setLayout(actionPanelLayout);
                actionPanelLayout.setHorizontalGroup(
                                actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(actionPanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jComboBox1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                115,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(refreshLabel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                30,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(70, 70, 70)
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                actionPanelLayout.setVerticalGroup(
                                actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(actionPanelLayout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addGroup(actionPanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(jPanel2,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jComboBox1)))
                                                .addGroup(actionPanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(refreshLabel,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));

                javax.swing.GroupLayout listProductPanelLayout = new javax.swing.GroupLayout(listProductPanel);
                listProductPanel.setLayout(listProductPanelLayout);
                listProductPanelLayout.setHorizontalGroup(
                                listProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));
                listProductPanelLayout.setVerticalGroup(
                                listProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));

                javax.swing.GroupLayout productsPanelLayout = new javax.swing.GroupLayout(productsPanel);
                productsPanel.setLayout(productsPanelLayout);
                productsPanelLayout.setHorizontalGroup(
                                productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(productsPanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(actionPanel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 21, Short.MAX_VALUE))
                                                .addGroup(productsPanelLayout.createSequentialGroup()
                                                                .addComponent(listProductPanel,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));
                productsPanelLayout.setVerticalGroup(
                                productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(productsPanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(actionPanel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(listProductPanel,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));

                cartPanel.setMaximumSize(new java.awt.Dimension(32767, 309));

                jLabel3.setBackground(new java.awt.Color(0, 255, 255));
                jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabel3.setForeground(new java.awt.Color(255, 255, 255));
                jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel3.setText("GIỎ HÀNG");
                jLabel3.setOpaque(true);

                javax.swing.GroupLayout cartProductsPanelLayout = new javax.swing.GroupLayout(cartProductsPanel);
                cartProductsPanel.setLayout(cartProductsPanelLayout);
                cartProductsPanelLayout.setHorizontalGroup(
                                cartProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));
                cartProductsPanelLayout.setVerticalGroup(
                                cartProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));

                deleteButton.setText("jButton3");
                deleteButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                deleteButtonActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout cartPanelLayout = new javax.swing.GroupLayout(cartPanel);
                cartPanel.setLayout(cartPanelLayout);
                cartPanelLayout.setHorizontalGroup(
                                cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cartPanelLayout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(deleteButton,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                60,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(11, 11, 11))
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(cartPanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cartProductsPanel,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));
                cartPanelLayout.setVerticalGroup(
                                cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(cartPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                28,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cartProductsPanel,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(deleteButton,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                36,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                jLabel4.setBackground(new java.awt.Color(0, 255, 255));
                jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabel4.setForeground(new java.awt.Color(255, 255, 255));
                jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel4.setText("HÓA ĐƠN");
                jLabel4.setOpaque(true);

                jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel10.setText("Số điện thoại:");

                tfSDT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tfSDT.setEnabled(false);

                jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel11.setText("Tên Khách:");

                tfTenKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tfTenKH.setEnabled(false);

                jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel12.setText("Tổng tiền:");

                tfTongTien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tfTongTien.setEnabled(false);

                jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel13.setText("Tiền khách đưa:");

                tfTienKhachDua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

                jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel14.setText("Tiền thừa:");

                tfTienThua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tfTienThua.setEnabled(false);

                cbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));
                cbGender.setEnabled(false);

                btnIn.setBackground(new java.awt.Color(51, 255, 0));
                btnIn.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                btnIn.setForeground(new java.awt.Color(255, 255, 255));
                btnIn.setText("IN HÓA ĐƠN");
                btnIn.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnInActionPerformed(evt);
                        }
                });

                btnHuy.setBackground(new java.awt.Color(255, 0, 0));
                btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                btnHuy.setForeground(new java.awt.Color(255, 255, 255));
                btnHuy.setText("HỦY BỎ");

                tfMaKM.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tfMaKM.setText("Mã KM");
                tfMaKM.setEnabled(false);

                jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel15.setText("Phương thức thanh toán:");

                paymentComboBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                paymentComboBox.setModel(
                                new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản" }));

                tfMaKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tfMaKH.setText("Mã KH");
                tfMaKH.setEnabled(false);

                javax.swing.GroupLayout invoicePanelLayout = new javax.swing.GroupLayout(invoicePanel);
                invoicePanel.setLayout(invoicePanelLayout);
                invoicePanelLayout.setHorizontalGroup(
                                invoicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(invoicePanelLayout.createSequentialGroup()
                                                                .addGap(16, 16, 16)
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel15,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                216,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(paymentComboBox,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                115,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(invoicePanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addGroup(invoicePanelLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel13,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                127,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(tfTienKhachDua,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                292,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(invoicePanelLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel12,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                127,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(tfTongTien,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                292,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(invoicePanelLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel11,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                127,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(tfTenKH,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                292,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(invoicePanelLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel10,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                127,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(tfSDT,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                292,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(invoicePanelLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel14,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                127,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(tfTienThua,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                292,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(invoicePanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(tfMaKM,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                140,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(tfMaKH)
                                                                                                                .addComponent(cbMaKH,
                                                                                                                                0,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(cbGender,
                                                                                                                                0,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(cbMaKM,
                                                                                                                                0,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE))
                                                                                                .addGap(19, 19, 19))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                invoicePanelLayout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                                                .addComponent(btnHuy,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                150,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGap(18, 18, 18)
                                                                                                                .addComponent(btnIn,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                204,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGap(108, 108,
                                                                                                                                108))))
                                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                invoicePanelLayout.setVerticalGroup(
                                invoicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(invoicePanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                28,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(tfSDT,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                39, Short.MAX_VALUE)
                                                                                .addComponent(jLabel10,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                39,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(cbMaKH))
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel11,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(5, 5, 5)
                                                                                                .addComponent(cbGender))
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(tfTenKH,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(jLabel12,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(tfTongTien,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(cbMaKM))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(tfTienKhachDua,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(tfMaKH,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(jLabel13,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(invoicePanelLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(tfTienThua,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(tfMaKM,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(jLabel14,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(jLabel15,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(paymentComboBox,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(invoicePanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btnHuy,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                64,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnIn,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                64,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(25, Short.MAX_VALUE)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(productInfoPanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(productsPanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(12, 12, 12)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(cartPanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(invoicePanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(0, 0, Short.MAX_VALUE)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(productInfoPanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(cartPanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(invoicePanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(productsPanel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(0, 0, Short.MAX_VALUE)));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_jComboBox1ActionPerformed

        private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addButtonActionPerformed
                // TODO add your handling code here:
                // CHUA XU LY KHI CHON NHIEU ROW !!!!!!!
                if (selectedThuoc == null)
                        return;
                try {
                        int value = Integer.parseInt(jTextField6.getText());
                        if (value <= 0) {
                                JOptionPane.showMessageDialog(null, "Vui lòng nhập số nguyên lớn hơn 0");
                        } else {
                                ChiTietHdDTO ct = new ChiTietHdDTO();
                                boolean existed = false;
                                for (ChiTietHdDTO temp : listCTHD) {
                                        if (temp.getMaThuoc() == selectedThuoc.getMaThuoc()) {
                                                ct = temp;
                                                existed = true;
                                                break;
                                        }
                                }

                                if (existed) {
                                        ct.setSoLuong(ct.getSoLuong() + value);
                                } else {
                                        ct.setDonGia(selectedThuoc.getGia());
                                        ct.setMaLh(BUSManager.loHangBUS.getMaLhByMaThuoc(selectedThuoc.getMaThuoc()));
                                        ct.setSoLuong((value));
                                        ct.setMaThuoc(selectedThuoc.getMaThuoc());
                                        // còn thiếu mã hd -> sẽ điền khi tạo - in hóa đơn (getGeneratedKeys)
                                        listCTHD.add(ct);
                                }

                                // Load Cart
                                loadCartData();

                        }
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Chỉ được nhập số nguyên");
                } finally {
                        // UI invoice
                        refreshTongTien();
                }

        }// GEN-LAST:event_addButtonActionPerformed

        private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deleteButtonActionPerformed
                // TODO add your handling code here:
                if (selectedCTHD != null) {
                        JOptionPane.showMessageDialog(null, "Đã xóa khỏi giỏ hàng");
                        listCTHD.remove(selectedCTHD);
                        selectedCTHD = new ChiTietHdDTO();
                        loadCartData();
                        refreshTongTien();
                }
        }// GEN-LAST:event_deleteButtonActionPerformed

        private void btnInActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnInActionPerformed
                String name = tfTenKH.getText();
                String phone = tfSDT.getText();
                String money = tfTienKhachDua.getText();
                KhuyenMaiDTO km = (KhuyenMaiDTO) cbMaKM.getSelectedItem();
                KhachHangDTO kh = (KhachHangDTO) cbMaKH.getSelectedItem();

                if (!name.isBlank()) {
                        if (!ValidationUtils.isValidCustomerName(name)) {
                                JOptionPane.showMessageDialog(this, "Tên khách hàng không hợp lệ!");
                                return;
                        }
                }

                if (!ValidationUtils.isValidPhoneNumber(phone)) {
                        JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
                        return;
                }

                if (!ValidationUtils.isValidFloatBiggerThanZero(money)) {
                        JOptionPane.showMessageDialog(this, "Số tiền khách đưa phải là số dương!");
                        return;
                }

                // 1. Xử lý Khuyến mãi (KM)
    int maKm = 2; // Giả sử 2 là mã KM mặc định (Không áp dụng)
    if (km != null && BUSManager.khuyenMaiBUS.isKMValid(km)) {
        maKm = km.getMaKm();
    }
    
    // 2. Xử lý Khách hàng (KH)
    int maKh = 1; // Giả sử 1 là mã Khách lẻ mặc định (Guest)
    if (kh != null) {
        maKh = kh.getMaKh();
    }

                if (getCartSum().compareTo(BigDecimal.ZERO) <= 0) {
                        JOptionPane.showMessageDialog(this, "Giỏ hàng không được trống!");
                        return;
                }

                try {
                        float tienKhachDua = Float.parseFloat(money);
                        float tongTien = Float.parseFloat(tfTongTien.getText());

                        // MÃ KM DEFAULT = 2!!!!!!!!!!!
                        if (tienKhachDua >= tongTien) {
                                JOptionPane.showMessageDialog(null, "Tiến hành in hóa đơn");
                                BigDecimal bd = new BigDecimal(Float.toString(tongTien));

                                // Xử lý HD
                                HoaDonDTO hd = new HoaDonDTO(TaiKhoanBUS.getCurrentUser().getMaTk(), maKh, maKm, bd,
                                                java.sql.Date.valueOf(java.time.LocalDate.now()),
                                                (String) paymentComboBox.getSelectedItem(), 1);
                                                System.out.println(hd);
                                BUSManager.hoaDonBUS.addHoaDon(hd, listCTHD);
                        } else {
                                JOptionPane.showMessageDialog(null, "Số tiền khách đưa không hợp lệ!");
                        }
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Kiểm tra tổng tiền và giỏ hàng!");
                } catch (Exception ex) {
                        Logger.getLogger(HoaDonForm.class.getName()).log(Level.SEVERE, null, ex);
                }
        }// GEN-LAST:event_btnInActionPerformed

        private void refreshLabelMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_refreshLabelMouseClicked
                // TODO add your handling code here:
                loadThuocData();
        }// GEN-LAST:event_refreshLabelMouseClicked

        private ImageIcon loadImage(String path) {
                try {
                        File file = new File(path);

                        // Ảnh trên filesystem
                        if (file.exists()) {
                                Image img = ImageIO.read(file); // QUAN TRỌNG: dùng ImageIO.read()
                                if (img != null)
                                        return new ImageIcon(img);
                        }

                        // Thử load trong resources
                        URL url = getClass().getResource(path);
                        if (url != null) {
                                Image img = ImageIO.read(url); // load bằng ImageIO
                                if (img != null)
                                        return new ImageIcon(img);
                        }

                        // Thử "src" nếu đường dẫn lưu như /image/xxx.webp
                        file = new File("src" + path);
                        if (file.exists()) {
                                Image img = ImageIO.read(file);
                                if (img != null)
                                        return new ImageIcon(img);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel actionPanel;
        private javax.swing.JButton addButton;
        private javax.swing.JButton btnHuy;
        private javax.swing.JButton btnIn;
        private javax.swing.JPanel cartPanel;
        private javax.swing.JPanel cartProductsPanel;
        private javax.swing.JComboBox<String> cbGender;
        private javax.swing.JComboBox<KhachHangDTO> cbMaKH;
        private javax.swing.JComboBox<KhuyenMaiDTO> cbMaKM;
        private javax.swing.JButton deleteButton;
        private javax.swing.JPanel invoicePanel;
        private javax.swing.JComboBox<String> jComboBox1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel13;
        private javax.swing.JLabel jLabel14;
        private javax.swing.JLabel jLabel15;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTextField jTextField6;
        private javax.swing.JLabel lblDonGia;
        private javax.swing.JLabel lblMaThuoc;
        private javax.swing.JLabel lblTenThuoc;
        private javax.swing.JLabel lblThanhPhan;
        private javax.swing.JPanel listProductPanel;
        private javax.swing.JLabel magnifyingGlassLabel;
        private javax.swing.JComboBox<String> paymentComboBox;
        private javax.swing.JLabel productIconLabel;
        private javax.swing.JPanel productInfoPanel;
        private javax.swing.JPanel productsPanel;
        private javax.swing.JLabel refreshLabel;
        private javax.swing.JTextArea taThanPhan;
        private javax.swing.JTextField tfDonGia;
        private javax.swing.JTextField tfMaKH;
        private javax.swing.JTextField tfMaKM;
        private javax.swing.JTextField tfMaThuoc;
        private javax.swing.JTextField tfSDT;
        private javax.swing.JTextField tfTenKH;
        private javax.swing.JTextField tfTenThuoc;
        private NumberOnlyField tfTienKhachDua;
        private javax.swing.JTextField tfTienThua;
        private javax.swing.JTextField tfTimKiemThuoc;
        private javax.swing.JTextField tfTongTien;
        // End of variables declaration//GEN-END:variables
}
