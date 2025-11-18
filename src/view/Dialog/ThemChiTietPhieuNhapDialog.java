package view.Dialog;

import javax.swing.*;

import BUS.BUSManager;
import DTO.ChiTietPnDTO;
import DTO.LoHangDTO;
import test.NumberOnlyField;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ThemChiTietPhieuNhapDialog extends JDialog {

    private JLabel lblMaPn;
    private JComboBox<LoHangDTO> cboMaLh;
    private NumberOnlyField txtDonGia;
    private NumberOnlyField txtSoLuong;

    private ChiTietPnDTO result = null; // <= dữ liệu trả về

    public ThemChiTietPhieuNhapDialog(JDialog parent, String maPn) {
        super(parent, "Thêm Chi Tiết Phiếu Nhập", true);
        // ArrayList<LoHangDTO> listLoHang = BUSManager.loHangBUS.getListLoHang();
        setSize(750, 300);
        setLocationRelativeTo(parent);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã phiếu nhập
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Mã Phiếu Nhập:"), gbc);

        lblMaPn = new JLabel(maPn);
        gbc.gridx = 1;
        add(lblMaPn, gbc);

        // Mã lô hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Mã Lô Hàng:"), gbc);

        cboMaLh = new JComboBox<>();
        loadLoHang();
        gbc.gridx = 1;
        add(cboMaLh, gbc);

        // Đơn giá
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Đơn Giá:"), gbc);

        txtDonGia = new NumberOnlyField();
        gbc.gridx = 1;
        add(txtDonGia, gbc);

        // Số lượng
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Số Lượng:"), gbc);

        txtSoLuong = new NumberOnlyField();
        gbc.gridx = 1;
        add(txtSoLuong, gbc);

        // Nút xác nhận
        JButton btnOk = new JButton("Xác Nhận");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(btnOk, gbc);

        btnOk.addActionListener(e -> onSubmit());
    }

    private void onSubmit() {
        try {
            String maPn = lblMaPn.getText();
            LoHangDTO lh = (LoHangDTO) cboMaLh.getSelectedItem();
            double donGia = Double.parseDouble(txtDonGia.getText());
            int soLuong = Integer.parseInt(txtSoLuong.getText());

            if (donGia <= 0 || soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá / Số lượng không hợp lệ!");
                return;
            }

            result = new ChiTietPnDTO(Integer.parseInt(maPn), lh.getMaLh(), new BigDecimal(donGia), soLuong);

            dispose(); // đóng dialog

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số!");
        }
    }

    private void loadLoHang() {
        ArrayList<LoHangDTO> dsLoHang = BUSManager.loHangBUS.getListLoHang();
        cboMaLh.removeAllItems();
        for (LoHangDTO lh : dsLoHang) {
            cboMaLh.addItem(lh);
        }
    }

    public ChiTietPnDTO getResult() {
        return result;
    }
}
