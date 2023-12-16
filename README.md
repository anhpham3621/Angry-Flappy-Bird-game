# AngryFlappyBird
COMSC-225: Software Design and Development Final Project 2023.  
Contributors: Keisha Modi, Anh Pham & Emmanuella Umoye  

## GAME DESCRIPTION
The game Angry Flappy Bird features a dynamic bird navigating through obstacles like the floor, the pipes, or the monster. To make the bird go up to avoid these obstacles, the user clicks one button called "Go!" on the user interface. If the user clicks nothing, the bird defaults to moving down. During the game's iteration, the bird needs to be kept flying so it does not hit the floor, otherwise the game will be over. While in flying motion, the bird must collect as many eggs as possible and prevent the evil monster character from stealing any eggs. There are two types of eggs: white eggs and golden eggs. If the bird intersects with the white egg, the score increases by 5 points. If the bird catches a rare golden egg, the bird switches to autopilot mode for 6 seconds, meaning that the user does not have to press any buttons to keep the bird from falling or avoiding obstacles. The incentive of the game is to win as many points as possible. See the instructions below for details on how to play the game.

## WHAT DOES THE GAME LOOK LIKE?
-- Image of Game
![062B272A-0050-43CA-8ACC-640A7A1A1513 2](https://github.com/MHC-FA23-CS225/angryflappybird-kea/assets/144300541/648eb358-95dd-4afd-b3af-3a923d283791)


## Core Components of the Game
- Bird: At the start, the bird has a score of 0 and a maximum of 3 lives for each game cycle. The bird has to avoid the floor, pipes, and monster. When the bird hits the monster or the floor, it dies immediately and the score becomes 0 again and the number of lives is reset to 3. When the bird hits either of the pipes, it loses one life. The bird also has to take as many eggs as possible to increase its score and go on autopilot mode. The goal is to not die for as long as possible and collect as many points as possible.
    
- Pipes: There are two types of pipe on the screen. The downward-facing pipe is the pipe at the top of the screen and the upward-facing pipe is the pipe at the bottom. The bird has to avoid them. The upward-facing pipe randomly has a white egg or a rare golden egg placed on it. The pipes' height differences are of random lengths, however, the vertical gap between the pair of upward and downward pipes remains constant throughout the game. The bird's score increases by 1 for every set of pipes successfully passed i.e. without a collision. The bird's life decreases by 1 for every pipe it collides with. Again, when the life count is 0, the bird dies and the game will be reset.
   
- Monster: The monsters are characters that steal eggs from the bird or cause the bird to die. When the monster intersects with the blob, the game ends. When the monster intersects with the white eggs, stealing the egg, 2 points are taken off from the bird's total score. When the monster intersects with the gold egg, 5 points are also taken off the bird's total score.
  
- Eggs: There are two types of eggs.
  - White egg: This is a gem for the bird. When the bird interacts with a white egg, it gains 5 additional points
  - Gold Egg: When the bird interacts with a gold egg, it goes on autopilot mode for about 6 seconds.
   
- Floor: The floor is animated on the screen and moves continuously. When the bird collides with the floor, the game is over and all components of the game, like the score and lives, are reset
  
- Background: The background is animated to change from night to day periodically
  
- The Legends: this is the information on the side giving the user information on the consequences of interacting with different elements on the bird's path.

- Game Mode: We have 3 modes of the game: Easy, Hard and Medium. The speed of the game increases based on the mode. The easy mode has the lowest speed, and the hard mode has the hardest speed.
  - [ ] Easy Mode: Slow movement of bird and other objects in the game  
  - [ ] Medium Mode: Slightly fast movement of bird and other objects in the game  
  - [ ] Hard Mode: Very Fast movement of bird and other objects in the game  

## Additional Features
  - [ ] Sound Effects: The game has a sound effect when the bird moves, collides with obstacles or gets an egg.  
  - [ ] gameOver Animation: When the game is over, the animated GAME OVER text appears on the screen, informing the user of the status of the game.
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
   - This project has helped me develop my problem-solving skills and perseverance. Many different aspects of the project required me to think critically and
     wisely about what I expected from my code and how to execute it. I also learned the importance of time management and proper planning. From this project, we were able
     to delegate tasks and plan our implementations daily. From this game, I increased my knowledge of how to properly use JavaFX and Git, a skill that will be very helpful in the future. My favorite part was working in person with my group mates and celebrating our collaborative successes together.  
**Copyright 2023**
