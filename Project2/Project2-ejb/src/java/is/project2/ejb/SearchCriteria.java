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

    private String criteria;//artist, title, artist&title
    private String argument;
    private String argument2;
    
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

    /**
     * @return the argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * @param argument the argument to set
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * @return the argument2
     */
    public String getArgument2() {
        return argument2;
    }

    /**
     * @param argument2 the argument2 to set
     */
    public void setArgument2(String argument2) {
        this.argument2 = argument2;
    }

}
