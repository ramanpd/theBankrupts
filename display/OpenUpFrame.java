package Sprint4v2.display;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static javafx.scene.input.DataFormat.URL;

/**
 * Created by Michal on 07/04/2017.
 */
public class OpenUpFrame extends JFrame{
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // universal size
    private double screenWidth = screenSize.getWidth();
    private double screenHeight = 0.95 * screenSize.getHeight();
    private int fontSize = (int)(screenHeight/40);
    private String fontType = "Serif";

    public OpenUpFrame(){

        Box box = Box.createVerticalBox();
        JButton[] buttons = {new JButton("New Game"), new JButton("Settings"), new JButton("Help"), new JButton("Exit")};
        JPanel[] panels = {new JPanel(), new JPanel(), new JPanel(), new JPanel()};
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("images/logo.png")).getImage().getScaledInstance((int)(screenWidth/5.0), (int)(screenHeight/5.0), Image.SCALE_SMOOTH));
        JLabel logo = (new JLabel(imageIcon));
        box.add(logo,CENTER_ALIGNMENT);

        for(int i = 0; i< buttons.length; i++){
            buttons[i].setPreferredSize(new Dimension((int)(screenHeight/3.0), (int)(screenHeight/15.0)));
            buttons[i].setFont(new Font(fontType, Font.PLAIN, fontSize));
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            panels[i].add(buttons[i]);
            box.add(panels[i]);
            box.add(Box.createVerticalStrut((int)(screenHeight/150.0)));
        }
        add(box);

        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                // start new game
                NewGameFrame game = new NewGameFrame(fontSize,fontType);
                game.setNewGame();
                dispose();
            }
        });

        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // display settings
                JFrame settings = createSettings();
                settings.setVisible(true);
            }
        });

        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(this.getClass().getResource("images/rules.pdf").toURI());
                } catch (Exception t) {
                    t.printStackTrace();
                }
            }
        });

        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setSize((int)(screenHeight/1.5), (int)(screenHeight/1.5));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private JFrame createSettings (){
        JFrame settings = new JFrame();

        Box box = Box.createVerticalBox();
        JComboBox settingsOptions[] = new JComboBox[3];
        JLabel label[] = {new JLabel("Font type:"),new JLabel("Font size (in %):") };
        JPanel[] panels = {new JPanel(), new JPanel(), new JPanel(), new JPanel()};
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("images/logo.png")).getImage().getScaledInstance((int)(screenWidth/5.0), (int)(screenHeight/5.0), Image.SCALE_SMOOTH));
        JLabel logo = (new JLabel(imageIcon));
        box.add(logo,CENTER_ALIGNMENT);
        String[][] fontSizeArray = {{"Serif (Recommended)","SansSerif","Monospaced"},{"100% (Recommended)","080%","120%","150%","050%"}};

        for(int i=0; i<label.length; i++){
            label[i].setPreferredSize(new Dimension((int)(screenHeight/3.0), (int)(screenHeight/15.0)));
            label[i].setFont(new Font(fontType, Font.BOLD, fontSize));
            label[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            panels[i].add(label[i]);
            settingsOptions[i] = new JComboBox(fontSizeArray[i]);
            ((JLabel)settingsOptions[i].getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            settingsOptions[i].setFont(new Font(fontType, Font.PLAIN, fontSize));
            settingsOptions[i].setPreferredSize(new Dimension((int)(screenHeight/3.0), (int)(screenHeight/15.0)));
            panels[i].add(settingsOptions[i]);
            box.add(panels[i]);
            box.add(Box.createVerticalStrut((int)(screenHeight/150.0)));
        }
        box.add(Box.createVerticalStrut((int)(screenHeight/50.0)));
        JButton saveButton =new JButton("Save");
        JButton cancelButton =new JButton("Cancel");
        saveButton.setPreferredSize(new Dimension((int)(screenHeight/8.0), (int)(screenHeight/15.0)));
        saveButton.setFont(new Font(fontType, Font.BOLD, fontSize));
        cancelButton.setPreferredSize(new Dimension((int)(screenHeight/8.0), (int)(screenHeight/15.0)));
        cancelButton.setFont(new Font(fontType, Font.BOLD, fontSize));
        panels[2].add(saveButton);
        panels[2].add(cancelButton);
        box.add(panels[2]);
        settings.add(box);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                // start new game
                settings.dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                // start new game
                String choiceFont = (String) settingsOptions[0].getSelectedItem();
                if(Objects.equals(choiceFont, "Serif (Recommended)")){
                    choiceFont = "Serif";
                }
                String choiceSize = (String) settingsOptions[1].getSelectedItem();
                choiceSize = choiceSize.substring(0,3);

                // set this font and this size everywhere....
                fontSize = (int)(fontSize*(Integer.parseInt(choiceSize)/100.0));
                fontType = choiceFont;
                settings.dispose();
            }
        });

        settings.setSize((int)(screenHeight/1.5), (int)(screenHeight/1.5));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        settings.setLocation(dim.width/2-settings.getSize().width/2, dim.height/2-settings.getSize().height/2);

        settings.setResizable(false);
        settings.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        return settings;
    }

}
