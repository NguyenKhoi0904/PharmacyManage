package view;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.table.*;

public class NhaCungCapForm extends JFrame {

    private JTextField txtMaNCC, txtTenNCC, txtDiaChi, txtSoDT, txtEmail, txtTimKiem;
    private JTable table;
    private JButton btnThem, btnXoa, btnSua, btnLamMoi;

    private NhaCungCapBUS nccBUS = NhaCungCapBUS.getInstance();

    public NhaCungCapForm() {
        setTitle("Quản Lý Nhà Cung Cấp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 800);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 255, 255));
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font lblFont = new Font("Segoe UI", Font.PLAIN, 18);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel lblMaNCC = new JLabel("Mã NCC:");
        lblMaNCC.setFont(lblFont);
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(lblMaNCC, gbc);

        txtMaNCC = new JTextField(15);
        txtMaNCC.setFont(txtFont);
        txtMaNCC.setEditable(false); // user không sửa mã
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(txtMaNCC, gbc);

        JLabel lblTenNCC = new JLabel("Tên NCC:");
        lblTenNCC.setFont(lblFont);
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(lblTenNCC, gbc);

        txtTenNCC = new JTextField(15);
        txtTenNCC.setFont(txtFont);
        gbc.gridx = 3; gbc.gridy = 0;
        inputPanel.add(txtTenNCC, gbc);

        JLabel lblSoDT = new JLabel("Số điện thoại:");
        lblSoDT.setFont(lblFont);
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(lblSoDT, gbc);

        txtSoDT = new JTextField(15);
        txtSoDT.setFont(txtFont);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(txtSoDT, gbc);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(lblFont);
        gbc.gridx = 2; gbc.gridy = 1;
        inputPanel.add(lblEmail, gbc);

        txtEmail = new JTextField(15);
        txtEmail.setFont(txtFont);
        gbc.gridx = 3; gbc.gridy = 1;
        inputPanel.add(txtEmail, gbc);

        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(lblFont);
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(lblDiaChi, gbc);

        txtDiaChi = new JTextField(35);
        txtDiaChi.setFont(txtFont);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 3;
        inputPanel.add(txtDiaChi, gbc);
        gbc.gridwidth = 1;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnThem = createStyledButton("Thêm");
        btnXoa = createStyledButton1("Xóa");
        btnSua = createStyledButton2("Sửa");
        btnLamMoi = createStyledButton3("Làm Mới");
        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtTimKiem.putClientProperty(
            FlatClientProperties.TEXT_FIELD_LEADING_ICON,
            new FlatSVGIcon("image/search.svg", 20, 20)
        );
        txtTimKiem.putClientProperty(
            FlatClientProperties.PLACEHOLDER_TEXT,
            "Tìm kiếm ..."
        );
        txtTimKiem.putClientProperty(
            FlatClientProperties.STYLE,
            "arc: 8;"
        );

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(txtTimKiem);

        JPanel topCenterPanel = new JPanel(new BorderLayout());
        topCenterPanel.add(inputPanel, BorderLayout.CENTER);
        topCenterPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.add(topCenterPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        String[] columnNames = {"Mã NCC", "Tên NCC", "Số điện thoại", "Email", "Địa chỉ"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhà cung cấp"));
        add(scrollPane, BorderLayout.SOUTH);

        addEventHandlers();
        loadTableNCC();
        findRealTime();
        autoGenerateMaNCC();
    }

    private void autoGenerateMaNCC() {
        txtMaNCC.setText(String.valueOf(nccBUS.generate_maNCC()));
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(76, 175, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFocusPainted(false);
        return btn;
    }
    private JButton createStyledButton1(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(244, 67, 54));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFocusPainted(false);
        return btn;
    }
    private JButton createStyledButton2(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(255, 152, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFocusPainted(false);
        return btn;
    }
    private JButton createStyledButton3(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(25, 118, 210));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFocusPainted(false);
        return btn;
    }

    private void addEventHandlers() {

        btnThem.addActionListener(e -> {
            try {
                int ma = nccBUS.generate_maNCC(); // tự tạo mã
                NhaCungCapDTO ncc = new NhaCungCapDTO(ma, txtTenNCC.getText(), txtSoDT.getText(),
                        txtDiaChi.getText(), txtEmail.getText(), 1);

                if (nccBUS.addNhaCungCap(ncc)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadTableNCC();
                    autoGenerateMaNCC();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        });

        btnSua.addActionListener(e -> {
            try {
                int ma = Integer.parseInt(txtMaNCC.getText());
                NhaCungCapDTO ncc = new NhaCungCapDTO(ma, txtTenNCC.getText(), txtSoDT.getText(),
                        txtDiaChi.getText(), txtEmail.getText(), 1);

                if (nccBUS.updateNhaCungCap(ncc)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadTableNCC();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        });

        btnXoa.addActionListener(e -> {
            try {
                int ma = Integer.parseInt(txtMaNCC.getText());
                if (nccBUS.deleteNhaCungCap(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadTableNCC();
                    autoGenerateMaNCC();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Chọn mã nhà cung cấp hợp lệ!");
            }
        });

        btnLamMoi.addActionListener(e -> {
            txtTenNCC.setText("");
            txtSoDT.setText("");
            txtEmail.setText("");
            txtDiaChi.setText("");
            txtTimKiem.setText("");
            autoGenerateMaNCC();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMaNCC.setText(table.getValueAt(row, 0).toString());
                txtTenNCC.setText(table.getValueAt(row, 1).toString());
                txtSoDT.setText(table.getValueAt(row, 2).toString());
                txtEmail.setText(table.getValueAt(row, 3).toString());
                txtDiaChi.setText(table.getValueAt(row, 4).toString());
            }
        });
    }

    private void loadTableNCC() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (NhaCungCapDTO ncc : nccBUS.getListNhaCungCap()) {
            model.addRow(new Object[]{
                    ncc.getMaNcc(),
                    ncc.getTenNcc(),
                    ncc.getSdtNcc(),
                    ncc.getEmailNcc(),
                    ncc.getDiaChi()
            });
        }
    }
    
    private void findRealTime(){
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        TableRowSorter<DefaultTableModel> sorted = new TableRowSorter<>(model);
        table.setRowSorter(sorted);
        
        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filters();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filters();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filters();
            }
            
            private void filters(){
                String keyword = txtTimKiem.getText();
                if(keyword.isEmpty()){
                    sorted.setRowFilter(null);
                }else{
                    java.util.List<RowFilter<Object, Object>> filter = new ArrayList<>();
                    filter.add(RowFilter.regexFilter("(?i)" + keyword, 0));
                    filter.add(RowFilter.regexFilter("(?i)" + keyword, 1));
                    filter.add(RowFilter.regexFilter("(?i)" + keyword, 2));
                    filter.add(RowFilter.regexFilter("(?i)" + keyword, 3));
                    filter.add(RowFilter.regexFilter("(?i)" + keyword, 4));
                    sorted.setRowFilter(RowFilter.orFilter(filter));
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NhaCungCapForm());
    }
}
