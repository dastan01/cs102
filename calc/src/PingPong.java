import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PingPong extends Application {

	AnimationTimer timer;

	Ball ball;
	double ballSpeed = 1.5;//k

	Scene scene;

	Paddle rightPaddle;
	Paddle leftPaddle;

	Wall rightWall;
	Wall leftWall;
	Wall topWall;
	Wall bottomWall;
	Wall midWall;

	/**
	 * The animation of the ball
	 */
	Timeline ballAnimation;
	Timeline ballWaitAnimation;

	Button startButton;
	Button showHelpButton;
	Button hideHelpButton;

	Labels score1;
	Labels score2;
	Labels p1FB;// fireball
	Labels p2FB;
	Labels speedOf;
	Labels result;
	Labels helpTextlabel;
	private String labelColor = "#ffedfe";

	Audio AudioBGMPlay;
	Audio AudioBGMWait;
	Audio AudioReady;
	Audio AudioPressReady;
	Audio AudioHit;
	Audio AudioHitWall;
	Audio AudioP1;
	Audio AudioP2;
	Audio AudioFire;

	private static final String READY_IMAGE_LOC = "RUready.png";
	private Image readyImage;
	private ImageView areReadyiv;
	private static final String LOGO_IMAGE_LOC = "logo_ok.png";
	private Image logoImage;
	private ImageView logoShow;
	private static final String BACKGROUND_IMG_LOC = "background_image.png";
	private Image backGroundImage;
	private Node background;

	FadeTransition ft;
	FadeTransition logo;

	int p1, p2, p1FBs, p2FBs, H = 500, W = 1000;

	boolean running,
			goBar2UP,
			goBar2Down,
			goBar2Left,
			goBar2Right,
			goBar1UP,
			goBar1Down,
			goBar1Left,
			goBar1Right,
			p1fire,
			p2fire,
			isFiring = false,
			isPlaying;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {

		setUpAllComponent();

		AudioBGMWait.play();

		ballWaitAnimation = new Timeline(60, new KeyFrame(Duration.millis(10.0),
				t -> {
					isPlaying = false;
					ball.move(ballSpeed);
					checkForCollision();
				}));
		ballWaitAnimation.setCycleCount(Timeline.INDEFINITE);
		ballWaitAnimation.playFromStart();

		ballAnimation = new Timeline(30 , new KeyFrame(Duration.millis(10.0),
				t -> {
					ball.move(ballSpeed);
					checkForCollision();
					updateScoreAndSpeed();
					checkIfGameOver();

				}));
		ballAnimation.setCycleCount(Timeline.INDEFINITE);

		Group testGroup = new Group(
				background,
				logoShow,
				midWall.getWallShape(),
				ball.getBallShape(),
				leftPaddle.getPaddleShape(),
				rightPaddle.getPaddleShape(),
				leftWall.getWallShape(),
				rightWall.getWallShape(),
				topWall.getWallShape(),
				bottomWall.getWallShape(),
				startButton,
				hideHelpButton,
				showHelpButton,
				score1.getLabelType(),
				score2.getLabelType(),
				result.getLabelType(),
				speedOf.getLabelType(),
				p1FB.getLabelType(),
				p2FB.getLabelType(),
				helpTextlabel.getLabelType(),
				areReadyiv);

		scene = new Scene(testGroup, W, H);

		keyboardEvents();

		stage.setScene(scene);
		stage.setTitle("Ping Pong Game");
		stage.show();

		timer.start();

	}

	private void setUpAllComponent() {
		createBall();
		createPaddle();
		createWall();
		setAudio();
		setUpImage();
		setUpLabel();
		setUpButton();
		controlPaddleXY();
	}

	private void createBall() {
		ball = new Ball(W / 2, H / 2, 10);
	}

	private void createPaddle() {
		leftPaddle = new Paddle(20, (H / 2) - 50, 10, 100);// H - 20
		leftPaddle.setFill(Color.RED);

		rightPaddle = new Paddle(W - 30, (H / 2) - 50, 10, 100);
		rightPaddle.setFill(Color.ROYALBLUE);
	}

	private void createWall() {
		rightWall = new Wall(W - 10, 0, 10, H);
		rightWall.setFill(Color.ROYALBLUE);

		leftWall = new Wall(0, 0, 10, H);
		leftWall.setFill(Color.RED);

		topWall = new Wall(0, 0, W, 10);
		topWall.setFill(Color.GOLD);

		bottomWall = new Wall(0, H - 10, W, 10);
		bottomWall.setFill(Color.BLUEVIOLET);

		midWall = new Wall((W / 2) - 3, 0, 5, H);
		midWall.setFill(Color.BLACK);
	}

	private void setAudio() {
		AudioBGMPlay = new Audio("SD_BGM_CUSTOM_QRISPY.m4a");
		AudioBGMPlay.cycleCountINF();
		AudioHit = new Audio("SD_SHOT_JAPAN_JUST.m4a");
		AudioHit.cycleCount(1);
		AudioHitWall = new Audio("SD_SHOT_TABLETENNIS_JUST.m4a");
		AudioHitWall.cycleCount(1);
		AudioPressReady = new Audio("SD_SE_CUSTOM_AUTUMN.m4a");
		AudioPressReady.cycleCount(1);
		AudioReady = new Audio("SD_CV_AREYOUREADY.m4a");
		AudioReady.cycleCount(1);
		AudioP1 = new Audio("SD_CV_REDWIN.m4a");
		AudioP1.cycleCount(1);
		AudioP2 = new Audio("SD_CV_BLUEWIN.m4a");
		AudioP2.cycleCount(1);
		AudioBGMWait = new Audio("SD_BGM_CUSTOM_AUTUMN.m4a");
		AudioBGMWait.cycleCountINF();
		AudioFire = new Audio("SD_SHOT_ELECTRO2_JUST.m4a");
		AudioFire.cycleCount(1);
	}

	private void setUpImage() {
		backGroundImage = new Image(BACKGROUND_IMG_LOC);
		background = new ImageView(backGroundImage);

		readyImage = new Image(READY_IMAGE_LOC);
		areReadyiv = new ImageView(readyImage);
		areReadyiv.setLayoutX(249);
		areReadyiv.setLayoutY(H / 2 - 60);
		areReadyiv.setVisible(false);
		ft = new FadeTransition();
		ft.setNode(areReadyiv);
		ft.setDuration(new Duration(2100));
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);

		logoImage = new Image(LOGO_IMAGE_LOC);
		logoShow = new ImageView(logoImage);
		logoShow.setVisible(true);
		logo = new FadeTransition();
		logo.setNode(logoShow);
		logo.setAutoReverse(true);
		logoSetting(2000, 0.5, 1.0, 0);
	}

	private void logoSetting(int duration, double from, double to, int cyclecount) {
		logo.setDuration(new Duration(duration));
		logo.setFromValue(from);
		logo.setToValue(to);
		if (cyclecount == 0)
			logo.setCycleCount(FadeTransition.INDEFINITE);
		else
			logo.setCycleCount(cyclecount);
		logo.play();
	}

	private void setUpLabel() {
		score1 = new Labels("Player Red: " + p1, 50, 50);
		score1.TextFill(Color.web(labelColor));

		score2 = new Labels("Player Blue: " + p2, W - 150, 50);
		score2.TextFill(Color.web(labelColor));

		p1FB = new Labels("Power:[          ]", 50, 70);
		p1FB.TextFill(Color.web(labelColor));

		p2FB = new Labels("Power:[          ]", W - 150, 70);
		p2FB.TextFill(Color.web(labelColor));

		speedOf = new Labels("Now Speed is: " + ballSpeed, 50, 90);
		speedOf.TextFill(Color.web(labelColor));

		result = new Labels("Result is ", (W / 2) - 50, 200);
		result.SetVisible(false);
		result.TextFill(Color.web(labelColor));

		helpTextlabel = new Labels(helpText(), W / 2, H / 3);
		helpTextlabel.SetVisible(false);
		helpTextlabel.TextFill(Color.web(labelColor));
	}

	public void setUpButton() {
		startButton = new Button("Start!");
		startButton.setLayoutX((W / 2) - 35);
		startButton.setLayoutY(450);
		startButton.setOnAction(e -> {
			ballWaitAnimation.stop();
			ball.setCenterX(W / 2);
			ball.setCenterY(H / 2);
			isPlaying = true;
			logo.stop();
			logoSetting(5000, 0.7, 0.0, 1);
			startButton.setVisible(false);
			hideHelpButton.setVisible(false);
			showHelpButton.setVisible(false);
			result.SetVisible(false);
			AudioBGMWait.stop();
			makePaddleReturn();
			AudioPressReady.play();
			Timeline timeline1 = new Timeline(new KeyFrame(
					Duration.millis(6000),
					ae1 -> {
						AudioReady.play();
						ballWaitAnimation.stop();
						ft.play();
						areReadyiv.setVisible(true);
						Timeline timeline2 = new Timeline(new KeyFrame(
								Duration.millis(2000),
								ae2 -> {
									ballAnimation.playFromStart();
									AudioBGMPlay.play();
								}));
						timeline2.play();
					}));
			timeline1.play();
		});

		showHelpButton = new Button("Instruction");
		showHelpButton.setLayoutX((W / 2) - 40);
		showHelpButton.setLayoutY(400);
		showHelpButton.setOnAction(h -> {
			helpTextlabel.SetVisible(true);
			showHelpButton.setVisible(false);
			hideHelpButton.setVisible(true);
		});
		hideHelpButton = new Button("Return");
		hideHelpButton.setLayoutX((W / 2) - 40);
		hideHelpButton.setLayoutY(400);
		hideHelpButton.setOnAction(h -> {
			helpTextlabel.SetVisible(false);
			showHelpButton.setVisible(true);
			hideHelpButton.setVisible(false);
		});
	}

	private String helpText() {
		return "Players must hit the ball\n" +  //Instruction
				"Who misses the ball, he lost.\n";

	}

	private void setFBpower(Labels p2fb2, int x) {
		if (x == 0)
			p2fb2.SetText("power[          ]");
		else if (x == 1)
			p2fb2.SetText("power[OO       ]");
		else if (x == 2)
			p2fb2.SetText("power[OOOO   ]");
		else if (x == 3)
			p2fb2.SetText("power[OOOOOO]");
	}

	private void checkForCollision() {
		checkForBallAndWallCollision();
		checkForBallCollisionLeft();
		checkForBallCollisionRight();
		checkForPaddleAndWallBoundary();
	}

	private void checkForBallAndWallCollision() {
		if (topWall.isCollision(ball) && ball.isMovingUp()) {
			ball.moveDown(ballSpeed);
			AudioHitWall.play();
		}
		if (bottomWall.isCollision(ball) && ball.isMovingDown()) {
			ball.moveUp(ballSpeed);
			AudioHitWall.play();
		}
		if (isPlaying == false) {
			if (leftWall.isCollision(ball) && ball.isMovingLeft()) {
				ball.moveRight(ballSpeed);
				AudioHitWall.play();
			}
			if (rightWall.isCollision(ball) && ball.isMovingRight()) {
				ball.moveLeft(ballSpeed);
				AudioHitWall.play();
			}
		}
	}

	private void checkForBallCollisionRight() {
		if (rightPaddle.isCollision(ball) && ball.isMovingRight()) {
			if (isPlaying) {
				p2++;
				if (isFiring) {
					isFiring = false;
					p2++;
					ballSpeed -= 2;
				}
				if (p2FBs == 3) {
					if (p2fire) {
						ballSpeed += 2;
						isFiring = true;
						p2fire = false;
						p2FBs = 0;
						setFBpower(p2FB, p2FBs);
						AudioFire.play();
					}
				} else {
					ballSpeed *= 1.05;
					if (p2FBs <= 3) {
						p2FBs++;
						setFBpower(p2FB, p2FBs);
					}
				}
			}
			ball.moveLeft(ballSpeed);
			AudioHit.play();
		}
	}

	private void checkForBallCollisionLeft() {

		if (leftPaddle.isCollision(ball) && ball.isMovingLeft()) {
			if (isPlaying) {
				p1++;
				if (isFiring) {
					isFiring = false;
					p1++;
					ballSpeed -= 2;
				}
				if (p1FBs == 3) {
					if (p1fire) {
						ballSpeed += 2;
						isFiring = true;
						p1fire = false;
						p1FBs = 0;
						setFBpower(p1FB, p1FBs);
						AudioFire.play();
					}
				} else {
					ballSpeed *= 1.05;
					if (p1FBs <= 3) {
						p1FBs++;
						setFBpower(p1FB, p1FBs);
					}
				}
			}
			ball.moveRight(ballSpeed);
			AudioHit.play();
		}

	}

	private void checkForPaddleAndWallBoundary() {
		double rightWallEdge = rightWall.getX();
		double leftWallEdge = leftWall.getX() + leftWall.getWidth();
		double topWallEdge = topWall.getY() + topWall.getHeight();
		double bottomWallEdge = bottomWall.getY() - rightPaddle.getHeight();
		double midWallEdgeX = midWall.getX() + midWall.getWidth();

		if (rightPaddle.getX() > rightWallEdge) {
			rightPaddle.move(rightWallEdge - rightPaddle.getWidth(), rightPaddle.getY());
		}
		if (rightPaddle.getX() < midWallEdgeX) {
			rightPaddle.move(midWallEdgeX, rightPaddle.getY());
		}
		if (rightPaddle.getY() < topWallEdge) {
			rightPaddle.move(rightPaddle.getX(), topWallEdge);
		}
		if (rightPaddle.getY() > bottomWallEdge) {
			rightPaddle.move(rightPaddle.getX(), bottomWallEdge);
		}

		if (leftPaddle.getX() > midWallEdgeX) {
			leftPaddle.move(midWallEdgeX - midWall.getWidth(), leftPaddle.getY());
		}
		if (leftPaddle.getX() < leftWallEdge) {
			leftPaddle.move(leftWallEdge, leftPaddle.getY());
		}
		if (leftPaddle.getY() < topWallEdge) {
			leftPaddle.move(leftPaddle.getX(), topWallEdge);
		}
		if (leftPaddle.getY() > bottomWallEdge) {
			leftPaddle.move(leftPaddle.getX(), bottomWallEdge);
		}
	}

	private void checkIfGameOver() {
		if (rightWall.isCollision(ball) && ball.isMovingRight() ||
				leftWall.isCollision(ball) && ball.isMovingLeft()) {
			if (rightWall.isCollision(ball) && ball.isMovingRight()) {
				result.SetLayoutX(W / 5);
				result.SetText("Player Red WIN");
				result.SetVisible(true);
				AudioP1.play();
			} else {
				result.SetLayoutX(W - 250);
				result.SetText("Player Blue WIN");
				result.SetVisible(true);
				AudioP2.play();
			}
			writeScore();
			setFBpower(p1FB, 0);
			setFBpower(p2FB, 0);
			p1FBs = 0;
			p2FBs = 0;
			ballSpeed = 1.5;
			p1 = 0;
			p2 = 0;
			ballAnimation.stop();
			startButton.setVisible(true);
			hideHelpButton.setVisible(true);
			showHelpButton.setVisible(true);
			logoShow.setVisible(true);
			ball.setCenterX(W / 2);
			ball.setCenterY(H / 2);
			AudioBGMPlay.stop();
			AudioBGMWait.play();
			makePaddleReturn();
			ballWaitAnimation.play();
			AudioBGMWait.play();
			logoSetting(2000, 0.5, 1.0, 0);
			updateScoreAndSpeed();
		}
	}

	private void controlPaddleXY() {
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				int d1x = 0, d2x = 0, d1y = 0, d2y = 0;

				if (goBar2UP)
					d1x -= 5;
				if (goBar2Down)
					d1x += 5;
				if (goBar2Left)
					d1y -= 5;
				if (goBar2Right)
					d1y += 5;
				if (goBar1UP)
					d2x -= 5;
				if (goBar1Down)
					d2x += 5;
				if (goBar1Left)
					d2y -= 5;
				if (goBar1Right)
					d2y += 5;
				if (running) {
					d2x *= 3;
					d1x *= 3;
				}
				if (rightPaddle.getX() >= 5 && rightPaddle.getX() <= W - 5)
					rightPaddle.moveDown(d1x);
				if (rightPaddle.getY() >= 5 && rightPaddle.getY() <= W - 5)
					rightPaddle.moveRight(d1y);
				if (leftPaddle.getX() >= 5 && leftPaddle.getX() <= W - 5)
					leftPaddle.moveDown(d2x);
				if (leftPaddle.getY() >= 5 && leftPaddle.getY() <= W - 5)
					leftPaddle.moveRight(d2y);
				checkForPaddleAndWallBoundary();

			}
		};
	}

	private void updateScoreAndSpeed() {
		score1.SetText("Player Red: " + p1);
		score2.SetText("Player Blue: " + p2);
		speedOf.SetText("Now Speed is: " + String.format("%.2f", ballSpeed));
	}

	private void writeScore() {
		String str = "Player Red Score:" + p1 + "  Player Blue Score:" + p2 + "  @ballSpeed:" + String.format("%.2f", ballSpeed);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter("OutputFile.txt", true);
			bw = new BufferedWriter(fw);

			bw.write(str);
			bw.newLine();
		} catch (IOException e) {
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}

	public void Instruction() {
        System.out.println("Everything is here");


    }

	private void keyboardEvents() {

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					goBar2UP = true;
					break;
				case DOWN:
					goBar2Down = true;
					break;
				case LEFT:
					goBar2Left = true;
					p2fire = true;
					break;
				case RIGHT:
					goBar2Right = true;
					p2fire = false;
					break;
				case W:
					goBar1UP = true;
					break;
				case S:
					goBar1Down = true;
					break;
				case A:
					goBar1Left = true;
					p1fire = false;
					break;
				case D:
					goBar1Right = true;
					p1fire = true;
					break;
				case SHIFT:
					running = true;
					break;
				case Q:
					if (p1fire) {
						p1fire = false;
					} else
						p1fire = true;
					break;
				case M:
					p2fire = true;
					break;
				default:
					break;
				}
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					goBar2UP = false;
					break;
				case DOWN:
					goBar2Down = false;
					break;
				case LEFT:
					goBar2Left = false;
					p2fire = false;
					break;
				case RIGHT:
					goBar2Right = false;
					break;
				case W:
					goBar1UP = false;
					break;
				case S:
					goBar1Down = false;
					break;
				case A:
					goBar1Left = false;
					break;
				case D:
					goBar1Right = false;
					p1fire = false;
					break;
				case SHIFT:
					running = false;
					break;
				default:
					break;
				}
			}
		});

	}

	private void makePaddleReturn() {
		leftPaddle.move(20, (H / 2) - 50);
		rightPaddle.move(W - 30, (H / 2) - 50);
	}
}