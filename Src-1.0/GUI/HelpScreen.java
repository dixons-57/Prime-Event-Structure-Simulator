package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Main.GlobalAttributes;

/* Class which represents the Help window as viewed
 * by the user - JFrame derived
 */
public class HelpScreen extends JFrame{
    
	private static final long serialVersionUID = 1L;

	/* The main content panel */
    JPanel main= new JPanel();
    
    /* The area which displays the actual information */
    JTextArea text = new JTextArea();
    
    /* The scroll pane for the text are */
    JScrollPane textScrollPane = new JScrollPane(text);
    
	/* Global instance of this class to allow for convenient referencing
	 * throughout the program - only ever one instance so this is preferred
	 */
    static HelpScreen global;
    
    /* Class constructor - configures the window
     * and sets the appropriate text
     */
    public HelpScreen(){
    	
    	/* Calls the JFrame constructor */
        super("Help");
        
        /* Sets the global instance of this class to this instance */
        global=this;
        
        /* Set the window icon */
        this.setIconImage(GlobalAttributes.icon);
        
        /* Sets the layout manager to Border Layout */
        main.setLayout(new BorderLayout());
        
        /* Adds the text area */
        main.add(textScrollPane,BorderLayout.CENTER);
        
        /* Sets the appropriate text for the user to view */
        text.setText("Copyright 2012 Daniel Morrison\n\n");
        text.append("Enter a series of instructions separated by semi-colons (;).\n");
        text.append("To declare an event, simply enter a string e.g. \"x\".\n");
        text.append("To specify a precedes/enables relation, enter an instruction of the" +
        		" form \"x<y\", where x and y are events and x must occur before y.\n");
        text.append("To specify a conflict relation, enter an instruction of the form \"x#y\", where " +
        		"x and y are events and either one may occur during an execution sequence but " +
        		"not both (i.e. x NAND y = 1).\nEvents must be declared before they can be used in " +
        		"enables or conflict relations.\n");
        text.append("In Model mode, nodes can be dragged to new locations if their current " +
        		"location is inconvenient. \nNodes cannot be moved for the transition graph, as their locations are " +
        		"always optimal.\nTransition graph generation is not recommended for large definitions.\n\n" );
        text.append("In Circuit mode, the behaviour of the conflict resolution elements is as follows.\n\n");
        text.append("Standard:\tS0 = I1.O1.S1 + I2.O2.S2 \n\tS1 = I1.O1.S1 + I2.SBO2.S1 + SBI1.SBO1.S2\n");
        text.append("\tS2 = I2.O2.S2 + I1.SBO1.S2 + SBI2.SBO2.S1\n\nThis follows a first-come first-served policy ");
        text.append("with regards to selecting the winner of the conflict.\n\n");
        text.append("Synchronised:\tS0 = I1.(I2.(O1.PBO2.SBO2.S1 + O2.PBO1.SBO1.S2) + PBI2.O1.PBO2.S1) + \n");
        text.append("\t         I2.(I1.(O2.PBO1.SBO1.S2 + O1.PBO2.SBO2.S1) + PBI1.O2.PBO1.S2)\n");
        text.append("\tS1 = I1.O1.S1 + I2.SBO2.S1 + SBI1.SBO1.S2\n");
        text.append("\tS2 = I2.O2.S2 + I1.SBO1.S2 + SBI2.SBO2.S1\n\nThis is identical to the previous element, except ");
        text.append("that the initial winner is determined by giving both events an opportunity to compete via forward " +
        		"signals from previous instances of the element.");
        
        /* Configures the text area such that the user cannot modify */
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        
        /* Configures the overall window dimensions and visibility */
        this.setMinimumSize(new Dimension(550,500));
        this.setPreferredSize(new Dimension(550,500));
        this.setVisible(true);
        this.setContentPane(main);
        this.pack();
        
        /* Sets the close button to hide the window, rather than
         * destroy it */
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }    
}