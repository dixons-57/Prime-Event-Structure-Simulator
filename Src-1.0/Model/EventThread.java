package Model;

import Main.GlobalAttributes;

/* Thread that represents an atomic event - an instance of this
 * Thread class exists for each event which is defined */
public class EventThread extends Thread {
    
	/* The thread/event ID - used only internally and is not
	 * displayed to the user */
	int id;
	
	/* Flag to indicate whether this event should keep trying
	 * to execute */
	boolean cont=true;
	
	/* Flag to indicate whether the event should stay paused
	 * or continue executing
	 */
	boolean paused=true;
	
	/* Flag to indicate whether the event should cease execution
	 * due to a kill signal - this causes the thread to break out
	 * of its loop and end gracefully
	 */
	boolean kill=false;
	
	/* Constructor for this Thread class - assigns the ID
	 * for the event
	 */
    EventThread(int id) {
        this.id=id;
    }

    /* Thread execution semantics */
    public void run() {
    	
    	/* Infinite loop */
        while(cont && !kill){
        	
        	/* If the thread is paused and alive, then
        	 * simply yield the CPU until woken up
        	 */
        	while(paused && !kill){
        		Thread.yield();
        	}
        	
        	if(!kill){
        		
	        	/* Record the current system time */
	        	long time=System.currentTimeMillis();
	        	
	        	/* Wait for a random amount of time between 0 and maxWait milliseconds */
	        	int wait=(int)(GlobalAttributes.random()*(float)GlobalAttributes.maxWait);
	        	
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
			
        	/* If the thread is paused and alive, then
        	 * simply yield the CPU until woken up
        	 */
			while(paused && !kill){
				Thread.yield();
			}
			
			/* Attempt to complete the event - this is a sychronised method
			 * call - so no concurrent calls from multiple events (this would
			 * in fact be very bad)
			 */
			if(!kill){
				cont=EventStructure.completeEvent(id);
			}
        }
    }

}
