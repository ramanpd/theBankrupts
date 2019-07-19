/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import Sprint4v2.Dice;
import Sprint4v2.player.Player;
import Sprint4v2.player.Token;
import Sprint4v2.player.ChanceCard;
import Sprint4v2.player.CommunityChestCards;

// class responsible for cammand inputs

public class CommandListener {
	int dice1Value, dice2Value; // value of dice
	private int tempDice1, tempDice2;
	int playerNumber = 0; // current number of players playing
	private int numberOfPlayers; // total nubmer of players
	private Player[] players; // will hold player info
	private int countOfPlayers;
	private static int noOFDiceRollsForJailedPerson;
	// state 0 to roll
	// state 1 to pay rent
	// state 2 to done
	// state 3 to implement a chance card
	// state 4 to implement a tax card
	// state 5 to implement community chest
	// state 6 to implement free parking
	// state 7 to implement jail
	// state 8 to implement go
	// state 9 to pay fine
	// state 10 to buy
	// state 11 to bankrupt
	// state 12 when imprisoned
	// state 99 to quit when only one player is left
	private static int state = 0;
	private int sumOfDice = 0; // sum of both dice
	public BoardBox boardBoxes[]; // holds boxes
	private Token tokens[]; // holds tokens
	private static int noOfDoublesRolled = 0;
	private boolean playerIsJustReleasedFromJail = false;
	CommunityChestCards communityChest;
	ChanceCard chanceCard;

	// Constructor initialises class values
	CommandListener(int nrOfPlayers, Player playersPlaying[], BoardBox boardBox[], Token tokens[]) {
		numberOfPlayers = nrOfPlayers;
		countOfPlayers = nrOfPlayers;
		players = playersPlaying;
		this.boardBoxes = boardBox;
		this.tokens = tokens;
		communityChest = new CommunityChestCards(playersPlaying, tokens);
		chanceCard = new ChanceCard(playersPlaying, tokens, boardBoxes);
	}

	// method for dice sum, changing it to zero after return
	int[] getDiceSum() {
		int[] temp = new int[2];
		temp[0] = tempDice1;
		temp[1] = tempDice2;
		sumOfDice = 0;
		tempDice1 = 0;
		tempDice2 = 0;
		return temp;
	}

