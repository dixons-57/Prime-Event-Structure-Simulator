package Circuit;
import java.awt.Font;
import java.awt.Graphics2D;
import GUI.GraphicsPanel;
import Main.GlobalAttributes;

/* This class contains all hardcoded circuit examples' render logic. 
 * The structural logic for these circuits is contained in the CircuitExamples class.
 * This class is never instantiated. The detailed render logic of each individual example
 * is NOT documented here, as it is irrelevant to program logic. Many variations on
 * the same circuit (alternative configurations, synchronised versions etc.) share
 * the same method but with different parameters to reduce code repetition.
 * Each individual circuit element is responsible for its own rendering, this class
 * supplies locations and sizes.
 */
public class CircuitExamplesRender {

	/* The size of the largest available square area on screen */
	static int size;

	/* The general draw method which is called by the GraphicsPanel
	 * (hence the public attribute). It takes the graphics handle
	 * and also the available size of on-screen area.
	 * It passes these handles to the relevant method depending on
	 * the circuit we are rendering (each circuit has its own method),
	 * along with the attributes which dictate whether to draw
	 * synchronised and/or alternative versions of the circuit
	 */
	public static void draw(Graphics2D g2, int size) {
		CircuitExamplesRender.size=size;
		switch(GlobalAttributes.circuitSelected){
		case 1:
			r3Way2PairConflictCircuit(g2,false,false);
			break;
		case 2:
			r3Way2PairConflictCircuit(g2,true,false);
			break;
		case 3:
			r3Way2PairConflictCircuit(g2,false,true);
			break;
		case 4:
			r3Way2PairConflictCircuit(g2,true,true);
			break;
		case 5:
			r3Way3PairConflictCircuit(g2,false);
			break;
		case 6:
			r3Way3PairConflictCircuit(g2,true);
			break;
		case 7:
			r4Way3PairConflictCircuit(g2,false,false);
			break;
		case 8:
			r4Way3PairConflictCircuit(g2,true,false);
			break;
		case 9:
			r4Way3PairConflictCircuit(g2,false,true);
			break;
		case 10:
			r4Way3PairConflictCircuit(g2,true,true);
			break;
		case 11:
			r4Way4PairConflictCircuit(g2,false,false);
			break;
		case 12:
			r4Way4PairConflictCircuit(g2,true,false);
			break;
		case 13:
			r4Way4PairConflictCircuit(g2,false,true);
			break;
		case 14:
			r4Way4PairConflictCircuit(g2,true,true);
			break;
		case 15:
			r6WayCircuit(g2);
		}
	}

