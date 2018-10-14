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
public class Kurssi {

    private String nimi;
    private List<Aihe> aiheet;
    private Integer id;

    public Kurssi(String nimi) {
        this.nimi = nimi;
        this.aiheet = new ArrayList<>();

    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAiheet(List<Aihe> aiheet) {
        this.aiheet = aiheet;
    }

    public List<Aihe> getAiheet() {
        return aiheet;
    }
    
    

}
