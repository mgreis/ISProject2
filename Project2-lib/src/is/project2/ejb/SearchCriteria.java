/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

/**
 * Search for the target string.
 *
 * @author Flávio J. Saraiva
 */
public class SearchCriteria {

    private String criteria;
    
    public SearchCriteria(String criteria) {
        assert(criteria != null);
        this.criteria = criteria;
    }
    
    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

}
