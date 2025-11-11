Cấu trúc folder src:
src
 └── ├── DTO           # Chứa các class entity (theo ERD & class diagram)
      │    ├── TaiKhoan.java
      │    ├── KhachHang.java
      │    ├── NhanVien.java
      │    ├── Thuoc.java
      │    ├── DanhMucThuoc.java
      │    ├── LoHang.java
      │    ├── NhaCungCap.java
      │    ├── HoaDon.java
      │    ├── ChiTietHoaDon.java
      │    ├── PhieuNhap.java
      │    ├── ChiTietPhieuNhap.java
      │    └── .....
      │
      ├── DAO             # Data Access Object (CRUD với DB)
      │    ├── TaiKhoanDAO.java
      │    ├── ThuocDAO.java
      │    ├── HoaDonDAO.java
      │    ├── PhieuNhapDAO.java
      │    ├── KhachHangDAO.java
      │    └── ...
      │
      ├── BUS         # Chứa business logic
      │    ├── TaiKhoanBUS.java
      │    ├── HoaDonBUS.java     # Tính toán tổng tiền, áp dụng khuyến mãi
      │    ├── ThuocBUS.java      # Quản lý tồn kho
      │    ├── PhieuNhapBUS.java
      │    └── ...
      │
      ├── util            # Các tiện ích (helper class)
      │    ├── BigDecimalUtils.java      # Kết nối database
      │    ├── Convert_JFrame_To_JPanel.java
      │    └── DateChooser.java
      │
      ├── View            # Giao diện 
      │    ├── LoginFrame.java
      │    ├── MainFrame.java
      │    ├── HoaDonPanel.java
      │    ├── ThuocPanel.java
      │    └── ...
      │
      └── PharmacyManage.java        # Điểm khởi động chương trình
      
