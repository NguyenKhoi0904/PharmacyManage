package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.ThuocDTO;
import database.JDBCUtil;

public class ThuocDAO implements DAOinterface<ThuocDTO> {
    public static ThuocDAO getInstance() {
        return new ThuocDAO();
    }

    @Override
    public ArrayList<ThuocDTO> selectAll() {
        ArrayList<ThuocDTO> result = new ArrayList<ThuocDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM thuoc WHERE trang_thai=1";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new ThuocDTO(rs.getInt("ma_thuoc"), rs.getInt("ma_dmt"), rs.getString("ten_thuoc"),
                        rs.getBigDecimal("gia"), rs.getString("don_vi_tinh"), rs.getString("nha_san_xuat"),
                        rs.getString("xuat_xu"), rs.getString("url_anh"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ThuocDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM thuoc WHERE ma_thuoc=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new ThuocDTO(rs.getInt("ma_thuoc"), rs.getInt("ma_dmt"), rs.getString("ten_thuoc"),
                        rs.getBigDecimal("gia"), rs.getString("don_vi_tinh"), rs.getString("nha_san_xuat"),
                        rs.getString("xuat_xu"), rs.getString("url_anh"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(ThuocDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO thuoc (ma_thuoc,ma_dmt,ten_thuoc,gia,don_vi_tinh,nha_san_xuat,xuat_xu,url_anh,trang_thai) VALUES (?,?,?,?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaThuoc());
            ps.setInt(2, data.getMaDmt());
            ps.setString(3, data.getTenThuoc());
            ps.setBigDecimal(4, data.getGia());
            ps.setString(5, data.getDonViTinh());
            ps.setString(6, data.getNhaSanXuat());
            ps.setString(7, data.getXuatXu());
            ps.setString(8, data.getUrlAnh());
            ps.setInt(9, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(ThuocDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE thuoc SET ma_dmt=?,ten_thuoc=?,gia=?,don_vi_tinh=?,nha_san_xuat=?,xuat_xu=?,url_anh=?,trang_thai=? WHERE ma_thuoc=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaDmt());
            ps.setString(2, data.getTenThuoc());
            ps.setBigDecimal(3, data.getGia());
            ps.setString(4, data.getDonViTinh());
            ps.setString(5, data.getNhaSanXuat());
            ps.setString(6, data.getXuatXu());
            ps.setString(7, data.getUrlAnh());
            ps.setInt(8, data.getTrangThai());
            ps.setInt(9, data.getMaThuoc());
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
            String sql = "UPDATE thuoc SET trang_thai=0 WHERE ma_thuoc=?";
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
