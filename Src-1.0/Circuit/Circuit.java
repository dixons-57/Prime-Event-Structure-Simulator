package Circuit;

import GUI.GraphicsPanel;

/* This class encapsulates all data and operations that are 
 * relevant to circuit operation, i.e. thread starting/stopping etc, l
 * loading/unloading etc. - this class is never instantiated.
 * To ensure that one instance is only ever in operation and for
 * convenience, all methods are referenced statically.
 */
public class Circuit {

	/* The array of circuit elements - each being a thread */
	public static CircuitElement[] circuitElements=new CircuitElement[0];

	/* Flag to indicate to the elements whether they should render
	 * to the screen - this is always true in normal program operation,
	 * but helps for circuit testing without the graphical overhead
	 */
	static boolean render;

	/* Loads the selected circuit */
	public static void load(boolean render){

		/* Builds the data structure for the selected
		 * circuit */
		CircuitExamples.selectCircuit();
		Circuit.render=render;

		/* Indicates to each element whether it should
		 * render to the screen and then starts
		 * the thread for that element
		 */
		for(int i=0;i<circuitElements.length;i++){
			circuitElements[i].render=render;
			circuitElements[i].start();
		}
	}

	/* Sets the pause flag to false for all elements/threads,
	 * causing their execution to continue
	 */
	public static void unPauseThreads(){
		for(int i=0;i<circuitElements.length;i++){
			circuitElements[i].paused=false;
		}
	}

	/* Sets the pause flag to true for all elements/threads,
	 * causing their execution to continue
	 */
	public static void pauseThreads(){
		for(int i=0;i<circuitElements.length;i++){
			circuitElements[i].paused=true;
		}
	}

	/* Resets every element/thread to its initial setting */
	public static void resetThreads(){
		for(int i=0;i<circuitElements.length;i++){

			/* Send the kill signal and unpause all threads
			 * This allows the threads to finish of their
			 * own accord when given the opportunity
			 */
			circuitElements[i].kill=true;
			circuitElements[i].paused=false;
		}

		/* Wait for each thread to finish */
		for(int i=0;i<circuitElements.length;i++){
			try {
				circuitElements[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/* Reload the circuit (has the effect of resetting
		 * everything)
		 */
		load(render);

		/* Cause an on-screen repaint */
		if(render){
			GraphicsPanel.global.repaint();
		}
	}

	/* Clears all circuit data from memory */
	public static void unload(){
		for(int i=0;i<circuitElements.length;i++){

			/* Send the kill signal and unpause all threads
			 * This allows the threads to finish of their
			 * own accord when given the opportunity
			 */
			circuitElements[i].kill=true;
			circuitElements[i].paused=false;
		}
		
		/* Wait for each thread to finish */
		for(int i=0;i<circuitElements.length;i++){
			try {
				circuitElements[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/* Delete the array of threads */
		circuitElements=new CircuitElement[0];
	}

}