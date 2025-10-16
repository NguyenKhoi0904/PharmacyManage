package BUS;

import java.util.ArrayList;

import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;

public class NhaCungCapBUS {
    private ArrayList<NhaCungCapDTO> listNhaCungCap;
    private final NhaCungCapDAO nhaCungCapDAO;

    public NhaCungCapBUS() {
        this.nhaCungCapDAO = NhaCungCapDAO.getInstance();
        this.listNhaCungCap = this.nhaCungCapDAO.selectAll();
    }

    // ========== DATABASE HANDLE ==========
    public boolean addNhaCungCap(NhaCungCapDTO nhaCungCapDTO) {
        // kiểm tra nếu email nhà cung cấp sai định dạng đuôi
        if (!nhaCungCapDTO.getEmailNcc().contains("@email.com") &&
                !nhaCungCapDTO.getEmailNcc().contains("@gmail.com")) {
            System.out.println("lỗi hàm addNhaCungCap sai định dạng email");
            return false;
        }

        // kiểm tra xem nếu số điện thoại sai định dạng
        if (nhaCungCapDTO.getSdtNcc().length() > 11 ||
                nhaCungCapDTO.getSdtNcc().length() < 10) {
            System.out.println("lỗi hàm addNhaCungCap sai định dạng sdt");
            return false;
        }

        // thêm vào db + list
        if (this.nhaCungCapDAO.insert(nhaCungCapDTO) > 0) {
            this.listNhaCungCap.add(nhaCungCapDTO);
            return true;
        }
        System.out.println("lỗi hàm addNhaCungCap mục <this.nhaCungCapDAO.insert>");
        return false;
    }

    public boolean updateNhaCungCap(int ma_ncc, NhaCungCapDTO nhaCungCapDTO) {
        if (this.nhaCungCapDAO.update(nhaCungCapDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listNhaCungCap.size(); i++) {
                if (this.listNhaCungCap.get(i).getMaNcc() == ma_ncc) {
                    this.listNhaCungCap.set(i, nhaCungCapDTO);
                    break;
                }
            }
            return true;
        }
        System.out.println("Lỗi CSDL: Không thể cập nhật nhà cung cấp.");
        return false;
    }

    public boolean deleteNhaCungCap(int ma_ncc) {
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
        System.out.println("lỗi hàm deleteNhaCungCap: Không tìm thấy nhà cung cấp hoặc lỗi CSDL.");
        return false;
    }

    // ========== BUSINESS LOGIC ==========

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
}
