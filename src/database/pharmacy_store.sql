-- Database : Pharmacy Store
DROP DATABASE IF EXISTS pharmacy_store;
CREATE DATABASE pharmacy_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pharmacy_store;

-- ========== TẠO BẢNG ==========

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
    url_anh VARCHAR(1000),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_dmt) REFERENCES danhmucthuoc(ma_dmt)
);

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
    gioi_tinh ENUM('Nam','Nữ','Khac'),
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
    ma_thuoc INT,
    sl_nhap INT,
    sl_ton INT,
    ngay_sx DATE NOT NULL,
    han_sd DATE NOT NULL,
    gia_nhap DECIMAL(10,2),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_ncc) REFERENCES nhacungcap(ma_ncc),
    FOREIGN KEY (ma_thuoc) REFERENCES thuoc(ma_thuoc)
);

-- 7. Chi Tiết Phiếu Nhập (bảng trung gian)
CREATE TABLE chitiet_pn (
    ma_pn INT,
    ma_lh INT,
    don_gia DECIMAL(10,2),
    so_luong INT, 
    PRIMARY KEY (ma_pn,ma_lh),
    FOREIGN KEY (ma_pn) REFERENCES phieunhap(ma_pn),
    FOREIGN KEY (ma_lh) REFERENCES lohang(ma_lh)
);

-- 10. Khuyến Mãi
CREATE TABLE khuyenmai (
    ma_km INT PRIMARY KEY AUTO_INCREMENT,
    ten_km VARCHAR(100),
    loai_km VARCHAR(50),
    gia_tri_km DECIMAL(10,2),
    dieu_kien_km VARCHAR(100),
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    trang_thai INT(2) NOT NULL, 
    diem_can_tich_luy INT
    
);

-- 11. Hoá Đơn
CREATE TABLE hoadon (
    ma_hd INT PRIMARY KEY AUTO_INCREMENT,
    ma_nv INT,
    ma_kh INT NULL,
    ma_km INT NULL,
    tong_tien DECIMAL(10,2),
    ngay_xuat DATE NOT NULL,
    phuong_thuc_tt VARCHAR(50),
    trang_thai INT(2) NOT NULL,
    FOREIGN KEY (ma_nv) REFERENCES nhanvien(ma_nv),
    FOREIGN KEY (ma_kh) REFERENCES khachhang(ma_kh),
    FOREIGN KEY (ma_km) REFERENCES khuyenmai(ma_km)
);

-- 12. Chi Tiết Hoá Đơn
CREATE TABLE chitiet_hd (
    ma_hd INT,
    ma_lh INT,
    ma_thuoc INT,
    don_gia DECIMAL(10,2),
    so_luong INT,
    PRIMARY KEY (ma_hd, ma_lh, ma_thuoc),
    FOREIGN KEY (ma_hd) REFERENCES hoadon(ma_hd),
    FOREIGN KEY (ma_lh) REFERENCES lohang(ma_lh),
    FOREIGN KEY (ma_thuoc) REFERENCES thuoc(ma_thuoc)
);

