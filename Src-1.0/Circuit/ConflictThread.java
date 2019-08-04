package Circuit;

import java.awt.Color;
import java.awt.Graphics2D;

import GUI.GraphicsPanel;
import Main.GlobalAttributes;

/* This class represents the delay-insensitive UNsynchronised conflict-resolution 
 * element. ConflictSynchThread is a subclass of this, and shares all of this classes
 * attributes, as well as all methods except for run(), of which it has its own
 * override - sending the kill and unpause signals will cause it to 
 * skip over all execution statements and then terminate 
 * 
 * For CCS semantics of this element's behaviour, see the Help window in the program */
class ConflictThread extends CircuitElement{

	/* The main input lines */
	WireThread I1;
	WireThread I2;
	
	/* The subsequent blocked input lines - SBI 1 and 2 */
	WireThread SBI1;
	WireThread SBI2;
	
	/* The main output lines */
	WireThread O1;
	WireThread O2;
	
	/* The subsequent blocked output lines - SBO 1 and 2 */
	WireThread SBO1;
	WireThread SBO2;
	
	/* The internal state of the element */
	int state=0;

	/* Indicates whether the element is processing */
	boolean processing=false;
	
	/* The name of the element - represents which two events
	 * it is solving the conflict for */
	String name;

	/* Constructor */
	ConflictThread(String name){
		this.name=name;
	}

	/* Execution logic */
	@Override
	public void run(){

		/* Infinite loop until kill signal */
		while(!kill){

			/* If in the initial state */
			if(state==0){

				/* Wait until a standard input arrives */
				while(((!I1.hasArrived() && !I2.hasArrived()) || paused) &&!kill){
					Thread.yield();
				}

				if(!kill){
					
					/* Records whether I1 or I2 is processed */
					int pick=0;

					/* If both inputs have arrived then
					 * arbitrarily pick one and record the
					 * selection */
					if(I1.hasArrived()&&I2.hasArrived()){
						if(GlobalAttributes.random()<0.50f){
							pick=1;
						}
						else{
							pick=2;
						}
					}
					
					/* If only I1 has arrived, then pick it by default */
					else if(I1.hasArrived()){
						pick=1;
					}
					
					/* If only I2 has arrived, then pick it by default */
					else if(I2.hasArrived()){
						pick=2;
					}

					/* If the first input was selected */
					if(pick==1){

						/* Lower the signal on I1 */
						I1.setActive(false);

						/* Wait to simulate process time */
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
						
						/* Wait until O1 is available */
						while((O1.isActive() || paused) && !kill){
							Thread.yield();
						}

						/* Set output 1 to active and the internal state to 1 */
						if(!kill){
							O1.setActive(true);
							state=1;
						}
					}
					
					/* If the second input was selected */
					else if(pick==2){

						/* Lower the signal on I2 */
						I2.setActive(false);

						/* Wait to simulate process time */
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
						
						/* Wait until O2 is available */
						while((O2.isActive() || paused) && !kill){
							Thread.yield();
						}
						
						/* Set output 2 to active and the internal state to 2 */
						if(!kill){
							O2.setActive(true);
							state=2;
						}
					}
				}
			}
			
			/* If the internal state is 1 or 2, then defer to the dedicated method -
			 * this is so that the subclass ConflictSynchThread can share the
			 * logic */
			else if(state==1 || state==2){
				stateOneAndTwo();
			}
			if(!kill){
				
				/* Set the processing state back to normal */
				processing=false;
				if(render){
					GraphicsPanel.global.repaint();
				}
			}
		}
	}

