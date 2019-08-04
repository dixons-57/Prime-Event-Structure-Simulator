package Circuit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import GUI.GraphicsPanel;
import Main.GlobalAttributes;

/* This class represents the delay-insensitive Join element - sending the kill
 * and unpause signals will cause it to skip over all execution statements and
 * then terminate */
class JoinThread extends CircuitElement{

	/* The element has 2 inputs and 1 output */
	WireThread input1;
	WireThread input2;
	WireThread output;

	/* Indicates whether the element is processing */
	boolean processing=false;

	/* Execution logic */
	@Override
	public void run(){

		/* Infinite loop until kill signal*/
		while(!kill){

			/* Wait until an input arrivea */
			while(((!input1.hasArrived()&&!input2.hasArrived()) || paused) &&!kill){
				Thread.yield();
			}
			
			if(!kill){
				
				/* Lower the signals on the input wire and wait
				 * for the other input, then lower it*/
				if(input1.hasArrived()){
					input1.setActive(false);
					processing=true;
					if(render){
						GraphicsPanel.global.repaint();
					}
					while((!input2.hasArrived() || paused) && !kill){
						Thread.yield();
					}
					if(!kill){
						input2.setActive(false);
					}
				}
				else if(input2.hasArrived()){
					input2.setActive(false);
					processing=true;
					if(render){
						GraphicsPanel.global.repaint();
					}
					while((!input1.hasArrived() || paused) && !kill){
						Thread.yield();
					}
					if(!kill){
						input1.setActive(false);
					}
				}
				if(!kill){
					
					/* Wait for a random amount of time between 0 and maxWait milliseconds */
					int wait=(int)(GlobalAttributes.random()*((float)GlobalAttributes.maxWait));
	
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
				}
			}

			/* Wait until the output wire is available */
			while((output.isActive() || paused) &&!kill){
				Thread.yield();
			}

			if(!kill){

				/* Set the output wire to be active and the internal
				 * state back to normal */
				output.setActive(true);
				processing=false;
				if(render){
					GraphicsPanel.global.repaint();
				}
			}
		}
	}

	/* Render logic for the element, given the location
	 * and size of the element */
	void drawJoin(Graphics2D g2, int x, int y, int size){
		
		/* Set the fill colour depending on whether
		 * the element is processing or not */
		if(this.processing){
			g2.setColor(GlobalAttributes.processingColor);
		}else{
			g2.setColor(Color.WHITE);
		}	
		
		/* Draw the outline and fill */
		g2.setStroke(new BasicStroke(1.0f));
		g2.fillRect(x, y, size,size);
		g2.setColor(Color.BLACK);
		g2.drawRect(x, y, size,size);
		
		/* Draw the label */
		g2.setFont(GlobalAttributes.middleFont);
		int stringWidth=g2.getFontMetrics().stringWidth("J");
		g2.drawString("J", x+size/2-stringWidth/2, 
				y+size-size/6);
		
		/* Draw the connection points */
		g2.fillOval(x+size/2-size/10, y-size/10, size/5, size/5);		
		g2.fillOval(x+size/3-size/10, y+size-size/10, size/5, size/5);
		g2.fillOval(x+2*size/3-size/10, y+size-size/10, size/5, size/5);
	}
}