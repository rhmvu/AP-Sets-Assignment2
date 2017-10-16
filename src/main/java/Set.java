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
        do{
            //System.out.println("result =  " + result.toString());
            if(!result.contains(this.set.retrieve())){
                //System.out.println("inserting: " + this.set.retrieve());
                result.insert(this.set.retrieve());
            }
        }while(this.set.goToNext());

        return result;
    }

    @Override
    public SetInterface<E> intersection(SetInterface<E> toIntersect){
        SetInterface<E> result = new Set<E>();
        if (this.isEmpty() || set.isEmpty()) {
        	//System.out.println("result " + result.toString());
        	return result;
        }
        this.set.goToFirst();
        do{
            if(toIntersect.contains(this.set.retrieve())){
                result.insert(this.set.retrieve());
            }
        }while(this.set.goToNext());
    	//System.out.println("result " + result.toString());
        return result;
    }
    @Override
    public SetInterface<E> complement(SetInterface<E> toComplement){
        SetInterface<E> result = this.copy();
        
        if (this.isEmpty() || set.isEmpty()) {
        	return result;
        }
        this.set.goToFirst();
        do{
            //System.out.printf("result = %s\n",result.toString());
            //System.out.printf("set2= %s\n",toComplement.toString());

            if(toComplement.contains(this.set.retrieve())){
                //System.out.printf("contains = %d\n",this.set.retrieve());
                result.remove(this.set.retrieve());
            }
        }while(this.set.goToNext());
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
        if(set.find(d)) {
            set.remove();
            //System.out.printf("Set removed\n");
            //System.out.printf("Set Size = %d",this.size());
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
        if(isEmpty()){
            return false;
        }
        do{
            E value = set.retrieve();
            set.remove();
            if(set.find(value)){
                set.insert(value);
                return true;
            }
            set.insert(value);
        }while(this.set.goToNext());
        return false;
    }
    @Override
    public SetInterface<E> fixDoubleOccurencies(){
        set.goToFirst();
        if(isEmpty()){
            return this;
        }
        do{
            E value = set.retrieve();
            remove((E) value);
            if(!set.find(value)){
                set.insert(value);
            }
        }while(this.set.goToNext());
        return this;
    }

    @Override
    public String toString(){
        String result = "";
        if (set.goToFirst()){
            result = set.retrieve().toString();
        }
        while (set.goToNext()){
            result += " " +set.retrieve().toString(); //Only works if this works too...
        }
        return result;
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
