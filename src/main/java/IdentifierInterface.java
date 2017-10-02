/**	@elements : objects of type String
 *	@structure : singular
 *	@domain : any String
 *	@constructor - Identifier();
 *	<dl>
 *		<dt><b>PRE-conditie</b><dd>		-
 *		<dt><b>POST-conditie</b><dd> 	An identifier object has been created which can be returned as a String.
 * </dl>
 **/

public interface IdentifierInterface<String> {

    /**	@precondition  -
     *	@postcondition - The identifier is returned in Stringform.
     **/
    String getString();
}
