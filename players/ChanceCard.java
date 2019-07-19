package Sprint4v2.player;

import Sprint4v2.BoardBox;
import Sprint4v2.player.Player;
import Sprint4v2.player.Token;

public class ChanceCard {
	//TYPES OF CHANCE CHEST 
		//1 PAY RENT
		//2 GOTO A PLACE
		//3 COLLECT MONEY
		//4 COLLECT MONEY FROM OTHER PLAYERS
		//5 GET OUT OF JAIL CARD
		//6 PAY RENT OR TAKE A CHANCE
		//7 GOTO JAIL WITHOUT COLLECTING MONEY
		//8 Go Back 3 SPACES
		//9 REPAIR HOUSES AND HOTELS
	private String chanceCards[] = { "Advance to Go.",
			"Go to jail. Move directly to jail. Do not pass Go. Do not collect $200.",
			"Advance to Hilo. If you pass Go collect $200.",
			"Take a trip to Las Vegas and if you pass Go collect $200.",
			"Advance to San Diego. If you pass Go collect $200.",
			"Advance to Florida.",
			"Go back three spaces.",
			"Make general repairs on all of your houses. For each house pay $25. For each hotel pay $100.",
			"You are assessed for street repairs: $40 per house, $115 per hotel.",
			"Pay school fees of $150.",
			"Drunk in charge fine $20.",
			"Speeding fine $15.",
			"Your building loan matures. Receive $150.",
			"You have won a crossword competition. Collect $100.",
			"Bank pays you dividend of $50.",
			"Get out of jail free. This card may be kept until needed or sold." };
	private int chanceCardType[]={2,7,2,2,2,2,8,9,9,1,1,1,3,3,3,5};
	private int gotoBoxNo[]={0,10,3,14,31,25,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int collectMoney[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,100,50,-1};
	private int payMoney[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,150,20,15,-1,-1,-1,-1};
	private int orderOfCards[]={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	private int currentCard;
	private Player playersPlaying[];
	private Token tokens[];
	private static int cardIndex;
	private int previousCard;
	private BoardBox boardBoxes[];
	public ChanceCard(Player[] players, Token tokens[], BoardBox boardBoxes[])
	{
		//shuffle order
		//fisher yates
		playersPlaying=players;
		this.tokens=tokens;
		this.boardBoxes=boardBoxes;
	    for(int i=0;i<16;i++)
	    {
	        int j=(int)(Math.random()*16); //j is a random number such that j is between 0 and i (inclusive)
	        if(j!=i )
	        {
	            //swap
	            int temp;
	            temp=orderOfCards[j];
	            orderOfCards[j]=orderOfCards[i];
	            orderOfCards[i]=temp;
	        }
	        
	    }
	    cardIndex=0;
		
	}
	public void implementChanceCardForPlayer(int playerNumber)
	{
		currentCard=orderOfCards[cardIndex];
		if(chanceCardType[currentCard]==2)
		{
			if(gotoBoxNo[currentCard]-playersPlaying[playerNumber].getPlayerPosititonOnBoard()<0)
			{
				playersPlaying[playerNumber].incrementPlayerBalance(200);
			}
			int distance=0;
			if(gotoBoxNo[currentCard]-playersPlaying[playerNumber].getPlayerPosititonOnBoard()>0)
			{
				distance=gotoBoxNo[currentCard]-playersPlaying[playerNumber].getPlayerPosititonOnBoard();
			}
			else
			{
				distance=40-playersPlaying[playerNumber].getPlayerPosititonOnBoard()+gotoBoxNo[currentCard];
			}
			tokens[playerNumber].moveToken(distance);
			playersPlaying[playerNumber].setPlayerPositionOnBoard(gotoBoxNo[currentCard]);
		}
		else if(chanceCardType[currentCard]==3)
		{
			playersPlaying[playerNumber].incrementPlayerBalance(collectMoney[currentCard]);
		}
		else if(chanceCardType[currentCard]==1)
		{
			playersPlaying[playerNumber].decrementPlayerBalance(payMoney[currentCard]);
		}
		else if(chanceCardType[currentCard]==7)
		{
			int distance=0;
			if(gotoBoxNo[currentCard]-playersPlaying[playerNumber].getPlayerPosititonOnBoard()>0)
			{
				distance=gotoBoxNo[currentCard]-playersPlaying[playerNumber].getPlayerPosititonOnBoard();
			}
			else
			{
				distance=40-playersPlaying[playerNumber].getPlayerPosititonOnBoard()+gotoBoxNo[currentCard];
			}
			tokens[playerNumber].moveToken(distance);
			playersPlaying[playerNumber].setTakenToJailStatus(true);
			playersPlaying[playerNumber].setPlayerPositionOnBoard(gotoBoxNo[currentCard]);
		}
		else if(chanceCardType[currentCard]==5)
		{
			playersPlaying[playerNumber].addGetOutOFJailCard();
		}
		else if(chanceCardType[currentCard]==8)
		{
			tokens[playerNumber].moveToken(37);
			playersPlaying[playerNumber].setPlayerPositionOnBoard(gotoBoxNo[currentCard]);
		}
		else if(chanceCardType[currentCard]==9)
		{
			int totalNoOfHouses=0;
			int totalNoOfHotels=0;
			for (String s : playersPlaying[playerNumber].propertiesBought) {
				int noOfHouses=boardBoxes[(boardBoxes[0].getBoxNumberByName(s))].getNoOfHousesBuilt();
				if(noOfHouses==5)
				{
					totalNoOfHotels=totalNoOfHotels+1;
				}
				else
				{
					totalNoOfHouses=totalNoOfHouses+noOfHouses;
				}
			}
			int val1;
			int val2;
			if(currentCard==7)
			{
				val1=25;
				val2=100;
			}
			else
			{
				val1=40;
				val2=115;
			}
			playersPlaying[playerNumber].decrementPlayerBalance((val1*totalNoOfHouses)+(val2*totalNoOfHotels));
		}
		cardIndex=(cardIndex+1)%16;
		previousCard=currentCard;
	}
	public String getChanceCardDescription()
	{
		return chanceCards[currentCard];
	}
	public int getChanceCardType()
	{
		return chanceCardType[currentCard];
	}
	public int getCurrentChanceCard()
	{
		return currentCard;
	}

}
