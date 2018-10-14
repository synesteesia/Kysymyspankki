package fi.amikko.kysymyspankki;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.utils.StringUtils;

public class Kysymyspankki {

    public static void main(String[] args) throws Exception {
        File tiedosto = new File("db", "kysymyspankki.db");
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());

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

            Kysymys kysymys = kysymysDao.findOne(Integer.parseInt(id));
            List<Vastaus> vastaukset = vastausDao.findByKysymysId(kysymys.getId());
            kysymys.setVastaukset(vastaukset);

            Aihe aihe = aiheDao.findOne(kysymys.getAihe());
            Kurssi kurssi = kurssiDao.findOne(aihe.getKurssi());

            map.put("kurssi", kurssi);
            map.put("aihe", aihe);
            map.put("kysymys", kysymys);

            return new ModelAndView(map, "kysymysNakyma");
        }, new ThymeleafTemplateEngine());

        Spark.post("/kysymys", (request, response) -> {

            String teksti = request.queryParams("kysymysteksti");
            String aiheNimi = request.queryParams("aiheNimi");
            String kurssiNimi = request.queryParams("kurssiNimi");
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

            try {
                Vastaus uusi = new Vastaus(vastausTeksti, oikeinB, Integer.parseInt(kysymysId));

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
}
