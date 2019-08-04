package Circuit;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import GUI.GraphicsPanel;
import GUI.MainWindow;
import Main.GlobalAttributes;

/* JFrame which encapsulates all operations and data concerning the statistical analysis
 * of a circuit. This will run the selected circuit repeatedly until stopped, and 
 * calculate (using a Bayesian model) the probability of a particular output occurring. 
 * This is not available for circuits which do not have conflict resolution or do but 
 * possess livelocking (due to the circuit's nature). Whilst this frame is active, the 
 * main window is disabled.*/
public class StatTesting extends JFrame implements WindowListener, ActionListener, Runnable{

	private static final long serialVersionUID = 1L;
	
	/* Main content pane */
	JPanel contentPane=new JPanel();
	
	/* Start and stop buttons for controlling analysis execution */
	JButton start=new JButton("Start Testing");
	JButton stop=new JButton("End Testing");
	
	/* The main analysis thread */
	Thread testThread=new Thread(this);
	
	/* The text area for displaying the results of the analysis */
	JTextArea output=new JTextArea();
	
	/* Flags to control execution of the analysis */
	boolean execute=false;
	boolean kill=false;
	
	/* The circuit to analyse */
	int circuitNo;
	
	/* The maximum wait to apply to each element in the circuit
	 * when analysing, this is different from the global attribute,
	 * as we are interested in the final result, not viewing the
	 * execution */
	int maxWait=100;
	
	/* Store the previous global value of maxWait, in order to restore
	 * it afterward analysis is complete */
	int previousMaxWait;
	
	/* Constructor which sets up the window and adds listeners */
	public StatTesting(){
		
		/* Configure the window */
		super("Statistical Analysis");
		this.setContentPane(contentPane);
		this.setPreferredSize(new Dimension(500,300));
		this.setMinimumSize(new Dimension(500,300));
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setIconImage(GlobalAttributes.icon);
		
		/* Add the output and the two buttons to the content
		 * pane */
		contentPane.add(output);
		contentPane.add(start);
		contentPane.add(stop);
		
		/* Configure the output area */
		output.setPreferredSize(new Dimension(480,230));
		output.setEditable(false);

		/* Record locally the circuit we are analysing
		 * and update the global maximum wait with the
		 * local value we are using */
		circuitNo=GlobalAttributes.circuitSelected;
		previousMaxWait=GlobalAttributes.maxWait;
		GlobalAttributes.maxWait=this.maxWait;
		
		/* Add button action listeners */
		start.addActionListener(this);
		stop.addActionListener(this);
		
		/* Add window listener to record when the window
		 * is closed */
		this.addWindowListener(this);
		
		/* Start the analysis thread, this will be paused
		 * initially */
		testThread.start();
	}

	/* Required, unused method */
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	/* Required, unused method */
	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	/* Called when this window is closed */
	@Override
	public void windowClosing(WindowEvent arg0) {
		
		/* Send the kill signal and wait
		 * for the thread to finish */
		kill=true;
		try {
			testThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/* Restore the previous maximum wait */
		GlobalAttributes.maxWait=previousMaxWait;
		
		/* Load the circuit back to the default */
		Circuit.load(true);
		
		/* Re-enable the main window */
		GraphicsPanel.global.setIgnoreRepaint(false);
		GraphicsPanel.global.setVisible(true);
		MainWindow.global.setEnabled(true);
	}

	/* Required, unused method */
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	/* Required, unused method */
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	/* Required, unused method */
	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	/* Required, unused method */
	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	/* Action listener method - which is the sole instance
	 * of this class */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		/* If the start button is clicked */
		if(arg0.getActionCommand().equals("Start Testing")){
			
			/* Unpause the thread, enable the stop button
			 * and disable the start button */
			execute=true;
			start.setEnabled(false);
			stop.setEnabled(true);
		}
		
		/* If the stop button is clicked */
		else{
			
			/* Pause the thread, disable the stop button
			 * and enable the start button */
			execute=false;
			start.setEnabled(true);
			stop.setEnabled(false);
		}
	}

