package Circuit;

import GUI.ControlPanel;
import Main.GlobalAttributes;

/* This class contains all hardcoded circuit examples - due to the complexity
 * of a circuit (structure, layout etc.), this is the only means supported.
 * The render logic for these examples is in the CircuitExamplesRender class.
 * This class is never instantiated. The detailed logic of each individual example
 * is NOT documented here, as it is irrelevant to program logic. Many variations on
 * the same circuit (alternative configurations, synchronised versions etc.) share
 * the same method but with different parameters to reduce code repetition
 */
public class CircuitExamples {

	/* Load the list of circuits available to the control panel circuit selection box */
	@SuppressWarnings("unchecked")
	public static void loadList(){
		ControlPanel.global.circuitSelection.addItem("3 events, 2 conflicts");
		ControlPanel.global.circuitSelection.addItem("3 events, 2 conflicts - synchronised");
		ControlPanel.global.circuitSelection.addItem("3 events, 2 conflicts (alternative)");
		ControlPanel.global.circuitSelection.addItem("3 events, 2 conflicts (alternative) - synchronised");
		ControlPanel.global.circuitSelection.addItem("3 events, 3 conflicts");
		ControlPanel.global.circuitSelection.addItem("3 events, 3 conflicts - synchronised");
		ControlPanel.global.circuitSelection.addItem("4 events, 3 conflicts");
		ControlPanel.global.circuitSelection.addItem("4 events, 3 conflicts - synchronised");
		ControlPanel.global.circuitSelection.addItem("4 events, 3 conflicts (alternative)");
		ControlPanel.global.circuitSelection.addItem("4 events, 3 conflicts (alternative) - synchronised");
		ControlPanel.global.circuitSelection.addItem("4 events, 4 conflicts"); //this
		ControlPanel.global.circuitSelection.addItem("4 events, 4 conflicts - synchronised"); //this
		ControlPanel.global.circuitSelection.addItem("4 events, 4 conflicts (alternative)");
		ControlPanel.global.circuitSelection.addItem("4 events, 4 conflicts (alternative) - synchronised");
		ControlPanel.global.circuitSelection.addItem("6 events");
	}

	/* Return a string which represents the current circuit
	 * as an event structure - the circuit emulates this particular
	 * event structure */
	public static String loadDescription(int i){
		switch (i){
		case 1:
			return "a;b;c;\na#b;\nb#c;";
		case 2:
			return "a;b;c;\na#b;\nb#c;";
		case 3:
			return "a;b;c;\na#b;\nb#c;";
		case 4:
			return "a;b;c;\na#b;\nb#c;";
		case 5:
			return "a;b;c;\na#b;\nb#c;\na#c;";
		case 6:
			return "a;b;c;\na#b;\nb#c;\na#c;";
		case 7:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;";
		case 8:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;";
		case 9:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;";
		case 10:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;";
		case 11:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;\na#d;";
		case 12:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;\na#d;";
		case 13:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;\na#d;";
		case 14:
			return "a;b;c;d;\na#b;\nb#c;\nc#d;\na#d;";
		case 15:
			return "a;b;c;d;e;f;\na<b;\na<c;\nc<d;\nc<e;\nd<f;\ne<f;";
		default: 
			return"Invalid";
		}
	}

	/* Select the circuit to load depending on what the selected
	 * index of the circuit selection box is - each circuit
	 * has a dedicated method */
	public static void selectCircuit() {
		switch (GlobalAttributes.circuitSelected){
		case 1:
			create3Way2PairCircuit(false,false);
			break;
		case 2:
			create3Way2PairCircuit(true,false);
			break;
		case 3:
			create3Way2PairCircuit(false,true);
			break;
		case 4:
			create3Way2PairCircuit(true,true);
			break;
		case 5:
			create3Way3PairCircuit(false);
			break;
		case 6:
			create3Way3PairCircuit(true);
			break;
		case 7:
			create4Way3PairCircuit(false,false);
			break;
		case 8:
			create4Way3PairCircuit(true,false);
			break;
		case 9:
			create4Way3PairCircuit(false,true);
			break;
		case 10:
			create4Way3PairCircuit(true,true);
			break;
		case 11:
			create4Way4PairCircuit(false,false);
			break;
		case 12:
			create4Way4PairCircuit(true,false);
			break;
		case 13:
			create4Way4PairCircuit(false,true);
			break;
		case 14:
			create4Way4PairCircuit(true,true);
			break;
		case 15:
			create6WayCircuit();
			break;
		}
		
		/* Prevent statistical analysis from being available for circuits
		 * 11,12 and 15 (15 doesn't require analysis, and 11/12 is subject
		 * to livelocks by its inherent nature, so analysis is not possible)
		 */
		if(GlobalAttributes.circuitSelected<15 && GlobalAttributes.circuitSelected!=11
				&& GlobalAttributes.circuitSelected!=12){
			ControlPanel.global.analysis.setEnabled(true);
		}
		else{
			ControlPanel.global.analysis.setEnabled(false);
		}
		
	}
	
