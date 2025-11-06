package view;


import view.Dialog.AddKMDialog;
import BUS.BUSManager;
import BUS.HoaDonBUS;
import DTO.KhuyenMaiDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.IconUtils;
import view.Dialog.EditKMDialog;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author admin
 */
public class KhuyenMaiForm extends javax.swing.JFrame {

    private KhuyenMaiDTO selectedKM;
    private ArrayList<KhuyenMaiDTO> listKM = new ArrayList<KhuyenMaiDTO>();
    
    private DefaultTableModel maKmTableModel;
    private JTable maKmTable;
    
    public KhuyenMaiForm() {
        initComponents();
        
        // DEBUG ONLY !!!
        BUSManager.initAllBUS();
        listKM = BUSManager.khuyenMaiBUS.getListKhuyenMai();
        
        setIcons();
        setupListMaKM();
        
        addEventForSearching(tfTimKiemKM, maKmTable, listKM);
    }
    
    private void setIcons()
    {
        IconUtils.setIcon(magnifyingGlassLabel, "magnifying-glass.png", true);
        IconUtils.setIcon(refreshLabel, "refresh.png", true);
    }


    private JTable createMaKmTable() {
        String[] columnNames = {
            "STT", "Mã", "Tên", "Loại", "Giá trị", "Điều kiện", "Ngày BĐ", "Ngày KT", "Trạng Thái"
        };

        maKmTableModel = new DefaultTableModel(columnNames, 0);
        maKmTable = new JTable(maKmTableModel);
        maKmTable.setFillsViewportHeight(true);
        maKmTable.setRowHeight(28);
        maKmTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        maKmTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        maKmTable.getTableHeader().setBackground(new Color(240, 240, 240));
        maKmTable.getTableHeader().setForeground(Color.BLACK);

        // Căn giữa cột STT
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        maKmTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Căn phải cho cột "Giá trị"
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        maKmTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        // Hiệu ứng nền xen kẽ
        DefaultTableCellRenderer alternateRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                } else {
                    c.setBackground(new Color(184, 207, 229)); // màu khi chọn
                }
                return c;
            }
        };
        maKmTable.setDefaultRenderer(Object.class, alternateRenderer);

        // Tự động giãn cột
        maKmTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        
        // Sự kiện click chọn hóa đơn
        maKmTable.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseClicked(MouseEvent e) {
                int selectedRowIndex = maKmTable.getSelectedRow();
                if (selectedRowIndex != -1) {
                    int selectedMaKM = (int) maKmTable.getValueAt(selectedRowIndex, 1); // Ma KM
                    selectedKM = BUSManager.khuyenMaiBUS.getKhuyenMaiByMaKm(selectedMaKM);
                }
            }
        });

        return maKmTable;
    }

    private void loadData() {
        if (maKmTableModel == null) return;

        // Lấy danh sách khuyến mãi từ BUS
        listKM = BUSManager.khuyenMaiBUS.getListKhuyenMai();

        // Xóa dữ liệu cũ
        maKmTableModel.setRowCount(0);

        // Định dạng ngày (nếu cần)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        int stt = 1;
        for (KhuyenMaiDTO km : listKM) {
            Object[] row = {
                stt++,
                km.getMaKm(),
                km.getTenKm(),
                km.getLoaiKm(),
                String.format("%,.2f", km.getGiaTriKm()), // định dạng số đẹp hơn
                km.getDieuKienKm(),
                (km.getNgayBatDau() != null) ? sdf.format(km.getNgayBatDau()) : "",
                (km.getNgayKetThuc() != null) ? sdf.format(km.getNgayKetThuc()) : "",
                km.getTrangThai() > 0 ? "TRUE" : "FALSE"
            };
            maKmTableModel.addRow(row);
        }
    }

    private void setupListMaKM() {
        maKmTable = createMaKmTable();

        JScrollPane scrollPane = new JScrollPane(maKmTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        listMaKMPanel.setLayout(new BorderLayout());
        listMaKMPanel.removeAll();
        listMaKMPanel.add(scrollPane, BorderLayout.CENTER);

        // Nạp dữ liệu
        loadData();

        listMaKMPanel.revalidate();
        listMaKMPanel.repaint();
    }

    private void addEventForSearching(JTextField txtSearch, JTable table, ArrayList<KhuyenMaiDTO> originalList) {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            private void search() {
                String keyword = txtSearch.getText().trim().toLowerCase();

                // Nếu không nhập gì thì hiển thị toàn bộ danh sách
                if (keyword.isEmpty()) {
                    updateKhuyenMaiTable(originalList, table);
                    return;
                }

                // Lọc danh sách theo từ khóa (tìm theo tên, loại, giá trị, điều kiện, ngày...)
                ArrayList<KhuyenMaiDTO> filteredList = new ArrayList<>();
                for (KhuyenMaiDTO km : originalList) {
                    if (String.valueOf(km.getMaKm()).contains(keyword)
                        || (km.getTenKm() != null && km.getTenKm().toLowerCase().contains(keyword))
                        || (km.getLoaiKm() != null && km.getLoaiKm().toLowerCase().contains(keyword))
                        || (km.getGiaTriKm() != null && km.getGiaTriKm().toString().contains(keyword))
                        || (km.getDieuKienKm() != null && km.getDieuKienKm().toLowerCase().contains(keyword))
                        || (km.getNgayBatDau() != null && km.getNgayBatDau().toString().toLowerCase().contains(keyword))
                        || (km.getNgayKetThuc() != null && km.getNgayKetThuc().toString().toLowerCase().contains(keyword))
                        || String.valueOf(km.getTrangThai()).contains(keyword)) {
                        filteredList.add(km);
                    }
                }

                // Cập nhật lại bảng
                updateKhuyenMaiTable(filteredList, table);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { search(); }
            @Override
            public void removeUpdate(DocumentEvent e) { search(); }
            @Override
            public void changedUpdate(DocumentEvent e) { search(); }
        });
    }
    private void updateKhuyenMaiTable(ArrayList<KhuyenMaiDTO> list, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy"); // định dạng ngắn gọn

        for (KhuyenMaiDTO km : list) {
            int stt = 1;
            String ngayBatDauFormatted = "";
            String ngayKetThucFormatted = "";

            if (km.getNgayBatDau() != null) {
                ngayBatDauFormatted = dateFormat.format(km.getNgayBatDau());
            }
            if (km.getNgayKetThuc() != null) {
                ngayKetThucFormatted = dateFormat.format(km.getNgayKetThuc());
            }

            model.addRow(new Object[] {
                stt,
                km.getMaKm(),
                km.getTenKm(),
                km.getLoaiKm(),
                km.getGiaTriKm() != null ? km.getGiaTriKm() : "",
                km.getDieuKienKm(),
                ngayBatDauFormatted,
                ngayKetThucFormatted,
                km.getTrangThai()
            });
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

        jLabel2 = new javax.swing.JLabel();
        actionPanel = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        magnifyingGlassLabel = new javax.swing.JLabel();
        tfTimKiemKM = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        refreshLabel = new javax.swing.JLabel();
        listMaKMPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1350, 750));

        jLabel2.setBackground(new java.awt.Color(0, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("THÔNG TIN MÃ KM");
        jLabel2.setOpaque(true);

        jComboBox1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "A - Z", "Z - A", "Giá tăng" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        magnifyingGlassLabel.setText("jLabel8");

        tfTimKiemKM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfTimKiemKM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(magnifyingGlassLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfTimKiemKM, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(magnifyingGlassLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTimKiemKM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        addButton.setText("Thêm");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Xóa");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        editButton.setText("Sửa");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        refreshLabel.setText("jLabel8");

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 449, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(refreshLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout listMaKMPanelLayout = new javax.swing.GroupLayout(listMaKMPanel);
        listMaKMPanel.setLayout(listMaKMPanelLayout);
        listMaKMPanelLayout.setHorizontalGroup(
            listMaKMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1350, Short.MAX_VALUE)
        );
        listMaKMPanelLayout.setVerticalGroup(
            listMaKMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 648, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1350, Short.MAX_VALUE)
            .addComponent(listMaKMPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(actionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listMaKMPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed

        // Tạo và hiển thị dialog chỉnh sửa
        AddKMDialog dialog = new AddKMDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), selectedKM);
        dialog.setVisible(true);
        
        if (dialog.isSaved()){
            loadData();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        if (selectedKM == null) {
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
            boolean deleted = BUSManager.khuyenMaiBUS.deleteKhuyenMai(selectedKM.getMaKm());
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Xoá hóa đơn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                // refresh UI
                loadData(); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xoá hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
                // Tạo và hiển thị dialog chỉnh sửa
        EditKMDialog dialog = new EditKMDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), selectedKM);
        dialog.setVisible(true);
        
        if (dialog.isSaved()){
            loadData();
        }
    }//GEN-LAST:event_editButtonActionPerformed

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
            java.util.logging.Logger.getLogger(KhuyenMaiForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KhuyenMaiForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KhuyenMaiForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KhuyenMaiForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KhuyenMaiForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel listMaKMPanel;
    private javax.swing.JLabel magnifyingGlassLabel;
    private javax.swing.JLabel refreshLabel;
    private javax.swing.JTextField tfTimKiemKM;
    // End of variables declaration//GEN-END:variables
}
