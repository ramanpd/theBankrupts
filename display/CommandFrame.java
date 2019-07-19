/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2.display;

import Sprint4v2.CommandListener;

import javax.swing.*;
import java.awt.*;

// class handles the Command
public class CommandFrame { // class for the Command Panel
	private JPanel commandPanel; // main panel
	public CommandListener listener; // makes action on command
	private JScrollPane commandOutputPane;
	public JButton buttonToSubmitCommands;
	private double screenHeight;
	String fontType;
	int fontSize;

	private JTextArea textArea;
	public JTextField textField;

	public CommandFrame(double screenHeight, String fontT, int fontS) { // assign values
		fontType= fontT;
		fontSize = (int)(fontS/3.0*2.0);
		commandPanel = new JPanel();
		this.screenHeight = screenHeight;
		createCommandFrame();
	}

	private void createCommandFrame() { // function creating text area and input
		// line with button
		buttonToSubmitCommands = new JButton("Submit");
		buttonToSubmitCommands.setFont(new Font(fontType, Font.PLAIN, fontSize));
		JPanel inputPanel = new JPanel();
		textField = new JTextField();
		textField.setFont(new Font(fontType, Font.PLAIN, fontSize));
		commandOutputPane = new JScrollPane();
		textArea = new JTextArea();
		textArea.setFont(new Font(fontType, Font.PLAIN, fontSize));
		textArea.setEditable(false);
		commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("Commands");
		label.setFont(new Font(fontType, Font.BOLD, (int) (fontSize * 1.5)));
		label.setAlignmentX(0.5f);
		commandPanel.add(label);
		commandPanel.add(commandOutputPane);
		commandPanel.add(inputPanel);
		commandOutputPane.setViewportView(textArea);
		inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		inputPanel.add(textField);
		inputPanel.add(buttonToSubmitCommands);
	}

	public void submitCommand() { // function which updates text area
		JScrollBar coScrollBar = commandOutputPane.getVerticalScrollBar(); // on
		// submission
		// of
		// a
		// command
		coScrollBar.setValue(coScrollBar.getMaximum());
		String tempCommand = listener.checkForCommands(textField.getText());
		if (tempCommand != null) {
			textArea.append(tempCommand + "\n");
		}
	}

	public void clear() {
		textField.setText("");
	} // edits text field

	public JPanel getPanel() {
		return commandPanel;
	}
}