/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import BUS.BUSManager;
import BUS.HoaDonBUS;
import DTO.DanhMucThuocDTO;
import DTO.HoaDonDTO;
import DTO.ThuocDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.IconUtils;

/**
 *
 * @author admin
 */
public class QuanLyHoaDonForm extends javax.swing.JFrame {

    private HoaDonDTO selectedHD;
    private ArrayList<HoaDonDTO> listHD = new ArrayList<HoaDonDTO>();
    
    private JTable listHDTable;
    private DefaultTableModel listHDTableModel;
    public QuanLyHoaDonForm() {
        initComponents();
        // DEBUG ONLY !!!
        BUSManager.initAllBUS();
        listHD = HoaDonBUS.getInstance().getListHoaDon();
        
        setupListHoaDon();
    }
    
    private JTable createHoaDonTable() {
        String[] columnNames = {
            "Mã HĐ", "Mã NV", "Mã KH", "Mã KM",
            "Tổng tiền", "Ngày xuất", "Phương thức TT", "Trạng thái"
        };

        listHDTableModel = new DefaultTableModel(columnNames, 0);
        listHDTable = new JTable(listHDTableModel);
        listHDTable.setFillsViewportHeight(true);
        listHDTable.setRowHeight(28);
        listHDTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listHDTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        listHDTable.getTableHeader().setBackground(new Color(240, 240, 240));
        listHDTable.getTableHeader().setForeground(Color.BLACK);

        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        listHDTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // mã HĐ
        listHDTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // mã NV
        listHDTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // mã KH
        listHDTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // mã KM
        listHDTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // trạng thái

        // Căn phải cho tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        listHDTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // tổng tiền

        // Hiệu ứng nền xen kẽ
        listHDTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        listHDTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRowIndex = listHDTable.getSelectedRow();
                if (selectedRowIndex != -1) {
                    int selectedMaHD = (int) listHDTable.getValueAt(selectedRowIndex, 0);
                    selectedHD = BUSManager.hoaDonBUS.getHoaDonByMaHd(selectedMaHD);

                }
            }
        });

    return listHDTable;
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
        pnlListHD = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1350, 750));
        setSize(new java.awt.Dimension(1350, 750));

        jLabel2.setBackground(new java.awt.Color(0, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("DANH SÁCH HÓA ĐƠN");
        jLabel2.setOpaque(true);

        buttonPanel.setPreferredSize(new java.awt.Dimension(630, 67));
        buttonPanel.setLayout(null);

        editButton.setText("Sửa");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(editButton);
        editButton.setBounds(111, 6, 94, 53);

        deleteButton.setText("Xóa");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(deleteButton);
        deleteButton.setBounds(211, 6, 94, 53);

        infoButton.setText("Chi tiết");
        infoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(infoButton);
        infoButton.setBounds(311, 6, 94, 53);

        importButton.setText("Nhập excel");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(importButton);
        importButton.setBounds(411, 6, 94, 53);

        exportButton.setText("Xuất excel");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(exportButton);
        exportButton.setBounds(511, 6, 94, 53);

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(991, 991, 991))
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 719, Short.MAX_VALUE)
                        .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlListHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlListHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        if (selectedHD == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo và hiển thị dialog chỉnh sửa
        EditHDDialog dialog = new EditHDDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), selectedHD);
        dialog.setVisible(true);

        // Sau khi dialog đóng, kiểm tra xem có lưu không
        if (dialog.isSaved()) {
            loadHoaDonData(); // reload lại bảng
        }
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
    }//GEN-LAST:event_infoButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_importButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportButtonActionPerformed

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
    private javax.swing.JPanel pnlListHD;
    // End of variables declaration//GEN-END:variables
}