	/* Renders the circuit which contains 3 events and 2 conflicts. This supports
	 * 2 possible configurations (using the alternative flag), both with a
	 * synchronised version (using the synched flag) */
	private static void r3Way2PairConflictCircuit(Graphics2D g2, boolean synched, boolean alternative) {
		int conflictWidth=size/2;
		int conflictHeight=conflictWidth/4;
		int mergeSize=conflictWidth/8;
		int verticalGap=conflictWidth/5;
		int ABx=125*size/1000;
		int BCx=ABx+conflictWidth-conflictWidth/2;
		int ABy;
		int BCy;
		int mergeBx;
		int mergeBy;
		if(alternative){
			ABy=2*size/10;
			BCy=5*size/10;
			mergeBx=BCx+conflictWidth/4-mergeSize/2;
			mergeBy=BCy+conflictHeight+verticalGap;
		}
		else{
			ABy=5*size/10;
			BCy=2*size/10;
			mergeBx=ABx+3*conflictWidth/4-mergeSize/2;
			mergeBy=ABy+conflictHeight+verticalGap;
		}
		int mergeAx=ABx+conflictWidth/4-mergeSize/2;
		int mergeAy=ABy+conflictHeight+verticalGap;
		int mergeCx=BCx+3*conflictWidth/4-mergeSize/2;
		int mergeCy=BCy+conflictHeight+verticalGap;

		GlobalAttributes.middleFont = new Font("Arial", Font.PLAIN, conflictWidth/10);
		GlobalAttributes.smallFont= new Font("Arial", Font.PLAIN, conflictWidth/20);

		if(alternative){
			((WireThread)Circuit.circuitElements[6]).standardDraw(g2,mergeBx+2*mergeSize/3, mergeBy+mergeSize, mergeBx+2*mergeSize/3, GraphicsPanel.global.getHeight());
			((WireThread)Circuit.circuitElements[11]).mergeInputDraw(g2,BCx+conflictWidth/8,BCy+conflictHeight,mergeBx+mergeSize/3,mergeBy+mergeSize,verticalGap/2);
			((WireThread)Circuit.circuitElements[13]).crossDraw(g2,BCx+conflictWidth/8, BCy,ABx+7*conflictWidth/8,ABy+conflictHeight,conflictWidth/10);
			((WireThread)Circuit.circuitElements[17]).standardDraw(g2,ABx+3*conflictWidth/4, 0,ABx+3*conflictWidth/4,ABy);
			if(synched){((WireThread)Circuit.circuitElements[19]).crossDraw(g2,BCx+3*conflictWidth/8, BCy,ABx+5*conflictWidth/8, ABy+conflictHeight,conflictWidth/10);}
		}
		else{
			((WireThread)Circuit.circuitElements[6]).standardDraw(g2,mergeBx+1*mergeSize/3, mergeBy+mergeSize, mergeBx+1*mergeSize/3, GraphicsPanel.global.getHeight());
			((WireThread)Circuit.circuitElements[11]).mergeInputDraw(g2,ABx+7*conflictWidth/8,ABy+conflictHeight,mergeBx+2*mergeSize/3,mergeBy+mergeSize,verticalGap/2);
			((WireThread)Circuit.circuitElements[13]).crossDraw(g2, ABx+7*conflictWidth/8,ABy,BCx+conflictWidth/8, BCy+conflictHeight,conflictWidth/10);
			((WireThread)Circuit.circuitElements[17]).standardDraw(g2,BCx+conflictWidth/4, 0,BCx+conflictWidth/4,BCy);
			if(synched){((WireThread)Circuit.circuitElements[19]).crossDraw(g2,ABx+5*conflictWidth/8, ABy,BCx+3*conflictWidth/8, BCy+conflictHeight,conflictWidth/10);}
		}	
		((WireThread)Circuit.circuitElements[5]).standardDraw(g2,mergeAx+2*mergeSize/3, mergeAy+mergeSize, mergeAx+2*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[7]).standardDraw(g2,mergeCx+1*mergeSize/3, mergeCy+mergeSize, mergeCx+1*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[8]).mergeInputDraw(g2,ABx+conflictWidth/8,ABy+conflictHeight,mergeAx+mergeSize/3,mergeAy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[9]).standardDraw(g2,ABx+conflictWidth/4, ABy+conflictHeight,mergeAx+mergeSize/2, mergeAy);
		((WireThread)Circuit.circuitElements[10]).standardDraw(g2,ABx+3*conflictWidth/4, ABy+conflictHeight,mergeBx+mergeSize/2, mergeBy);
		((WireThread)Circuit.circuitElements[12]).standardDraw(g2,ABx+3*conflictWidth/4, ABy,BCx+conflictWidth/4, BCy+conflictHeight);
		((WireThread)Circuit.circuitElements[14]).standardDraw(g2,BCx+3*conflictWidth/4, BCy+conflictHeight,mergeCx+mergeSize/2, mergeCy);
		((WireThread)Circuit.circuitElements[15]).mergeInputDraw(g2,BCx+7*conflictWidth/8,BCy+conflictHeight,mergeCx+2*mergeSize/3,mergeCy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[16]).standardDraw(g2,ABx+conflictWidth/4, 0,ABx+conflictWidth/4,ABy);
		((WireThread)Circuit.circuitElements[18]).standardDraw(g2,BCx+3*conflictWidth/4, 0,BCx+3*conflictWidth/4,BCy);

		((ConflictThread)Circuit.circuitElements[0]).drawConflict(g2,ABx,ABy,conflictWidth,conflictHeight,synched,"a","b");
		((ConflictThread)Circuit.circuitElements[1]).drawConflict(g2,BCx,BCy,conflictWidth,conflictHeight,synched,"b","c");
		
		((MergeThread)Circuit.circuitElements[2]).drawMerge(g2,mergeAx,mergeAy,mergeSize);
		((MergeThread)Circuit.circuitElements[3]).drawMerge(g2,mergeBx,mergeBy,mergeSize);
		((MergeThread)Circuit.circuitElements[4]).drawMerge(g2,mergeCx,mergeCy,mergeSize);

		g2.setFont(GlobalAttributes.middleFont);
		g2.drawString("a", mergeAx+2*mergeSize/3+size/100, size-size/100);
		if(alternative){
			g2.drawString("b", mergeBx+2*mergeSize/3+size/100, size-size/100);
		}
		else{
			g2.drawString("b", mergeBx+mergeSize/3+size/100, size-size/100);
		}
		g2.drawString("c", mergeCx+mergeSize/3+size/100, size-size/100);
		g2.drawString("a", mergeAx+mergeSize/2+size/100, size/25);
		g2.drawString("b", mergeBx+mergeSize/2+size/100, size/25);
		g2.drawString("c", mergeCx+mergeSize/2+size/100, size/25);
	}
	
