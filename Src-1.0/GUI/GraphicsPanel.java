package GUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Circuit.Circuit;
import Circuit.CircuitExamplesRender;
import Main.GlobalAttributes;
import Model.EventStructure;
import Model.EventStructureRender;
import Model.TransitionGraph;
import Model.TransitionGraphRender;

/* JPanel-derived class, used for the graphical representation of the
 * simulator. Rendering logic is delegated to the relevant subsystem.
 * This class also contains logic for dragging nodes when viewing
 * the Event Structure */
public class GraphicsPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	/* Global instance of this class to allow for convenient referencing
	 * throughout the program - only ever one instance so this is preferred */
	public static GraphicsPanel global;	

	/* The actual width of the panel */
	int width;

	/* The actual height of the panel */
	int height;

	/* The internal integer ID of the graph node that is being dragged */
	int graphSelected=-1;
	
	/* The size of the largest available square area on screen */
	int size;

	/* Constructor for setting up class */
	public GraphicsPanel(){

		/* Calls JPanel constructor */
		super();

		/* Sets the global instance of this class to this instance */
		global=this;

		/* Finalises panel options */
		this.setVisible(true);
		this.setBackground(GlobalAttributes.graphicsBackground);
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		/* Adds mouse event listener - handled locally by this class */
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/* Overridden paint method */
	public void paint(Graphics g){

		try{
			
			/* Java2D handle */
			Graphics2D g2=(Graphics2D)g;

			/* Store the window's width and height */
			width=this.getWidth();
			height=this.getHeight();
			size=Math.min(width, height);
			
			/* Clear the screen with a grey area */
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(0, 0, width, height);

			/* If render mode is causality graph and one has been loaded, then delegate
			 * rendering to the dedicated circuit rendering class, while passing
			 * the graphics handle */
			if(GlobalAttributes.renderMode==1 && EventStructure.events.length>0){
				EventStructureRender.draw(g2,width,height);
			}

			/* If render mode is transition graph and one has been loaded, then delegate
			 * rendering to the dedicated transition graph rendering class, while passing
			 * the graphics handle */
			else if(GlobalAttributes.renderMode==2 && TransitionGraph.nodes.size()>0){
				TransitionGraphRender.draw(g2, width, height);
			}
			
			/* If render mode is circuit and a circuit has been loaded, then delegate
			 * rendering to the dedicated circuit rendering class, while passing
			 * the graphics handle */
			else if(GlobalAttributes.renderMode==3 && Circuit.circuitElements.length>0){
				CircuitExamplesRender.draw(g2,size);
			}
		}
		catch(Exception e){
			/* Do nothing if an exception occurs when trying to paint. This may occur spontaneously
			 * when trying to paint a component (the time taken to paint is subject to the thread scheduler)
			 * which no longer exists (has been garbage collected etc). This prevents unavoidable exceptions
			 * being output to the console */			
		}
	}

	/* Calculate the positions of all objects to be drawn (on a scale of 0-100 for
	 * each axis) - called upon each initialisation of the event structure */
	public void calculatePositions(){

		/* If a transition graph is not available */
		if(!GlobalAttributes.transitions){
			
			/* Clear unneeded transition graph data */
			TransitionGraph.clearGraph();
		}
		
		/* Calculate all positions of the nodes */
		EventStructureRender.calculatePositions();

		/* Otherwise, if a transition graph is available, calculate positions
		 * similarly for the nodes for that */
		if(GlobalAttributes.transitions){
			TransitionGraphRender.calculatePositions();
		}
	}

	/* Moves the event which is being clicked on when the mouse
	 * is dragged (if any) */
	@Override
	public void mouseDragged(MouseEvent arg0) {

		/* If an event is selected */
		if(graphSelected!=-1){

			/* Update the position of the currently selected event to
			 * the new coordinates */
			EventStructureRender.updatePosition(graphSelected,
					(int)(((float)arg0.getX())/width*100),(int)(((float)arg0.getY())/height*100));		
			
			/* Forces a repaint of the display area */
			this.repaint();
		}
	}

	/* Required method - unused */
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	/* Required method - unused */
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	/* Required method - unused */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	/* Required method - unused */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/* Flags an event as being selected if the mouse
	 * is over it when the LMB is clicked */
	@Override
	public void mousePressed(MouseEvent arg0) {

		/* Retrieve the cursor coordinates relative
		 * to this JPanel */
		int x=arg0.getX();
		int y=arg0.getY();

		/* Selection logic for standard graph mode */
		if(GlobalAttributes.renderMode==1){

			/* Iterate through all events */
			for(int i=EventStructure.events.length-1;i>=0;i--){

				/* Find the position of the event */
				int y2=EventStructureRender.graphPositions[i*2+1]*height/100;
				int x2=EventStructureRender.graphPositions[i*2]*width/100;

				/* Calculate the distance from the origin of the event
				 * to the location of the cursor */
				double dist=Math.sqrt(Math.pow(y2-y,2)+Math.pow(x2-x,2));

				/* If the distance is smaller than the radius of the event
				 * (i.e. if we are within a node's circle */
				if(dist<size/20){

					/* Flag the event as selected and break
					 * out of the loop */
					graphSelected=i;
					break;
				}
			}
		}
	}

	/* De-selects the event which was previously being dragged */
	@Override
	public void mouseReleased(MouseEvent arg0) {

		/* If an event is selected */
		if(graphSelected!=-1){

			/* Set the variable to the dummy value */
			graphSelected=-1;
		}
	}

}
