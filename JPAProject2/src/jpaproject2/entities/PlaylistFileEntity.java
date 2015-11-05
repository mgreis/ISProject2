/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaproject2.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Mário
 */
@Entity
@Table(name = "PLAYLISTFILE")
@IdClass(value=PlaylistFilePK.class)
public class PlaylistFileEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Id
    @ManyToOne
    @JoinColumn(name="playlist_id")
    private PlaylistEntity playlist;
    @Id
    @ManyToOne
    @JoinColumn(name = "musicfile_id")
    private MusicFileEntity musicFile;
    
    public PlaylistFileEntity(PlaylistEntity playlist,MusicFileEntity musicFile){
        this.setPlaylist(playlist);
        this.setMusicFile(musicFile);
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof PlaylistFileEntity)) {
            return false;
        }
        PlaylistFileEntity other = (PlaylistFileEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PlaylistFileEntity[ id=" + id + " ]";
    }

    /**
     * @return the playlist
     */
    public PlaylistEntity getPlaylist() {
        return playlist;
    }

    /**
     * @param playlist the playlist to set
     */
    public void setPlaylist(PlaylistEntity playlist) {
        this.playlist = playlist;
    }

    /**
     * @return the musicFile
     */
    public MusicFileEntity getMusicFile() {
        return musicFile;
    }

    /**
     * @param musicFile the musicFile to set
     */
    public void setMusicFile(MusicFileEntity musicFile) {
        this.musicFile = musicFile;
    }
    
}
