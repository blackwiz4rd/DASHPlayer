# DASHPlayer
A simple Java implementation of a DASH client for adaptive video streaming developed for my Bachelor's degree final exam.
A python server was added to allow developers to test their algorithms (see #Server built in python with django).

# Dependencies
All libraries are located in the ```lib/``` folder, link them to your IDE (This project was built with IntelliJ, so you don't need to do it if you use that IDE).
For IntelliJ users, set project language level to "9" in "Project Structure | Project" and "Project SDK" to JDK.
The video player is based on [VLCJ](https://github.com/caprica/vlcj) that uses LibVLC, install the VLC application to get it.
However VLCJ has reported to have issues with MacOS. As it some functionalities are limited.</br> 
I have commented some lines in in both Player.java and DashPlayer.java because of that.</br>
Multiple lines were commented:</br>
player.play(); in DashAlgorithm.java</br>
player.playItem(PlayerEventListener.segIndex - 1); in *Custom*DashAlgorithm.java</br>

To activate DDASH algorithm you need to start the Learning agent first and then start the algorithm from ``Player.java`` or ``PlayerNOGUI.java``:
```sh start_agent.sh```

To install dependencies for ``src/python_lib/Agent.py`` and ``src/server/manage.py``run
```chmod +x setup.sh```
```sh setup.sh```

# Run the player from command line
To run the server from command line run
To install dependencies run</br>
```chmod +x start_player.sh```</br>
```sh start_player.sh```

# Server built in python with django
```git clone https://github.com/blackwiz4rd/StorageServer```

# Code structure
The code is structured as it follows:</br>
```Player.java``` contains a main file for GUI only elements.</br>
The Player depends on GUI elements available in:</br>
```Point.java```</br>
```Messages.java```</br>
```RenderPlot.java```</br></br>

```PlayerNOGUI.java``` contains a main file to run the specified algorithm and doesn't use GUI.</br></br>

```DashPlayer.java``` is used by both ```Player.java``` and ```PlayerNOGUI.java```  to start the videos.</br>

```DashAlgorithm.java``` is the model for building new algorithms and has to be extended to implement new DASH algorithms. All DASH algorithms run as a `Thread`. </br>

Some algorithms were implemented extending ```DashAlgorithm.java``` in ```*DashAlgorithm.java```:</br>
BitRateBased</br>
BufferBased</br>
Festive</br>
MCP</br>
<b>MLP2</b> (refer to paper)</br>
PANDA</br>
As it follows to start each algorithm the start() function is used.
Additional files are:</br>
```ChannelLoader.java``` that implements ```interfaces/Channel.java```.</br>
```FileDownloader.java``` that implements ```interfaces/Channel.java```.</br>

```MPDParser.java``` that parses the Media Presentation Description file.</br>
```Plotter.java``` that plots stats.</br>
(```PlayerEventListener```)</br>

```MarkovChannel.java```</br>
```MarkovDecisionProcess.java```</br>
```MarkovDownloader.java```</br>

```SyntheticVideo.java``` and ```VideoLoader.java``` that implements ```interfaces/Video.java```.</br>
```Trainer.java``` that extends Thread and was used to train the agent for MLP2.</br>


===========================================

This directory contains the source code of my bachelor's degree final exam.

Bachelor Degree in Information Engineering
University of Padua, Italy

Copyright and license information can be found in the file LICENSE. 
Additional information can be found in the file NOTICE.

# Authors
This work is based on the project of Iacopo Mandatelli, Matteo Biasetton, Luca Piazzon.
Final implementation of adaptive algorithms Davide Talon.
The code was revisited from Luca Attanasio.
