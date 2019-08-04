package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Main.GlobalAttributes;

/* Class for rendering the main program window - closing this frame
 * will end the program */
public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	
	/* JPanels that comprise the main window */
    JPanel main;
    GraphicsPanel graphics; 
    ControlPanel controls;
    
	/* Global instance of this class to allow for convenient referencing
	 * throughout the program - only ever one instance so this is preferred
	 */
    public static MainWindow global;
    
    /* Window constructor */
    public MainWindow(){
        
        /* Calls JFrame constructor */
        super(GlobalAttributes.title);
        
        /* Sets the global instance of this class to this instance */
        global=this;
        
        /* Set up the main content panel for the window */
        main=new JPanel();
        main.setVisible(true);
        main.setPreferredSize(GlobalAttributes.initialSize);
        main.setBackground(GlobalAttributes.background);
        main.setLayout(new BorderLayout());
        
        /* Set up controls pane - input and buttons */
        controls = new ControlPanel();
        controls.setPreferredSize(new Dimension(300,400));
        main.add(controls,BorderLayout.LINE_START);
        
        /* Set up graphics pane - render target for the visualisation */
        graphics = new GraphicsPanel();
        main.add(graphics,BorderLayout.CENTER);
        
        /* Finalise frame properties and display */
        this.setMinimumSize(GlobalAttributes.initialSize);
        this.setContentPane(main);     
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        
        /* Instantiate the console output window */
        new OutputFrame();
        
        /* Load the icon image into memory and store the data statically so that it
         * can be accessed by all frames */
		try {
			GlobalAttributes.icon = ImageIO.read(this.getClass().getResource("icon.jpg"));  
			global.setIconImage(GlobalAttributes.icon);
			OutputFrame.global.setIconImage(GlobalAttributes.icon);
	    } catch (IOException e) {
	    }
    }    

}
