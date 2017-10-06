public class Set<E extends Comparable> implements SetInterface<E>{

    ListInterface set;

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
        Set result = new Set();
        return result;
    }

    @Override
    public SetInterface intersection(SetInterface toIntersect){
        Set result = new Set();
        return result;
    }
    @Override
    public SetInterface complement(SetInterface toComplement){
        Set result = new Set();
        return result;
    }
    @Override
    public SetInterface symDifference(SetInterface toSymDiffer){
        Set result = new Set();
        return result;
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
    public String toString(){
        String result;
        result = "<";
        for (int i = 0;i<set.size() && set.goToFirst();i++){
            result+= set.retrieve().toString(); //Only works if this works too...
        }
        return result+=">";
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
