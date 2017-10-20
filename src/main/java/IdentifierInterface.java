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
    String toString();

    /**	@precondition  -
     *	@postcondition - returned the hashcode of the Identifier.
     **/
    int hashCode();

    /**	@precondition  -
     *	@postcondition - The Identifier is compared to the object in the parameter.
     *                      @success:   True is returned.
     *                      @failure    False is returned.
     **/
    boolean equals(Object toCompare);


    /**	@precondition  - The String in the input does not disrupt the EBNF Identifier format.
     *	@postcondition - The String in the input has been added to the Identifier.
     **/
    void appendIdentifier(String toAppend);

    /**	@precondition  -
     *	@postcondition -
     *                     @success:	The String in the input was conform the ENBF Identifier format.
     *                     @failure:    The String in the input was not conform the ENBF Identifier format.
     **/
    boolean hasCorrectIdentifierFormat(String input);
}