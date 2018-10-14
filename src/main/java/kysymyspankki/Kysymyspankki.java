package kysymyspankki;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.utils.StringUtils;

public class Kysymyspankki {

    public static void main(String[] args) throws Exception {

        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        File tiedosto = new File("db", "kysymyspankki.db");
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());

        createTables(database);

        KysymysDao kysymysDao = new KysymysDao(database);
        KurssiDao kurssiDao = new KurssiDao(database);
        AiheDao aiheDao = new AiheDao(database);
        VastausDao vastausDao = new VastausDao(database);

        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Kurssi> kurssit = kurssiDao.findAll();
            for (int i = 0; i < kurssit.size(); i++) {
                Kurssi kurssiI = kurssit.get(i);
                List<Aihe> aiheetL = aiheDao.findByCourseId(kurssiI.getId());
                kurssiI.setAiheet(aiheetL);
                for (int ii = 0; ii < aiheetL.size(); ii++) {
                    Aihe aiheI = aiheetL.get(ii);
                    List<Kysymys> kysymyksetL = kysymysDao.findByAiheId(aiheI.getId());
                    aiheI.setKysymykset(kysymyksetL);
                }
            }
            map.put("kurssit", kurssit);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/kysymys/:id", (request, response) -> {
            HashMap map = new HashMap<>();
            String id = request.params(":id");

            int kysymysIdInt;
            boolean pyyntoOK = true;

            try {
                kysymysIdInt = Integer.parseInt(id);
                Kysymys kysymys = kysymysDao.findOne(kysymysIdInt);

                if (kysymys == null) {
                    pyyntoOK = false;
                }

                List<Vastaus> vastaukset = vastausDao.findByKysymysId(kysymys.getId());
                kysymys.setVastaukset(vastaukset);

                Aihe aihe = aiheDao.findOne(kysymys.getAihe());
                Kurssi kurssi = kurssiDao.findOne(aihe.getKurssi());

                map.put("kurssi", kurssi);
                map.put("aihe", aihe);
                map.put("kysymys", kysymys);
            } catch (NumberFormatException e) {

                System.out.println(e);
                pyyntoOK = false;
            }

            if (!pyyntoOK) {
                List<Kurssi> kurssit = kurssiDao.findAll();
                for (int i = 0; i < kurssit.size(); i++) {
                    Kurssi kurssiI = kurssit.get(i);
                    List<Aihe> aiheetL = aiheDao.findByCourseId(kurssiI.getId());
                    kurssiI.setAiheet(aiheetL);
                    for (int ii = 0; ii < aiheetL.size(); ii++) {
                        Aihe aiheI = aiheetL.get(ii);
                        List<Kysymys> kysymyksetL = kysymysDao.findByAiheId(aiheI.getId());
                        aiheI.setKysymykset(kysymyksetL);
                    }
                }
                map.put("kurssit", kurssit);

                return new ModelAndView(map, "index");
            }

            return new ModelAndView(map, "kysymysNakyma");
        }, new ThymeleafTemplateEngine());

        Spark.post("/kysymys", (request, response) -> {

            String teksti = request.queryParams("kysymysteksti");
            String aiheNimi = request.queryParams("aiheNimi");
            String kurssiNimi = request.queryParams("kurssiNimi");

            if (StringUtils.isEmpty(teksti) || StringUtils.isEmpty(aiheNimi)
                    || StringUtils.isEmpty(kurssiNimi)) {

                response.redirect("/");
            }

            Kurssi kurssi = kurssiDao.findByName(kurssiNimi);
            if (kurssi == null) {
                kurssi = kurssiDao.create(new Kurssi(kurssiNimi));
            }
            Aihe aihe = aiheDao.findByCourseIDandName(kurssi.getId(), aiheNimi);
            if (aihe == null) {
                aihe = aiheDao.create(new Aihe(aiheNimi, kurssi.getId()));
            }
            Kysymys kysymys = kysymysDao.findByAiheIDandTeksti(aihe.getId(), teksti);
            if (kysymys == null) {
                kysymysDao.create(new Kysymys(teksti, aihe.getId()));
            }
            response.redirect("/");

            return "";
        });

        Spark.post("/kysymys/:id", (req, res) -> {
            String vastausTeksti = req.queryParams("vastausTeksti");
            String oikein = req.queryParams("oikein");
            String kysymysId = req.params(":id");
            boolean oikeinB = StringUtils.isNotEmpty(oikein);
            int kysymysIdInt;

            try {
                kysymysIdInt = Integer.parseInt(kysymysId);
            } catch (NumberFormatException e) {
                System.out.println(e);
                res.redirect("/");
                return "";
            }

            if (StringUtils.isEmpty(vastausTeksti)) {
                res.redirect("/kysymys/" + kysymysId);
            }

            try {
                Vastaus uusi = new Vastaus(vastausTeksti, oikeinB, kysymysIdInt);

                vastausDao.create(uusi);
            } catch (Exception ex) {
                System.out.println(ex);
            }

            res.redirect("/kysymys/" + kysymysId);
            return "";
        });

        Spark.post("/kysymys/delete/:id", (req, res) -> {
            try {
                Integer id = Integer.parseInt(req.params(":id"));
                Kysymys kysymys = kysymysDao.findOne(id);
                Aihe aihe = aiheDao.findOne(kysymys.getAihe());
                Kurssi kurssi = kurssiDao.findOne(aihe.getKurssi());

                kysymysDao.delete(id);

                if (kysymysDao.findByAiheId(aihe.getId()).isEmpty()) {
                    aiheDao.delete(aihe.getId());

                    if (aiheDao.findByCourseId(kurssi.getId()).isEmpty()) {
                        kurssiDao.delete(kurssi.getId());
                    }
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }
            res.redirect("/");
            return "";
        });

        Spark.post("/kysymys/:id/vastaus/delete/:vid", (req, res) -> {
            String id = req.params(":vid");
            String url = "/";
            try {
                vastausDao.delete(Integer.parseInt(id));
                String kid = req.params(":id");
                int kidI = Integer.parseInt(kid);
                url = "/kysymys/" + kid;

            } catch (Exception ex) {
                System.out.println(ex);
            }
            res.redirect(url);
            return "";
        });

    }

    public static void createTables(Database database) throws Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt
                = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Kurssi ("
                + "id integer PRIMARY KEY,"
                + "nimi varchar(50))");
        stmt.executeUpdate();

        stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Aihe ("
                + "id integer PRIMARY KEY,"
                + "nimi varchar(50),"
                + "kurssi_id integer,"
                + "FOREIGN KEY (kurssi_id) REFERENCES Kurssi(id))");
        stmt.executeUpdate();

        stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Kysymys ("
                + "id integer PRIMARY KEY,"
                + "teksti varchar(500),"
                + "aihe_id integer,"
                + "FOREIGN KEY (aihe_id) REFERENCES Aihe(id))");
        stmt.executeUpdate();

        stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Vastaus ("
                + "id integer PRIMARY KEY,"
                + "vastausteksti varchar(200),"
                + "oikein boolean,"
                + "kysymys_id integer,"
                + "FOREIGN KEY (kysymys_id) REFERENCES Kysymys(id))");
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
