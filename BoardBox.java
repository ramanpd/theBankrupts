/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2;

//class is implemented so that each box on board has unique details

public class BoardBox {
	private int boxNo;
	// Conatins the names of each box
	private final static String[] boxNames = { "Go", "Honolulu", "Chest", "Hilo", "Income Tax", "Massachusetts",
			"Boston", "Chance", "Worcester", "Cambridge", "Jail", "Reno", "Electric Company", "Henderson", "Las Vegas",
			"Nevada", "Columbia", "Chest", "St.Louis", "Kansas City", "Free Parking", "Rockford", "Chance", "Aurora",
			"Chicago", "Florida", "Orlando", "Miami", "Water Works", "Jacksonville", "Go To Jail", "San Diego",
			"Los Angeles", "Chest", "San Francisco", "California", "Chance", "Buffalo", "Income Tax", "New York" };
	// Conatins the nprice of each box
	private final static int[] values = { 0, 60, 0, 60, 200, 200, 100, 0, 100, 120, 0, 140, 150, 140, 160, 200, 180, 0,
			180, 200, 0, 220, 0, 220, 240, 200, 260, 260, 150, 280, 0, 300, 300, 0, 320, 200, 0, 350, 200, 400 };

	// Card Types
	// 1 for property
	// 2 for chance card
	// 3 tax card
	// 4 for community chest
	// 5 free parking
	// 6 jail
	// 7 go
	// 8 companies
	// 9 railway

	private final static int[] cardTypeValues = { 7, 1, 4, 1, 3, 9, 1, 2, 1, 1, 6, 1, 8, 1, 1, 9, 1, 4, 1, 1, 5, 1, 2,
			1, 1, 9, 1, 1, 8, 1, 6, 1, 1, 4, 1, 9, 2, 1, 3, 1 };

	// Color Groups
	// 1 for purple
	// 2 for light blue
	// 3 for pink
	// 4 for orange
	// 5 for red
	// 6 for yellow
	// 7 for green
	// 8 for dark blue
	// 9 for railway
	// 10 for companies

	private final static int[] colorGroupValues = { 0, 1, 0, 1, 0, 9, 2, 0, 2, 2, 0, 3, 10, 3, 3, 9, 4, 0, 4, 4, 0, 5,
			0, 5, 5, 9, 6, 6, 10, 6, 0, 7, 7, 0, 7, 9, 0, 8, 0, 8 };
	public final static int[] maximumNoOfCardsInEachColorGroup = { 2, 3, 3, 3, 3, 3, 3, 2, 4, 2 };
	public final static String[] colorNames={"Purple","light Blue","Pink","Orange","Red","Yellow","Green","Dark Blue","Railways","Companies"};
	private final static int noOfPurpleCards = 2;
	private final static int noOfLightBlueCards = 3;
	private final static int noOfPinkCards = 3;
	private final static int noOfOrangeCards = 3;
	private final static int noOfRedCards = 3;
	private final static int noOfYellowCards = 3;
	private final static int noOfGreenCards = 3;
	private final static int noOfDarkBlueCards = 2;
	private final static int noOfRailwayCards = 4;
	private final static int noOfCompanyCards = 2;

	// rent values of the properties
	private final static int[] rentValues = { 0, 2, 0, 4, 0, 0, 6, 0, 6, 8, 0, 10, 0, 10, 12, 0, 14, 0, 14, 16, 0, 18,
			0, 18, 20, 0, 22, 22, 0, 24, 0, 26, 26, 0, 28, 0, 0, 35, 0, 50 };
	private final static int[] rentWithOneHouse = { 0, 10, 0, 20, 0, 0, 30, 0, 30, 40, 0, 50, 0, 50, 60, 0, 70, 0, 70,
			80, 0, 90, 0, 90, 100, 0, 110, 110, 0, 120, 0, 130, 130, 0, 150, 0, 0, 175, 0, 200 };
	private final static int[] rentWithTwoHouse = { 0, 30, 0, 60, 0, 0, 90, 0, 90, 100, 0, 150, 0, 150, 180, 0, 200, 0,
			200, 220, 0, 250, 0, 250, 300, 0, 330, 330, 0, 360, 0, 390, 390, 0, 450, 0, 0, 500, 0, 600 };
	private final static int[] rentWithThreeHouse = { 0, 90, 0, 180, 0, 0, 270, 0, 270, 300, 0, 450, 0, 450, 500, 0,
			550, 0, 550, 600, 0, 700, 0, 700, 750, 0, 800, 800, 0, 850, 0, 900, 900, 0, 1000, 0, 0, 1100, 0, 1400 };
	private final static int[] rentWithFourHouse = { 0, 160, 0, 320, 0, 0, 400, 0, 400, 450, 0, 625, 0, 625, 700, 0,
			700, 0, 700, 800, 0, 875, 0, 875, 925, 0, 975, 975, 0, 1025, 0, 1100, 1100, 0, 1200, 0, 0, 1300, 0, 1700 };
	private final static int[] rentWithHotel = { 0, 250, 0, 450, 0, 0, 550, 0, 550, 600, 0, 750, 0, 750, 900, 0, 900, 0,
			950, 1000, 0, 1050, 0, 1050, 1100, 0, 1150, 1150, 0, 1200, 0, 1275, 1275, 0, 1400, 0, 0, 1500, 0, 2000 };
	private final static int[] priceToBuildOneHouse = { 0, 50, 0, 50, 0, 0, 50, 0, 50, 50, 0, 100, 0, 100, 100, 0, 100,
			0, 100, 100, 0, 150, 0, 150, 150, 0, 150, 150, 0, 150, 0, 200, 200, 0, 200, 0, 0, 200, 0, 200 };
	private final static int oneRailwayRent = 25;
	private final static int twoRailwayRent = 50;
	private final static int threeRailwayRent = 100;
	private final static int fourRailwayRent = 200;
	private String boxName;
	private int status; // 0 for not owned 1 for bought 2 for mortgaged
	private int rent;
	private int value;
	private int ownedBy; // is the box free or taken
	private int players;
	private double xCoordinateCenter;
	private double yCoordinateCenter;
	private int cardType;
	private int colorGroup;
	private int noOfHousesBuilt;
	private int isHotelBuilt;