	/* Execution logic for the analysis thread */
	@Override
	public void run() {
		
		/* Record the number of tests performed */
		int noOfTests=0;
		
		/* Stores the number of successes for
		 * each possible output - this array
		 * is the length of the number of possible
		 * outputs for the current circuit */
		int[] results=prepareVariables();
		
		/* Infinite loop until kill signal */
		while(!kill){	
			if(execute){
				
				/* Load the circuit and execute */
				Circuit.load(true);
				Circuit.unPauseThreads();
				
				/* Await the results */
				awaitResult(results);
				
				/* Increment the number of tests
				 * performed */
				noOfTests++;
				
				/* Update the results on-screen */
				updateOutput(noOfTests,results);
				
				/* Clear the circuit data and state */
				Circuit.unload();
			}
			else{
				Thread.yield();
			}
		}
	}
	
	/* Create the length of the results array to be
	 * equal to the number of possible outputs from
	 * the circuit - This is hardcoded for each possible
	 * circuit selected */
	private int[] prepareVariables(){
		if(circuitNo>=1&&circuitNo<=4){
			return new int[2];
		}
		else if(circuitNo==5 || circuitNo==6){
			return new int[3];
		}
		else if(circuitNo>=7 && circuitNo<=10){
			return new int[3];
		}
		else if(circuitNo>=11 && circuitNo<=14){
			return new int[2];
		}
		return null;
	}
	
	/* Awaits a successful output from the circuit - the final output
	 * wires are examined for signals, and when a valid output occurs, the
	 * result is recorded. As this is specific to each circuit, the possible
	 * outputs are NOT documented */
	private void awaitResult(int[] results){
		if(circuitNo>=1&&circuitNo<=4){
			while(!(((WireThread)Circuit.circuitElements[16]).hasArrived()&&((WireThread)Circuit.circuitElements[18]).hasArrived()) &&
			!((WireThread)Circuit.circuitElements[17]).hasArrived()){
				Thread.yield();
			}
			if(((WireThread)Circuit.circuitElements[16]).hasArrived() && ((WireThread)Circuit.circuitElements[18]).hasArrived()){
				results[0]++;
			}
			else if(((WireThread)Circuit.circuitElements[17]).hasArrived()){
				results[1]++;
			}
		}
		else if(circuitNo==5 || circuitNo==6){
			while(!((WireThread)Circuit.circuitElements[16]).hasArrived()&&!((WireThread)Circuit.circuitElements[17]).hasArrived() &&
					!((WireThread)Circuit.circuitElements[18]).hasArrived()){
						Thread.yield();
					}
					if(((WireThread)Circuit.circuitElements[16]).hasArrived()){
						results[0]++;
					}
					else if(((WireThread)Circuit.circuitElements[17]).hasArrived()){
						results[1]++;
					}
					else if(((WireThread)Circuit.circuitElements[18]).hasArrived()){
						results[2]++;
					}
		}
		else if(circuitNo>=7 && circuitNo<=10){
			while(!(((WireThread)Circuit.circuitElements[16]).hasArrived()&&((WireThread)Circuit.circuitElements[18]).hasArrived()) &&
					!(((WireThread)Circuit.circuitElements[16]).hasArrived()&&((WireThread)Circuit.circuitElements[25]).hasArrived()) &&
					!(((WireThread)Circuit.circuitElements[17]).hasArrived()&&((WireThread)Circuit.circuitElements[25]).hasArrived())){
						Thread.yield();
					}
					if(((WireThread)Circuit.circuitElements[16]).hasArrived() &&
							((WireThread)Circuit.circuitElements[18]).hasArrived()){
						results[0]++;
					}
					else if(((WireThread)Circuit.circuitElements[16]).hasArrived() &&
							((WireThread)Circuit.circuitElements[25]).hasArrived()){
						results[1]++;
					}
					else if(((WireThread)Circuit.circuitElements[17]).hasArrived() &&
							((WireThread)Circuit.circuitElements[25]).hasArrived()){
						results[2]++;
					}
		}
		else if(circuitNo>=11 && circuitNo<=14){
			while(!(((WireThread)Circuit.circuitElements[16]).hasArrived()&&((WireThread)Circuit.circuitElements[18]).hasArrived()) &&
					!(((WireThread)Circuit.circuitElements[17]).hasArrived()&&((WireThread)Circuit.circuitElements[25]).hasArrived())){
						Thread.yield();
					}
					if(((WireThread)Circuit.circuitElements[16]).hasArrived() &&
							((WireThread)Circuit.circuitElements[18]).hasArrived()){
						results[0]++;
					}
					else if(((WireThread)Circuit.circuitElements[17]).hasArrived() &&
							((WireThread)Circuit.circuitElements[25]).hasArrived()){
						results[1]++;
					}
		}
	}
	
