/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.amikko.kysymyspankki;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws Exception;

    List<T> findAll() throws Exception;

    T create(T object) throws Exception;

    void delete(K key) throws Exception;
}
