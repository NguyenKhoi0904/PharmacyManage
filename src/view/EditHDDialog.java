/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import BUS.BUSManager;
import DTO.HoaDonDTO;
import DTO.KhuyenMaiDTO;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import utils.ValidationUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import utils.BigDecimalUtils;

/**
 *
 * @author admin
 */
public class EditHDDialog extends javax.swing.JDialog {

    private HoaDonDTO hoaDon;
    private boolean saved = false; // dùng để biết có lưu thay đổi không
    
    // trạng thái để tránh vòng lặp listener khi setText programmatically
    private boolean isUpdatingTongTien = false;

    // lưu giá trị tổng tiền "gốc" (chưa áp dụng KM)
    private java.math.BigDecimal baseTongTien = java.math.BigDecimal.ZERO;


    public EditHDDialog(Frame parent, HoaDonDTO hoaDon) {
        super(parent, "Chỉnh sửa hóa đơn", true); // true = modal dialog
        initComponents();
        this.hoaDon = hoaDon;
        loadBaseTongTien();
        loadHoaDonData(); // table
        setLocationRelativeTo(parent);
        addEvent();
    }
    
    private void loadBaseTongTien(){
        BigDecimal phanTramGiam = BigDecimalUtils.getPhanTramGiamFromMaKM(tfMaKM.getText());
        if (phanTramGiam.compareTo(BigDecimal.ZERO) == 1)
            // Nghĩa là ban đầu hóa đơn có mã giảm có giá trị km > 0
        {
            // Hóa đơn có mã khuyến mãi (ví dụ: giảm 0.1 nghĩa là 10%)
            BigDecimal tongTienSauGiam = hoaDon.getTongTien();

            // baseTongTien = tongTienSauGiam / (1 - phanTramGiam)
            BigDecimal motTruGiam = BigDecimal.ONE.subtract(phanTramGiam);
            if (motTruGiam.compareTo(BigDecimal.ZERO) > 0) {
                this.baseTongTien = tongTienSauGiam.divide(motTruGiam, 2, RoundingMode.HALF_UP);
            } else {
                // Tránh chia cho 0 nếu phanTramGiam = 1.0 (100%)
                this.baseTongTien = tongTienSauGiam;
            }

        }
        else {
            // Không có khuyến mãi thì base chính là tổng tiền hiện tại
            this.baseTongTien = hoaDon.getTongTien();
        }
    }
    private void loadHoaDonData() {
        tfMaHD.setText(String.valueOf(hoaDon.getMaHd()));
        tfMaNV.setText(String.valueOf(hoaDon.getMaNv()));
        tfMaKH.setText(String.valueOf(hoaDon.getMaKh()));
        tfMaKM.setText(hoaDon.getMaKm() != null ? String.valueOf(hoaDon.getMaKm()) : "");
        tfTongTien.setText(String.format("%,.0f", hoaDon.getTongTien()));
        tfTongTienMoi.setText(String.format("%,.0f", hoaDon.getTongTien()));
        tfNgayXuat.setText(new SimpleDateFormat("dd/MM/yyyy").format(hoaDon.getNgayXuat()));

        cbPayment.setSelectedItem(hoaDon.getPhuongThucTt());
        cbTrangThai.setSelectedIndex(hoaDon.getTrangThai());
    }
    public boolean isSaved() {
        return saved;
    }
    
