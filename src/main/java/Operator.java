class Operator {

    static final int    HIGH_PRECEDENCE = 2,
                        LOW_PRECEDENCE = 1;
    private char value;
    private int precedence;


    Operator(String value){
        this.value = value.charAt(0);
        setAPPrecedence();
    }

    Operator(String value, int precedence){
        this.value = value.charAt(0);
        this.precedence = precedence;
    }

    Operator(char value, int precedence){
        this.value = value;
        this.precedence = precedence;
    }

    public char getValue() {
        return value;
    }

    public int getPrecedence() {
        return precedence;
    }

    private void setAPPrecedence(){
        if(value == '*'){
            precedence = HIGH_PRECEDENCE;
        }else{
            precedence = LOW_PRECEDENCE;
        }
    }


}
