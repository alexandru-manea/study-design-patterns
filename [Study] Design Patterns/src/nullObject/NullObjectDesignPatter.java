package nullObject;

public class NullObjectDesignPatter
{
	/**
	 * Null object - object with defined neutral (null) behaviour
	 * 			   - encapsulate the absence of an object by providing a substitutable alternative that offers suitable
	 * 				 default do nothing behaviour
	 * 
	 * Null object design pattern - describes the uses of such objects
	 * 
	 * Motivation - references have to be checked to ensure they are not null before invoking any methods
	 * 			  - given that an object reference can be null, and that the result of a null check is to do nothing
	 *				or use some default value, how can the absence of an object (the presence of a null reference)
	 *				can be treated transparently?
	 *			  - sometimes a class that requires a collaborator does not need the collaborator to do anything, but
	 *			    it needs to treat it the same way as one that actually provides behaviour
	 *
	 * 
	 * Description - instead of using a null reference to convey the absence of an object (e.g., non-existing costumer),
	 * one uses an object which implements the expected behaviour, but whose method body is empty.
	 * 
	 * Advantages - predictability
	 *
	 * Disadvantages - 
	 * 
	 * Uses - stubs for testing
	 *      - when a collaborator instance which should do nothing is required
	 *      - want to abstract the handling of null away from the client
	 *      
	 * HOW
	 * 
	 */
	
	 class Client
	 {
		 
	 }
	
	 abstract class AbstractObject
	 {
		 public abstract void request();
	 }
	
	 class RealObject extends AbstractObject
	 {
		 public void request()
		 {
			 System.out.println("Doing work...");
		 }
	 }
	 
	 class NullObject extends AbstractObject
	 {
		 public void request() {}
	 }
	
	
	
	
	
	
	
	
	
	
}