	/* This takes the number of tests and the result data and displays
	 * this on the screen. This is acheived by calculating the ratio
	 * of successes between the possible outputs, and displaying this as
	 * a percentage of the overall number of tests. Again, as this is circuit
	 * specific, each possible analysis is NOT documented */
	private void updateOutput(int noOfTests, int[] results){
		output.setText("Number of tests: "+noOfTests+"\n\n");
		if(circuitNo>=1&&circuitNo<=4){
			float percentage1=(((float)results[0])/((float)noOfTests))*100;
			float percentage2=(((float)results[1])/((float)noOfTests))*100;
			output.append("Number of \"a\"/\"c\" successes: "+results[0]);
			output.append("\nNumber of \"b\" successes: "+results[1]);
			output.append("\n\nPercentage of \"a\"/\"c\" successes: "+percentage1+"%");
			output.append("\n\nPercentage of \"b\" successes: "+percentage2+"%");
		}
		else if(circuitNo==5 || circuitNo==6){
				float percentage1=(((float)results[0])/((float)noOfTests))*100;
				float percentage2=(((float)results[1])/((float)noOfTests))*100;
				float percentage3=(((float)results[2])/((float)noOfTests))*100;
				output.append("Number of \"a\" successes: "+results[0]);
				output.append("\nNumber of \"b\" successes: "+results[1]);
				output.append("\nNumber of \"c\" successes: "+results[2]);
				output.append("\n\nPercentage of \"a\" successes: "+percentage1+"%");
				output.append("\n\nPercentage of \"b\" successes: "+percentage2+"%");
				output.append("\n\nPercentage of \"c\" successes: "+percentage3+"%");
		}
		else if(circuitNo>=7 && circuitNo<=10){
			float percentage1=(((float)results[0])/((float)noOfTests))*100;
			float percentage2=(((float)results[1])/((float)noOfTests))*100;
			float percentage3=(((float)results[2])/((float)noOfTests))*100;
			output.append("Number of \"a\"/\"c\" successes: "+results[0]);
			output.append("\nNumber of \"a\"/\"d\" successes: "+results[1]);
			output.append("\nNumber of \"b\"/\"d\" successes: "+results[2]);
			output.append("\n\nPercentage of \"a\"/\"c\" successes: "+percentage1+"%");
			output.append("\n\nPercentage of \"a\"/\"d\" successes: "+percentage2+"%");
			output.append("\n\nPercentage of \"b\"/\"d\" successes: "+percentage3+"%");
		}
		else if(circuitNo>=11 && circuitNo<=14){
			float percentage1=(((float)results[0])/((float)noOfTests))*100;
			float percentage2=(((float)results[1])/((float)noOfTests))*100;
			output.append("Number of \"a\"/\"c\" successes: "+results[0]);
			output.append("\nNumber of \"b\"/\"d\" successes: "+results[1]);
			output.append("\n\nPercentage of \"a\"/\"c\" successes: "+percentage1+"%");
			output.append("\n\nPercentage of \"b\"/\"d\" successes: "+percentage2+"%");
		}
	}
	
}
