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
public class Kysymys {

    private String kysymysTeksti;
    private List<Vastaus> vastaukset;
    private Integer id;
    private Integer aihe;

    public Kysymys(String teksti, Integer aihe) {
        this.kysymysTeksti = teksti;
        this.vastaukset = new ArrayList<>();
        this.aihe = aihe;

    }

    public String getKysymysTeksti() {
        return kysymysTeksti;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAihe() {
        return aihe;
    }

    public Integer getId() {
        return id;
    }

    public List<Vastaus> getVastaukset() {
        return vastaukset;
    }

    public void setVastaukset(List<Vastaus> vastaukset) {
        this.vastaukset = vastaukset;
    }

}
