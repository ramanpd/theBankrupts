/*
TheBankrupts
Final version for Sprint 2

Patryk Labuzek - 15440728
Michal Gwizdz  - 15522923
Raman Prasad   - 15203657
*/

package Sprint4v2;

import Sprint4v2.display.OpenUpFrame;

import java.awt.*;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("unused")
			@Override
			public void run() {
				OpenUpFrame tem = new OpenUpFrame();
			}
		});
	}
}