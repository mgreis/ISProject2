/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.jpa.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 * Classe que representa um ficheiro de música de uma conta.
 *
 * @author Mário
 * @author Flávio J. Saraiva
 */
@Entity
public class MusicFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Account owner;

    private String title;

    private String artist;

    private String album;
    
    private byte[] fileData;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date releaseYear;

    @Column(nullable = false)
    private String filePath;

    public MusicFile() {
    }

    public MusicFile(Account owner, String title, String artist, String album, Date releaseYear, String filePath, byte[] fileData) {
        this.owner = owner;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseYear = releaseYear;
        this.filePath = filePath;
        this.fileData = fileData;
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
    public Account getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Account owner) {
        this.owner = owner;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * @param album the album to set
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * @return the releaseYear
     */
    public Date getReleaseYear() {
        return releaseYear;
    }

    /**
     * @param releaseYear the releaseYear to set
     */
    public void setReleaseYear(Date releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MusicFile)) {
            return false;
        }
        MusicFile other = (MusicFile) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
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
        buf.append(", title=");
        buf.append(title);
        buf.append(", artist=");
        buf.append(artist);
        buf.append(", album=");
        buf.append(album);
        buf.append(", releaseYear=");
        buf.append(releaseYear);
        buf.append(", filePath=");
        buf.append(filePath);
        buf.append(" ]");
        return buf.toString();
    }

    /**
     * @return the fileData
     */
    public byte[] getFileData() {
        return fileData;
    }

    /**
     * @param fileData the fileData to set
     */
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

}
