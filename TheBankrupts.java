
/**
 TheBankrupts
 Final version for Sprint 5
 Patryk Labuzek - 15440728
 Michal Gwizdz  - 15522923
 Raman Prasad   - 15203657
 */
import java.util.ArrayList;

public class TheBankrupts implements Bot {

	BoardAPI board;
	PlayerAPI player;
	DiceAPI dice;
	int stateOfBot = 0;
	// 0 - player has to roll
	// 1 - rolled
	// 2 - rolled to a free property / buy for now
	// 3 - rolled to an owned property has to pay
	// 4 - roll in jail need doubles
	// 5 - jail first turn
	// 6 - negative balance

	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	ArrayList<String> redeemProperty = new ArrayList<>(); // for storing
															// mortgage
															// properties in
															// order.

	TheBankrupts(BoardAPI board, PlayerAPI player, DiceAPI dice) {
		this.board = board;
		this.player = player;
		this.dice = dice;
	}

	public String getName() {
		return "TheBankrupts";
	}

	public String getCommand() {
		// Add your code here
		if (stateOfBot == 0 && !player.isInJail()) {
			stateOfBot = 1;
			return "roll";
		}
		if ((stateOfBot == 1 || stateOfBot == 4) && board.isProperty(player.getPosition())) {
			// check if worth to buy
			if (board.getProperty(player.getPosition()).isOwned() && stateOfBot == 1 || stateOfBot == 2) {
				stateOfBot = 3;
				// paid no need for command

			} else if (board.getProperty(player.getPosition()).isOwned() && player.getBalance() < 0
					&& player.getAssets() < 0) {
				return "bankrupt";
			} else if (!board.getProperty(player.getPosition()).isOwned() && player.getBalance() >= 250
					&& player.getBalance() - board.getProperty(player.getPosition()).getPrice() >= 150) {
				stateOfBot = 2;
				return "buy";
			}
		}
		// when balance is below 0
		if (player.getBalance() < 0 && (stateOfBot == 1 || stateOfBot == 3 || stateOfBot == 6)) {
			ArrayList<Property> propertyList = player.getProperties(); // go
																		// through
																		// player
																		// properties
			if (!player.getProperties().isEmpty()) { // select worts property to
														// mortgage
				Property toSell = propertyList.get(0);
				for (Property p : propertyList) {
					if (!p.isMortgaged() && board.isSite(p.getShortName())) {
						if (board.isSite(toSell.getShortName()) && player.isGroupOwner((Site) toSell)
								&& !player.isGroupOwner((Site) p)) {
							toSell = p;
							break;
						}
					}
					if (!p.isMortgaged() && !board.isSite(p.getShortName())) {
						if (board.isSite(toSell.getShortName()) && player.isGroupOwner((Site) toSell)) {
							toSell = p;
						}
					}
				}
				if (player.getBalance() + toSell.getMortgageRemptionPrice() < 0) {
					stateOfBot = 6;
				} else {
					stateOfBot = 0;
				}
				if (!toSell.isMortgaged() && (board.isSite(toSell.getShortName()) && !((Site) toSell).hasBuildings())) {
					redeemProperty.add(toSell.getShortName());
					return "mortgage " + toSell.getShortName();
				}

				if (player.getNumHousesOwned() > 0) { // demolish houses if we
														// have any on a
														// negative balance
					// sell houses from colors
					for (Property p : propertyList) {
						if (board.isSite(p.getShortName()) && ((Site) p).hasBuildings()) {
							if (board.isSite(toSell.getShortName()) && !((Site) toSell).hasBuildings()) {
								toSell = p;
							}
							if (board.isSite(toSell.getShortName())
									&& ((Site) p).getNumBuildings() < ((Site) toSell).getNumBuildings()
									|| p.getRent() < toSell.getRent()) {
								toSell = p;
								// again check if that house is enough
							}
						}
					}
					if (board.isSite(toSell.getShortName()) && ((Site) toSell).hasBuildings()) {
						return "demolish " + toSell.getShortName() + " 1";
					}
				}

				if (player.getBalance() < 0) {
					// morage color groups if still negative balance
					int i = 0;
					while (i < player.getProperties().size() - 1 && !player.getProperties().get(i).isMortgaged()) {
						i++;
					}
					if (!player.getProperties().get(i).isMortgaged()
							&& (board.isSite(player.getProperties().get(i).getShortName())
									&& !((Site) player.getProperties().get(i)).hasBuildings())) {
						redeemProperty.add(0, player.getProperties().get(i).getShortName());
						return "mortgage " + player.getProperties().get(i).getShortName();
					}
				}
			}
			return "bankrupt"; // if still on the negative, player is bankrupt.
		}

		int minValue = 100;
		if (player.getBalance() > minValue && player.getNumProperties() >= 2) { // building
																				// houses
			ArrayList<Property> propertyList = player.getProperties();
			int numberToBuild;
			Property lowestHouses = propertyList.get(0); // search for a colour
															// group
			for (Property p : propertyList) {
				if ((!board.isSite(lowestHouses.getShortName()) || !player.isGroupOwner((Site) lowestHouses))
						&& board.isSite(p.getShortName()) && player.isGroupOwner((Site) p)) {
					lowestHouses = p;
				}
				if (board.isSite(lowestHouses.getShortName()) && player.isGroupOwner((Site) lowestHouses)) {
					if (!((Site) lowestHouses).hasBuildings()) {
						break;
					}
					if (board.isSite(p.getShortName()) && player.isGroupOwner((Site) p)
							&& ((Site) lowestHouses).getNumBuildings() > ((Site) p).getNumBuildings()) {
						lowestHouses = p;
					}
				}
			}
			if (board.isSite(lowestHouses.getShortName()) && player.isGroupOwner((Site) lowestHouses)) { // building
																											// houses
				numberToBuild = (int) Math
						.floor((player.getBalance() - 50) / (((Site) lowestHouses).getBuildingPrice() * 3.0));
				if (numberToBuild == 0) { // get number of houses to be built
					if (((Site) lowestHouses).getBuildingPrice() < player.getBalance() - 100) {
						numberToBuild = 1;
					}
				}
				while (!((Site) lowestHouses).canBuild(numberToBuild) && numberToBuild > 0) {
					numberToBuild--;
				}
				if (numberToBuild > 3) {
					numberToBuild = 3;
				}
				if (numberToBuild > 0 && !lowestHouses.isMortgaged() && player.isGroupOwner((Site) lowestHouses)) {
					return "build " + lowestHouses.getShortName() + " " + numberToBuild;
				}
			}
		}

		if (player.getBalance() > 150 && !redeemProperty.isEmpty()) { // if we
																		// have
																		// money
																		// redeem
																		// properties
			// property is mortgaged need to redeem
			if (board.getProperty(redeemProperty.get(0)).getMortgageRemptionPrice() < player.getBalance() - 50) {
				String toBeRedeemed = redeemProperty.remove(0);
				return "redeem " + toBeRedeemed;
			}
		}

		if (player.isInJail()) { // if in jail
			if (28 - totalPropertiesOwned() >= 8) {
				if (player.hasGetOutOfJailCard()) { // use card if we have
					return "card";
				} else {
					return "pay";
				}
			} else if (stateOfBot != 5) { // proceed
				stateOfBot = 5;
				return "done";
			}
			stateOfBot = 4;
			return "roll";
		}
		if (dice.isDouble() && (stateOfBot == 1 || stateOfBot == 3 || stateOfBot == 2 || stateOfBot == 4)) {
			if (stateOfBot == 4 || player.isInJail()) {
				if (board.isProperty(player.getPosition())) {
					if (!board.getProperty(player.getPosition()).isOwned() && player.getBalance() >= 250
							&& player.getBalance() - board.getProperty(player.getPosition()).getPrice() >= 150) {
						stateOfBot = 2;
						return "buy";
					}
				} else {
					stateOfBot = 0;
					return "done";
				}
			} else {
				if (board.isProperty(player.getPosition())) {
					if (!board.getProperty(player.getPosition()).isOwned() && player.getBalance() >= 250
							&& player.getBalance() - board.getProperty(player.getPosition()).getPrice() >= 150) {
						stateOfBot = 4;
						return "buy";
					}
					else {
						stateOfBot = 4;
						return "roll";
					}
				}
				stateOfBot = 4;
				return "roll";
			}

		}
		stateOfBot = 0;
		return "done";

	}

	private int totalPropertiesOwned() {
		int totalPropertiesOwned = 0;
		for (int i = 0; i < 40; i++) {
			if (board.isProperty(i)) {
				if (board.getProperty(i).isOwned()) {
					totalPropertiesOwned = totalPropertiesOwned + 1;
				}
			}
		}
		return totalPropertiesOwned;
	}

	public String getDecision() {
		// Add your code here
		// take a chance if we have below 2000 and less then 7 houses 4 hotels,
		// one card is to pay for them.
		if (player.getBalance() < 500 && player.getNumHousesOwned() < 4 && player.getNumHotelsOwned() < 1) {
			return "chance";
		}
		return "pay";
	}

}
