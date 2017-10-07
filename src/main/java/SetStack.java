public class SetStack {
    static final int STACK_SIZE = 100;

    int stackPointer;
    Set[] elements;

    SetStack(){
        elements = new Set[STACK_SIZE];
        stackPointer = -1;
    }

    void push(Set value){
        stackPointer+=1;
        elements[stackPointer]= value;

    }

    Set pop(){
        Set pop = elements[stackPointer];
        stackPointer-=1;
        return pop;
    }
    void DeleteTOS(){
        elements[stackPointer] = null;
        stackPointer-=1;
        return;
    }

    Set peek(){
        return elements[stackPointer];
    }

    int size(){
        return stackPointer+1;
    }
}
