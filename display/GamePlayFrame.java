/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2.display;

import Sprint4v2.BoardBox;
import Sprint4v2.player.ChanceCard;
import Sprint4v2.player.CommunityChestCards;
import Sprint4v2.player.Player;
import Sprint4v2.player.Token;

import javax.swing.*;
import java.awt.*;

// class handle Gameplay
public class GamePlayFrame { // class for Game Play Panel

	private JPanel gamePlayPanel;
	private double screenHeight;
	private JTextArea textArea2;
	private int state = 0;
	private int lastSumOfDiceRoll = 0;
	private Player[] playersPlaying;
	private int countOfPlayers;
	private static int noOFDiceRollsForJailedPerson;
	private boolean playerIsJustReleasedFromJail = false;
	int fontSize;
	String fontType;
	private static int noOfDoublesRolled = 0;
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
	// state 99 to quit when only one player left

	// cnstructor initialises values
	public GamePlayFrame(double screenHeight, Player players[], int numberOfPlayers, String fontT, int fontS) { // assign
		// values
		this.screenHeight = screenHeight;
		countOfPlayers = numberOfPlayers;
		playersPlaying = players;
		fontSize = (int) (fontS / 3.0 * 2.0);
		fontType = fontT;
		createGamePlayFrame();
	}

	// creates GamePlay
	private void createGamePlayFrame() { // create the panel with some given
		// text
		gamePlayPanel = new JPanel();
		Box box = Box.createVerticalBox();
		textArea2 = new JTextArea();
		textArea2.setFont(new Font(fontType, Font.PLAIN, fontSize));
		gamePlayPanel.setLayout(new BoxLayout(gamePlayPanel, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("GamePlay");
		label.setFont(new Font(fontType, Font.BOLD, (int) (fontSize * 1.5)));
		textArea2.setEditable(false);
		label.setAlignmentX(0.5f);
		textArea2.append("\n\t Type 'help' for Command List \n\t Type 'roll' to Advance a Player\n");
		gamePlayPanel.add(box);
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(textArea2);
		scroll.setPreferredSize(new Dimension((int) screenHeight, (int) screenHeight));
		gamePlayPanel.add(scroll);
	}

	// updates GamePlay with different cases
	public void updateOnCommand(JTextField textField, int playerTurn, int dice1, int dice2, BoardBox boardBoxes[],
			Token tokens[], CommunityChestCards communityChest, ChanceCard chanceCard) {
		// state 0 to Roll
		// state
		if (textField.getText().equalsIgnoreCase("ROLL") && (state == 0 || state == 12)) // if
		// input
		// is
		// ROLL
		{
			if (state == 12) {
				if (dice1 == dice2) {
					noOFDiceRollsForJailedPerson = 0;
					state = 0;
					playerIsJustReleasedFromJail = true;
					textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
							+ " rolls a DOUBLE, therefore gets out of Jail!\n");
				} else {
					noOFDiceRollsForJailedPerson = noOFDiceRollsForJailedPerson + 1;
					if (noOFDiceRollsForJailedPerson == 3) {
						textArea2.append((playersPlaying[playerTurn - 1].getPlayerName()
								+ " paid $50 fine to get out of jail!\n"));
						playerIsJustReleasedFromJail = true;
						noOFDiceRollsForJailedPerson = 0;
						state = 0;
					}
					else
					{
						state=2;
					}
				}
			}
			if (state == 0) {
				state = 2;
				// displays move and current box info
				textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " moves by: " + (dice1 + dice2)
						+ " to: " + boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxName() + "\n");

				// Checks if property is owned
				if (boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned() != 0) {
					textArea2.append("\t Property Owned By "
							+ playersPlaying[boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned() - 1]
									.getPlayerName()
							+ "\n");
					state = 2;
				}

				// checks for DOUBLES
				if (!playerIsJustReleasedFromJail) {
					if (dice1 == dice2) {
						textArea2.append("\t DOUBLES! Roll Again \n");
						noOfDoublesRolled = noOfDoublesRolled + 1;
						state = 0;
					}
				} else {
					playerIsJustReleasedFromJail = false;
				}
				if (noOfDoublesRolled == 3) {
					textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
							+ " rolls 3 DOUBLES, therefore moves to Jail!\n");
					state = 2;
				} else {
					if (state != 0) {
						lastSumOfDiceRoll = dice1 + dice2;
						if ((boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()].getCardType() == 1)
								|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 8)
								|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 9)) {
							if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned() == 0)) {
								state = 10;
							} else {
								if (boardBoxes[tokens[playerTurn - 1].getBoxNo()].getStatusOfBox() != 2) {

									if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].getCardType() == 8)) {
										int noOfCardsHolding = playersPlaying[boardBoxes[tokens[playerTurn - 1]
												.getBoxNo()].checkIfOwned()
												- 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1]
														.getBoxNo()].getCardColorGroup() - 1];
										if (noOfCardsHolding == 1) {
											boardBoxes[tokens[playerTurn - 1].getBoxNo()]
													.setRentValue(4 * lastSumOfDiceRoll);
										} else {
											boardBoxes[tokens[playerTurn - 1].getBoxNo()]
													.setRentValue(10 * lastSumOfDiceRoll);
										}
									}
									if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].getCardType() == 9)) {
										int noOfCardsHolding = playersPlaying[boardBoxes[tokens[playerTurn - 1]
												.getBoxNo()].checkIfOwned()
												- 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1]
														.getBoxNo()].getCardColorGroup() - 1];
										if (noOfCardsHolding == 1) {
											boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(25);
										} else if (noOfCardsHolding == 2) {
											boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(50);
										} else if (noOfCardsHolding == 3) {
											boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(100);
										} else if (noOfCardsHolding == 4) {
											boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(200);
										}
									}

									textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " paid $"
											+ boardBoxes[tokens[playerTurn - 1].getBoxNo()].getRentValue() + " to "
											+ playersPlaying[boardBoxes[tokens[playerTurn - 1].getBoxNo()]
													.checkIfOwned() - 1].getPlayerName()
											+ "\n");
									playersPlaying[playerTurn - 1].decrementPlayerBalance(
											boardBoxes[tokens[playerTurn - 1].getBoxNo()].getRentValue());
									playersPlaying[boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned() - 1]
											.incrementPlayerBalance(
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].getRentValue());
								} else {
									textArea2.append("Property is Mortgaged. No rent required");
								}
								state = 2;
							}
						} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
								.getCardType() == 2) {
							textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " draws a Chance Card:"
									+ chanceCard.getChanceCardDescription() + "\n\t\t\t" + "Card is Implemented\n");

							if (chanceCard.getChanceCardType() == 2 || chanceCard.getChanceCardType() == 8
									|| chanceCard.getChanceCardType() == 7) {
								lastSumOfDiceRoll = dice1 + dice2;
								if ((boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 1)
										|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
												.getCardType() == 8)
										|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
												.getCardType() == 9)) {
									if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned() == 0)) {
										state = 10;
									} else {
										if (boardBoxes[tokens[playerTurn - 1].getBoxNo()].getStatusOfBox() != 2) {

											if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].getCardType() == 8)) {
												int noOfCardsHolding = playersPlaying[boardBoxes[tokens[playerTurn - 1]
														.getBoxNo()].checkIfOwned()
														- 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1]
																.getBoxNo()].getCardColorGroup() - 1];
												if (noOfCardsHolding == 1) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()]
															.setRentValue(4 * lastSumOfDiceRoll);
												} else {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()]
															.setRentValue(10 * lastSumOfDiceRoll);
												}
											}
											if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].getCardType() == 9)) {
												int noOfCardsHolding = playersPlaying[boardBoxes[tokens[playerTurn - 1]
														.getBoxNo()].checkIfOwned()
														- 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1]
																.getBoxNo()].getCardColorGroup() - 1];
												if (noOfCardsHolding == 1) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(25);
												} else if (noOfCardsHolding == 2) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(50);
												} else if (noOfCardsHolding == 3) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(100);
												} else if (noOfCardsHolding == 4) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(200);
												}
											}

											textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " paid $"
													+ boardBoxes[tokens[playerTurn - 1].getBoxNo()].getRentValue()
													+ " to "
													+ playersPlaying[boardBoxes[tokens[playerTurn - 1].getBoxNo()]
															.checkIfOwned() - 1].getPlayerName()
													+ "\n");
											playersPlaying[playerTurn - 1].decrementPlayerBalance(
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].getRentValue());
											playersPlaying[boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned()
													- 1].incrementPlayerBalance(
															boardBoxes[tokens[playerTurn - 1].getBoxNo()]
																	.getRentValue());
										} else {
											textArea2.append("Property is Mortgaged. No rent required");
										}
										state = 2;
									}
								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 3) {
									textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
											+ " paid $200 Income Tax." + "\n");
									playersPlaying[playerTurn - 1].decrementPlayerBalance(200);

								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 5) {
									textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
											+ " lands on the Free Parking!\n");
									state = 2;

								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 6) {
									if (playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard() > 10) {
										textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
												+ " lands on GOTO JAIl and is sent to jail!\n");
									}

									if (playersPlaying[playerTurn - 1].getTakenToJailStatus() == false) {
										state = 2;
									} else {
										state = 2;
									}

								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 7) {
									textArea2
											.append(playersPlaying[playerTurn - 1].getPlayerName() + " lands on GO!\n");
									state = 2;

								}

							} else {
								state = 2;
							}

						} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
								.getCardType() == 3) {
							textArea2.append(
									playersPlaying[playerTurn - 1].getPlayerName() + " paid $200 Income Tax." + "\n");
							playersPlaying[playerTurn - 1].decrementPlayerBalance(200);

						} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
								.getCardType() == 4) {
							textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
									+ " draws a Community chest:" + communityChest.getCommunityCardDescription()
									+ "\n\t\t\t" + "Card is Implemented\n");
							if (communityChest.getCommunityCardType() == 2
									|| communityChest.getCommunityCardType() == 7) {
								lastSumOfDiceRoll = dice1 + dice2;
								if ((boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 1)
										|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
												.getCardType() == 8)
										|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
												.getCardType() == 9)) {
									if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned() == 0)) {
										state = 10;
									} else {
										if (boardBoxes[tokens[playerTurn - 1].getBoxNo()].getStatusOfBox() != 2) {

											if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].getCardType() == 8)) {
												int noOfCardsHolding = playersPlaying[boardBoxes[tokens[playerTurn - 1]
														.getBoxNo()].checkIfOwned()
														- 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1]
																.getBoxNo()].getCardColorGroup() - 1];
												if (noOfCardsHolding == 1) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()]
															.setRentValue(4 * lastSumOfDiceRoll);
												} else {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()]
															.setRentValue(10 * lastSumOfDiceRoll);
												}
											}
											if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].getCardType() == 9)) {
												int noOfCardsHolding = playersPlaying[boardBoxes[tokens[playerTurn - 1]
														.getBoxNo()].checkIfOwned()
														- 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1]
																.getBoxNo()].getCardColorGroup() - 1];
												if (noOfCardsHolding == 1) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(25);
												} else if (noOfCardsHolding == 2) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(50);
												} else if (noOfCardsHolding == 3) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(100);
												} else if (noOfCardsHolding == 4) {
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].setRentValue(200);
												}
											}

											textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " paid $"
													+ boardBoxes[tokens[playerTurn - 1].getBoxNo()].getRentValue()
													+ " to "
													+ playersPlaying[boardBoxes[tokens[playerTurn - 1].getBoxNo()]
															.checkIfOwned() - 1].getPlayerName()
													+ "\n");
											playersPlaying[playerTurn - 1].decrementPlayerBalance(
													boardBoxes[tokens[playerTurn - 1].getBoxNo()].getRentValue());
											playersPlaying[boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned()
													- 1].incrementPlayerBalance(
															boardBoxes[tokens[playerTurn - 1].getBoxNo()]
																	.getRentValue());
										} else {
											textArea2.append("Property is Mortgaged. No rent required");
										}
										state = 2;
									}
								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 3) {
									textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
											+ " paid $200 Income Tax." + "\n");
									playersPlaying[playerTurn - 1].decrementPlayerBalance(200);

								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 5) {
									textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
											+ " lands on the Free Parking!\n");
									state = 2;

								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 6) {
									if (playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard() > 10) {
										textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
												+ " lands on GOTO JAIl and is sent to jail!\n");
									}

									if (playersPlaying[playerTurn - 1].getTakenToJailStatus() == false) {
										state = 2;
									} else {
										state = 2;
									}

								} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 7) {
									textArea2
											.append(playersPlaying[playerTurn - 1].getPlayerName() + " lands on GO!\n");
									state = 2;

								}

							} else {
								state = 2;
							}

						} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
								.getCardType() == 5) {
							textArea2.append(
									playersPlaying[playerTurn - 1].getPlayerName() + " lands on the Free Parking!\n");
							state = 2;

						} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
								.getCardType() == 6) {
							if (playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard() > 10) {
								textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
										+ " lands on GOTO JAIl and is sent to jail!\n");
							}

							if (playersPlaying[playerTurn - 1].getTakenToJailStatus() == false) {
								state = 2;
							} else {
								state = 2;
							}

						} else if (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
								.getCardType() == 7) {
							textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " lands on GO!\n");
							state = 2;

						}
					}
				}
			}
		} else if (textField.getText().equalsIgnoreCase("BUY") && state == 10) // if
		// input
		// is
		// BUY

		{
			if (playersPlaying[playerTurn - 1].getPlayerBalance()
					- boardBoxes[tokens[playerTurn - 1].getBoxNo()].getValue() >= 0) {
				if ((boardBoxes[tokens[playerTurn - 1].getBoxNo()].checkIfOwned() == 0)
						&& ((boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()].getCardType() == 1)
								|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 8)
								|| (boardBoxes[playersPlaying[playerTurn - 1].getPlayerPosititonOnBoard()]
										.getCardType() == 9))) {
					// player must pay
					playersPlaying[playerTurn - 1]
							.decrementPlayerBalance(boardBoxes[tokens[playerTurn - 1].getBoxNo()].getValue());
					playersPlaying[playerTurn
							- 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1].getBoxNo()]
									.getCardColorGroup() - 1]++;
					playersPlaying[playerTurn - 1].buyProperty(
							boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxName(),
							boardBoxes[tokens[playerTurn - 1].getBoxNo()].getValue());
					boardBoxes[tokens[playerTurn - 1].getBoxNo()].changeOwnership(playerTurn);
					textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " bought: "
							+ boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxName() + " for $"
							+ boardBoxes[tokens[playerTurn - 1].getBoxNo()].getValue() + "\n");
					boardBoxes[tokens[playerTurn - 1].getBoxNo()].setStatusOfBox(1);
					state = 2;
				} else {
					state = 1;
				}
			} else {
				textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + "cannot buy : "
						+ boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxName() + " for $"
						+ boardBoxes[tokens[playerTurn - 1].getBoxNo()].getValue() + " due to insufficent funds.\n");
			}

		} else if (textField.getText().contains("mortgage")) // if input is BUY
		{
			System.out.println("Entered");
			String propertyToBeMortgaged = textField.getText().replace("mortgage ", "");
			for (String s : playersPlaying[playerTurn - 1].propertiesBought) {
				if (s.contains(propertyToBeMortgaged)) {
					boardBoxes[boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxNumberByName(s)].setStatusOfBox(2);
					int propertyValue = boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxValueByName(s);
					System.out.println(propertyValue);
					playersPlaying[playerTurn - 1].incrementPlayerBalance(propertyValue / 2);
					textArea2.append(
							playersPlaying[playerTurn - 1].getPlayerName() + " mortgaged the Property " + s + "\n");
				}

			}

		} else if (textField.getText().contains("build")) // if input is BUY
		{
			String propertyOnWhichToBuilt = textField.getText().replace("build ", "");
			String[] part = propertyOnWhichToBuilt.split("(?<=\\D)(?=\\d)");
			propertyOnWhichToBuilt = part[0].trim();
			int boxNo = -1;
			int noOfUnitsToBeBuild = Integer.parseInt(part[1].trim());
			boolean propertyFound = false;
			for (String s : playersPlaying[playerTurn - 1].propertiesBought) {

				if (s.contains(propertyOnWhichToBuilt)) {
					if (boardBoxes[boardBoxes[0].getBoxNumberByName(s)].getNoOfHousesBuilt()
							+ noOfUnitsToBeBuild <= 5) {
						boxNo = boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxNumberByName(s);
						propertyOnWhichToBuilt = s;
						propertyFound = true;
					}
				}

			}
			if (playersPlaying[playerTurn - 1].colorGroupsOwnedByPlayer[boardBoxes[tokens[playerTurn - 1].getBoxNo()]
					.getCardColorGroup()
					- 1] == boardBoxes[0].maximumNoOfCardsInEachColorGroup[boardBoxes[tokens[playerTurn - 1].getBoxNo()]
							.getCardColorGroup() - 1]) {
				if (boxNo > 0) {
					if ((noOfUnitsToBeBuild + boardBoxes[boxNo].getNoOfHousesBuilt()) <= 5) {
						boardBoxes[boxNo].buildNHouses(noOfUnitsToBeBuild);
						playersPlaying[playerTurn - 1].decrementPlayerBalance(
								noOfUnitsToBeBuild * boardBoxes[boxNo].getPriceToBuildOneHouse(boxNo));
						if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 1) {
							boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithOneHouse(boxNo));
						} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 2) {
							boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithTwoHouse(boxNo));
						} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 3) {
							boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithThreeHouse(boxNo));
						} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 4) {
							boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithFourHouse(boxNo));
						} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 5) {
							boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithHotel(boxNo));
						}

					}
					textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " build " + noOfUnitsToBeBuild
							+ " houses in the Property " + propertyOnWhichToBuilt + "\n");
				}
			}

		} else if (textField.getText().contains("demolish")) // if input is BUY
		{
			String propertyOnWhichToDemolish = textField.getText().replace("demolish ", "");
			String[] part = propertyOnWhichToDemolish.split("(?<=\\D)(?=\\d)");
			propertyOnWhichToDemolish = part[0].trim();
			int boxNo = -1;
			int noOfUnitsToBeDemolished = Integer.parseInt(part[1].trim());
			boolean propertyFound = false;
			for (String s : playersPlaying[playerTurn - 1].propertiesBought) {
				if (s.contains(propertyOnWhichToDemolish)) {
					if (boardBoxes[boardBoxes[0].getBoxNumberByName(s)].getNoOfHousesBuilt()
							- noOfUnitsToBeDemolished >= 0) {
						boxNo = boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxNumberByName(s);
						propertyOnWhichToDemolish = s;
						propertyFound = true;
					}
				}
			}
			if (boxNo > 0) {
				if ((boardBoxes[boxNo].getNoOfHousesBuilt() - noOfUnitsToBeDemolished) <= 5
						&& (boardBoxes[boxNo].getNoOfHousesBuilt() - noOfUnitsToBeDemolished) >= 0) {
					boardBoxes[boxNo].demolishNHouse(noOfUnitsToBeDemolished);
					playersPlaying[playerTurn - 1].incrementPlayerBalance(
							noOfUnitsToBeDemolished * boardBoxes[boxNo].getPriceToBuildOneHouse(boxNo) / 2);
					if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 1) {
						boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithOneHouse(boxNo));
					} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 0) {
						boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithNoHouse(boxNo));

					} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 2) {
						boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithTwoHouse(boxNo));
					} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 3) {
						boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithThreeHouse(boxNo));
					} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 4) {
						boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithFourHouse(boxNo));
					} else if ((boardBoxes[boxNo].getNoOfHousesBuilt()) == 5) {
						boardBoxes[boxNo].setRentValue(boardBoxes[boxNo].getRentWithHotel(boxNo));
					}

				}
				textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " demolished "
						+ noOfUnitsToBeDemolished + " houses in the Property " + propertyOnWhichToDemolish + "\n");
			}

		} else if (textField.getText().equalsIgnoreCase("Bankrupt") && (state == 11 || state == 2)) {
			countOfPlayers--;
			playersPlaying[playerTurn - 1].bankruptPlayer();
			for (String s : playersPlaying[playerTurn - 1].propertiesBought) {
				boardBoxes[(boardBoxes[0].getBoxNumberByName(s))]
						.bankruptDeclared((boardBoxes[0].getBoxNumberByName(s)));
				;
			}
			playersPlaying[playerTurn - 1].propertiesBought.clear();
			state = 2;
			textArea2.append(playersPlaying[playerTurn - 1].getPlayerName()
					+ " declared Bankrupt. All properties returned to the Bank.\n");
		} else if (textField.getText().contains("redeem")) // if input is BUY
		{
			String propertyToBeRedeemed = textField.getText().replace("redeem ", "");
			boolean canBeRedeemed = false;
			for (String s : playersPlaying[playerTurn - 1].propertiesBought) {
				if (s.contains(propertyToBeRedeemed)
						&& boardBoxes[(boardBoxes[0].getBoxNumberByName(s))].getStatusOfBox() == 2) {
					canBeRedeemed = true;
					propertyToBeRedeemed = s;
				}
			}
			if (canBeRedeemed) {
				boardBoxes[boardBoxes[tokens[playerTurn - 1].getBoxNo()].getBoxNumberByName(propertyToBeRedeemed)]
						.setStatusOfBox(1);
				int propertyValue = boardBoxes[tokens[playerTurn - 1].getBoxNo()]
						.getBoxValueByName(propertyToBeRedeemed);
				playersPlaying[playerTurn - 1].decrementPlayerBalance(propertyValue / 2);
				textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " redeemed the Property "
						+ propertyToBeRedeemed + "\n");
			}

		} else if (textField.getText().equalsIgnoreCase("done") && (state == 2 || state == 10)
				&& (playersPlaying[playerTurn - 1].getPlayerBalance() >= 0)) // if
		// input
		// is
		// DONE
		{
			if (countOfPlayers == 1) {
				textArea2.append("\t*** GAME OVER*** \n \t    please quit");
			} else {
				state = 0;
				// balance updates
				System.out.println(playersPlaying[playerTurn - 1].getPlayerName() + " :balance"
						+ playersPlaying[playerTurn - 1].getPlayerBalance());
			}
			noOfDoublesRolled=0;
			if (playersPlaying[playerTurn - 1].getTakenToJailStatus() == true) {
				state = 12;
			}
		} else if (textField.getText().equalsIgnoreCase("Card") && state == 12) {
			if (playersPlaying[playerTurn - 1].getNoOfGetOutOfJailCards() > 0) {
				textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " used the GET OUT OF JAIL CARD!\n");
				playersPlaying[playerTurn - 1].useGetOutOFJailCard();
				playersPlaying[playerTurn - 1].setTakenToJailStatus(false);
				state = 0;
				noOFDiceRollsForJailedPerson = 0;
			} else {
				state = 12;
			}

		} else if (textField.getText().equalsIgnoreCase("PAY") && state == 12) {
			textArea2.append(playersPlaying[playerTurn - 1].getPlayerName() + " paid $50 fine to get out of jail!\n");
			noOFDiceRollsForJailedPerson = 0;
			state = 0;
		} else if (state == 3 || state == 4 || state == 5 || state == 6 || state == 7 || state == 9 || state == 8) {
			textArea2.append("Function not valid\n");
			state = 2;
		}

	}

	public JPanel getPanel() {
		return gamePlayPanel;
	}
}