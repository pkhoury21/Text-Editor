import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.StdIn;

public class SpellChecker {
	
	SeparateChainingHashST<String, String> dictionary;
	LinkedStack<String> possibleWords = new LinkedStack<String>();//stringList that holds possible words
	
	
	public boolean getInfo(String word) {//method for TextEditor class...
		if (!checkWord(word)) {
			removeFirstCharacter(word, possibleWords);
			 removeLastCharacter(word, possibleWords);
			 addCharacterToStart(word, possibleWords);
			 addCharacterToEnd(word, possibleWords);
			 swapAdjacentCharacters(word, possibleWords);
			 return false;
			 }
		else {//if the word is spelled correct...
			//System.out.println("no mistakes found");
			return true;
		}
		//return possibleWords;
	}
	
	public LinkedStack getCorrections() {//Method for TextEditor class...
		return possibleWords;
	}
	
	public void clearCorrections() {//Method for TextEditor class...
		for (String string : possibleWords) {
			possibleWords.pop();
		}
	}
	
	
	
	private static void initDictionary(SeparateChainingHashST<String, String> dictionary, String fileName) {
		In input = new In(fileName);
	
		 while (!input.isEmpty()) {
             String s = input.readLine();
         	/*
     		 * TODO: Remove the following line and change it to add an entry into the "dictionary"
     		 * hash table.
     		 */
             //System.out.println(s);
             dictionary.put(s, s);//puts key-value of correctly spelled word in Hash Table
         }
	}
	
	public SpellChecker(String fileName) {
		dictionary = new SeparateChainingHashST<String, String>();
		initDictionary(dictionary, fileName);
	}
	
	public boolean checkWord(String word) {//just simple method that calls contains()	
		return dictionary.contains(word);
	}
	
	public static void removeFirstCharacter(String value, LinkedStack<String> possibleWords) {//method to remove first char and add to stack...	
		possibleWords.push(value.substring(1, value.length()));
	}
	
	public static void removeLastCharacter(String value, LinkedStack<String> possibleWords) {//method to remove last char and add to stack...	
		possibleWords.push(value.substring(0, value.length()-1));
	}
	
	public static void addCharacterToStart(String value, LinkedStack<String> possibleWords) {//method that loops through alphabet upper and lower case and adds that char to the begginign of the string then to the stack...	
		String temp;
		for(char alphabet = 'a'; alphabet <='z'; alphabet++ ) {
			temp = alphabet+value;
			//System.out.println("Temp is: " + temp);
			possibleWords.push(temp);
	    }
		for(char alphabet = 'A'; alphabet <='Z'; alphabet++ ) {
			temp = alphabet+value;
			possibleWords.push(temp);
	    }
	}
	
	public static void addCharacterToEnd(String value, LinkedStack<String> possibleWords) {//method that loops through alphabet upper and lower case and adds that char to the end of the string then to the stack...	
		String temp;
		for(char alphabet = 'a'; alphabet <='z'; alphabet++ ) {
			temp = value+alphabet;
			//System.out.println("Temp is: " + temp);
			possibleWords.push(temp);
	    }
		for(char alphabet = 'A'; alphabet <='Z'; alphabet++ ) {
			temp = value+alphabet;
			possibleWords.push(temp);
	    }
	}
	
	public static void swapAdjacentCharacters(String value, LinkedStack<String> possibleWords) {//goes through for loop, puts string in char array and starts to swap adjacent chars starting from beginning to end...	
		String temp;
		char current;
		char next;
		char [] letters;
		for (int i = 0; i < value.length(); i++) {
			if(i >= value.length()-1) {//can change this to loop instead but easier for reading here, stops so it doesn't go over the string length...
				break;
			}
			else {//else it goes from the char it's at to the one in-front and swaps, then adds to stack...
				letters = value.toCharArray();
				next = value.charAt(i+1);
				current = value.charAt(i);
				letters[i] = next;
				letters[i+1] = current;
				
				temp = "";
				for (int j = 0; j < letters.length; j++) {
					temp += letters[j];
				}
				possibleWords.push(temp);
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		 SpellChecker spellChecker = new SpellChecker("dictionary.txt");
		 
		 System.out.println("Enter your word...");
		 while(!StdIn.isEmpty()) {//Control+Z to terminate on eclipse...
			 String value  = StdIn.readString();
			 System.out.println("Value is: " + value);
			 
			 if (!spellChecker.checkWord(value)) {//if not in Hash Table then start to make the string adjustments for the possible string
				 System.out.println("Wrong spelling, did you mean?...");
				 removeFirstCharacter(value, spellChecker.possibleWords);
				 removeLastCharacter(value, spellChecker.possibleWords);
				 addCharacterToStart(value, spellChecker.possibleWords);
				 addCharacterToEnd(value, spellChecker.possibleWords);
				 swapAdjacentCharacters(value, spellChecker.possibleWords);
				 
				 for (String possibilities : spellChecker.possibleWords) {//for each loop to go through stack...
					if(spellChecker.checkWord(possibilities)) {
						System.out.println("GOOD: " + possibilities);
					}
					spellChecker.possibleWords.pop();//to clear the stack since the iteration doesn't...
				 }
			 }
			 else {//if the word is spelled correct...
				 System.out.println("no mistakes found");
			 }
			 
		 }
		 System.out.println("Finished...");
}
