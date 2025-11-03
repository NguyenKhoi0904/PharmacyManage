/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import BUS.BUSManager;
import DTO.ChiTietHdDTO;
import DTO.DanhMucThuocDTO;
import DTO.HoaDonDTO;
import DTO.ThuocDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.IconUtils;

/**
 *
 * @author admin
 */
public class DetailHDDialog extends javax.swing.JDialog {

    private HoaDonDTO hoaDon;
    private JTable thuocTable;
    private DefaultTableModel thuocTableModel;
    
    public DetailHDDialog(java.awt.Frame parent, HoaDonDTO hoaDon) {
        super(parent, "Chi Tiết Hóa Đơn", true);
        this.hoaDon = hoaDon;
        initComponents();
        loadHoaDonData();
        setupListProduct();
        setLocationRelativeTo(parent);
    }
    private void loadHoaDonData() {
        tfMaHD.setText(String.valueOf(hoaDon.getMaHd()));
        tfMaNV.setText(String.valueOf(hoaDon.getMaNv()));
        tfMaKH.setText(String.valueOf(hoaDon.getMaKh()));
        tfMaKM.setText(hoaDon.getMaKm() != null ? String.valueOf(hoaDon.getMaKm()) : "");
        tfTongTien.setText(String.format("%,.0f", hoaDon.getTongTien()));
        tfNgayXuat.setText(new SimpleDateFormat("dd/MM/yyyy").format(hoaDon.getNgayXuat()));

        cbPayment.setSelectedItem(hoaDon.getPhuongThucTt());
        cbTrangThai.setSelectedIndex(hoaDon.getTrangThai());
    }
    private JTable createThuocTable() {
    String[] columnNames = {
        "Mã thuốc", "Tên thuốc", "Đơn vị tính", "Đơn giá"
    };

    thuocTableModel = new DefaultTableModel(columnNames, 0);
    thuocTable = new JTable(thuocTableModel);
    thuocTable.setFillsViewportHeight(true);
    thuocTable.setRowHeight(28);
    thuocTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    thuocTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    thuocTable.getTableHeader().setBackground(new Color(240, 240, 240));
    thuocTable.getTableHeader().setForeground(Color.BLACK);

    // Căn giữa cột "Mã thuốc"
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    thuocTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

    // Căn phải cho cột "Đơn giá"
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
    thuocTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

    // Hiệu ứng nền xen kẽ (striped rows)
    DefaultTableCellRenderer alternateRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
            } else {
                c.setBackground(new Color(184, 207, 229)); // màu chọn nhẹ
            }
            return c;
        }
    };
    thuocTable.setDefaultRenderer(Object.class, alternateRenderer);

    return thuocTable;
}

private void loadThuocData() {
    // Nếu table hoặc model chưa khởi tạo, thoát sớm tránh NullPointerException
    if (thuocTableModel == null) return;

    ArrayList<ChiTietHdDTO> listCTHD = BUSManager.chiTietHdBUS.getListChiTietHdByMaHd(hoaDon.getMaHd());
    thuocTableModel.setRowCount(0); // Clear dữ liệu cũ

    for (ChiTietHdDTO cthd : listCTHD) {
        ThuocDTO thuoc = BUSManager.thuocBUS.getThuocByMaThuoc(cthd.getMaThuoc());
        if (thuoc != null) {
            Object[] row = {
                thuoc.getMaThuoc(),
                thuoc.getTenThuoc(),
                thuoc.getDonViTinh(),
                String.format("%,.0f", thuoc.getGia()) // format tiền đẹp
            };
            thuocTableModel.addRow(row);
        }
    }
}

private void setupListProduct() {
    // Chỉ tạo bảng 1 lần
    thuocTable = createThuocTable();

    JScrollPane scrollPane = new JScrollPane(thuocTable);
    scrollPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220)),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
    scrollPane.getViewport().setBackground(Color.WHITE);

    pnlListThuoc.setLayout(new BorderLayout());
    pnlListThuoc.removeAll(); // xóa nội dung cũ nếu có
    pnlListThuoc.add(scrollPane, BorderLayout.CENTER);

    // Gọi load sau khi bảng đã sẵn sàng
    loadThuocData();

    pnlListThuoc.revalidate();
    pnlListThuoc.repaint();
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pnlInfo = new javax.swing.JPanel();
        lblMaNV = new javax.swing.JLabel();
        tfMaNV = new javax.swing.JTextField();
        lblMaKH = new javax.swing.JLabel();
        tfMaKH = new javax.swing.JTextField();
        lblMaKM = new javax.swing.JLabel();
        tfMaKM = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfTongTien = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbTrangThai = new javax.swing.JComboBox<>();
        cbPayment = new javax.swing.JComboBox<>();
        lblMaNV1 = new javax.swing.JLabel();
        tfMaHD = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tfNgayXuat = new javax.swing.JTextField();
        pnlListThuoc = new javax.swing.JPanel();
        btnHuy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(0, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CHI TIẾT HÓA ĐƠN");
        jLabel1.setOpaque(true);

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaNV.setText("Mã nhân viên:");

        tfMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfMaNV.setEnabled(false);

        lblMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaKH.setText("Mã khách hàng:");

        tfMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfMaKH.setEnabled(false);

        lblMaKM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaKM.setText("Mã khuyến mãi:");

        tfMaKM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfMaKM.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Tổng tiền:");

        tfTongTien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfTongTien.setEnabled(false);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Phương thức TT:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Trạng thái:");

        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chưa thanh toán", "Hoàn thành" }));
        cbTrangThai.setEnabled(false);

        cbPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản" }));
        cbPayment.setEnabled(false);

        lblMaNV1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaNV1.setText("Mã hóa đơn:");

        tfMaHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfMaHD.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Ngày xuất:");

        tfNgayXuat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfNgayXuat.setEnabled(false);

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKM, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaNV1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfMaNV)
                    .addComponent(tfMaKH)
                    .addComponent(tfMaKM)
                    .addComponent(tfTongTien)
                    .addComponent(cbTrangThai, 0, 150, Short.MAX_VALUE)
                    .addComponent(cbPayment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfMaHD)
                    .addComponent(tfNgayXuat))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaNV1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaKM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfMaKM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfNgayXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlListThuocLayout = new javax.swing.GroupLayout(pnlListThuoc);
        pnlListThuoc.setLayout(pnlListThuocLayout);
        pnlListThuocLayout.setHorizontalGroup(
            pnlListThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        pnlListThuocLayout.setVerticalGroup(
            pnlListThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 292, Short.MAX_VALUE)
        );

        btnHuy.setBackground(new java.awt.Color(255, 0, 0));
        btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("ĐÓNG");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(pnlListThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(243, 243, 243)
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(pnlListThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHuy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnHuyActionPerformed

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
            java.util.logging.Logger.getLogger(DetailHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetailHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetailHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetailHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DetailHDDialog dialog = new DetailHDDialog(new javax.swing.JFrame(), new HoaDonDTO());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JComboBox<String> cbPayment;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaKM;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMaNV1;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JPanel pnlListThuoc;
    private javax.swing.JTextField tfMaHD;
    private javax.swing.JTextField tfMaKH;
    private javax.swing.JTextField tfMaKM;
    private javax.swing.JTextField tfMaNV;
    private javax.swing.JTextField tfNgayXuat;
    private javax.swing.JTextField tfTongTien;
    // End of variables declaration//GEN-END:variables
}
