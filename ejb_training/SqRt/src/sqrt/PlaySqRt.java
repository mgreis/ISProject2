package sqrt;

import java.util.Scanner;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import stateless.SquareRootBeanInterface;

public class PlaySqRt {

    /**
     * @param args
     * @throws NamingException
     */
    public static void main(String[] args) throws NamingException {
        
        SquareRootBeanInterface sqrt = (SquareRootBeanInterface) InitialContext.doLookup
        ("EnterpriseApplication2-ejb/SquareRootBean!stateless.SquareRootBeanInterface");
        System.out.println(sqrt.calculateSquareRoot(consoleInterface()));
        System.out.println("Very impressive!");
    }
    
    private static double consoleInterface() {

        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please insert a number: ");
        double reference = keyboard.nextDouble();
        return reference;

    }
    
}