-- ========== THÊM DATA MẪU ==========
INSERT INTO taikhoan (ma_tk, tai_khoan, mat_khau, ten, sdt, vai_tro, trang_thai) VALUES
-- Admin -> Mật khẩu: admin
-- (1, 'admin01', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Nguyễn Văn A', '0901000001', 'admin', 1),
(1, 'admin01', '$2a$10$C4MBXtcHNdb1X0HAnK/u4eMueASAsbsGCbROFoFLghuf41O4Rd9fq', 'Nguyễn Văn A', '0901000001', 'admin', 1),

-- Nhân viên -> Mật khẩu: 123
(2, 'nv_hoang', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Hoàng Minh', '0901000002', 'nhanvien', 1),
(3, 'nv_linh', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Linh Nguyễn', '0901000003', 'nhanvien', 1),
(4, 'nv_anh', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Anh Tuấn', '0901000004', 'nhanvien', 1),
(5, 'nv_quyen', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Trần Thị Quyên', '0901000005', 'nhanvien', 1),
(6, 'nv_huy', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Lê Văn Huy', '0901000006', 'nhanvien', 1),
(7, 'nv_thao', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Phạm Thảo', '0901000007', 'nhanvien', 1),
(8, 'nv_dung', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Nguyễn Dũng', '0901000008', 'nhanvien', 1),
(9, 'nv_tam', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Tâm Lê', '0901000009', 'nhanvien', 1),
(10, 'nv_lan', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Lan Đỗ', '0901000010', 'nhanvien', 1),

-- Khách hàng -> Mật khẩu: 123
(11, 'kh_vip', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Lê Văn C', '0912000001', 'khachhang', 1),
(12, 'kh_tuan', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Nguyễn Tuấn', '0912000002', 'khachhang', 1),
(13, 'kh_huyen', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Huyền Trân', '0912000003', 'khachhang', 1),
(14, 'kh_phuc', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Phúc Nguyễn', '0912000004', 'khachhang', 1),
(15, 'kh_nga', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Nga Phạm', '0912000005', 'khachhang', 1),
(16, 'kh_thao', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Thảo My', '0912000006', 'khachhang', 1),
(17, 'kh_minh', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Minh Châu', '0912000007', 'khachhang', 1),
(18, 'kh_anh', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Anh Đào', '0912000008', 'khachhang', 1),
(19, 'kh_long', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Long Vũ', '0912000009', 'khachhang', 1),
(20, 'kh_mai', '$2a$10$kqoiiCMqfWOEgVzADRyxoetTBZ0cVaZoMd/.pRrX62kxGbCiiPwuu', 'Mai Lâm', '0912000010', 'khachhang', 1);

-- 4. Nhà Cung Cấp 
INSERT INTO nhacungcap (ma_ncc, ten_ncc, sdt_ncc, dia_chi, email_ncc, trang_thai) VALUES
(101, 'Dược phẩm Tâm Bình', '02438685110', 'Hà Nội', 'info@tambinh.vn',1),
(102, 'Dược Hậu Giang', '02923891433', 'Cần Thơ', 'dhg@dhgpharma.com.vn',1),
(103, 'Imexpharm', '02773884567', 'Đồng Tháp', 'info@imexpharm.vn',1),
(104, 'Traphaco', '02438215388', 'Hà Nội', 'contact@traphaco.com.vn',1),
(105, 'Stada VN', '02837754455', 'TP.HCM', 'stada@stada.vn',1),
(106, 'Bayer VN', '02838292666', 'TP.HCM', 'info@bayer.com',1),
(107, 'Sanofi VN', '02838292929', 'TP.HCM', 'contact@sanofi.com',1),
(108, 'DHG Pharma', '02923891433', 'Cần Thơ', 'dhg@dhgpharma.com.vn',1),
(109, 'Rohto VN', '02743824567', 'Bình Dương', 'rohto@rohto.com',1),
(110, 'Pfizer VN', '02839305555', 'TP.HCM', 'pfizer@pfizer.com',1),
(111, 'Servier VN', '02839306666', 'TP.HCM', 'servier@servier.com',1),
(112, 'Leo Pharma VN', '02838448899', 'TP.HCM', 'leo@leo.com',1),
(113, 'Blackmores VN', '02838442222', 'TP.HCM', 'blackmores@blackmores.com',1),
(114, 'Roche VN', '02838282233', 'TP.HCM', 'roche@roche.com',1),
(115, 'Mega Lifesciences', '02839114444', 'TP.HCM', 'mega@mls.com',1),
(116, 'Euvipharm', '02743886677', 'Long An', 'info@euvipharm.vn',1),
(117, 'Boston Pharma', '02837668899', 'TP.HCM', 'info@bostonpharma.vn',1),
(118, 'Pymepharco', '02573884444', 'Phú Yên', 'info@pymepharco.vn',1),
(119, 'Mediplantex', '02438223344', 'Hà Nội', 'info@mediplantex.vn',1),
(120, 'OPV Pharma', '02837665522', 'TP.HCM', 'opv@opvpharma.com',1),
(121, 'Uni Pharma', '02838292211', 'TP.HCM', 'uni@unipharma.com',1),
(122, 'Mega Biopharma', '02839224455', 'TP.HCM', 'mega@biopharma.com',1);

-- 8. Danh Mục Thuốc
INSERT INTO danhmucthuoc (ma_dmt, ten_dmt, trang_thai) VALUES
(201, 'Thuốc cảm cúm & Hạ sốt',1),
(202, 'Kháng sinh & Kháng viêm',1),
(203, 'Vitamin & Khoáng chất', 1),
(204, 'Thuốc giảm đau', 1),
(205, 'Thuốc tiêu hóa', 1),
(206, 'Thuốc ho & long đờm', 1),
(207, 'Thuốc tim mạch', 1),
(208, 'Thuốc da liễu', 1),
(209, 'Thuốc thần kinh', 1),
(210, 'Thuốc chống dị ứng', 1),
(211, 'Thuốc chống viêm khớp', 1),
(212, 'Thuốc bổ não', 1),
(213, 'Thuốc tiểu đường', 1),
(214, 'Thuốc hạ mỡ máu', 1),
(215, 'Thuốc dạ dày', 1),
(216, 'Thuốc chống nấm', 1),
(217, 'Thuốc chống trầm cảm', 1),
(218, 'Thực phẩm chức năng', 1),
(219, 'Thuốc kháng virus', 1),
(220, 'Thuốc nhỏ mắt', 1);

-- 10. Khuyến Mãi
INSERT INTO khuyenmai (ten_km, loai_km, gia_tri_km, dieu_kien_km, ngay_bat_dau, ngay_ket_thuc, trang_thai, diem_can_tich_luy) VALUES
('Giảm 10% cho khách VIP', 'Phần trăm', 10.00, 'Khách hàng có 100 điểm tích lũy', '2025-10-01', '2025-12-31', 1, 100),
('Mã mặc định', 'Phần trăm', 0.00, 'KHÔNG', '2025-11-01', '3025-11-30', 1, 0),
('Tết 2025 - Giảm 5%', 'Phần trăm', 5.00, 'Tổng > 100k', '2025-01-10', '2025-02-10', 1, 0),
('8/3 - Giảm 10%', 'Phần trăm', 10.00, 'Khách nữ', '2025-03-01', '2025-03-10', 1, 0),
('Ngày thuốc Việt', 'Phần trăm', 7.00, 'Hàng Việt Nam', '2025-04-01', '2025-04-30', 1, 0),
('Giảm 20k cho đơn > 200k', 'Giá trị', 20000.00, 'Tổng > 200k', '2025-05-01', '2025-06-01', 1, 20),
('Hè năng động', 'Phần trăm', 8.00, 'Mọi khách hàng', '2025-06-01', '2025-07-15', 1, 0),
('Back to school', 'Phần trăm', 10.00, 'Học sinh sinh viên', '2025-08-01', '2025-09-01', 1, 0),
('Giảm 15% thuốc bổ não', 'Phần trăm', 15.00, 'Danh mục 212', '2025-09-10', '2025-10-10', 1, 0),
('Giảm 10% cho đơn trên 500k', 'Phần trăm', 10.00, 'Tổng > 500k', '2025-10-01', '2025-11-01', 1, 0),
('Flash sale', 'Phần trăm', 12.00, 'Mọi khách hàng', '2025-11-01', '2025-11-15', 1, 0),
('Mừng sinh nhật', 'Phần trăm', 5.00, 'Khách hàng có sinh nhật tháng 11', '2025-11-01', '2025-11-30', 1, 0),
('Noel sale', 'Phần trăm', 20.00, 'Đơn > 300k', '2025-12-01', '2025-12-31', 1, 0),
('Khuyến mãi đặc biệt', 'Phần trăm', 25.00, 'VIP khách hàng', '2025-12-10', '2025-12-25', 1, 100),
('Giảm 30k thuốc cảm', 'Giá trị', 30000.00, 'Danh mục 201', '2025-12-01', '2025-12-31', 1, 0),
('Xuân 2026', 'Phần trăm', 10.00, 'Tổng > 150k', '2026-01-10', '2026-02-10', 1, 0),
('Giảm 5% mọi đơn', 'Phần trăm', 5.00, 'Không điều kiện', '2025-11-01', '2026-01-01', 1, 10),
('Sinh nhật khách hàng', 'Phần trăm', 10.00, 'Theo tháng sinh', '2025-01-01', '2025-12-31', 1, 0),
('Tuần lễ vàng', 'Phần trăm', 12.00, 'Đơn > 1 triệu', '2025-07-10', '2025-07-17', 1, 0),
('Giảm 10% thực phẩm chức năng', 'Phần trăm', 10.00, 'Danh mục 218', '2025-09-01', '2025-10-01', 1, 10),
('Siêu ưu đãi', 'Phần trăm', 8.00, 'Tổng > 300k', '2025-08-10', '2025-09-10', 1, 0),
('Ưu đãi khủng', 'Khuyến mãi đặc biệt', 50.00, 'Chọn sản phẩm chỉ định', '2025-10-15', '2025-11-15', 1, 0);

-- 2. Nhân Viên
INSERT INTO nhanvien (ma_nv, ma_tk, ngay_vao_lam, luong, email, dia_chi, gioi_tinh, ngay_sinh, vi_tri, trang_thai) VALUES
(1, 1, '2023-01-15', 25000000.00, 'admin@gmail.com', 'Quận 1, TP.HCM', 'Nam', '1990-05-10', 'Quản lý',1),
(2, 2, '2024-01-01', 12000000.00, 'hoangnv@gmail.com', 'Quận 2, TP.HCM', 'Nam', '1995-02-14', 'Nhân viên bán hàng',1),
(3, 3, '2024-03-10', 11500000.00, 'linhnv@gmail.com', 'Quận 3, TP.HCM', 'Nữ', '1998-06-21', 'Nhân viên bán hàng',1),
(4, 4, '2024-04-22', 11800000.00, 'anhnv@gmail.com', 'Quận 4, TP.HCM', 'Nam', '1997-10-05', 'Nhân viên kho',1),
(5, 5, '2024-02-11', 12000000.00, 'quyennv@gmail.com', 'Quận 5, TP.HCM', 'Nữ', '1999-09-12', 'Nhân viên bán hàng',1),
(6, 6, '2023-12-01', 11000000.00, 'huynv@gmail.com', 'Quận 6, TP.HCM', 'Nam', '1994-04-18', 'Nhân viên giao hàng',1),
(7, 7, '2024-05-09', 12500000.00, 'thaonv@gmail.com', 'Quận 7, TP.HCM', 'Nữ', '1996-12-02', 'Nhân viên tư vấn',1),
(8, 8, '2024-03-05', 13000000.00, 'dungnv@gmail.com', 'Quận 8, TP.HCM', 'Nam', '1993-11-07', 'Nhân viên kho',1),
(9, 9, '2024-06-01', 12500000.00, 'tamnv@gmail.com', 'Quận 9, TP.HCM', 'Nữ', '1998-03-28', 'Nhân viên thu ngân',1),
(10, 10, '2024-07-01', 11500000.00, 'lannv@gmail.com', 'Quận 10, TP.HCM', 'Nữ', '1997-08-30', 'Nhân viên bán hàng',1);

-- 3. Khách Hàng
INSERT INTO khachhang (ma_kh, ma_tk, ngay_dang_ky, diem_tich_luy, trang_thai) VALUES
(11, 11, '2024-05-23', 300, 1),
(12, 12, '2024-06-02', 120, 1),
(13, 13, '2024-06-12', 450, 1),
(14, 14, '2024-07-01', 200, 1),
(15, 15, '2024-07-15', 90, 1),
(16, 16, '2024-08-05', 500, 1),
(17, 17, '2024-08-22', 250, 1),
(18, 18, '2024-09-10', 310, 1),
(19, 19, '2024-09-25', 180, 1),
(20, 20, '2024-10-01', 400, 1);

-- 5. Phiếu Nhập
INSERT INTO phieunhap (ma_pn, ma_nv, thoi_gian_nhap, dia_diem, trang_thai) VALUES
(43, 1, '2025-01-10 09:30:00', 'Kho Tân Bình', 1),
(44, 1, '2025-01-15 14:20:00', 'Kho Gò Vấp', 1),
(45, 1, '2025-01-20 08:45:00', 'Kho Quận 5', 1),
(46, 1, '2025-02-05 13:00:00', 'Kho Thủ Đức', 1),
(47, 3, '2025-02-15 10:00:00', 'Kho Quận 3', 1),
(48, 2, '2025-02-28 16:15:00', 'Kho Quận 7', 1),
(49, 4, '2025-03-10 09:00:00', 'Kho Bình Thạnh', 1),
(50, 6, '2025-03-15 15:45:00', 'Kho Hà Nội', 1),
(51, 5, '2025-03-20 11:00:00', 'Kho Đà Nẵng', 1),
(52, 2, '2025-03-30 10:30:00', 'Kho TP.HCM', 1),
(53, 2, '2025-04-02 14:30:00', 'Kho Quận 10', 1),
(54, 7, '2025-04-05 09:15:00', 'Kho Cần Thơ', 1),
(55, 9, '2025-04-10 15:45:00', 'Kho Bình Dương', 1),
(56, 10, '2025-04-15 13:25:00', 'Kho Đồng Nai', 1),
(57, 8, '2025-05-01 10:10:00', 'Kho Tây Ninh', 1),
(58, 10, '2025-05-05 09:00:00', 'Kho Vũng Tàu', 1),
(59, 6, '2025-05-10 11:00:00', 'Kho Quận 9', 1),
(60, 7, '2025-05-15 14:45:00', 'Kho Quận 8', 1),
(61, 4, '2025-05-20 16:00:00', 'Kho Quận 4', 1),
(62, 5, '2025-05-25 09:15:00', 'Kho Long An', 1);

-- 9. Thuốc
INSERT INTO thuoc (ma_thuoc, ma_dmt, ten_thuoc, gia, don_vi_tinh, nha_san_xuat, xuat_xu, url_anh, trang_thai) VALUES
(61, 201, 'Panadol Cold & Flu', 25000.00, 'Vỉ (10 viên)', 'GSK', 'Anh', '/image/product-image/placeholder_them_thuoc.jpg', 1),
(62, 202, 'Amoxicillin 500mg', 15000.00, 'Hộp (20 viên)', 'DHG Pharma', 'Việt Nam', '/image/product-image/placeholder_them_thuoc.jpg', 1),
(63, 203, 'Vitamin C 500mg', 20000, 'Vỉ (10 viên)', 'Traphaco', 'Việt Nam', '/image/product-image/thuoc_vitaminC.jpg', 1),
(64, 204, 'Paracetamol 500mg', 15000, 'Vỉ (10 viên)', 'DHG', 'Việt Nam', '/image/product-image/thuoc_para.jpg', 1),
(65, 205, 'Smecta', 35000, 'Hộp (10 gói)', 'Ipsen', 'Pháp', '/image/product-image/smecta.jpg', 1),
(66, 206, 'Prospan', 90000, 'Chai 100ml', 'Engelhard', 'Đức', '/image/product-image/prospan.jpg', 1),
(67, 207, 'Natrilix SR', 80000, 'Hộp (30 viên)', 'Servier', 'Pháp', '/image/product-image/natrilix.jpg', 1),
(68, 208, 'Fucidin Cream', 95000, 'Tuýp 15g', 'Leo Pharma', 'Đan Mạch', '/image/product-image/fucidin.jpg', 1),
(69, 209, 'Piracetam 800mg', 50000, 'Hộp (30 viên)', 'DHG', 'Việt Nam', '/image/product-image/piracetam.jpg', 1),
(70, 210, 'Loratadin 10mg', 25000, 'Vỉ (10 viên)', 'Stada', 'Việt Nam', '/image/product-image/loratadin.jpg', 1),
(71, 211, 'Mobic 7.5mg', 70000, 'Hộp (10 viên)', 'Boehringer', 'Đức', '/image/product-image/mobic.jpg', 1),
(72, 212, 'Ginkgo Biloba', 120000, 'Hộp (60 viên)', 'Traphaco', 'Việt Nam', '/image/product-image/ginkgo.jpg', 1),
(73, 213, 'Metformin 500mg', 30000, 'Hộp (20 viên)', 'DHG', 'Việt Nam', '/image/product-image/metformin.jpg', 1),
(74, 214, 'Atorvastatin 10mg', 45000, 'Hộp (20 viên)', 'Stada', 'Việt Nam', '/image/product-image/atorvastatin.jpg', 1),
(75, 215, 'Omeprazol 20mg', 40000, 'Hộp (14 viên)', 'DHG', 'Việt Nam', '/image/product-image/omeprazol.jpg', 1),
(76, 216, 'Canesten Cream', 85000, 'Tuýp 20g', 'Bayer', 'Đức', '/image/product-image/canesten.jpg', 1),
(77, 217, 'Sertraline 50mg', 90000, 'Hộp (30 viên)', 'Pfizer', 'Anh', '/image/product-image/sertraline.jpg', 1),
(78, 218, 'Blackmores Fish Oil', 350000, 'Lọ (100 viên)', 'Blackmores', 'Úc', '/image/product-image/fishoil.jpg', 1),
(79, 219, 'Tamiflu 75mg', 220000, 'Hộp (10 viên)', 'Roche', 'Thụy Sĩ', '/image/product-image/tamiflu.jpg', 1),
(80, 220, 'V.Rohto', 40000, 'Chai 13ml', 'Rohto', 'Nhật Bản', '/image/product-image/rohto.jpg', 1),
(81, 203, 'Vitamin D3 1000IU', 50000, 'Lọ (60 viên)', 'Traphaco', 'Việt Nam', '/image/product-image/vitaminD3.jpg', 1),
(82, 205, 'Enterogermina', 95000, 'Hộp (10 ống)', 'Sanofi', 'Pháp', '/image/product-image/entero.jpg', 1);

-- 6. Lô Hàng
INSERT INTO lohang (ma_lh, ma_ncc, ma_thuoc, sl_nhap, sl_ton, ngay_sx, han_sd, gia_nhap, trang_thai) VALUES
 (51, 101, 61, 0, 0, '2024-01-15', '2026-01-15', 12000.00, 1),
 (52, 102, 62, 0, 0, '2024-08-10', '2025-12-31', 95000.00, 1),
-- (53, 101, 62, 0, 0, '2025-05-01', '2027-05-01', 8000.00, 1), 
-- (54, 102, 61, 0, 0, '2024-04-10', '2028-12-31', 50000.00, 1),
(55, 103, 63, 0, 0, '2024-12-01', '2026-12-01', 10000, 1),
(56, 104, 64, 0, 0, '2025-01-10', '2026-01-10', 8000, 1),
(57, 105, 65, 0, 0, '2024-11-01', '2026-11-01', 25000, 1),
(58, 106, 66, 0, 0, '2024-12-05', '2026-12-05', 70000, 1),
(59, 107, 67, 0, 0, '2024-09-15', '2026-09-15', 60000, 1),
(60, 108, 68, 0, 0, '2025-01-15', '2027-01-15', 65000, 1),
(61, 109, 69, 0, 0, '2025-02-01', '2027-02-01', 40000, 1),
(62, 110, 70, 0, 0, '2025-03-01', '2026-03-01', 12000, 1),
(63, 111, 71, 0, 0, '2025-03-10', '2027-03-10', 50000, 1),
(64, 112, 72, 0, 0, '2025-03-20', '2027-03-20', 90000, 1),
(65, 113, 73, 0, 0, '2025-04-01', '2026-04-01', 20000, 1),
(66, 114, 74, 0, 0, '2025-04-10', '2027-04-10', 30000, 1),
(67, 115, 75, 0, 0, '2025-04-20', '2027-04-20', 25000, 1),
(68, 116, 76, 0, 0, '2025-05-01', '2027-05-01', 70000, 1),
(69, 117, 77, 0, 0, '2025-05-10', '2027-05-10', 80000, 1),
(70, 118, 78, 0, 0, '2025-05-20', '2027-05-20', 250000, 1),
(71, 119, 79, 0, 0, '2025-05-25', '2026-05-25', 150000, 1),
(72, 120, 80, 0, 0, '2025-06-01', '2027-06-01', 25000, 1),
(73, 121, 81, 0, 0, '2025-06-10', '2027-06-10', 30000, 1),
(74, 122, 82, 0, 0, '2025-06-20', '2027-06-20', 70000, 1);

-- 7. Chi Tiết Phiếu Nhập
INSERT INTO chitiet_pn (ma_pn, ma_lh, don_gia, so_luong) VALUES
-- Mỗi phiếu nhập gồm nhiều loại thuốc (mỗi lô tương ứng 1 loại)
(43, 55, 10000, 100),
(43, 56, 8000, 50),
(43, 57, 25000, 70),

(44, 56, 8000, 150),
(44, 58, 70000, 40),
(44, 59, 60000, 60),

(45, 57, 25000, 80),
(45, 60, 65000, 40),
(45, 61, 40000, 120),

(46, 58, 70000, 60),
(46, 62, 12000, 100),
(46, 63, 50000, 50),

(47, 59, 60000, 70),
(47, 64, 90000, 30),
(47, 65, 20000, 100),

(48, 66, 30000, 60),
(48, 67, 25000, 70),
(48, 68, 70000, 50),

(49, 69, 80000, 40),
(49, 70, 250000, 30),
(49, 71, 150000, 20),

(50, 72, 25000, 80),
(50, 73, 30000, 60),
(50, 74, 70000, 40),

(51, 55, 10000, 90),
(51, 56, 8000, 110),
(51, 57, 25000, 50),

(52, 58, 70000, 70),
(52, 59, 60000, 40),
(52, 60, 65000, 50),

(53, 61, 40000, 120),
(53, 62, 12000, 80),
(53, 63, 50000, 30),

(54, 64, 90000, 40),
(54, 65, 20000, 60),
(54, 66, 30000, 50),

(55, 67, 25000, 100),
(55, 68, 70000, 70),
(55, 69, 80000, 60),

(56, 70, 250000, 50),
(56, 71, 150000, 40),
(56, 72, 25000, 100),

(57, 73, 30000, 80),
(57, 74, 70000, 50),
(57, 55, 10000, 60),

(58, 56, 8000, 100),
(58, 57, 25000, 50),
(58, 58, 70000, 40),

(59, 59, 60000, 60),
(59, 60, 65000, 30),
(59, 61, 40000, 70),

(60, 62, 12000, 90),
(60, 63, 50000, 60),
(60, 64, 90000, 40),

(61, 65, 20000, 50),
(61, 66, 30000, 70),
(61, 67, 25000, 80),

(62, 68, 70000, 50),
(62, 69, 80000, 60),
(62, 70, 250000, 30),

(62, 51, 12000, 100),
(62, 52, 95000, 50);

-- 11. Hoá Đơn 
INSERT INTO hoadon (ma_nv, ma_kh, ma_km, tong_tien, ngay_xuat, phuong_thuc_tt, trang_thai) VALUES
(1, 11, 1, 120000.00, '2025-10-08', 'Chuyển khoản', 1),
(1, 11, 2, 50000.00, '2025-10-08', 'Tiền mặt', 1),
(2, 12, 3, 120000, '2025-05-01', 'Tiền mặt', 1),
(2, 12, 4, 180000, '2025-05-02', 'Chuyển khoản', 1),
(3, 13, 5, 220000, '2025-05-03', 'Tiền mặt', 1),
(3, 13, 6, 150000, '2025-05-04', 'Chuyển khoản', 1),
(4, 14, 7, 320000, '2025-05-05', 'Tiền mặt', 1),
(4, 14, 8, 400000, '2025-05-06', 'Chuyển khoản', 1),
(5, 15, 9, 280000, '2025-05-07', 'Tiền mặt', 1),
(5, 15, 10, 150000, '2025-05-08', 'Chuyển khoản', 1),
(6, 16, 11, 250000, '2025-05-09', 'Tiền mặt', 1),
(6, 16, 12, 370000, '2025-05-10', 'Chuyển khoản', 1),
(7, 17, 13, 420000, '2025-05-11', 'Tiền mặt', 1),
(7, 17, 14, 270000, '2025-05-12', 'Chuyển khoản', 1),
(8, 18, 15, 180000, '2025-05-13', 'Tiền mặt', 1),
(8, 18, 16, 350000, '2025-05-14', 'Chuyển khoản', 1),
(9, 19, 17, 240000, '2025-05-15', 'Tiền mặt', 1),
(9, 19, 18, 190000, '2025-05-16', 'Chuyển khoản', 1),
(10, 20, 19, 280000, '2025-05-17', 'Tiền mặt', 1),
(10, 20, 20, 500000, '2025-05-18', 'Chuyển khoản', 1),
(1, 11, 3, 150000, '2025-05-19', 'Tiền mặt', 1),
(2, 12, 4, 320000, '2025-05-20', 'Chuyển khoản', 1);

-- 12. Chi Tiết Hoá Đơn
INSERT INTO chitiet_hd (ma_hd, ma_lh, ma_thuoc, don_gia, so_luong) VALUES
(1, 51, 61, 25000.00, 4),
(2, 51, 61, 25000.00, 2),
(3, 55, 63, 20000, 3),
(4, 56, 64, 15000, 4),
(5, 57, 65, 35000, 2),
(6, 58, 66, 90000, 1),
(7, 59, 67, 80000, 2),
(8, 60, 68, 95000, 1),
(9, 61, 69, 50000, 3),
(10, 62, 70, 25000, 4),
(11, 63, 71, 70000, 2),
(12, 64, 72, 120000, 1),
(13, 65, 73, 30000, 3),
(14, 66, 74, 45000, 2),
(15, 67, 75, 40000, 2),
(16, 68, 76, 85000, 1),
(17, 69, 77, 90000, 1),
(18, 70, 78, 350000, 1),
(19, 71, 79, 220000, 1),
(20, 72, 80, 40000, 2),
(21, 73, 81, 50000, 3),
(22, 74, 82, 95000, 2);

-- ===== CẬP NHẬT sl_ton,sl_nhap =====
UPDATE lohang
SET sl_nhap = (
    SELECT SUM(so_luong)
    FROM chitiet_pn
    WHERE chitiet_pn.ma_lh = lohang.ma_lh
),
sl_ton = (
    SELECT SUM(so_luong)
    FROM chitiet_pn
    WHERE chitiet_pn.ma_lh = lohang.ma_lh
)
WHERE EXISTS (
    SELECT 1
    FROM chitiet_pn
    WHERE chitiet_pn.ma_lh = lohang.ma_lh
);