/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2.player;

import java.util.ArrayList;

// class handles the Players
public class Player {
	private String name; // name of player
	private static int turnNo = 1; // players turn
	private int sequenceNo;
	private int moneyInHand; // balance
	private int positionOnTheBoard; // position on the board
	private int noOfGetOutOfJailCard;
	private boolean takenToJail;
	public ArrayList<String> propertiesBought = new ArrayList<String>(); // list
	// of
	// owned
	// properties
	private int totalWorthOfPropertiesBought; // total worth of properties
	public int[] colorGroupsOwnedByPlayer = new int[10];

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public boolean activeStatus;

	// constructor to initialise the values
	public Player(String name) {
		this.name = name;
		this.sequenceNo = turnNo;
		this.moneyInHand = 1500;
		for (int i = 0; i < 10; i++) {
			colorGroupsOwnedByPlayer[i] = 0;
		}
		this.positionOnTheBoard = 0;
		this.activeStatus=true;
		// System.out.println("Name:" + this.name + " SequenceNo:" +
		// this.sequenceNo);
		this.noOfGetOutOfJailCard=0;
		this.takenToJail=false;
		turnNo++;

	}
	public int getNoOfGetOutOfJailCards()
	{
		return noOfGetOutOfJailCard;
	}
	public boolean getTakenToJailStatus()
	{
		return this.takenToJail;
	}
	public void setTakenToJailStatus(boolean status)
	{
		this.takenToJail=status;
	}
	public void setPlayerActiveStatus(boolean status)
	{
		activeStatus=status;
	}
	public boolean getPlayerActiveStatus()
	{
		return activeStatus;
	}
	public int getPlayerSequenceNo() {
		return this.sequenceNo;
	} // returns Sequence number

	public String getPlayerName() {
		return this.name;
	} // returns players name

	public int getPlayerBalance() {
		return moneyInHand;
	} // returns balance

	public void incrementPlayerBalance(int amount) {
		moneyInHand = moneyInHand + amount;
	} // adds to balance

	public void decrementPlayerBalance(int amount) {
		moneyInHand = moneyInHand - amount;
	} // takes from balance
	public void bankruptPlayer()
	{
		activeStatus=false;
		this.moneyInHand = 0;
		for (int i = 0; i < 10; i++) {
			colorGroupsOwnedByPlayer[i] = 0;
		}
		this.positionOnTheBoard = 0;
	}
	public void buyProperty(String property, int amount) // when buy property
	{
		propertiesBought.add(property);
		totalWorthOfPropertiesBought += amount;
	}

	public void sellProperty(String property) // when sells property
	{
		for (String s : propertiesBought) {
			if (s.equalsIgnoreCase(property)) {
				propertiesBought.remove(property);
			}
		}
	}

	public void setPlayerPositionOnBoard(int position) {
		positionOnTheBoard = position;
	} // sets the position of player

	public int getPlayerPosititonOnBoard() {
		return positionOnTheBoard;
	} // returns players position

	public int getTotalWorthOfProperties() {
		return totalWorthOfPropertiesBought;
	} // calculates the worth of properties
	public boolean checkIfPossessGetOutOFJailCard()
	{
		return (noOfGetOutOfJailCard>0);
	}
	public void useGetOutOFJailCard()
	{
		noOfGetOutOfJailCard=noOfGetOutOfJailCard-1;
	}
	public void addGetOutOFJailCard()
	{
		noOfGetOutOfJailCard=noOfGetOutOfJailCard+1;
	}
}