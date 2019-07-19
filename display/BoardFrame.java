/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2.display;

import Sprint4v2.BoardBox;
import Sprint4v2.player.ChanceCard;
import Sprint4v2.player.CommunityChestCards;
import Sprint4v2.player.Player;
import Sprint4v2.player.Token;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.geom.AffineTransform;

// class handles the Board
public class BoardFrame extends JPanel{

    private Player[] playersPlaying;
    private double screenHeight;            // dimensions
    private double screenWidth;
    public BoardBox boardBoxes[];           // Boxes of the board
    public Token tokens[];                  // Tokens on the board
    private BufferedImage boardImage;       // Monopoly Image
    private double boardLength;             // Board size (square)
    private int numberOfPlayers;            // Total nr of Players
    private int dice1=0;
    private int dice2=0;
    private int currentBoxNo=0;
    private Boolean moveMade=false;
    private double[][] houses = new double[40][2];
    private BufferedImage[] images;
    private CommunityChestCards communityChestCards;
    private ChanceCard chanceCard;
    private BoardBox[] boardBox;
    private int fontSize;
    private String fontType;

    public BoardFrame(int nrOfPlayer, Player players[], double screenH, double screenW, String fontT, int fontS) {

        this.screenHeight = screenH;                // get dimensions and
        this.screenWidth = screenW;                 // create a panel
        fontSize=fontS;
        fontType=fontT;
        playersPlaying=players;
        numberOfPlayers = nrOfPlayer;
        tokens = new Token[numberOfPlayers];        // create tokens for each Sprint2v2.player
        try {
            boardImage = ImageIO.read(this.getClass().getResource("images/monopolyBoard.png"));       // add the image
            boardLength=screenHeight/1.33;                                                              // scale the image
        } catch (IOException ignored) {}

        for(int i=0;i<numberOfPlayers;i++)
        {
            tokens[i]=new Token();                                                                      // make each Sprint2v2.player a new token
        }
        createBoardBoxInfo();                       // create the Board Frame
        createMenuBar();
    }