	// method for most command inputs in the game (brief)
	public String checkForCommands(String commandInput) {
		String commandOutput = "";

		// all the controls ignore text cases
		if (commandInput.equalsIgnoreCase("ROLL") && (state == 0 || state == 12)) // if
																					// input
																					// is
		// ROLL
		{
			
			Dice dice = new Dice();
			dice1Value = dice.getDie1();
			dice2Value = dice.getDie2();
			tempDice2 = dice2Value;
			tempDice1 = dice1Value;
			sumOfDice = (dice1Value + dice2Value);

			// displays the dice
			commandOutput = "Dices Rolled:\n Dice 1: " + dice1Value + " Dice 2: " + dice2Value + " Sum: "
					+ (dice1Value + dice2Value);
			if (state == 12) {
				if (dice1Value == dice2Value) {
					noOFDiceRollsForJailedPerson = 0;
					state = 0;
					playerIsJustReleasedFromJail = true;
				} else {
					noOFDiceRollsForJailedPerson = noOFDiceRollsForJailedPerson + 1;
					if (noOFDiceRollsForJailedPerson == 3) {
						commandOutput = commandOutput + "\n"
								+ (players[playerNumber].getPlayerName() + " paid $50 fine to get out of jail!\n");
						players[playerNumber].decrementPlayerBalance(50);
						players[playerNumber].setTakenToJailStatus(false);
						playerIsJustReleasedFromJail = true;
						noOFDiceRollsForJailedPerson = 0;
						state = 0;
					}
					else
					{
						tokens[playerNumber].moveToken(40-(dice1Value+dice2Value));
						state=2;
					}
				}
			}
			if (state == 0) {
				state = 2; // change states accordingly
				// if player passess GO add 200
				if (((players[playerNumber].getPlayerPosititonOnBoard() + sumOfDice) % 40)
						- players[playerNumber].getPlayerPosititonOnBoard() < 0) {
					players[playerNumber].incrementPlayerBalance(200);
				}

				// if player rolld DOUBLES
				if (!playerIsJustReleasedFromJail) {
					if (dice1Value == dice2Value) { // check for special cases
						commandOutput = commandOutput + "**DOUBLES!**";
						noOfDoublesRolled = noOfDoublesRolled + 1;
						state = 0;
					}
				} else {
					playerIsJustReleasedFromJail = false;
				}

				// displays the current box/property Info
				if ((boardBoxes[(tokens[playerNumber].getBoxNo() + sumOfDice) % 40].getCardType() == 1)
						|| (boardBoxes[(tokens[playerNumber].getBoxNo() + sumOfDice) % 40].getCardType() == 8)
						|| (boardBoxes[(tokens[playerNumber].getBoxNo() + sumOfDice) % 40].getCardType() == 9)) {
					commandOutput = commandOutput + "\nProperty Information:\n" + "Property Name:"
							+ boardBoxes[(tokens[playerNumber].getBoxNo() + sumOfDice) % 40].getBoxName()
							+ "\nPrice of Property: "
							+ boardBoxes[(tokens[playerNumber].getBoxNo() + sumOfDice) % 40].getValue()
							+ "\nRent of Property: "
							+ boardBoxes[(tokens[playerNumber].getBoxNo() + sumOfDice) % 40].getRentValue();

					// in case property is owned & checks info
					if (boardBoxes[(tokens[playerNumber].getBoxNo() + (dice1Value + dice2Value)) % 40]
							.checkIfOwned() != 0) {

						commandOutput = commandOutput + "\nOwnerShip Status: Owned by "
								+ players[boardBoxes[(tokens[playerNumber].getBoxNo() + sumOfDice) % 40].checkIfOwned()
										- 1].getPlayerName();
						// state = 1;
					} else {
						commandOutput = commandOutput + "\nOwnerShip Status: No Owner";
					}
				}

				// sets player position on board
				players[playerNumber]
						.setPlayerPositionOnBoard((players[playerNumber].getPlayerPosititonOnBoard() + sumOfDice) % 40);

				// checks if 3 doubles rolled
				if (noOfDoublesRolled == 3) {
					players[playerNumber].setTakenToJailStatus(true);
					if (players[playerNumber].getPlayerPosititonOnBoard() > 10) {
						tokens[playerNumber].moveToken(50 - players[playerNumber].getPlayerPosititonOnBoard());
					} else {
						tokens[playerNumber].moveToken(10 - players[playerNumber].getPlayerPosititonOnBoard());
					}
					players[playerNumber].setPlayerPositionOnBoard(10);
					noOfDoublesRolled = 0;
					commandOutput = commandOutput + "3 DOUBLES rolled, therefore sent to jail!\n";
					commandOutput = commandOutput + players[playerNumber].getPlayerName()
							+ " is imprisoned.\nPlease show Get out of jail card or pay $50 fine or roll DOUBLES in any of your next three turns.\n";
					state = 2;
				} else {
					if (state != 0) {
						if ((boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 1)
								|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 8)
								|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 9)) {

							if ((boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].checkIfOwned() == 0)) {
								state = 10;
							} else {
								commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName() + ":"
										+ " paid the rent!\n";
								if (players[playerNumber].getPlayerBalance()
										- boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
												.getRentValue() >= 0) {
								} else {
									commandOutput = commandOutput
											+ "Negative Balance, mortage or sell properties to continue the game, otherwise declare Bankrupt!";

								}
								state = 2;

							}

						} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 2) {

							chanceCard.implementChanceCardForPlayer(playerNumber);
							commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName() + ":"
									+ " draws Chance Card!\n";
							commandOutput = commandOutput + "Card reads: " + chanceCard.getChanceCardDescription()
									+ "\n";
							if (chanceCard.getChanceCardType() == 2 || chanceCard.getChanceCardType() == 8
									|| chanceCard.getChanceCardType() == 7) {
								if ((boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 1)
										|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
												.getCardType() == 8)
										|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
												.getCardType() == 9)) {

									if ((boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
											.checkIfOwned() == 0)) {
										state = 10;
									} else {
										commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName()
												+ ":" + " paid the rent!\n";
										if (players[playerNumber].getPlayerBalance()
												- boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
														.getRentValue() >= 0) {
										} else {
											commandOutput = commandOutput
													+ "Negative Balance, mortage or sell properties to continue the game, otherwise declare Bankrupt!";

										}
										state = 2;

									}

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 3) {
									commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName() + ":"
											+ " paid $200 Income tax!\n";
									state = 2;

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 5) {
									commandOutput = commandOutput + "\n" + "Welcome to the Free Parking!\n";
									state = 2;

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 6) {

									if (players[playerNumber].getTakenToJailStatus() == false) {
										commandOutput = commandOutput + "\nWelcome to Jail, thanks for visiting\n";
										state = 2;
									} else {
										commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName()
												+ " is imprisoned.\nPlease show Get out of jail card or pay $50 fine or roll DOUBLES in any of your next three turns.";
										state = 2;
									}

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 7) {
									commandOutput = commandOutput + "\n" + "Welcome to Go!\n";
									state = 2;

								}
							} else {
								state = 2;
							}
						} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 3) {
							commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName() + ":"
									+ " paid $200 Income tax!\n";
							state = 2;

						} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 4) {

							communityChest.implementCommunityChestForPlayer(playerNumber);
							commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName() + ":"
									+ " draws Community Chest!\n";
							commandOutput = commandOutput + "Card reads: "
									+ communityChest.getCommunityCardDescription() + "\n";
							if (communityChest.getCommunityCardType() == 2
									|| communityChest.getCommunityCardType() == 7) {
								if ((boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 1)
										|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
												.getCardType() == 8)
										|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
												.getCardType() == 9)) {

									if ((boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
											.checkIfOwned() == 0)) {
										state = 10;
									} else {
										commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName()
												+ ":" + " paid the rent!\n";
										if (players[playerNumber].getPlayerBalance()
												- boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
														.getRentValue() >= 0) {
										} else {
											commandOutput = commandOutput
													+ "Negative Balance, mortage or sell properties to continue the game, otherwise declare Bankrupt!";

										}
										state = 2;

									}

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 3) {
									commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName() + ":"
											+ " paid $200 Income tax!\n";
									state = 2;

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 5) {
									commandOutput = commandOutput + "\n" + "Welcome to the Free Parking!\n";
									state = 2;

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 6) {

									if (players[playerNumber].getTakenToJailStatus() == false) {
										commandOutput = commandOutput + "\nWelcome to Jail, thanks for visiting\n";
										state = 2;
									} else {
										commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName()
												+ " is imprisoned.\nPlease show Get out of jail card or pay $50 fine or roll DOUBLES in any of your next three turns.";
										state = 2;
									}

								} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()]
										.getCardType() == 7) {
									commandOutput = commandOutput + "\n" + "Welcome to Go!\n";
									state = 2;

								}
							} else {
								state = 2;
							}

						} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 5) {
							commandOutput = commandOutput + "\n" + "Welcome to the Free Parking!\n";
							state = 2;

						} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 6) {
							if (players[playerNumber].getPlayerPosititonOnBoard() > 10) {
								players[playerNumber].setPlayerPositionOnBoard(10);
								players[playerNumber].setTakenToJailStatus(true);
								tokens[playerNumber].moveToken(20);
							}
							if (players[playerNumber].getTakenToJailStatus() == false) {
								commandOutput = commandOutput + "\nWelcome to Jail, thanks for visiting\n";
								state = 2;
							} else {
								commandOutput = commandOutput + "\n" + players[playerNumber].getPlayerName()
										+ " is imprisoned.\nPlease show Get out of jail card or pay $50 fine or roll DOUBLES in any of your next three turns.";
								state = 2;
							}

						} else if (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 7) {
							commandOutput = commandOutput + "\n" + "Welcome to Go!\n";
							state = 2;

						}
					}
				}
			}
		} else if (commandInput.equalsIgnoreCase("Card") && state == 12) {
			if (players[playerNumber].getNoOfGetOutOfJailCards() > 0) {
				commandOutput = commandOutput + "\nUsed the get out of Jail card";
				players[playerNumber].setTakenToJailStatus(false);
				noOFDiceRollsForJailedPerson = 0;
				state = 0;
			} else {
				commandOutput = commandOutput + "\nDo not have Get Out Of Jail Card";
				state = 12;
			}
		} else if (commandInput.equalsIgnoreCase("PAY") && state == 12) {
			commandOutput = commandOutput + "\nPaid $50 fine, now you can roll.";
			players[playerNumber].decrementPlayerBalance(50);
			players[playerNumber].setTakenToJailStatus(false);
			noOFDiceRollsForJailedPerson = 0;
			state = 0;
		} else if (commandInput.equalsIgnoreCase("HELP")) // if input is HELP
		{
			// defines all possible accepted commands
			commandOutput = "\t Commands defined are as follows:\n\t ROLL: to roll the dices. \n\t BUY: to buy current spot.\n\tBALANCE: to check the balance.  \n\t PAY RENT: to pay the rent. \n\t PROPERTY: to list the properties owned by the player. \n\t DONE: to end turn.\n\tQUIT: to quit the game\n\tREDEEM <PROPERTY>: to redeem a mortgaged property\n\tMORTGAGE <PROPERTY>: to mortgage a property\n\tBUILD <Property> <Units>: to build house units on a property\n\tDEMOLISH <Property> <Units>: to demolish the house units on a property\n\tBANKRUPT: to declare bankrupcy and end the game\n\tDRAW: to draw a card\n\tPAY FINE: to pay any kind of fine\n\t GOOJ <Player_Name><Price>: offer a get out of Jail";
		} else if (commandInput.equalsIgnoreCase("BANKRUPT") && (state == 11 || state == 2)) // if
		// input
		// is
		// HELP
		{
			// defines all possible accepted commands
			players[playerNumber].activeStatus = false;
			countOfPlayers--;
			commandOutput = players[playerNumber].getPlayerName() + " declared Bankrupt!";
			state = 2;
		}

		else if (commandInput.equalsIgnoreCase("BUY") && state == 10) {
			if (players[playerNumber].getPlayerBalance()
					- boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getValue() >= 0) {
				if (((boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 1)
						|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 8)
						|| (boardBoxes[players[playerNumber].getPlayerPosititonOnBoard()].getCardType() == 9))) {
					commandOutput = "\tProperty Purchased.";
				} else {
					commandOutput = "Cannot be bought";
				}
			} else {
				commandOutput = "Property cannot be purchased due to insufficient funds";
			}

		}

		else if (commandInput.equalsIgnoreCase("done") && (state == 2 || state == 10)
				&& (players[playerNumber].getPlayerBalance() >= 0)) // if
		// input
		// is
		// DONE
		{
			commandOutput = "End of Turn ";
			playerNumber++;

			if (countOfPlayers == 1) {
				state = 99;
			} else {
				if (playerNumber == numberOfPlayers) {
					playerNumber = 0;
				}
				while (!players[playerNumber].isActiveStatus()) {
					playerNumber++;
					if (playerNumber == numberOfPlayers) {
						playerNumber = 0;
					}
				}
				state = 0;
				noOfDoublesRolled = 0;
				if (players[playerNumber].getTakenToJailStatus() == true) {
					state = 12;
				}
			}
		} else if (commandInput.contains("mortgage")) {
			String propertyToBeMortgaged = commandInput.replace("mortgage ", "");
			boolean propertyFound = false;
			for (String s : players[playerNumber].propertiesBought) {
				if (s.contains(propertyToBeMortgaged)) {
					propertyFound = true;
				}
			}
			if (propertyFound) {
				commandOutput = "\t Property Mortgaged ";
			} else {
				commandOutput = "Property not found!";
			}

		}

		else if (commandInput.contains("redeem")) {
			String propertyToBeRedeemed = commandInput.replace("redeem ", "");
			boolean canBeRedeemed = false;
			for (String s : players[playerNumber].propertiesBought) {
				if (s.contains(propertyToBeRedeemed)
						&& (boardBoxes[boardBoxes[0].getBoxNumberByName(s)].getStatusOfBox() == 2)) {
					canBeRedeemed = true;
				}
			}
			if (canBeRedeemed) {
				commandOutput = "\t Property Redeemed ";
			} else {
				commandOutput = "Property not found!";
			}

		} else if (commandInput.contains("build")) // if input is
		// DONE
		{
			String propertyOnWhichToBuilt = commandInput.replace("build ", "");
			String[] part = propertyOnWhichToBuilt.split("(?<=\\D)(?=\\d)");
			propertyOnWhichToBuilt = part[0];
			propertyOnWhichToBuilt = propertyOnWhichToBuilt.trim();
			int noOfUnitsToBeBuild = Integer.parseInt(part[1].trim());
			boolean propertyFound = false;
			for (String s : players[playerNumber].propertiesBought) {
				if (s.contains(propertyOnWhichToBuilt)) {
					propertyFound = true;
					propertyOnWhichToBuilt = s;
				}
			}
			if (players[playerNumber].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerNumber].getBoxNo()]
					.getCardColorGroup()
					- 1] == boardBoxes[0].maximumNoOfCardsInEachColorGroup[boardBoxes[tokens[playerNumber].getBoxNo()]
							.getCardColorGroup() - 1]) {
				if (propertyFound) {
					if (boardBoxes[boardBoxes[0].getBoxNumberByName(propertyOnWhichToBuilt)].getNoOfHousesBuilt()
							+ noOfUnitsToBeBuild <= 5) {
						commandOutput = noOfUnitsToBeBuild + " Units Build";
					} else {
						commandOutput = noOfUnitsToBeBuild + " Units cannot be Build";
					}
				} else {
					commandOutput = "Property not found!";
				}
			} else {
				commandOutput = "Need all properties in the "
						+ boardBoxes[0].colorNames[boardBoxes[tokens[playerNumber].getBoxNo()].getCardColorGroup() - 1]
						+ " color group to build houses\ntherefore, unable to build houses!";
			}
		}

		else if (commandInput.contains("demolish")) // if input
		// is DONE
		{
			String propertyOnWhichToDemolish = commandInput.replace("demolish ", "");
			String[] part = propertyOnWhichToDemolish.split("(?<=\\D)(?=\\d)");
			propertyOnWhichToDemolish = part[0];
			propertyOnWhichToDemolish = propertyOnWhichToDemolish.trim();
			int noOfUnitsToBeDemolished = Integer.parseInt(part[1].trim());
			boolean propertyFound = false;
			for (String s : players[playerNumber].propertiesBought) {
				if (s.contains(propertyOnWhichToDemolish)) {
					propertyFound = true;
					propertyOnWhichToDemolish = s;
				}
			}
			if (propertyFound) {
				if (boardBoxes[boardBoxes[0].getBoxNumberByName(propertyOnWhichToDemolish)].getNoOfHousesBuilt()
						- noOfUnitsToBeDemolished >= 0) {
					commandOutput = noOfUnitsToBeDemolished + " Units demolished";
				} else {
					commandOutput = noOfUnitsToBeDemolished + " Units cannot be demolished, because there are only "
							+ boardBoxes[boardBoxes[0].getBoxNumberByName(propertyOnWhichToDemolish)]
									.getNoOfHousesBuilt()
							+ " houses on the Property";
				}

			} else {
				commandOutput = "Property not found!";
			}

		} else if (commandInput.equalsIgnoreCase("Balance")) // if input is
		// BALANCE
		{
			commandOutput = "Current balance: " + players[playerNumber].getPlayerBalance() + "\n";
		} else if (commandInput.equalsIgnoreCase("PROPERTY")) // if input is
		// PROPERTY
		{
			commandOutput = "\nList of owned properties:";
			for (String s : players[playerNumber].propertiesBought) {
				commandOutput = commandOutput + "\n" + s;
			}
		}

		else if (commandInput.equalsIgnoreCase("quit") || state == 99) // if
		// input
		// is
		// QUIT
		{
			// game finishes and counts up all possession to determine the
			// winner
			commandOutput = "Game is over";
			int winnerTotalWorth = 0;
			ArrayList<String> winner = new ArrayList<String>(); // holds the
			// winner
			// details

			for (Player s : players) {
				if (winnerTotalWorth < (s.getPlayerBalance() + s.getTotalWorthOfProperties())) {
					winner.clear();
					winner.add(s.getPlayerName());
					winnerTotalWorth = (s.getPlayerBalance() + s.getTotalWorthOfProperties());
				} else if (winnerTotalWorth == (s.getPlayerBalance() + s.getTotalWorthOfProperties())) {
					winner.add(s.getPlayerName());
				}
			}

			String output = new String();

			for (String s : winner) {
				output = output + s + ", ";
			}
			JOptionPane.showMessageDialog(null, "Congratulations: " + output + "\n You are the winner of the Game");
			System.exit(0);
		} else if (state == 3 || state == 4 || state == 5 || state == 6 || state == 7 || state == 9 || state == 8) {
			commandOutput = "Function not valid";
			state = 2;
		}

		else // otherwise input is invalid
		{
			commandOutput = "\t Invalid Command or Not Available at This Time.";
		}

		return commandOutput; // returns the command output each time
	}
}
