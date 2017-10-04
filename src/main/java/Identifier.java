public class Identifier implements IdentifierInterface {

    private StringBuffer identifier;

    Identifier() {
        identifier = new StringBuffer();
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

    @Override
    public int hashCode() {
        return identifier.toString().hashCode();
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare instanceof Identifier) {
            if (toCompare.hashCode() == this.hashCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean appendValidIdentifier(String toAppend){
        if (hasCorrectIdentifierFormat(toAppend)){
            append(toAppend);
            return true;
        }
        return false;
    }

    private void append(String toAppend){
        identifier.append(toAppend);
    }

    private boolean isLetter(char input){
        return Character.isLetter(input);
    }

    private boolean isDigit(char input){
        return Character.isDigit(input);
    }

    private boolean hasCorrectIdentifierFormat(String input){
        if(!isLetter(input.charAt(0))){
            return  false;
        }
        for (int i = 1; i < input.length();i++){
            char currentChar = input.charAt(i);
            if(!(isLetter(currentChar) || isDigit(currentChar))){
                return false;
            }
        }
        return true;
    }
}
