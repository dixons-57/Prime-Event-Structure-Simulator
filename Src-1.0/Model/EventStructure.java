package Model;
import java.util.LinkedList;

import GUI.ControlPanel;
import GUI.GraphicsPanel;
import GUI.OutputFrame;
import Main.GlobalAttributes;

/* This class encapsulates all data and operations that define a
 * Prime Event Structure - this class is never instantiated.
 * To ensure that one instance is only ever in operation and for
 * convenience, all methods are referenced statically.
 * Internally, events are referred to by their index in a 1D array,
 * this is invisible to the user however, who sees them as literal
 * string-named objects. The mapping from names to indexes is 1:1
 * and is dependent on the order in which the events are declared.
 * This class also contains logic for parsing the user's own 
 * definition of an Event Structure.
 */
public class EventStructure{
    
    /* List of event names */
    public static String[] events=new String[0];
    
    /* Corresponding array of threads - one per event */
    static EventThread[] eventThreads=new EventThread[0];
    
    /* 1D array to track which events have been completed */
    public static boolean[] completed = new boolean[0];
    
    /* 1D array to track which events are/aren't eventually allowed */
    public static boolean[] eventuallyAllowed = new boolean[0];
    
    /* 2D matrix to store the precede relationship between events, i.e. a lookup matrix.
     * 1 for an entry means that this row event precedes this column event
     */
    public static boolean[][] precedes=new boolean[0][0];
    
    /* 2D matrix to store the succeeds relationship between events, i.e. a lookup matrix.
     * 1 for an entry means that this row event succeeds this column event. This is
     * derivable from the above matrix, but is pre-calculated in order to speed up calculating
     * whether an event can be completed
     */
    static boolean[][] succeeds=new boolean[0][0];
    
    /* 2D matrix to store the conflict relationship between events, i.e. a lookup matrix.
     * 1 for an entry means that this row event is in conflict with this column event.
     */
    public static boolean[][] conflicts=new boolean[0][0];
    
    /* Initialises all arrays/matrices for the given array of event names -
     * does not store actual relationships */
    static void initialise(String[] n){
    	
    	/* Store the list of event names */
        events=n;
        
        /* Set the arrays/matrices to the correct length */
        completed=new boolean[n.length];
        eventuallyAllowed=new boolean[n.length];
        precedes=new boolean[n.length][n.length];
        succeeds=new boolean[n.length][n.length];
        conflicts = new boolean[n.length][n.length];
        eventThreads=new EventThread[n.length];
        
        /* Initialise all arrays/matrices to default values */
        for(int i=0;i<n.length;i++){
            completed[i]=false;
            eventuallyAllowed[i]=true;
            
            for(int j=0;j<n.length;j++){
                precedes[i][j]=false;
                succeeds[i][j]=false;
                conflicts[i][j]=false;
            }
            
            /* Creates a thread for each event */
            eventThreads[i]=new EventThread(i);
            eventThreads[i].start();
        }
    }
    