	public int getCardType() {
		return cardType;
	};

	public int getCardColorGroup() {
		return colorGroup;
	}

	// getting the center coordinates of the box
	public BoardBox(double x, double y, int boxNo) {
		xCoordinateCenter = x;
		yCoordinateCenter = y;
		players = 0;
		this.boxNo = boxNo;
		boxName = boxNames[boxNo];
		value = values[boxNo];
		rent = rentValues[boxNo];
		status = 0;
		cardType = cardTypeValues[boxNo];
		ownedBy = 0;
		colorGroup = colorGroupValues[boxNo];
		noOfHousesBuilt = 0;
		isHotelBuilt = 0;
	}

	public int getNoOfHousesBuilt() {
		return noOfHousesBuilt;
	}
	public void bankruptDeclared(int boxNo)
	{
		rent = rentValues[boxNo];
		ownedBy = 0;
		noOfHousesBuilt = 0;
		isHotelBuilt = 0;
		players = 0;
	}

	public void demolishNHouse(int n) {
		noOfHousesBuilt -= n;
	}

	public void buildNHouses(int n) {
		noOfHousesBuilt += n;
	}

	public BoardBox() {
	}

	public int getStatusOfBox() {
		return status;
	}

	public void setStatusOfBox(int i) {
		status = i;
	}

	public String getBoxName() {
		return boxName;
	} // returns the name of the box

	public int getBoxValueByName(String s) {
		int i = 0;
		for (String v : boxNames) {
			if (v.contains(s)) {
				return values[i];
			}
			i++;
		}
		return 0;
	}

	public int getBoxNumberByName(String s) {
		int i = 0;
		for (String v : boxNames) {
			if (v.contains(s)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	public int getValue() {
		return value;
	} // returns the purchase value

	// returns coordinates
	public double getYcoordinate() {
		return this.yCoordinateCenter;
	}

	public double getXcoordinate() {
		return this.xCoordinateCenter;
	}

	public int getPlayers() {
		return players;
	} // returns players

	public void setPlayers(int n) {
		players = n;
	}

	public void addPlayer() {
		players++;
	} // adds players

	public void removePlayer() {
		players--;
	} // takes players away

	public int getBoxNo() {
		return boxNo;
	}; // returns the number of the box

	public void changeOwnership(int playerNumber) {
		ownedBy = playerNumber;
	} // changes ownership

	public int checkIfOwned() {
		return ownedBy;
	} // checks who owns the property

	public int getRentValue() {
		return rent;
	} // returns rent value

	public void setRentValue(int n) {
		rent = n;
	}

	public int getPriceToBuildOneHouse(int BoxNo) {
		return priceToBuildOneHouse[boxNo];
	}

	public int getRentWithNoHouse(int BoxNo) {
		return rentValues[boxNo];
	}

	public int getRentWithOneHouse(int BoxNo) {
		return rentWithOneHouse[boxNo];
	}

	public int getRentWithTwoHouse(int BoxNo) {
		return rentWithTwoHouse[boxNo];
	}

	public int getRentWithThreeHouse(int BoxNo) {
		return rentWithThreeHouse[boxNo];
	}

	public int getRentWithFourHouse(int BoxNo) {
		return rentWithFourHouse[boxNo];
	}

	public int getRentWithHotel(int BoxNo) {
		return rentWithHotel[boxNo];
	}
}