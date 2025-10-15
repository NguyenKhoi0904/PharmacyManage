-- Database : Pharmacy Store
DROP DATABASE IF EXISTS pharmacy_store;
CREATE DATABASE pharmacy_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pharmacy_store;

-- ========== TẠO BẢNG ==========

-- 1. Tài Khoản
CREATE TABLE taikhoan (
    ma_tk INT PRIMARY KEY,
    tai_khoan VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(100) NOT NULL,
    ten VARCHAR(100),
    sdt VARCHAR(15),
    vai_tro ENUM('admin', 'nhanvien', 'khachhang') NOT NULL,
    trang_thai INT(2) NOT NULL
);

-- 2. Nhân Viên
CREATE TABLE nhanvien (
    ma_nv INT PRIMARY KEY,
    ma_tk INT,
    ngay_vao_lam DATE NOT NULL,
    luong DECIMAL(10,2),
    email VARCHAR(100),
    dia_chi VARCHAR(100),
    gioi_tinh ENUM('Nam','Nu','Khac'),
    ngay_sinh DATE,
    vi_tri VARCHAR(50),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_tk) REFERENCES taikhoan(ma_tk)
);

-- 3. Khách Hàng
CREATE TABLE khachhang (
    ma_kh INT PRIMARY KEY,
    ma_tk INT,
    ngay_dang_ky DATE NOT NULL,
    diem_tich_luy INT DEFAULT 0,
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_tk) REFERENCES taikhoan(ma_tk)
);

-- 4. Nhà Cung Cấp
CREATE TABLE nhacungcap (
    ma_ncc INT PRIMARY KEY,
    ten_ncc VARCHAR(100),
    sdt_ncc VARCHAR(20),
    dia_chi VARCHAR(100),
    email_ncc VARCHAR(100),
    trang_thai INT(2) NOT NULL
);

-- 5. Phiếu Nhập
CREATE TABLE phieunhap (
    ma_pn INT PRIMARY KEY,
    ma_nv INT,
    thoi_gian_nhap DATETIME NOT NULL,
    dia_diem VARCHAR(100),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_nv) REFERENCES nhanvien(ma_nv)
);

-- 6. Lô Hàng
CREATE TABLE lohang (
    ma_lh INT PRIMARY KEY,
    ma_ncc INT,
    sl_nhap INT,
    sl_ton INT,
    ngay_sx DATE NOT NULL,
    han_sd DATE NOT NULL,
    gia_nhap DECIMAL(10,2),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_ncc) REFERENCES nhacungcap(ma_ncc)
);

-- 7. Chi Tiết Phiếu Nhập (bảng trung gian)
CREATE TABLE chitiet_pn (
    ma_pn INT PRIMARY KEY,
    ma_lh INT,
    don_gia DECIMAL(10,2),
    so_luong INT, 
    FOREIGN KEY (ma_pn) REFERENCES phieunhap(ma_pn),
    FOREIGN KEY (ma_lh) REFERENCES lohang(ma_lh)
);

-- 8. Danh Mục Thuốc
CREATE TABLE danhmucthuoc (
    ma_dmt INT PRIMARY KEY,
    ten_dmt VARCHAR(100),
    trang_thai INT(2) NOT NULL
);

-- 9. Thuốc
CREATE TABLE thuoc (
    ma_thuoc INT PRIMARY KEY,
    ma_dmt INT,
    ten_thuoc VARCHAR(100),
    gia DECIMAL(10,2),
    don_vi_tinh VARCHAR(20),
    nha_san_xuat VARCHAR(100),
    xuat_xu VARCHAR(100),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_dmt) REFERENCES danhmucthuoc(ma_dmt)
);

-- 10. Khuyến Mãi
CREATE TABLE khuyenmai (
    ma_km INT PRIMARY KEY,
    ten_km VARCHAR(100),
    loai_km VARCHAR(50),
    gia_tri_km DECIMAL(10,2),
    dieu_kien_km VARCHAR(100),
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    hieu_luc BOOLEAN,
    trang_thai INT(2) NOT NULL
);

-- 11. Hoá Đơn
CREATE TABLE hoadon (
    ma_hd INT PRIMARY KEY,
    ma_kh INT,
    ma_km INT NULL,
    tong_tien DECIMAL(10,2),
    ngay_xuat DATE NOT NULL,
    phuong_thuc_tt VARCHAR(50),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_kh) REFERENCES khachhang(ma_kh),
    FOREIGN KEY (ma_km) REFERENCES khuyenmai(ma_km)
);

-- 12. Chi Tiết Hoá Đơn
CREATE TABLE chitiet_hd (
    ma_hd INT PRIMARY KEY,
    ma_thuoc INT,
    don_gia DECIMAL(10,2),
    so_luong INT,
    FOREIGN KEY (ma_hd) REFERENCES hoadon(ma_hd),
    FOREIGN KEY (ma_thuoc) REFERENCES thuoc(ma_thuoc)
);

-- ========== THÊM DATA MẪU ==========

-- 1. Tài Khoản (ma_tk: 1, 2, 3)
INSERT INTO taikhoan (ma_tk, tai_khoan, mat_khau, ten, sdt, vai_tro, trang_thai) VALUES
(1, 'admin01', 'e10adc3949ba59abbe56e057f20f883e', 'Nguyễn Văn A', '0901234567', 'admin',1),
(2, 'nv_quyen', 'e10adc3949ba59abbe56e057f20f883e', 'Trần Thị B', '0907654321', 'nhanvien',1),
(3, 'kh_vip', 'e10adc3949ba59abbe56e057f20f883e', 'Lê Văn C', '0912345678', 'khachhang',1);

