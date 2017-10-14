/**	@elements : objects of type E
 *	@structure : Sequential
 *	@domain : Elements which extend Comparable
 *	@constructor - Set();
 *	<dl>
 *		<dt><b>PRE-condition</b><dd>		-
 *		<dt><b>POST-condition</b><dd> 	A Set object has been created which can be returned as a String.
 * </dl>
 **/
public interface SetInterface<E extends Comparable> {



    /**	@precondition -
     *  @postcondition - The current Set has been initialized and returned as a SetInterface.
     **/
    SetInterface<E> init();

    /**	@precondition -
     *  @postcondition - The Union of the current Set and the parameterized Set has been returned as a SetInterface.
     **/
    SetInterface<E> union(SetInterface<E> toUnion);

    /**	@precondition -
     *  @postcondition - The Intersection of the current Set and the parameterized Set has been returned as a SetInterface.
     **/
    SetInterface<E> intersection(SetInterface<E> toIntersect);

    /**	@precondition -
     *  @postcondition - The Complement of the current Set and the parameterized Set has been returned as a SetInterface.
     **/
    SetInterface<E> complement(SetInterface<E> toComplement);

    /**	@precondition -
     *  @postcondition - The Symantic Difference of the current Set and the parameterized Set has been returned as a SetInterface.
     **/
    SetInterface<E> symDifference(SetInterface<E> toSymDiffer);

    /**	@precondition -
     *  @postcondition - FALSE: Set is not empty.
     *  				TRUE:  Set is empty.
     **/
    boolean isEmpty();

    /**	@precondition -
     *  @postcondition - FALSE: current Set does NOT contain parameterized Object.
     *  				TRUE:  current Set does contain parameterized Object.
     **/
    boolean contains(E d);

    /**	@precondition  -
     *	@postcondition - The number of elements has been returned.
     **/
    int size();

    /**	@precondition  - d must be Comparable.
     *	@postcondition - Element d has been added to the Set
     *                   Set-POST has been returned.
     **/
    SetInterface<E> insert(E d);

    /**	@precondition  - Set must not be empty.
     *	@postcondition - Element d has been removed from the Set
     *                   Set-POST has been returned.
     **/
    SetInterface<E> remove(E d);

    /**
     * @precondition -
     * @postcondition A deep copy of the Set has been returned.
     */
    SetInterface<E> copy();

    /**	@precondition -
     *  @postcondition - FALSE: current Set does NOT contain 2 elements with the same value;
     *  				TRUE:  current Set does contain 2 elements with the same value;
     **/
    boolean hasDoubleOccurencies();

    /**	@precondition -
     *  @postcondition - set-POST contains unique elements.
     **/
    SetInterface<E> fixDoubleOccurencies();
    /**	@precondition  -
     *	@postcondition - The Set is returned in Stringform.
     **/
    String toString();


}
