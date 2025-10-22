
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import DTO.ThuocDTO;
import BUS.BUSManager;

public class ThuocDialog extends JDialog {
    private JTextField txtMa, txtTen, txtGia, txtDonVi, txtMaDanhMuc;
    private JButton btnLuu, btnHuy;
    private ThuocDTO thuoc;

    public ThuocDialog(Frame parent, ThuocDTO thuoc) {
        super(parent, true);
        this.thuoc = thuoc;

        setTitle(thuoc == null ? "Thêm thuốc mới" : "Sửa thông tin thuốc");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Mã thuốc:"));
        txtMa = new JTextField();
        add(txtMa);

        add(new JLabel("Tên thuốc:"));
        txtTen = new JTextField();
        add(txtTen);

        add(new JLabel("Giá:"));
        txtGia = new JTextField();
        add(txtGia);

        add(new JLabel("Đơn vị:"));
        txtDonVi = new JTextField();
        add(txtDonVi);

        add(new JLabel("Mã danh mục:"));
        txtMaDanhMuc = new JTextField();
        add(txtMaDanhMuc);

        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        add(btnLuu);
        add(btnHuy);

        // if (thuoc != null) {
        // txtMa.setText(thuoc.getMaThuoc());
        // txtMa.setEnabled(false);
        // txtTen.setText(thuoc.getTenThuoc());
        // txtGia.setText(thuoc.getGia().toString());
        // txtDonVi.setText(thuoc.getDonVi());
        // txtMaDanhMuc.setText(thuoc.getMaDanhMuc());
        // }

        btnLuu.addActionListener(e -> luu());
        btnHuy.addActionListener(e -> dispose());
    }

    private void luu() {
        try {
            if (thuoc == null) {
                // ThuocDTO newThuoc = new ThuocDTO(
                // txtMa.getText(),
                // txtTen.getText(),
                // new BigDecimal(txtGia.getText()),
                // txtDonVi.getText(),
                // txtMaDanhMuc.getText());
                // BUSManager.thuocBUS.add(newThuoc);
                BUSManager.thuocBUS.addThuoc(new ThuocDTO(ERROR, ALLBITS, getName(), null, getName(), getTitle(),
                        getWarningString(), getName(), ABORT));
            } else {
                // thuoc.setTenThuoc(txtTen.getText());
                // thuoc.setGia(new BigDecimal(txtGia.getText()));
                // thuoc.setDonVi(txtDonVi.getText());
                // thuoc.setMaDanhMuc(txtMaDanhMuc.getText());
                // BUSManager.thuocBUS.update(thuoc);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}
