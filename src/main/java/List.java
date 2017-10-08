public class List<E extends Comparable> implements ListInterface<E>{

    int numberOfElements;
    Node head,
            current,
            tail;


    List(){
        numberOfElements = 0;
    }

    private class Node {

        E data;
        Node prior,
                next;

        public Node(E d) {
            this(d, null, null);
        }

        public Node(E data, Node prior, Node next) {
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }

    }

    @Override
    public boolean isEmpty() {
        if(head == null){
            return true;
        }
        return false;
    }

    @Override
    public ListInterface<E> init() {
        head = null;
        tail = null;
        current = null;
        numberOfElements = 0;
        return this;
    }

    @Override
    public int size() {
        return numberOfElements;
    }

    @Override
    public ListInterface<E> insert(E d) {
        if (isEmpty()){
            current = tail= head = new Node(d);

        } else {
            if (tail.data.compareTo(d)<=0) {
                Node tailNode = new Node(d, tail, null);
                current = tail = tail.next = tailNode;
            } else {
                if (head.data.compareTo(d)>=0) {
                    Node headNode = new Node(d, null, head);
                    current = head = head.prior = headNode;
                } else {
                    find(d);
                    Node middleNode =  new Node(d,current,current.next);
                    current = middleNode.next.prior = middleNode.prior.next =middleNode;
                }
            }
        }
        numberOfElements+=1;
        return this;
    }

    @Override
    public E retrieve() {
        return current.data;
    }

    @Override
    public ListInterface<E> remove() {
        if(numberOfElements == 1){
            init();
            return null;
        }
        if (current.next == null && current.prior != null){
            current = current.prior;
            current.next= current.next.prior = null;
        } else {
            if(current.prior !=null){
                current.prior.next = current.next;
                current.next.prior = current.prior;
            }

            Node temp = current;
            current = temp.next;
            temp.next = temp.prior = null;
        }

        numberOfElements-=1;
        return this;
    }

    @Override
    public boolean find(E d) {
        if(isEmpty()){
            return false;
        }
        goToFirst();
        do{
            if (current.data.hashCode() == d.hashCode() || current.data == d) {
                return true;
            }
        }while(goToNext());
        current = head;
        if(head.data.compareTo(d) > 0){
        } else {
            while (current.data.compareTo(d)< 0) {
                if(current.next == null){
                    return false;
                }
                current = current.next;
            }
            current = current.prior;
        }
        return false;
    }

    @Override
    public boolean goToFirst() {
        if (isEmpty()) {
            return false;
        }else {
            current = head;
            return true;
        }
    }

    @Override
    public boolean goToLast() {
        if (isEmpty()) {
            return false;
        }else {
            current = tail;
            return true;
        }
    }

    @Override
    public boolean goToNext() {
        if (isEmpty() || current.next == null) {
            return false;
        }else {
            current = current.next;
            return true;
        }
    }

    @Override
    public boolean goToPrevious() {
        if (isEmpty() || current.prior == null) {
            return false;
        }else {
            current = current.prior;
            return true;
        }
    }

    @Override
    public ListInterface<E> copy() { //Doesn't create a deep copy yet?
        Node oldCurrent = current;
        List<E> temp = new List<E>();
        if (this.isEmpty()){
            return temp.init();
        }
        this.goToLast();
        temp.insert(this.retrieve());
        temp.tail = temp.current;
        while (current.prior != null){
            this.current = current.prior;
            temp.insert(this.retrieve());
        }
        temp.head = temp.current;
        current = oldCurrent;
        return temp;
    }
    
    /*void deleteDataInCurrent(){ //used for CopyTest
    	current.data = null;
    }*/
}



