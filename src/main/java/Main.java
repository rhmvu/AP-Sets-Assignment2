import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.regex.Pattern;

public class Main {
    static final private String IDENTIFIER_SPACE_EXCEPTION = "Space in Identifier not allowed",
			IDENTIFIER_FORMAT_EXCEPTION = "Invalid identifier, please read the documentation",
			IDENTIFIER_NOT_FOUND_EXCEPTION = ", parsed as identifier has no corresponding Set",
            INVALID_SET_EXCEPTION = "Invalid Set, please read the documentation",
			SET_MISSING_NUMBER_EXCEPTION = "Invalid Set, number missing between \\\"\\{\\,\\\"",
			SET_INVALID_TOKEN_EXCEPTION = "Invalid token in set, please read the documentation",
			MISSING_OPERATOR_EOL_EXCEPTION = "Operator or end of line missing, please read the documentation",
            INVALID_STATEMENT_EXCEPTION = "Invalid statement, please read the documentation",
           // DOUBLE_VALUE_SET = "Invalid Set: Set contains 2 elements with the same value",
            HELP_MESSAGE = "This Set interpreter works with operators +,-,* and Sets containing big Integers\n"
                    + "Set Interpreter REQUIRES you to omit spaces in identifiers\n"
                    +"However, you can use spaces and run the program with '--omit-spaces' to bypass this.\n\n"
                    +"Allowed statements:\n?<Set/Factor> to output a set or factor\n"
                    +"<Identifier>=<Set/Factor> to assign a Set to an Identifier.\n\n"
                    + "Set Interpreter by Kostas Moumtzakis & Ruben van der Ham";

    
    PrintStream out;
    HashMap<IdentifierInterface, SetInterface<BigInteger>> setCollection;

    Main(){
        out = new PrintStream(System.out);
        setCollection = new HashMap<>();
    }
    
    public void parseStatement(String input) throws APException {
    	Scanner statement = format(input);
    	
    	if (nextCharIsLetter(statement)) {
            parseAssignment(statement);
    	} else if (nextCharIs(statement, '?')) {
    		printStatement(statement);
    	} else if (! nextCharIs(statement, '/')){
    		throw new APException(INVALID_STATEMENT_EXCEPTION);
    	}
    }
    
    private Scanner format(String line) throws APException {
    	Scanner input = new Scanner(line);
    	StringBuilder statement = new StringBuilder();
    	
    	while (input.hasNext()) {
    		if (nextCharIs(input, '/')) {
    			statement.append(input.nextLine());
        		return new Scanner(statement.toString());
        	} else if (nextCharIsLetter(input) || nextCharIsDigit(input)) {
    			statement.append(input.next());
    			while (nextCharIs(input, ' ')) {
    				skipToken(input.next(), ' ');
    				if (nextCharIsLetter(input) || nextCharIsDigit(input)) {
    					throw new APException(IDENTIFIER_SPACE_EXCEPTION);
    				}
    			}
    		} else if (nextCharIs(input, '{')) {
    			statement.append(input.next());
    			while (nextCharIs(input, ' ')) {
    				skipToken(input.next(), ' ');
				}
    			if (!(nextCharIs(input, '}') || nextCharIsDigit(input))) {
    				throw new APException(INVALID_SET_EXCEPTION);
    			}
    			if (nextCharIs(input, '0')) {
        			statement.append(input.next());
    				if (nextCharIsDigit(input)) {
    					throw new APException(INVALID_SET_EXCEPTION);
    				}
    			}
    		} else if (nextCharIs(input, ' ')) {
				skipToken(input.next(), ' ');
    		} else if (nextCharIsDigit(input)) {
    			statement.append(input.next());
    			while (nextCharIs(input, ' ')) {
    				skipToken(input.next(), ' ');
    				if (nextCharIsDigit(input)) {
    					throw new APException(INVALID_SET_EXCEPTION);
    				}
    			}
    		} else if (nextCharIs(input, ',')) {
    			statement.append(input.next());
    			while (nextCharIs(input, ' ')) {
    				skipToken(input.next(), ' ');
    			}
				if (!nextCharIsDigit(input)) {
					throw new APException(INVALID_SET_EXCEPTION);
				}
    		} else {
    			statement.append(input.next());
    		}
    	}
    	
    	return new Scanner(statement.toString());
    }
    
    public void parseAssignment(Scanner input) throws APException {
    	SetInterface<BigInteger> set;
    	input.useDelimiter("=");
    	IdentifierInterface identifier = parseIdentifier(input.next());
    	try {
        	set = parseExpression(new Scanner(input.next()));
    	} catch (Exception e) {
    		throw new APException(INVALID_STATEMENT_EXCEPTION);
    	}
    	setCollection.put(identifier, set);
    }
    
    public void printStatement(Scanner input) throws APException {
		skipToken(input.next(), '?');
        SetInterface<BigInteger> set = parseExpression(input);
        out.println(SetToString(set));
    }

