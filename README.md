# Prime Event Structure Simulator
 A tool for examining and simulating the set of valid execution behaviours for a given Prime Event Structure definition. Also generates state transition graphs. Created in support of master's degree.

 CONTENTS
I - 	AUTHOR
II - 	FILES INCLUDED IN THIS PACKAGE
III -	REQUIREMENTS
IV -	FEATURES
V - 	INSTALLATION AND USAGE




I - AUTHOR

This software was developed by Daniel Morrison for submission on part of the 4th year of a Master Of Computer Science (MComp) Degree Programme. Its use was intended as both a deliverable and as a general tool in the aid of study for this project. The program in its current form is the sum of several months work resulting in the software included in this package.



II - FILES INCLUDED IN THIS PACKAGE

Inside the file PrimeEventStructureSimulator-1.0.zip you should find the following directory and file structre.

/
	/PrimeEventStructureSimulator-1.0/
		Main/
			-GlobalAttributes.class
			-Main.class
		GUI/
			-ControlPanel.class
			-GraphicsPanel.class
			-HelpScreen.class
			-MainWindow.class
			-OutputFrame.class
			-icon.jpg
		Model/
			-EventStructure.class
			-EventStructureRender.class
			-EventThread.class
			-TransitionGraph.class
			-TransitionGraphNode.class
			-TransitionGraphRender.class
		Circuit/
			-Circuit.class
			-CircuitElement.class
			-CircuitExamples.class
			-CircuitExamplesRender.class
			-ConflictSynchThread.class
			-ConflictThread.class
			-ForkThread.class
			-JoinThread.class
			-MergeThread.class
			-StatTesting.class
			-WireThread.class
		-Run.bat
	/Src-1.0/
		Main/
			-GlobalAttributes.java
			-Main.java
		GUI/
			-ControlPanel.java
			-GraphicsPanel.java
			-HelpScreen.java
			-MainWindow.java
			-OutputFrame.java
			-icon.jpg
		Model/
			-EventStructure.java
			-EventStructureRender.java
			-EventThread.java
			-TransitionGraph.java
			-TransitionGraphNode.java
			-TransitionGraphRender.java
		Circuit/
			-Circuit.java
			-CircuitElement.java
			-CircuitExamples.java
			-CircuitExamplesRender.java
			-ConflictSynchThread.java
			-ConflictThread.java
			-ForkThread.java
			-JoinThread.java
			-MergeThread.java
			-StatTesting.java
			-WireThread.java
	-README.txt



III -	REQUIREMENTS

This software has no specific hardware requirements, however a reasonably recent processor as of the time of this writing (2012), i.e. Intel Core (or equivalent) or above, is recommended.

The software requirements are the Java Runtime Enviroment (JRE) and a 32-bit/64-bit operating system which supports Java (Windows, UNIX or Linux for example). 

WARNING - You must use the version of the JRE (32-bit/64-bit) which matches the version (32-bit/64-bit) of the OS which you are using. Not doing so has known to cause the software to hang.

A zip extractor is needed to access the archive, however this is included by default in many modern operating systems, including Windows.



IV -	FEATURES

-Intuitive GUI for easy modification of options
-Enter your own Prime Event Structure definition
-View Prime Event Structure as a causality graph or as a transition graph!
-View a variety of pre-programmed delay-insensitive circuits which emulate Prime Event Structures
-Run statistical analysis on these circuits to view the probability of each output



V - 	INSTALLATION AND USAGE

1) Extract the PrimeEventStructureSimulator-1.0 folder from the archive.
2) Either launch Run.bat (if you are a Windows user), or manually invoke the java command with the Main/Main.class file as a parameter from within the PrimeEventStructureSimulator-1.0 directory.