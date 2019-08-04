package Circuit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import GUI.GraphicsPanel;
import Main.GlobalAttributes;

/* This class represents the delay-insensitive Merge element - sending the kill
 * and unpause signals will cause it to skip over all execution statements and
 * then terminate */
class MergeThread extends CircuitElement{

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

			/* Only one input needs to be "connected up" for this
			 * element to work, so we need to rule out possibilities
			 * where one of these wires is null (rather than just inactive)
			 */
			
			/* If both wires are connected */
			if(input1!=null && input2!=null){
				
				/* Wait until an input arrives */
				while(((!input1.hasArrived()&&!input2.hasArrived()) || paused) &&!kill){
					Thread.yield();
				}
			}
			
			/* If only input 1 is connected, then wait until it arrives */
			else if(input1!=null){
				while((!input1.hasArrived() || paused) &&!kill){
					Thread.yield();
				}
			}
			
			/* If only input 2 is connected, then wait until it arrives */
			else{
				while((!input2.hasArrived() || paused) &&!kill){
					Thread.yield();
				}
			}

			if(!kill){
				
				/* Remembers which input we have chosen to process
				 * (as we may have to non-deterministically select
				 * one if both are available)
				 */
				int pick=0;
				
				/* If both inputs are connected */
				if(input1!=null && input2!=null){
					
					/* If both have arrived, then arbitrarily select
					 * one of the two and record it
					 */
					if(input1.hasArrived() && input2.hasArrived()){
						if(GlobalAttributes.random()<0.50f){
							pick=1;
						}
						else{
							pick=2;
						}
					}
					
					/* If only input 1 has arrived then select it
					 * by default */
					else if(input1.hasArrived()){
		
						pick=1;
					}
					
					/* If only input 2 has arrived then select it
					 * by default */
					else if(input2.hasArrived()){
						pick=2;
					}
				}
				
				/* If only input 1 is connected, then select
				 * it by default if it has arrived
				 */
				else if(input1!=null){
					if(input1.hasArrived()){
						pick=1;
					}
				}
				
				/* If only input 2 is connected, then select
				 * it by default if it has arrived
				 */
				else if(input2!=null){
					if(input2.hasArrived()){
						
						pick=2;
					}
				}

				/* If input 1 has been selected, then acknowledge it */
				if(pick==1){
					input1.setActive(false);
				}
				
				/* If input 2 has been selected, then acknowledge it */
				else if(pick==2){
					input2.setActive(false);
				}
	
				/* Set the internal state to processing */
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
			
			/* Wait until the output wire is available */
			while((output.isActive() || paused) && !kill){
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
	void drawMerge(Graphics2D g2, int x, int y, int size){
		
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
		int stringWidth=g2.getFontMetrics().stringWidth("M");
		g2.drawString("M", x+size/2-stringWidth/2, 
				y+size-size/6);
		
		/* Draw the connection points */
		g2.fillOval(x+size/2-size/10, y-size/10, size/5, size/5);		
		g2.fillOval(x+size/3-size/10, y+size-size/10, size/5, size/5);
		g2.fillOval(x+2*size/3-size/10, y+size-size/10, size/5, size/5);
	}
}