	/* Creates a circuit which contains 3 events and 2 conflicts. This supports
	 * 2 possible configurations (using the alternative flag), both with a
	 * synchronised version (using the synched flag) */
	static void create3Way2PairCircuit(boolean synched, boolean alternative){
		ConflictThread AandB;
		ConflictThread BandC;
		
		if(synched){
			AandB=new ConflictSynchThread("A and B Conflict");
			BandC=new ConflictSynchThread("B and C Conflict"); 
			WireThread BprevBlocked=new WireThread();
			Circuit.circuitElements=new CircuitElement[20];
			if(alternative){
				((ConflictSynchThread)AandB).PBI2=BprevBlocked;
				((ConflictSynchThread)BandC).PBO1=BprevBlocked;
			}
			else{
				((ConflictSynchThread)AandB).PBO2=BprevBlocked;
				((ConflictSynchThread)BandC).PBI1=BprevBlocked;
			}
			Circuit.circuitElements[19]=BprevBlocked;
		}
		else{
			AandB=new ConflictThread("A and B Conflict");
			BandC=new ConflictThread("B and C Conflict");
			Circuit.circuitElements=new CircuitElement[19];
		}
		
		MergeThread mergeA=new MergeThread();
		MergeThread mergeB=new MergeThread();
		MergeThread mergeC=new MergeThread();
		WireThread Astart=new WireThread();
		WireThread Bstart=new WireThread();
		WireThread Cstart=new WireThread();
		WireThread AmergeIn=new WireThread();
		WireThread AmergeOut=new WireThread();
		WireThread BmergeOut=new WireThread();
		WireThread BmergeIn=new WireThread();
		WireThread Bconnect=new WireThread();
		WireThread BsubBlocked=new WireThread();
		WireThread CmergeOut=new WireThread();
		WireThread CmergeIn=new WireThread();
		WireThread endA=new WireThread();
		WireThread endB=new WireThread();
		WireThread endC=new WireThread();

		mergeA.input1=AmergeIn;
		mergeA.input2=Astart;
		mergeA.output=AmergeOut;
		mergeB.output=BmergeOut;
		mergeC.input1=Cstart;
		mergeC.input2=CmergeIn;
		mergeC.output=CmergeOut;
		AandB.I1=AmergeOut;
		AandB.I2=BmergeOut;
		AandB.O1=endA;
		AandB.SBO1=AmergeIn;
		BandC.I2=CmergeOut;
		BandC.O2=endC;
		BandC.SBO2=CmergeIn;
		
		if(alternative){
			AandB.I2=Bconnect;
			AandB.SBO2=BsubBlocked;	
			AandB.O2=endB;
			BandC.O1=Bconnect;
			BandC.SBI1=BsubBlocked;
			BandC.I1=BmergeOut;
			BandC.SBO1=BmergeIn;
			mergeB.input1=BmergeIn;
			mergeB.input2=Bstart;
		}
		else{
			AandB.I2=BmergeOut;
			AandB.O2=Bconnect;
			AandB.SBO2=BmergeIn;
			AandB.SBI2=BsubBlocked;
			BandC.I1=Bconnect;
			BandC.O1=endB;
			BandC.SBO1=BsubBlocked;
			mergeB.input2=BmergeIn;
			mergeB.input1=Bstart;
		}
		
		Circuit.circuitElements[0]=AandB;
		Circuit.circuitElements[1]=BandC;
		Circuit.circuitElements[2]=mergeA;
		Circuit.circuitElements[3]=mergeB;
		Circuit.circuitElements[4]=mergeC;
		Circuit.circuitElements[5]=Astart;
		Circuit.circuitElements[6]=Bstart;
		Circuit.circuitElements[7]=Cstart;
		Circuit.circuitElements[8]=AmergeIn;
		Circuit.circuitElements[9]=AmergeOut;
		Circuit.circuitElements[10]=BmergeOut;
		Circuit.circuitElements[11]=BmergeIn;
		Circuit.circuitElements[12]=Bconnect;
		Circuit.circuitElements[13]=BsubBlocked;
		Circuit.circuitElements[14]=CmergeOut;
		Circuit.circuitElements[15]=CmergeIn;
		Circuit.circuitElements[16]=endA;
		Circuit.circuitElements[17]=endB;
		Circuit.circuitElements[18]=endC;
		
		Astart.setActive(true);
		Bstart.setActive(true);
		Cstart.setActive(true);
	}
	