	/* Execution logic for when the element has an internal state
	 * of 1 or 2 */
	void stateOneAndTwo(){
		
		/* If the internal state is 1 */
		if(state==1){
			
			/* If the SBI1 line is connected */
			if(SBI1!=null){
				
				/* Wait until a valid input I1, I2, or SBI1 arrives */
				while(((!I1.hasArrived() && !I2.hasArrived() &&
						!SBI1.hasArrived()) || paused) && !kill){
					Thread.yield();
				}
			}
			
			/* Else just wait until either I1 or I2 arrives */
			else{
				while(((!I1.hasArrived() && !I2.hasArrived())
							|| paused) && !kill){
					Thread.yield();
				}
			}
			if(!kill){
				
				/* Records whether I1, I2 or SBI1 is processed */
				int pick=0;

				/* Pick a random number between 0 and 1 in order
				 * to allow arbitrary selection */
				float select=GlobalAttributes.random();

				/* If SBI1 is connected, and all 3 inputs have arrived, then
				 * decide between the 3 depending on which third the random
				 * number lies between, and record the selection */
				if(I1.hasArrived() &&I2.hasArrived()&&SBI1!=null&&SBI1.hasArrived()){
					if(select<=0.33333333f){
						pick=1;
					}
					else if(select<=0.66666666f){
						pick=2;
					}
					else if(select<=1.0f){
						pick=3;
					}
				}
				
				/* If SBI1 is connected, and SBI1 and I1 inputs have arrived, then
				 * decide between the 2 depending on which half the random
				 * number lies between, and record the selection */
				else if(I1.hasArrived() &&SBI1!=null&&SBI1.hasArrived()){
					if(select<0.5f){
						pick=1;
					}
					else if(select<=1.0f){
						pick=3;
					}
				}
				
				/* If SBI1 is connected, and SBI1 and I2 inputs have arrived, then
				 * decide between the 2 depending on which half the random
				 * number lies between, and record the selection */
				else if(I2.hasArrived() &&SBI1!=null&&SBI1.hasArrived()){
					if(select<0.5f){
						pick=2;
					}
					else if(select<=1.0f){
						pick=3;
					}
				}
				
				/* If I1 and I2 inputs have arrived, then decide between the 
				 * 2 depending on which half the random number lies between, 
				 * and record the selection */
				else if(I1.hasArrived() &&I2.hasArrived()){
					if(select<0.5f){
						pick=1;
					}
					else if(select<=1.0f){
						pick=2;
					}
				}
				
				/* If only I1 has arrived, then select it by default */
				else if(I1.hasArrived()){
					pick=1;
				}
				
				/* If only I2 has arrived, then select it by default */
				else if(I2.hasArrived()){
					pick=2;
				}
				
				/* If SBI1 is connected and is the only signal to
				 * have arrived, then select it by default */
				else if(SBI1!=null&&SBI1.hasArrived()){
					pick=3;
				}
				
				/* If I1 was selected for processing */
				if(pick==1){

					/* Lower the signal on I1 */
					I1.setActive(false);
					
					/* Set the processing state to processing */
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
					
					/* Wait until output 1 is available */
					while((O1.isActive() || paused) &&!kill){
						Thread.yield();
					}
					if(!kill){
						
						/* Set output 1 to active */
						O1.setActive(true);
					}
				}
				
				/* If I2 was selected for processing */
				else if(pick==2){

					/* Lower the signal on I2 */
					I2.setActive(false);
					
					/* Set the processing state to processing */
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
					
					/* Wait until SBO2 becomes available */
					while((SBO2.isActive() || paused) && !kill){
						Thread.yield();
					}
					if(!kill){
						
						/* Set SBO2 to active */
						SBO2.setActive(true);
					}
				}
				
				/* If SBI1 was selected for processing */
				else if(pick==3){
					
					/* Lower the signal on SBI1 */
					SBI1.setActive(false);
					
					/* Set the processing state to processing */
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
					
					/* Wait for SBO1 to become available */
					while((SBO1.isActive() || paused) && !kill){
						Thread.yield();
					}
					if(!kill){
						
						/* Raise the signal on SBO1 and set
						 * the internal state to 2 */
						SBO1.setActive(true);
						state=2;
					}
				}
			}
		}
		
		/* If the internal state is 2 */
		else if(state==2){

			/* If SBI2 is connected */
			if(SBI2!=null){
				
				/* Wait until a valid input I1, I2, or SBI2 arrives */
				while(((!I1.hasArrived() && !I2.hasArrived() &&
						!SBI2.hasArrived()) || paused) && !kill){
					Thread.yield();
				}
			}
			
			/* Else just wait until either I1 or I2 arrives */
			else{
				while(((!I1.hasArrived() && !I2.hasArrived())
							|| paused) && !kill){
					Thread.yield();
				}
			}
			if(!kill){
				
				/* Records whether I1, I2 or SBI2 is processed */
				int pick=0;

				/* Pick a random number between 0 and 1 in order
				 * to allow arbitrary selection */
				float select=GlobalAttributes.random();

				/* If SBI2 is connected, and all 3 inputs have arrived, then
				 * decide between the 3 depending on which third the random
				 * number lies between, and record the selection */
				if(I1.hasArrived() &&I2.hasArrived()&&SBI2!=null&&SBI2.hasArrived()){
					if(select<=0.33333333f){
						pick=1;
					}
					else if(select<=0.66666666f){
						pick=2;
					}
					else if(select<=1.0f){
						pick=4;
					}
				}
				
				/* If SBI2 is connected, and SBI2 and I1 inputs have arrived, then
				 * decide between the 2 depending on which half the random
				 * number lies between, and record the selection */
				else if(I1.hasArrived()&&SBI2!=null &&SBI2.hasArrived()){
					if(select<0.5f){
						pick=1;
					}
					else if(select<=1.0f){
						pick=4;
					}
				}
				
				/* If SBI2 is connected, and SBI2 and I2 inputs have arrived, then
				 * decide between the 2 depending on which half the random
				 * number lies between, and record the selection */
				else if(I2.hasArrived()&&SBI2!=null &&SBI2.hasArrived()){
					if(select<0.5f){
						pick=2;
					}
					else if(select<=1.0f){
						pick=4;
					}
				}
				
				/* If I1 and I2 inputs have arrived, then decide between the 
				 * 2 depending on which half the random number lies between, 
				 * and record the selection */
				else if(I1.hasArrived() &&I2.hasArrived()){
					if(select<0.5f){
						pick=1;
					}
					else if(select<=1.0f){
						pick=2;
					}
				}
				
				/* If only I1 has arrived, then select it by default */
				else if(I1.hasArrived()){
					pick=1;
				}
				
				/* If only I2 has arrived, then select it by default */
				else if(I2.hasArrived()){
					pick=2;
				}
				
				/* If SBI2 is connected and is the only signal to
				 * have arrived, then select it by default */
				else if(SBI2!=null&&SBI2.hasArrived()){
					pick=4;
				}
				
				/* If I1 was selected for processing */
				if(pick==1){

					/* Lower the signal on I1 */
					I1.setActive(false);
					
					/* Set the processing state to processing */
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
					
					/* Wait for SBO1 to become available */
					while((SBO1.isActive() || paused) &&!kill){
						Thread.yield();
					}
					if(!kill){
						
						/* Set SBO1 to active */
						SBO1.setActive(true);
					}
				}
				
				/* If I2 was selected for processing */
				else if(pick==2){

					/* Lower the signal on I2 */
					I2.setActive(false);
					
					/* Set the processing state to processing */
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
					
					/* Wait for O2 to become available */
					while((O2.isActive() || paused) && !kill){
						Thread.yield();
					}
					if(!kill){
						
						/* Set O2 to active */
						O2.setActive(true);
					}
				}
				
				/* If SBI2 was selected for processing */
				else if(pick==4){
					
					/* Lower the signal on SBI2 */
					SBI2.setActive(false);
					
					/* Set the processing state to processing */
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
					
					/* Wait for SBO2 to become available */
					while((SBO2.isActive() || paused) && !kill){
						Thread.yield();
					}
					if(!kill){
						
						/* Set SBO2 to active and set the internal state to 1 */
						SBO2.setActive(true);
						state=1;
					}
				}
			}
		}	
	}
	/* Render logic for the element, given the location, width, and height of the element, as well as event labels. 
	 * If this element is an instance of the sychronised version (ConflictSynchThread subclass), then extra labels
	 * and connection points are rendered */
	void drawConflict(Graphics2D g2, int x, int y, int width, int height, boolean synched, String left, String right){

		/* Set the fill colour depending on whether
		 * the element is processing or not */
		if(processing){
			g2.setColor(GlobalAttributes.processingColor);
		}
		else{
			g2.setColor(Color.WHITE);
		}
		g2.fillRect(x,y,width,height);
		
		/* Draw the outline and fill */
		g2.setStroke(GlobalAttributes.basicStroke);
		g2.setColor(Color.BLACK);
		g2.drawRect(x,y,width,height);
		
		/* Draw the label */
		g2.setFont(GlobalAttributes.middleFont);
		String write="Solve "+left+"#"+right;
		int stringWidth=g2.getFontMetrics().stringWidth(write);
		g2.drawString(write,x+width/2-stringWidth/2,y+height-width/10);

		/* Draw a tick on the left and the right, and set the colours to green
		 * or red depending on which side of the element is blocked/allowed */
		g2.setStroke(GlobalAttributes.thickStroke);
		if(state==0 || state==1){
			g2.setColor(Color.GREEN);
			g2.drawLine(x+width/6-width/20, y+height/2, x+width/6, y+height/2+width/20);
			g2.drawLine(x+width/6, y+height/2+width/20, x+width/6+width/20, y+height/2-width/20);
		}
		else{
			g2.setColor(Color.RED);
			g2.drawLine(x+width/6-width/20, y+height/2-width/20, x+width/6+width/20, y+height/2+width/20);
			g2.drawLine(x+width/6-width/20, y+height/2+width/20, x+width/6+width/20, y+height/2-width/20);
		}
		if(state==0 || state==2){
			g2.setColor(Color.GREEN);
			g2.drawLine(x+5*width/6-width/20, y+height/2, x+5*width/6, y+height/2+width/20);
			g2.drawLine(x+5*width/6, y+height/2+width/20, x+5*width/6+width/20, y+height/2-width/20);
		}
		else{
			g2.setColor(Color.RED);
			g2.drawLine(x+5*width/6-width/20, y+height/2-width/20, x+5*width/6+width/20, y+height/2+width/20);
			g2.drawLine(x+5*width/6-width/20, y+height/2+width/20, x+5*width/6+width/20, y+height/2-width/20);
		}

		/* Draw the labels common to both types of the element */
		g2.setColor(Color.BLACK);
		g2.setFont(GlobalAttributes.smallFont);
		stringWidth=g2.getFontMetrics().stringWidth("I1");
		int stringHeight=g2.getFontMetrics().getHeight();
		g2.drawString("I1",x+width/4-stringWidth/2,y+height-height/10);
		g2.drawString("I2",x+3*width/4-stringWidth/2,y+height-height/10);
		stringWidth=g2.getFontMetrics().stringWidth("O1");
		g2.drawString("O1",x+width/4-stringWidth/2,y+stringHeight);
		g2.drawString("O2",x+3*width/4-stringWidth/2,y+stringHeight);
		stringWidth=g2.getFontMetrics().stringWidth("SBO1");
		g2.drawString("SBO1",x+width/8-stringWidth/2,y+height-height/10);
		g2.drawString("SBO2",x+7*width/8-stringWidth/2,y+height-height/10);
		stringWidth=g2.getFontMetrics().stringWidth("SBI1");
		g2.drawString("SBI1",x+width/8-stringWidth/2,y+stringHeight);
		g2.drawString("SBI2",x+7*width/8-stringWidth/2,y+stringHeight);
		
		/* Draw the connection points common to both types of the element*/
		g2.fillOval(x+width/8-width/75, y-width/75, width/37, width/37);
		g2.fillOval(x+width/4-width/75, y-width/75, width/37, width/37);
		g2.fillOval(x+3*width/4-width/75, y-width/75, width/37, width/37);
		g2.fillOval(x+7*width/8-width/75, y-width/75, width/37, width/37);
		g2.fillOval(x+width/8-width/75, y+height-width/75, width/37, width/37);
		g2.fillOval(x+width/4-width/75, y+height-width/75, width/37, width/37);
		g2.fillOval(x+3*width/4-width/75, y+height-width/75, width/37, width/37);
		g2.fillOval(x+7*width/8-width/75, y+height-width/75, width/37, width/37);

		/* If synchronised, then draw the extra labels and connection points */
		if(synched){
			stringWidth=g2.getFontMetrics().stringWidth("PBI1");
			g2.drawString("PBI1",x+3*width/8-stringWidth/2,y+height-height/10);
			g2.drawString("PBI2",x+5*width/8-stringWidth/2,y+height-height/10);
			stringWidth=g2.getFontMetrics().stringWidth("PBO1");
			g2.drawString("PBO1",x+3*width/8-stringWidth/2,y+stringHeight);
			g2.drawString("PBO2",x+5*width/8-stringWidth/2,y+stringHeight);
			g2.fillOval(x+3*width/8-width/75, y-width/75, width/37, width/37);
			g2.fillOval(x+5*width/8-width/75, y-width/75, width/37, width/37);
			g2.fillOval(x+3*width/8-width/75, y+height-width/75, width/37, width/37);
			g2.fillOval(x+5*width/8-width/75, y+height-width/75, width/37, width/37);
		}
	}
}