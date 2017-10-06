/**	@elements : objects of char
 *	@structure : singular
 *	@domain : characters
 *	@constructor - Identifier();
 *	<dl>
 *		<dt><b>PRE-condition</b><dd>		-
 *		<dt><b>POST-condition</b><dd> 	- An identifier object has been created.
 * </dl>
 **/

public interface IdentifierInterface {

    /**	@precondition  -
     *	@postcondition - The Identifier is returned as a String.
     **/
    public String toString();

    /**	@precondition  -
     *	@postcondition - returned the hashcode of the Identifier.
     **/
    public int hashCode();

    /**	@precondition  -
     *	@postcondition - The Identifier is compared to the object in the parameter.
     *                      @success:   True is returned.
     *                      @failure    False is returned.
     **/
    public boolean equals(Object toCompare);


    /**	@precondition  -
     *	@postcondition -
     *                     @success:	The String in the input has been added to the Identifier.
     *                     @failure:    The String in the input was not conform the ENBF Identifier format, input not added.
     **/
    public boolean appendValidIdentifier(String toAppend);
}
