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
public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public List<Vastaus> findAll() throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus");
        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();

        while (rs.next()) {
            Vastaus t = new Vastaus(rs.getString("vastausteksti"), rs.getBoolean("oikein"), rs.getInt("kysymys_id"));
            t.setId(rs.getInt("id"));
            vastaukset.add(t);
        }

        return vastaukset;
    }

    @Override
    public Vastaus create(Vastaus vastaus) throws Exception {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus"
                + " (vastausteksti, oikein, kysymys_id)"
                + " VALUES (?,?,?)");
        stmt.setString(1, vastaus.getVastausTeksti());
        stmt.setBoolean(2, vastaus.isOikein());
        stmt.setInt(3, vastaus.getKysymys());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Vastaus"
                + " WHERE vastausteksti = ? AND kysymys_id = ?");
        stmt.setString(1, vastaus.getVastausTeksti());
        stmt.setInt(2, vastaus.getKysymys());

        ResultSet rs = stmt.executeQuery();
        rs.next();

        Vastaus a = new Vastaus(rs.getString("vastausteksti"), rs.getBoolean("oikein"), rs.getInt("kysymys_id"));
        a.setId(rs.getInt("id"));

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    @Override
    public Vastaus findOne(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Vastaus a = new Vastaus(rs.getString("vastausteksti"), rs.getBoolean("oikein"), rs.getInt("kysymys_id"));
        a.setId(rs.getInt("id"));
        stmt.close();
        rs.close();

        conn.close();

        return a;

    }

    @Override
    public void delete(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
        public List<Vastaus> findByKysymysId(Integer id) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();

        while (rs.next()) {
            Vastaus t = new Vastaus(rs.getString("vastausteksti"), rs.getBoolean("oikein"), rs.getInt("kysymys_id"));
            t.setId(rs.getInt("id"));
            vastaukset.add(t);
        }

        return vastaukset;
    }

}
