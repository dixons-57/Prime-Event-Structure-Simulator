package Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import Main.GlobalAttributes;

/* This class is responsible for the rendering logic of the transition
 * graph for Event Structures. It also generates on-screen coordinates
 * for each node */
public class TransitionGraphRender {

	/* The arrowhead object used for rendering the ends of lines */
	static Polygon arrowHead;
	
	/* The size of the largest available square area on screen */
	static int size;
	
	/* The number of nodes on the widest level of the transition graph
	 * (if one exists) */
	static int widestLevel=0;
	
	/* The array which stores transition node positions, two elements per node */
	public static int[] transitionPositions=new int[0];
	
	/* Rendering logic */
	public static void draw(Graphics2D g2, int width, int height){

		/* Default font with which to draw the node data */
		Font font = new Font("Arial", Font.PLAIN, 14);

		/* Set the font */
		g2.setFont(font);

		/* The maximum width of combined string data
		 * for any level of the tree */
		int maxStringWidth=width;

		/* Calculate the widest possible string that can be created
		 * by appending all node data on a single level of the tree */
		StringBuffer widestString=new StringBuffer();
		for(int i=0;i<=TransitionGraph.noAtLevel.get(widestLevel);i++){
			for(int j=0;j<widestLevel;j++){
				widestString.append("@@");
			}
			widestString.append("@");
		}

		/* Calculate the width of this string in pixels at the current font */
		int currentWidth=g2.getFontMetrics().stringWidth(widestString.toString());

		/* Calculate the ratio with which to scale the font in order to achieve
		 * optimal width */
		float ratio=(float)maxStringWidth/(float)currentWidth;

		/* Calculate the new font size using this ratio */
		float newFontSize=(float)14 * ratio;

		/* Apply this new font size */
		font=new Font("Arial",Font.PLAIN,(int)newFontSize);
		g2.setFont(font);

		/* Create the arrow-head for transition graph mode (size is different for transition
		 * graph mode */
		arrowHead = new Polygon();  
		arrowHead.addPoint( 0,0);
		arrowHead.addPoint( (int)-(((float)size)/120), (int)-(((float)size)/60));
		arrowHead.addPoint( (int)(((float)size)/120),(int)-(((float)size)/60));

		/* For every transition node */
		for(int i=0;i<transitionPositions.length;i=i+2){

			/* Store the width and height of the string for the node */
			float stringWidth=g2.getFontMetrics().stringWidth(TransitionGraph.nodes.get(i/2).nodeData());
			float stringHeight=g2.getFontMetrics().getHeight();

			/* Calculate the onscreen coordinates of the center of the node and string */
			float centerX=(int)(((float)transitionPositions[i]/100)*width);
			float centerY=(int)(((float)transitionPositions[i+1]/100)*height);

			/* Set the render colour to black */
			g2.setColor(Color.BLACK);

			/* For every outgoing transition for the current node */
			for(int j=0; j<TransitionGraph.nodes.get(i/2).targetNodes.size();j++){

				/* Calculate the end coordinates of the connecting line (the top of the target node) */
				int endX=(int)(((float)transitionPositions[TransitionGraph.nodes.get(i/2).targetNodes.get(j)*2]/100)*width);
				int endY=(int)(((float)transitionPositions[TransitionGraph.nodes.get(i/2).targetNodes.get(j)*2+1]/100)*height-stringHeight/2);
				g2.drawLine((int)centerX,(int)(centerY+stringHeight/2),endX,endY);

				/* Create an empty transform for storing the final arrow-head coordinates */
				AffineTransform tx = new AffineTransform();

				/* Translate the arrow head to the end of the line */
				tx.translate(endX,endY);

				/* Calculate the angle from which to rotate the arrow-head */
				double angle2 = Math.atan2(endY-centerY+stringHeight/2, endX-centerX);

				/* Rotate the arrow-head at its new location */
				tx.rotate((angle2-Math.PI/2d));  

				/* Return the transformed arrow-head's coordinates after
				 * translating and rotating it */
				Shape arrowHeadShape=tx.createTransformedShape(arrowHead);  

				/* Fill in the arrow-head */
				g2.fill(arrowHeadShape);

			}

			/* Assume that the current transition node isn't part of the
			 * execution trace */
			boolean completed=false;

			/* Search the list of nodes involved in the execution trace
			 * for the current node */
			for(int j=0;j<TransitionGraph.executionTrace.size();j++){
				if(TransitionGraph.executionTrace.get(j).intValue()==
					TransitionGraph.nodes.get(i/2).id){
					completed=true;
					break;
				}
			}

			/* If the node is part of the trace, set the colour to red */
			if(completed==true){
				g2.setColor(GlobalAttributes.completedColour);
			}

			/* Otherwise, set the colour to white */
			else{
				g2.setColor(Color.WHITE);
			}

			/* Fill the node at the appropriate location */
			g2.fillRect((int) (centerX-stringWidth/2),(int) (centerY-stringHeight/2),(int) stringWidth,(int) stringHeight);

			/* Then set the colour to black and draw the node outline */
			g2.setColor(Color.BLACK);
			g2.drawRect((int) (centerX-stringWidth/2),(int) (centerY-stringHeight/2),(int) stringWidth,(int) stringHeight);

			/* Finally, render the string over the top */
			g2.drawString(TransitionGraph.nodes.get(i/2).nodeData(), centerX-((float)stringWidth)/(2.0f),
					centerY+((float)stringHeight)/(2.0f)-2*stringHeight/7);
		}
	}
	
	/* Calculates on-screen coordinates for all nodes */
	public static void calculatePositions(){

		/* Stores the positions of all transition nodes - two cells per
		 * coordinate (x and y) */
		transitionPositions=new int[TransitionGraph.nodes.size()*2];

		/* The number of node positions calculated so far in the loop
		 * for each level in the graph */
		int[] levelsSoFar=new int[TransitionGraph.noAtLevel.size()];

		/* For every node in the graph */
		for(int i=0;i<TransitionGraph.nodes.size();i++){

			/* Calculate the level in which the node appears (depth) */
			int level=TransitionGraph.nodes.get(i).completedEvents.size();

			/* Increment the number of node positions calculated so far at this level */
			levelsSoFar[level-1]++;

			/* Calculate the x and y positions for the node */
			transitionPositions[2*i]=100/TransitionGraph.noAtLevel.get(level-1)*levelsSoFar[level-1]-100/TransitionGraph.noAtLevel.get(level-1)/2;
			transitionPositions[2*i+1]=100/TransitionGraph.noAtLevel.size()*level-100/TransitionGraph.noAtLevel.size()/2;
		}

		/* The maximum number of characters that appear on a particular level 
		 * (used to calculate the widest level of the graph */
		int maxHorizontalChars=0;

		/* For every level in the tree */
		for(int i=0;i<TransitionGraph.noAtLevel.size();i++){

			/* If the number of characters is larger than the stored maximum, then
			 * replace it for subsequent iterations' comparisons */
			if(TransitionGraph.noAtLevel.get(i)*(2*i+1)>maxHorizontalChars){
				maxHorizontalChars=TransitionGraph.noAtLevel.get(i)*(i+1);
				widestLevel=i;
			}
		}	
	}
	
}