	/* Renders the circuit which contains 3 events and 3 conflicts. This supports a
	 * synchronised version (using the synched flag) */
	private static void r3Way3PairConflictCircuit(Graphics2D g2, boolean synched) {
		int conflictWidth=size/3;
		int conflictHeight=conflictWidth/4;
		int mergeSize=conflictWidth/8;
		int verticalGap=conflictWidth/5;
		int ABx=300*size/1000;
		int BCx=650*size/1000;
		int ACx=ABx;
		int ABy=7*size/10;
		int BCy=5*size/10;
		int ACy=3*size/10;
		int mergeAx=ABx+conflictWidth/4-mergeSize/2;
		int mergeBx=ABx+3*conflictWidth/4-mergeSize/2;
		int mergeBy=ABy+conflictHeight+verticalGap;
		int mergeAy=ABy+conflictHeight+verticalGap;
		int mergeCx=BCx+3*conflictWidth/4-mergeSize/2;
		int mergeCy=BCy+conflictHeight+verticalGap;

		GlobalAttributes.middleFont = new Font("Arial", Font.PLAIN, conflictWidth/10);
		GlobalAttributes.smallFont= new Font("Arial", Font.PLAIN, conflictWidth/20);

		((WireThread)Circuit.circuitElements[5]).standardDraw(g2,mergeAx+2*mergeSize/3, mergeAy+mergeSize, mergeAx+2*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[6]).standardDraw(g2,mergeBx+1*mergeSize/3, mergeBy+mergeSize, mergeBx+1*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[7]).standardDraw(g2,mergeCx+1*mergeSize/3, mergeCy+mergeSize, mergeCx+1*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[8]).mergeInputDraw(g2,ABx+conflictWidth/8,ABy+conflictHeight,mergeAx+mergeSize/3,mergeAy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[9]).standardDraw(g2,ABx+conflictWidth/4, ABy+conflictHeight,mergeAx+mergeSize/2, mergeAy);
		((WireThread)Circuit.circuitElements[10]).standardDraw(g2,ABx+3*conflictWidth/4, ABy+conflictHeight,mergeBx+mergeSize/2, mergeBy);
		((WireThread)Circuit.circuitElements[11]).mergeInputDraw(g2,ABx+7*conflictWidth/8,ABy+conflictHeight,mergeBx+2*mergeSize/3,mergeBy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[12]).crossDraw(g2,ABx+3*conflictWidth/4, ABy,BCx+conflictWidth/4, BCy+conflictHeight,conflictWidth/10);
		((WireThread)Circuit.circuitElements[13]).crossDraw(g2, ABx+7*conflictWidth/8,ABy,BCx+conflictWidth/8, BCy+conflictHeight,conflictWidth/25);
		((WireThread)Circuit.circuitElements[14]).standardDraw(g2,BCx+3*conflictWidth/4, BCy+conflictHeight,mergeCx+mergeSize/2, mergeCy);
		((WireThread)Circuit.circuitElements[15]).mergeInputDraw(g2,BCx+7*conflictWidth/8,BCy+conflictHeight,mergeCx+2*mergeSize/3,mergeCy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[16]).standardDraw(g2,ACx+conflictWidth/4, 0,ACx+conflictWidth/4,ACy);
		((WireThread)Circuit.circuitElements[17]).crossDraw(g2,BCx+conflictWidth/4, BCy,ACx+3*conflictWidth/4,0,2*conflictWidth/5);
		((WireThread)Circuit.circuitElements[18]).crossDraw(g2,ACx+3*conflictWidth/4, ACy,BCx+3*conflictWidth/4,0,conflictWidth/5);
		((WireThread)Circuit.circuitElements[20]).standardDraw(g2,ABx+conflictWidth/4, ABy, ACx+conflictWidth/4, ACy+conflictHeight);
		((WireThread)Circuit.circuitElements[22]).standardDraw(g2,ABx+conflictWidth/8, ABy, ACx+conflictWidth/8, ACy+conflictHeight);
		((WireThread)Circuit.circuitElements[21]).crossDraw(g2,BCx+3*conflictWidth/4, BCy,ACx+3*conflictWidth/4,ACy+conflictHeight,conflictWidth/25);
		((WireThread)Circuit.circuitElements[23]).crossDraw(g2,BCx+7*conflictWidth/8, BCy,ACx+7*conflictWidth/8,ACy+conflictHeight,conflictWidth/25);	
		if(synched){
			((WireThread)Circuit.circuitElements[24]).standardDraw(g2,ACx+3*conflictWidth/8, ABy,ACx+3*conflictWidth/8, ACy+conflictHeight);
			((WireThread)Circuit.circuitElements[25]).crossDraw(g2,ABx+5*conflictWidth/8, ABy,BCx+3*conflictWidth/8, BCy+conflictHeight,2*conflictWidth/15);
			((WireThread)Circuit.circuitElements[26]).crossDraw(g2,BCx+5*conflictWidth/8, BCy,ACx+5*conflictWidth/8,ACy+conflictHeight,conflictWidth/20);
		}

		((ConflictThread)Circuit.circuitElements[0]).drawConflict(g2,ABx,ABy,conflictWidth,conflictHeight,synched,"a","b");
		((ConflictThread)Circuit.circuitElements[1]).drawConflict(g2,BCx,BCy,conflictWidth,conflictHeight,synched,"b","c");
		((ConflictThread)Circuit.circuitElements[19]).drawConflict(g2,ACx,ACy,conflictWidth,conflictHeight,synched,"a","c");
		
		((MergeThread)Circuit.circuitElements[2]).drawMerge(g2,mergeAx,mergeAy,mergeSize);
		((MergeThread)Circuit.circuitElements[3]).drawMerge(g2,mergeBx,mergeBy,mergeSize);
		((MergeThread)Circuit.circuitElements[4]).drawMerge(g2,mergeCx,mergeCy,mergeSize);

		g2.setFont(GlobalAttributes.middleFont);
		g2.drawString("a", mergeAx+2*mergeSize/3+size/100, size-size/100);
		g2.drawString("b", mergeBx+mergeSize/3+size/100, size-size/100);
		g2.drawString("c", mergeCx+mergeSize/3+size/100, size-size/100);
		g2.drawString("a", mergeAx+mergeSize/2+size/100, size/25);
		g2.drawString("b", mergeBx+mergeSize/2+size/100, size/25);
		g2.drawString("c", mergeCx+mergeSize/2+size/100, size/25);
	}

