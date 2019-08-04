package Circuit;

import java.awt.Color;
import java.awt.Graphics2D;

import GUI.GraphicsPanel;
import Main.GlobalAttributes;

/* This class represents the delay-insensitive wire - sending the kill
 * and unpause signals will cause it to skip over all execution statements and
 * then terminate */
class WireThread extends CircuitElement{

	/* Represents how far along the wire the signal is, this represents
	 * a percentage and is incremented by ten percent during each execution
	 * iteration */
	int travelCompletion=0;
	
	/* Indicates whether a signal is present at all */
	boolean active=false;

	/* Returns whether the wire is active or not */
	synchronized boolean isActive(){
		return active;
	}

	/* Returns whether the signal has reached the
	 * end of the wire or not */
	synchronized boolean hasArrived(){
		if(travelCompletion<100){
			return false;
		}
		else{
			return true;
		}
	}

	/* Sets whether a signal is present or not -
	 * this will also set its completion to zero
	 * percent
	 */
	synchronized void setActive(Boolean set){
		active=set;
		travelCompletion=0;
		if(render){
			GraphicsPanel.global.repaint();
		}
	}

	/* Execution logic */
	public void run(){
		
		/* Infinite loop until kill signal */
		while(!kill){
			
			/* If a signal is present */
			if(isActive() && !paused && !kill){

				/* Wait for a random amount of time between 0 and maxWait milliseconds */
				int wait=(int)(GlobalAttributes.random()*((float)GlobalAttributes.maxWait)/10);

				/* Loop 10 times (increment completion by 10 percent during each
				 * iteration) */
				for(int i=0;i<10;i++){

					/* Record the current system time */
					long time=System.currentTimeMillis();

					/* If the correct amount of time to wait hasn't been reached,
					 * and the thread has not been killed, then wait and yield the CPU
					 */
					while((System.currentTimeMillis()<(time+wait))&&!kill){

						/* Prolong the wait because execution has been paused */
						if(paused){
							time=System.currentTimeMillis();
						}
						Thread.yield();
					}

					/* Break out of the for-loop prematurely if the kill
					 * signal has been sent
					 */
					if(kill){
						break;
					}

					/* Increment completion by ten percent */
					travelCompletion+=10;
					if(render){
						GraphicsPanel.global.repaint();
					}

					while(paused && !kill){
						Thread.yield();
					}
				}
				
				/* Block until the signal is removed/reset */
				while((travelCompletion==100 || paused) && !kill){
					Thread.yield();
				}
			}
			else if(!kill){
				Thread.yield();
			}
		}
	}

	/* Render logic used when a standard straight-line is needed to
	 * represent the wire, requires only start and end points */
	void standardDraw(Graphics2D g2,int x1, int y1, int x2, int y2){
		
		/* Set colour depending on signal completion */
		setWireColor(g2);
		
		/* Set line thickness */
		g2.setStroke(GlobalAttributes.thickStroke);
		
		/* Draw the line */
		g2.drawLine(x1,y1,x2,y2);
	}

	/* Render logic used when a zig-zag line is needed to
	 * represent the wire, requires start and end points, as well as
	 * a distance before the zig-zag starts */
	void crossDraw(Graphics2D g2,int x1, int y1, int x2, int y2, int straight){
		
		/* Set colour depending on signal completion */
		setWireColor(g2);
		
		/* Set line thickness */
		g2.setStroke(GlobalAttributes.thickStroke);
		
		/* Draw the 3 lines */
		g2.drawLine(x1, y1, x1,y1-straight);
		g2.drawLine(x1, y1-straight,x2, y2+straight);
		g2.drawLine(x2, y2+straight,x2, y2);
	}

	/* Render logic used when the wire connects up to a merge element further
	 * down the screen (i.e. when block outputs connect to merge inputs). This
	 * requires start and end points, as well as a distance past the merge element
	 * for the wire to travel before bending back upwards
	 */
	void mergeInputDraw(Graphics2D g2,int x1, int y1, int x2, int y2, int excess){
		
		/* Set colour depending on signal completion */
		setWireColor(g2);
		
		/* Set line thickness */
		g2.setStroke(GlobalAttributes.thickStroke);
		
		/* Draw the 3 lines */
		g2.drawLine(x1,y1,x1,y2+excess);
		g2.drawLine(x1,y2+excess,x2,y2+excess);
		g2.drawLine(x2,y2+excess,x2,y2);
	}

	/* Set the colour depending on the signal completion */
	void setWireColor(Graphics2D g2){
		
		/* If the wire has a signal present, then calculate
		 * the colour based on the percentage - the wire will
		 * fade to red as the completion approaches 100%
		 */
		if(isActive()){
			g2.setColor(new Color(255,255/100*(100-travelCompletion),255/100*(100-travelCompletion)));
		}
		
		/* Else just set the wire to black if it is inactive */
		else{
			g2.setColor(Color.BLACK);
		}
	}
}