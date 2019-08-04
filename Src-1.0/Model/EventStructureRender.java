package Model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import Main.GlobalAttributes;

/* This class is responsible for the rendering logic of the causality
 * graph for Event Structures. It also generates on-screen coordinates
 * for each node */
public class EventStructureRender{

	/* The arrowhead object used for rendering the ends of lines */
	static Polygon arrowHead;
	
	/* The size of the largest available square area on screen */
	static int size;
	
	/* The array which stores graph node positions, two elements per event */
	public static int[] graphPositions=new int[0];
	
	/* Rendering logic */
	public static void draw(Graphics2D g2,int width, int height){
		
		/* Record the largest available square area */
		size=Math.min(width, height);
		
		/* Set the colour to black to draw connecting lines */
		g2.setColor(Color.BLACK);

		/* Basic line for causality */
		Stroke lineStroke=new BasicStroke(2.0f);
		
		/* Create the arrow-head for standard graph mode (size is different for transition
		 * graph mode */
		arrowHead = new Polygon();  
		arrowHead.addPoint( 0,0);
		arrowHead.addPoint( (int)-(((float)size)/60), (int)-(((float)size)/30));
		arrowHead.addPoint( (int)(((float)size)/60),(int)-(((float)size)/30)); 	

		/* Dotted line for conflict */
		Stroke thindashed = new BasicStroke(2.0f,BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 4.0f,8.0f},0.0f);

		/* For every pair of nodes */
		for(int i=0;i<graphPositions.length;i+=2){
			for(int j=0;j<EventStructure.events.length*2;j+=2){

				/* If the first event precedes the second */
				if(EventStructure.precedes[i/2][j/2]){

					/* Set the line stroke to standard */
					g2.setStroke(lineStroke);

					/* Call the function to draw the connecting line */
					drawLine(g2,(int)(((float)graphPositions[i]/100)*width), (int)(((float)graphPositions[i+1]/100)*height),
							(int)(((float)graphPositions[j]/100)*width), (int)(((float)graphPositions[j+1]/100)*height));
				}

				/* If the first event is in conflict with the second */
				if(EventStructure.conflicts[i/2][j/2]){

					/* Set the line stroke to dashed */
					g2.setStroke(thindashed);

					/* Call the function to draw the connecting line */
					drawLine(g2,(int)(((float)graphPositions[i]/100)*width), (int)(((float)graphPositions[i+1]/100)*height), 
							(int)(((float)graphPositions[j]/100)*width), (int)(((float)graphPositions[j+1]/100)*height));
				}
			}
		}

		/* Set the line stroke to a basic non-dashed line */
		g2.setStroke(new BasicStroke(1.0f));        

		/* Calculate font with which to draw the event names */
		Font font = new Font("Arial", Font.PLAIN, size/20);

		/* Set the font to render names with */
		g2.setFont(font);