	/* Renders the circuit which contains 4 events and 3 conflicts. This supports
	 * 2 possible configurations (using the alternative flag), both with a
	 * synchronised version (using the synched flag) */
	private static void r4Way3PairConflictCircuit(Graphics2D g2, boolean synched, boolean alternative) {
		int conflictWidth=size/4;
		int conflictHeight=conflictWidth/4;
		int mergeSize=conflictWidth/8;
		int verticalGap=conflictWidth/5;
		int ABx=250*size/1000;
		int BCx=ABx+conflictWidth-conflictWidth/2;
		int ABy;
		int BCy;
		int CDy;
		int mergeBy;
		if(alternative){
			ABy=35*size/100;
			BCy=55*size/100;
			CDy=35*size/100;
			mergeBy=BCy+conflictHeight+verticalGap;
		}
		else{
			ABy=7*size/10;
			BCy=45*size/100;
			CDy=2*size/10;
			mergeBy=ABy+conflictHeight+verticalGap;
		}
		int mergeBx=ABx+3*conflictWidth/4-mergeSize/2;
		int mergeAx=ABx+conflictWidth/4-mergeSize/2;
		int mergeAy=ABy+conflictHeight+verticalGap;
		int mergeCx=BCx+3*conflictWidth/4-mergeSize/2;
		int mergeCy=BCy+conflictHeight+verticalGap;
		int CDx=BCx+conflictWidth-conflictWidth/2;
		int mergeDx=CDx+3*conflictWidth/4-mergeSize/2;
		int mergeDy=CDy+conflictHeight+verticalGap;

		GlobalAttributes.middleFont = new Font("Arial", Font.PLAIN, conflictWidth/10);
		GlobalAttributes.smallFont= new Font("Arial", Font.PLAIN, conflictWidth/20);

		if(alternative){
			((WireThread)Circuit.circuitElements[6]).standardDraw(g2,mergeBx+2*mergeSize/3, mergeBy+mergeSize, mergeBx+2*mergeSize/3, GraphicsPanel.global.getHeight());
			((WireThread)Circuit.circuitElements[10]).standardDraw(g2,BCx+conflictWidth/4, BCy+conflictHeight,mergeBx+mergeSize/2, mergeBy);
			((WireThread)Circuit.circuitElements[11]).mergeInputDraw(g2,BCx+conflictWidth/8,BCy+conflictHeight,mergeBx+1*mergeSize/3,mergeBy+mergeSize,verticalGap/2);
			((WireThread)Circuit.circuitElements[12]).standardDraw(g2,BCx+conflictWidth/4, BCy,ABx+3*conflictWidth/4, ABy+conflictHeight);
			((WireThread)Circuit.circuitElements[13]).crossDraw(g2,BCx+conflictWidth/8, BCy,ABx+7*conflictWidth/8,ABy+conflictHeight,conflictWidth/10);
			((WireThread)Circuit.circuitElements[17]).standardDraw(g2,ABx+3*conflictWidth/4, 0,ABx+3*conflictWidth/4,ABy);
			if(synched){
				((WireThread)Circuit.circuitElements[27]).crossDraw(g2,BCx+3*conflictWidth/8, BCy,ABx+5*conflictWidth/8, ABy+conflictHeight,conflictWidth/10);
				((WireThread)Circuit.circuitElements[28]).crossDraw(g2,BCx+5*conflictWidth/8, BCy,CDx+3*conflictWidth/8, CDy+conflictHeight,conflictWidth/10);
			}
		}
		else{
			((WireThread)Circuit.circuitElements[6]).standardDraw(g2,mergeBx+1*mergeSize/3, mergeBy+mergeSize, mergeBx+1*mergeSize/3, GraphicsPanel.global.getHeight());
			((WireThread)Circuit.circuitElements[10]).standardDraw(g2,ABx+3*conflictWidth/4, ABy+conflictHeight,mergeBx+mergeSize/2, mergeBy);
			((WireThread)Circuit.circuitElements[11]).mergeInputDraw(g2,ABx+7*conflictWidth/8,ABy+conflictHeight,mergeBx+2*mergeSize/3,mergeBy+mergeSize,verticalGap/2);
			((WireThread)Circuit.circuitElements[12]).standardDraw(g2,ABx+3*conflictWidth/4, ABy,BCx+conflictWidth/4, BCy+conflictHeight);
			((WireThread)Circuit.circuitElements[13]).crossDraw(g2, ABx+7*conflictWidth/8,ABy,BCx+conflictWidth/8, BCy+conflictHeight,conflictWidth/10);
			((WireThread)Circuit.circuitElements[17]).standardDraw(g2,BCx+conflictWidth/4, 0,BCx+conflictWidth/4,BCy);
			if(synched){
				((WireThread)Circuit.circuitElements[27]).crossDraw(g2,ABx+5*conflictWidth/8, ABy,BCx+3*conflictWidth/8, BCy+conflictHeight,conflictWidth/10);
				((WireThread)Circuit.circuitElements[28]).crossDraw(g2,BCx+5*conflictWidth/8, BCy,CDx+3*conflictWidth/8, CDy+conflictHeight,conflictWidth/10);
			}
		}
		((WireThread)Circuit.circuitElements[5]).standardDraw(g2,mergeAx+2*mergeSize/3, mergeAy+mergeSize, mergeAx+2*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[7]).standardDraw(g2,mergeCx+1*mergeSize/3, mergeCy+mergeSize, mergeCx+1*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[8]).mergeInputDraw(g2,ABx+conflictWidth/8,ABy+conflictHeight,mergeAx+mergeSize/3,mergeAy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[9]).standardDraw(g2,ABx+conflictWidth/4, ABy+conflictHeight,mergeAx+mergeSize/2, mergeAy);
		((WireThread)Circuit.circuitElements[14]).standardDraw(g2,BCx+3*conflictWidth/4, BCy+conflictHeight,mergeCx+mergeSize/2, mergeCy);
		((WireThread)Circuit.circuitElements[15]).mergeInputDraw(g2,BCx+7*conflictWidth/8,BCy+conflictHeight,mergeCx+2*mergeSize/3,mergeCy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[16]).standardDraw(g2,ABx+conflictWidth/4, 0,ABx+conflictWidth/4,ABy);
		((WireThread)Circuit.circuitElements[18]).standardDraw(g2,CDx+conflictWidth/4, CDy,CDx+conflictWidth/4,0);
		((WireThread)Circuit.circuitElements[20]).standardDraw(g2,mergeDx+mergeSize/3, mergeDy+mergeSize,mergeDx+mergeSize/2,GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[21]).mergeInputDraw(g2,CDx+7*conflictWidth/8,CDy+conflictHeight,mergeDx+2*mergeSize/3,mergeDy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[22]).standardDraw(g2,mergeDx+mergeSize/2, mergeDy,CDx+3*conflictWidth/4,CDy+conflictHeight);
		((WireThread)Circuit.circuitElements[23]).standardDraw(g2,CDx+conflictWidth/4, CDy+conflictHeight,BCx+3*conflictWidth/4,BCy);
		((WireThread)Circuit.circuitElements[24]).crossDraw(g2,BCx+7*conflictWidth/8, BCy,CDx+conflictWidth/8, CDy+conflictHeight,conflictWidth/10);
		((WireThread)Circuit.circuitElements[25]).standardDraw(g2,CDx+3*conflictWidth/4, CDy,CDx+3*conflictWidth/4,0);

		((ConflictThread)Circuit.circuitElements[0]).drawConflict(g2,ABx,ABy,conflictWidth,conflictHeight,synched,"a","b");
		((ConflictThread)Circuit.circuitElements[1]).drawConflict(g2,BCx,BCy,conflictWidth,conflictHeight,synched,"b","c");
		((ConflictThread)Circuit.circuitElements[26]).drawConflict(g2,CDx,CDy,conflictWidth,conflictHeight,synched,"c","d");
		
		((MergeThread)Circuit.circuitElements[2]).drawMerge(g2,mergeAx,mergeAy,mergeSize);
		((MergeThread)Circuit.circuitElements[3]).drawMerge(g2,mergeBx,mergeBy,mergeSize);
		((MergeThread)Circuit.circuitElements[4]).drawMerge(g2,mergeCx,mergeCy,mergeSize);
		((MergeThread)Circuit.circuitElements[19]).drawMerge(g2,mergeDx,mergeDy,mergeSize);
		
		g2.setFont(GlobalAttributes.middleFont);
		g2.drawString("a", mergeAx+2*mergeSize/3+size/100, size-size/100);
		if(alternative){
			g2.drawString("b", mergeBx+2*mergeSize/3+size/100, size-size/100);
		}
		else{
			g2.drawString("b", mergeBx+mergeSize/3+size/100, size-size/100);
		}
		g2.drawString("c", mergeCx+mergeSize/3+size/100, size-size/100);
		g2.drawString("d", mergeDx+mergeSize/3+size/100, size-size/100);
		g2.drawString("a", mergeAx+mergeSize/2+size/100, size/25);
		g2.drawString("b", mergeBx+mergeSize/2+size/100, size/25);
		g2.drawString("c", mergeCx+mergeSize/2+size/100, size/25);
		g2.drawString("d", mergeDx+mergeSize/2+size/100, size/25);
	}

