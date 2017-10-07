public class StringStack {
    static final int STACK_SIZE = 100;

    int stackPointer;
    String[] elements;

    StringStack(){
        elements = new String[STACK_SIZE];
        stackPointer = -1;
    }

    void push(String value){
        stackPointer+=1;
        elements[stackPointer]= value;

    }

    String pop(){
        String pop = elements[stackPointer];
        stackPointer-=1;
        return pop;
    }
    void DeleteTOS(){
        elements[stackPointer] = null;
        stackPointer-=1;
        return;
    }

    String peek(){
        return elements[stackPointer];
    }

    int size(){
        return stackPointer+1;
    }
}
