public class Set<E extends Comparable> implements SetInterface<E>{
    static final String SET_OPEN = "{";
    static final String SET_CLOSE = "}";
    private ListInterface set;

    Set(){
        init();
    }

    @Override
    public SetInterface init(){
        set = new List();
        return this;
    }

    @Override
    public SetInterface union(SetInterface toUnion){
        SetInterface result = toUnion.copy();
        this.set.goToFirst();
        do{
            if(!result.contains(this.set.retrieve())){
               result.insert(this.set.retrieve());
            }
        }while(this.set.goToNext());
        return result;
    }

    @Override
    public SetInterface intersection(SetInterface toIntersect){
        SetInterface result = new Set();
        this.set.goToFirst();
        do{
            if(toIntersect.contains(this.set.retrieve())){
                result.insert(this.set.retrieve());
            }
        }while(this.set.goToNext());
        return result;
    }
    @Override
    public SetInterface complement(SetInterface toComplement){
        SetInterface result = this.copy();
        this.set.goToFirst();
        do{

            if(toComplement.contains(this.set.retrieve())){
                result.remove(this.set.retrieve());
            }
        }while(this.set.goToNext());
        return result;
    }
    @Override
    public SetInterface symDifference(SetInterface toSymDiffer){
        SetInterface union = this.union(toSymDiffer);
        SetInterface intersect = this.intersection(toSymDiffer);
        return union.complement(intersect);
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
    public SetInterface insert(E d){
        set.insert(d);
        return this;
    }

    @Override
    public SetInterface remove(E d){
        if(set.find(d)) {
            set.remove();
        }
        return this;
    }

    @Override
    public SetInterface copy(){
        Set clone = new Set();
        clone.set = this.set.copy();
        return clone;
    }

    @Override
    public String toString(){
        String result;
        result = SET_OPEN;
        if (set.goToFirst()){
            result += set.retrieve().toString();
        }
        while (set.goToNext()){
            result+= "," +set.retrieve().toString(); //Only works if this works too...
        }
        return result+=SET_CLOSE;
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
