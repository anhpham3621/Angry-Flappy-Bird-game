# AngryFlappyBird
COMSC-225: Software Design and Development Final Project 2023.  
Contributors: Keisha Modi, Anh Pham & Emmanuella Umoye  

## GAME DESCRIPTION
The game Angry Flappy Bird features a dynamic blob (bird) navigating through obstacles like the floor, the pipes, or the monster. To make the bird goes up to avoid these obstacles, the user clicks one button called "Go!" on the user interface. If the user does not click anything, the bird defaults to moving down. During the game's iteration, the bird needs to be kept flying so it does not hit the floor, otherwise the game will be over. While in flying motion, the bird must collect as many eggs as possible and prevent the evil pigs from stealing any eggs. There are two types of eggs: white eggs and golden eggs. If the bird intersects with the white egg, the score increases. If the bird catches a rare golden egg, the bird switches to autopilot mode for a few seconds, meaning that the user does not have to press any buttons to keep the bird avoiding obstacles. To win the game, the user has to help the bird collect as many scores as possible. See the instructions below on details on how to play the game.

## WHAT DOES THE GAME LOOK LIKE?
-- Note: Insert an image here

## Core Components of the Game
- Blob: At the start, the bird has 0 scores and a maximum of 3 lives for each cycle. The blob (bird) has to avoid the floor, pipes, and pig. When the blob hits the pig or the floor, it dies immediately and the score becomes 0 again and the number of lives is reset to 3. When the blob hits either of the pipes, it loses one life. The bird also has to take as many eggs as possible to increase its score and go on autopilot mode. The goal is to not die for as long as possible and collects as many points as possible.
    
- Pipes: There are two types of pipe on the screen. The downward-facing pipe is the pipe at the top of the screen and the upward-facing pipe is the pipe at the bottom. The blob has to avoid them. The upward-facing pipe can randomly has a white egg or a rare golden egg placed on it. The pipes' height differences are of random lengths, however the gap between the two kinds of pipes remain constant throughout the game. The blob's score increases by 1 for every set of pipes successfully passed i.e. without a collision. The blob's life decreases by 1 for every pipe it collides with. Again, when the lives count is 0, the bird dies and the game will be reset.
   
- Monster Pigs: The pigs are objects that harm the blob. When the pig intersects with the blob, the game ends. When the pig intersects with the white eggs, meaning that the pig has stolen that egg, 2 points are taken off the blob's total score. When the pig intersects with the gold egg, 5 eggs are taken off the blob's total score.
  
- Eggs: There are two types of eggs.
  - White egg: This is a gem for the blob. When the blob interacts with a white egg, it gains 5 additional points
  - Gold Egg: When the blob interacts with a gold egg, it goes on autopilot mode for about 6 seconds.
   
- Floor: The floor is animated on the screen and moves continuously. The blob interacting with the floor leads to the game being over (the bird dies) and the score count and lives count are reset.
- Background: The background is animated to change from night to day periodically
- The Legends: this is the information on the side giving the user information on the consequences of interacting with different elements on the bird's path.
- Game Mode: We have 3 modes of the game: Easy, Hard and Medium. The speed of the game increases based on the mode. The easy mode has the smallest speed, and the hard mode has the hardest speed.
  - [ ] Easy Mode: Slow movement of blob and other objects in the game  
  - [ ] Medium Mode: Slightly fast movement of blob and other objects in the game  
  - [ ] Hard Mode: Very Fast movement of blob and other objects in the game  

## Additional Features
  - [ ] Sound Effects: The game has a sound effect when the bird moves, collides with obstacles, or gets an egg.  
  - [ ] gameOver Animation: When the game is over, the animated GAME OVER text appears on the screen, informing the user the status of the game.
  - [ ] Autopilot mode: The bird switches to autopilot mode and does not need to avoid obstacles. It just goes through a straight line.
  - [ ] Rotate the bird image when it drops: The bird is rotated downwards when it hits a pig and drops to the floor.  

## Playing the game: Requirements, Setup, and Playing the Game
  ### JAVA: Version 11 or higher is required to run this project.
    - Visit this link to install JAVA 11: https://adoptopenjdk.net/ and follow additional instructions here
    - To ensure Java is correctly installed, open the terminal and run: java --version.

  ### JAVA FX: **JavaFX is a Graphical User Interface (GUI) toolkit for Java programs**
    - Installation
        - Navigate to https://gluonhq.com/products/javafx/
        - Depending on what Java version you installed, download the compatible version of JavaFX SDK. 
        - Unzip the folder and remember its location for later use.
        - Additional steps for Mac users:
            - Navigate to the folder where JavaFX was unzipped. 
            - Go to lib, for each .dylib file 
            - Right-click > Open-with > Terminal > Open and then, close the terminal
    - Binding Eclipse with JavaFX:
    - MAC users:       Click on Eclipse > Preferences
    - Window users:  Click on Window > Preferences
    - Go to Java > BuildPath > User Libraries
          New > Name “JavaFX11” > Select it and “Add External Jars” > 
    - Find the location where JavaFX (slide 6) was downloaded and select all .jar files.

  ### ECLIPSE
    - An IDE for running computer programs. It has tools that support JAVA development
    - Download Eclipse here: (https://www.eclipse.org/downloads/)
    - Bind with JAVA
        - Binding with JAVA:
        - MAC users:       Click on Eclipse > Preferences
        - Window users:  Click on Window > Preferences
        - Go to Java > Installed JREs > Choose the one you installed. > Execution Environment > JavaSE-11 & Check on compatible JRE*
        - Go to Java > Compiler > Set the Compiler Compliance Level to 11*
        - Apply & Close
**Your Computer Is All Set for Running JavaFX Programs**
## Reflection
- Emmanuella
  - I have learned a lot about JavaFX and Java through this project. We utilized all the concepts we learned so far in this class to finish AngryFlappyBird. It was a challenging project especially as we were not used to JavaFX, but I am glad we pulled through.
- Anh
  - Doing projects is an active learning method as I get to experiment with different methods to implement what I've learned in class into a concrete product. It is also beneficial that I get to execute my vision for the project since we get to decide what we want to do with our unique game based on the given core orientation. Practicing Github has also been very helpful and I believe I will use this skill for a long time.
- Keisha

**Copyright 2023**