	/* Creates a circuit which contains 4 events and 3 conflicts. This supports
	 * 2 possible configurations (using the alternative flag), both with a
	 * synchronised version (using the synched flag) */
	private static void create4Way3PairCircuit(boolean synched, boolean alternative) {
		ConflictThread AandB;
		ConflictThread BandC;
		ConflictThread CandD;
		
		if(synched){
			AandB=new ConflictSynchThread("A and B Conflict");
			BandC=new ConflictSynchThread("B and C Conflict"); 
			CandD=new ConflictSynchThread("C and D Conflict"); 
			WireThread BprevBlocked=new WireThread();
			WireThread CprevBlocked=new WireThread();
			Circuit.circuitElements=new CircuitElement[29];
			if(alternative){
				((ConflictSynchThread)AandB).PBI2=BprevBlocked;
				((ConflictSynchThread)BandC).PBO1=BprevBlocked;
				((ConflictSynchThread)CandD).PBI1=CprevBlocked;
				((ConflictSynchThread)BandC).PBO2=CprevBlocked;
			}
			else{
				((ConflictSynchThread)AandB).PBO2=BprevBlocked;
				((ConflictSynchThread)BandC).PBI1=BprevBlocked;
				((ConflictSynchThread)BandC).PBO2=CprevBlocked;
				((ConflictSynchThread)CandD).PBI1=CprevBlocked;
			}
			Circuit.circuitElements[27]=BprevBlocked;
			Circuit.circuitElements[28]=CprevBlocked;
		}
		else{
			AandB=new ConflictThread("A and B Conflict");
			BandC=new ConflictThread("B and C Conflict");
			CandD=new ConflictThread("C and D Conflict"); 
			Circuit.circuitElements=new CircuitElement[27];
		}
		
		MergeThread mergeA=new MergeThread();
		MergeThread mergeB=new MergeThread();
		MergeThread mergeC=new MergeThread();
		MergeThread mergeD=new MergeThread();
		WireThread Astart=new WireThread();
		WireThread Bstart=new WireThread();
		WireThread Cstart=new WireThread();
		WireThread Dstart=new WireThread();
		WireThread AmergeIn=new WireThread();
		WireThread AmergeOut=new WireThread();
		WireThread BmergeIn=new WireThread();
		WireThread BmergeOut=new WireThread();
		WireThread CmergeIn=new WireThread();
		WireThread CmergeOut=new WireThread();
		WireThread DmergeIn=new WireThread();
		WireThread DmergeOut=new WireThread();
		WireThread endA=new WireThread();
		WireThread endB=new WireThread();
		WireThread endC=new WireThread();	
		WireThread endD=new WireThread();
		WireThread Bconnect=new WireThread();
		WireThread Cconnect=new WireThread();
		WireThread BsubBlocked=new WireThread();
		WireThread CsubBlocked=new WireThread();

		mergeA.input1=AmergeIn;
		mergeA.input2=Astart;
		mergeA.output=AmergeOut;
		mergeB.input1=Bstart;
		mergeB.input2=BmergeIn;
		mergeB.output=BmergeOut;
		mergeC.input1=Cstart;
		mergeC.input2=CmergeIn;
		mergeC.output=CmergeOut;
		mergeD.input1=Dstart;
		mergeD.input2=DmergeIn;
		mergeD.output=DmergeOut;
		AandB.O1=endA;
		AandB.SBO1=AmergeIn;
		AandB.I1=AmergeOut;	
		BandC.I2=CmergeOut;
		BandC.O2=Cconnect;
		BandC.SBO2=CmergeIn;
		BandC.SBI2=CsubBlocked;
		CandD.I1=Cconnect;
		CandD.I2=DmergeOut;
		CandD.O1=endC;
		CandD.O2=endD;
		CandD.SBO1=CsubBlocked;
		CandD.SBO2=DmergeIn;
		
		if(alternative){
			AandB.I2=Bconnect;
			AandB.SBO2=BsubBlocked;
			AandB.O2=endB;
			BandC.I1=BmergeOut;
			BandC.SBO1=BmergeIn;
			BandC.SBI1=BsubBlocked;
			BandC.O1=Bconnect;
		}
		else{
			AandB.O2=Bconnect;
			AandB.I2=BmergeOut;
			AandB.SBO2=BmergeIn;
			AandB.SBI2=BsubBlocked;
			BandC.I1=Bconnect;
			BandC.O1=endB;
			BandC.SBO1=BsubBlocked;
		}
		
		Circuit.circuitElements[0]=AandB;
		Circuit.circuitElements[1]=BandC;
		Circuit.circuitElements[2]=mergeA;
		Circuit.circuitElements[3]=mergeB;
		Circuit.circuitElements[4]=mergeC;
		Circuit.circuitElements[5]=Astart;
		Circuit.circuitElements[6]=Bstart;
		Circuit.circuitElements[7]=Cstart;
		Circuit.circuitElements[8]=AmergeIn;
		Circuit.circuitElements[9]=AmergeOut;
		Circuit.circuitElements[10]=BmergeOut;
		Circuit.circuitElements[11]=BmergeIn;
		Circuit.circuitElements[12]=Bconnect;
		Circuit.circuitElements[13]=BsubBlocked;
		Circuit.circuitElements[14]=CmergeOut;
		Circuit.circuitElements[15]=CmergeIn;
		Circuit.circuitElements[16]=endA;
		Circuit.circuitElements[17]=endB;
		Circuit.circuitElements[18]=endC;
		Circuit.circuitElements[19]=mergeD;
		Circuit.circuitElements[20]=Dstart;
		Circuit.circuitElements[21]=DmergeIn;
		Circuit.circuitElements[22]=DmergeOut;
		Circuit.circuitElements[23]=Cconnect;
		Circuit.circuitElements[24]=CsubBlocked;
		Circuit.circuitElements[25]=endD;
		Circuit.circuitElements[26]=CandD;
		
		Astart.setActive(true);
		Bstart.setActive(true);
		Cstart.setActive(true);
		Dstart.setActive(true);
	}
	