    private void addEvent() {
        // Ma KM
        tfMaKM.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { refreshTongTienFromBase(); }
            @Override public void removeUpdate(DocumentEvent e) { refreshTongTienFromBase(); }
            @Override public void changedUpdate(DocumentEvent e) { refreshTongTienFromBase(); }
        });

        // Khi người dùng sửa tfTongTien (đầu vào base) -> cập nhật baseTongTien và tính lại
        tfTongTien.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { onUserChangedTongTien(); }
            @Override public void removeUpdate(DocumentEvent e) { onUserChangedTongTien(); }
            @Override public void changedUpdate(DocumentEvent e) { onUserChangedTongTien(); }
        });
    }

    private void onUserChangedTongTien() {
        if (isUpdatingTongTien) return; // tránh recursion
        // đọc giá trị do user gõ (có thể dạng 123,000)
        String text = tfTongTien.getText();
        java.math.BigDecimal parsed = BigDecimalUtils.parseAmount(text);
        // cập nhật baseTongTien
        baseTongTien = parsed;
        // tính và hiển thị kết quả đã áp KM
        refreshTongTienFromBase();
    }

    private void refreshTongTienFromBase() {
        if (isUpdatingTongTien) return;
        try {
            isUpdatingTongTien = true;

            // Lấy phần trăm giảm
            java.math.BigDecimal phanTramGiam = BigDecimalUtils.getPhanTramGiamFromMaKM(tfMaKM.getText()); // 0.10 nghĩa 10%
            if (phanTramGiam == null) phanTramGiam = java.math.BigDecimal.ZERO;

            // Tính tiền giảm = baseTongTien * phanTramGiam
            java.math.BigDecimal tienGiam = baseTongTien.multiply(phanTramGiam);
            // làm tròn về nguyên (đồng)
            tienGiam = tienGiam.setScale(0, java.math.RoundingMode.HALF_UP);

            java.math.BigDecimal tongTienMoi = baseTongTien.subtract(tienGiam);
            if (tongTienMoi.compareTo(java.math.BigDecimal.ZERO) < 0) tongTienMoi = java.math.BigDecimal.ZERO;

            // Hiển thị: format có phân nhóm
            // ⚠️ Dời việc setText sang thread kế tiếp để không vi phạm lock
            String formatted = BigDecimalUtils.formatAmount(tongTienMoi);
            javax.swing.SwingUtilities.invokeLater(() -> tfTongTienMoi.setText(formatted));
        } finally {
            isUpdatingTongTien = false;
        }
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
        btnHuy = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        lblMaNV1 = new javax.swing.JLabel();
        tfMaHD = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tfNgayXuat = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfTongTienMoi = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1350, 800));
        setSize(new java.awt.Dimension(1350, 800));

        jLabel1.setBackground(new java.awt.Color(0, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SỬA HÓA ĐƠN");
        jLabel1.setOpaque(true);

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaNV.setText("Mã nhân viên:");

        tfMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaKH.setText("Mã khách hàng:");

        tfMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblMaKM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaKM.setText("Mã khuyến mãi:");

        tfMaKM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Tổng tiền:");

        tfTongTien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Phương thức TT:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Trạng thái:");

        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chưa thanh toán", "Hoàn thành" }));

        cbPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản" }));

        btnHuy.setBackground(new java.awt.Color(255, 0, 0));
        btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("HỦY");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnLuu.setBackground(new java.awt.Color(51, 255, 0));
        btnLuu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnLuu.setForeground(new java.awt.Color(255, 255, 255));
        btnLuu.setText("LƯU");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        lblMaNV1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaNV1.setText("Mã hóa đơn:");

        tfMaHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfMaHD.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Ngày xuất:");

        tfNgayXuat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfNgayXuat.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Tổng tiền mới:");

        tfTongTienMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfTongTienMoi.setEnabled(false);

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInfoLayout.createSequentialGroup()
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlInfoLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnHuy)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlInfoLayout.createSequentialGroup()
                                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMaKM, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMaNV1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfMaNV)
                                    .addComponent(tfMaKH)
                                    .addComponent(tfMaKM)
                                    .addComponent(tfTongTien)
                                    .addComponent(cbTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbPayment, 0, 150, Short.MAX_VALUE)
                                    .addComponent(tfMaHD)
                                    .addComponent(tfNgayXuat)
                                    .addComponent(tfTongTienMoi))))
                        .addGap(37, 37, 37))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTongTienMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLuu)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:

        if (!ValidationUtils.isValidFloatBiggerThanZero(tfMaNV.getText())) {
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa phải là số dương!");
            return;
        }
        if (!ValidationUtils.isValidFloatBiggerThanZero(tfMaKH.getText())) {
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa phải là số dương!");
            return;
        }
        if (!ValidationUtils.isAnyNumber(tfMaKM.getText())) {
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa phải là số dương!");
            return;
        }
        
        int maNV = Integer.parseInt(tfMaNV.getText());
        if (BUSManager.nhanVienBUS.getNhanVienByMaNv(maNV) == null) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại!");
            return;
        }

        int maKH = Integer.parseInt(tfMaKH.getText());
        if (BUSManager.khachHangBUS.getKhachHangByMaKh(maKH) == null) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không tồn tại!");
            return;
        }
        
        int maKM = ValidationUtils.validateMaKM(tfMaKM.getText()) != null ?
                ValidationUtils.validateMaKM(tfMaKM.getText()) : 302;

                
        try {
            hoaDon.setMaNv(maNV);
            hoaDon.setMaKh(maKH);
            hoaDon.setMaKm(maKM);
            hoaDon.setTongTien(new java.math.BigDecimal(tfTongTienMoi.getText().replace(".", "")));

            Date parsedDate = (Date) new SimpleDateFormat("dd/MM/yyyy").parse(tfNgayXuat.getText());
            hoaDon.setNgayXuat(new java.sql.Date(parsedDate.getTime()));

            hoaDon.setPhuongThucTt(cbPayment.getSelectedItem().toString());
            hoaDon.setTrangThai(cbTrangThai.getSelectedIndex());

            BUSManager.hoaDonBUS.updateHoaDon(hoaDon.getMaHd(), hoaDon);

            JOptionPane.showMessageDialog(this, "Cập nhật hóa đơn thành công!");
            saved = true; // đánh dấu đã lưu
            dispose(); // đóng dialog
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnLuuActionPerformed


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
            java.util.logging.Logger.getLogger(EditHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditHDDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditHDDialog dialog = new EditHDDialog(new javax.swing.JFrame(), new HoaDonDTO());
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
    private javax.swing.JButton btnLuu;
    private javax.swing.JComboBox<String> cbPayment;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaKM;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMaNV1;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JTextField tfMaHD;
    private javax.swing.JTextField tfMaKH;
    private javax.swing.JTextField tfMaKM;
    private javax.swing.JTextField tfMaNV;
    private javax.swing.JTextField tfNgayXuat;
    private javax.swing.JTextField tfTongTien;
    private javax.swing.JTextField tfTongTienMoi;
    // End of variables declaration//GEN-END:variables
}
