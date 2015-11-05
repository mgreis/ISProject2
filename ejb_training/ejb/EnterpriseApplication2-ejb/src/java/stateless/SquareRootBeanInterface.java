package stateless;

import javax.ejb.Remote;

@Remote
public interface SquareRootBeanInterface {

    //calculates the square root of a real number;
    public double calculateSquareRoot(double number);
}
