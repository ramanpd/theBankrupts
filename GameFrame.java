/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2;

import Sprint4v2.CommandListener;
import Sprint4v2.display.BoardFrame;
import Sprint4v2.display.CommandFrame;
import Sprint4v2.display.GameInfoFrame;
import Sprint4v2.display.GamePlayFrame;
import Sprint4v2.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {			// main class for the Frame

	private static GameInfoFrame gameInfoFrame;			// create panels for the window
	private static GamePlayFrame gamePlayFrame;
	private static CommandFrame commandFrame;
	private static BoardFrame boardFrame;

	// Split the component in top and bottom
	private int TokenNumber = 0;						// Current Player that is moving
    private int numberOfPlayers=0;  // Total nr of Players
    private Player playersPlaying[];
	private Container c = getContentPane();				// container for all classes

	int fontSize;
	String fontType;

	public GameFrame(int fontS, String fontT) {
		fontSize=fontS;
		fontType=fontT;
	}

	public GameFrame createGameFrame() {				// create classes and divide main window

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = 0.95 * screenSize.getHeight();
		setPreferredSize(new Dimension((int) screenWidth, (int) (screenHeight)));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JSplitPane mainSplitPane = new JSplitPane();			// main window dividers
		JSplitPane leftSplitPane = new JSplitPane();
		JSplitPane rightSplitPane = new JSplitPane();

		mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		leftSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		mainSplitPane.setDividerLocation((int) (screenHeight / 1.3));		// specify division dimensions
		leftSplitPane.setDividerLocation((int) (screenHeight / 1.25));
		rightSplitPane.setDividerLocation((int) (screenHeight / 1.5));

		c.setLayout(new GridLayout());				// GridLayout

		mainSplitPane.setDividerSize(0);			// set border to 0
		leftSplitPane.setDividerSize(0);
		rightSplitPane.setDividerSize(0);
		setResizable(true);

		mainSplitPane.setLeftComponent(leftSplitPane);		// assign Panes to Component sides
		mainSplitPane.setRightComponent(rightSplitPane);

		Box box = Box.createVerticalBox();
        boardFrame = new BoardFrame(numberOfPlayers,playersPlaying, screenHeight, screenWidth, fontType, fontSize);		// add Frames to specified area
		boardFrame.loadImages();
		box.add(boardFrame.createMenuBar());
		box.add(boardFrame);
		leftSplitPane.setTopComponent(box);


		// command Frame
		commandFrame = new CommandFrame(screenHeight, fontType, fontSize);
        commandFrame.listener = new CommandListener(numberOfPlayers,playersPlaying, boardFrame.boardBoxes, boardFrame.tokens );
		leftSplitPane.setBottomComponent(commandFrame.getPanel());
		getRootPane().setDefaultButton(commandFrame.buttonToSubmitCommands);
		// GamePlay
		gamePlayFrame = new GamePlayFrame(screenHeight,playersPlaying, numberOfPlayers,fontType,fontSize);
		rightSplitPane.setBottomComponent(gamePlayFrame.getPanel());

		// Game Info
		gameInfoFrame = new GameInfoFrame(screenHeight, TokenNumber, playersPlaying, numberOfPlayers,fontType,fontSize);
		rightSplitPane.setTopComponent(gameInfoFrame.getPanel());

        addListener();			// add button for action
		c.add(mainSplitPane);
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		return null;
	}

	// adds listener to the command
	private void addListener() {		// look for submit
		commandFrame.buttonToSubmitCommands.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {		// update all Panels on Submission
                commandFrame.submitCommand();
                TokenNumber = commandFrame.listener.playerNumber;
                boardFrame.moveTokenByDice(commandFrame.listener.getDiceSum(), TokenNumber, commandFrame.listener.communityChest, commandFrame.listener.chanceCard, commandFrame.listener.boardBoxes);
				gamePlayFrame.updateOnCommand(commandFrame.textField, (TokenNumber+1), commandFrame.listener.dice1Value,commandFrame.listener.dice2Value ,boardFrame.boardBoxes, boardFrame.tokens,commandFrame.listener.communityChest,commandFrame.listener.chanceCard);
                gameInfoFrame.updateOnCommand(TokenNumber);
                commandFrame.clear();
            }
		});
	}

	// updates the frame
	public void updateFrame(int updatedNoOfPlayers,Player players[])
	{
		dispose();
		numberOfPlayers=updatedNoOfPlayers;
		//playersPlaying=new Player[updatedNoOfPlayers];
		playersPlaying=players;
		createGameFrame();

	}

}