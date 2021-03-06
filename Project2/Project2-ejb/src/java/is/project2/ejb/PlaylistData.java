/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import java.util.ArrayList;

/**
 *
 * @author Flávio J. Saraiva
 */
public class PlaylistData {

    private final Long id;
    private String name;
    private ArrayList<Long> music;

    public PlaylistData(Long id) {
        assert (id != null);
        this.id = id;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the music
     */
    public ArrayList<Long> getMusic() {
        if (music == null) {
            music = new ArrayList<>();
        }
        return music;
    }
}
