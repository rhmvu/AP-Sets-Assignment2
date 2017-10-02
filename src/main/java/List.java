public class List<E extends Comparable> implements ListInterface<E>{

    Node head,
    tail;

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
        return null;
    }

    @Override
    public int size() {
        if(isEmpty()){
           return 0;
        } else {
            int size = 1;
            Node k = head;
            while (k.next != null) {
                size += 1;
                k = k.next;
            }
            return size;
        }
    }

    @Override
    public ListInterface<E> insert(E d) {
        return null;
    }

    @Override
    public E retrieve() {
        return null;
    }

    @Override
    public ListInterface<E> remove() {
        return null;
    }

    @Override
    public boolean find(E d) {
        return false;
    }

    @Override
    public boolean goToFirst() {
        return false;
    }

    @Override
    public boolean goToLast() {
        return false;
    }

    @Override
    public boolean goToNext() {
        return false;
    }

    @Override
    public boolean goToPrevious() {
        return false;
    }

    @Override
    public ListInterface<E> copy() {
        return null;
    }
}



