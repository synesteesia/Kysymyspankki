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
public class KurssiDao implements Dao<Kurssi, Integer> {

    private Database database;

    public KurssiDao(Database database) {
        this.database = database;
    }

    @Override
    public List<Kurssi> findAll() throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kurssi");
        ResultSet rs = stmt.executeQuery();
        List<Kurssi> kurssit = new ArrayList<>();

        while (rs.next()) {
            Kurssi t = new Kurssi(rs.getString("nimi"));
            t.setId(rs.getInt("id"));
            kurssit.add(t);
        }

        return kurssit;
    }

    @Override
    public Kurssi create(Kurssi kurssi) throws Exception {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kurssi"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, kurssi.getNimi());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Kurssi"
                + " WHERE nimi = ?");
        stmt.setString(1, kurssi.getNimi());

        ResultSet rs = stmt.executeQuery();
        rs.next();

        Kurssi k = new Kurssi(rs.getString("nimi"));
        k.setId(rs.getInt("id"));

        stmt.close();
        rs.close();

        conn.close();

        return k;
    }

    @Override
    public Kurssi findOne(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kurssi WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Kurssi k = new Kurssi(rs.getString("nimi"));
        k.setId(rs.getInt("id"));
        stmt.close();
        rs.close();

        conn.close();

        return k;

    }

    public Kurssi findByName(String kurssiNimi) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kurssi WHERE nimi = ?");
        stmt.setString(1, kurssiNimi);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Kurssi k = new Kurssi(rs.getString("nimi"));
        k.setId(rs.getInt("id"));
        stmt.close();
        rs.close();

        conn.close();

        return k;
    }
    

    @Override
    public void delete(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kurssi WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

}
