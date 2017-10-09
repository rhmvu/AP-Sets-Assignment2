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
        System.out.printf("\nSHUNYARD output:%S\n",rpnString);
        
    	return result;
    	
    }
    
    private String shuntingYard(String inFix) throws APException {
    	OperatorStack operatorStack = new OperatorStack();
        StringBuffer result = new StringBuffer();
        Scanner ShunScanner = new Scanner(inFix);
        System.out.printf("\n\nSHUNYARD STARTED\n\n");
        while(ShunScanner.hasNext()){
            //System.out.printf("\n\nnew token present\n\n");
            if(nextCharIsLetter(ShunScanner)|| nextCharIs(ShunScanner,'{')){
                ShunScanner.useDelimiter("\\+|\\-|\\||\\*");
                String token = ShunScanner.next();
                System.out.printf("factor: %s found\n",token);
                result.append(token);
                result.append('_');
            }else{
                if(nextCharIsOperand(ShunScanner)){
                    ShunScanner.useDelimiter("[A-Za-z0-9]|\\{");
                    String token = ShunScanner.next();
                    System.out.printf("operand: %s found\n",token);
                    Operator operator = new Operator(token);
                    while (operatorStack.size() > 0 && operator.getPrecedence() <= operatorStack.peek().getPrecedence()) {
                        result.append(operatorStack.pop().getValue());
                        result.append('_');
                    }
                    operatorStack.push(operator);
                } else{
                    if(nextCharIs(ShunScanner,'(')){
                        ShunScanner.useDelimiter("[A-Za-z0-9]");
                        Operator operator = new Operator(ShunScanner.next());
                        operatorStack.push(operator);
                    }else{
                        if (nextCharIs(ShunScanner,')')){
                            while(operatorStack.peek().getValue() != '('){
                                result.append(operatorStack.pop().getValue());
                                result.append('_');
                            }
                            if(operatorStack.peek().getValue()== '('){
                                operatorStack.pop();
                            }else{
                                throw new APException("ShuntingYard couldn't proccess your expression '(' missing");
                            }
                        }
                    }

                }
            }
        }
        while(operatorStack.size() != 0){
            result.append(operatorStack.pop().getValue());
            result.append('_');
        }

        //output string: RPN notation delimited with ',' e.g "var1_{5,6,7,8,9}_+_var3_-"
        return result.toString();
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