		/* For every event to draw */
		for(int i=0;i<graphPositions.length;i+=2){

			/* If the event is completed, set the colour to red */
			if(EventStructure.completed[i/2]){
				g2.setColor(GlobalAttributes.completedColour);
			}

			/* If the event is allowed, but not completed, set the
			 * colour to white */
			else if(EventStructure.eventuallyAllowed[i/2]){
				g2.setColor(Color.WHITE);
			}

			/* If the event is locked out due to a conflict, set the
			 * colour to black */
			else{
				g2.setColor(Color.GRAY);
			}

			/* Draw the node with the coordinates as the centre point - 
			 * the node takes 10% of the available screen area in either direction,
			 * with 5% on either side of the coordinate */
			g2.fillOval((int)(((float)graphPositions[i]/100)*width)-size/20, 
					(int)(((float)graphPositions[i+1]/100)*height)-size/20, size/10, size/10);

			/* Now draw the node's outline at the same coordinates */
			g2.setColor(Color.BLACK);

			/* Calculate the width and height of the string to be drawn 
			 * - this allows us to centre the name over the node */
			int stringWidth=g2.getFontMetrics().stringWidth(EventStructure.events[i/2]);
			int stringHeight=g2.getFontMetrics().getHeight();

			/* Draws the string at the centre of the node */
			g2.drawString(EventStructure.events[i/2], ((int)(((float)graphPositions[i]/100)*width))-stringWidth/2, 
					(int)(((float)graphPositions[i+1]/100)*height)+stringHeight/4);

			/* Draws the node outline */
			g2.drawOval((int)(((float)graphPositions[i]/100)*width)-size/20, 
					(int)(((float)graphPositions[i+1]/100)*height)-size/20, size/10, size/10);
		}    
	}
	
	/* Draws a line from the node with center x1,y1 to the node with center x2,y2 
	 * - draws from edge-to-edge of the target nodes */
	static void drawLine(Graphics2D g, int x1, int y1, int x2, int y2) {

		/* The difference in X coordinates */
		double lengthX=Math.abs(x2-x1);

		/* The difference in Y coordinates */
		double lengthY=Math.abs(y2-y1);

		/* The length of the connecting line between the events */
		double lengthSlope = Math.sqrt(Math.pow(lengthX,2)+Math.pow(lengthY,2));

		/* The angle between the slope and the X-axis */
		double angle=Math.asin(lengthY/lengthSlope);

		/* The new length of the line after subtracting 2*radius of the ovals */
		double newLength=lengthSlope-(size/10);

		/* The new X-length (newX = cos(angle)*newLength) */
		double newXLength=Math.cos(angle)*newLength;

		/* The new Y-length (newY = sin(angle)*newLength) */
		double newYLength=Math.sin(angle)*newLength;

		/* The change in X (old length - new length) */
		double changeX=lengthX-newXLength;

		/* The change in Y (old length-new length) */
		double changeY=lengthY-newYLength;

		/* Create an empty transform for storing the final arrow-head coordinates */
		AffineTransform tx = new AffineTransform();

		/* Draws the actual line - this takes into account oval edges and calculates 
		 * new start/end values accordingly, these vary depending on which way the 
		 * line is "pointing" (hence the comparison operators) - this also translates
		 * the arrow-head to the final coordinates (which is also affected by direction),
		 * hence it is paired up with a corresponding drawLine instruction */
		if(x2>x1 && y2>y1){
			g.drawLine(x1+((int)(changeX/2)),y1+((int) (changeY/2)),x2-((int)(changeX/2)),y2-((int)(changeY/2)));
			tx.translate(x2-((int)(changeX/2)),y2-((int)(changeY/2)));
		}
		else if(x2>x1 && y2==y1){
			g.drawLine(x1+((int)(changeX/2)),y1+((int) (changeY/2)),x2-((int)(changeX/2)),y2);
			tx.translate(x2-((int)(changeX/2)),y2);
		}
		else if(x2>x1 && y2<y1){
			g.drawLine(x1+((int)(changeX/2)),y1-((int) (changeY/2)),x2-((int)(changeX/2)),y2+((int)(changeY/2)));
			tx.translate(x2-((int)(changeX/2)),y2+((int)(changeY/2)));
		}
		else if(x2<x1 && y2>y1){
			g.drawLine(x1-((int)(changeX/2)),y1+((int) (changeY/2)),x2+((int)(changeX/2)),y2-((int)(changeY/2)));
			tx.translate(x2+((int)(changeX/2)),y2-((int)(changeY/2)));
		}
		else if(x2<x1 && y2==y1){
			g.drawLine(x1-((int)(changeX/2)),y1+((int) (changeY/2)),x2+((int)(changeX/2)),y2);
			tx.translate(x2+((int)(changeX/2)),y2);
		}
		else if(x2==x1 && y2>y1){
			g.drawLine(x1+((int)(changeX/2)),y1+((int) (changeY/2)),x2,y2-((int)(changeY/2)));
			tx.translate(x2,y2-((int)(changeY/2)));
		}
		else if(x2==x1 && y2<y1){
			g.drawLine(x1+((int)(changeX/2)),y1-((int) (changeY/2)),x2,y2+((int)(changeY/2)));
			tx.translate(x2,y2+((int)(changeY/2)));
		}
		else if(x2<x1 && y2<y1){
			g.drawLine(x1-((int)(changeX/2)),y1-((int) (changeY/2)),x2+((int)(changeX/2)),y2+((int)(changeY/2)));
			tx.translate(x2+((int)(changeX/2)),y2+((int)(changeY/2)));
		}

		/* Calculate the angle from which to rotate the arrow-head */
		double angle2 = Math.atan2(y2-y1, x2-x1);

		/* Rotate the arrow-head at its new location */
		tx.rotate((angle2-Math.PI/2d));  

		/* Return the transformed arrow-head's coordinates after
		 * translating and rotating it */
		Shape arrowHeadShape=tx.createTransformedShape(arrowHead);  

		/* Fill in the arrow-head */
		g.fill(arrowHeadShape);
	}
	
	/* Calculates on-screen coordinates for all nodes */
	public static void calculatePositions(){
		
		/* Create array for storing object locations */
		graphPositions=new int[EventStructure.events.length*2];

		/* Default the location to 0,0*/
		int locationX=0;
		int locationY=0;

		/* Boolean to stop retrying for location calculation -
		 * prevents hanging if number of events grows too high
		 */
		boolean allowRetry=true;

		/* Randomly allocates locations for the events - prevents overlap */
		for(int i=0;i<EventStructure.events.length;i++){

			/* Determines whether we need to retry the calculation for this node */
			boolean retry=false;

			/* The number of times that this particular node has had it's location
			 * blocked due to close proximity of another node */
			int retryCount=0;

			do {

				/* If this node has had 10 attempts at finding a location, then give
				 * up for this and all subsequent nodes */
				if(retryCount==10){
					allowRetry=false;
				}

				/* Increment the number of attempts */
				retryCount++;

				/* Assume that we do not need to retry */
				retry=false;

				/* Calculate some coordinates */
				locationX=(int)(GlobalAttributes.random.nextFloat()*100);
				locationY=(int)(GlobalAttributes.random.nextFloat()*100);

				/* Only if we have given up on checking against existing nodes (stops
				 * exponential increase in waiting time or permanent looping) */
				if(allowRetry){

					/* Check against all nodes that have been assigned so far */
					for(int j=0;j<(i+1)*2;j+=2){

						/* If we are within 10 percent of another node in either axis, or within
						 * 5 percent of the edge of the edge of the area */
						if((locationX<(graphPositions[j]+10) && locationX>(graphPositions[j]-10) &&
								locationY<(graphPositions[j+1]+10) && locationY>(graphPositions[j+1]-10)) ||
								locationX<5 || locationX>95 || locationY<5 || locationY>95){

							/* Indicate that we should try again for this particular node */
							retry=true;
							break;
						}
					}
				}

			/* Retry again if failed to find a location */
			}while(retry);

			/* Store the location of the object */
			graphPositions[2*i]=locationX;
			graphPositions[2*i+1]=locationY;
		}	
	}

	/* Attempts to move the given node to the new coordinate */
	public static void updatePosition(int graphSelected, int i, int j) {
		
		/* Calculate new coordinates for the selected event */
		graphPositions[graphSelected*2]=i;
		graphPositions[graphSelected*2+1]=j;

		/* Move the event back onto the display area if it has been
		 * dragged too far */
		if(graphPositions[graphSelected*2]<5){
			graphPositions[graphSelected*2]=5;
		}
		else if(graphPositions[graphSelected*2]>95){
			graphPositions[graphSelected*2]=95;
		}
		if(graphPositions[graphSelected*2+1]<5){
			graphPositions[graphSelected*2+1]=5;
		}
		else if(graphPositions[graphSelected*2+1]>95){
			graphPositions[graphSelected*2+1]=95;
		}
	}
	
}
