package Circuit;

/* Common parent class for all circuit elements, this allows
 * a slight reduction in attribute repetition and allows the
 * storing of various sub-classes in a CircuitElement array
 */
public class CircuitElement extends Thread {

	/* Flags to control execution flow */
	boolean paused=true;
	boolean kill=false;
	
	/* Flag to indicate whether the element should render
	 * to the screen - this is always true in normal program operation,
	 * but helps for circuit testing without the graphical overhead
	 */
	boolean render=true;
}
