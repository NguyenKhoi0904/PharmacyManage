/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import view.Dialog.EditHDDialog;
import view.Dialog.DetailHDDialog;
import BUS.BUSManager;
import BUS.ChiTietHdBUS;
import BUS.HoaDonBUS;
import DTO.ChiTietHdDTO;
import DTO.DanhMucThuocDTO;
import DTO.HoaDonDTO;
import DTO.KhachHangDTO;
import DTO.ThuocDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.IconUtils;

/**
 *
 * @author admin
 */
public class QuanLyHoaDonForm extends javax.swing.JFrame {

    private HoaDonDTO selectedHD;
    private ArrayList<HoaDonDTO> listHD = new ArrayList<HoaDonDTO>();
    
    private JTable tblListHD;
    private DefaultTableModel listHDTableModel;
    public QuanLyHoaDonForm() {
        initComponents();
        editButton.setVisible(false);
        // DEBUG ONLY !!!
         BUSManager.initAllBUS();
        listHD = HoaDonBUS.getInstance().getListHoaDon();
        
        setupListHoaDon();
        IconUtils.setIcon(magnifyingGlassLabel4, "magnifying-glass.png", true);
        IconUtils.setIcon(refreshLabel, "refresh.png", true);

        
        addEventForSearching(tfTimKiemHoaDon, tblListHD, listHD);
    }
    
