/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import java.util.Collection;
import java.util.Map;
import javax.ejb.Remote;

/**
 * Bean interface manage playlists.
 *
 * @author Flávio J. Saraiva
 */
@Remote
public interface PlaylistManagerBeanRemote {

    /**
     * List playlists.
     *
     * @param accountId Account id.
     * @param sort Sort order.
     * @return List of playlist id-name pairs.
     * @throws MusicAppException If an error occurs.
     */
    public Collection<Map.Entry<Long, String>> list(Long accountId, SortOrder sort) throws MusicAppException;

    /**
     * Creates a new playlist.
     *
     * @param accountId Account id.
     * @param name Playlist name.
     * @return Playlist id.
     * @throws MusicAppException If an error occurs.
     */
    public Long create(Long accountId, String name) throws MusicAppException;

    /**
     * Loads playlist data.
     *
     * @param accountId Account id.
     * @param playlistId Playlist id.
     * @return Playlist data.
     * @throws MusicAppException If an error occurs.
     */
    public PlaylistData load(Long accountId, Long playlistId) throws MusicAppException;

    /**
     * Saves playlist data.
     *
     * @param accountId Account id.
     * @param playlist Playlist data.
     * @throws MusicAppException If an error occurs.
     */
    public void save(Long accountId, PlaylistData playlist) throws MusicAppException;

    /**
     * Deletes a playlist.
     *
     * @param accountId Account id.
     * @param playlistId Playlist id.
     * @throws MusicAppException If an error occurs.
     */
    public void delete(Long accountId, Long playlistId) throws MusicAppException;

}
