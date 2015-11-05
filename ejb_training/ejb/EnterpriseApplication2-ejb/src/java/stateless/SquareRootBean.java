package stateless;

import javax.ejb.Stateless;

@Stateless
public class SquareRootBean implements SquareRootBeanInterface {

    private double number;

    /**
     * Default constructor.
     */
    public SquareRootBean() {
    }
    
    /**
     * calculates the square root of a real number;
     * @param number
     * @return 
     */
    public double calculateSquareRoot(double number){
        // if the number is negative there won't be a real square root so we return a negative number;
        if (number<0)
        {
           return -Math.sqrt(-number);
        }
            
        else{
            return Math.sqrt(number);
        }
        
    }
    

    /**
     * @return the number
     */
    public double getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(double number) {
        this.number = number;
    }

}