    /* Clears all event structure data and definitions from memory */
	public static void unload() {
		
		/* Signals all threads to terminate */
		for(int i=0;i<events.length;i++){	
            eventThreads[i].kill=true;
            eventThreads[i].paused=false;
        }
        
		/* Initialises all arrays to empty */
	    events=new String[0];
	    completed = new boolean[0];
	    eventuallyAllowed = new boolean[0];
	    precedes=new boolean[0][0];
	    succeeds=new boolean[0][0];
	    conflicts=new boolean[0][0];
        EventStructureRender.graphPositions=new int[0];
    	TransitionGraphRender.transitionPositions=new int[0];
		
    	/* Waits for all threads to end of their own accord
    	 * before returning swing control back to the user
    	 */ 
        for(int i=0;i<eventThreads.length;i++){
	        try {
				eventThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }

        /* Initialises the array of threads to empty */
	    eventThreads=new EventThread[0];
	}
    
    /* Reset the Event Structure to the initial state -
     * this resets the allowed/completed arrays and threads
     */
    public static void resetThreads(){
        for(int i=0;i<events.length;i++){
        	
        	/* Reset the arrays */
            eventuallyAllowed[i]=true;
            completed[i]=false;
            
            /* Send the kill signal and unpause all threads
             * This allows the threads to finish of their
             * own accord when given the opportunity
             */
            eventThreads[i].kill=true;
            eventThreads[i].paused=false;
        }
        
    	/* Waits for all threads to end of their own accord
    	 * before returning swing control back to the user
    	 */ 
        for(int i=0;i<eventThreads.length;i++){
        	
	        	try {
					eventThreads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        
	        /* Create a new thread in the paused state
	         * and start it
	         */
	        eventThreads[i]=new EventThread(i);
	        eventThreads[i].start();
        }
        
        /* Resets the transition graph trace
         * if one exists */
        if(GlobalAttributes.transitions){
        	TransitionGraph.resetTrace();
        }
        
        /* Cause an on-screen repaint */
        GraphicsPanel.global.repaint();
    }
    
    /* Set the first event to be a precedes relation
     * of the second event - also does the opposite for
     * the reverse
     */
    static void setPrecedes(String name1, String name2){
        precedes[lookup(name1)][lookup(name2)]=true;
        succeeds[lookup(name2)][lookup(name1)]=true;
    }

    /* Returns the internal integer index
     * of the event with the given string name
     */
    static int lookup(String name) {
    	
    	/* Iterate through all events */
        for(int i=0;i<events.length;i++){
        	
        	/* Return the index if it matches
        	 * the given string
        	 */
            if(events[i].equals(name)){
                return i;
            }
        }
        
        /* Else return a default code - this
         * stops the compiler from complaining
         */
        return -1;
    }

    /* Sets both events to be in conflict with each
     * other
     */
    static void setConflicts(String name1, String name2){
        conflicts[lookup(name1)][lookup(name2)]=true;
        conflicts[lookup(name2)][lookup(name1)]=true;
    }
    
    /* This sends the signal to all threads to pause
     * This won't interrupt a currently processing event
     * and will wait until it has completed
     */
    public static void pauseThreads(){
        for(int i=0;i<eventThreads.length;i++){
        	eventThreads[i].paused=true;
        }
    }
    
    /* This sends the signal to all threads to continue
     */
    public static void unPauseThreads(){
    	for(int i=0;i<eventThreads.length;i++){
    		eventThreads[i].paused=false;
    	}
    }

    /* Attempts to complete the event at the specified index - This is
     * a synchronised method and prevents multiple event threads from 
     * accessing this code concurrently - returns true if an event
     * should attempt to keep executing - false is returned to a thread
     * the event has become locked out or has been completed */
    synchronized static boolean completeEvent(int event) {
    	
        OutputFrame.global.write("Event: "+events[event]+ " is attempting");
        
        /* Check if the event is locked out */
        if(!eventuallyAllowed[event]){
            OutputFrame.global.write("\tEvent locked out");
            
            /* If the event has been locked out - then it should
             * not continue attempting to execute */
            return false;
        }
        
        /* Check if preceding events have been completed */
        for(int i=0; i<events.length;i++){
            if(succeeds[event][i]){
                if(completed[i]){
                    OutputFrame.global.write("\tEnabled by event: "+events[i]+" which has been " +
                    		"completed");
                }
                else{
                    OutputFrame.global.write("\tEnabled by event: "+events[i]+" which has not been "+
                            "completed");
                    
                    /* The event has not been completed so it should
                     * attempt to keep executing */
                    return true;
                }
            }
        }
        
        /* If successful, set the corresponding flag for this event's
         * completion to true */
        completed[event]=true;
        OutputFrame.global.write("\tEvent completed");
        
        /* Check the completed event against
         * all other events
         */
        for(int i=0;i<events.length;i++){
        	
        	/* If the other event is in conflict, disable the allowed flag
        	 * for the second event
        	 */
            if(conflicts[event][i]){
                OutputFrame.global.write("\tIn conflict with event: "+events[i]);
                eventuallyAllowed[i]=false;
                OutputFrame.global.write("\tEvent: "+events[i]+" disabled");
                
                /* Recursively disable any successive events of this disabled
                 * event */
                inheritConflict(i);
            }
        }
        
        /* Only perform execution trace if we are in transition graph
         * mode */
        if(GlobalAttributes.transitions){
        
	        /* Update the execution trace for the transition graph */
	        TransitionGraph.updateTrace(event);
        }
        
        /* Force a repaint of the visualisation */
        GraphicsPanel.global.repaint();
        
        /* The event has succeeded so it should no longer attempt to keep
         * executing */
        return false;
    }

    /* Recursively disables any events that succeed the specified disabled
     * event
     */
    static void inheritConflict(int event) {
    	
    	/* Iterate through all events */
        for(int i=0;i<events.length;i++){
        	
        	/* If the other event is a successor of the original event,
        	 * disable it and recursively continue the trace
        	 */
            if(precedes[event][i]){
                eventuallyAllowed[i]=false;
                OutputFrame.global.write("\tEvent: "+events[i]+" has inherited event: "+
                        events[event]+"'s conflict");
                inheritConflict(i);
            }
        }
    }

    /* Loads the specified event structure from the control panel's input
     * area into memory 
     */
    public static void load() {
        OutputFrame.global.write("Clearing event matrices");
        
        /* Clear the existing event data */
        unload();
        
        /* Parse the input area from the control panel
         * and generate the event structure */
        parseText();
        
        /* Output useful information about the relationships 
         * for each event by using the lookup matrix - this is
         * done after all parsing rather than during. This is
         * less efficient but much easier to read for the user, 
         * as information pertaining to each event is unlikely to
         * be relevantly ordered when it is parsed
         */
        for(int i=0;i<events.length;i++){
            OutputFrame.global.write("Created event: "+events[i]);
            for(int j=0;j<events.length;j++){
                if(precedes[i][j]){
                    OutputFrame.global.write("\tPrecedes event: "+events[j]);
                }
                if(succeeds[i][j]){
                    OutputFrame.global.write("\tSucceeds event: "+events[j]);
                }
                if(conflicts[i][j]){
                    OutputFrame.global.write("\tConflicts with event: "+events[j]);
                }
            }
        }
        
        /* If we are in transition graph mode, then generate the graph
         * and determine on-screen positions */
        if(GlobalAttributes.renderMode==2){
        	TransitionGraph.buildGraph();
        	GraphicsPanel.global.calculatePositions();
        }
        
        /* Calculate on-screen positions for the graphical nodes */
        GraphicsPanel.global.calculatePositions();
        
        /* Force a repaint of the visualisation */
        GraphicsPanel.global.repaint();
    }    
    
    /* Parses the input area of the control panel and generates the
     * corresponding event structure. The routine tries its best to continue
     * in the face of input errors on part of the user
     * Events must be declared atomically before they are used as part of
     * causality or conflict relations
     */
    private static void parseText(){
    	
    	/* Retrieve the text and remove newline characters or empty space */
    	String text=ControlPanel.global.inputArea.getText();
    	text=text.replaceAll(" ", "");
    	text=text.replaceAll("\\n","");
    	
    	OutputFrame.global.write("Parsing input");
    	
    	/* Separate into individual expressions */
    	String[] expressions=text.split(";");
    	
    	/* Lists for adding events, causality and conflicts */
    	LinkedList<String> events=new LinkedList<String>();
    	LinkedList<String> precedes=new LinkedList<String>();
    	LinkedList<String> conflicts=new LinkedList<String>();
    	
    	/* For every expression*/
    	for(int i=0;i<expressions.length;i++){
    		
    		/* Attempt to break into a causality relation with two arguments*/
    		String[] precedesNames=expressions[i].split("<");
    		
    		/* If the result is length 1 - then it is not a causality relation */
    		if(precedesNames.length==1){
    			
    			/* Attempt to break into a conflict relation with two arguments */
    			String[] conflictsNames=expressions[i].split("#");
    			
    			/* If the result is length 1 - then it is simply an atomic event defined */
    			if(conflictsNames.length==1){
    				
    				/* Retrieve the name of the event */
    				String eventName=conflictsNames[0];
    				
    				/* Assume it is a valid name */
    				Boolean valid=true;
    				
    				/* Check that each character is valid, must be 0-9, A-Z or a-z */
    				for(int j=0; j<eventName.length();j++){
    					char currentChar=eventName.charAt(j);
    					if(!(currentChar>='0' && currentChar<='9') && !(currentChar>='A' && currentChar<='Z') && !(currentChar>='a' && currentChar<='z')){
    						valid=false;
    						break;
    					}
    				}
    				
    				/* If the string is valid - check against existing names */
    				if(valid){
    					
    					/* Assume that it does not exist */
    					Boolean exists=false;
    					
    					/* Check against all currently parsed events */
    					for(int j=0; j<events.size();j++){
    						if(events.get(j).equals(eventName)){
    							exists=true;
    							break;
    						}
    					}
    					
    					/* If the event does not already exist - add it to the list */
    					if(!exists){
    						events.add(eventName);
    					}
    					
    					/* If it does exist, report an error */
    					else{
    	    		    	OutputFrame.global.write("Event already exists");
    					}
    				}
    				
    				/* Otherwise throw an error */
    				else{
        		    	OutputFrame.global.write("Invalid event - Use only ASCII 0-9, A-Z or a-z");	
    				}
    			}
    			
    			/* If the result is length 2 - then it is a conflict relation */
    			else if(conflictsNames.length==2){
    				
    				/* Retrieve the two arguments */
    				String conflicts1=conflictsNames[0];
    				String conflicts2=conflictsNames[1];
        			
        			/* Check arguments for difference */
        			if(conflicts1.equals(conflicts2)){
        		    	OutputFrame.global.write("Invalid conflict relation - arguments must be distinct");
        			}
        			else{
        				
        				/* Assume both arguments are invalid */
        				boolean oneValid=false;
            			boolean twoValid=false;
            			
	    				/* Check against all currently parsed events */
	    				for(int j=0; j<events.size();j++){
	    					
	    					/* If the first argument is found, set its flag to true */
	    					if(events.get(j).equals(conflicts1)){
	    						oneValid=true;
	    					}
	    					
	    					/* If the second argument is found, set its flag to true */
	    					else if(events.get(j).equals(conflicts2)){
	    						twoValid=true;
	    					}
	    					
	    					/* If both arguments have been validated, then break out
	    					 * of the loop */
	    					if(oneValid && twoValid){
	    						break;
	    					}
	    				}
	    				
	    				/* If both arguments are valid, then add the two
	    				 * events to the list of conflicted pairs */
	    				if(oneValid && twoValid){
	    					conflicts.add(conflicts1);
	    					conflicts.add(conflicts2);
	    				}
	    				else{
	        		    	OutputFrame.global.write("Invalid conflict relation - one or more events are undefined");
	    				}
        			}
    			}
    			
    			/* If the result is longer - then it is an error as this is not permitted */
    			else{
    		    	OutputFrame.global.write("Parser error, too many arguments for # relation");
    			}
    		}
    		
    		/* If the result is length 2 - then it is a causality relation */
    		else if(precedesNames.length==2){
    			
    			/* Retrieve the two arguments */
    			String precedes1=precedesNames[0];
    			String precedes2=precedesNames[1];
    			
    			/* Check arguments for difference */
    			if(precedes1.equals(precedes2)){
    		    	OutputFrame.global.write("Invalid conflict relation - arguments must be distinct");
    			}
    			else{
    				
    				/* Assume both arguments are invalid */
    				boolean oneValid=false;
        			boolean twoValid=false;
    			
        			/* Check against all currently parsed events */
					for(int j=0; j<events.size();j++){
						
						/* If the first argument is found, set its flag to true */
						if(events.get(j).equals(precedes1)){
							oneValid=true;
						}
						
						/* If the second argument is found, set its flag to true */
						if(events.get(j).equals(precedes2)){
							twoValid=true;
						}
						
    					/* If both arguments have been validated, then break out
    					 * of the loop */
						if(oneValid && twoValid){
							break;
						}
					}
					
    				/* If both arguments are valid, then add the two
    				 * events to the list of causality pairs */
					if(oneValid && twoValid){
						precedes.add(precedes1);
						precedes.add(precedes2);
					}
					else{
	    		    	OutputFrame.global.write("Invalid causality relation - one or more events are undefined");
					}
    			}
    			
    		}
    		
    		/* If the result is longer - then it is an error as this is not permitted */
    		else{
		    	OutputFrame.global.write("Parser error, too many arguments for < relation");
    		}
    	}
    	
    	/* Convert the list of names to an array */
    	String[] eventNames=new String[events.size()];
    	for(int i=0;i<eventNames.length;i++){
    		eventNames[i]=events.get(i);
    	}
    	
    	/* Initialises the event structure with the list of names */
    	initialise(eventNames);
    	
    	/* Set the precedes/succeeds matrices with the pairs of events
    	 * by retrieving them from the list of pairs */
    	for(int i=0;i<precedes.size();i+=2){
    		setPrecedes(precedes.get(i),precedes.get(i+1));
    	}
    	
    	/* Set the conflicts matrix with the pairs of events
    	 * by retrieving them from the list of pairs */
    	for(int i=0;i<conflicts.size();i+=2){
    		setConflicts(conflicts.get(i),conflicts.get(i+1));
    	}
    }
    
}
