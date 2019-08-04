package Circuit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import GUI.GraphicsPanel;
import Main.GlobalAttributes;

/* This class represents the delay-insensitive Fork element - sending the kill
 * and unpause signals will cause it to skip over all execution statements and
 * then terminate */
class ForkThread extends CircuitElement{

	/* The element has 1 input and 2 outputs */
	WireThread input;
	WireThread output1;
	WireThread output2;
	
	/* Indicates whether the element is processing */
	boolean processing=false;

	/* Execution logic */
	@Override
	public void run(){

		/* Infinite loop until kill signal */
		while(!kill){

			/* Wait until input arrives */
			while((!input.hasArrived() || paused) && !kill){
				Thread.yield();
			}
			
			if(!kill){
				
				/* Lower the signal on the input wire and set
				 * the internal state to processing */
				input.setActive(false);
				processing=true;
				if(render){
					GraphicsPanel.global.repaint();
				}
				
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

			/* Set the output wires to be active and the internal
			 * state back to normal */
			/* Wait until an output wire is available */
			while(((output1.isActive() && output2.isActive()) || paused) && !kill){
				Thread.yield();
			}
			
			if(!kill){
				
				/* Set the output wire to active, then wait
				 * for the other wire to become active and
				 * set it */
				if(!output1.isActive()){
					output1.setActive(true);
					if(render){
						GraphicsPanel.global.repaint();
					}
					while((output2.isActive() || paused) && !kill){
						Thread.yield();
					}
					if(!kill){
						output2.setActive(true);
					}
				}
				else if(!output2.isActive()){
					output2.setActive(true);
					if(render){
						GraphicsPanel.global.repaint();
					}
					while((output1.isActive() || paused)&&!kill){
						Thread.yield();
					}
					if(!kill){
						output1.setActive(true);
					}
				}
				if(!kill){
					processing=false;
					if(render){
						GraphicsPanel.global.repaint();
					}
				}
			}
		}
	}

	/* Render logic for the element, given the location
	 * and size of the element */
	void drawFork(Graphics2D g2, int x, int y, int size){
		
		/* Set the fill colour depending on whether
		 * the element is processing or not
		 */
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
		int stringWidth=g2.getFontMetrics().stringWidth("F");
		g2.drawString("F", x+size/2-stringWidth/2, 
				y+size-size/6);
		
		/* Draw the connection points */
		g2.fillOval(x+size/2-size/10, y+size-size/10, size/5, size/5);		
		g2.fillOval(x+size/3-size/10, y-size/10, size/5, size/5);
		g2.fillOval(x+2*size/3-size/10, y-size/10, size/5, size/5);
	}
}