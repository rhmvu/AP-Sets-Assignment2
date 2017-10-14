import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.regex.Pattern;

public class Main {
    static final String IDENTIFIER_FORMAT_EXCEPTION = "Space in Identifier not allowed",
            IDENTIFIER_BLANK_EXCEPTION = "An Identifier has to have a name",
            INVALID_STATEMENT = "Invalid statement, please read the documentation",
            IDENTIFIER_NOT_FOUND = ", parsed as identifier has no corresponding Set",
            DOUBLE_VALUE_SET = "Invalid Set: Set contains 2 elements with the same value",
            HELP_MESSAGE = "This Set interpreter works with operators +,-,* and Sets containing big Integers\n"
                    + "Set Interpreter REQUIRES you to omit spaces in identifiers\n"
                    +"However, you can use spaces and run the program with '--omit-spaces' to bypass this.\n\n"
                    +"Allowed statements:\n?<Set/Factor> to output a set or factor\n"
                    +"<Identifier>=<Set/Factor> to assign a Set to an Identifier.\n\n"
                    + "Set Interpreter by Kostas Moumtzakis & Ruben van der Ham";

    
    PrintStream out;
    HashMap<Identifier, SetInterface<BigInteger>> sets;

    Main(){
        out = new PrintStream(System.out);
        sets = new HashMap<Identifier, SetInterface<BigInteger>>();
    }
    
    public void parseStatement(Scanner input) throws APException {
    
    	if (nextCharIsLetter(input)) {
            parseAssignment(input);
    	} else if (nextCharIs(input, '?')) {
    		printStatement(input);
    	} else if (!nextCharIs(input, '/')) {
    		throw new APException(INVALID_STATEMENT);
    	}
    }
    
    private void parseAssignment(Scanner input) throws APException {
    	input.useDelimiter(" |=");
    	Identifier identifier = parseIdentifier(input.next());
    	System.out.println("Identifier: " + identifier.toString());
    	//input = input.skip("=");
    	
    	SetInterface<BigInteger> set = parseExpression(input);
    	System.out.println("Set: " + set.toString());
    	sets.put(identifier, set);
    }
    
    private void printStatement(Scanner input) throws APException {
    	input.skip("\\?");//skip ?,\\to escape.
        SetInterface<BigInteger> set = parseExpression(input);
        System.out.println("Output: " + set.toString());
    }
    
    private Identifier parseIdentifier(String input) throws APException {
    	Identifier result = new Identifier();
    	
    	if(result.hasCorrectIdentifierFormat(input)) {
    		result.appendIdentifier(input);
        } else {
            System.out.println(input);
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
    	
        return result;
    }
    
    private SetInterface<BigInteger> parseExpression(Scanner input) throws APException {
    	SetInterface<BigInteger> result = null;
    	String expression = input.nextLine();
        System.out.println("Expression: " + expression);
        
    	return result;
    	
    }
    
    private boolean nextCharIsLetter(Scanner input){
        input.useDelimiter("");
        return input.hasNext("[a-zA-Z]");
    }
    
    private boolean nextCharIsOperand(Scanner input) {
        input.useDelimiter("");
        return input.hasNext("\\*") ||input.hasNext("\\+")||input.hasNext("\\-")||input.hasNext("\\|");
    }
    
    private boolean nextCharIsOpenParenthesis(Scanner input) {
        input.useDelimiter("");
        return input.hasNext("[(]");
    }
    
    private boolean nextCharIsDigit (Scanner input) {
        input.useDelimiter("");
    	return input.hasNext("[0-9]");
	}

    private boolean nextCharIs(Scanner input, char c){
        input.useDelimiter("");
        return input.hasNext(Pattern.quote(c+""));
    }
    
    private boolean isOperator(String input){
        return input.equals("+")||input.equals("-")||input.equals("*")||input.equals("|");
    }

    private void start() {
        Scanner in = new Scanner(System.in);
        Scanner statement;

        while(in.hasNextLine()) {
			statement = new Scanner(in.nextLine().replace(" ", ""));

			try {
				parseStatement(statement);
			}
			catch (APException e) {
				out.println(e);
			}
		}
        in.close();
    }

    public static void main(String[] argv) {
            new Main().start();
    }
}
