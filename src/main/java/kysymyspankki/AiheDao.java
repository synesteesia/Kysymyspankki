/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kysymyspankki;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mikko
 */
public class AiheDao implements Dao<Aihe, Integer> {

    private Database database;

    public AiheDao(Database database) {
        this.database = database;
    }

    @Override
    public List<Aihe> findAll() throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Aihe");
        ResultSet rs = stmt.executeQuery();
        List<Aihe> aiheet = new ArrayList<>();

        while (rs.next()) {
            Aihe t = new Aihe(rs.getString("nimi"), rs.getInt("kurssi_id"));
            t.setId(rs.getInt("id"));
            aiheet.add(t);
        }

        return aiheet;
    }

    @Override
    public Aihe create(Aihe aihe) throws Exception {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Aihe"
                + " (nimi, kurssi_id)"
                + " VALUES (?, ?)");
        stmt.setString(1, aihe.getNimi());
        stmt.setInt(2, aihe.getKurssi());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Aihe"
                + " WHERE nimi = ? AND kurssi_id = ?");
        stmt.setString(1, aihe.getNimi());
        stmt.setInt(2, aihe.getKurssi());

        ResultSet rs = stmt.executeQuery();
        rs.next();

        Aihe a = new Aihe(rs.getString("nimi"), rs.getInt("kurssi_id"));
        a.setId(rs.getInt("id"));

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    @Override
    public Aihe findOne(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Aihe WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Aihe a = new Aihe(rs.getString("nimi"), rs.getInt("kurssi_id"));
        a.setId(rs.getInt("id"));
        stmt.close();
        rs.close();

        conn.close();

        return a;

    }

    @Override
    public void delete(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Aihe WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public List<Aihe> findByCourseId(int kurssiId) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Aihe WHERE kurssi_id = ?");
        stmt.setInt(1, kurssiId);
        ResultSet rs = stmt.executeQuery();
        List<Aihe> aiheet = new ArrayList<>();

        while (rs.next()) {
            Aihe t = new Aihe(rs.getString("nimi"), rs.getInt("kurssi_id"));
            t.setId(rs.getInt("id"));
            aiheet.add(t);
        }

        return aiheet;

    }

    public Aihe findByCourseIDandName(int id, String nimi) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Aihe WHERE kurssi_id = ? AND nimi = ?");
        stmt.setInt(1, id);
        stmt.setString(2, nimi);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Aihe a = new Aihe(rs.getString("nimi"), rs.getInt("kurssi_id"));
        a.setId(rs.getInt("id"));
        stmt.close();
        rs.close();

        conn.close();

        return a;

    }

}
