/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2;

import javax.swing.*;

public class Dice					 // class that handles the dice action
{
	private int die1; 				// number on first dice
	private int die2; 				// number on second dice
	private int sum; 				// the sum of both dice
	private JLabel picture;			// picture of dice

	public Dice() 					// new dice class
	{
		roll();
	}

	public void roll() 				// rolls the dice
	{
		die1 = (int) (Math.random() * 6) + 1;
		die2 = (int) (Math.random() * 6) + 1;

		// images appear based on the number
		// picture = new JLabel();
		// ImageIcon icon1 = createImageIcon("images/" + die1 + ".png");
		// picture.setIcon(icon1);
		// ImageIcon icon2 = createImageIcon("images/" + die2 + ".png");
		// picture.setIcon(icon2);
	}

	// protected static ImageIcon createImageIcon(String path) {
	// //java.net.URL imgURL = TokenSelection.class.getResource(path);
	// if (imgURL != null) {
	// return new ImageIcon(imgURL);
	// } else {
	// System.err.println("Couldn't find file: " + path);
	// return null;
	// }
	// }

	public int getDie1()					 // value of die1
	{
		return die1;
	}

	public int getDie2() 					// value of die2
	{
		return die2;
	}
}
