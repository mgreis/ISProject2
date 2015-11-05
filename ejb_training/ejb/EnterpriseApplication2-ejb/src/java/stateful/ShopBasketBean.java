package stateful;

import javax.ejb.Stateful;


@Stateful
public class ShopBasketBean implements ShopBasketInterface {
    private String a = "bogus";
    

    /**
     * Default constructor.
     */
    public ShopBasketBean() {
    }

    /**
     * @return the a
     */
    public String getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(String a) {
        this.a = a;
    }
    
  
    

}