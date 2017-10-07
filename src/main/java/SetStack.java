public class SetStack {
    static final int STACK_SIZE = 100;

    int stackPointer;
    SetInterface[] elements;

    SetStack(){
        elements = new Set[STACK_SIZE];
        stackPointer = -1;
    }

    void push(SetInterface value){
        stackPointer+=1;
        elements[stackPointer]= value;

    }

    SetInterface pop(){
        SetInterface pop = elements[stackPointer];
        stackPointer-=1;
        return pop;
    }
    void DeleteTOS(){
        elements[stackPointer] = null;
        stackPointer-=1;
        return;
    }

    SetInterface peek(){
        return elements[stackPointer];
    }

    int size(){
        return stackPointer+1;
    }
}
