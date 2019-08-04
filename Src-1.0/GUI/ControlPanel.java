package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Circuit.Circuit;
import Circuit.CircuitExamples;
import Circuit.StatTesting;
import Main.GlobalAttributes;
import Model.EventStructure;
import Model.TransitionGraph;

/* This class encapsulates the control panel that is viewed on the left hand
 * side of the main window */
public class ControlPanel extends JPanel 
implements ActionListener, ItemListener{
	
	private static final long serialVersionUID = 1L;

	/* Switches between graph mode and circuit mode - currently only
	 * graph mode is implemented */
    @SuppressWarnings("rawtypes")
	JComboBox mode = new JComboBox();
   
    /* Holds the mode combo box */
    JPanel modePanel = new JPanel();
	
	/* Holds general buttons relevant to all modes */
	JPanel buttonPanel = new JPanel();
    
    /* Displays buttons when the program is in graph mode */
    JPanel graphButtonPanel=new JPanel();
	
	/* Displays buttons relevant to circuit mode */
	JPanel circuitButtonPanel = new JPanel();
	
	/* Holds the analysis button and information when
	 * in circuit drawing mode */
	JPanel analysisButtonPanel = new JPanel();
	
	/* Allows the user to select a circuit from a drop-down list -
	 * this is public because all items are added by the Circuit
	 * subsystem package */
	@SuppressWarnings("rawtypes")
	public JComboBox circuitSelection=new JComboBox();
	
	/* Contains the definition of the event structure which the
	 * current circuit is emulating */
	JTextArea circuitInfo=new JTextArea();
	JScrollPane infoScrollPane=new JScrollPane(circuitInfo);
    
    /* Input field for entering a random number generator seed */
    JTextField randomSeed = new JTextField(8);
    
    /* Input field for entering the maximum wait period in ms */
    JTextField maxWait = new JTextField(8);
    
    /* Controls generation of the transition graph */
    JCheckBox checkbox = new JCheckBox();

    /* Switches between causality graph and transition mode */
	JButton view = new JButton("Switch view");
	
	/* Enters circuit analysis mode - this is public as the
	 * Circuit subsystem controls its availability
	 */
	public JButton analysis=new JButton("Analyse");
    
    /* Buttons for controlling execution of the event structure */
	JButton load = new JButton("Load");
	JButton start = new JButton("Start");
	JButton stop = new JButton("Stop");
	JButton reset = new JButton("Reset");
	
	/* Button to show/hide the console output window */
	JButton console = new JButton("Console");
	
	/* Button to show the help window */
	JButton help = new JButton("Help");

	/* Text area for the user-defined Event Structure - this is
	 * parsed when clicking the Load button */
	public JTextArea inputArea = new JTextArea();
	JScrollPane inputScrollPane= new JScrollPane(inputArea);
	
	/* Labels for each relevant mode */
	JLabel modelMessage=new JLabel("Define Prime Event Structure");
	JLabel circuitMessage=new JLabel("Select circuit implementation");
	Component messageGap=Box.createRigidArea(new Dimension(78,0));
	
	/* Global instance of this class to allow for convenient referencing
	 * throughout the program - only ever one instance so this is preferred
	 */
	public static ControlPanel global;
	
	/* Class constructor - configures the panel */
    @SuppressWarnings("unchecked")
	public ControlPanel(){
    	
    	/* Calls the super class JPanel constructor */
        super();
        
        /* Sets the global instance variable to this instance */
        global=this;
        
        /* Sets the layout manager to BorderLayout */
        this.setLayout(new BorderLayout());
        
        /* Add the default item to the mode combo box */
        mode.addItem("Model");
        mode.addItem("Circuit");
        
        /* Load the list of available circuits into the drop-down list -
         * this occurs even if we are not in circuit mode as it requires
         * simpler logic to do it immediately and does not incur much overhead
         * anyway */
        CircuitExamples.loadList();
        
        /* Configure the JPanel responsible for holding the mode
         * combo box - set labels and blank space accordingly */
        modePanel.setPreferredSize(new Dimension(300,60));
        modePanel.add(new JLabel("Mode:"));
        modePanel.add(mode);
        modePanel.add(Box.createRigidArea(new Dimension(300,5)));
        modePanel.add(modelMessage);
        modePanel.add(messageGap);
        this.add(modePanel,BorderLayout.PAGE_START);
        
        /* Configure the user-defined Event Structure input area */
        inputArea.setLineWrap(true);
        inputArea.setBackground(Color.LIGHT_GRAY);
        inputArea.setForeground(Color.BLACK);
        inputArea.setFont(new Font("Arial", Font.PLAIN, 16));       
        inputArea.setDisabledTextColor(Color.BLACK);
        this.add(inputScrollPane,BorderLayout.CENTER);
        
        /* Configure the circuit information area (similar to
         * the above input area) */
        circuitInfo.setEditable(false);
        circuitInfo.setLineWrap(true);
        circuitInfo.setBackground(Color.LIGHT_GRAY);
        circuitInfo.setForeground(Color.BLACK);
        circuitInfo.setFont(new Font("Arial", Font.PLAIN, 16));      
        circuitInfo.setPreferredSize(new Dimension(300,205));
		circuitInfo.setText(CircuitExamples.loadDescription(1));
        
        /* Set a default event structure definition */
        inputArea.setText("a;b;c;d;e;f;g;h;i;j;\na<b;\n" +
        		"a<f;\nb#f;\nb<c;\nc<d;\nf<g;\nh<i;\ni<j;");
        
        /* Configure the buttons and information that only appear
         * when in graph mode */
        graphButtonPanel.add(new JLabel("Generate Transition Graph"),BorderLayout.PAGE_END);
        graphButtonPanel.add(checkbox,BorderLayout.PAGE_END);
        graphButtonPanel.add(view,BorderLayout.PAGE_END);
        
        /* Configure the buttons and information that only appear
         * when in circuit mode */
        circuitButtonPanel.setPreferredSize(new Dimension(300,50));
        circuitButtonPanel.setLayout(new BorderLayout());
        circuitButtonPanel.add(circuitSelection,BorderLayout.PAGE_START);
        circuitButtonPanel.add(infoScrollPane,BorderLayout.CENTER);
        circuitSelection.setPreferredSize(new Dimension(290,25));
        circuitSelection.setMaximumRowCount(5);
        
        /* Configure the panel which holds the analysis
         * info and button (part of circuit mode display) */
        analysisButtonPanel.add(new JLabel("Statistical Analysis"));
        analysisButtonPanel.add(analysis);
        
        /* Configure the JPanel responsible for general
         * input fields and buttons - set labels and blank space
         * accordingly */
        buttonPanel.add(new JLabel("Random Seed:"));
        buttonPanel.add(randomSeed);
        buttonPanel.add(Box.createRigidArea(new Dimension(300,0)));
        buttonPanel.add(new JLabel("Ideal Maximum Wait:"));
        buttonPanel.add(maxWait);
        buttonPanel.add(new JLabel("ms"));
        buttonPanel.add(Box.createRigidArea(new Dimension(300,0)));
        buttonPanel.add(load);
        load.setPreferredSize(new Dimension(90,25));
        buttonPanel.add(Box.createRigidArea(new Dimension(30,0)));
        buttonPanel.add(reset);
        reset.setPreferredSize(new Dimension(90,25));
        buttonPanel.add(Box.createRigidArea(new Dimension(300,0)));
        buttonPanel.add(start);
        start.setPreferredSize(new Dimension(90,25));
        buttonPanel.add(Box.createRigidArea(new Dimension(30,0)));
        buttonPanel.add(stop);
        stop.setPreferredSize(new Dimension(90,25));
        buttonPanel.add(Box.createRigidArea(new Dimension(300,0)));
        buttonPanel.add(console);
        console.setPreferredSize(new Dimension(90,25));
        buttonPanel.add(Box.createRigidArea(new Dimension(30,0)));
        buttonPanel.add(help);
        help.setPreferredSize(new Dimension(90,25));
        buttonPanel.setPreferredSize(new Dimension(300,205));
        
        /* Add the button panel to the graph panel as this is the default
         * mode */
        graphButtonPanel.add(buttonPanel);
        this.add(graphButtonPanel,BorderLayout.PAGE_END);
        
        /* Set initial states of buttons and input fields */
        reset.setEnabled(false);
        start.setEnabled(false);
        stop.setEnabled(false);
        view.setEnabled(false);
        randomSeed.setText("11111111");
        maxWait.setText("3000");
        GlobalAttributes.random.setSeed(11111111);
        GlobalAttributes.seed=11111111;
        GlobalAttributes.maxWait=3000;
        
        /* Set the alignment of the input fields */
        randomSeed.setHorizontalAlignment(JTextField.RIGHT);
        maxWait.setHorizontalAlignment(JTextField.RIGHT);
        
        /* Set final configuration options for this panel as a whole*/
        this.setVisible(true);
        this.setBackground(GlobalAttributes.controlsBackground);  
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        /* Set the action listener for the various buttons - which is this
         * class instance */
        load.addActionListener(this);
        reset.addActionListener(this);
        start.addActionListener(this);
        stop.addActionListener(this);
        console.addActionListener(this);
        help.addActionListener(this);
        view.addActionListener(this);
        analysis.addActionListener(this);
        
        /* Set the item listener for the combo boxes - which is simply
         * this class again */
        mode.addItemListener(this);
        circuitSelection.addItemListener(this);
    }

    /* Action listener method for the various control buttons */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		/* If the "Switch view" button is pressed */
		if(arg0.getActionCommand().equals("Switch view")){
			
			/* Switch the render mode */
			if(GlobalAttributes.renderMode==1 && GlobalAttributes.transitions){
				GlobalAttributes.renderMode=2;
			}
			else if(GlobalAttributes.renderMode==2){
				GlobalAttributes.renderMode=1;
			}
			
			/* And force a repaint */
			GraphicsPanel.global.repaint();
		}
		/* If the Console button is pressed */
		else if(arg0.getActionCommand().equals("Console")){
			
			/* If the window is visible then ihide it */
			if(OutputFrame.global.isVisible()){
				OutputFrame.global.setVisible(false);
			}
			
			/* Otherwise make it visible */
			else{
				OutputFrame.global.setVisible(true);
			}
		}
		
		/* If the Load button is pressed */
		else if(arg0.getActionCommand().equals("Load")){
			
			/* If we are in causality/transition graph mode */
			if(GlobalAttributes.renderMode==1 || GlobalAttributes.renderMode==2){
				
				/* Call upon the EventStructure class to parse the
				 * input and allow the execution buttons to be pressed
				 */
			    OutputFrame.global.write("Preparing event structure");
			    EventStructure.load();
			    
				/* If there is an event structure loaded */
				if(EventStructure.events.length>0){
					
					/* Enable the button which allow the user
					 * to switch to transition graph view -
					 * providing that the option to generate
					 * the graph was selected when loading
					 * the event structure. Also record
					 * that the transition graph is available
					 */
					if(checkbox.isSelected()){
						TransitionGraph.buildGraph();
						GlobalAttributes.transitions=true;
						view.setEnabled(true);
					}
					
					/* Otherwise disable transition graph
					 * relevant buttons and options
					 */
					else{
						GlobalAttributes.transitions=false;
						GlobalAttributes.renderMode=1;
						view.setEnabled(false);
					}
					
					/* Calculate the on-screen positions of whatever data we are displaying */
					GraphicsPanel.global.calculatePositions();
				}
			}
			
			/* Otherwise if in circuit mode */
			else{
				OutputFrame.global.write("Loading circuit");
				
				/* Load the circuit selected in the combo box */
				GlobalAttributes.circuitSelected=circuitSelection.getSelectedIndex()+1;
				Circuit.load(true);
			}
			
			/* Enable relevant execution control buttons */
		    start.setEnabled(true);
		    reset.setEnabled(true);
		    maxWait.setEnabled(true);
		    randomSeed.setEnabled(true);
		    checkbox.setEnabled(true);
		}
		
		/* If the Reset button is pressed */
		else if(arg0.getActionCommand().equals("Reset")){
			
			/* If we are in causality/transition graph mode */
			if(GlobalAttributes.renderMode==1 || GlobalAttributes.renderMode==2){
				
				/* Call upon the EventStructure class to reset
				 * the completion of events to their initial state
				 */
			    EventStructure.resetThreads();
			    OutputFrame.global.write("Event Structure reset");
			}
			
			/* If we are in circuit mode */
			else{
				
				/* Call upon the Circuit class to reset
				 * the completion of events to their initial state
				 */
			    Circuit.resetThreads();
			    OutputFrame.global.write("Circuit reset");
			}
			
			/* Re-enable the input fields */
		    maxWait.setEnabled(true);
		    randomSeed.setEnabled(true);
		}
		
		/* If the Start button is pressed */
		else if(arg0.getActionCommand().equals("Start")){
		    OutputFrame.global.write("Execution started");
		    
		    /* Disable all relevant buttons */
		    start.setEnabled(false);
		    load.setEnabled(false);
		    
			/* If we are in causality/transition graph mode then disable
			 * the input field and checkbox */
		    if(GlobalAttributes.renderMode==1 || GlobalAttributes.renderMode==2){
		    	inputArea.setEnabled(false);
		    	checkbox.setEnabled(false);
		    }
		    
		    /* Enable the stop button */
		    stop.setEnabled(true);
		    
		    /* Disable the mode selection box and the reset button */
		    mode.setEnabled(false);
		    reset.setEnabled(false);
		    
		    /* Parse the Random Seed field and set the pseudo-random
		     * generator to this seed - if this fails then the value
		     * will remain unchanged and stay as before
		     */
		    if(randomSeed.isEnabled() && maxWait.isEnabled()){
			    try{
			    	int seed=Integer.parseInt(randomSeed.getText());
			    	if(seed!=GlobalAttributes.seed){
				        GlobalAttributes.random.setSeed(seed);
				        GlobalAttributes.seed=seed;
			    	}
			    }catch(Exception e){
			    	randomSeed.setText(Integer.toString(GlobalAttributes.seed));
			    }
			    
			    /* Parse the Maximum Wait field and set the global attribute
			     * to this value - if this fails then the value will remain
			     * unchanged and stay as before
			     */
			    try{
			    	int wait=Integer.parseInt(maxWait.getText());
			        GlobalAttributes.maxWait=wait;
			    }catch(Exception e){
			    	maxWait.setText(Integer.toString(GlobalAttributes.maxWait));
			    }
		    }
		    
		    /* Disable seed and wait inputs until reset/load */
		    maxWait.setEnabled(false);
		    randomSeed.setEnabled(false);
		    
		    /* If we are in graph mode */
		    if(GlobalAttributes.renderMode==1 || GlobalAttributes.renderMode==2){
		    	
			    /* Start the execution threads */
			    EventStructure.unPauseThreads();
		    }
		    
		    /* If we are in circuit mode */
		    else{
		    	
		    	/* Start the circuit threads */
		    	Circuit.unPauseThreads();	
		    }
		}
		
		/* If the Stop button is pressed */
		else if(arg0.getActionCommand().equals("Stop")){
		    OutputFrame.global.write("Execution stopped");
		    
		    /* If we are in graph mode */
		    if(GlobalAttributes.renderMode==1 || GlobalAttributes.renderMode==2){
		    	
			    /* Stop the execution threads */
			    EventStructure.pauseThreads();
		    }
		    
		    /* If we are in circuit mode */
		    else{
		    	
		    	/* Stop the circuit threads */
		    	Circuit.pauseThreads();	
		    }
		    
		    /* Enable all relevant buttons */
		    start.setEnabled(true);
		    load.setEnabled(true);
		    reset.setEnabled(true);
		    
		    /* If we are in graph mode, then re-enable the input area
		     * and the checkbox
		     */
		    if(GlobalAttributes.renderMode==1 || GlobalAttributes.renderMode==2){
		    	inputArea.setEnabled(true);
		    	checkbox.setEnabled(true);
		    }
		    
		    /* Disable the stop button and enable the mode selection
		     * box */
		    stop.setEnabled(false);
		    mode.setEnabled(true);
		}
		
		/* If the Help button is pressed then show the
		 * Help window */
		else if(arg0.getActionCommand().equals("Help")){
			
			/* If the Help window has not yet been instantiated
			 * then create an instance */
			if(HelpScreen.global==null){
				new HelpScreen();
			}
			
			/* If the window is visible then it hides it */
			else if(HelpScreen.global.isVisible()){
				HelpScreen.global.setVisible(false);
			}
			
			/* Otherwise it makes it visible */
			else{
				HelpScreen.global.setVisible(true);
			}
		}
		
		/* If the Analyse button is pressed, then disable
		 * GUI control of the main window, and then
		 * create a new statistical analyse window
		 */
		else if(arg0.getActionCommand().equals("Analyse")){
			MainWindow.global.setEnabled(false);
			new StatTesting();
		}
	}

    /* Item listener method for the two combo boxes (mode selection and circuit
     * selection) */
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		
		/* This prevents two events from being fired
		 * by the combo box when changing options (a select and
		 * then a de-select event) */
		if(arg0.getStateChange()==ItemEvent.SELECTED){
			
			/* Set to graph mode if selected */
			if(arg0.getSource().equals(mode)&&mode.getSelectedIndex()==0){
				
				/* Set to causality mode by default and unload the current
				 * circuit */
				GlobalAttributes.renderMode=1;
				Circuit.unload();
				
				/* Hide, show, and re-organise the relevant labels
				 * and buttons */
				this.remove(circuitButtonPanel);
				this.remove(analysisButtonPanel);
				this.add(inputScrollPane,BorderLayout.CENTER);
				this.add(graphButtonPanel,BorderLayout.PAGE_END);
				analysisButtonPanel.remove(buttonPanel);
				graphButtonPanel.add(buttonPanel);
				modePanel.remove(circuitMessage);
				modePanel.remove(messageGap);
				modePanel.add(modelMessage);
				modePanel.add(messageGap);
				
				/* Re-validate the visual components (adjusts
				 * layouts accordingly) */
				modePanel.validate();
				graphButtonPanel.validate();
				MainWindow.global.validate();
				modePanel.repaint();
				graphButtonPanel.repaint();
				MainWindow.global.repaint();
			}
			
			/* Set to circuit mode if selected */
			else if(arg0.getSource().equals(mode)&&mode.getSelectedIndex()==1){
				
				/* Set to circuit mode and unload the current
				 * event structure */
				GlobalAttributes.renderMode=3;
				EventStructure.unload();
				
				/* Hide, show, and re-organise the relevant labels
				 * and buttons */
				this.remove(inputScrollPane);
				this.remove(graphButtonPanel);
				this.add(circuitButtonPanel,BorderLayout.CENTER);
				this.add(analysisButtonPanel,BorderLayout.PAGE_END);
				graphButtonPanel.remove(buttonPanel);
				analysisButtonPanel.add(buttonPanel);
				analysis.setEnabled(false);
				modePanel.remove(modelMessage);
				modePanel.remove(messageGap);
				modePanel.add(circuitMessage);
				modePanel.add(messageGap);
				
				/* Re-validate the visual components (adjusts
				 * layouts accordingly) */
				modePanel.validate();
				circuitButtonPanel.validate();
				MainWindow.global.validate();
				modePanel.repaint();
				circuitButtonPanel.repaint();
				MainWindow.global.repaint();
			}
			
			/* If the circuit selected has been changed, then
			 * set the appropriate description */
			else if(arg0.getSource().equals(circuitSelection)){
				circuitInfo.setText(CircuitExamples.loadDescription(circuitSelection.getSelectedIndex()+1));
			}
			
			/* Set all control buttons and input fields 
			 * to their default states */
			reset.setEnabled(false);
			start.setEnabled(false);
			stop.setEnabled(false);
			checkbox.setSelected(false);
			view.setEnabled(false);
			randomSeed.setEnabled(true);
			maxWait.setEnabled(true);
			
			/* Repaint the render area */
			GraphicsPanel.global.repaint();
		}
	}
    
}
