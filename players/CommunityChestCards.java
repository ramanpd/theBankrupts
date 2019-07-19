package Sprint4v2.player;

import Sprint4v2.player.Player;
import Sprint4v2.player.Token;

public class CommunityChestCards {
	//TYPES OF COMMUNITY CHEST 
		//1 PAY RENT
		//2 GOTO A PLACE
		//3 COLLECT MONEY
		//4 COLLECT MONEY FROM OTHER PLAYERS
		//5 GET OUT OF JAIL CARD
		//6 PAY RENT OR TAKE A CHANCE
		//7 GOTO JAIL WITHOUT COLLECTING MONEY
	private String communityChestCards[] = { "Advance to Go.", "Go back to Boston.", "Go to jail.",
			"Move directly to jail. Do not pass Go. Do not collect $200.", "Pay hospital $100.",
			"Doctor's fee. Pay $50.", "Pay your insurance premium $50.", "Bank error in your favour. Collect $200.",
			"Annuity matures. Collect $100.", "You inherit $100.", "From sale of stock you get $50.",
			"Receive interest on 7% preference shares: $25.", "Income tax refund. Collect $20.",
			"You have won second prize in a beauty contest. Collect $10.",
			"It is your birthday. Collect $10 from each player.",
			"Get out of jail free. This card may be kept until needed or sold.", "Pay a £10 fine or take a Chance." };
	private int communityChestType[]={2,2,2,7,1,1,1,3,3,3,3,3,3,3,4,5,6};
	private int gotoBoxNo[]={0,6,10,10,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int collectMoney[]={-1,-1,-1,-1,-1,-1,-1,200,100,100,50,25,20,10,-1,-1,-1};
	private int payMoney[]={-1,-1,-1,-1,100,50,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int orderOfCards[]={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	private int currentCard;
	private int previousCard;
	private static int cardIndex;
	private Player playersPlaying[];
	private Token tokens[];
	public CommunityChestCards(Player[] players, Token tokens[])
	{
		//shuffle order
		//fisher yates
		playersPlaying=players;
		this.tokens=tokens;
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
	    currentCard=orderOfCards[cardIndex];
		
	}
	public void implementCommunityChestForPlayer(int playerNumber)
	{
		currentCard=orderOfCards[cardIndex];
		if(communityChestType[currentCard]==2)
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
			if(gotoBoxNo[currentCard]==10)
			{
				playersPlaying[playerNumber].setTakenToJailStatus(true);
			}
			playersPlaying[playerNumber].setPlayerPositionOnBoard(gotoBoxNo[currentCard]);
		}
		else if(communityChestType[currentCard]==3)
		{
			playersPlaying[playerNumber].incrementPlayerBalance(collectMoney[currentCard]);
		}
		else if(communityChestType[currentCard]==1)
		{
			playersPlaying[playerNumber].decrementPlayerBalance(payMoney[currentCard]);
		}
		else if(communityChestType[currentCard]==7)
		{
			int distance=0;
			playersPlaying[playerNumber].setTakenToJailStatus(true);
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
		else if(communityChestType[currentCard]==5)
		{
			playersPlaying[playerNumber].addGetOutOFJailCard();
		}
		else if(communityChestType[currentCard]==4)
		{
			int totalPlayers=0;
			for(Player s: playersPlaying)
			{
				s.decrementPlayerBalance(10);
				totalPlayers++;
			}
			playersPlaying[playerNumber].incrementPlayerBalance(totalPlayers*10);
		}
		cardIndex=(cardIndex+1)%16;
		previousCard=currentCard;
	}
	public String getCommunityCardDescription()
	{
		return communityChestCards[currentCard];
	}
	public int getCommunityCardType()
	{
		return communityChestType[currentCard];
	}
	public String getPreviousCommunityChestDescription()
	{
		return communityChestCards[previousCard];
	}
	public int getCurrentCommunityCard()
	{
		return currentCard;
	}

}
