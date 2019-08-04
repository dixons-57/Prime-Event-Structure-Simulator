package Main;

import GUI.MainWindow;
import GUI.OutputFrame;

/* Class that represents entry point for the program - this serves only
 * to allow for an obvious launcher class
 */
public class Main {

	/* Main method for the JVM to execute */
    public static void main(String[] args){
    	
    	/* Spawns the main window - program is blocked from
    	 * halting until this window is closed
    	 */
        @SuppressWarnings("unused")
		MainWindow window=new MainWindow();
        OutputFrame.global.write("Program started");
    }
    
}
