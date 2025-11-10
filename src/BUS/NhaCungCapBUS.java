package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.LoHangDAO;
import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;

public class NhaCungCapBUS {

    // singleton instance
    private static NhaCungCapBUS instance;

    private ArrayList<NhaCungCapDTO> listNhaCungCap;
    private final NhaCungCapDAO nhaCungCapDAO;

    private NhaCungCapBUS() {
        this.nhaCungCapDAO = NhaCungCapDAO.getInstance();
        this.listNhaCungCap = this.nhaCungCapDAO.selectAll();
    }

    // singleton init
    public static NhaCungCapBUS getInstance() {
        if (instance == null) {
            instance = new NhaCungCapBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addNhaCungCap(NhaCungCapDTO nhaCungCapDTO) {
        // kiểm tra nếu mã ncc đã tồn tại
        if (checkIfMaNccExist(nhaCungCapDTO.getMaNcc())) {
            JOptionPane.showMessageDialog(null, "Mã nhà cung cấp đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu email nhà cung cấp sai định dạng đuôi
        if (!this.checkIfEmailValidate(nhaCungCapDTO)) {
            return false;
        }

        // kiểm tra xem nếu số điện thoại sai định dạng
        if (this.isValidPhone(nhaCungCapDTO.getSdtNcc())) {
            JOptionPane.showMessageDialog(null, "lỗi hàm addNhaCungCap sai định dạng sdt", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.nhaCungCapDAO.insert(nhaCungCapDTO) > 0) {
            this.listNhaCungCap.add(nhaCungCapDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm addNhaCungCap mục <this.nhaCungCapDAO.insert>", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateNhaCungCap(NhaCungCapDTO nhaCungCapDTO) {
        // kiểm tra nếu mã ncc không tồn tại
        if (!checkIfMaNccExist(nhaCungCapDTO.getMaNcc())) {
            JOptionPane.showMessageDialog(null, "Mã nhà cung cấp không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu email nhà cung cấp sai định dạng đuôi
        if (!this.checkIfEmailValidate(nhaCungCapDTO)) {
            return false;
        }

        // kiểm tra xem nếu số điện thoại sai định dạng
        if (this.isValidPhone(nhaCungCapDTO.getSdtNcc())) {
            JOptionPane.showMessageDialog(null, "lỗi hàm addNhaCungCap sai định dạng sdt", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.nhaCungCapDAO.update(nhaCungCapDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listNhaCungCap.size(); i++) {
                if (this.listNhaCungCap.get(i).getMaNcc() == nhaCungCapDTO.getMaNcc()) {
                    this.listNhaCungCap.set(i, nhaCungCapDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật nhà cung cấp.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteNhaCungCap(int ma_ncc) {
        // kiểm tra nếu vẫn còn lô hàng tồn tại -> không cho xoá
        if (this.checkIfLoHangExist(ma_ncc)) {
            return false;
        }

        if (this.nhaCungCapDAO.deleteById(String.valueOf(ma_ncc)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listNhaCungCap.size(); i++) {
                if (this.listNhaCungCap.get(i).getMaNcc() == ma_ncc) {
                    this.listNhaCungCap.remove(i);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm deleteNhaCungCap: Không tìm thấy nhà cung cấp hoặc lỗi CSDL.",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public boolean checkIfMaNccExist(int ma_ncc) {
        if (this.getMapByMaNcc().containsKey(ma_ncc)) {
            // nếu mã nhà cung cấp của nhà cung cấp đã tồn tại trong bảng nhacungcap
            return true;
        }
        return false;
    }

    private boolean checkIfLoHangExist(int ma_ncc) {
        if (LoHangDAO.getInstance().selectAllByMaNcc(ma_ncc).isEmpty()) {
            return false;
        }
        JOptionPane.showMessageDialog(null, "Không thể xoá nhà cung cấp khi vẫn còn lô hàng được chỉ đến", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return true;
    }

    private boolean checkIfEmailValidate(NhaCungCapDTO nhaCungCapDTO) {
        if (!nhaCungCapDTO.getEmailNcc().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null, "Email nhà cung cấp không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isValidPhone(String sdt) {
        return sdt.matches("^0\\d{9,10}$");
    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<NhaCungCapDTO> getListNhaCungCap() {
        return this.listNhaCungCap;
    }

    public NhaCungCapDTO getNhaCungCapByMaNcc(int ma_ncc) {
        for (NhaCungCapDTO ncc : this.listNhaCungCap) {
            if (ncc.getMaNcc() == ma_ncc) {
                return ncc;
            }
        }
        return null;

    }

    /**
     * 
     * @return HashMap&lt;NhaCungCapDTO.getMaNcc,NhaCungCapDTO&gt;
     */
    public HashMap<Integer, NhaCungCapDTO> getMapByMaNcc() {
        HashMap<Integer, NhaCungCapDTO> mapMaNcc = new HashMap<Integer, NhaCungCapDTO>();
        for (NhaCungCapDTO ncc : this.listNhaCungCap) {
            mapMaNcc.put(ncc.getMaNcc(), ncc);
        }
        return mapMaNcc;
    }
    
    public int generate_maNCC(){
        int max = 0;
        for (NhaCungCapDTO ncc: listNhaCungCap){
            if(ncc.getMaNcc()> max) max = ncc.getMaNcc();
        }
        return max+1;        
    }
    
}
