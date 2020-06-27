import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Text implements ActionListener{
	
	private JFrame frame;
	private JPanel panel;
	private JTextArea textBox;
	
	int count = 0;
	
	public Text() {
		frame = new JFrame("Patricks Text Editor");
		panel = new JPanel();
		textBox = new JTextArea();
		
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));//maybe make it to fit whole frame instead
		panel.setLayout(new GridLayout(0,1));
		panel.setBackground(Color.white);
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350, 350);
		frame.setVisible(true);
		
		frame.add(textBox);//adding the text component to the Frame
		
		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		
		JMenu file = new JMenu("File");
		menubar.add(file);
		JMenuItem save = new JMenuItem("Save");
		file.add(save);
		save.addActionListener(this);
		JMenuItem open = new JMenuItem("Open File...");
		file.add(open);
		open.addActionListener(this);
		JMenuItem print = new JMenuItem("Print");
		file.add(print);
		print.addActionListener(this);
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		exit.addActionListener(this);
		
		JMenu edit = new JMenu("Edit");
		menubar.add(edit);
		JMenuItem copy = new JMenuItem("Copy");
		edit.add(copy);
		copy.addActionListener(this);
		JMenuItem cut = new JMenuItem("Cut");
		edit.add(cut);
		cut.addActionListener(this);
		JMenuItem paste = new JMenuItem("Paste");
		edit.add(paste);
		paste.addActionListener(this);
		JMenuItem all = new JMenuItem("Select All");
		edit.add(all);
		all.addActionListener(this);
				
		JMenu help = new JMenu("Help");
		menubar.add(help);
		JMenuItem about = new JMenuItem("About");
		help.add(about);
		about.addActionListener(this);
		
		frame.revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String option = e.getActionCommand(); 
		
		if(option.equals("Exit")) {
			System.exit(0);
		}
		else if(option.equals("About")) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "This is a text editor, made by Patrick K, Email: patrickkhoury0gmail.com");	
		}
		else if(option.equals("Print")) {
			try {
				textBox.print();
			} catch (PrinterException e1) {
				e1.printStackTrace();
			}
		}
		else if(option.equals("Save")) {//******************Make sure its right
			String getFileName = JOptionPane.showInputDialog(null, "Enter your directory name with a file name at the end.\nEx: /Users/userName/Desktop/one.txt");	
			FileWriter outputStream = null;
			try {
				outputStream = new FileWriter(new File(getFileName), false);				
			System.out.println("File found");//^/Users/userName/Desktop/one.txt
			
			String getText = textBox.getText();
			outputStream.write(getText);
			outputStream.close();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(option.equals("Open File...")) {
			String getFileName = JOptionPane.showInputDialog(null, "Enter your directory name with a file name at the end.\nEx: /Users/userName/Desktop/one.txt");
			Scanner inputStream = null;
			try {
				inputStream = new Scanner(new FileInputStream(getFileName));
				System.out.println("File found");
			}
			catch(FileNotFoundException e1) {
				System.out.println("Sorry that file was not found");
			}
			
			String line = "";
			while (inputStream.hasNextLine()) {
				line = line + inputStream.nextLine()  + "\n" ;
				System.out.println(line);//using for debugging
			}
			textBox.setText(line);
			inputStream.close();
			
			
		}
		else if(option.equals("Select All")) {
			textBox.selectAll();
		}
		else if(option.equals("Copy")) {
			textBox.copy();
		}
		else if(option.equals("Cut")) {
			textBox.cut();
		}
		else if(option.equals("Paste")) {
			textBox.paste();
		}
		
	}
	
	public static void main(String[] args) {
		Text test = new Text();	
	}
}

