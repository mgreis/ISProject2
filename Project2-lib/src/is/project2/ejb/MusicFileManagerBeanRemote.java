/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import java.util.Collection;
import javax.ejb.Remote;

/**
 * Manage music files.
 *
 * @author Flávio J. Saraiva
 */
@Remote
public interface MusicFileManagerBeanRemote {

    /**
     * Get all my music files.
     *
     * @param accountId Account id.
     * @return Collection of music files.
     * @throws MusicAppException If an error occurs.
     */
    public Collection<MusicFileData> getMine(Long accountId) throws MusicAppException;

    /**
     * Get all the music files that are no mine.
     *
     * @param accountId Account id.
     * @return List of music files of an account.
     * @throws MusicAppException If an error occurs.
     */
    public Collection<MusicFileData> getOther(Long accountId) throws MusicAppException;

    /**
     * Get all music files that are not mine and match the search criteria.
     *
     * @param accountId Account id.
     * @param criteria Search criteria.
     * @return Collection of music files.
     * @throws MusicAppException If an error occurs.
     */
    public Collection<MusicFileData> findOther(Long accountId, SearchCriteria criteria) throws MusicAppException;

}
