Cấu trúc folder src:
src
 └── ├── entities           # Chứa các class entity (theo ERD & class diagram)
      │    ├── User.java
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
      │    ├── KhuyenMai.java
      │    └── HoaDonKhuyenMai.java   # Nếu bạn muốn tách bảng trung gian
      │
      ├── dao             # Data Access Object (CRUD với DB)
      │    ├── UserDAO.java
      │    ├── ThuocDAO.java
      │    ├── HoaDonDAO.java
      │    ├── PhieuNhapDAO.java
      │    └── ...
      │
      ├── service         # Chứa business logic
      │    ├── UserService.java
      │    ├── HoaDonService.java     # Tính toán tổng tiền, áp dụng khuyến mãi
      │    ├── ThuocService.java      # Quản lý tồn kho
      │    ├── PhieuNhapService.java
      │    └── ...
      │
      ├── util            # Các tiện ích (helper class)
      │    ├── DBConnection.java      # Kết nối database
      │    ├── DateUtils.java
      │    └── Validator.java
      │
      ├── ui            # Giao diện (nếu bạn dùng SwingJavaFX)
      │    ├── LoginFrame.java
      │    ├── MainFrame.java
      │    ├── HoaDonPanel.java
      │    ├── ThuocPanel.java
      │    └── ...
      │
      └── Main.java        # Điểm khởi động chương trình
      
