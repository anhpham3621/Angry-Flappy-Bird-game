package angryflappybird;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Label;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

//The Application layer
public class AngryFlappyBird extends Application {
	
	private Defines DEF = new Defines();
    
	//text display
	private int currentLives=3;
	Text livesText = new Text("Lives Left: 3");
	private Text gameOverText;
    // time related attributes
    private long clickTime, startTime, elapsedTime;   
    private AnimationTimer timer;
    
    // game components
    private Blob blob;
    private ArrayList<Sprite> floors;
    private ArrayList<Pipe> uPipes;
    private ArrayList<Pipe> dPipes;
    private Sprite whiteEgg;
    private Blob blob2;
    private double uPipeInitialY;
    
    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER;
    
    // scene graphs
    private Group gameScene;	 // the left half of the scene
    private VBox gameControl;	 // the right half of the GUI (control)
    private VBox gameLevel;
    private VBox gameDesc;
    private GraphicsContext gc;		
    private int currentScore;
    private int bg_counter = 0;
    double nextX_down = 0;
	double nextY_down = 0;
	double nextX_up = 0;
	double nextY_up = 0;
    
    private ImageView background;
    
    private Pipe uPipe;
    private Pipe dPipe;
    private boolean isNightBackground = true;
    
	// the mandatory main method 
    public static void main(String[] args) {
        launch(args);
    }
       