    private JTable createHoaDonTable() {
        String[] columnNames = {
            "Mã HĐ", "Mã NV", "Mã KH", "Mã KM",
            "Tổng tiền", "Ngày xuất", "Phương thức TT", "Trạng thái"
        };

        listHDTableModel = new DefaultTableModel(columnNames, 0);
        tblListHD = new JTable(listHDTableModel);
        tblListHD.setFillsViewportHeight(true);
        tblListHD.setRowHeight(28);
        tblListHD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblListHD.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblListHD.getTableHeader().setBackground(new Color(240, 240, 240));
        tblListHD.getTableHeader().setForeground(Color.BLACK);

        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblListHD.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // mã HĐ
        tblListHD.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // mã NV
        tblListHD.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // mã KH
        tblListHD.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // mã KM
        tblListHD.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // trạng thái

        // Căn phải cho tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tblListHD.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // tổng tiền

        // Hiệu ứng nền xen kẽ
        tblListHD.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                return c;
            }
        });

        // Sự kiện click chọn hóa đơn
        tblListHD.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRowIndex = tblListHD.getSelectedRow();
                if (selectedRowIndex != -1) {
                    int selectedMaHD = (int) tblListHD.getValueAt(selectedRowIndex, 0);
                    selectedHD = BUSManager.hoaDonBUS.getHoaDonByMaHd(selectedMaHD);

                }
            }
        });

    return tblListHD;
}

    private void loadHoaDonData() {
        listHD = BUSManager.hoaDonBUS.getListHoaDon(); // lấy danh sách từ BUS
        listHDTableModel.setRowCount(0); // xóa dữ liệu cũ

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (HoaDonDTO hd : listHD) {
            Object[] row = {
                hd.getMaHd(),
                hd.getMaNv(),
                hd.getMaKh(),
                hd.getMaKm() != null ? hd.getMaKm() : "—", // nếu null thì hiển thị dấu —
                String.format("%,.0f", hd.getTongTien()),
                sdf.format(hd.getNgayXuat()),
                hd.getPhuongThucTt(),
                hd.getTrangThai() == 1 ? "Hoàn tất" : "Đang xử lý"
            };
            listHDTableModel.addRow(row);
        }
    }
    private void setupListHoaDon() {
        JTable table = createHoaDonTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlListHD.setLayout(new BorderLayout());
        pnlListHD.add(scrollPane, BorderLayout.CENTER);

        // Lần đầu load dữ liệu
        loadHoaDonData();
    }
    private void addEventForSearching(JTextField txtSearch, JTable table, ArrayList<HoaDonDTO> originalList) {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            private void search() {
                String keyword = txtSearch.getText().trim().toLowerCase();

                // Nếu không nhập gì thì hiển thị toàn bộ danh sách
                if (keyword.isEmpty()) {
                    updateHoaDonTable(originalList, table);
                    return;
                }

                // Lọc danh sách theo từ khóa (ví dụ tìm trong mã, nhân viên, khách hàng, phương thức TT, ...)
                ArrayList<HoaDonDTO> filteredList = new ArrayList<>();
                for (HoaDonDTO hd : originalList) {
                    // Ghép logic tìm kiếm theo nhiều trường
                    if (String.valueOf(hd.getMaHd()).contains(keyword)
                        || String.valueOf(hd.getMaNv()).contains(keyword)
                        || String.valueOf(hd.getMaKh()).contains(keyword)
                        || (hd.getPhuongThucTt() != null && hd.getPhuongThucTt().toLowerCase().contains(keyword))
                        || (hd.getTongTien() != null && hd.getTongTien().toString().contains(keyword))
                        || (hd.getNgayXuat() != null && hd.getNgayXuat().toString().toLowerCase().contains(keyword))) {
                        filteredList.add(hd);
                    }
                }

                // Cập nhật lại bảng
                updateHoaDonTable(filteredList, table);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { search(); }
            @Override
            public void removeUpdate(DocumentEvent e) { search(); }
            @Override
            public void changedUpdate(DocumentEvent e) { search(); }
        });
    }
    private void updateHoaDonTable(ArrayList<HoaDonDTO> list, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // định dạng ngày ngắn gọn

        for (HoaDonDTO hd : list) {
            String ngayXuatFormatted = "";
            if (hd.getNgayXuat() != null) {
                ngayXuatFormatted = dateFormat.format(hd.getNgayXuat());
            }
            model.addRow(new Object[] {
                hd.getMaHd(),
                hd.getMaNv(),
                hd.getMaKh(),
                hd.getMaKm() != null ? hd.getMaKm() : "", // tránh null
                hd.getTongTien() != null ? hd.getTongTien() : "",
                ngayXuatFormatted,
                hd.getPhuongThucTt(),
                hd.getTrangThai() == 1 ? "Hoàn tất" : "Đang xử lý"
            });
        }
    }
    
    private void exportHoaDonToExcel(ArrayList<HoaDonDTO> listHD, ArrayList<ChiTietHdDTO> listCTHD, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("HoaDon");

        // ----- STYLE -----
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        int rowIndex = 0;

        for (HoaDonDTO hd : listHD) {
            // --- Ghi thông tin hóa đơn ---
            Row rowHd = sheet.createRow(rowIndex++);
            rowHd.createCell(0).setCellValue("Mã hóa đơn: " + hd.getMaHd());
            rowHd.createCell(1).setCellValue("Ngày xuất: " + hd.getNgayXuat());
            rowHd.createCell(2).setCellValue("Tổng tiền: " + hd.getTongTien());
            rowHd.createCell(3).setCellValue("PT Thanh toán: " + hd.getPhuongThucTt());
            rowHd.createCell(4).setCellValue("Trạng thái: " + hd.getTrangThai());
            rowHd.createCell(5).setCellValue("Mã KM: " + (hd.getMaKm() != null ? hd.getMaKm() : "2"));
            rowHd.createCell(6).setCellValue("Mã NV: " + hd.getMaNv());
            rowHd.createCell(7).setCellValue("Mã KH: " + hd.getMaKh());

            // --- Header chi tiết hóa đơn ---
            Row headerRow = sheet.createRow(rowIndex++);
            String[] headers = {"Mã Lô Hàng", "Mã Thuốc", "Đơn giá", "Số lượng"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- Ghi chi tiết thuộc hóa đơn này ---
            for (ChiTietHdDTO cthd : listCTHD) {
                if (cthd.getMaHd() == hd.getMaHd()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(cthd.getMaLh());
                    row.createCell(1).setCellValue(cthd.getMaThuoc());
                    row.createCell(2).setCellValue(cthd.getDonGia().doubleValue());
                    row.createCell(3).setCellValue(cthd.getSoLuong());
                }
            }

            // --- Dòng trống ngăn cách giữa các hóa đơn ---
            rowIndex++;
        }

        // --- Auto-size cột ---
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();            
            JOptionPane.showMessageDialog(null, "Đã lưu vào thư mục gốc");
            System.out.println("✅ Xuất file Excel thành công: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void importHoaDonFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            HoaDonBUS hoaDonBUS = HoaDonBUS.getInstance();

            int rowIndex = 0;
            int lastRow = sheet.getLastRowNum();

            while (rowIndex <= lastRow) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    rowIndex++;
                    continue;
                }

                // ---- XÁC ĐỊNH DÒNG HÓA ĐƠN ----
                Cell firstCell = row.getCell(0);
                if (firstCell == null || firstCell.getCellType() != CellType.STRING ||
                    !firstCell.getStringCellValue().startsWith("Mã hóa đơn")) {
                    rowIndex++;
                    continue;
                }

                // === LẤY THÔNG TIN HÓA ĐƠN ===
                String maHdStr = firstCell.getStringCellValue().replace("Mã hóa đơn: ", "").trim();
                int maHd = Integer.parseInt(maHdStr);

                String ngayXuatStr = getStringCellValue(row.getCell(1)).replace("Ngày xuất: ", "").trim();
                String tongTienStr = getStringCellValue(row.getCell(2)).replace("Tổng tiền: ", "").trim();
                String phuongThucTt = getStringCellValue(row.getCell(3)).replace("PT Thanh toán: ", "").trim();
                String trangThaiStr = getStringCellValue(row.getCell(4)).replace("Trạng thái: ", "").trim();
                String maKmStr = getStringCellValue(row.getCell(5)).replace("Mã KM: ", "").trim();
                String maNvStr = getStringCellValue(row.getCell(6)).replace("Mã NV: ", "").trim();
                int maNv = Integer.parseInt(maNvStr);
                String maKhStr = getStringCellValue(row.getCell(7)).replace("Mã KH: ", "").trim();
                int maKh = Integer.parseInt(maKhStr);

                HoaDonDTO hd = new HoaDonDTO();
                hd.setMaHd(maHd);
                hd.setMaNv(maNv);
                hd.setMaKh(maKh);

                // Convert chuỗi sang Date
                try {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(ngayXuatStr);
                    hd.setNgayXuat(sqlDate);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày cho hóa đơn " + maHd);
                    rowIndex++;
                    continue;
                }

                // Convert BigDecimal và trạng thái
                hd.setTongTien(BigDecimal.valueOf(Double.parseDouble(tongTienStr)));
                hd.setPhuongThucTt(phuongThucTt);
                try {
                    hd.setTrangThai(Integer.parseInt(trangThaiStr));
                } catch (NumberFormatException nfe) {
                    hd.setTrangThai(0); // mặc định nếu chưa xác định
                }

                // --- Xử lý mã khuyến mãi ---
                Integer maKm = null;
                if (!maKmStr.equalsIgnoreCase("NULL") && !maKmStr.isEmpty()) {
                    try {
                        maKm = Integer.parseInt(maKmStr);

                        // ✅ Kiểm tra xem mã KM có tồn tại trong DB không
                        if (!BUSManager.khuyenMaiBUS.checkIfMaKmExist(maKm)) {
                            System.out.println("⚠️ Mã KM " + maKm + " không tồn tại.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Lỗi định dạng mã KM: " + maKmStr);
                    }
                }

                hd.setMaKm(maKm);

                rowIndex += 2; // bỏ qua dòng tiêu đề chi tiết hóa đơn

                // ---- ĐỌC CHI TIẾT HÓA ĐƠN ----
                ArrayList<ChiTietHdDTO> cthdList = new ArrayList<>();

                while (rowIndex <= lastRow) {
                    Row ctRow = sheet.getRow(rowIndex);
                    if (ctRow == null) {
                        rowIndex++;
                        continue;
                    }

                    Cell firstCTCell = ctRow.getCell(0);
                    if (firstCTCell == null || firstCTCell.getCellType() == CellType.BLANK) {
                        rowIndex++;
                        break;
                    }

                    if (firstCTCell.getCellType() == CellType.STRING &&
                        firstCTCell.getStringCellValue().startsWith("Mã hóa đơn")) {
                        break;
                    }

                    ChiTietHdDTO cthd = new ChiTietHdDTO();
                    cthd.setMaHd(maHd);
                    cthd.setMaLh((int) ctRow.getCell(0).getNumericCellValue());
                    cthd.setMaThuoc((int) ctRow.getCell(1).getNumericCellValue());
                    cthd.setDonGia(BigDecimal.valueOf(ctRow.getCell(2).getNumericCellValue()));
                    cthd.setSoLuong((int) ctRow.getCell(3).getNumericCellValue());

                    cthdList.add(cthd);
                    rowIndex++;
                }

                if (cthdList.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "❌ Hóa đơn " + maHd + " không có chi tiết hóa đơn. Bỏ qua!",
                            "Lỗi import", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                
                // ---- TÍNH LẠI ĐƠN GIÁ & TỔNG TIỀN THEO DB ----
                BigDecimal tongTien = BigDecimal.ZERO;

                for (ChiTietHdDTO cthd : cthdList) {
                    // Lấy đơn giá thuốc từ DB theo mã thuốc
                    BigDecimal donGiaThuoc = BUSManager.thuocBUS.getThuocByMaThuoc(cthd.getMaThuoc()).getGia();
                    if (donGiaThuoc == null) {
                        System.out.println("⚠️ Không tìm thấy đơn giá cho thuốc " + cthd.getMaThuoc() + ". Gán = 0");
                        donGiaThuoc = BigDecimal.ZERO;
                    }

                    // Cập nhật lại đơn giá và cộng dồn tổng tiền
                    cthd.setDonGia(donGiaThuoc);
                    BigDecimal thanhTien = donGiaThuoc.multiply(BigDecimal.valueOf(cthd.getSoLuong()));
                    tongTien = tongTien.add(thanhTien);
                }

                // Cập nhật tổng tiền hóa đơn
                hd.setTongTien(tongTien);
                
                // ---- LƯU DATABASE ----
                if (hoaDonBUS.checkIfMaHdExist(hd.getMaHd())) {
                    // Cập nhật hóa đơn nếu tồn tại
                    if (hoaDonBUS.updateHoaDon(hd.getMaHd(), hd)) {
                        System.out.println("🔄 Cập nhật hóa đơn " + maHd + " thành công");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Không thể cập nhật hóa đơn " + maHd,
                                "Lỗi import", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } else {
                    // Thêm mới nếu chưa có
                    if (hoaDonBUS.addHD(hd)) {
                        System.out.println("✅ Thêm mới hóa đơn " + maHd + " thành công");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Không thể thêm hóa đơn " + maHd,
                                "Lỗi import", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                }

                // ---- Lưu chi tiết hóa đơn ----
                ChiTietHdBUS chiTietHdBUS = ChiTietHdBUS.getInstance();
                for (ChiTietHdDTO cthd : cthdList) {
                    if (chiTietHdBUS.existsCTHD(cthd.getMaHd(), cthd.getMaLh(), cthd.getMaThuoc())) {
                        // Nếu đã có → cập nhật
                        if (chiTietHdBUS.updateChiTietHd(cthd)) {
                            System.out.println("🔄 Cập nhật CTHD (HD=" + maHd + ", LH=" + cthd.getMaLh() + ")");
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Không thể cập nhật chi tiết hóa đơn cho mã " + maHd,
                                    "Lỗi import", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Nếu chưa có → thêm mới
                        if (chiTietHdBUS.addChiTietHd(cthd)) {
                            System.out.println("➕ Thêm mới CTHD (HD=" + maHd + ", LH=" + cthd.getMaLh() + ")");
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Không thể thêm chi tiết hóa đơn cho mã " + maHd,
                                    "Lỗi import", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "✅ Import hoàn tất!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi đọc file Excel: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Trả về giá trị chuỗi từ ô Excel, an toàn kể cả khi null.
     */
    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
        return "";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        actionPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        infoButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        magnifyingGlassLabel4 = new javax.swing.JLabel();
        tfTimKiemHoaDon = new javax.swing.JTextField();
        refreshLabel = new javax.swing.JLabel();
        pnlListHD = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1350, 750));

        jLabel2.setBackground(new java.awt.Color(0, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("DANH SÁCH HÓA ĐƠN");
        jLabel2.setOpaque(true);

        buttonPanel.setPreferredSize(new java.awt.Dimension(630, 67));

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/wrench.png"))); // NOI18N
        editButton.setText("Sửa");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove.png"))); // NOI18N
        deleteButton.setText("Xóa");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        infoButton.setText("Chi tiết");
        infoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoButtonActionPerformed(evt);
            }
        });

        importButton.setText("Nhập excel");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        exportButton.setText("Xuất excel");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(infoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(importButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        magnifyingGlassLabel4.setText("jLabel8");

        tfTimKiemHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfTimKiemHoaDon.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(magnifyingGlassLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfTimKiemHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(magnifyingGlassLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTimKiemHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refreshLabel.setText("jLabel8");
        refreshLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(refreshLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlListHDLayout = new javax.swing.GroupLayout(pnlListHD);
        pnlListHD.setLayout(pnlListHDLayout);
        pnlListHDLayout.setHorizontalGroup(
            pnlListHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlListHDLayout.setVerticalGroup(
            pnlListHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlListHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(actionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1350, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlListHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
//        if (selectedHD == null) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // Tạo và hiển thị dialog chỉnh sửa
//        EditHDDialog dialog = new EditHDDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), selectedHD);
//        dialog.setVisible(true);
//
//        // Sau khi dialog đóng, kiểm tra xem có lưu không
//        if (dialog.isSaved()) {
//            loadHoaDonData(); // reload lại bảng
//        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        if (selectedHD == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Xác nhận với người dùng trước khi xoá
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xoá hóa đơn này không?",
            "Xác nhận xoá",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return; // người dùng chọn NO -> thoát
        }

        try {
            boolean deleted = BUSManager.hoaDonBUS.deleteHoaDon(selectedHD.getMaHd());
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Xoá hóa đơn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                // refresh UI
                loadHoaDonData(); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xoá hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void infoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoButtonActionPerformed
        // TODO add your handling code here:
        if (selectedHD == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo và hiển thị dialog chỉnh sửa
        DetailHDDialog dialog = new DetailHDDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), selectedHD);
        dialog.setVisible(true);
        
        if (dialog.isSaved())
        {
            loadHoaDonData();
        }
    }//GEN-LAST:event_infoButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel để import");

        // Chỉ cho phép chọn file .xlsx hoặc .xls
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Excel Files (*.xlsx, *.xls)", "xlsx", "xls"
        );
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Gọi hàm import Excel
            importHoaDonFromExcel(filePath);
            
            // Update UI
            updateHoaDonTable(listHD, tblListHD);
        } else {
            JOptionPane.showMessageDialog(this, "Đã hủy chọn file.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_importButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        // TODO add your handling code here:
        exportHoaDonToExcel(listHD, BUSManager.chiTietHdBUS.getListChiTietHd(), "HoaDon.xlsx");
    }//GEN-LAST:event_exportButtonActionPerformed

    private void refreshLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshLabelMouseClicked
        // TODO add your handling code here:
        // Update UI
        loadHoaDonData();
        updateHoaDonTable(listHD, tblListHD);
    }//GEN-LAST:event_refreshLabelMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuanLyHoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyHoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyHoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyHoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyHoaDonForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton importButton;
    private javax.swing.JButton infoButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel magnifyingGlassLabel4;
    private javax.swing.JPanel pnlListHD;
    private javax.swing.JLabel refreshLabel;
    private javax.swing.JTextField tfTimKiemHoaDon;
    // End of variables declaration//GEN-END:variables
}