	/* Creates a circuit which contains 4 events and 4 conflicts. This supports
	 * 2 possible configurations (using the alternative flag), both with a
	 * synchronised version (using the synched flag) */
	private static void r4Way4PairConflictCircuit(Graphics2D g2, boolean synched, boolean alternative) {
		int conflictWidth=size/5;
		int conflictHeight=conflictWidth/4;
		int mergeSize=conflictWidth/8;
		int verticalGap=conflictWidth/5;
		int ABx=250*size/1000;
		int ABy;
		int BCx=ABx+conflictWidth-conflictWidth/2;
		int BCy;
		int CDx=BCx+conflictWidth-conflictWidth/2;
		int CDy;
		int ADx=125*size/1000;
		int ADy;
		int mergeBy;
		if(alternative){
			ABy=5*size/10;
			BCy=8*size/10;
			CDy=5*size/10;
			ADy=2*size/10;
			mergeBy=BCy+conflictHeight+verticalGap;
		}
		else{
			ABy=8*size/10;
			BCy=6*size/10;
			CDy=4*size/10;
			ADy=2*size/10;
			mergeBy=ABy+conflictHeight+verticalGap;
		}
		int mergeBx=ABx+3*conflictWidth/4-mergeSize/2;
		int mergeAx=ABx+conflictWidth/4-mergeSize/2;
		int mergeAy=ABy+conflictHeight+verticalGap;
		int mergeCx=BCx+3*conflictWidth/4-mergeSize/2;
		int mergeCy=BCy+conflictHeight+verticalGap;
		int mergeDx=CDx+3*conflictWidth/4-mergeSize/2;
		int mergeDy=CDy+conflictHeight+verticalGap;

		GlobalAttributes.middleFont = new Font("Arial", Font.PLAIN, conflictWidth/10);
		GlobalAttributes.smallFont= new Font("Arial", Font.PLAIN, conflictWidth/20);

		if(alternative){
			((WireThread)Circuit.circuitElements[6]).standardDraw(g2,mergeBx+2*mergeSize/3, mergeBy+mergeSize, mergeBx+2*mergeSize/3, GraphicsPanel.global.getHeight());
			((WireThread)Circuit.circuitElements[10]).standardDraw(g2,BCx+conflictWidth/4, BCy+conflictHeight,mergeBx+mergeSize/2, mergeBy);
			((WireThread)Circuit.circuitElements[11]).mergeInputDraw(g2,BCx+conflictWidth/8,BCy+conflictHeight,mergeBx+1*mergeSize/3,mergeBy+mergeSize,verticalGap/2);
			((WireThread)Circuit.circuitElements[12]).standardDraw(g2,BCx+conflictWidth/4, BCy,ABx+3*conflictWidth/4, ABy+conflictHeight);
			((WireThread)Circuit.circuitElements[13]).crossDraw(g2,BCx+conflictWidth/8, BCy,ABx+7*conflictWidth/8,ABy+conflictHeight,conflictWidth/10);
			((WireThread)Circuit.circuitElements[17]).standardDraw(g2,ABx+3*conflictWidth/4, 0,ABx+3*conflictWidth/4,ABy);
			if(synched){
				((WireThread)Circuit.circuitElements[32]).crossDraw(g2,BCx+3*conflictWidth/8, BCy,ABx+5*conflictWidth/8, ABy+conflictHeight,conflictWidth/5);
			}
		}
		else{
			((WireThread)Circuit.circuitElements[6]).standardDraw(g2,mergeBx+1*mergeSize/3, mergeBy+mergeSize, mergeBx+1*mergeSize/3, GraphicsPanel.global.getHeight());
			((WireThread)Circuit.circuitElements[10]).standardDraw(g2,ABx+3*conflictWidth/4, ABy+conflictHeight,mergeBx+mergeSize/2, mergeBy);
			((WireThread)Circuit.circuitElements[11]).mergeInputDraw(g2,ABx+7*conflictWidth/8,ABy+conflictHeight,mergeBx+2*mergeSize/3,mergeBy+mergeSize,verticalGap/2);
			((WireThread)Circuit.circuitElements[12]).standardDraw(g2,ABx+3*conflictWidth/4, ABy,BCx+conflictWidth/4, BCy+conflictHeight);
			((WireThread)Circuit.circuitElements[13]).crossDraw(g2, ABx+7*conflictWidth/8,ABy,BCx+conflictWidth/8, BCy+conflictHeight,conflictWidth/10);
			((WireThread)Circuit.circuitElements[17]).standardDraw(g2,BCx+conflictWidth/4, 0,BCx+conflictWidth/4,BCy);
			if(synched){
				((WireThread)Circuit.circuitElements[32]).crossDraw(g2,ABx+5*conflictWidth/8, ABy,BCx+3*conflictWidth/8, BCy+conflictHeight,conflictWidth/5);
			}
		}
		if(synched){
			((WireThread)Circuit.circuitElements[33]).crossDraw(g2,BCx+5*conflictWidth/8, BCy,CDx+3*conflictWidth/8, CDy+conflictHeight,conflictWidth/5);
			((WireThread)Circuit.circuitElements[34]).crossDraw(g2,ABx+3*conflictWidth/8, ABy,ADx+3*conflictWidth/8, ADy+conflictHeight,conflictWidth/10);
			((WireThread)Circuit.circuitElements[35]).crossDraw(g2,CDx+5*conflictWidth/8, CDy,ADx+5*conflictWidth/8, ADy+conflictHeight,conflictWidth/10);
		}
		((WireThread)Circuit.circuitElements[5]).standardDraw(g2,mergeAx+2*mergeSize/3, mergeAy+mergeSize, mergeAx+2*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[7]).standardDraw(g2,mergeCx+1*mergeSize/3, mergeCy+mergeSize, mergeCx+1*mergeSize/3, GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[8]).mergeInputDraw(g2,ABx+conflictWidth/8,ABy+conflictHeight,mergeAx+mergeSize/3,mergeAy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[9]).standardDraw(g2,ABx+conflictWidth/4, ABy+conflictHeight,mergeAx+mergeSize/2, mergeAy);
		((WireThread)Circuit.circuitElements[14]).standardDraw(g2,BCx+3*conflictWidth/4, BCy+conflictHeight,mergeCx+mergeSize/2, mergeCy);
		((WireThread)Circuit.circuitElements[15]).mergeInputDraw(g2,BCx+7*conflictWidth/8,BCy+conflictHeight,mergeCx+2*mergeSize/3,mergeCy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[16]).standardDraw(g2,ADx+conflictWidth/4, 0,ADx+conflictWidth/4,ADy);
		((WireThread)Circuit.circuitElements[18]).standardDraw(g2,CDx+conflictWidth/4, CDy,CDx+conflictWidth/4,0);
		((WireThread)Circuit.circuitElements[20]).standardDraw(g2,mergeDx+mergeSize/3, mergeDy+mergeSize,mergeDx+mergeSize/2,GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[21]).mergeInputDraw(g2,CDx+7*conflictWidth/8,CDy+conflictHeight,mergeDx+2*mergeSize/3,mergeDy+mergeSize,verticalGap/2);
		((WireThread)Circuit.circuitElements[22]).standardDraw(g2,mergeDx+mergeSize/2, mergeDy,CDx+3*conflictWidth/4,CDy+conflictHeight);
		((WireThread)Circuit.circuitElements[23]).standardDraw(g2,CDx+conflictWidth/4, CDy+conflictHeight,BCx+3*conflictWidth/4,BCy);
		((WireThread)Circuit.circuitElements[24]).crossDraw(g2,BCx+7*conflictWidth/8, BCy,CDx+1*conflictWidth/8, CDy+conflictHeight,conflictWidth/10);
		((WireThread)Circuit.circuitElements[25]).crossDraw(g2,ADx+3*conflictWidth/4,ADy,CDx+3*conflictWidth/4, 0,4*conflictWidth/10);
		((WireThread)Circuit.circuitElements[28]).crossDraw(g2,ABx+conflictWidth/4,ABy,ADx+conflictWidth/4, ADy+conflictHeight,conflictWidth/10);
		((WireThread)Circuit.circuitElements[29]).crossDraw(g2,CDx+3*conflictWidth/4,CDy,ADx+3*conflictWidth/4, ADy+conflictHeight,conflictWidth/10);
		((WireThread)Circuit.circuitElements[30]).crossDraw(g2,ABx+conflictWidth/8,ABy,ADx+conflictWidth/8, ADy+conflictHeight,conflictWidth/10); 
		((WireThread)Circuit.circuitElements[31]).crossDraw(g2,CDx+7*conflictWidth/8,CDy,ADx+7*conflictWidth/8, ADy+conflictHeight,conflictWidth/10); 

		((ConflictThread)Circuit.circuitElements[0]).drawConflict(g2,ABx,ABy,conflictWidth,conflictHeight,synched,"a","b");
		((ConflictThread)Circuit.circuitElements[1]).drawConflict(g2,BCx,BCy,conflictWidth,conflictHeight,synched,"b","c");
		((ConflictThread)Circuit.circuitElements[26]).drawConflict(g2,CDx,CDy,conflictWidth,conflictHeight,synched,"c","d");
		((ConflictThread)Circuit.circuitElements[27]).drawConflict(g2,ADx,ADy,conflictWidth,conflictHeight,synched,"a","d");
		
		((MergeThread)Circuit.circuitElements[2]).drawMerge(g2,mergeAx,mergeAy,mergeSize);
		((MergeThread)Circuit.circuitElements[3]).drawMerge(g2,mergeBx,mergeBy,mergeSize);
		((MergeThread)Circuit.circuitElements[4]).drawMerge(g2,mergeCx,mergeCy,mergeSize);
		((MergeThread)Circuit.circuitElements[19]).drawMerge(g2,mergeDx,mergeDy,mergeSize);
		
		g2.setFont(GlobalAttributes.middleFont);
		g2.drawString("a", mergeAx+2*mergeSize/3+size/100, size-size/100);
		if(alternative){
			g2.drawString("b", mergeBx+2*mergeSize/3+size/100, size-size/100);
		}
		else{
			g2.drawString("b", mergeBx+mergeSize/3+size/100, size-size/100);
		}
		g2.drawString("c", mergeCx+mergeSize/3+size/100, size-size/100);
		g2.drawString("d", mergeDx+mergeSize/3+size/100, size-size/100);
		g2.drawString("a", ADx+conflictWidth/4+size/100, size/25);
		g2.drawString("b", BCx+conflictWidth/4+size/100, size/25);
		g2.drawString("c", CDx+conflictWidth/4+size/100, size/25);
		g2.drawString("d", CDx+3*conflictWidth/4+size/100, size/25);
	}
	