    // the start method sets the Stage layer
    @Override
    public void start(Stage primaryStage) throws Exception {

    	// initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
    	resetGameScene(true);  // resets the gameScene
    	
        HBox root = new HBox();
		HBox.setMargin(gameScene, new Insets(0,0,0,15));
		root.getChildren().add(gameScene);
		root.getChildren().add(gameControl);
//		root.getChildren().add(scoreText);                                                                     
		
		// add scene graphs to scene
        Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
        
        // finalize and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    // the getContent method sets the Scene layer
    private void resetGameControl() {
    	
        
        DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
        
        
        gameControl = new VBox(30);
        
        gameDesc = new VBox(15);
        
        Text pigTextLabel = new Text("Avoid the pigs");
        Text goldEggLabel = new Text("Collect the golden eggs");
        Text whiteEggLabel = new Text("Collect the white eggs");
        Text pipeLabel = new Text("Avoid the pipes");
        
        ImageView pigImage = new ImageView(DEF.IMAGE.get("monster_thief"));
        pigImage.setFitHeight(50);
        pigImage.setFitWidth(50);
        ImageView goldEggImage = new ImageView(DEF.IMAGE.get("golden_egg"));
        goldEggImage.setFitHeight(50);
        goldEggImage.setFitWidth(50);
        ImageView whiteEggImage = new ImageView(DEF.IMAGE.get("white_egg"));
        whiteEggImage.setFitHeight(50);
        whiteEggImage.setFitWidth(50);
        
//        this is appearing by the pig for some reason
        ImageView pipeImage = new ImageView(DEF.IMAGE.get("upward_pipe"));
        pipeImage.setFitHeight(50);
        pipeImage.setFitWidth(150);
      
        HBox pHBox = new HBox(5);
        pHBox.getChildren().addAll(pigImage, pigTextLabel);
//        
        HBox wHBox = new HBox(5);
        wHBox.getChildren().addAll(whiteEggImage, whiteEggLabel);
        
        HBox piHBox = new HBox(5);
        pHBox.getChildren().addAll(pipeImage, pipeLabel);
        
        HBox gHBox = new HBox(5);
        gHBox.getChildren().addAll(goldEggImage, goldEggLabel);

        gameDesc.getChildren().addAll(pHBox, gHBox, wHBox, piHBox);
        gameLevel = new VBox(5);
        gameLevel.getChildren().addAll(DEF.easyButton, DEF.mediumButton, DEF.hardButton);
        gameControl.getChildren().addAll(DEF.startButton, gameLevel, gameDesc);
        

    }
   
    private void mouseClickHandler(MouseEvent e) {
    	if (GAME_OVER) {
            resetGameScene(false);
        }
    	else if (GAME_START){
            clickTime = System.nanoTime();   
        }
    	GAME_START = true;
        CLICKED = true;
    }
    
    private void resetGameScene(boolean firstEntry) {
        // Create a Text object to display the score
        Text scoreText = new Text("0");

        // You can customize the font size and style if needed
        //scoreText.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-fill: black");
        scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        scoreText.setFill(Color.WHITE); 
        scoreText.setX(10);
        scoreText.setY(35);
        // Create a Text object to display the][i[ score
        
        livesText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        livesText.setFill(Color.BROWN); 
        livesText.setX(280);
        livesText.setY(560);
        gameOverText = new Text("Game Over, click to play again");
        gameOverText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        gameOverText.setFill(Color.RED);
        gameOverText.setX(DEF.SCENE_WIDTH / 2 - 150);
        gameOverText.setY(DEF.SCENE_HEIGHT / 2);

        // Set the initial visibility of gameOverText to false
        gameOverText.setVisible(false);

//        gameScene = new Group();
//        gameScene.getChildren().addAll(background, canvas, scoreText, livesText, gameOverText);
        // reset variables
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
        floors = new ArrayList<>();
        uPipes = new ArrayList<>();
        dPipes = new ArrayList<>();
        
        if(firstEntry) {
            // create two canvases
            Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            //Make background defined globally as an attribute
            // create a background
            background = DEF.IMVIEW.get("day_background");
//            System.out.println(background);
            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, canvas, scoreText, livesText,gameOverText);
        }
    
        // initialize floor
        for(int i=0; i<DEF.FLOOR_COUNT; i++) {
            
            int posX = i * DEF.FLOOR_WIDTH;
            int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
            Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor1"));
            floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            floor.render(gc);
            floors.add(floor);
        }

        Random rand = new Random();
        for (int i = 0; i < DEF.PIPE_COUNT; i++) {
            // Random value in the range [-PIPE_GAP, PIPE_GAP]
            double randomOffset = rand.nextDouble() * DEF.PIPE_RANGE; // Random value in the range [-PIPE_GAP, PIPE_GAP] 
            // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
            randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
            double initialY = DEF.D_PIPE_POS_Y - randomOffset;
//            System.out.println("INITIALY DOWN " + initialY);

            dPipe = new Pipe(DEF.D_PIPE_POS_X, initialY, DEF.IMAGE.get("dpipe2"));
            dPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            dPipe.render(gc);
            dPipes.add(dPipe);


            // Calculate the initial Y forU uPipe based on the corresponding dPipe
            uPipeInitialY = initialY + DEF.PIPE_Y_GAP;
            uPipe = new Pipe(DEF.U_PIPE_POS_X, uPipeInitialY, DEF.IMAGE.get("upipe1"));
//            System.out.println("INITIALY UP " + uPipeInitialY);
            uPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            uPipe.render(gc);
            uPipes.add(uPipe);
        }

        //for rendering the eggs on the pipe
        System.out.println(uPipeInitialY);
        whiteEgg = new Sprite(DEF.U_PIPE_POS_X, uPipeInitialY, DEF.IMAGE.get("white_egg"));
//        System.out.println(whiteEgg);
        whiteEgg.setVelocity(-0.5, 0);
        whiteEgg.render(gc);
        
        int pipe_gap = 150;

        // initialize blob
        blob = new Blob(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird1"));
        blob.render(gc);

        
        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
    }

    //timer stuff
    class MyTimer extends AnimationTimer {
    	
    	private Timeline backgroundSwitchTimeline;
    	
    	int counter = 0;
    	int bgr_counter = 0;
    	 public void handle(long now) {   	
    		 bgr_counter++;
    		 // time keeping
    	     elapsedTime = now - startTime;
    	     startTime = now;
    	     
    	     // clear current scene
    	     gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);

    	     if (GAME_START) {
    	    	 
    	    	 //step1: update background
    	    	 if (bgr_counter>450) {
//    	    		 System.out.println("The bgr counter is : " + bgr_counter);
    	    		 bgr_counter=0;
    	    		 isNightBackground= !isNightBackground;
    	    	 }
    	    		 displayBackground();
    	    	 

    	    	 //step2: update floor
    	    	 moveFloor();
    	    	 
    	    	 //step3: movePipe
    	    	 movePipe();
    	    	 
    	    	 // step4: update blob
    	    	 moveBlob();
    	    	 
    	    	 //setp5: update egg
    	    	 moveEgg();
    	    	 
    	    	 //step5: check for collision
    	    	 checkCollision();
    	     }
    	 }
    	 private void displayBackground() {
    	     if(isNightBackground) {
    	     gc.drawImage (DEF.IMAGE.get("night_background"), 0, 0);
    	     } 
    	 else if(!isNightBackground) {
    	     gc.drawImage (DEF.IMAGE.get("day_background"), 0, 0);
    	     }
    	 }
    	 // step1: update floor
    	 private void moveFloor() {
    		
    		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    			if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
    				double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
    	        	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    	        	floors.get(i).setPositionXY(nextX, nextY);
    			}
    			floors.get(i).render(gc);
    			floors.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
    		}
    	 }
    	 

    	 /**
    	  * @params None
    	  * movePipe is responsible for animating the upward and downward pipes
    	  */
    	Random rand1 = new Random();
	    public void movePipe() {
	    	
	    for (int i = 0; i < DEF.PIPE_COUNT-1; i++) {
	        if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
	            nextX_down = dPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX() + DEF.D_PIPE_POS_X;
	            //double nextY = DEF.D_PIPE_POS_Y;
	            double randomOffset = rand1.nextDouble() * DEF.PIPE_RANGE; // Random value in the range [-PIPE_GAP, PIPE_GAP] 
	            // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
	            randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
	            nextY_down = DEF.D_PIPE_POS_Y - randomOffset;
//	            System.out.println("NEXT Y DOWN " + nextY_down);
	            dPipes.get(i).setPositionXY(nextX_down, nextY_down);
	            
	            
	            nextX_up = uPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX() + DEF.U_PIPE_POS_X;
	            nextY_up = nextY_down + DEF.PIPE_Y_GAP;
	            //double nextY = DEF.U_PIPE_POS_Y;
	            uPipes.get(i).setPositionXY(nextX_up, nextY_up);
//	            whiteEgg.setPositionXY(nextX_up+100, nextY_up);
	        }
//	        whiteEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        
//	        System.out.println("the bird position: " + nextY_up);
	        dPipes.get(i).render(gc);
	        dPipes.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
	        
	        uPipes.get(i).render(gc);
	        uPipes.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
//	        whiteEgg.render(gc);
//	        moveEgg(nextX_up, nextY_up);
//	        whiteEgg.update(DEF.EASY_SCENE_SHIFT_TIME);
	        }
	    };

    	 // step2: update blob
    	 private void moveBlob() {
    		 
			long diffTime = System.nanoTime() - clickTime;
			
			// blob flies upward with animation
			if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
				
				int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
				imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
				blob.setImage(DEF.IMAGE.get("bird"+String.valueOf(imageIndex+1)));
				blob.setVelocity(0, DEF.BLOB_FLY_VEL);
			}
			// blob drops after a period of time without button click
			else {
				
				//check the image when it's landing
				//if the wing is upward, flip it to a downward flapping wing
			    blob.setVelocity(0, DEF.BLOB_DROP_VEL); 
			    CLICKED = false;
			}

			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			blob.render(gc);
    	 }
    	 
    	 public void moveEgg() {
    		 
    		 for (int i = 0; i < 2; i++) {
    			 
    			 
    			 if (whiteEgg.getPositionX() <= -DEF.FLOOR_WIDTH) {
    				 System.out.println("moving egg");
     				double nextX = DEF.FLOOR_WIDTH;
     	        	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
     	        	whiteEgg.setPositionXY(nextX, 250);
     			}
    			 whiteEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    	   			whiteEgg.render(gc);
    	   			whiteEgg.update(DEF.EASY_SCENE_SHIFT_TIME);
//    			 double x_pos = 250;
//    			 double y_pos = 250;
//    			 whiteEgg.setPositionXY(x_pos, y_pos);
    		 }
    		 
//    		 WHEN I take out the position, the bird velocity works but only onced
//    		 	
    	        
    	 }

    	 public void checkCollision() {
    	        boolean hitAUpipe = false; 
    	        // check collision  with the floor
    	        for (Sprite floor: floors) {
    	            GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
    	        }
    	        
    	        /**handling the blob intersecting with a upipe
    	         * the life of the blob should go to life - 1
    	         * the UI should be updated
    	         * if the life reaches 0, the game ends
    	        **/
    	        
    	        for (Pipe uPipe: uPipes) {
    	            if (blob.intersectsPipe(uPipe)) {
    	                
    	                //show the hit effect
//    	                  showHitEffect();
    	                //re-render the current_lives
    	            	currentLives--;
    	            	
    	            	// if (currentLives>=0){
    	            	updateLivesText();
    	            	 //}
    	                GAME_OVER = GAME_OVER || blob.intersectsPipe(uPipe);
    	            }
    	        }
    	        
    	        /**handling the blob intersecting with a dpipe
    	        *the life of the blob should go to life - 1
    	        *the UI should be updated
    	        *if the life reaches 0, the game ends
    	        **/
    	        for (Pipe dPipe: dPipes) {
    	           if (blob.intersectsPipe(dPipe)) {
//    	              current_lives --;
    	                //show the hit effect
    	            //showHitEffect();
    	                //re-render the current_lives
    	        	   currentLives--;
//    	        	   if (currentLives>0){
    	        		  updateLivesText();
    	        	   //}
    	               GAME_OVER = GAME_OVER || blob.intersectsPipe(dPipe);
    	           }
    	        }
    	        
    	        /**
    	         * This handles the logic for letting the white eggs increment the total coins
    	         * available
    	         */
//    	          for (Sprite egg: whiteEggs) {
//    	              if (blob.intersectsPipe(dPipe)) {
//    	                  current_score ++;
//    	                  //show the the additional score gained
//    	                  showHitEffect();
//    	                  //re-render the current_score on the screen
//    	              }
//    	          }
    	        
    	        //set the game_over to true if no lives remaining
//    	          if (currentLives == 0) {
//    	              GAME_OVER = true;
//    	          }
    	        
    	        // end the game when blob hit stuff
    	        if (GAME_OVER) {
    	            showHitEffect(); 
    	            for (Sprite floor: floors) {
    	                floor.setVelocity(0, 0);
    	            }
    	            timer.stop();
    	            if (currentLives == 0) {
//      	              GAME_OVER = true;
    	            	gameOverText.setVisible(true);
    	            	gameScene.setOnMouseClicked(e -> {
        	                gameOverText.setVisible(false); // Hide the game over message when restarting
        	            });
     	            }
    	         // Display the game over message
    	            

    	            // Allow the player to play again by clicking anywhere on the scene
//    	            gameScene.setOnMouseClicked(e -> {
//    	                gameOverText.setVisible(false); // Hide the game over message when restarting
//    	            });
    	        }
    	        
    	     }
    	 
	     private void showHitEffect() {
	        ParallelTransition parallelTransition = new ParallelTransition();
	        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
	        fadeTransition.setToValue(0);
	        fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
	        fadeTransition.setAutoReverse(true);
	        parallelTransition.getChildren().add(fadeTransition);
	        parallelTransition.play();
	     }
	     
	     //when bird loses a life, update remaining lives
	     //havent tested this function
	     private void updateLivesText() {
	    	    if (currentLives > 0) {
	    	        livesText.setText("Lives Left: " + currentLives);
	    	    } else {
	    	        livesText.setText("Lives Left: 0");
	    	    }
	    	}
    	 
    } // End of MyTimer class

} // End of AngryFlappyBird Class

