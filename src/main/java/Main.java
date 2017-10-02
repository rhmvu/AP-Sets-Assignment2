
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

    static final String SPACE_IDENTIFIER_EXCEPTION = "Space in Identifier not allowed";

    Scanner input;
    PrintStream out;
    HashMap<Identifier, Set<BigInteger>> data; //Set to be created

    Main(){
        out = new PrintStream(System.out);
        data = new HashMap<Identifier, Set<BigInteger>>();
    }



    Set parseInput(String statement)throws APException {
        //Scanner statementScanner = new Scanner(statement);
        Set result = null;
        if (nextCharIsLetter(statement)){
            result = parseIdentifier(statement);
        } else {
            if (nextCharIsAccolade(statement)){
                result = parseSet(statement);
            } else {
                if (nextCharIsParenthesis(statement)) {
                    result = parseComplexFactor(statement);
                }

            }}
        return result;
    }

    Set parseIdentifier(String statement) throws APException {
        Scanner statementScanner = new Scanner(statement);
        statementScanner.useDelimiter("=");
        if (statementScanner.hasNext()){
            String identifierName = statementScanner.next();

            if(!identifierName.contains(" ")) {
                Identifier tempID = new Identifier(identifierName);
                statementScanner.skip("=");
                Set tempSet = parseInput(statementScanner.next());
            }else{
                throw new APException(SPACE_IDENTIFIER_EXCEPTION);
            }
        }
        //store new id/set combo in hashmap
        return null;
    }

    Set parseSet(String statement){
        Scanner statementScanner = new Scanner(statement);
        //parse statement
    }

    Set parseComplexFactor(String statement){
        Scanner statementScanner = new Scanner(statement);
        //parse statement
    }



    private boolean nextCharIsLetter(String input){
        return Character.isLetter(input.charAt(0));
    }
    private boolean nextCharIsAccolade(String input) {
        return input.charAt(0) == '{';
    }
    private boolean nextCharIsParenthesis(String input) {
        return input.charAt(0) == '(';
    }

    private void start() {
        input = new Scanner(System.in);
        while (input.hasNextLine()) {
            try {
                parseInput(input.nextLine());
            }catch(APException exception){
                out.println(exception);
            }
        }
    }

    public static void main(String[] argv) {
        new Main().start();
    }
}
