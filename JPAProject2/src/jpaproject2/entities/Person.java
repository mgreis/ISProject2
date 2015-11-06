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
import javax.persistence.OneToMany;

/**
 * Classe que representa uma pessoa.
 *
 * @author Mário
 * @author Flávio J. Saraiva
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner")
    private Collection<MusicFile> musicFiles;

    @OneToMany(mappedBy = "owner")
    private Collection<Playlist> playlists;

    public Person() {
    }

    public Person(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the musicFiles
     */
    public Collection<MusicFile> getMusicFiles() {
        if (musicFiles == null) {
            musicFiles = new ArrayList<>();
        }
        return musicFiles;
    }

    /**
     * @return the playlists
     */
    public Collection<Playlist> getPlaylists() {
        if (playlists == null) {
            playlists = new ArrayList<>();
        }
        return playlists;
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
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
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
        buf.append(", email=");
        buf.append(email);
        buf.append(", password=");
        buf.append(password);
        buf.append(", musicFiles={");
        boolean comma = false;
        for (MusicFile musicFile : getMusicFiles()) {
            if (comma) {
                buf.append(", ");
            }
            buf.append(musicFile.getId());
            comma = true;
        }
        buf.append("}, playlists={");
        comma = false;
        for (Playlist playlist : getPlaylists()) {
            if (comma) {
                buf.append(", ");
            }
            buf.append(playlist.getId());
            comma = true;
        }
        buf.append("} ]");
        return buf.toString();
    }

}
