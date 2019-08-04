package Model;
import java.util.LinkedList;

/* This class represents a node in the state transition system representation
 * of the Event Structure. It encompasses all data pertaining to a node as well
 * as operations to add data and compare data amongst pairs of nodes */
public class TransitionGraphNode {
		
		/* The unique ID for this graph node */
		public int id;
		
		/* The list of completed events (which actually identifies the node
		 * to the user) */
		public LinkedList<String> completedEvents=new LinkedList<String>();
		
		/* The transitions to other nodes, represented as a label
		 * with the completing event */
		LinkedList<String> transitionLabels=new LinkedList<String>();
		
		/* The ID of the nodes to which the above transitions connect
		 * (1-1 mapping between these arrays) */
		public LinkedList<Integer> targetNodes = new LinkedList<Integer>();
		
		/* Standard constructor, takes an ID to assign to this node */
		TransitionGraphNode(int id){
			this.id=id;
		}
		
		/* Most commonly-used constructur - takes an ID as above as well
		 * as a node from which to replicate data. This is useful for
		 * creating a new node which is similar to the old node */
		TransitionGraphNode(int id, TransitionGraphNode source){
			
			/* Assign the id */
			this.id=id;
			
			/* Iterate through all of the source nodes events
			 * and add them to this node's list */
			for(int i=0;i<source.completedEvents.size();i++){
				completedEvents.add(source.completedEvents.get(i));
			}
		}
		
		/* Add a completed event to this node 
		 * (usually called after the above constructor */
		void addEvent(String event){
			completedEvents.add(event);
		}
		
		/* Adds a transition (label and directed edge) to
		 * the specified target node with a given ID */
		void addTransition(String label, int target){
			transitionLabels.add(label);
			targetNodes.add(new Integer(target));
		}
		
		/* Checks whether this node contains a particular
		 * component/event
		 */
		boolean contains(String event){
			
			/* Iterates through all events of this node */
			for(int i=0;i<completedEvents.size();i++){
				
				/* If the event is found, return true */
				if(completedEvents.get(i).equals(event)){
					return true;
				}
			}
			
			/* Otherwise return false */
			return false;
		}
		
		/* Checks whether this node is equal to a given node (as in the two sets
		 * which represent the nodes are identical) */
		boolean isEqualTo(TransitionGraphNode other){
			
			/* If the sets are of different sizes, then immediately conclude
			 * that the two nodes can't be equal */
			if(completedEvents.size()!=other.completedEvents.size()){
				return false;
			}
			else{ 
				
				/* For every event in this node */
				for(int i=0;i<completedEvents.size();i++){
					
					/* Assume that it does not exist in the other node */
					boolean found=false;
					
					/* Iterate through all events of the other node */
					for(int j=0;j<completedEvents.size();j++){
						
						/* If the event is found in the other node, then
						 * flag it as found and break out of the inner-loop
						 */
						if(completedEvents.get(i).equals(other.completedEvents.get(j))){
							found=true;
							break;
						}
					}
					
					/* If the current event hasn't been found, return false
					 */
					if(found==false){
						return false;
					}
				}
				
				/* If this code is reached, then the two nodes must be identical */
				return true;
			}
		}
		
		/* Returns the string representation of this node - this is simply
		 * a collection of the completed events associated with this node
		 * in set notation form */
		public String nodeData(){
			
			/* Create a string buffer for efficient appending */
			StringBuffer string=new StringBuffer();
			
			/* Create the open brace */
			string.append("{");
			
			/* Add each event and a comma */
			for(int j=1;j<completedEvents.size()-1;j++){
				string.append(completedEvents.get(j)+", ");
			}
			
			/* Add the final event and a closing brace */
			string.append(completedEvents.get(completedEvents.size()-1));
			string.append("}");
			
			/* Convert to standard string and return */
			return string.toString();
		}
		
	}