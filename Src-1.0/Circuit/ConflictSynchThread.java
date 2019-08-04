package Circuit;

import GUI.GraphicsPanel;
import Main.GlobalAttributes;

/* This class represents the delay-insensitive synchronised conflict-resolution 
 * element. It is a subclass of the ConflictThread class. It uses all parent methods
 * except for run(), of which it has its own override, as all execution semantics
 * are identical except for the initial state (state 0). It uses all parent attributes
 * as well as a few extra input/output wire attributes - sending the kill and unpause 
 * signals will cause it to skip over all execution statements and then terminate
 * 
 * For CCS semantics of this element's behaviour, see the Help window in the program */
class ConflictSynchThread extends ConflictThread{

	/* The additional inputs for this sub-element - PBI 1 and 2 */
	WireThread PBI1;
	WireThread PBI2;

	/* The additional outputs for this sub-element - PBO 1 and 2 */
	WireThread PBO1;
	WireThread PBO2;

	/* Call the parent constructor */
	ConflictSynchThread(String name){
		super(name);
	}

	/* Execution logic */
	@Override
	public void run(){

		/* Infinite loop until kill signal */
		while(!kill){

			/* If in the initial state */
			if(state==0){

				/* If both PBI are connected */
				if(PBI1!=null && PBI2!=null){

					/* Wait until a correct input arrives */
					while(((!I1.hasArrived() && !PBI2.hasArrived() &&
							!I2.hasArrived() && !PBI1.hasArrived() &&
							!I1.hasArrived() && !I2.hasArrived()) || paused) &&!kill){
						Thread.yield();
					}
				}

				/* If only PBI1 is connected */
				else if(PBI1!=null){

					/* Wait until a correct input arrives */
					while(((!I2.hasArrived() && !PBI1.hasArrived() &&
							!I1.hasArrived() && !I2.hasArrived()) || paused) &&!kill){
						Thread.yield();
					}
				}

				/* If only PBI2 is connected */
				else if(PBI2!=null){

					/* Wait until a correct input arrives */
					while(((!I1.hasArrived() && !PBI2.hasArrived() &&
							!I1.hasArrived() && !I2.hasArrived()) || paused) &&!kill){
						Thread.yield();
					}
				}

				/* If no PBIs are connected */
				else{

					/* Wait until a correct input arrives */
					while(((!I1.hasArrived() && !I2.hasArrived()) || paused) &&!kill){
						Thread.yield();
					}
				}

				if(!kill){

					/* Records which input is initially processed */
					int pick=0;

					/* If all four possible inputs have arrived, then select
					 * a winner arbitrarily */
					if(I1.hasArrived()&&I2.hasArrived()&&
							PBI1!=null&& PBI1.hasArrived()&&
							PBI2!=null&& PBI2.hasArrived()){
						if(GlobalAttributes.random()<0.25f){
							pick=1;
						}
						else if(GlobalAttributes.random()<0.5f){
							pick=2;
						}
						else if(GlobalAttributes.random()<0.75f){
							pick=3;
						}
						else if(GlobalAttributes.random()<1.0f){
							pick=4;
						}
					}
					/* Else if I1, I2 and PBI1 have arrived, select
					 * a winner arbitrarily */
					else if(I1.hasArrived()&&I2.hasArrived()&&
							PBI1!=null&&PBI1.hasArrived()){
						if(GlobalAttributes.random()<0.333333f){
							pick=1;
						}
						else if(GlobalAttributes.random()<0.666666f){
							pick=2;
						}
						else if(GlobalAttributes.random()<1.0f){
							pick=3;
						}
					}
					/* Else if I1, I2 and PBI2 have arrived, select
					 * a winner arbitrarily */
					else if(I1.hasArrived()&&I2.hasArrived()&&
							PBI2!=null&&PBI2.hasArrived()){
						if(GlobalAttributes.random()<0.333333f){
							pick=1;
						}
						else if(GlobalAttributes.random()<0.666666f){
							pick=2;
						}
						else if(GlobalAttributes.random()<1.0f){
							pick=4;
						}
					}
					/* Else if I1, PBI1 and PBI2 have arrived, select
					 * a winner arbitrarily */
					else if(I1.hasArrived()&&PBI1!=null&&PBI1.hasArrived()&&
							PBI2!=null&&PBI2.hasArrived()){
						if(GlobalAttributes.random()<0.333333f){
							pick=1;
						}
						else if(GlobalAttributes.random()<0.666666f){
							pick=3;
						}
						else if(GlobalAttributes.random()<1.0f){
							pick=4;
						}
					}
					/* Else if I2, PBI1 and PBI2 have arrived, select
					 * a winner arbitrarily */
					else if(I2.hasArrived()&&PBI1!=null&&PBI1.hasArrived()&&
							PBI2!=null&&PBI2.hasArrived()){
						if(GlobalAttributes.random()<0.333333f){
							pick=2;
						}
						else if(GlobalAttributes.random()<0.666666f){
							pick=3;
						}
						else if(GlobalAttributes.random()<1.0f){
							pick=4;
						}
					}
					/* Else if I1 and I2 have arrived, select
					 * a winner arbitrarily */
					else if(I1.hasArrived()&&I2.hasArrived()){
						if(GlobalAttributes.random()<0.5f){
							pick=1;
						}
						else{
							pick=2;
						}
					}
					/* Else if I1 and PBI1 have arrived, select
					 * a winner arbitrarily */
					else if(I1.hasArrived()&&PBI1!=null&&PBI1.hasArrived()){
						if(GlobalAttributes.random()<0.5f){
							pick=1;
						}
						else{
							pick=3;
						}
					}
					/* Else if I1 and PBI2 have arrived, select
					 * a winner arbitrarily */
					else if(I1.hasArrived()&&PBI2!=null&&PBI2.hasArrived()){
						if(GlobalAttributes.random()<0.5f){
							pick=1;
						}
						else{
							pick=4;
						}
					}
					/* Else if I2 and PBI1 have arrived, select
					 * a winner arbitrarily */
					else if(I2.hasArrived()&&PBI1!=null&&PBI1.hasArrived()){
						if(GlobalAttributes.random()<0.5f){
							pick=2;
						}
						else{
							pick=3;
						}
					}
					/* Else if I2 and PBI2 have arrived, select
					 * a winner arbitrarily */
					else if(I2.hasArrived()&&PBI2!=null&&PBI2.hasArrived()){
						if(GlobalAttributes.random()<0.5f){
							pick=2;
						}
						else{
							pick=4;
						}
					}
					/* Else if I1 has arrived, select it */
					else if(I1.hasArrived()){
						pick=1;
					}
					/* Else if I2 has arrived, select it */
					else if(I2.hasArrived()){
						pick=2;
					}
					/* Else if PBI1 has arrived, select it */
					else if(PBI1!=null && PBI1.hasArrived()){
						pick=3;
					}
					/* Else if PBI2 has arrived, select it */
					else if(PBI2!=null && PBI2.hasArrived()){
						pick=4;
					}

					boolean blocked=false;

					/* If processing I1, set it to become
					 * inactive and then wait for I2 or PBI2 */
					if(pick==1){
						I1.setActive(false);
						processing=true;
						if(render){
							GraphicsPanel.global.repaint();
						}

						if(PBI2!=null){
							/* Wait for I2 or PBI2 to become active */
							while(((!I2.hasArrived() && !PBI2.hasArrived()) || paused) && !kill){
								Thread.yield();
							}
						}
						else{
							/* Wait for I2 to become active */
							while((!I2.hasArrived() || paused) && !kill){
								Thread.yield();
							}
						}

						if(!kill){

							/* Lower the signal on I2 if I2 is active */
							if(I2.hasArrived()){
								I2.setActive(false);
							}

							/* Lower the signal on PBI2 if it is active
							 * and record the fact that I2 was clearly
							 * blocked */
							else if(PBI2.hasArrived()){
								PBI2.setActive(false);
								blocked=true;
							}
							if(render){
								GraphicsPanel.global.repaint();
							}

							/* Wait for a random amount of time between 0 and maxWait milliseconds */
							int wait=(int)(GlobalAttributes.random()*((float)GlobalAttributes.maxWait));

							/* Record the current system time */
							long time=System.currentTimeMillis();

							/* If the correct amount of time to wait hasn't been reached,
							 * and the thread has not been killed, then wait and yield the CPU */
							while((System.currentTimeMillis()<(time+wait))&&!kill){

								/* Prolong the wait because execution has been paused */
								if(paused){
									time=System.currentTimeMillis();
								}
								Thread.yield();
							}

							/*Decide if I1 or I2 is the winner */
							if(!blocked){
								if(GlobalAttributes.random()<0.5f){
									setWinner(1,blocked);
								}
								else{
									setWinner(2,blocked);
								}
							}else{
								setWinner(1,blocked);
							}

						}
					}
					/* If processing I2, set it to become
					 * inactive and then wait for I1 or PBI1 */
					else if(pick==2){
						I2.setActive(false);
						processing=true;
						if(render){
							GraphicsPanel.global.repaint();
						}

						if(PBI1!=null){
							/* Wait for I1 or PBI1 to become active */
							while(((!I1.hasArrived() && !PBI1.hasArrived()) || paused) && !kill){
								Thread.yield();
							}
						}
						else{
							/* Wait for I1 to become active */
							while((!I1.hasArrived() || paused) && !kill){
								Thread.yield();
							}
						}

						if(!kill){

							/* Lower the signal on I1 if I1 is active */
							if(I1.hasArrived()){
								I1.setActive(false);
							}

							/* Lower the signal on PBI1 if it is active
							 * and record the fact that I1 was clearly
							 * blocked */
							else if(PBI1.hasArrived()){
								PBI1.setActive(false);
								blocked=true;
							}
							if(render){
								GraphicsPanel.global.repaint();
							}

							/* Wait for a random amount of time between 0 and maxWait milliseconds */
							int wait=(int)(GlobalAttributes.random()*((float)GlobalAttributes.maxWait));

							/* Record the current system time */
							long time=System.currentTimeMillis();

							/* If the correct amount of time to wait hasn't been reached,
							 * and the thread has not been killed, then wait and yield the CPU */
							while((System.currentTimeMillis()<(time+wait))&&!kill){

								/* Prolong the wait because execution has been paused */
								if(paused){
									time=System.currentTimeMillis();
								}
								Thread.yield();
							}

							/*Decide if I1 or I2 is the winner */
							if(!blocked){
								if(GlobalAttributes.random()<0.5f){
									setWinner(1,blocked);
								}
								else{
									setWinner(2,blocked);
								}
							}else{
								setWinner(2,blocked);
							}

						}
					}
					/* If processing PBI1, set it to become
					 * inactive and then wait for I2 */
					else if(pick==3){
						PBI1.setActive(false);
						processing=true;
						if(render){
							GraphicsPanel.global.repaint();
						}
						/* Wait for I2 or PBI2 to become active */
						while((!I2.hasArrived() || paused) && !kill){
							Thread.yield();
						}
						
						if(!kill){
							I2.setActive(false);
							if(render){
								GraphicsPanel.global.repaint();
							}

							/* Wait for a random amount of time between 0 and maxWait milliseconds */
							int wait=(int)(GlobalAttributes.random()*((float)GlobalAttributes.maxWait));

							/* Record the current system time */
							long time=System.currentTimeMillis();

							/* If the correct amount of time to wait hasn't been reached,
							 * and the thread has not been killed, then wait and yield the CPU */
							while((System.currentTimeMillis()<(time+wait))&&!kill){

								/* Prolong the wait because execution has been paused */
								if(paused){
									time=System.currentTimeMillis();
								}
								Thread.yield();
							}
							setWinner(2,true);
						}
					}
					/* If processing PBI2, set it to become
					 * inactive and then wait for I1 */
					else if(pick==4){
						PBI2.setActive(false);
						processing=true;
						if(render){
							GraphicsPanel.global.repaint();
						}
						/* Wait for I1 or PBI1 to become active */
						while((!I1.hasArrived() || paused) && !kill){
							Thread.yield();
						}

						if(!kill){
							I1.setActive(false);
							if(render){
								GraphicsPanel.global.repaint();
							}

							/* Wait for a random amount of time between 0 and maxWait milliseconds */
							int wait=(int)(GlobalAttributes.random()*((float)GlobalAttributes.maxWait));

							/* Record the current system time */
							long time=System.currentTimeMillis();

							/* If the correct amount of time to wait hasn't been reached,
							 * and the thread has not been killed, then wait and yield the CPU */
							while((System.currentTimeMillis()<(time+wait))&&!kill){

								/* Prolong the wait because execution has been paused */
								if(paused){
									time=System.currentTimeMillis();
								}
								Thread.yield();
							}
							setWinner(1,true);
						}
					}
				}
			}

			/* If the internal state is 1 or 2, then simply defer execute logic
			 * to the superclass */
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

	void setWinner(int winner, boolean blocked){
		if(winner==1){
			if(!kill){

				/* If PBO2 is connected up, and we also need to
				 * send backwards feedback, then wait for any
				 * of the three lines to become available */
				if(PBO2!=null && blocked==false){
					while(((O1.isActive() && SBO2.isActive() && PBO2.isActive())
							|| paused) && !kill){
						Thread.yield();
					}
				}
				/* If PBO2 is connected up, but we do not
				 * need to send backwards feedback, then wait
				 * for PBO2 or O1 to become available */
				else if(PBO2!=null && blocked){
					while(((O1.isActive() && PBO2.isActive())
							|| paused) && !kill){
						Thread.yield();
					}
				}
				/* If PBO2 is not connected, and we need to
				 * send backwards feedback, then wait for
				 * O1 or SBO2 to become available */
				else if(PBO2==null && blocked==false){
					while(((O1.isActive() && SBO2.isActive()) || paused) && !kill){
						Thread.yield();
					}
				}
				/* If PBO2 is not connected, and we do not
				 * need to send backwards feedback, then wait
				 * for O1 to become available */
				else if(PBO2==null && blocked){
					while((O1.isActive() ||paused)&&!kill){
						Thread.yield();
					}
				}

				if(!kill){

					/* If O1 is available */
					if(!O1.isActive()){

						/* Raise the signal on O1 */
						O1.setActive(true);
						if(render){
							GraphicsPanel.global.repaint();
						}

						/* If PBO2 is connected up and we need
						 * to send backwards feedback, then wait for
						 * PBO2 or SBO2 to become available */
						if(PBO2!=null && blocked==false){
							while(((SBO2.isActive() && PBO2.isActive())
									|| paused) && !kill){
								Thread.yield();
							}
							if(!kill){

								/* If SBO2 is available, set it active
								 * then wait for PBO2 and then set it
								 * active */
								if(!SBO2.isActive()){
									SBO2.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((PBO2.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										PBO2.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
								/* Else if PBO2 is available, set it active
								 * then wait for SBO2 and then set it
								 * active */
								else if(!PBO2.isActive()){
									PBO2.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((SBO2.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										SBO2.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
							}
						}
						/* Else if PBO2 is connected up and
						 * we don't need to send backwards
						 * feedback, then just wait for
						 * PBO2 to become available */
						else if(PBO2!=null && blocked){
							while((PBO2.isActive() || paused) &!kill){
								Thread.yield();
							}
							if(!kill){
								PBO2.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
						/* Else if PBO2 is not connected up
						 * and we do need to send backwards
						 * feedback, then wait for SBO2
						 * to become available */
						else if(PBO2==null && blocked==false){
							while((SBO2.isActive() || paused) & !kill){
								Thread.yield();
							}
							if(!kill){
								SBO2.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
					}
					/* SBO2 is available and we do need to send
					 * backwards feedback */
					else if(!SBO2.isActive() && blocked==false){

						/* Raise the signal on SBO1 */
						SBO2.setActive(true);
						if(render){
							GraphicsPanel.global.repaint();
						}

						/* If PBO2 is connected up then wait for
						 * PBO2 or O1 to become available */
						if(PBO2!=null){
							while(((O1.isActive() && PBO2.isActive())
									|| paused) && !kill){
								Thread.yield();
							}
							if(!kill){
								/* If O1 is available, set it active
								 * then wait for PBO2 and then set it
								 * active */
								if(!O1.isActive()){
									O1.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((PBO2.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										PBO2.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
								/* Else if PBO2 is available, set it active
								 * then wait for O1 and then set it
								 * active */
								else if(!PBO2.isActive()){
									PBO2.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((O1.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										O1.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
							}
						}
						/* Else if PBO2 is not connected up
						 * then wait for O1
						 * to become available */
						else if(PBO2==null){
							while((O1.isActive() || paused) & !kill){
								Thread.yield();
							}
							if(!kill){
								O1.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
					}
					/* PBO2 is available and connected up */
					else if(PBO2!=null && !PBO2.isActive()){

						/* Raise the signal on PBO2 */
						PBO2.setActive(true);
						if(render){
							GraphicsPanel.global.repaint();
						}

						/* If we need to send backwards feedback, then
						 * wait for SBO1 or O1 to become available */
						if(!blocked){
							while(((O1.isActive() && SBO1.isActive())
									|| paused) && !kill){
								Thread.yield();
							}
							if(!kill){
								/* If O1 is available, set it active
								 * then wait for SBO1 and then set it
								 * active */
								if(!O1.isActive()){
									O1.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((SBO1.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										SBO1.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
								/* Else if SBO1 is available, set it active
								 * then wait for O1 and then set it
								 * active */
								else if(!SBO1.isActive()){
									SBO1.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((O1.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										O1.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
							}
						}
						/* Else if we are not sending
						 * backwards feedback then wait for O1
						 * to become available */
						else if(blocked){
							while((O1.isActive() || paused) & !kill){
								Thread.yield();
							}
							if(!kill){
								O1.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
					}
					if(!kill){

						/* Set the internal state to 1 */
						state=1;
					}
				}
			}
		}
		else if (winner==2){
			if(!kill){

				/* If PBO1 is connected up, and we also need to
				 * send backwards feedback, then wait for any
				 * of the three lines to become available */
				if(PBO1!=null && blocked==false){
					while(((O2.isActive() && SBO1.isActive() && PBO1.isActive())
							|| paused) && !kill){
						Thread.yield();
					}
				}
				/* If PBO1 is connected up, but we do not
				 * need to send backwards feedback, then wait
				 * for PBO1 or O2 to become available */
				else if(PBO1!=null && blocked){
					while(((O2.isActive() && PBO1.isActive())
							|| paused) && !kill){
						Thread.yield();
					}
				}
				/* If PBO1 is not connected, and we need to
				 * send backwards feedback, then wait for
				 * O2 or SBO1 to become available */
				else if(PBO1==null && blocked==false){
					while(((O2.isActive() && SBO1.isActive()) || paused) && !kill){
						Thread.yield();
					}
				}
				/* If PBO1 is not connected, and we do not
				 * need to send backwards feedback, then wait
				 * for O2 to become available */
				else if(PBO1==null && blocked){
					while((O2.isActive() ||paused)&&!kill){
						Thread.yield();
					}
				}

				if(!kill){

					/* If O2 is available */
					if(!O2.isActive()){

						/* Raise the signal on O2 */
						O2.setActive(true);
						if(render){
							GraphicsPanel.global.repaint();
						}

						/* If PBO1 is connected up and we need
						 * to send backwards feedback, then wait for
						 * PBO1 or SBO1 to become available */
						if(PBO1!=null && blocked==false){
							while(((SBO2.isActive() && PBO1.isActive())
									|| paused) && !kill){
								Thread.yield();
							}
							if(!kill){

								/* If SBO1 is available, set it active
								 * then wait for PBO1 and then set it
								 * active */
								if(!SBO1.isActive()){
									SBO1.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((PBO1.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										PBO1.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
								/* Else if PBO1 is available, set it active
								 * then wait for SBO1 and then set it
								 * active */
								else if(!PBO1.isActive()){
									PBO1.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((SBO1.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										SBO1.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
							}
						}
						/* Else if PBO1 is connected up and
						 * we don't need to send backwards
						 * feedback, then just wait for
						 * PBO1 to become available */
						else if(PBO1!=null && blocked){
							while((PBO1.isActive() || paused) &!kill){
								Thread.yield();
							}
							if(!kill){
								PBO1.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
						/* Else if PBO1 is not connected up
						 * and we do need to send backwards
						 * feedback, then wait for SBO1
						 * to become available */
						else if(PBO1==null && blocked==false){
							while((SBO1.isActive() || paused) & !kill){
								Thread.yield();
							}
							if(!kill){
								SBO1.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
					}
					/* SBO1 is available and we do need to send
					 * backwards feedback */
					else if(!SBO1.isActive() && blocked==false){

						/* Raise the signal on SBO1 */
						SBO1.setActive(true);
						if(render){
							GraphicsPanel.global.repaint();
						}

						/* If PBO1 is connected up then wait for
						 * PBO1 or O2 to become available */
						if(PBO1!=null){
							while(((O2.isActive() && PBO2.isActive())
									|| paused) && !kill){
								Thread.yield();
							}
							if(!kill){
								/* If O2 is available, set it active
								 * then wait for PBO1 and then set it
								 * active */
								if(!O2.isActive()){
									O2.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((PBO1.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										PBO1.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
								/* Else if PBO1 is available, set it active
								 * then wait for O2 and then set it
								 * active */
								else if(!PBO1.isActive()){
									PBO2.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((O2.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										O2.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
							}
						}
						/* Else if PBO1 is not connected up
						 * then wait for O2
						 * to become available */
						else if(PBO1==null){
							while((O2.isActive() || paused) & !kill){
								Thread.yield();
							}
							if(!kill){
								O2.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
					}
					/* PBO1 is available and connected up */
					else if(PBO1!=null && !PBO1.isActive()){

						/* Raise the signal on PBO1 */
						PBO1.setActive(true);
						if(render){
							GraphicsPanel.global.repaint();
						}

						/* If we need to send backwards feedback, then
						 * wait for SBO2 or O2 to become available */
						if(!blocked){
							while(((O2.isActive() && SBO2.isActive())
									|| paused) && !kill){
								Thread.yield();
							}
							if(!kill){
								/* If O2 is available, set it active
								 * then wait for SBO2 and then set it
								 * active */
								if(!O2.isActive()){
									O2.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((SBO2.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										SBO2.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
								/* Else if SBO2 is available, set it active
								 * then wait for O2 and then set it
								 * active */
								else if(!SBO2.isActive()){
									SBO2.setActive(true);
									if(render){
										GraphicsPanel.global.repaint();
									}
									while((O2.isActive() || paused) && !kill){
										Thread.yield();
									}
									if(!kill){
										O2.setActive(true);
										if(render){
											GraphicsPanel.global.repaint();
										}
									}
								}
							}
						}
						/* Else if we are not sending
						 * backwards feedback then wait for O2
						 * to become available */
						else if(blocked){
							while((O2.isActive() || paused) & !kill){
								Thread.yield();
							}
							if(!kill){
								O2.setActive(true);
								if(render){
									GraphicsPanel.global.repaint();
								}
							}
						}
					}
					if(!kill){

						/* Set the internal state to 2 */
						state=2;
					}
				}
			}
		}
	}
}