	/* Creates a circuit which contains 3 events and 3 conflicts. This supports a
	 * synchronised version (using the synched flag) */
	static void create3Way3PairCircuit(boolean synched){
		ConflictThread AandB;
		ConflictThread BandC;
		ConflictThread AandC;
		
		if(synched){
			AandB=new ConflictSynchThread("A and B Conflict");
			BandC=new ConflictSynchThread("B and C Conflict"); 
			AandC=new ConflictSynchThread("A and C Conflict");
			WireThread BprevBlocked=new WireThread();
			WireThread CprevBlocked=new WireThread();
			WireThread AprevBlocked=new WireThread();
			Circuit.circuitElements=new CircuitElement[27];
			((ConflictSynchThread)AandC).PBI1=AprevBlocked;
			((ConflictSynchThread)AandB).PBO1=AprevBlocked;
			((ConflictSynchThread)AandB).PBO2=BprevBlocked;
			((ConflictSynchThread)BandC).PBI1=BprevBlocked;
			((ConflictSynchThread)AandC).PBI2=CprevBlocked;
			((ConflictSynchThread)BandC).PBO2=CprevBlocked;
			Circuit.circuitElements[24]=AprevBlocked;
			Circuit.circuitElements[25]=BprevBlocked;
			Circuit.circuitElements[26]=CprevBlocked;
		}
		else{
			AandB=new ConflictThread("A and B Conflict");
			BandC=new ConflictThread("B and C Conflict");
			AandC=new ConflictThread("A and C Conflict");
			Circuit.circuitElements=new CircuitElement[24];
		}
		
		MergeThread mergeA=new MergeThread();
		MergeThread mergeB=new MergeThread();
		MergeThread mergeC=new MergeThread();
		WireThread Astart=new WireThread();
		WireThread Bstart=new WireThread();
		WireThread Cstart=new WireThread();
		WireThread AmergeIn=new WireThread();
		WireThread AmergeOut=new WireThread();
		WireThread BmergeIn=new WireThread();
		WireThread BmergeOut=new WireThread();
		WireThread CmergeIn=new WireThread();
		WireThread CmergeOut=new WireThread();
		WireThread endA=new WireThread();
		WireThread endB=new WireThread();
		WireThread endC=new WireThread();
		WireThread Aconnect=new WireThread();
		WireThread Bconnect=new WireThread();
		WireThread Cconnect=new WireThread();
		WireThread AsubBlocked=new WireThread();
		WireThread BsubBlocked=new WireThread();
		WireThread CsubBlocked=new WireThread();

		mergeA.input1=AmergeIn;
		mergeA.input2=Astart;
		mergeA.output=AmergeOut;
		mergeB.output=BmergeOut;
		mergeB.input2=BmergeIn;
		mergeB.input1=Bstart;
		mergeC.input1=Cstart;
		mergeC.input2=CmergeIn;
		mergeC.output=CmergeOut;
		AandB.I2=BmergeOut;
		AandB.O1=Aconnect;
		AandB.SBO1=AmergeIn;
		AandB.I2=BmergeOut;
		AandB.O2=Bconnect;
		AandB.SBO2=BmergeIn;
		AandB.I1=AmergeOut;
		AandB.SBI2=BsubBlocked;
		AandB.SBI1=AsubBlocked;
		BandC.I2=CmergeOut;
		BandC.O2=Cconnect;
		BandC.SBO2=CmergeIn;
		BandC.I1=Bconnect;
		BandC.O1=endB;
		BandC.SBO1=BsubBlocked;
		BandC.SBI2=CsubBlocked;
		AandC.I1=Aconnect;
		AandC.I2=Cconnect;
		AandC.SBO1=AsubBlocked;
		AandC.SBO2=CsubBlocked;
		AandC.O1=endA;
		AandC.O2=endC;
		
		Circuit.circuitElements[0]=AandB;
		Circuit.circuitElements[1]=BandC;
		Circuit.circuitElements[2]=mergeA;
		Circuit.circuitElements[3]=mergeB;
		Circuit.circuitElements[4]=mergeC;
		Circuit.circuitElements[5]=Astart;
		Circuit.circuitElements[6]=Bstart;
		Circuit.circuitElements[7]=Cstart;
		Circuit.circuitElements[8]=AmergeIn;
		Circuit.circuitElements[9]=AmergeOut;
		Circuit.circuitElements[10]=BmergeOut;
		Circuit.circuitElements[11]=BmergeIn;
		Circuit.circuitElements[12]=Bconnect;
		Circuit.circuitElements[13]=BsubBlocked;
		Circuit.circuitElements[14]=CmergeOut;
		Circuit.circuitElements[15]=CmergeIn;
		Circuit.circuitElements[16]=endA;
		Circuit.circuitElements[17]=endB;
		Circuit.circuitElements[18]=endC;
		Circuit.circuitElements[19]=AandC;
		Circuit.circuitElements[20]=Aconnect;
		Circuit.circuitElements[21]=Cconnect;
		Circuit.circuitElements[22]=AsubBlocked;
		Circuit.circuitElements[23]=CsubBlocked;
		
		Astart.setActive(true);
		Bstart.setActive(true);
		Cstart.setActive(true);
	}
	
