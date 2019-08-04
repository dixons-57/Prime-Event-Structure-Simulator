package Model;
import java.util.LinkedList;

/* This class contains the logic which generates the state transition
 * system representation of the Event Structure */
public class TransitionGraph {

	/* The list of node objects for the graph */
	public static LinkedList<TransitionGraphNode> nodes = new LinkedList<TransitionGraphNode>();
	
	/* Stores the number of nodes at each level in the tree - this is used
	 * to help calculate render sizes and positions
	 */
	public static LinkedList<Integer> noAtLevel = new LinkedList<Integer>();
	
	/* The list of completed nodes during an execution sequence - the integer value
	 * represents the node ID */
	public static LinkedList<Integer> executionTrace = new LinkedList<Integer>();
	
	/* The number of nodes produced so far - this is incremented and allows
	 * us to assign a numerical identifier to each node */
	static int counter=0;
	
	/* Clears the transition graph data */
	public static void clearGraph(){
		nodes.clear();
		noAtLevel.clear();
		counter=0;
	}
	
	/* Updates the execution trace of the graph using the ID of the event
	 * which has just completed */
	static void updateTrace(int id){
		
		/* Retrieve the last element from the trace */
		Integer lastNode = executionTrace.getLast();
		TransitionGraphNode node=null;
		for(int i=0;i<nodes.size();i++){
			if(nodes.get(i).id==lastNode.intValue()){
				node=nodes.get(i);
				break;
			}
		}
		
		/* Calculate the next node in the execution trace by identifying
		 * the transition which has just occurred */
		for(int i=0;i<node.transitionLabels.size();i++){
			if(node.transitionLabels.get(i).equals(EventStructure.events[id])){
				executionTrace.add(new Integer(node.targetNodes.get(i)));
				break;
			}
		}
	}
	
	/* Reset the execution trace */
	static void resetTrace(){
		executionTrace.clear();	
		executionTrace.add(new Integer(0));
	}
	
	/* Builds the transition graph - the event structure must already be
	 * "loaded" into the EventStructure class */
	public static void buildGraph(){
		
		/* Clear the graph */
		clearGraph();
		
		/* Reset the execution trace of the graph */
		resetTrace();
		
		/* Create the initial empty node, where no events have been
		 * completed */
		TransitionGraphNode empty= new TransitionGraphNode(counter);
		empty.addEvent("");
		nodes.add(empty);
		
		/* Begin recursive traversal of the graph */
		traverseGraph(empty);
		
	}
	
	/* Recursively traverses the entire transition graph
	 * from the specified node */
	static void traverseGraph(TransitionGraphNode source){
		
		/* Modify the list that tracks the number of nodes
		 * at each level in the tree - this data is used for
		 * rendering (size calculation) */
		if(noAtLevel.size()<source.completedEvents.size()){
			noAtLevel.add(new Integer(1));
		}
		else {
			Integer temp=noAtLevel.remove(source.completedEvents.size()-1);
			Integer newInt=new Integer(temp.intValue()+1);
			noAtLevel.add(source.completedEvents.size()-1,newInt);
		}
		
		/* For every possible event */
		for(int i=0;i<EventStructure.events.length;i++){
			
			/* Continue if the event is not in the list for the current node */
			if(!source.contains(EventStructure.events[i])){
				
				/* If the event can be completed from the current node */
				if(checkCanComplete(source.completedEvents,EventStructure.events[i])){
					
					/*Increment the counter */
					counter++;
					
					/* Create a new node identical to the old node, and add the newly
					 * completed event to it
					 */
					TransitionGraphNode target=new TransitionGraphNode(counter,source);
					target.addEvent(EventStructure.events[i]);
					
					/* Assume that the node just created is not already present */
					TransitionGraphNode existing=null;
					
					/* Check against all existing nodes */
					for(int j=0;j<nodes.size();j++){
						if(nodes.get(j).isEqualTo(target)){
							existing=nodes.get(j);
							break;
						}
					}
					
					/* If the node does in fact exist, just create a link between
					 * the current node and that node with the event as a transition
					 */
					if(existing!=null){
						source.addTransition(EventStructure.events[i],existing.id);
						counter--;
					}
					
					/* Otherwise, add the new node to the graph, link to it
					 * and then move to it and continue the traversal recursively
					 */
					else{
						source.addTransition(EventStructure.events[i], target.id);
						nodes.add(target);
						traverseGraph(target);
					}
				}
			}
		}
	}
	
	/* Returns true if an event can be completed from the current node (specified
	 * as a string of already completed events)
	 */
	static boolean checkCanComplete(LinkedList<String> completed, String event){
		
		/* Check against all possible events */
		for(int i=0;i<EventStructure.events.length;i++){
			
			/* If the desired event is a successor of the event which we are
			 * checking against, and the event is not a component of this state, 
			 * then we immediately return false	
			 */
			if(EventStructure.succeeds[EventStructure.lookup(event)][i] &&
					!completed.contains(EventStructure.events[i])){
				return false;
			}
			
			/* If the desired event is in conflict with the event which we
			 * are checking against, and the event IS a component of this state,
			 * then we also return false
			 */
			if(EventStructure.conflicts[EventStructure.lookup(event)][i] &&
					completed.contains(EventStructure.events[i])){
				return false;
			}
		}
		
		/* If the desired event has survived all of the above tests, then
		 * it is a valid transition from the current node, and hence
		 * we return true
		 */
		return true;
	}
		
}
