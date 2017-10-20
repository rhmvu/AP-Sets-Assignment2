import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.regex.Pattern;

public class Main {

	private static final String HELP_MESSAGE = "This Set interpreter works with operators +,-,* and Sets containing big Integers\n"
			+ "Set Interpreter REQUIRES you to omit spaces in identifiers\n"
			+"Allowed statements:\n?<Set/Factor> to output a set or factor\n"
			+"<Identifier>=<Set/Factor> to assign a Set to an Identifier.\n\n"
			+ "Set Interpreter by Kostas Moumtzakis & Ruben van der Ham";

	PrintStream out;
	HashMap<IdentifierInterface, SetInterface<BigInteger>> setCollection;

	Main(){
		out = new PrintStream(System.out);
		setCollection = new HashMap<>();
	}
	/**
	 * Method for clarifying the type of statement
	 * @param input from user
	 * @throws APException if incorrect input is detected
	 */
	private void parseStatement(String input) throws APException {
		Scanner statement = format(input);

		if (nextCharIsLetter(statement)) {
			parseAssignment(statement);
		} else if (nextCharIs(statement, '?')) {
			printStatement(statement);
		} else if (! nextCharIs(statement, '/')){
			throw new APException("Invalid statement, please read the documentation");
		}
	}
	/**
	 * Method for checking the validity of the inputed line
	 * @param line of input (statement)
	 * @return formatted Scanner object
	 * @throws APException if incorrect input is detected
	 */
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
						throw new APException("Spaces in Identifier and between numbers not allowed");
					}
				}
			} else if (nextCharIs(input, ' ')) {
				skipToken(input.next(), ' ');
			} else {
				statement.append(input.next());
			}
		}
		String formated = statement.toString();
		for (int i = 0; i < formated.length(); i++) {
			if (formated.charAt(i) == ',') {
				
				try {
					if (!Character.isDigit(formated.charAt(i - 1)) || !Character.isDigit(formated.charAt(i + 1))) {
						throw new APException("Missing number in set");
					}
				} catch (Exception e) {
					throw new APException("Missing number in set");
				}
			} else if (formated.charAt(i) == '0') {
				if (!Character.isDigit(formated.charAt(i - 1)) && Character.isDigit(formated.charAt(i + 1))) {
					throw new APException("Invalid number");
				}
			}
		}
		return new Scanner(statement.toString());
	}
	/**
	 * Method for parsing the inputed assignment
	 * @param input from user
	 * @throws APException if incorrect input is detected
	 */
	private void parseAssignment(Scanner input) throws APException {
		SetInterface<BigInteger> set;
		input.useDelimiter("=");
		IdentifierInterface identifier = parseIdentifier(input.next());
		try {
			set = parseExpression(new Scanner(input.next()));
		} catch (Exception e) {
			throw new APException("Invalid statement, please read the documentation");
		}
		setCollection.put(identifier, set);
	}
	/**
	 * Method that prints the requested set
	 * @param input from user
	 * @throws APException if incorrect input is detected
	 */
	private void printStatement(Scanner input) throws APException {
		skipToken(input.next(), '?');
		SetInterface<BigInteger> set = parseExpression(input);
		out.println(SetToString(set));
	}
	/**
	 * Method that returns a set in a string format
	 * @param set
	 * @return The set values seperated by spaces in String object
	 */
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
	/**
	 * Method that validates and returns a Identifier from a string
	 * @param input string
	 * @return Identifier Object
	 * @throws APException if incorrect input is detected
	 */
	private IdentifierInterface parseIdentifier(String input) throws APException {
		IdentifierInterface result = new Identifier();

		if(result.hasCorrectIdentifierFormat(input)) {
			result.appendIdentifier(input);
		} else {
			throw new APException("Identifier has not valid format");
		}

		return result;
	}
	/**
	 * Method for calculating the set out of an expression
	 * @param expression
	 * @return the result set of the expression
	 * @throws APException if incorrect input is detected
	 */
	private SetInterface<BigInteger> parseExpression(Scanner expression) throws APException {
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
			} else if (openComplexFactors == 0 && (nextCharIs(expression, '+') || nextCharIs(expression, '-') || nextCharIs(expression, '|'))) {
				String operator = expression.next();

				if (result == null) {
					result = parseTerm(new Scanner(term.toString()));
				}
				term.setLength(0);

				while (expression.hasNext()) {

					if (openComplexFactors == 0 && (nextCharIs(expression, '+') || nextCharIs(expression, '-') || nextCharIs(expression, '|'))) {
						
						switch (operator) {
							case "+":
								result = result.union(parseTerm(new Scanner(term.toString())));
								term.setLength(0);
								break;
							case "-":
								result = result.complement(parseTerm(new Scanner(term.toString())));
								term.setLength(0);
								break;
							case "|":
								result = result.symDifference(parseTerm(new Scanner(term.toString())));
								term.setLength(0);
								break;
						}
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
					
					switch (operator) {
						case "+":
							result = result.union(parseTerm(new Scanner(term.toString())));
							term.setLength(0);
							break;
						case "-":
							result = result.complement(parseTerm(new Scanner(term.toString())));
							term.setLength(0);
							break;
						case "|":
							result = result.symDifference(parseTerm(new Scanner(term.toString())));
							term.setLength(0);
							break;
					}
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
	/**
	 * Method that validates and calculates a term
	 * @param term
	 * @return the set resulted from the term
	 * @throws APException if incorrect input is detected
	 */
	private SetInterface<BigInteger> parseTerm(Scanner term) throws APException {
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
	/**
	 * Method that validates and calculates a factor
	 * @param factor
	 * @return the set resulted from the factor
	 * @throws APException if incorrect input is detected
	 */
	private SetInterface<BigInteger> parseFactor(Scanner factor) throws APException {
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
					throw new APException("Identifier \\\""+ identifier.toString() +"\\\" does not correspond to a Set");
				}
			} else if (nextCharIs(factor, '{')) {
				skipToken(factor.next(), '{');

				if (nextCharIs(factor, ',')) {
					throw new APException("Number missing in set");
				}

				while (!nextCharIs(factor, '}') && factor.hasNext()) {
					set.append(factor.next());
				}

				if (factor.hasNext()) {
					skipToken(factor.next(), '}');
				} else {
					throw new APException("Invalid token in set");
				}

				if (factor.hasNext()) {
					throw new APException("Operator or end of line missing");
				}
				result = parseSet(set.toString());
			} else {
				throw new APException("Invalid statement detected");
			}
		}
		if (openComplexFactors != 0) {
			throw new APException("Missing parenthesis detected");
		}

		return result;
	}
	/**
	 * Method that validates and calculates a set
	 * @param factor
	 * @return the set resulted from a string of numbers
	 * @throws APException if incorrect input is detected
	 */
	private SetInterface<BigInteger> parseSet(String numbers) throws APException {
		SetInterface<BigInteger> result = new Set<>();
		Scanner parser = new Scanner(numbers);
		parser.useDelimiter(",");

		while (parser.hasNext()) {
			try {
				result.insert(parser.nextBigInteger());
			} catch (Exception e) {
				throw new APException("Invalid number detected in a set");
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
		} catch (Exception e){
			new Main().start();
		}
		new Main().start();
	}
}