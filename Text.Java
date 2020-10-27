import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;

import edu.princeton.cs.algs4.LinkedStack;

public class Text implements ActionListener{
	
	private JFrame frame;
	private JPanel panel;
	private static JTextPane textBox;
	JMenuItem exit;

	static SquigglePainter red = new SquigglePainter(Color.RED);
	SpellChecker check = new SpellChecker("dictionary.txt");
	LinkedStack<String> temp = new LinkedStack<String>();
	LinkedStack<String> list = new LinkedStack<String>();
	
	int count = 0;
	
	public Text() {
		frame = new JFrame("Patricks Text Editor");
		panel = new JPanel();
		textBox = new JTextPane();
		
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(0,1));
		panel.setBackground(Color.white);
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350, 350);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);//centers to screen...
		frame.revalidate();
		
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
		exit = new JMenuItem("Exit");
		file.add(exit);
		exit.addActionListener(this);
		
		JMenu edit = new JMenu("Edit");
		menubar.add(edit);
		JMenuItem spellCheck = new JMenuItem("SpellCheck");
		edit.add(spellCheck);
		spellCheck.addActionListener(this);
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
		JMenuItem info = new JMenuItem("Task Info");
		help.add(info);
		info.addActionListener(this);
		
		frame.revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String option = e.getActionCommand(); 
		
		if(option.equals("Exit")) {
			System.exit(0);
		}
		else if(option.equals("About")) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "<html>This is a text editor, made by Patrick K<br>Email: patrickkhoury0gmail.com</html>");	
		}
		else if(option.equals("Task Info")) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "<html>Includes SpellChecker<br>To use spellchecking, go to Edit -> Spellchecking<br>Then right click on words to see corrections, or add to dictionary</html>");			
		}
		else if(option.equals("SpellCheck")) {
			final Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
			
			final StyledDocument doc = textBox.getStyledDocument();
			doc.addDocumentListener(new DocumentListener() {
				private void clearStyle(final DocumentEvent e) {
					SwingUtilities.invokeLater(() -> doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true));		
				}
				       
				@Override
				public void insertUpdate(final DocumentEvent e) {
					clearStyle(e);
				}
				
				@Override
				public void removeUpdate(final DocumentEvent e) {
				    clearStyle(e);
				}
				
				@Override
				public void changedUpdate(final DocumentEvent e) {
				    //Skip but still update...
				}
			});
			
			final SimpleAttributeSet sas = new SimpleAttributeSet();
			StyleConstants.setUnderline(sas, true);
			StyleConstants.setBackground(sas, Color.red);
			final int start = textBox.getSelectionStart();
			final int end = textBox.getSelectionEnd();
			doc.setCharacterAttributes(start, end - start, sas, true);
			
			int index = 0;
			String at = "";
			String word = "";
			while(index < doc.getLength()) {
				try {
					at = doc.getText(index, 1);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				
				if(isPunctuation(at)) { 
					if(isPunctuation(word)){
						word = "";
						}
					else {
						checkWord(check, word, list, temp, index, doc, sas, at);
						word = "";
					}
				}
				else if(index == doc.getLength()-1) {
					if(at.equals("\n")) {
						checkWord(check, word, list, temp, index, doc, sas, at);
						word = "";
					}
					else {
						try {
							checkWord(check, word + doc.getText(index, 1), list, temp, index, doc, sas, at);//using to get last character...
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				}
				else if(at.equals("\n")) {
					if(index == doc.getLength()-1) {
						checkWord(check, word, list, temp, index, doc, sas, at);
					}
					else if(word != "") {
						checkWord(check, word, list, temp, index, doc, sas, at);
					}
					else {//if it's a empty string...
						//Skip
					}
					
					word = "";
				}
				else if(at.equals(" ")) {
					if(word.equals("")) {
						//Skip
					}
					else {
						checkWord(check, word, list, temp, index, doc, sas, at);
					}
					//then reset word variable
					word = "";
				}
				else {
					word += at;
				}
				index++;
			}
			
			textBox.addMouseListener(new MouseAdapter() {
				private boolean pendingPopUp = false;
	                
				private void maybePop(final MouseEvent mevt) {
                    if (mevt.isPopupTrigger()) {
                        if (pendingPopUp)
                            System.err.println("A popup is already popped. Close it to pop a new one.");
                        else
                            pop(mevt);
                    }
                }
                
                @Override
                public void mouseClicked(final MouseEvent mevt) {
                    maybePop(mevt);
                }
                
                @Override
                public void mousePressed(final MouseEvent mevt) {
                    maybePop(mevt);
                }
                
                @Override
                public void mouseReleased(final MouseEvent mevt) {
                    maybePop(mevt);
                }
                
	        	private void pop(final MouseEvent mevt) {
	        		if (SwingUtilities.isRightMouseButton(mevt)) {
	        			try {
	        				final StyledDocument doc = textBox.getStyledDocument();
	                        final int offset = textBox.viewToModel(mevt.getPoint());
	                         
	                        //Find what word is at the location of the document where the user clicked:
	                        final int start = Utilities.getWordStart(textBox, offset),
	                        end = Utilities.getWordEnd(textBox, offset);
                            
                            //Set the selection to be that word:
                            textBox.setSelectionStart(start);
                            textBox.setSelectionEnd(end);
                            
                            final String Atword = doc.getText(start, end - start);
                            
                            addWordButton(check, Atword, list, temp);

                            //Create the popup:
                            final JPanel popupPanel = new JPanel();

                            //Create the words
                            final int cnt = list.size();
                            final ArrayList<JButton> words = new ArrayList<>();
                            for (String string : list) {
                                final JButton button = new JButton(string);
                                popupPanel.add(button);
                                words.add(button);
                                list.pop();
                            }
                            
                            final JButton addToDictionary = new JButton("Add to Dictionary");
                            popupPanel.add(addToDictionary);
                            
                            final JButton ignore = new JButton("Ignore");
                            popupPanel.add(ignore);
                            
                            final JButton cancel = new JButton("Cancel");
                            popupPanel.add(cancel);

                            final Popup popup = PopupFactory.getSharedInstance().getPopup(textBox, popupPanel, mevt.getXOnScreen(), mevt.getYOnScreen());

                            //Add action listener to the word and cancel
                            words.forEach(button -> button.addActionListener(e -> {
                                try {
                                    final String newWord = ((JButton) e.getSource()).getText();
                                    
                                    doc.remove(start, end - start);
                                    doc.insertString(start, newWord, null);
                                    
                                    textBox.setCaretPosition(start + newWord.length());
                                }
                                catch (final BadLocationException | RuntimeException x) {
                                    JOptionPane.showMessageDialog(textBox, "Oups!");
                                }
                                finally {
                                    popup.hide();
                                    pendingPopUp = false;
                                }
                            }));
                            
                            cancel.addActionListener(e -> {
                                popup.hide();
                                textBox.setSelectionStart(offset);
                                textBox.setSelectionEnd(offset);
                                pendingPopUp = false;
                            });
                            
                            ignore.addActionListener(e -> {
                            	try {
									doc.remove(start, end - start);
									doc.insertString(start, Atword, null);
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
                                popup.hide();
                                textBox.setSelectionStart(offset);
                                textBox.setSelectionEnd(offset);
                                pendingPopUp = false;
                            });
                        
                            addToDictionary.addActionListener(e -> {
                            	check.dictionary.put(Atword, Atword);//Adds word to the dictionary...
                            	try {
									doc.remove(start, end - start);
									doc.insertString(start, Atword, null);
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
                                popup.hide();
                                textBox.setSelectionStart(offset);
                                textBox.setSelectionEnd(offset);
                                pendingPopUp = false;
                            });
                            
                            pendingPopUp = true;
                            popup.show();
                        }
                        catch (final BadLocationException | RuntimeException x) {
                            JOptionPane.showMessageDialog(textBox, "Oups! No word found?...");
                        }
                    }
                }
                
            });
			check.clearCorrections();
			frame.revalidate();
		}
		else if(option.equals("Print")) {
			try {
				textBox.print();
			} catch (PrinterException e1) {
				e1.printStackTrace();
			}
		}
		else if(option.equals("Save")) {	
			JFileChooser fileChooser = new JFileChooser();
			int response = fileChooser.showSaveDialog(null);
			String getFileName = "";
			if(response == JFileChooser.APPROVE_OPTION) {
				getFileName = fileChooser.getSelectedFile().toString();
			}
			else {
				System.out.println("Could not find file");
			}
			//--
			FileWriter outputStream = null;
			try {
				outputStream = new FileWriter(new File(getFileName), false);				
			System.out.println("File found");
			
			String getText = textBox.getText();
			outputStream.write(getText);
			outputStream.close();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(option.equals("Open File...")) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt", "text");
			fileChooser.setFileFilter(filter);
			int response = fileChooser.showOpenDialog(null);
			String getFileName = "";
			if(response == JFileChooser.APPROVE_OPTION) {
				getFileName = fileChooser.getSelectedFile().toString();
			}
			else {
				System.out.println("Could not find file");
			}
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
	
	public static String getPacket() {
		System.out.println(textBox.getText());
		return textBox.getText();
	}
	
	public static void main(String[] args) {
		Text test = new Text();	
	}
	
	
	public static boolean isPunctuation(String ch) {
    	if(ch.equals(".") || ch.equals(",") || ch.equals("!") || ch.equals("?")
    			|| ch.equals(";") || ch.equals(":") || ch.equals("\"")|| ch.equals("'")) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }
    
    public static void checkWord(SpellChecker check, String word, LinkedStack list, LinkedStack temp, int index, StyledDocument doc, SimpleAttributeSet sas, String at) {
    	if(check.getInfo(word.toLowerCase())) {
    		//Spelled correct
    	}
		else {
			if(!at.equals(" ")) {
				if(!at.equals("\n")) {
					index+=1;
				}
			}
			try
			{
				textBox.getHighlighter().addHighlight(index-word.length(), index, red);
			}
			catch(BadLocationException ble) {
				System.out.println(ble.getMessage());
			}
			
			
		}
    }
   
    public static void addWordButton(SpellChecker check, String word,  LinkedStack list, LinkedStack temp) {
    	check.clearCorrections();
		check.getInfo(word);
		
		temp = check.getCorrections();
		
		for (Object string : temp) {
			if(check.getInfo((String)string) ) {
				list.push(string);
			}
			else {
				continue;
			}
			temp.pop();
		}
    }
    
}
