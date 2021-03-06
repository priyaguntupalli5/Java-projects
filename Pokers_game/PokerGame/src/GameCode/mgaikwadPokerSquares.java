package GameCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/* A version of the solitaire game "Poker Squares" with variable point systems.
 * Author: Todd W. Neller

Notes: 

A Poker Squares grid is represented as a 5-by-5 array of Card objects.  A null indicates an empty position.
In the solitaire game of Poker Squares, a deck is initially shuffled.
Each turn, the player draws a card from the deck and places it in any empty cell of a 5-by-5 grid.  Once placed, cards may not be moved.
After the last cell is filled, each row and column are scored according to a point system for Poker Squares hands.
For example, the American system is as follows:
100 - Royal Flush: A T-J-Q-K-A sequence all of the same suit. Example: TC, JC, QC, KC, AC.
75 - Straight Flush: Five cards in sequence all of the same suit. Example: AD, 2D, 3D, 4D, 5D.
50 - Four of a Kind: Four cards of the same rank. Example: 9C, 9D, 9H, 9S, 6H.
25 - Full House: Three cards of one rank with two cards of another rank. Example: 7S, 7C, 7D, 8H, 8S.
20 - Flush: Five cards all of the same suit. Example: AH, 2H, 3H, 5H, 8H.
15 - Straight: Five cards in sequence. Aces may be high or low but not both. (Straights do not wrap around.) Example: 8C, 9S, TH, JD, QC.
10 - Three of a Kind: Three cards of the same rank. Example: 2S, 2H, 2D, 5C, 7S.
5 - Two Pair: Two cards of one rank with two cards of another rank. Example: 3H, 3D, 4C, 4S, AC.
2 - One Pair: Two cards of one rank.  Example: 5D, 5H, TC, QS, AH.
(0 otherwise)

The player's total score is the sum of the scores for each of the 10 row and column hands.

For our purposes, a player is considered better if it has a higher expected game score, i.e. has a higher score average over many games.

In our implementation, each turn a PokerSquaresPlayer will be passed (1) a Card object and (2) the number of milliseconds remaining in the game, 
and will return a length 2 integer array with the row and column the player placed the card.  In the event that the player makes an illegal 
play or "times out", i.e. runs out of time for play, the player loses with a final score of 10 times the minimum hand score.

This file contains not only the code to run a simple demonstration game with a random player, but also code to perform batch game testing,
and tournament evaluation.

5/29/2015: Fixed inconsistent point system printing and usage in main method demo.

 */

public class mgaikwadPokerSquares {

	public static final int SIZE = 5; // square grid size
	public static final long POINT_SYSTEM_MILLIS = 10000L;
	public static final long GAME_MILLIS = 30000L; // a total of 30 seconds (30000 milliseconds) per game

	private mgaikwadPokerSquaresPlayer player; // current player
	private mgaikwadPokerSquaresPointSystem system; // current point system
	private long gameMillis = GAME_MILLIS; // maximum milliseconds for current game
	private boolean verbose = true; // whether or not to print move-by-move transcript of the game
	private mgaikwadCard[][] grid = new mgaikwadCard[SIZE][SIZE]; // current game grid
	private Random random = new Random(); // current game random number generator
	private int minPoints; // minimum possible score for current point system.
	private long millisRemaining = gameMillis, startTime = 0;
	private int cardPositions = 0;
	private int currentLevel = -1;
	private int index = 0;
	public static int max_score;
	public mgaikwadCard[][] grid_copy;
	public ArrayList<mgaikwadCard[][]> list;
	ArrayList<Integer> scores;
	Map<String, List<int[]>> cardSeenMap = new HashMap<>();
	boolean cardMatchedWithMap = false;

