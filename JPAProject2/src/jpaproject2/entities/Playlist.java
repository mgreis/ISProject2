/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaproject2.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

/**
 * Classe que representa uma playlist de uma pessoa.
 *
 * @author Mário
 * @author Flávio J. Saraiva
 */
@Entity
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Person owner;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "playlist")
    @OrderColumn(name = "index")
    private Collection<PlaylistFile> playlistFiles;

    public Playlist() {
    }

    public Playlist(String name, Person owner) {
        this.name = name;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the owner
     */
    public Person getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Person owner) {
        this.owner = owner;
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
     * @return the playlistFiles
     */
    public Collection<PlaylistFile> getPlaylistFiles() {
        if (playlistFiles == null) {
            playlistFiles = new ArrayList<>();
        }
        return playlistFiles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Playlist)) {
            return false;
        }
        Playlist other = (Playlist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(getClass().getSimpleName());
        buf.append("[ id=");
        buf.append(id);
        buf.append(", owner=");
        buf.append(owner != null ? owner.getId() : null);
        buf.append(", name=");
        buf.append(name);
        buf.append(", playlistFiles={");
        boolean comma = false;
        for (PlaylistFile playlistFile : getPlaylistFiles()) {
            if (comma) {
                buf.append(",");
            }
            buf.append(playlistFile.getId());
            comma = true;
        }
        buf.append("} ]");
        return buf.toString();
    }

}
