import javax.sound.midi.SysexMessage;
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
            HELP_MESSAGE = "This Set interpreter works with operators +,-,* and Sets containing big Integers\n"
                    + "Set Interpreter REQUIRES you to omit spaces in identifiers\n"
                    +"However, you can use spaces and run the program with '--omit-spaces' to bypass this.\n\n"
                    +"Allowed statements:\n?<Set/Factor> to output a set or factor\n"
                    +"<Identifier>=<Set/Factor> to assign a Set to an Identifier.\n\n"
                    + "Set Interpreter by Kostas Moumtzakis & Ruben van der Ham";

    Scanner input;
    PrintStream out;
    HashMap<Identifier, SetInterface<BigInteger>> variables;

    Main(){
        out = new PrintStream(System.out);
        variables = new HashMap<Identifier, SetInterface<BigInteger>>();
    }



    void parseStatement(Scanner input)throws APException {
        //System.out.println("input was:"+input.nextLine());
        //input.reset();
        //skipSpaces(input);
        if (nextCharIs(input, '/')) {
            System.out.println("Comment found");
            System.out.println(input.nextLine());
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

    SetInterface parseFactor(Scanner input) throws APException{
        skipSpaces(input);
        SetInterface result = null;
        if (nextCharIsLetter(input)){
            result = getSetByID(input);
        } else {
            if (nextCharIs(input,'{')){
                input.useDelimiter("}");
                result = parseSet(new Scanner(input.next()));
                input.skip("}");
            } else {
                if (nextCharIsOpenParenthesis(input)) {
                    result = parseComplexFactor(input);
                    input.skip("\\)");
                }else{
                    throw new APException("Expression/Factor has wrong format");
                }

            }}
        return result;
    }


    void parseAssignment(Scanner input) throws APException {
        input.useDelimiter(" |="); //delimit on " " OR "="
        Scanner identifierScanner = new Scanner(input.next());
        Identifier tempIdentifier = parseIdentifier(identifierScanner);
        input = skipSpaces(input);
        if(nextCharIsLetter(input) ||nextCharIs(input,' ')){    //If next char is letter it would mean there is a space in the identifier and only the first part is parsed.
            System.out.println("NextChar is letter");
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
        if(nextCharIs(input,'=')){
            input.skip("=");
        }else{
            throw new APException("'=' missing, Assignment has not the correct format");
        }
        System.out.println("Before set in assignment");
        SetInterface tempSet = parseExpression(input);
        System.out.println("AFTER set in assignment");
        System.out.printf("This is the set:%s\n",tempSet.toString());
        variables.put(tempIdentifier,tempSet);
    }

    Identifier parseIdentifier(Scanner input) throws APException{
        //input.useDelimiter("=");
        String identifierName = input.next();
        if (identifierName.equals("")) {
            throw new APException(IDENTIFIER_BLANK_EXCEPTION);
        }

        Identifier tempID = new Identifier();
        if(!tempID.appendValidIdentifier(identifierName)) {
            System.out.println("appendValid was WRONG");
            System.out.println(identifierName);
            throw new APException(IDENTIFIER_FORMAT_EXCEPTION);
        }
        return tempID;
    }

    SetInterface parseExpression(Scanner input)throws APException{ //TODO add APEXCEPTION
        SetInterface result;
        String expression = input.nextLine();
        System.out.printf("\nExpression:%S\n",expression);
        //expression = expression.replace(" ","");
        String rpnString = shuntingYard(expression);
        System.out.printf("\nSHUNYARD output:%S\n",rpnString);
        Scanner rpnScanner = new Scanner(rpnString);
        System.out.println("ShunYard done");
        SetStack rpnStack = new SetStack();
        rpnScanner.useDelimiter("_");
        SetInterface set1,
                set2;
        String token;

        while(rpnScanner.hasNext()){
            token = rpnScanner.next();
            if(isOperator(token)){
                set1 = rpnStack.pop();
                set2 = rpnStack.pop();
                System.out.printf("Operator: %s found\n",token);
                if (token.charAt(0) == '*'){
                    SetInterface intersect = set1.intersection(set2);
                    rpnStack.push(intersect);
                }else {
                    if (token.charAt(0) == '+') {
                        SetInterface union = set1.union(set2);
                        rpnStack.push(union);
                    } else {
                        if (token.charAt(0) == '-') {
                            SetInterface complement = set1.complement(set2);
                            rpnStack.push(complement);
                        } else {
                            if (token.charAt(0) == '|') {
                                SetInterface symDifference = set1.symDifference(set2);
                                rpnStack.push(symDifference);
                            }
                        }
                    }
                }
            }else {
                System.out.printf("factor: %s found\n",token);
                SetInterface newSet = parseFactor(new Scanner(token));
                //System.out.printf(newSet.toString());
                rpnStack.push(newSet);
            }
            rpnScanner.skip("_");
        }
        result = rpnStack.pop();
        System.out.printf("\n\nRESULT:%s",result.toString());
        return result;
    }


        String shuntingYard(String inFix) throws APException{
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
    void parsePrint(Scanner input) throws APException{
        input.skip("\\?");//skip ?,\\to escape.
        SetInterface toPrint = parseExpression(input);
        System.out.printf("this set has been returned:%s\n",toPrint);
        //out.println(toPrint.toString());
        System.out.println("PRINT OUTPUT:"+toPrint.toString());
    }


    SetInterface parseSet(Scanner input) throws APException{
        input.skip("\\{");
        Set result = new Set();
        BigInteger newElement;
        while(input.hasNext()){
            if(nextCharIs(input,',')) {
                input.skip(",");
            }
            try {
                newElement = input.nextBigInteger();
            }catch(Exception e){
                throw new APException("Couldn't parse inputted Set, end sets with a '}'");
            }
            result.insert(newElement);
        }
        return result;
    }

    SetInterface parseComplexFactor(Scanner input) throws APException{
        input.skip("\\(");
        return parseExpression(input);
    }

    SetInterface getSetByID(Scanner input) throws APException{
        //input.useDelimiter("[^A-Za-z0-9]");
        SetInterface result;
        Scanner identifierScanner = new Scanner(input.next());
        Identifier parsedIdentifier = parseIdentifier(identifierScanner);
        if(variables.containsKey(parsedIdentifier)) {
            result = variables.get(parsedIdentifier);
        }else{
            throw new APException(parsedIdentifier.toString()+IDENTIFIER_NOT_FOUND);
        }
        return result;
    }

    Scanner skipSpaces(Scanner input){
        while(nextCharIs(input,' ')){
            input.skip(" ");
        }
        return input;
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
        Scanner directInput = new Scanner(System.in);
        while (directInput.hasNextLine()) {
            try {
                if(false) {
                    String statement = input.nextLine();
                    statement = statement.replace(" ", "");
                    Scanner spaceLess = new Scanner(statement);
                    parseStatement(spaceLess);
                }else {
                    input = new Scanner(directInput.nextLine().replace(" ",""));
                    System.out.println("NEWLINEEEEEEE!!!");
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
        }catch (Exception e){};
        new Main().start();
    }
}
