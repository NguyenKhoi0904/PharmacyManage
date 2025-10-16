package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.LoHangDTO;
import database.JDBCUtil;

public class LoHangDAO implements DAOinterface<LoHangDTO> {
    public static LoHangDAO getInstance() {
        return new LoHangDAO();
    }

    @Override
    public ArrayList<LoHangDTO> selectAll() {
        ArrayList<LoHangDTO> result = new ArrayList<LoHangDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM lohang WHERE trang_thai=1";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new LoHangDTO(rs.getInt("ma_lh"), rs.getInt("ma_ncc"), rs.getInt("sl_nhap"),
                        rs.getInt("sl_ton"), rs.getDate("ngay_sx"), rs.getDate("han_sd"),
                        rs.getBigDecimal("gia_nhap"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public LoHangDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM lohang WHERE ma_lh=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new LoHangDTO(rs.getInt("ma_lh"), rs.getInt("ma_ncc"), rs.getInt("sl_nhap"),
                        rs.getInt("sl_ton"), rs.getDate("ngay_sx"), rs.getDate("han_sd"),
                        rs.getBigDecimal("gia_nhap"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(LoHangDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO lohang (ma_lh,ma_ncc,sl_nhap,sl_ton,ngay_sx,han_sd,gia_nhap,trang_thai) VALUES (?,?,?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaLh());
            ps.setInt(2, data.getMaNcc());
            ps.setInt(3, data.getSlNhap());
            ps.setInt(4, data.getSlTon());
            ps.setDate(5, data.getNgaySx());
            ps.setDate(6, data.getHanSd());
            ps.setBigDecimal(7, data.getGiaNhap());
            ps.setInt(8, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(LoHangDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE lohang SET ma_ncc=?,sl_nhap=?,sl_ton=?,ngay_sx=?,han_sd=?,gia_nhap=?,trang_thai=? WHERE ma_lh=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaNcc());
            ps.setInt(2, data.getSlNhap());
            ps.setInt(3, data.getSlTon());
            ps.setDate(4, data.getNgaySx());
            ps.setDate(5, data.getHanSd());
            ps.setBigDecimal(6, data.getGiaNhap());
            ps.setInt(7, data.getTrangThai());
            ps.setInt(8, data.getMaLh());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int updateSlTon(int ma_lh, int sl_thay_doi) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE lohang SET sl_ton = sl_ton + ? WHERE ma_lh = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            // 1. so_luong_thay_doi: Có thể là dương (thêm vào) hoặc âm (trừ đi)
            ps.setInt(1, sl_thay_doi);
            ps.setInt(2, ma_lh);

            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteById(String id) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE lohang SET trang_thai=0 WHERE ma_lh=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Kiểm tra xem một Lô Hàng có đủ số lượng tồn kho để trừ đi hay không.
     * Dùng cho HonDonBUS
     * 
     * @param ma_lh            Mã Lô Hàng
     * @param so_luong_can_tru Số lượng cần trừ đi (bán ra)
     * @return true nếu tồn kho >= số lượng cần trừ, ngược lại là false
     */
    public boolean checkSlTon(int ma_lh, int so_luong_can_tru) {
        boolean isEnough = false;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT sl_ton FROM lohang WHERE ma_lh=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ma_lh);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int sl_ton_hien_tai = rs.getInt("sl_ton");
                if (sl_ton_hien_tai >= so_luong_can_tru) {
                    isEnough = true;
                }
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isEnough;
    }
}
