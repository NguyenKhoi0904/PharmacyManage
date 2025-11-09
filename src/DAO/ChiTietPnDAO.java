package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.ChiTietPnDTO;
import database.JDBCUtil;

public class ChiTietPnDAO {

    // Singleton instance
    private static ChiTietPnDAO instance;

    // Singleton getter
    public static ChiTietPnDAO getInstance() {
        if (instance == null) {
            instance = new ChiTietPnDAO();
        }
        return instance;
    }

    // =============================
    // SELECT ALL
    // =============================
    public ArrayList<ChiTietPnDTO> selectAll() {
        ArrayList<ChiTietPnDTO> result = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM chitiet_pn");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                result.add(new ChiTietPnDTO(
                        rs.getInt("ma_pn"),
                        rs.getInt("ma_lh"),
                        rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================
    // SELECT BY ma_pn
    // =============================
    public ArrayList<ChiTietPnDTO> selectAllByMaPn(int ma_pn) {
        ArrayList<ChiTietPnDTO> result = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM chitiet_pn WHERE ma_pn=?")) {

            pst.setInt(1, ma_pn);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(new ChiTietPnDTO(
                            rs.getInt("ma_pn"),
                            rs.getInt("ma_lh"),
                            rs.getBigDecimal("don_gia"),
                            rs.getInt("so_luong")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================
    // SELECT BY (ma_pn, ma_lh)
    // =============================
    public ChiTietPnDTO selectById(int ma_pn, int ma_lh) {
        ChiTietPnDTO result = null;
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM chitiet_pn WHERE ma_pn=? AND ma_lh=?")) {

            ps.setInt(1, ma_pn);
            ps.setInt(2, ma_lh);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new ChiTietPnDTO(
                            rs.getInt("ma_pn"),
                            rs.getInt("ma_lh"),
                            rs.getBigDecimal("don_gia"),
                            rs.getInt("so_luong")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================
    // INSERT
    // =============================
    public int insert(ChiTietPnDTO obj) {
        int result = 0;
        String sql = "INSERT INTO chitiet_pn(ma_pn, ma_lh, don_gia, so_luong) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, obj.getMaPn());
            pst.setInt(2, obj.getMaLh());
            pst.setBigDecimal(3, obj.getDonGia());
            pst.setInt(4, obj.getSoLuong());
            result = pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================
    // UPDATE
    // =============================
    public int update(ChiTietPnDTO data) {
        int result = 0;
        String sql = "UPDATE chitiet_pn SET don_gia=?, so_luong=? WHERE ma_pn=? AND ma_lh=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, data.getDonGia());
            ps.setInt(2, data.getSoLuong());
            ps.setInt(3, data.getMaPn());
            ps.setInt(4, data.getMaLh());
            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================
    // DELETE BY ma_pn
    // =============================
    public int deleteById(int ma_pn) {
        int result = 0;
        String sql = "DELETE FROM chitiet_pn WHERE ma_pn=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ma_pn);
            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================
    // DELETE BY ma_pn + ma_lh
    // =============================
    public int deleteById(int ma_pn, int ma_lh) {
        int result = 0;
        String sql = "DELETE FROM chitiet_pn WHERE ma_pn=? AND ma_lh=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ma_pn);
            ps.setInt(2, ma_lh);
            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
