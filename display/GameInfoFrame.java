/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2.display;

import Sprint4v2.player.Player;
import Sprint4v2.player.Token;

import java.awt.*;
import javax.swing.*;

//import static Sprint2v3.display.GameInfoFrame.TokenSelection.createAndShowGUI;
import static java.awt.Component.CENTER_ALIGNMENT;

// class handles Game Info
public class GameInfoFrame { // class for Game Info Panel

	private JPanel tokenPanel; // main panel
	private JLabel playerTurn;

	private Player[] playersPlaying; // number of players playing
	private int noOfPlayers;
	private int fontSize;
	private String fontType;

	private JTextField[] playerBalance;
	private JTextArea propertyInformationTextArea[];

	// constructor initisalises values
	public GameInfoFrame(double screenHeight, int playerToBeMoved, Player players[], int nrOfPlayers, String fontT, int fontS) {
		tokenPanel = new JPanel();
		noOfPlayers = nrOfPlayers;
		tokenPanel.setLayout(new BoxLayout(tokenPanel, BoxLayout.Y_AXIS));
		playersPlaying = players;
		fontSize=fontS;
		fontType=fontT;
		createGameInfoFrame(playerToBeMoved);
	}

	// creates Game Info
	private void createGameInfoFrame(int playerToBeMoved) {
		Box box = Box.createVerticalBox();
		JLabel label = new JLabel("GameInfo");
		label.setFont(new Font(fontType, Font.BOLD, fontSize));
		label.setAlignmentX(CENTER_ALIGNMENT);
		// playerTurn = new JLabel("Player No. " + (playerToBeMoved+1) + "
		// Please ROLL.");
		playerTurn = new JLabel("");
		playerTurn.setForeground(Token.colors[playerToBeMoved]);
		playerTurn.setFont(new Font(fontType, Font.BOLD, fontSize));
		playerTurn.setAlignmentX(CENTER_ALIGNMENT);

		box.add(label);
		box.add(playerTurn);
		tokenPanel.add(box);

		JPanel[] playerInfo = new JPanel[noOfPlayers];
		JLabel[] playerName = new JLabel[noOfPlayers];
		playerBalance = new JTextField[noOfPlayers];
		propertyInformationTextArea = new JTextArea[noOfPlayers];
		JScrollPane[] propertyInformationScrollPane = new JScrollPane[noOfPlayers];
		for (int i = 0; i < noOfPlayers; i++) {
			playerInfo[i] = new JPanel();
			playerInfo[i].setLayout(new BoxLayout(playerInfo[i], BoxLayout.Y_AXIS));
			playerName[i] = new JLabel(playersPlaying[i].getPlayerName());
			playerName[i].setForeground(Token.colors[i]);
			playerName[i].setFont(new Font(fontType, Font.BOLD, fontSize));
			playerName[i].setAlignmentX(CENTER_ALIGNMENT);
			playerName[i].setFont(new Font(fontType, Font.PLAIN, fontSize));
			playerBalance[i] = new JTextField();
			playerBalance[i].setBackground(Token.colors[i]);
			playerBalance[i].setText("Balance: " + playersPlaying[i].getPlayerBalance());
			playerBalance[i].setFont(new Font(fontType, Font.PLAIN, fontSize));
			propertyInformationTextArea[i] = new JTextArea();
			propertyInformationTextArea[i].setBackground(Token.colors[i]);
			// propertyInformationTextArea[i].setFont(new Font(fontType,
			// Font.BOLD, fontSize));
			propertyInformationScrollPane[i] = new JScrollPane();
			propertyInformationScrollPane[i].setViewportView(propertyInformationTextArea[i]);
			propertyInformationScrollPane[i].setFont(new Font(fontType, Font.PLAIN, fontSize));
			playerInfo[i].add(playerName[i]);
			playerInfo[i].add(playerBalance[i]);
			playerInfo[i].add(propertyInformationScrollPane[i]);
		}
		for (int i = 0; i < noOfPlayers; i++) {
			tokenPanel.add(playerInfo[i]);
		}
	}

	// Updates Game Inof
	public void updateOnCommand(int player) {
		// function to update panel on command
		playerTurn.setForeground(Token.colors[player]);
		// playerTurn.setText("Player No. " + (player+1) + " Please ROLL.");
		playerTurn.setText(playersPlaying[player].getPlayerName() + ": Please Roll.");
		for (int i = 0; i < noOfPlayers; i++) {
			playerBalance[i].setText("Balance: " + playersPlaying[i].getPlayerBalance());
		}
		for (int i = 0; i < noOfPlayers; i++) {
			String output = "";
			for (String s : playersPlaying[i].propertiesBought) {
				output = output + s + "\n";
			}
			propertyInformationTextArea[i].setText("List of Properties Owned:\n" + output);
		}
		tokenPanel.repaint();
	}

	public JPanel getPanel() {
		return tokenPanel;
	}
}
