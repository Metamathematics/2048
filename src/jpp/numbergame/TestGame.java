package jpp.numbergame;

import static helper.HelperGame2048.*;

public class TestGame {
	public static void main(String[] args) {

		Tile tile1 = new Tile(new Coordinate2D(1,1),5);
		Coordinate2D coord1 = new Coordinate2D(1,1);
		Coordinate2D coord2 = new Coordinate2D(115,32);
		
		NumberGame game = new NumberGame(2,2,2);
/*
		game.addTile(0, 0, 2);
		game.addTile(2, 0, 4);
		game.addTile(4, 0, 2);
		game.addTile(6, 0, 2);
		*/
		
		move(game);
		
	}
	
	public static void addRandom(NumberGame game) {
		System.out.println(game);
		//Tile randomTile = game.addRandomTile();
		//System.out.println(randomTile);
		
		System.out.println(game.canMove());
		System.out.println(game);
	}
	
	public static void move(NumberGame game) {
		System.out.println(game);
		game.move(Direction.LEFT);
		System.out.println(game);
	}
	
	
}
