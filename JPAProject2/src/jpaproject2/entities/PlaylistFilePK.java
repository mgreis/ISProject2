/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaproject2.entities;

import java.io.Serializable;

/**
 *
 * @author Mário
 */
public class PlaylistFilePK implements Serializable {

    private PlaylistEntity playlist;
    private MusicFileEntity musicFile;
    private Long id;
    
    public PlaylistFilePK(PlaylistEntity playlist,MusicFileEntity musicFile, Long id){
        this.setPlaylist(playlist);
        this.setMusicFile(musicFile);
        this.setId(id);
        
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getPlaylist() != null ? this.getPlaylist().hashCode() : 0);
        hash += (this.getMusicFile() != null ? this.getMusicFile().hashCode() : 0);
        hash += (this.getId() != null ? this.getMusicFile().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaylistFilePK)) {
            return false;
        }
        PlaylistFilePK other = (PlaylistFilePK) object;
        if (!other.musicFile.equals(musicFile)) {
            return false;
        }
        if (!other.playlist.equals(playlist)) {
            return false;
        }
        if (!other.id.equals(id)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "jpaproject2.entities.PlaylistFilePK[ id=" + getPlaylist() + getMusicFile() + getId() + " ]";
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
