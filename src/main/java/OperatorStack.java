public class OperatorStack {
    static final int STACK_SIZE = 100;

    int stackPointer;
    Operator[] elements;

    OperatorStack(){
        elements = new Operator[STACK_SIZE];
        stackPointer = -1;
    }

    void push(Operator value){
        stackPointer+=1;
        elements[stackPointer]= value;

    }

    Operator pop(){
        Operator pop = elements[stackPointer];
        stackPointer-=1;
        return pop;
    }
    void DeleteTOS(){
        elements[stackPointer] = null;
        stackPointer-=1;
        return;
    }

    Operator peek(){
        return elements[stackPointer];
    }

    int size(){
        return stackPointer+1;
    }
}
