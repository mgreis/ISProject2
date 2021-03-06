/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.jpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Classe que representa um entrada na playlist.
 *
 * @author Mário
 * @author Flávio J. Saraiva
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"playlist_id", "position"}))
public class PlaylistFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MusicFile musicFile;

    @Column(nullable = false)
    private int position;

    public PlaylistFile() {
    }

    public PlaylistFile(Playlist playlist, MusicFile musicFile) {
        this.playlist = playlist;
        this.musicFile = musicFile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the playlist
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * @param playlist the playlist to set
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * @return the musicFile
     */
    public MusicFile getMusicFile() {
        return musicFile;
    }

    /**
     * @param musicFile the musicFile to set
     */
    public void setMusicFile(MusicFile musicFile) {
        this.musicFile = musicFile;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param index the position to set
     */
    public void setPosition(int index) {
        this.position = index;
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
        if (!(object instanceof PlaylistFile)) {
            return false;
        }
        PlaylistFile other = (PlaylistFile) object;
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
        buf.append(", playlist=");
        buf.append(playlist != null ? playlist.getId() : null);
        buf.append(", musicFile=");
        buf.append(musicFile != null ? musicFile.getId() : null);
        buf.append(", position=");
        buf.append(position);
        buf.append(" ]");
        return buf.toString();
    }

}
