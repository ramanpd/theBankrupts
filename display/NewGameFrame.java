/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2.display;

import Sprint4v2.Dice;
import Sprint4v2.GameFrame;
import Sprint4v2.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// class handles NEW GAME set up upon click on the menu button
public class NewGameFrame
{
	public Player playersPlaying[]; // number of players playing
    private ArrayList<String> playerName=new ArrayList<>(); // player names
    private int noOfPlayers=0;
    String[] noOfPlayerOptions = {"0","2","3","4","5","6"}; // array stores possible player numbers

    JFrame newGame = new JFrame("New Game"); // pop up window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // universal size
    double screenWidth = screenSize.getWidth();
    double screenHeight = 0.95 * screenSize.getHeight();
    int fontSize;
    String fontType;

    public NewGameFrame(int fontS, String fontT){
        fontSize = fontS;
        fontType = fontT;
    }
    // sets up new game
    public void setNewGame()
    {
        newGame.setSize((int)(screenHeight), (int)(screenHeight/1.5));
        newGame.setLocation(screenSize.width/2-newGame.getSize().width/2, screenSize.height/2-newGame.getSize().height/2);
        newGame.setResizable(false);
        newGame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // divides the frame
        JSplitPane topPanelSplitPane = new JSplitPane(); // mainPanel will contain top elements
        topPanelSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT); // vertical orientation
        topPanelSplitPane.setDividerLocation((int)(screenHeight*0.06));
        topPanelSplitPane.setDividerSize(0);

        JSplitPane bottomPanelSplitPane=new JSplitPane(); // will contain middle elements
        bottomPanelSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        bottomPanelSplitPane.setDividerLocation((int)(screenHeight*0.5));

        JPanel dropBoxPanel=new JPanel(); // for JComboBox
        // BoxLayout to align elements vertically
        dropBoxPanel.setLayout(new BoxLayout(dropBoxPanel,BoxLayout.LINE_AXIS));
        JPanel buttonPanel=new JPanel(); // for JButton
        JButton applyButton=new JButton("Apply");
        applyButton.setFont(new Font(fontType, Font.PLAIN, fontSize));

        JComboBox noOfPlayersDropBox=new JComboBox(noOfPlayerOptions); // Dropdown with array properties
        ((JLabel)noOfPlayersDropBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        noOfPlayersDropBox.setFont(new Font(fontType, Font.PLAIN, fontSize));
        JLabel label = new JLabel("Number of Players", SwingConstants.CENTER);
        label.setPreferredSize(new Dimension((int)(screenHeight/1.99),label.getHeight()));
        label.setFont(new Font(fontType, Font.BOLD, (int) (fontSize*1.5)));
        dropBoxPanel.add(label);


        // adding and setting all panels
        dropBoxPanel.add(noOfPlayersDropBox);
        buttonPanel.add(applyButton);
        bottomPanelSplitPane.setBottomComponent(buttonPanel);
        topPanelSplitPane.setTopComponent(dropBoxPanel);
        topPanelSplitPane.setBottomComponent(bottomPanelSplitPane);

        JTextField[] playerNameTextField=new JTextField[6];
        JButton[] playerNameLabel=new JButton[6];
        //JPanel[] playerInputPanel=new JPanel[6];

        // handles the ability to edit amount of text fields based on the number of players
        // at start none are editable
        for(int i=0;i<6;i++)
        {
            //playerInputPanel[i].setLayout(new BoxLayout(playerInputPanel[i],BoxLayout.X_AXIS));
            playerNameLabel[i]= new JButton("Player "+(i+1));
            playerNameLabel[i].setFont(new Font(fontType, Font.PLAIN, fontSize));
            playerNameTextField[i]=new JTextField();
            playerNameTextField[i].setEditable(false);

        }

        JPanel playerInfoPanel=new JPanel();
        playerInfoPanel.setLayout(new GridLayout(6,3));

        for(int i=0;i<6;i++)
        {
            playerInfoPanel.add(playerNameLabel[i]);
            playerInfoPanel.add(playerNameTextField[i]);
        }
        bottomPanelSplitPane.setTopComponent(playerInfoPanel);
        newGame.add(topPanelSplitPane);
        newGame.setVisible(true);
        noOfPlayersDropBox.getRootPane().setDefaultButton(applyButton);
        noOfPlayersDropBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // handles the ability to edit amount of text fields based on the number of players
                for(int i=0;i<6;i++)
                {
                    playerNameTextField[i].setEditable(false);
                }
                JComboBox cb = (JComboBox)e.getSource();
                String noOfPlayersString = (String)cb.getSelectedItem();
                noOfPlayers=Integer.parseInt(noOfPlayersString);

                // editable only the same as number of players
                for(int i=0;i<noOfPlayers;i++)
                {
                    playerNameTextField[i].setFont(new Font(fontType, Font.PLAIN, fontSize));
                    playerNameTextField[i].setEditable(true);
                }
            }
        });

        // on button click
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // adds playets
                for(int i=0;i<noOfPlayers;i++) {
                    playerName.add(playerNameTextField[i].getText());
                }

                playersPlaying=new Player[noOfPlayers];

                // sets their names
                for(int i=0;i<noOfPlayers;i++)
                {
                	playersPlaying[i]=new Player(playerName.get(i));
                }
                
                newGame.dispose(); // close JFrame
                rollDiceToDecideTurns();
                GameFrame updatedFrame=new GameFrame(fontSize,fontType);
                updatedFrame.updateFrame(noOfPlayers,playersPlaying);

            }
        });

    }

    // method to decide who goes first
    private void rollDiceToDecideTurns()
    {
    	Dice dice[]=new Dice[noOfPlayers];
    	int diceValues[]=new int[noOfPlayers];
    	for(int i=0;i<noOfPlayers;i++)
    	{
    		dice[i]=new Dice();
    		diceValues[i]=dice[i].getDie1()+dice[i].getDie2();
    	}
    	for(int i=0;i<noOfPlayers;i++)
    	{
    		for(int j=0;j<noOfPlayers-i-1;j++)
    		{
    			if(diceValues[j]<diceValues[j+1])
    			{
    				int temp;
    				temp=diceValues[j];
    				diceValues[j]=diceValues[j+1];
    				diceValues[j+1]=temp;
    				Player tempPlayer;
    				tempPlayer=playersPlaying[j];
    				playersPlaying[j]=playersPlaying[j+1];
    				playersPlaying[j+1]=tempPlayer;
    			}
    		}
    	}
    }

}
