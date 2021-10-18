import java.io.*;
import java.util.*;

public class spelling{

	/* Normally, it is bad practice to have the main function throw an Exception,
	 *   but because this is a programming competition and you want to complete
	 *   the problem as fast as possible, having main throw an Exception is 
	 *   a good idea.  If you didn't, you would have to put try/catch clauses
	 *   around all of the file I/O statements.
	 **/
	public static void main(String[] args) throws Exception{
	
		BufferedReader br = new BufferedReader(new FileReader("spelling.in"));
		StringTokenizer tokenizer;
		String DELIMS = " \t.,?!";
		int n, k;
		String[] dictionary;
		int[][] dictionaryLetterCount;
		int[] tokenLetterCount;
		String fixedSentence, token;
		boolean tokenInDictionary, tokenIsWordi;
		
		/* I call the trim() function just in case the judges are mean
		 *   and put spaces before or after the number
		 * If the judges did put spaces on the first line,
		 *   the Integer.parseInt() function would throw an exception
		 **/
		n = Integer.parseInt(br.readLine().trim());
		
		/* Create an array of Strings that will contain all of the words
		 *   in our dictionary.
		 **/
		dictionary = new String[n];
		
		/* To determine whether or not a word in the sentence and a word
		 *   in the dictionary are equal, we are going to count the 
		 *   occurance of each letter in the strings and see if those
		 *   counts are equal.  Therefore, each word in the dictionary
		 *   will have a corresponding array of 26 integers.
		 *
		 * Note: When we count the number of letters in a string, we do
		 *   not count the first and last letters as these are never
		 *   permuted.
		 *
		 * Here we initialize this dictionaryLetterCount, read in all of
		 *   the dictionary words, and obtain their letter counts.
		 **/
		dictionaryLetterCount = new int[n][];
		for(int i = 0; i < n; i++){
		
			dictionary[i] = br.readLine().trim();

			dictionaryLetterCount[i] = getLetterCount(dictionary[i]);

		}
		
		/* Read in the number of sentences to process
		 **/
		k = Integer.parseInt(br.readLine().trim());
		for(int m = 1; m <= k; m++){
		
			/* Process a sentence.
			 * The StringTokenizer constructor below takes in the sentence,
			 *   the delimeters used to seperate tokens, and a boolean value
			 *   indicating whether or not the delimeter characters are returned
			 *   as tokens.  Because we want to print out the delimeters, we pass
			 *   true as the third parameter.
			 **/
			tokenizer = new StringTokenizer(br.readLine(), DELIMS, true);
			
			/* This will be the sentence that we print out when we are done
			 *   processing the jumbled sentence.
			 **/
			fixedSentence = "";
			
			/* Process every token in the tokenizer.
			 **/
			while(tokenizer.hasMoreTokens()){
			
				/* Get one of the tokens.
				 **/
				token = tokenizer.nextToken();
				
				/* In the simplest case, the current token is a delimeter.
				 * We simply append the token to the end of our fixedSentence.
				 **/
				if(DELIMS.indexOf(token) >= 0){
				
					fixedSentence += token;
				
				/* If the token is not a simple delimeter then it could be a word
				 *   from the dictionary.  Here is where we search through the 
				 *   dictionary to determine whether or not it is a valid word.
				 **/
				}else{
				
					/* Obtain the 26 int letter count of the current word.
					 * We will use this to test for equality when searching
					 *   through the dictionary.
					 **/
					tokenLetterCount = getLetterCount(token);
					
					/* We initially set the tokenInDictionary boolean value to false,
					 *   and loop through all of the words in the dictionary to 
					 *   determine if the current word in the sentence is indeed in
					 *   the dictionary.  If it is, then tokenInDictionary will be
					 *   set to true.
					 **/
					tokenInDictionary = false;
					for(int i = 0; i < n; i++){

						/* This boolean value indicates whether or not
						 *   the word at dictionary[i] is equal to the
						 *   current word we are processing.
						 **/
						tokenIsWordi = true;
						
						/* We know the words are not equal if their lengths
						 *   are not equal.
						 **/
						if(dictionary[i].length() != token.length()){
						
							tokenIsWordi = false;

						/* If the word contains three of fewer characters, we
						 *   can simply check for String equality because words
						 *   of length 3 or less cannot be jumbled.
						 * Note: When checking for equality make sure to use
						 *   equalsIgnoreCase because the words in the sentence
						 *   can be capitalized, yet the words in the dictionary
						 *   are not.
						 **/
						}else if(dictionary[i].length() <= 3){
						
							if(!dictionary[i].equalsIgnoreCase(token)){
								tokenIsWordi = false;
							}
						
						/* The word at dictionary[i] and the current word in the sentence
						 *   both have the same length and the length is greater than 3
						 *   characters.  Now we check to see if their letter counts,
						 *   first letter and last letter are equal.  If all three of
						 *   these are true, then the dictionary[i] word and the current
						 *   word in the sentence are equal.
						 **/
						}else{
						
							/* Check to make sure the letter counts are equal.
							 **/
							for(int j = 0; j < 26; j++){
								if(dictionaryLetterCount[i][j] != tokenLetterCount[j]){
									tokenIsWordi = false;
									break;
								}
							}
							
							/* Make sure the first and last characters of the words are equal.
							 **/
							if(token.toLowerCase().charAt(0) != dictionary[i].charAt(0)){
								tokenIsWordi = false;
							}
							if(token.charAt(token.length()-1) != dictionary[i].charAt(dictionary[i].length()-1)){
								tokenIsWordi = false;
							}
							
						}

						/* If we have found a match, then append the dictionary string to the
						 *   fixed sentence and break out of the dictionary loop.
						 * Also make sure to set tokenInDictionary to true.
						 *
						 * Note: We take the first character from the word in the sentence because
						 *   it could be capitalized.
						 **/
						if(tokenIsWordi){
							tokenInDictionary = true;
							fixedSentence += token.charAt(0)+dictionary[i].substring(1);
							break;
						}
						
					}
					
					/* If the current word is not in the dictionary then the sentence is
					 *   invalid, so we set the fixedSentence equal to the error message
					 *   and stop processing the sentence.
					 **/
					if(!tokenInDictionary){
						fixedSentence = "Invalid message.";
						break;
					}
					
				}
				
			}
			
			/* Simply print the output.
			 **/
			System.out.println("Message " + m + ": " + fixedSentence);
			System.out.println();
			
		}

	}

	/* Returns an 26 element int array containing the letter count
	 *   of the word.
	 * Note: does not count the first and last characters of the word.
	 **/
	private static int[] getLetterCount(String word){
	
		int[] letterCount = new int[26];
		String lowerCaseWord = word.toLowerCase();
		
		Arrays.fill(letterCount, 0);
		
		for(int i = 1; i < lowerCaseWord.length()-1; i++){
			letterCount[lowerCaseWord.charAt(i) - 'a']++;
		}
		
		return letterCount;
		
	}
		
}