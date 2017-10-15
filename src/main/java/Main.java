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
    HashMap<IdentifierInterface, SetInterface<BigInteger>> sets;

    Main(){
        out = new PrintStream(System.out);
        sets = new HashMap<IdentifierInterface, SetInterface<BigInteger>>();
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
    
    public void parseAssignment(Scanner input) throws APException {
    	input.useDelimiter("\\=");
    	IdentifierInterface identifier = parseIdentifier(input.next());
    	System.out.println("Identifier: " + identifier.toString());
    	
    	Scanner expression = new Scanner(input.next());
    	//System.out.println("Expression1: " + expression.next());
    	SetInterface<BigInteger> set = parseExpression(expression);
    	System.out.println("Set: " + set.toString());
    	sets.put(identifier, set);
    }
    
    public void printStatement(Scanner input) throws APException {
    	input.skip("\\?");
        SetInterface<BigInteger> set = parseExpression(input);
        System.out.println("Output: " + set.toString());
    }
    
    private IdentifierInterface parseIdentifier(String input) throws APException {
    	IdentifierInterface result = new Identifier();
    	
    	if(result.hasCorrectIdentifierFormat(input)) {
    		result.appendIdentifier(input);
        } else {
            System.out.println("Wrong: " + input);
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
    	System.out.println("Identifier1: " + result.toString());
    	
        return result;
    }
    
    private SetInterface<BigInteger> parseExpression(Scanner expression) throws APException {
    	SetInterface<BigInteger> result = new Set<BigInteger>();
    	StringBuilder term = new StringBuilder();
    	
    	while (expression.hasNext()) {
    		
    		if (nextCharIs(expression, '+')) {
        		expression.skip("\\+");
        		result = parseTerm(new Scanner(term.toString()));
        		Scanner newExpression = new Scanner(expression.nextLine());
        		return result.union(parseExpression(newExpression));
        		
        	} else if (nextCharIs(expression, '|')) {
        		expression.skip("\\|");
        		result = parseTerm(new Scanner(term.toString()));
        		Scanner newExpression = new Scanner(expression.nextLine());
        		return result.symDifference(parseExpression(newExpression));
        		
        	} else if (nextCharIs(expression, '-')) {
        		expression.skip("\\-");
        		result = parseTerm(new Scanner(term.toString()));
        		Scanner newExpression = new Scanner(expression.nextLine());
        		return result.complement(parseExpression(newExpression));
        		
        	} else {
        		term.append(expression.next());
        	}
    	}
		System.out.println("Term: " + term.toString());
		result = parseTerm(new Scanner(term.toString()));
    	
    	return result;
    }
    
    private SetInterface<BigInteger> parseTerm(Scanner term) throws APException {
    	SetInterface<BigInteger> result = new Set<BigInteger>();
    	StringBuilder factor = new StringBuilder();
    	
    	while (term.hasNext() && !nextCharIs(term, '*')) {
    		factor.append(term.next());
    	}
		System.out.println("Factor: " + factor.toString());
		result = parseFactor(new Scanner(factor.toString()));
    	
    	if (nextCharIs(term, '*')) {
    		term.skip("\\*");
    		result = result.intersection(parseTerm(new Scanner(term.nextLine())));
    	}
    	
    	return result;
    }
    
    private SetInterface<BigInteger> parseFactor(Scanner factor) throws APException {
    	SetInterface<BigInteger> result = new Set<BigInteger>();
    	
    	while(factor.hasNext()) {
	    	if (nextCharIsLetter(factor)) {
	    		StringBuilder id = new StringBuilder();
	    		
	    		id.append(factor.next());
	    		
	    		while (nextCharIsLetter(factor)) {
	        		id.append(factor.next());
	    		}
	    		IdentifierInterface identifier = parseIdentifier(id.toString());
	        	System.out.println("Identifier2: " + identifier.toString());
	        	
	    		result = sets.get(identifier);
	    		
	    	} else if (nextCharIs(factor, '{')) {
	    		factor.skip("\\{");
    			StringBuilder set = new StringBuilder();
    			
	    		while (!nextCharIs(factor, '}')) {
	    			set.append(factor.next());
	    		}
	    		factor.skip("\\}");
    			System.out.println("testSet: " + set.toString());
	    		
	    		result = parseSet(set.toString());
	    		
	    	} else if (nextCharIs(factor, '(')) {
	    		factor.skip("\\(");
    			StringBuilder expression = new StringBuilder();
	    		
	    		while (!nextCharIs(factor, ')')) {
	    			expression.append(factor.next());
	    		}
	    		factor.skip("\\)");
    			System.out.println("Expression2: " + expression.toString());
    			Scanner expressionScanner = new Scanner(expression.toString());
	    		
	    		result = parseExpression(expressionScanner);
	    	} else {
	    		throw new APException("hey");
	    	}
    	}
    	
    	return result;
    }
    
    private SetInterface<BigInteger> parseSet(String numbers) {
    	SetInterface<BigInteger> result = new Set<BigInteger>();
    	Scanner parser = new Scanner(numbers);
    	parser.useDelimiter(",");
    	
    	while (parser.hasNext()) {
    		result.insert(parser.nextBigInteger());
    	}
    	parser.close();
		System.out.println("TestSet2: " + result.toString());
    	
    	return result;
    }
    
    private boolean nextCharIsLetter(Scanner input){
        input.useDelimiter("");
        return input.hasNext("[a-zA-Z]");
    }
    
    private boolean nextCharIs(Scanner input, char c){
        input.useDelimiter("");
        //System.out.println("Char: " + input.nextLine());
        return input.hasNext(Pattern.quote(c+""));
    }
    
    private void start() {
        Scanner in = new Scanner(System.in);
        Scanner statement;

        while(in.hasNextLine()) {
			statement = new Scanner(in.nextLine().replaceAll(" ", ""));

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
