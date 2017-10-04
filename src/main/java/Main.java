import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Main {

    static final String IDENTIFIER_FORMAT_EXCEPTION = "Space in Identifier not allowed";
    static final String IDENTIFIER_BLANK_EXCEPTION = "An Identifier has to have a name";

    Scanner input;
    PrintStream out;
    HashMap<Identifier, Set<BigInteger>> data; //Set to be created

    Main(){
        out = new PrintStream(System.out);
        data = new HashMap<Identifier, Set<BigInteger>>();
    }



    Set parseInput(Scanner input)throws APException {
        Set result = null;
        if (nextCharIsLetter(input)){
            parseIdentifier(input);
        } else {
            if (nextCharIs(input,'{')){
                result = parseSet(input);
            } else {
                if (nextCharIs(input,'(')) {
                    result = parseComplexFactor(input);
                }

            }}
        return result;
    }

    void parseIdentifier(Scanner input) throws APException {
        input.useDelimiter("=");
        Scanner IdentifierScanner = new Scanner(input.next());

        String identifierName = IdentifierScanner.next();
        if (identifierName.equals("")) {
            throw new APException(IDENTIFIER_BLANK_EXCEPTION);
        }

        Identifier tempID = new Identifier();
        if(!tempID.appendValidIdentifier(identifierName)) {
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }

        input.skip("=");
        Set tempSet = parseInput(input);

        //store new id/set combo in hashmap??
    }

    Set parseSet(Scanner input) throws APException{
        Scanner statementScanner = new Scanner(input.next());
        //parse statement
    }

    Set parseComplexFactor(Scanner input) throws APException{
        Scanner statementScanner = new Scanner(input.next());
        //parse statement
    }



    private boolean nextCharIsLetter(Scanner input){
        return input.hasNext("[a-zA-Z]");
    }
   /* private boolean nextCharIsAccolade(Scanner input) {
        return input.hasNext("[{]");
    }
    private boolean nextCharIsParenthesis(Scanner input) {
        return input.hasNext("[(]");
    }*/

    private boolean nextCharIs(Scanner input, char c){
        return input.hasNext(Pattern.quote(c+""));
    }


    private void start() {
        input = new Scanner(System.in);
        while (input.hasNextLine()) {
            try {
                parseInput(input);
            }catch(APException exception){
                out.println(exception);
            }
        }
    }

    public static void main(String[] argv) {
        new Main().start();
    }
}
