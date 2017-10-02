public class Identifier implements IdentifierInterface{

    private String identifier;

    Identifier(String name){
            identifier = name;
    }

    public String getString(){
        return identifier;
    }
}