	/**
	 * Create a PokerSquares game with a given player and point system.
	 * 
	 * @param player Poker Squares player object
	 * @param system current Poker Squares point system
	 */
	public mgaikwadPokerSquares(mgaikwadPokerSquaresPlayer player, mgaikwadPokerSquaresPointSystem system) {
		this.player = player;
		this.system = system;
		minPoints = Integer.MAX_VALUE;
		for (int points : system.getScoreTable())
			if (points < minPoints)
				minPoints = points;
		minPoints *= 10;
		final mgaikwadPokerSquaresPlayer PLAYER = player;
		final mgaikwadPokerSquaresPointSystem SYSTEM = system;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				PLAYER.setPointSystem(SYSTEM, POINT_SYSTEM_MILLIS);
			}
		});
		thread.start();
		try {
			thread.join(POINT_SYSTEM_MILLIS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play a game of Poker Squares and return the final game score.
	 * 
	 * @return final game score
	 */
	public int play() {
		try {
			this.cardPositions = 0;
			this.currentLevel = -1;
			list = new ArrayList<>();
		     max_score = 0;
		   scores = new ArrayList<>();
			player.init();
			// shuffle deck
			Stack<mgaikwadCard> deck = new Stack<mgaikwadCard>();
			for (mgaikwadCard card : mgaikwadCard.getAllCards())
				deck.push(card);
			Collections.shuffle(deck, random);
			// clear grid
			for (int row = 0; row < SIZE; row++)
				for (int col = 0; col < SIZE; col++)
					grid[row][col] = null;
			
			// play game
			mgaikwadCard card = null;
			// empty grid added to list
			list.add(grid);
			scores.add(0);
			int totalGrids = 1;
			int previousLevelGrids = 0;
			boolean newLevel = false;
			
			for (index = 0; index < list.size(); index++) {	//traverse the tree using list
				mgaikwadCard[][] temp = list.get(index);
				this.startTime = System.currentTimeMillis();

				if (millisRemaining < 0) {	// times out
					System.err.println("Player Out of Time");
					return minPoints;
				} else {

					if (index == previousLevelGrids) {	//move to next level if current level is traversed entirely
						
						if (deck.empty()) {	//check if deck is empty
							System.out.println("Deck Exhausted!!!!");
							System.out.println("Maximum score" + max_score);
							break;
						}
						
						card = deck.pop();	//pop a new card for each level
						currentLevel++;
						newLevel = true;
						if (scores.size() != 1 && scores.size() != 0) {	
							max_score = Collections.max(scores);	//calculating maximum score for current level
							//System.out.println("max score : " + max_score);
						}

						scores.clear();	//clear the list after maximum score is computed for each level
					}
					
					if (newLevel) {
						previousLevelGrids = totalGrids;
						newLevel = false;
					}
					
					//millisRemaining -= System.currentTimeMillis() - startTime;
					placeCard(temp, card);
					
					if (currentLevel == 0 || cardMatchedWithMap)	//only 1 grid will be generated if rank or suit matches
						totalGrids += 1;
					else
						totalGrids += SIZE * SIZE - currentLevel;
					cardPositions = 0;
				}
			}
			
			return max_score;

		} catch (Exception e) {
			System.err.println("Exception thrown by " + player.getName() + ":");
			e.printStackTrace();
			return minPoints;
		}

	}

	// function to place a card
	public int placeCard(mgaikwadCard[][] grid, mgaikwadCard card) {
		if (cardPositions == 0) { // to solve reference copy issue
			list.remove(index);
			grid_copy = Arrays.stream(grid).map(mgaikwadCard[]::clone).toArray(mgaikwadCard[][]::new);
			list.add(index, grid_copy);
		}
		boolean newLevel = false;
		if (cardPositions < SIZE * SIZE - currentLevel) {

			// below line of code to generate random position in grid to place a card
			int[] play = player.getPlay(card, millisRemaining);

			millisRemaining -= System.currentTimeMillis() - startTime;
			if (millisRemaining < 0) {
				return minPoints;
			}

			if (play.length != 2 || play[0] < 0 || play[0] >= SIZE || play[1] < 0 || play[1] >= SIZE
					|| grid[play[0]][play[1]] != null) { // illegal play

			} else {	//generate all possible grids of a card if there is no match of rank or suit.
				String cardStr = card.toString();
				char[] cardArr = cardStr.toCharArray();
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 5; j++) {
						cardMatchedWithMap = false;
						if (grid[i][j] != null) {
							char[] cardPresentInGrid = grid[i][j].toString().toCharArray();
							if ((cardArr[0] == cardPresentInGrid[0] && cardArr[1] != cardPresentInGrid[1])
									|| (cardArr[0] != cardPresentInGrid[0] && cardArr[1] == cardPresentInGrid[1])) {
								cardMatchedWithMap = true;	 //Place a card in a particular row or column in which match is found
								for (int k = 0; k < 5; k++) {
									if ((grid[i][k]) == null) {
										play[0] = i;
										play[1] = k;
										break;
									}
									if ((grid[k][j]) == null) {
										play[1] = k;
										play[0] = j;
										break;
									}
								}
							} else {
								cardMatchedWithMap = false;
							}
						}
						if (cardMatchedWithMap)
							break;
					}
					if (cardMatchedWithMap)
						break;
				}

				grid[play[0]][play[1]] = card;

				if (list.size() == 1 || cardMatchedWithMap) {
					cardPositions = SIZE * SIZE - currentLevel;	//eliminate all other possible grids
				} else {
					cardPositions++;
				}

				grid_copy = Arrays.stream(grid).map(mgaikwadCard[]::clone).toArray(mgaikwadCard[][]::new);
				list.add(grid_copy); // add the new grid generated
				scores.add(system.getScore(grid_copy));	//add score of a particular grid

				newLevel = true;

				if (newLevel && currentLevel != 0)	
					grid[play[0]][play[1]] = null;

			}
			return placeCard(grid, card);
		} else
			return minPoints;
	}

	/**
	 * Play a sequence of games, collecting and reporting statistics.
	 * 
	 * @param numGames  number of games to play
	 * @param startSeed seed of first game. Successive games use successive seeds
	 * @param verbose   whether or not to provide verbose output of game play
	 * @return integer array of game scores
	 */
	public int[] playSequence(int numGames, long startSeed, boolean verbose) {
		this.verbose = verbose;
		if (verbose) {
			System.out.printf("%d games starting at seed %d\nPoint system:\n%s\n", numGames, startSeed, system);
		}
		int[] scores = new int[numGames];
		double scoreMean = 0;
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < numGames; i++) {
			this.millisRemaining = gameMillis;
			setSeed(startSeed + i);
			int score = play();
			scores[i] = score;
			scoreMean += score;
			if (scores[i] < min)
				min = scores[i];
			if (scores[i] > max)
				max = scores[i];
			int gameSeq = i+1;
			System.out.println("Maximum Score for game "+ gameSeq + ":" + score);
		}
		scoreMean /= numGames;
		double scoreStdDev = 0;
		for (int i = 0; i < numGames; i++) {
			double diff = scores[i] - scoreMean;
			scoreStdDev += diff * diff;
		}
		scoreStdDev = Math.sqrt(scoreStdDev / numGames);
		System.out.printf("Score Mean: %f, Standard Deviation: %f, Minimum: %d, Maximum: %d\n", scoreMean, scoreStdDev,
				min, max);
		return scores;
	}

	/**
	 * Hold a Poker Squares tournament between the given players and point systems,
	 * the number of games played by each player with each point system, and the
	 * start seed determining the sequence of deals all will encounter, returning an
	 * array of doubles corresponding to the tournament score of each player.
	 * Tournament scores are determined as follows. For each tournament, average
	 * scores are linearly scaled and transformed such that the player with the
	 * maximum or minimum average score receive a tournament score of 1.0 or 0.0
	 * respectively, with all other players receiving linearly scaled tournament
	 * scores between 0.0 and 1.0. For each scoring system, a player receives a
	 * tournament score, and in the ith position of the return array is the sum of
	 * all tournament scores of the ith player.
	 * 
	 * @param players        Poker Squares players taking part in the tournament
	 * @param systems        Poker Squares point systems used to evaluate players in
	 *                       the tournament
	 * @param gamesPerSystem the number of games that will be played by each player
	 *                       with each point system
	 * @param startSeed      the start seed for the pseudorandom number generator
	 *                       that generates card deals
	 * @return the sum of the tournament scores for each of the given players
	 */
	public static double[] playTournament(ArrayList<mgaikwadPokerSquaresPlayer> players,
			ArrayList<mgaikwadPokerSquaresPointSystem> systems, int gamesPerSystem, long startSeed) {
		double[] tournamentScores = new double[players.size()];
		for (mgaikwadPokerSquaresPointSystem system : systems) { // for each point system
			System.out.println("Point System:\n" + system);
			int[] totalScores = new int[players.size()];
			for (int i = 0; i < players.size(); i++) { // for each player
				mgaikwadPokerSquaresPlayer player = players.get(i);
				System.out.printf("Player: \"%s\"\n", player.getName());
				int[] scores = new mgaikwadPokerSquares(player, system).playSequence(gamesPerSystem, startSeed, false);
				for (int score : scores)
					totalScores[i] += score;
				System.out.printf("Player \"%s\" total score: %d\n", player.getName(), totalScores[i]);
			}
			int maxTotal = Integer.MIN_VALUE;
			int minTotal = Integer.MAX_VALUE;
			for (int totalScore : totalScores) {
				if (totalScore > maxTotal)
					maxTotal = totalScore;
				if (totalScore < minTotal)
					minTotal = totalScore;
			}
			for (int i = 0; i < players.size(); i++) { // for each player
				double normalizedTotal = (double) (totalScores[i] - minTotal) / (maxTotal - minTotal);
				System.out.println("Player \"" + players.get(i).getName() + "\" normalized score: " + normalizedTotal);
				tournamentScores[i] += normalizedTotal;
			}
		}
		double max = Double.NEGATIVE_INFINITY;
		for (double score : tournamentScores) {
			if (score > max)
				max = score;
		}
		System.out.printf("%20s %s\n", "Player", "Tournament Score");
		for (int i = 0; i < players.size(); i++) { // for each player
			System.out.printf("%20s %f\n", players.get(i).getName(), tournamentScores[i]);
		}

		return tournamentScores;
	}

	/**
	 * Set the seed of the game pseudorandom number generator.
	 * 
	 * @param seed pseudorandom number generator seed
	 */
	private void setSeed(long seed) {
		random.setSeed(seed);
	}

	/**
	 * Demonstrate single/batch game play testing and tournament evaluation of
	 * PokerSquaresPlayers.
	 * 
	 * @param args (not used)
	 */
	public static void main(String[] args) {

		// Demonstration of single game play (30 seconds)
//		System.out.println("Single game demo:");
//		PokerSquaresPointSystem.setSeed(0L);
//		PokerSquaresPointSystem system = PokerSquaresPointSystem.getBritishPointSystem();
//		System.out.println(system);
//		new PokerSquares(new RandomPlayer(), system).play();

		// Demonstration of batch game play (30 seconds per game)
//		System.out.println("\n\nBatch game demo:");
//		System.out.println(system);
//		new PokerSquares(new RandomPlayer(), system).playSequence(3, 0, true);

		// Demonstration of tournament evaluation (2 players, 3 point systems, 10 x 30s
		// games for each of the 2*3=6 player-system pairs)
		System.out.println("\n\nTournament evaluation demo:");

		ArrayList<mgaikwadPokerSquaresPlayer> players = new ArrayList<mgaikwadPokerSquaresPlayer>();
		players.add(new mgaikwadRandomPlayer());
		// players.add(new FlushPlayer());

		ArrayList<mgaikwadPokerSquaresPointSystem> systems = new ArrayList<mgaikwadPokerSquaresPointSystem>();
		mgaikwadPokerSquaresPointSystem.setSeed(42L);
		// systems.add(PokerSquaresPointSystem.getBritishPointSystem());
		systems.add(mgaikwadPokerSquaresPointSystem.getAmericanPointSystem());
		// systems.add(PokerSquaresPointSystem.getSingleHandPointSystem(PokerHand.FLUSH.id));
		// // 1 point for flushes, 0 for all other hands

		mgaikwadPokerSquares.playTournament(players, systems, 20, 0L); // play 20 games for each player under each scoring
																// system

	}
}