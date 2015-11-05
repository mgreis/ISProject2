/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateful;

import javax.ejb.Remote;

/**
 *
 * @author Mário
 */
@Remote
public interface ShopBasketInterface {
    public void setA(String a);
    public String getA();
}
