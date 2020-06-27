import java.awt.*; 
import javax.swing.*;

public class startingScreen{
	
	private JFrame frame;
	private JPanel panel;
	private JProgressBar progressBar;
	private int counter = 0;
	private Color myColor = new Color(173,216,230);
	
	public static void main(String[] args) {
		new startingScreen();
	}
	
	public startingScreen() {
		frame = new JFrame("Patrick's Text Editor");
		frame.setSize(350, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.pack();
		panel = new JPanel();
		panel.setBackground(myColor);
		frame.add(panel, BorderLayout.CENTER);
		frame.setBackground(Color.lightGray);
		frame.setVisible(true);
		
		
		JLabel label = new JLabel("Welcome to Text Editor", JLabel.CENTER);
		label.setFont(new Font("Papyrus", Font.PLAIN, 22));
		panel.add(label, BorderLayout.CENTER);
		
		JLabel label2 = new JLabel("", JLabel.CENTER);//#
		label2.setFont(new Font("", Font.PLAIN, 15));
		panel.add(label2, BorderLayout.CENTER);//#
		
		label.setBorder(BorderFactory.createEmptyBorder(50, 0, 15, 15));//was panel...
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(200,200));
		progressBar.setMaximum(100);
		progressBar.setVisible(true);
		panel.add(progressBar,BorderLayout.SOUTH);
		frame.revalidate();
		
		progressBar.setBorder(BorderFactory.createEmptyBorder(-120, 0, 15, 15));
				
		while (counter<=100) {
			
			progressBar.setStringPainted(true);
			if(counter < 65) {
				label2.setText("Setting up Work Space...");
			}
			else {
				label2.setText("Loading in Text Editor...");
			}
			progressBar.setValue(counter);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			counter++;
		}
		//System.out.println("Done");
		frame.setVisible(false);
		Text t = new Text();
	}
}
