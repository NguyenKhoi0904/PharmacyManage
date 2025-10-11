import java.util.ArrayList;

import DAO.ChiTietHdDAO;
import DAO.ChiTietPnDAO;
import DAO.DAOinterface;
import DAO.DanhMucThuocDAO;
import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import DAO.KhuyenMaiDAO;
import DAO.LoHangDAO;
import DAO.NhaCungCapDAO;
import DAO.NhanVienDAO;
import DAO.PhieuNhapDAO;
import DAO.TaiKhoanDAO;
import DAO.ThuocDAO;
import DTO.ChiTietHdDTO;
import DTO.ChiTietPnDTO;
import DTO.DanhMucThuocDTO;
import DTO.HoaDonDTO;
import DTO.KhachHangDTO;
import DTO.KhuyenMaiDTO;
import DTO.LoHangDTO;
import DTO.NhaCungCapDTO;
import DTO.NhanVienDTO;
import DTO.PhieuNhapDTO;
import DTO.TaiKhoanDTO;
import DTO.ThuocDTO;

public class DebugDatabase {
    private ArrayList<TaiKhoanDTO> listTaiKhoan;
    private ArrayList<ThuocDTO> listThuoc;
    private ArrayList<PhieuNhapDTO> listPhieuNhap;
    private ArrayList<NhanVienDTO> listNhanVien;
    private ArrayList<NhaCungCapDTO> listNhaCungCap;
    private ArrayList<LoHangDTO> listLoHang;
    private ArrayList<KhuyenMaiDTO> listKhuyenMai;
    private ArrayList<KhachHangDTO> listKhachHang;
    private ArrayList<HoaDonDTO> listHoaDon;
    private ArrayList<DanhMucThuocDTO> listDanhMucThuoc;
    private ArrayList<ChiTietPnDTO> listChiTietPn;
    private ArrayList<ChiTietHdDTO> listChiTietHd;

    public DebugDatabase() {
        this.listTaiKhoan = new TaiKhoanDAO().selectAll();
        this.listThuoc = new ThuocDAO().selectAll();
        this.listPhieuNhap = new PhieuNhapDAO().selectAll();
        this.listNhanVien = new NhanVienDAO().selectAll();
        this.listNhaCungCap = new NhaCungCapDAO().selectAll();
        this.listLoHang = new LoHangDAO().selectAll();
        this.listKhuyenMai = new KhuyenMaiDAO().selectAll();
        this.listKhachHang = new KhachHangDAO().selectAll();
        this.listHoaDon = new HoaDonDAO().selectAll();
        this.listDanhMucThuoc = new DanhMucThuocDAO().selectAll();
        this.listChiTietPn = new ChiTietPnDAO().selectAll();
        this.listChiTietHd = new ChiTietHdDAO().selectAll();
    }

    public static void main(String[] args) {
        DebugDatabase store = new DebugDatabase();
        store.showList();
    }

    public static <T> void printList(ArrayList<T> list) {
        for (T t : list) {
            System.out.println(t);
        }
    }

    private void showList() {
        printList(this.listTaiKhoan);
        printList(this.listThuoc);
        printList(this.listPhieuNhap);
        printList(this.listNhanVien);
        printList(this.listNhaCungCap);
        printList(this.listLoHang);
        printList(this.listKhuyenMai);
        printList(this.listKhachHang);
        printList(this.listHoaDon);
        printList(this.listDanhMucThuoc);
        printList(this.listChiTietPn);
        printList(this.listChiTietHd);
    }
}
