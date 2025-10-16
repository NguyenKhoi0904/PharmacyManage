package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.TaiKhoanDTO;
import database.JDBCUtil;

public class TaiKhoanDAO implements DAOinterface<TaiKhoanDTO> {

    public static TaiKhoanDAO getInstance() {
        return new TaiKhoanDAO();
    }

    // dùng cho kiểm tra tài khoản đăng nhập bằng BCrypt
    public TaiKhoanDTO selectByTaiKhoan(String taiKhoan) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM taikhoan WHERE tai_khoan=? AND trang_thai = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, taiKhoan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TaiKhoanDTO(rs.getInt("ma_tk"), rs.getString("tai_khoan"), rs.getString("mat_khau"),
                        rs.getString("ten"), rs.getString("sdt"), rs.getString("vai_tro"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<TaiKhoanDTO> selectAll() {
        ArrayList<TaiKhoanDTO> result = new ArrayList<TaiKhoanDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM taikhoan WHERE trang_thai=1";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new TaiKhoanDTO(rs.getInt("ma_tk"), rs.getString("tai_khoan"), rs.getString("mat_khau"),
                        rs.getString("ten"), rs.getString("sdt"), rs.getString("vai_tro"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public TaiKhoanDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM taikhoan WHERE ma_tk=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new TaiKhoanDTO(rs.getInt("ma_tk"), rs.getString("tai_khoan"), rs.getString("mat_khau"),
                        rs.getString("ten"), rs.getString("sdt"), rs.getString("vai_tro"), rs.getInt("trang_thai"));

            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(TaiKhoanDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO taikhoan (ma_tk,tai_khoan,mat_khau,ten,sdt,vai_tro,trang_thai) VALUES (?,?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaTk());
            ps.setString(2, data.getTaiKhoan());
            ps.setString(3, data.getMatKhau());
            ps.setString(4, data.getTen());
            ps.setString(5, data.getSdt());
            ps.setString(6, data.getVaiTro());
            ps.setInt(7, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(TaiKhoanDTO data) {
        // tai_khoan,mat_khau,ten,sdt,vai_tro
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE taikhoan SET tai_khoan=?,mat_khau=?,ten=?,sdt=?,vai_tro=?,trang_thai=? WHERE ma_tk=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, data.getTaiKhoan());
            ps.setString(2, data.getMatKhau());
            ps.setString(3, data.getTen());
            ps.setString(4, data.getSdt());
            ps.setString(5, data.getVaiTro());
            ps.setInt(6, data.getTrangThai());
            ps.setInt(7, data.getMaTk());
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
            String sql = "UPDATE taikhoan SET trang_thai=0 WHERE ma_tk=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
