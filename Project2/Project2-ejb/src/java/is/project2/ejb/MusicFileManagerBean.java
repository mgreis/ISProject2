/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import javax.ejb.Stateless;

/**
 *
 * @author Flávio
 */
@Stateless
public class MusicFileManagerBean implements MusicManagerBeanRemote {

    @Override
    public Long upload(MusicUploadData music) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MusicData load(Long id) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MusicData[] load(Long[] ids) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MusicData[] loadAllMine(Long accountId) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MusicData[] loadAllOther(Long accountId) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MusicData[] findOther(Long accountId, SearchCriteria criteria) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(Long accountId, MusicData music) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
