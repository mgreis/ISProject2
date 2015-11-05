package shopbasketclient;

import java.util.Scanner;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import stateful.ShopBasketInterface;

public class ShopBasketClient {

    /**
     * @param args
     * @throws NamingException
     */
    public static void main(String[] args) throws NamingException {
        
        ShopBasketInterface shopBasket = (ShopBasketInterface) InitialContext.doLookup
        ("EnterpriseApplication2-ejb/ShopBasketBean!stateful.ShopBasketInterface");
        //System.out.println(shopBasket.calculateSquareRoot(consoleInterface()));
        //System.out.println("Very impressive!");
        consoleInterface(shopBasket);
    }
    
    private static void consoleInterface(ShopBasketInterface shopBasket) {
        
        Scanner keyboard = new Scanner(System.in);
        while(true){
        System.out.print("1- set value\n2- get value\nPlease insert a number: ");
        int input = keyboard.nextInt();
        if (input==1){
            String inputString = keyboard.next();
            shopBasket.setA(inputString);
        }
        if (input==2){
            System.out.println("\nString: "+shopBasket.getA());
        }
        }

    }
    
}