    private String SetToString(SetInterface<BigInteger> set){
		StringBuilder output = new StringBuilder();
		
		if(set.goToFirstElement()){
			output.append(set.get());
			while(set.goToNextElement()){
				output.append(" ");
				output.append(set.get());
			}
		}
		return output.toString();
	}
    
    public IdentifierInterface parseIdentifier(String input) throws APException {
    	IdentifierInterface result = new Identifier();
    	
    	if(result.hasCorrectIdentifierFormat(input)) {
    		result.appendIdentifier(input);
        } else {
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
    	
        return result;
    }
    
    public SetInterface<BigInteger> parseExpression(Scanner expression) throws APException {
    	SetInterface<BigInteger> result = null;
    	StringBuilder term = new StringBuilder();
    	int openComplexFactors = 0;

    	while (expression.hasNext()) {
    		
    		if (nextCharIs(expression, '(')) {
    			openComplexFactors += 1;
				term.append(expression.next());
    		} else if (nextCharIs(expression, ')')) {
    			openComplexFactors -= 1;
				term.append(expression.next());
    		} else if (nextCharIs(expression, '+') && openComplexFactors == 0) {
	    		skipToken(expression.next(), '+');
	    		
	    		if (result == null) {
	    			result = parseTerm(new Scanner(term.toString()));
	    		}
	    		term.setLength(0);
	    		
    			while (expression.hasNext()) {
    				
    				if (openComplexFactors == 0 && (nextCharIs(expression, '+') || nextCharIs(expression, '-') || nextCharIs(expression, '|'))) {
    					result = result.union(parseTerm(new Scanner(term.toString())));
    		    		term.setLength(0);
    					break;
    				} else if (nextCharIs(expression, '(')) {
    					openComplexFactors +=1;
	    				term.append(expression.next());
    				} else if (nextCharIs(expression, ')')) {
    					openComplexFactors -=1;
        				term.append(expression.next());
    	    		} else {
    	    			term.append(expression.next());
    	    		}
    			}
    			
    			if (term != null) {
    				result = result.union(parseTerm(new Scanner(term.toString())));
    			}
        	} else if (nextCharIs(expression, '|') && openComplexFactors == 0) {
        		boolean b = true;
        		skipToken(expression.next(), '|');
	    		
	    		if (result == null) {
		    		result = parseTerm(new Scanner(term.toString()));
	    		}
	    		term.setLength(0);
	    		
	    		while (expression.hasNext()) {
    				
	    			if (openComplexFactors == 0 && (nextCharIs(expression, '+') || nextCharIs(expression, '-') || nextCharIs(expression, '|'))) {
    					result = result.symDifference(parseTerm(new Scanner(term.toString())));
    		    		term.setLength(0);
    					break;
    				} else if (nextCharIs(expression, '(')) {
    					openComplexFactors +=1;
	    				term.append(expression.next());
    				} else if (nextCharIs(expression, ')')) {
    					openComplexFactors -=1;
        				term.append(expression.next());
    	    		} else {
    	    			term.append(expression.next());
    	    		}
    			}
	    		if (result != null) {
	    			result = result.symDifference(parseTerm(new Scanner(term.toString())));
	    		}
        	} else if (nextCharIs(expression, '-') && openComplexFactors == 0) {
        		skipToken(expression.next(), '-');
	    		
	    		if (result == null) {
		    		result = parseTerm(new Scanner(term.toString()));
	    		}
	    		term.setLength(0);
	    		
	    		while (expression.hasNext()) {
    				
	    			if (openComplexFactors == 0 && (nextCharIs(expression, '+') || nextCharIs(expression, '-') || nextCharIs(expression, '|'))) {
    					result = result.complement(parseTerm(new Scanner(term.toString())));
    		    		term.setLength(0);
    					break;
    				} else if (nextCharIs(expression, '(')) {
    					openComplexFactors +=1;
	    				term.append(expression.next());
    				} else if (nextCharIs(expression, ')')) {
    					openComplexFactors -=1;
        				term.append(expression.next());
    	    		} else {
    	    			term.append(expression.next());
    	    		}
    			}
	    		if (result != null) {
	    			result = result.complement(parseTerm(new Scanner(term.toString())));
	    		}
        	} else {
        		term.append(expression.next());
        	}
    	}
    	
    	if (result == null) {
    		result = parseTerm(new Scanner(term.toString()));
    	}
    	
    	return result;
    }
    
    public SetInterface<BigInteger> parseTerm(Scanner term) throws APException {
    	SetInterface<BigInteger> result;
    	StringBuilder factor = new StringBuilder();
    	int openComplexFactors = 0;
    	
    	while (term.hasNext()) {
    		
    		if (nextCharIs(term, '(')) {
    			openComplexFactors += 1;
    			factor.append(term.next());
    		} else if (nextCharIs(term, ')')) {
    			openComplexFactors -= 1;
    			factor.append(term.next());
    		} else if (nextCharIs(term, '*') && openComplexFactors == 0) {
	    		skipToken(term.next(), '*');
        		result = parseFactor(new Scanner(factor.toString())).intersection(parseTerm(new Scanner(term.nextLine())));
        		return result;
    		} else {
    			factor.append(term.next());
        	}
    	}
		result = parseFactor(new Scanner(factor.toString()));
    	return result;
    }
    
    public SetInterface<BigInteger> parseFactor(Scanner factor) throws APException {
    	SetInterface<BigInteger> result = new Set<>();
    	int openComplexFactors = 0;
		StringBuilder set = new StringBuilder();
    	
    	while(factor.hasNext()) {
    		
    		if (nextCharIs(factor, '(')) {
	    		skipToken(factor.next(), '(');
    			openComplexFactors += 1;
    			while (factor.hasNext() && openComplexFactors != 0) {
    				
    				if (nextCharIs(factor, '(')) {
    	    			openComplexFactors += 1;
    	    			set.append(factor.next());
    				} else if (nextCharIs(factor, ')')) {
    	    			openComplexFactors -= 1;
    	    			
    	    			if (openComplexFactors == 0) {
    	    				if (factor.hasNext()) {
    	    	    			skipToken(factor.next(), ')');
    	    	    		} else {
    	    	    			throw new APException("Invalid token detected");
    	    	    		}
    	    			} else {
    	    				set.append(factor.next());
    	    			}
    				}else {
    					set.append(factor.next());
    				}
    			}
				result = parseExpression(new Scanner(set.toString()));
    		} else if (nextCharIsLetter(factor)) {
    			set.append(factor.next());
	    		
	    		while (nextCharIsLetter(factor)|| nextCharIsDigit(factor)) {
	    			set.append(factor.next());
	    		}
	    		IdentifierInterface identifier = parseIdentifier(set.toString());
	        	
    			if (setCollection.containsKey(identifier)) {
    				result = setCollection.get(identifier);
    			} else {
    				throw new APException(identifier.toString()+IDENTIFIER_NOT_FOUND_EXCEPTION);
    			}
	    	} else if (nextCharIs(factor, '{')) {
	    		skipToken(factor.next(), '{');
	    		
	    		if (nextCharIs(factor, ',')) {
	    			throw new APException(SET_MISSING_NUMBER_EXCEPTION);
	    		}
    			
	    		while (!nextCharIs(factor, '}') && factor.hasNext()) {
	    			set.append(factor.next());
	    		}
	    		
	    		if (factor.hasNext()) {
	    			skipToken(factor.next(), '}');
	    		} else {
	    			throw new APException(SET_INVALID_TOKEN_EXCEPTION);
	    		}
	    		
	    		if (factor.hasNext()) {
	    			throw new APException(MISSING_OPERATOR_EOL_EXCEPTION);
	    		}
	    		result = parseSet(set.toString());
	    	} else {
	    		throw new APException("What now...");
	    	}
    	}
    	if (openComplexFactors != 0) {
    		throw new APException("Missing parenthesis detected");
    	}
    	
    	return result;
    }
    
    public SetInterface<BigInteger> parseSet(String numbers) throws APException {
    	SetInterface<BigInteger> result = new Set<BigInteger>();
    	Scanner parser = new Scanner(numbers);
    	parser.useDelimiter(",");
    	
    	while (parser.hasNext()) {
    		try {
    			result.insert(parser.nextBigInteger());
    		} catch (Exception e) {
    			throw new APException("Invalid number");
    		}
    	}
    	parser.close();
    	
    	if (result.hasDoubleOccurencies()) {
    		result.fixDoubleOccurencies();
    	}
    	return result;
    }
    
    private void skipToken (String input, char c) throws APException {
    	Scanner token = new Scanner(input);
    	if (! nextCharIs(token, c)) {
    		throw new APException("Missing token: " + c);
	    }
    }
    
    private boolean nextCharIsLetter(Scanner input){
        input.useDelimiter("");
        return input.hasNext("[a-zA-Z]");
    }
    
    private boolean nextCharIsDigit (Scanner input) {
    	input.useDelimiter("");
    	return input.hasNext("[0-9]"); 
	}
    
    private boolean nextCharIs(Scanner input, char c){
        input.useDelimiter("");
        return input.hasNext(Pattern.quote(c+""));
    }
    
    private void start() {
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()) {
			try {
				parseStatement(in.nextLine());
			} catch (APException e) {
				System.out.println(e);
			}
		}
        in.close();
    }

    public static void main(String[] argv) {
    	try {
			if (argv[0].equals("--help")) {
				System.out.println(HELP_MESSAGE);
			}
		}catch (Exception e){
    		new Main().start();
		}
            new Main().start();
    }
}
