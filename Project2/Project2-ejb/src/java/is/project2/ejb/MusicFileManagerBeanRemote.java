/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import javax.ejb.Remote;

/**
 * Bean interface to manage music files.
 *
 * @author Flávio J. Saraiva
 */
@Remote
public interface MusicFileManagerBeanRemote {

    /**
     * Upload a new music.
     *
     * @param music Music upload data.
     * @return Music id.
     * @throws MusicAppException If an error occurs.
     */
    public Long upload(MusicUploadData music) throws MusicAppException;

    /**
     * Load the target music data.
     *
     * @param id Music id.
     * @return Music information.
     * @throws MusicAppException If an error occurs.
     */
    public MusicData load(Long id) throws MusicAppException;

    /**
     * Load the target music data.
     *
     * The order is preserved.
     *
     * @param ids Collection of music ids.
     * @return Music information.
     * @throws MusicAppException If an error occurs.
     */
    public MusicData[] load(Long[] ids) throws MusicAppException;

    /**
     * Load all my music.
     *
     * @param accountId Account id.
     * @return Collection of music files.
     * @throws MusicAppException If an error occurs.
     */
    public MusicData[] loadAllMine(Long accountId) throws MusicAppException;

    /**
     * Load all the music that is not mine.
     *
     * @param accountId Account id.
     * @return List of music files of an account.
     * @throws MusicAppException If an error occurs.
     */
    public MusicData[] loadAllOther(Long accountId) throws MusicAppException;

    /**
     * Find music that is not mine according to the search criteria.
     *
     * @param accountId Account id.
     * @param criteria Search criteria.
     * @return Collection of music files.
     * @throws MusicAppException If an error occurs.
     */
    public MusicData[] findOther(Long accountId, SearchCriteria criteria) throws MusicAppException;

    /**
     * Save the music data.
     *
     * @param accountId Account id.
     * @param music Music data.
     * @throws MusicAppException If an error occurs.
     */
    public void save(Long accountId, MusicData music) throws MusicAppException;

}