	/* Renders the trivial circuit which contains 6 events */
	private static void r6WayCircuit(Graphics2D g2){
		int fork1x=size/2;
		int fork1y=3*size/4;
		int fork2x=2*size/3;
		int fork2y=size/2;
		int joinx=2*size/3;
		int joiny=size/4;
		int elementSize=size/10;
		
		GlobalAttributes.middleFont = new Font("Arial", Font.PLAIN, size/13);
		
		((WireThread)Circuit.circuitElements[3]).standardDraw(g2,fork1x+elementSize/2,fork1y+elementSize, fork1x+elementSize/2,GraphicsPanel.global.getHeight());
		((WireThread)Circuit.circuitElements[4]).standardDraw(g2,fork1x+elementSize/3,fork1y, fork1x+elementSize/3,0);
		((WireThread)Circuit.circuitElements[5]).crossDraw(g2,fork1x+2*elementSize/3,fork1y, fork2x+elementSize/2,fork2y+elementSize,elementSize/3);
		((WireThread)Circuit.circuitElements[6]).standardDraw(g2,fork2x+elementSize/3,fork2y, joinx+elementSize/3,joiny+elementSize);
		((WireThread)Circuit.circuitElements[7]).standardDraw(g2,fork2x+2*elementSize/3,fork2y, joinx+2*elementSize/3,joiny+elementSize);
		((WireThread)Circuit.circuitElements[8]).standardDraw(g2,joinx+elementSize/2,joiny, joinx+elementSize/2,0);
		
		((ForkThread)Circuit.circuitElements[0]).drawFork(g2,fork1x,fork1y,elementSize);
		((ForkThread)Circuit.circuitElements[1]).drawFork(g2,fork2x,fork2y,elementSize);
		((JoinThread)Circuit.circuitElements[2]).drawJoin(g2,joinx,joiny,elementSize);
		
		g2.drawString("a", fork1x+elementSize/2+size/100, size-size/100);
		g2.drawString("b", fork1x+elementSize/3+size/100, size/12);
		g2.drawString("c", (fork2x-(fork1x+elementSize))/2+fork1x+elementSize/2, (fork1y-(fork2y+elementSize))/2+fork2y+elementSize);
		g2.drawString("d", fork2x+elementSize/3-size/20, (fork2y-(joiny+elementSize))/2+joiny+elementSize);
		g2.drawString("e", fork2x+2*elementSize/3+size/100, (fork2y-(joiny+elementSize))/2+joiny+elementSize);
		g2.drawString("f", joinx+elementSize/2+size/100, size/12);
	}
	
}
