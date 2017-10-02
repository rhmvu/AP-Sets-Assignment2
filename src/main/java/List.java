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
        /*if(isEmpty()){
            return 0;
        } else {
            int size = 1;
            Node k = head;
            while (k.next != null) {
                size += 1;
                k = k.next;
            }

        }
        return size;*/
        return numberOfElements;
    }

    @Override
    public ListInterface<E> insert(E d) {
        if (isEmpty()){
            current = tail= head = new Node(d);
            
        } else {
            Node newNode = new Node(d,tail,null);
            current = tail = tail.next = newNode;
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
        /** @precondition  - The list is not empty.
         * 	@postcondition - The current element of list-PRE is not present in list-POST.
         * 	    			current-POST points to
         *    					- if list-POST is empty
         *   						null
         *   					- if list-POST is not empty
         *     						if current-PRE was the last element of list-PRE
         *     							the last element of list-POST
         *     						else
         *     							the element after current-PRE
         *  				list-POST has been returned.
         **/
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
        while (current.next != null) {
            if (current.data == d) {
                return true;
            }
            current = current.next;
        }
        if(head.data.compareTo(d) > 0){ //hopefully compareTo works...
            current = head;
        } else {
            current = tail;
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
        List<E> temp = new List<E>();
        temp.current = this.current;
        temp.head = this.head;
        temp.tail = this.tail;

        goToFirst();
        while (current.next != null){
            temp.insert(retrieve());
        }
        current = temp.current;
        return temp;
    }
}



