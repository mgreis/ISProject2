/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import java.util.Map;

/**
 *
 * @author Mário
 */
final class MapEntry implements Map.Entry<Long,String>{
    private final Long key;
    private String value;
    
    public MapEntry(Long key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Long getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String old = this.value;
        this.value = value;
        return old;
    }
}
