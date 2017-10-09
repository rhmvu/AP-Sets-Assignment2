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
    HashMap<Identifier, SetInterface<BigInteger>> collection;

    Main(){
        out = new PrintStream(System.out);
        collection = new HashMap<Identifier, SetInterface<BigInteger>>();
    }
    
    public void parseStatement(Scanner input) throws APException {
    	
    	if (nextCharIs(input, '/')) {
    		input.skip("\\/");
    		System.out.println("Comment: " + input.nextLine());
    	} else if (nextCharIsLetter(input)) {
            parseAssignment(input);
    	} else if (nextCharIs(input, '?')) {
    		parsePrintStatement(input);
    	} else {
    		throw new APException(INVALID_STATEMENT);
    	}
    }
    
    private void parseAssignment(Scanner input) throws APException {
    	input.useDelimiter(" |=");
    	Identifier identifier = parseIdentifier(input.next());
    	
    	input = input.skip("=");
    	if (nextCharIsLetter(input) || nextCharIs(input, ' ') || nextCharIs(input, '=')) {
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
    	SetInterface<BigInteger> set = parseExpression(input);
    	System.out.println("Set: " + set.toString());
    	collection.put(identifier,set);
    }
    
    private void parsePrintStatement(Scanner input) throws APException {
    	input.skip("\\?");//skip ?,\\to escape.
        SetInterface<BigInteger> set = parseExpression(input);
        System.out.printf("this set has been returned:%s\n",set);
        System.out.println("Output: " + set.toString());
    }
    
    private Identifier parseIdentifier(String input) throws APException {
    	Identifier identifier = new Identifier();
    	
    	if (input.equals("")) {
            throw new APException(IDENTIFIER_BLANK_EXCEPTION);
        } else if(!identifier.appendValidIdentifier(input)) {
            System.out.println(input);
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
        return identifier;
    }
    
    private SetInterface<BigInteger> parseExpression(Scanner input) throws APException {
    	SetInterface<BigInteger> result = null;
    	String expression = input.nextLine();
        System.out.println("Expression: " + expression);
        String rpnString = shuntingYard(expression);
        
    	return result;
    	
    }
    
    private String shuntingYard(String inFix) throws APException {
    	
    	return null;
    }
    
    private boolean nextCharIs(Scanner input, char c){
        input.useDelimiter("");
        return input.hasNext(Pattern.quote(c+""));
    }
    
    private boolean nextCharIsLetter(Scanner input){
        input.useDelimiter("");
        return input.hasNext("[a-zA-Z]");
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
