package validation;
import java.util.*;
import java.util.regex.Pattern;



public class Validation {
    public static final String validateGender = null;
    public static final String RED="\u001B[31m";
    public static final String RESET="\u001B[0m";
    private static Pattern Username_PATTERN =Pattern.compile("^[a-zA-Z]+$");
    private static Pattern Password_Pattern=Pattern.compile("^[0-9]+$");
    private static Pattern Location_Pattern=Pattern.compile("^[a-zA-Z]+$");

    public static Object validateUsername;
    Scanner sc = new Scanner(System.in);

    public String validateUsername() {
        String username;
        while (true) {
            System.out.println("");
            username = sc.nextLine();
            if (!Username_PATTERN.matcher(username).matches()) 
            {
                System.out.println(RED+"SORRY ! PLEASE ENTER VALID Input "+RESET);
            } else 
            {
                break;
            }
        }
        return username;
    }
    public String validatePassword(){
        String password;
        while (true) {
            System.out.println("");
            password = sc.nextLine();
            if (!Password_Pattern.matcher(password).matches()) 
            {
                System.out.println(RED+"SORRY ! PLEASE ENTER VALID INPUT "+RESET);
            } else
            {
                break;
            }
        }
        return password;
    }
    public String validateLocation()
    {
        String location;
        while(true){
            System.out.println("");
            location=sc.nextLine();
            if(!Location_Pattern.matcher(location).matches()){
                System.out.println(RED+"Enter valid Location. Check if there is any space in your input"+RESET);
            }
            else
            {
                break;
            }
        }
        return location;
    }
}