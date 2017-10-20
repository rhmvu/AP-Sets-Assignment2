public class Set<E extends Comparable> implements SetInterface<E>{
    private ListInterface<E> set;

    Set(){
        init();
    }

    @Override
    public SetInterface<E> init(){
        set = new List<E>();
        return this;
    }

    @Override
    public SetInterface<E> union(SetInterface<E> toUnion){
        if (toUnion.isEmpty()) {
            return this.copy();
        }
        SetInterface<E> result = toUnion.copy();
        if (this.isEmpty()) {
            return result;
        }
        this.set.goToFirst();
        
        if (!result.contains(this.set.retrieve())){
            result.insert(this.set.retrieve());
        }
        
        while (this.set.goToNext()) {
	        if (!result.contains(this.set.retrieve())){
	            result.insert(this.set.retrieve());
	        }
        }
        return result;
    }

    @Override
    public SetInterface<E> intersection(SetInterface<E> toIntersect){
        SetInterface<E> result = new Set<E>();
        if (this.isEmpty() || set.isEmpty()) {
        	return result;
        }
        this.set.goToFirst();
        
        if (toIntersect.contains(this.set.retrieve())){
            result.insert(this.set.retrieve());
        }
        
        while (this.set.goToNext()) {
        	if (toIntersect.contains(this.set.retrieve())){
                result.insert(this.set.retrieve());
            }
        }
        return result;
    }
    @Override
    public SetInterface<E> complement(SetInterface<E> toComplement){
        SetInterface<E> result = this.copy();
        
        if (this.isEmpty() || set.isEmpty()) {
        	return result;
        }
        this.set.goToFirst();
        
        if (toComplement.contains(this.set.retrieve())){
            result.remove(this.set.retrieve());
        }
        
        while (this.set.goToNext()) {
        	if(toComplement.contains(this.set.retrieve())){
                result.remove(this.set.retrieve());
            }
        }
        return result;
    }
    @Override
    public SetInterface<E> symDifference(SetInterface<E> toSymDiffer){
        SetInterface<E> union = this.union(toSymDiffer);
        SetInterface<E> intersection = this.intersection(toSymDiffer);
        return union.complement(intersection);
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(E d){
        return set.find(d);
    }

    @Override
    public int size(){
        return set.size();
    }

    @Override
    public SetInterface<E> insert(E d){
        set.insert(d);
        return this;
    }

    @Override
    public SetInterface<E> remove(E d){
        if (set.find(d)) {
            set.remove();
        }
        return this;
    }

    @Override
    public SetInterface<E> copy(){
        Set<E> clone = new Set<E>();
        clone.set = this.set.copy();
        return clone;
    }

    @Override
    public boolean hasDoubleOccurencies(){
        set.goToFirst();
        if (isEmpty()){
            return false;
        }
        E value = set.retrieve();
        set.remove();
        
        if (set.find(value)){
            set.insert(value);
            return true;
        }
        set.insert(value);
            
        while (this.set.goToNext()) {
        	value = set.retrieve();
            set.remove();
            
            if (set.find(value)){
                set.insert(value);
                return true;
            }
            set.insert(value);
        }
        return false;
    }
    @Override
    public SetInterface<E> fixDoubleOccurencies(){
        set.goToFirst();
        
        if (isEmpty()){
            return this;
        }
        E value = set.retrieve();
        remove((E) value);
        if (!set.find(value)){
            set.insert(value);
        }
        
        while (this.set.goToNext()) {
        	value = set.retrieve();
            remove((E) value);
            
            if (!set.find(value)){
                set.insert(value);
            }
        }
        return this;
    }

    @Override
    public String toString(){
        String result = "";
        if (set.goToFirst()){
            result = set.retrieve().toString();
        }
        while (set.goToNext()){
            result += " " +set.retrieve().toString();
        }
        return result;
    }

    @Override
    public String get(){
        if (set.retrieve() == null){
            return null;
        }
        return set.retrieve().toString();
    }
    @Override
    public boolean goToFirstElement(){
        return set.goToFirst();
    }

    @Override
    public boolean goToLastElement(){
        return set.goToLast();
    }

    @Override
    public boolean goToNextElement(){
        return set.goToNext();
    }


    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare instanceof SetInterface) {
            if (toCompare.hashCode() == this.hashCode()) {
                return true;
            }
        }
        return false;
    }

}
