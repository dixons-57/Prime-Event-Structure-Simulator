package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import Main.GlobalAttributes;

/* Class which represents the console output of the program -
 * this actually exists as a separate frame so that it can
 * be easily viewed in a GUI environment and hidden/shown
 * whenever the user wishes */
public class OutputFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/* The main content panel of the frame */
	JPanel main= new JPanel();
	
	/* The console output text area */
	JTextArea outputArea=new JTextArea();
	JScrollPane outputScrollPane=new JScrollPane(outputArea);
	
	/* Global instance of this class to allow for convenient referencing
	 * throughout the program - only ever one instance so this is preferred
	 */
	public static OutputFrame global;
	
	/* Constructor for the class - configures the overall window as well as the
	 * sub-components */
    public OutputFrame(){
    	
    	/* Call the JFrame constructor */
        super("Console");

        /* Sets the global instance of this class to this instance */
        global=this;
        
        /* Configure the main content panel and add the output area */
        main.setLayout(new BorderLayout());
        main.add(outputScrollPane,BorderLayout.CENTER);
        
        /* Configure the output area */
        outputArea.setEditable(false); 
        outputArea.setLineWrap(true);
        outputArea.setBackground(GlobalAttributes.outputBackground);
        outputArea.setForeground(Color.WHITE);
        
        /* Set the output area to automatically scroll when adding
         * text */
        DefaultCaret caret = (DefaultCaret)outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        /* Configure the overall window */
        this.setVisible(true);
        this.setBackground(GlobalAttributes.outputBackground);
        this.setMinimumSize(new Dimension(400,200));
        this.setPreferredSize(new Dimension(400,200));
        this.setContentPane(main);
        this.pack();
        
        /* Set the window to hide when the close button is clicked */
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    
    /* Outputs the given string to the console - synchronised for safety but this is
     * probably not required */
    public synchronized void write(String out){
    	
    	/* Adds a new line to prevent multiple messages occurring on the same line */
    	outputArea.append(out+"\n");
    }
    
}
