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
    public void appendIdentifier(String toAppend){
            identifier.append(toAppend);
    }

    @Override
    public boolean hasCorrectIdentifierFormat(String input){
    	
        for (int i = 0; i < input.length();i++){
        	if ((isLetter(input.charAt(i))) || (isDigit(input.charAt(i)))) {
        		return true;
        	}
        }
        return false;
    }

    private boolean isLetter(char input){
        return Character.isLetter(input);
    }

    private boolean isDigit(char input){
        return Character.isDigit(input);
    }
}
