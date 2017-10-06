import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Main {

    static final String IDENTIFIER_FORMAT_EXCEPTION = "Space in Identifier not allowed",
                        IDENTIFIER_BLANK_EXCEPTION = "An Identifier has to have a name",
                        INVALID_STATEMENT = "Invalid statement, please read the documentation",
                        HELP_MESSAGE = "This Set interpreter works with operators +,-,* and Sets containing big Integers\n"
                                        + "Set Interpreter REQUIRES you to omit spaces in your commands.\n"
                                        +"However, you can use spaces and run the program with '--omit-spaces' to bypass this.\n\n"
                                        +"Allowed statements:\n?<Set/Factor> to output a set or factor\n"
                                        +"<Identifier>=<Set/Factor> to assign a Set to an Identifier.\n\n"
                                        + "Set Interpreter by Kostas Moumtzakis & Ruben van der Ham";

    Scanner input;
    PrintStream out;
    HashMap<Identifier, Set<BigInteger>> variables; //Set to be created

    Main(){
        out = new PrintStream(System.out);
        variables = new HashMap<Identifier, Set<BigInteger>>();
    }



    void parseStatement(Scanner input)throws APException {
        if (nextCharIs(input, '/')) {
            return; //comment, so this is skipped
        } else {
            if (nextCharIsLetter(input)) {
                parseAssignment(input);
            } else {
                if (nextCharIs(input, '?')) {
                    parsePrint(input);
                } else {
                    throw new APException(INVALID_STATEMENT);

                }
            }
        }
    }

    Set parseFactor(Scanner input) throws APException{
        Set result = null;
        if (nextCharIsLetter(input)){
            result = getSetByID(input);
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


    void parseAssignment(Scanner input) throws APException {
        input.useDelimiter("=");
        Scanner identifierScanner = new Scanner(input.next());
        Identifier tempIdentifier = parseIdentifier(identifierScanner);

        input.skip("=");
        Set tempSet = parseFactor(input);
        variables.put(tempIdentifier,tempSet);
    }

    Identifier parseIdentifier(Scanner input) throws APException{
        String identifierName = input.next();
        if (identifierName.equals("")) {
            throw new APException(IDENTIFIER_BLANK_EXCEPTION);
        }

        Identifier tempID = new Identifier();
        if(!tempID.appendValidIdentifier(identifierName)) {
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
        return tempID;
    }


    void parsePrint(Scanner input) throws APException{
        input.//print something??
    }


    Set parseSet(Scanner input) throws APException{
        Scanner statementScanner = new Scanner(input.next());
        //parse statement
    }

    Set parseComplexFactor(Scanner input) throws APException{
        Scanner statementScanner = new Scanner(input.next());
        //parse statement
    }
    Set getSetByID(Scanner input) throws APException{

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


    private void start(boolean omitSpaces) {
        input = new Scanner(System.in);
        while (input.hasNextLine()) {
            try {
                if(omitSpaces) {
                    String statement = input.nextLine();
                    statement = statement.replace(" ", "");
                    Scanner spaceLess = new Scanner(statement);
                    parseStatement(spaceLess);
                }else {
                    parseStatement(input);
                }
            }catch(APException exception){
                out.println(exception);
            }
        }
    }

    public static void main(String[] argv) {
        try{
            if(argv[0].equals("--omit-spaces")){
                new Main().start(true);
            }
            if (argv[0].equals("--help")){
                System.out.println(HELP_MESSAGE);
            }
        }catch (NullPointerException e){};
        new Main().start(false);
    }
}