-- 4. Nhà Cung Cấp (ma_ncc: 1, 2)
INSERT INTO nhacungcap (ma_ncc, ten_ncc, sdt_ncc, dia_chi, email_ncc, trang_thai) VALUES
(101, 'Dược phẩm Tâm Bình', '02438685110', 'Hà Nội', 'info@tambinh.vn',1),
(102, 'Dược Hậu Giang', '02923891433', 'Cần Thơ', 'dhg@dhgpharma.com.vn',1);

-- 8. Danh Mục Thuốc (ma_dmt: 1, 2)
INSERT INTO danhmucthuoc (ma_dmt, ten_dmt, trang_thai) VALUES
(201, 'Thuốc cảm cúm & Hạ sốt',1),
(202, 'Kháng sinh & Kháng viêm',1);

-- 10. Khuyến Mãi (ma_km: 1, 2)
INSERT INTO khuyenmai (ma_km, ten_km, loai_km, gia_tri_km, dieu_kien_km, ngay_bat_dau, ngay_ket_thuc, hieu_luc, trang_thai) VALUES
(301, 'Giảm 10% cho khách VIP', 'Phần trăm', 10.00, 'Khách hàng có 100 điểm tích lũy', '2025-10-01', '2025-12-31', TRUE, 1),
(302, 'Mua 3 tặng 1', 'Sản phẩm', 0.00, 'Mua 3 viên Panadol', '2025-11-01', '2025-11-30', TRUE, 1);

-- 2. Nhân Viên (ma_nv: 1, 2. Dùng ma_tk: 1, 2)
INSERT INTO nhanvien (ma_nv, ma_tk, ngay_vao_lam, luong, email, dia_chi, gioi_tinh, ngay_sinh, vi_tri, trang_thai) VALUES
(11, 1, '2023-01-15', 25000000.00, 'admin@store.com', 'Quận 1, TP.HCM', 'Nam', '1990-05-10', 'Quản lý',1),
(12, 2, '2024-03-01', 12000000.00, 'quyen@store.com', 'Quận 3, TP.HCM', 'Nu', '1998-11-20', 'Nhân viên bán hàng', 1);

-- 3. Khách Hàng (ma_kh: 1, 2. Dùng ma_tk: 3)
INSERT INTO khachhang (ma_kh, ma_tk, ngay_dang_ky, diem_tich_luy, trang_thai) VALUES
(21, 3, '2024-05-20', 150, 1),  -- Khách VIP (có tài khoản)
(22, NULL, '2024-10-07', 0, 1); -- Khách vãng lai (không có tài khoản)

-- 5. Phiếu Nhập (ma_pn: 1, 2. Dùng ma_nv: 11, 12)
INSERT INTO phieunhap (ma_pn, ma_nv, thoi_gian_nhap, dia_diem, trang_thai) VALUES
(41, 11, '2025-10-08 09:30:00', 'Kho chính TPHCM', 1),
(42, 12, '2025-10-07 15:45:00', 'Chi nhánh Hà Nội', 1);

-- 6. Lô Hàng (ma_lh: 1, 2, 3. Dùng ma_ncc: 101, 102)
INSERT INTO lohang (ma_lh, ma_ncc, sl_nhap, sl_ton, ngay_sx, han_sd, gia_nhap, trang_thai) VALUES
(51, 101, 1000, 500, '2024-01-15', '2026-01-15', 12000.00, 1),
(52, 102, 500, 450, '2024-08-10', '2025-12-31', 95000.00, 1),
(53, 101, 800, 800, '2025-05-01', '2027-05-01', 8000.00, 1);

-- 9. Thuốc (ma_thuoc: 1, 2. Dùng ma_dmt: 201, 202)
INSERT INTO thuoc (ma_thuoc, ma_dmt, ten_thuoc, gia, don_vi_tinh, nha_san_xuat, xuat_xu, trang_thai) VALUES
(61, 201, 'Panadol Cold & Flu', 25000.00, 'Vỉ (10 viên)', 'GSK', 'Anh', 1),
(62, 202, 'Amoxicillin 500mg', 15000.00, 'Hộp (20 viên)', 'DHG Pharma', 'Việt Nam', 1);

-- 7. Chi Tiết Phiếu Nhập (Dùng ma_pn: 41, 42 và ma_lh: 51, 52, 53)
INSERT INTO chitiet_pn (ma_pn, ma_lh, don_gia, so_luong) VALUES
(41, 52, 95000.00, 100),
(42, 53, 8000.00, 300);

-- 11. Hoá Đơn (ma_hd: 1, 2. Dùng ma_kh: 21, 22 và ma_km: 301)
INSERT INTO hoadon (ma_hd, ma_kh, ma_km, tong_tien, ngay_xuat, phuong_thuc_tt, trang_thai) VALUES
(71, 21, 301, 120000.00, '2025-10-08', 'Chuyển khoản', 1),
(72, 22, NULL, 50000.00, '2025-10-08', 'Tiền mặt', 1);

-- 12. Chi Tiết Hoá Đơn (Dùng ma_hd: 71, 72 và ma_thuoc: 61, 62)
INSERT INTO chitiet_hd (ma_hd, ma_thuoc, don_gia, so_luong) VALUES
(71, 61, 25000.00, 4),
(72, 61, 25000.00, 2);