	/* Creates a circuit which contains 4 events and 4 conflicts. This supports
	 * 2 possible configurations (using the alternative flag), both with a
	 * synchronised version (using the synched flag) */
	private static void create4Way4PairCircuit(boolean synched, boolean alternative) {
		ConflictThread AandB;
		ConflictThread BandC;
		ConflictThread CandD;
		ConflictThread AandD;
		
		if(synched){
			AandB=new ConflictSynchThread("A and B Conflict");
			BandC=new ConflictSynchThread("B and C Conflict"); 
			CandD=new ConflictSynchThread("C and D Conflict");
			AandD=new ConflictSynchThread("A and D Conflict");
			WireThread BprevBlocked=new WireThread();
			WireThread CprevBlocked=new WireThread();
			WireThread AprevBlocked=new WireThread();
			WireThread DprevBlocked=new WireThread();
			Circuit.circuitElements=new CircuitElement[36];
			if(alternative){
				((ConflictSynchThread)AandB).PBI2=BprevBlocked;
				((ConflictSynchThread)BandC).PBO1=BprevBlocked;
				((ConflictSynchThread)CandD).PBI1=CprevBlocked;
				((ConflictSynchThread)BandC).PBO2=CprevBlocked;
				((ConflictSynchThread)AandB).PBO1=AprevBlocked;
				((ConflictSynchThread)CandD).PBO2=DprevBlocked;
				((ConflictSynchThread)AandD).PBI1=AprevBlocked;
				((ConflictSynchThread)AandD).PBI2=DprevBlocked;
			}
			else{
				((ConflictSynchThread)AandB).PBO2=BprevBlocked;
				((ConflictSynchThread)BandC).PBI1=BprevBlocked;
				((ConflictSynchThread)BandC).PBO2=CprevBlocked;
				((ConflictSynchThread)CandD).PBI1=CprevBlocked;
				((ConflictSynchThread)AandB).PBO1=AprevBlocked;
				((ConflictSynchThread)AandD).PBI1=AprevBlocked;
				((ConflictSynchThread)CandD).PBO2=DprevBlocked;
				((ConflictSynchThread)AandD).PBI2=DprevBlocked;
			}
			Circuit.circuitElements[32]=BprevBlocked;
			Circuit.circuitElements[33]=CprevBlocked;
			Circuit.circuitElements[34]=AprevBlocked;
			Circuit.circuitElements[35]=DprevBlocked;
		}
		else{
			AandB=new ConflictThread("A and B Conflict");
			BandC=new ConflictThread("B and C Conflict");
			CandD=new ConflictThread("C and D Conflict"); 
			AandD=new ConflictThread("A and D Conflict");
			Circuit.circuitElements=new CircuitElement[32];
		}
		
		MergeThread mergeA=new MergeThread();
		MergeThread mergeB=new MergeThread();
		MergeThread mergeC=new MergeThread();
		MergeThread mergeD=new MergeThread();
		WireThread Astart=new WireThread();
		WireThread Bstart=new WireThread();
		WireThread Cstart=new WireThread();
		WireThread Dstart=new WireThread();
		WireThread AmergeIn=new WireThread();
		WireThread AmergeOut=new WireThread();
		WireThread BmergeIn=new WireThread();
		WireThread BmergeOut=new WireThread();
		WireThread CmergeIn=new WireThread();
		WireThread CmergeOut=new WireThread();
		WireThread DmergeIn=new WireThread();
		WireThread DmergeOut=new WireThread();
		WireThread endA=new WireThread();
		WireThread endB=new WireThread();
		WireThread endC=new WireThread();	
		WireThread endD=new WireThread();
		WireThread Aconnect=new WireThread();
		WireThread Bconnect=new WireThread();
		WireThread Cconnect=new WireThread();
		WireThread Dconnect=new WireThread();
		WireThread AsubBlocked=new WireThread();
		WireThread BsubBlocked=new WireThread();
		WireThread CsubBlocked=new WireThread();
		WireThread DsubBlocked=new WireThread();

		mergeA.input1=AmergeIn;
		mergeA.input2=Astart;
		mergeA.output=AmergeOut;
		mergeB.input1=Bstart;
		mergeB.input2=BmergeIn;
		mergeB.output=BmergeOut;
		mergeC.input1=Cstart;
		mergeC.input2=CmergeIn;
		mergeC.output=CmergeOut;
		mergeD.input1=Dstart;
		mergeD.input2=DmergeIn;
		mergeD.output=DmergeOut;
		AandB.SBO1=AmergeIn;
		AandB.I1=AmergeOut;	
		BandC.I2=CmergeOut;
		BandC.O2=Cconnect;
		BandC.SBO2=CmergeIn;
		BandC.SBI2=CsubBlocked;
		CandD.I1=Cconnect;
		CandD.I2=DmergeOut;
		CandD.O1=endC;
		CandD.SBO1=CsubBlocked;
		CandD.SBO2=DmergeIn;
		
		if(alternative){
			AandB.I2=Bconnect;
			AandB.SBO2=BsubBlocked;
			AandB.O2=endB;
			AandB.O1=Aconnect;
			AandB.SBI1=AsubBlocked;
			BandC.I1=BmergeOut;
			BandC.SBO1=BmergeIn;
			BandC.SBI1=BsubBlocked;
			BandC.O1=Bconnect;
			CandD.O2=Dconnect;
			CandD.SBI2=DsubBlocked;
			AandD.I1=Aconnect;
			AandD.I2=Dconnect;
			AandD.SBO1=AsubBlocked;
			AandD.SBO2=DsubBlocked;
			AandD.O2=endD;
			AandD.O1=endA;
		}
		else{
			AandB.O2=Bconnect;
			AandB.I2=BmergeOut;
			AandB.SBI2=BsubBlocked;
			AandB.O1=Aconnect;
			AandB.SBI1=AsubBlocked;
			AandB.SBO2=BmergeIn;
			BandC.I1=Bconnect;
			BandC.O1=endB;
			BandC.SBO1=BsubBlocked;
			CandD.O2=Dconnect;
			CandD.SBI2=DsubBlocked;
			AandD.I1=Aconnect;
			AandD.I2=Dconnect;
			AandD.SBO1=AsubBlocked;
			AandD.SBO2=DsubBlocked;
			AandD.O1=endA;
			AandD.O2=endD;
		}
		
		Circuit.circuitElements[0]=AandB;
		Circuit.circuitElements[1]=BandC;
		Circuit.circuitElements[2]=mergeA;
		Circuit.circuitElements[3]=mergeB;
		Circuit.circuitElements[4]=mergeC;
		Circuit.circuitElements[5]=Astart;
		Circuit.circuitElements[6]=Bstart;
		Circuit.circuitElements[7]=Cstart;
		Circuit.circuitElements[8]=AmergeIn;
		Circuit.circuitElements[9]=AmergeOut;
		Circuit.circuitElements[10]=BmergeOut;
		Circuit.circuitElements[11]=BmergeIn;
		Circuit.circuitElements[12]=Bconnect;
		Circuit.circuitElements[13]=BsubBlocked;
		Circuit.circuitElements[14]=CmergeOut;
		Circuit.circuitElements[15]=CmergeIn;
		Circuit.circuitElements[16]=endA;
		Circuit.circuitElements[17]=endB;
		Circuit.circuitElements[18]=endC;
		Circuit.circuitElements[19]=mergeD;
		Circuit.circuitElements[20]=Dstart;
		Circuit.circuitElements[21]=DmergeIn;
		Circuit.circuitElements[22]=DmergeOut;
		Circuit.circuitElements[23]=Cconnect;
		Circuit.circuitElements[24]=CsubBlocked;
		Circuit.circuitElements[25]=endD;
		Circuit.circuitElements[26]=CandD;
		Circuit.circuitElements[27]=AandD;
		Circuit.circuitElements[28]=Aconnect;
		Circuit.circuitElements[29]=Dconnect;
		Circuit.circuitElements[30]=AsubBlocked;
		Circuit.circuitElements[31]=DsubBlocked;
		
		Astart.setActive(true);
		Bstart.setActive(true);
		Cstart.setActive(true);
		Dstart.setActive(true);
	}
	
