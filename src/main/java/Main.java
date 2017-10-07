import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.Stack;

public class Main {

    static final String IDENTIFIER_FORMAT_EXCEPTION = "Space in Identifier not allowed",
                        IDENTIFIER_BLANK_EXCEPTION = "An Identifier has to have a name",
                        INVALID_STATEMENT = "Invalid statement, please read the documentation",
                        IDENTIFIER_NOT_FOUND = ", parsed as identifier has no corresponding Set",
                        HELP_MESSAGE = "This Set interpreter works with operators +,-,* and Sets containing big Integers\n"
                                        + "Set Interpreter REQUIRES you to omit spaces in your commands.\n"
                                        +"However, you can use spaces and run the program with '--omit-spaces' to bypass this.\n\n"
                                        +"Allowed statements:\n?<Set/Factor> to output a set or factor\n"
                                        +"<Identifier>=<Set/Factor> to assign a Set to an Identifier.\n\n"
                                        + "Set Interpreter by Kostas Moumtzakis & Ruben van der Ham";

    Scanner input;
    PrintStream out;
    HashMap<Identifier, Set<BigInteger>> variables;

    Main(){
        out = new PrintStream(System.out);
        variables = new HashMap<Identifier, Set<BigInteger>>();
    }



    void parseStatement(Scanner input)throws APException {
        skipSpaces();
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
        skipSpaces();
        Set result = null;
        if (nextCharIsLetter(input)){
            result = getSetByID(input);
        } else {
            if (nextCharIs(input,'{')){
                input.useDelimiter("}");
                result = parseSet(new Scanner(input.next()));
                input.skip("}");
            } else {
                if (nextCharIs(input,'(')) {
                    result = parseComplexFactor(input);
                }

            }}
        return result;
    }


    void parseAssignment(Scanner input) throws APException {
        input.useDelimiter("\\s \\s|\\s=\\s"); //delimit on " " OR "="
        Scanner identifierScanner = new Scanner(input.next());
        Identifier tempIdentifier = parseIdentifier(identifierScanner);
        skipSpaces();
        if(nextCharIsLetter(input)){    //If next char is letter it would mean there is a space in the identifier and only the first part is parsed.
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
        input.skip("=");
        Set tempSet = parseExpression(input);
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

    Set parseExpression(Scanner input)throws APException{
        Set result;
        String expression = input.nextLine();
        expression = expression.replace(" ","");
        Scanner rpnScanner = new Scanner(shuntingYard(expression));
        //first *
        Stack rpnStack = new Stack();
        rpnScanner.useDelimiter(",");
        while(rpnScanner.hasNext()){
            String token = rpnScanner.next();
            if(isOperator(token)){
                Set set1 = parseFactor(new Scanner(rpnStack.pop()));
                Set set2 = parseFactor(new Scanner(rpnStack.pop()));
                if (token.charAt(0) == '+'){

                }
            }else {

            }
        }

        input.useDelimiter("\\+|\\-|\\|"); //delimit on "+" OR "-" OR "|"
        input.useDelimiter("\\*");
        result = parseFactor(input);
    }


    String shuntingYard(String inFix){
        Stack stack = new Stack();

        Scanner SYScanner = new Scanner(inFix);
        SYScanner.useDelimiter("\\+|\\-|\\||\\*");

        //output string: RPN notation delimited with ',' e.g "var1,<5,6,7,8,9>,+,var3,-"



        /*while there are tokens to be read:
	read a token.
	if the token is a number, then push it to the output queue.
	if the token is an operator, then:
		while there is an operator at the top of the operator stack with
			greater than or equal to precedence and the operator is left associative:
				pop operators from the operator stack, onto the output queue.
		push the read operator onto the operator stack.
	if the token is a left bracket (i.e. "("), then:
		push it onto the operator stack.
	if the token is a right bracket (i.e. ")"), then:
		while the operator at the top of the operator stack is not a left bracket:
			pop operators from the operator stack onto the output queue.
		pop the left bracket from the stack.
		/* if the stack runs out without finding a left bracket, then there are
		mismatched parentheses. */
        if there are no more tokens to read:
        while there are still operator tokens on the stack:
		/* if the operator token on the top of the stack is a bracket, then
		there are mismatched parentheses. */
        pop the operator onto the output queue.
                exit.
    }
    void parsePrint(Scanner input) throws APException{
        input.skip("\\?");//skip ?,\\to escape.
        Set toPrint = parseExpression(input);
        out.println(toPrint.toString());
    }


    Set parseSet(Scanner input) throws APException{
        input.skip("\\{");
        Set result = new Set();
        BigInteger newElement;
        while(input.hasNext()){
            if(nextCharIs(input,',')) {
                input.skip(",");
            }
            newElement = input.nextBigInteger();
            result.insert(newElement);
        }
        return result;
    }

    Set parseComplexFactor(Scanner input) throws APException{

    }

    Set getSetByID(Scanner input) throws APException{
        input.useDelimiter("[^A-Za-z0-9]");
        Set result;
        Scanner identifierScanner = new Scanner(input.next());
        Identifier parsedIdentifier = parseIdentifier(identifierScanner);
        if(variables.containsKey(parsedIdentifier)) {
            result = variables.get(parsedIdentifier);
        }else{
            throw new APException(parsedIdentifier.toString()+IDENTIFIER_NOT_FOUND);
        }
        return result;
    }

    void skipSpaces(){
        while(nextCharIs(input,' ')){
            input.skip(" ");
        }
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
    private boolean isOperator(String input){
        return input.equals("+")||input.equals("-")||input.equals("*")||input.equals("|");
    }

    private void start() {
        input = new Scanner(System.in);
        while (input.hasNextLine()) {
            try {
                if(false) {
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
            if (argv[0].equals("--help")){
                System.out.println(HELP_MESSAGE);
            }
        }catch (NullPointerException e){};
        new Main().start();
    }
}
