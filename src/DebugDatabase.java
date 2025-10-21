import java.util.ArrayList;
import java.util.Random;

import BUS.TaiKhoanBUS;
import DTO.TaiKhoanDTO;

public class DebugDatabase {
    // bus
    private TaiKhoanBUS taiKhoanBUS;

    public DebugDatabase() {

        // bus
        this.taiKhoanBUS = TaiKhoanBUS.getInstance();
    }

    public static void main(String[] args) {
        DebugDatabase store = new DebugDatabase();
        // store.deleteTaiKhoan(2);
        // store.addNewTaiKhoan();
        // store.showList();
        if (store.loginTaiKhoan("kh_vip", "khachhang") != null) {
            System.out.println("Đăng nhập thành công");
        } else {
            System.out.println("Đăng nhập thất bại");
        }

        // System.out.println(BCrypt.hashpw("admin", BCrypt.gensalt()));
        // System.out.println(BCrypt.hashpw("123", BCrypt.gensalt()));
        // System.out.println(BCrypt.hashpw("khachhang", BCrypt.gensalt()));
    }

    private static <T> void printList(ArrayList<T> list) {
        for (T t : list) {
            System.out.println(t);
        }
    }

    private void deleteTaiKhoan(int id) {
        this.taiKhoanBUS.deleteTaiKhoan(id);
    }

    private void addNewTaiKhoan() {
        int random = new Random().nextInt(1000);
        this.taiKhoanBUS
                .addTaiKhoan(new TaiKhoanDTO(random, String.valueOf(random), "abcd", "ten", "88753", "admin", 1));
    }

    private TaiKhoanDTO loginTaiKhoan(String taiKhoan, String matKhau) {
        return this.taiKhoanBUS.login(taiKhoan, matKhau);
    }

    private void showList() {
        ArrayList<TaiKhoanDTO> taiKhoanDTO = taiKhoanBUS.getListTaiKhoan();
        for (TaiKhoanDTO taikhoan : taiKhoanDTO) {
            System.out.println(taikhoan);
        }
    }
}