	/* Creates a trivial circuit which contains 6 events. This does not
	 * contain any conflicts and merely illustrates the trivial nature
	 * of circuits without conflicts (hence this is the ONLY example, as
	 * others are not of interest) */
	private static void create6WayCircuit(){
		WireThread a=new WireThread();
		WireThread b=new WireThread();
		WireThread c=new WireThread();
		WireThread d=new WireThread();
		WireThread e=new WireThread();
		WireThread f=new WireThread();
		ForkThread fork1=new ForkThread();
		ForkThread fork2=new ForkThread();
		JoinThread join=new JoinThread();
		
		fork1.input=a;
		fork1.output1=b;
		fork1.output2=c;
		fork2.input=c;
		fork2.output1=d;
		fork2.output2=e;
		join.input1=d;
		join.input2=e;
		join.output=f;
		
		Circuit.circuitElements=new CircuitElement[9];
		Circuit.circuitElements[0]=fork1;
		Circuit.circuitElements[1]=fork2;
		Circuit.circuitElements[2]=join;
		Circuit.circuitElements[3]=a;
		Circuit.circuitElements[4]=b;
		Circuit.circuitElements[5]=c;
		Circuit.circuitElements[6]=d;
		Circuit.circuitElements[7]=e;
		Circuit.circuitElements[8]=f;
		
		a.setActive(true);
	}
}