    public JPanel createMenuBar() {                  // create Menu Panel Above Monopoly Board
        JPanel menuPanel = new JPanel();            // panel with few buttons
        //add(menuPanel);
        menuPanel.setMaximumSize(new Dimension((int) (screenWidth / 1.7), 60));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        JButton[] buttons = {new JButton("New Game"), new JButton("Save"), new JButton("Load"), new JButton("Help")};

        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameFrame game = new NewGameFrame(fontSize,fontType);
                game.setNewGame();

            }
        });

        for (JButton button:buttons) {
            button.setFont(new Font("Serif", Font.PLAIN, (int)screenHeight/50));
            menuPanel.add(button);
        }
        return menuPanel;
    }

    private void createBoardBoxInfo()                   // function to get center of each box on the Monopoly Board
    {
        boardBoxes = new BoardBox[40];
        double x = 0.07 * boardLength;
        double y = 1.01 * boardLength;
        for(int i=0;i<40;i++)
        {
            if(i < 11) {
                y -= 0.08 * boardLength;
                if(i % 9 == 1)
                    y -= 0.03 * boardLength;
            } else if (i < 21) {
                x += 0.078 * boardLength;
                if((i-10) % 9 == 1)
                    x += 0.04 * boardLength;
            } else if (i < 31) {
                y += 0.08 * boardLength;
                if((i-20) % 9 == 1)
                    y += 0.03 * boardLength;
            } else {
                x -= 0.078 * boardLength;
                if((i-30) % 9 == 1)
                    x -= 0.04 * boardLength;
            }
            boardBoxes[i]=new BoardBox(x, y, i);                // assign co-ordinates to each BoardBox[]
            houses[i][0] = x;
            houses[i][1] = y;
        }
        boardBoxes[0].setPlayers(numberOfPlayers);
    }
    public void moveTokenByDice(int[] diceValues, int PlayerToBeMoved, CommunityChestCards communityChestCards, ChanceCard chanceCard, BoardBox[] boardBox){     // function to move a token
        dice1 = diceValues[0];
        dice2 = diceValues[1];
        this.communityChestCards = communityChestCards;
        this.chanceCard = chanceCard;
        this.boardBox = boardBox;
        if(playersPlaying[PlayerToBeMoved].isActiveStatus()){
            makeMove(tokens[PlayerToBeMoved], dice1+dice2);
        }
        else{
            // remove the token...
            tokens[PlayerToBeMoved].removeToken();
        }
        if(dice1+dice2!=0 && dice1 != dice2) {
            moveMade = true;
        }
        repaint();
    }

    private void makeMove(Token token , int distance) {             // function to make move
        boardBoxes[token.getBoxNo()].removePlayer();
        boardBoxes[token.moveToken(distance)].addPlayer();
        currentBoxNo = token.getBoxNo();
    }

    @Override
    public void paintComponent(Graphics g) {                // function to paint tokens on the Board
        super.paintComponent(g);
        g.drawImage(boardImage, 0, 0,(int) (screenHeight / 1.30), (int) (screenHeight / 1.30),null); // see javadoc for more info on the parameter
        g.setPaintMode();
        for(BoardBox box : boardBoxes) {
            if(box.getPlayers() != 0) {
                paintBox(g, box);
            }
        }

        if (currentBoxNo != 2 && currentBoxNo != 17 && currentBoxNo != 33) {
            paintChest(g, images[0]);
        }
        if (currentBoxNo != 7 && currentBoxNo != 22 && currentBoxNo != 36) {
            paintQuestion(g, images[20]);
        }
        if(moveMade) {
            if (currentBoxNo != 0 || currentBoxNo != 2 || currentBoxNo != 4 || currentBoxNo != 7 || currentBoxNo != 10 || currentBoxNo != 12 || currentBoxNo != 17 || currentBoxNo != 20 || currentBoxNo != 22 || currentBoxNo != 28 || currentBoxNo != 33 || currentBoxNo != 36 || currentBoxNo != 38) {
                paintProperty(g, images[currentBoxNo + 40]);
            }
            if (currentBoxNo == 2 || currentBoxNo == 17 || currentBoxNo == 33) {
                paintChest(g, images[communityChestCards.getCurrentCommunityCard() + 1]);
            }
            if (currentBoxNo == 7 || currentBoxNo == 22 || currentBoxNo == 36) {
                paintQuestion(g, images[chanceCard.getCurrentChanceCard() + 21]);
            }
            paintDice1(g,images[100 + dice1]);
            paintDice2(g,images[100 + dice2]);

            moveMade=false;
        }
        if(boardBox!=null){
            for(int i=0; i<40; i++) {
                if(boardBox[i].getNoOfHousesBuilt() > 0){
                    paintHouses(g,i,boardBox[i].getNoOfHousesBuilt());
                }
            }
        }
    }

    public void loadImages(){
        images = new BufferedImage[120];
        int i;
        try {
            images[0] = ImageIO.read(this.getClass().getResource("images/community/chestBack.jpg"));
            for(i=1; i<= 17; i++){                                                                  // load chest cards
                images[i] = ImageIO.read(this.getClass().getResource("images/community/community"+Integer.toString(i-1)+".jpg"));
            }

            images[18] = ImageIO.read(this.getClass().getResource("images/houseImage.png"));        // house
            images[18] = resize(images[18],(int) (boardLength / 50.0),(int) (boardLength / 50.0));
            images[19] = ImageIO.read(this.getClass().getResource("images/hotelImage.png"));        // hotel
            images[19] = resize(images[19],(int) (boardLength / 45.0),(int) (boardLength / 45.0));

            images[20] = ImageIO.read(this.getClass().getResource("images/chance/chanceBack.jpg"));
            for(i=21; i<=36; i++){
                images[i] = ImageIO.read(this.getClass().getResource("images/chance/chance"+Integer.toString(i-21)+".jpg"));
            }
            for(i= 41; i<80; i++){
                if(i-40!=0 && i-40!=2 && i-40!= 4 && i-40!= 7 && i-40!= 10 && i-40!= 12 && i-40!= 17 && i-40!= 20 && i-40!= 22 && i-40!= 30 && i-40!= 28 && i-40!= 33 && i-40!= 36 && i-40!= 38 ){
                    images[i] = ImageIO.read(this.getClass().getResource("images/property/"+Integer.toString(i-40)+".jpg"));
                }
            }
            // dice
            for(i=101; i<=106; i++){
                images[i] = ImageIO.read(this.getClass().getResource("images/dice"+ Integer.toString(i-100) +".png"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paintProperty(Graphics g, BufferedImage property){
        g.drawImage(property,(int)(boardLength/5.0), (int)(boardLength - boardLength/2.5), (int)(2.2*boardLength/10.0),(int)(2.2*boardLength/10.0),this);
    }
    private void paintChest(Graphics g, BufferedImage chest) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform rotation = new AffineTransform();
        rotation.translate((int)(boardLength/3.05),(int)(boardLength/3.3));
        rotation.rotate(-Math.PI/8.5);
        rotation.translate(-(int)(boardLength/5.0)/2, -(int)(boardLength/7.0)/2);
        g2d.drawImage(resize(chest,(int)(boardLength/5.0),(int)(boardLength/7.0)), rotation, this);
    }

    private void paintDice1(Graphics g, BufferedImage correctDice){
        g.drawImage(correctDice,(int)(boardLength/2.2) - (int)(boardLength/15.0), (int)(boardLength/2.2), (int)(2.2*boardLength/15.0),(int)(2.2*boardLength/15.0),this);
    }
    private void paintDice2(Graphics g, BufferedImage correctDice){
        g.drawImage(correctDice,(int)(boardLength/2.2) + (int)(boardLength/15.0), (int)(boardLength/2.2), (int)(2.2*boardLength/15.0),(int)(2.2*boardLength/15.0),this);
    }

    private void paintQuestion(Graphics g, BufferedImage chance){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform rotation = new AffineTransform();
        rotation.translate((int)(boardLength/3.05) + (int)(boardLength/2.8),(int)(boardLength/3.3) + (int)(boardLength/2.35));
        rotation.rotate(-Math.PI/8.5);
        rotation.translate(-(int)(boardLength/5.0)/2, -(int)(boardLength/7.0)/2);
        g2d.drawImage(resize(chance,(int)(boardLength/5.0),(int)(boardLength/7.0)), rotation, this);
    }

    private static BufferedImage resize(BufferedImage img, int newWidth, int newHeight) {
        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    private void paintBox(Graphics g, BoardBox box) {       // function to assign tokens on a box accordingly to
        int radius=(int)(0.023*boardLength);
        double x = box.getXcoordinate();
        double y = box.getYcoordinate();
        Token t[] = new Token[numberOfPlayers];
        int i = 0;
        for(Token token : tokens) {
            if(token.getBoxNo() == box.getBoxNo()) {
                t[i++] = token;
            }
        }

        // holds possibilities of positions on a single box
        double[][][] positions = {
                {{0,0}},
                {{-0.7,0},{0.7,0}},
                {{0,-0.55},{-0.65,0.55},{0.65,0.55}},
                {{-0.55,-0.55},{0.55,-0.55},{-0.55,0.55},{0.55,0.55}},
                {{0,-0.9},{0.9,-0.2},{0.55,0.9},{-0.55, 0.9},{-0.9,-0.2}},
                {{-0.55, -1},{0.55,-1},{-1.1,0},{1.1,0},{-0.55,1},{0.55,1}}
        };

        for(int j=0; j<i; j++){ // j = current token; i = number of tokens
            g.setColor(t[j].getColor());
            g.fillOval((int)(x+positions[i-1][j][0]*radius),(int)(y+positions[i-1][j][1]*radius), radius,radius);
        }
    }

    private void paintHouses(Graphics g, int houseNrToPaint, int numberOfHouses){
        BufferedImage houseImage = images[18];
        int radius = (int) (boardLength / 50.0);
        int j = houseNrToPaint;

        double[][][] positions = {
                {{0, 0}},
                {{-0.7, 0}, {0.7, 0}},
                {{-1.05, 0}, {0, 0}, {1.05, 0}},
                {{-1.55, 0}, {-0.505, 0}, {0.505, 0}, {1.55, 0}},
        };

        if (j == 0 || j == 5 || j == 2 || j == 15 || j == 25 || j == 7 || j == 12 || j == 17 || j == 22 || j == 28 || j == 33 || j == 36 || j == 38) {
            j++;
        }
        if (j == 4 || j == 35) {
            j = j + 2;
        }
        if(numberOfHouses<5) {
            for (int i = 0; i < numberOfHouses; i++) {
                if (j < 10) {
                    g.drawImage(houseImage, (int) (houses[j][0] + boardLength / 15.0), (int) (houses[j][1] + positions[numberOfHouses - 1][i][0] * radius), this);
                }
                if (j > 10 && j < 20) {
                    g.drawImage(houseImage, (int) (houses[j][0] + positions[numberOfHouses - 1][i][0] * radius), (int) (houses[j][1] + boardLength / 18.0), this);
                }
                if (j > 20 && j < 30) {
                    g.drawImage(houseImage, (int) (houses[j][0] - boardLength / 15.0), (int) (houses[j][1] + positions[numberOfHouses - 1][i][0] * radius), this);
                }
                if (j > 30 && j < 40) {
                    g.drawImage(houseImage, (int) (houses[j][0] + positions[numberOfHouses - 1][i][0] * radius), (int) (houses[j][1] - boardLength / 18.0), this);
                }
            }
        }
        if(numberOfHouses==5){
            houseImage = images[19];
            if (j < 10) {
                g.drawImage(houseImage, (int) (houses[j][0] + boardLength / 15.0), (int) (houses[j][1] ), this);
            }
            if (j > 10 && j < 20) {
                g.drawImage(houseImage, (int) (houses[j][0]), (int) (houses[j][1] + boardLength / 18.0), this);
            }
            if (j > 20 && j < 30) {
                g.drawImage(houseImage, (int) (houses[j][0] - boardLength / 15.0), (int) (houses[j][1]), this);
            }
            if (j > 30 && j < 40) {
                g.drawImage(houseImage, (int) (houses[j][0]), (int) (houses[j][1] - boardLength / 18.0), this);
            }
        }
    }

}
