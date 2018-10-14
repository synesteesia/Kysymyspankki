/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kysymyspankki;

import java.util.ArrayList;

/**
 *
 * @author mikko
 */
public class Vastaus {
    private String vastausTeksti;
    private boolean oikein;
    private Integer id;
    private Integer kysymys;

    public Vastaus(String vastausTeksti, boolean oikein, Integer kysymys) {
        this.vastausTeksti = vastausTeksti;
        this.oikein = oikein;
        this.kysymys = kysymys;
        
    }

    public String getVastausTeksti() {
        return vastausTeksti;
    }

    public boolean isOikein() {
        return oikein;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getKysymys() {
        return kysymys;
    }

    public Integer getId() {
        return id;
    }
    
    
    
    
    
    
    
    
}
