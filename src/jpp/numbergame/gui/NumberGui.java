package jpp.numbergame.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jpp.numbergame.Direction;
import jpp.numbergame.NumberGame;

public class NumberGui extends Application {

	final int rSize = 500;
	final int arc = 25;

	NumberGame game = new NumberGame(4, 4, 2);
	Label[][] valueLabels;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws InterruptedException {

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, rSize * 1.1, rSize * 1.25);

		valueLabels = newBoard(root);

		root.setTop(new StackPane(getTopLabel()));

		primaryStage.setTitle("2048 by Sergey Gerodes");
		primaryStage.setScene(scene);
		primaryStage.show();

		oneTurn(scene, root);

	}

	public void oneTurn(Scene scene, final BorderPane root) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (game.canMove()) {
					switch (event.getCode()) {
					case RIGHT:
						if (game.canMove(Direction.RIGHT)) {
							game.move(Direction.RIGHT);
							game.addRandomTile();
						}
						break;
					case LEFT:
						if (game.canMove(Direction.LEFT)) {
							game.move(Direction.LEFT);
							game.addRandomTile();
						}
						break;
					case DOWN:
						if (game.canMove(Direction.DOWN)) {
							game.move(Direction.DOWN);
							game.addRandomTile();
						}
						break;
					case UP:
						if (game.canMove(Direction.UP)) {
							game.move(Direction.UP);
							game.addRandomTile();
						}
						break;
					default:
						break;
					}
					// newBoard(root);
					renewLabels(root);
				}
				if (!game.canMove()) {
					gameOver(root);
				}

			}
		});
	}

	public void gameOver(BorderPane root) {
		// newBoard(root);
		renewLabels(root);
		Label score = new Label("GAME OVER \n Score: " + String.valueOf(game.getPoints()));
		score.setFont(Font.font(rSize * 30 / 800));
		StackPane bottom = new StackPane(score);
		root.setBottom(bottom);
	}

	public Label[][] newBoard(BorderPane root) {

		Label[][] valueLabels = new Label[4][4];

		Group gr = new Group();
		StackPane center = new StackPane();

		root.setCenter(center);
		center.getChildren().add(getRectangle("big"));
		center.getChildren().add(gr);

		for (int x = 0; x < 4; ++x) {
			for (int y = 0; y < 4; ++y) {

				StackPane s = new StackPane();

				Label value = valueLabels[x][y] = new Label(getTileValueString(x, y));
				value.setFont(Font.font(rSize * 65 / 800));

				s.getChildren().add(getRectangle("small"));
				s.getChildren().add(value);
				s.relocate(x * rSize / 4, y * rSize / 4);

				root.setBottom(new StackPane(getScoreLabel()));
				gr.getChildren().add(s);
			}
		}
		root.setCenter(center);

		return valueLabels;
	}

	public void renewLabels(BorderPane root) {
		//int help = 0;
		for (int x = 0; x < 4; ++x) {
			for (int y = 0; y < 4; ++y) {
				/*
				 * Label l = (Label)((StackPane)((Group)((
				 * StackPane)root.getCenter()).
				 * getChildren().get(1)).getChildren
				 * ().get(help)).getChildren().get(1 ); help++;
				 * l.setText(getTileValueString(x, y));
				 */
				valueLabels[x][y].setText(getTileValueString(x, y));
				root.setBottom(new StackPane(getScoreLabel()));
			}
		}
	}

	public Rectangle getRectangle(String s) {
		if (s.equals("small")) {
			Rectangle r = new Rectangle(rSize / 4.2, rSize / 4.2);
			r.setArcHeight(arc);
			r.setArcWidth(arc);
			r.setFill(Color.LIGHTGREY);
			return r;
		} else if (s.equals("big")) {
			Rectangle centerRect = new Rectangle(rSize * 1.03, rSize * 1.03);
			centerRect.setArcHeight(arc);
			centerRect.setArcWidth(arc);
			centerRect.setFill(Color.DARKGRAY);
			return centerRect;
		}
		return null;
	}

	public String getTileValueString(int x, int y) {
		String value = "";
		int points = game.get(x, y);

		if (points > 0) {
			value = String.valueOf(game.get(x, y));
		}
		return value;
	}

	public Label getScoreLabel() {
		Label score = new Label("Score: " + String.valueOf(game.getPoints()));
		score.setFont(Font.font(rSize * 40 / 800));
		return score;
	}

	public Label getTopLabel() {
		Label gameName = new Label("The 2048 game");
		gameName.setFont(Font.font(rSize * 40 / 800));
		return gameName;
	}
}
