/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.amikko.kysymyspankki;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mikko
 */
public class Aihe {
    private String nimi;
    private List<Kysymys> kysymykset;
    private Integer id;
    private Integer kurssi;

    public Aihe(String nimi, Integer kurssi) {
        this.nimi = nimi;
        this.kysymykset = new ArrayList<>();
        this.kurssi = kurssi;
    }

    public String getNimi() {
        return nimi;
    }

    public List<Kysymys> getKysymykset() {
        return kysymykset;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getKurssi() {
        return kurssi;
    }

    public Integer getId() {
        return id;
    }

    public void setKysymykset(List<Kysymys> kysymykset) {
        this.kysymykset = kysymykset;
    }
    
    
    
    
    
    
    
    
}
