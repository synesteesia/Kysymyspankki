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
public class KysymysDao implements Dao<Kysymys, Integer> {

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public List<Kysymys> findAll() throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys");
        ResultSet rs = stmt.executeQuery();
        List<Kysymys> kysymykset = new ArrayList<>();

        while (rs.next()) {
            Kysymys t = new Kysymys(rs.getString("teksti"), rs.getInt("aihe_id"));
            t.setId(rs.getInt("id"));
            kysymykset.add(t);
        }

        return kysymykset;
    }

    @Override
    public Kysymys create(Kysymys kysymys) throws Exception {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys"
                + " (teksti, aihe_id)"
                + " VALUES (?, ?)");
        stmt.setString(1, kysymys.getKysymysTeksti());
        stmt.setInt(2, kysymys.getAihe());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Kysymys"
                + " WHERE teksti = ? AND aihe_id = ?");
        stmt.setString(1, kysymys.getKysymysTeksti());
        stmt.setInt(2, kysymys.getAihe());

        ResultSet rs = stmt.executeQuery();
        rs.next();

        Kysymys a = new Kysymys(rs.getString("teksti"), rs.getInt("aihe_id"));
        a.setId(rs.getInt("id"));

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    @Override
    public Kysymys findOne(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Kysymys a = new Kysymys(rs.getString("teksti"), rs.getInt("aihe_id"));
        a.setId(rs.getInt("id"));
        stmt.close();
        rs.close();

        conn.close();

        return a;

    }

    @Override
    public void delete(Integer key) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public List<Kysymys> findByAiheId(int aiheId) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE aihe_id = ?");
        stmt.setInt(1, aiheId);
        ResultSet rs = stmt.executeQuery();
        List<Kysymys> kysymykset = new ArrayList<>();

        while (rs.next()) {
            Kysymys t = new Kysymys(rs.getString("teksti"), rs.getInt("aihe_id"));
            t.setId(rs.getInt("id"));
            kysymykset.add(t);
        }

        return kysymykset;

    }

    public Kysymys findByAiheIDandTeksti(int id, String teksti) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE aihe_id = ? AND teksti = ?");
        stmt.setInt(1, id);
        stmt.setString(2, teksti);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Kysymys a = new Kysymys(rs.getString("teksti"), rs.getInt("aihe_id"));
        a.setId(rs.getInt("id"));
        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

}
