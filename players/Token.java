/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2.player;

import javax.swing.*;
import java.awt.*;

// class handles the tokens
public class Token extends JPanel
{
	private int boxNo; // unique box number
	private Color color; // colour
	public static Color colors[] = {				// create colors for tokens
			Color.ORANGE,
			Color.BLUE,
			Color.GREEN,
			Color.MAGENTA,
			Color.CYAN,
			Color.YELLOW
	};

	private static int noOfPlayers = 0;					// number of players
	public Token(){
		this.boxNo=0;
		this.color = colors[noOfPlayers++];

	}

	public void removeToken(){this.color = new Color(0f,0f,0f,.0f );}
	public int getBoxNo()
	{
		return this.boxNo;
	} // returns box number
	public Color getColor() {
		return color;
	} // returns the colour
	public int moveToken(int distance) { // makes movement of the token
		boxNo = (boxNo + distance) % 40;
		return boxNo;
	}
}
