// Spelling.cpp by Jason Daly
//
// 19th Annual UCF High School Programming Tournament
//
// Solution for "Purmteed Slleipng", written by Arup Guha
//
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#ifdef WIN32
    #define strcasecmp _stricmp
#endif

// Storage for the dictionary
int dictSize;
char dictionary[10050][25];
char sortedDictionary[10050][25];

// The sentence we're currently working on (split into token words and
// punctuation)
char testSentence[90][90];

// The number of words in the current sentence
int wordCount;

// The index of the current word in the current sentence
int currentWord;



// The basic idea of this solution is that if you sort the middle letters of 
// the dictionary words and the words in the given messages, then each sorted 
// word that is valid permutation of a dictionary word will match the 
// corresponding sorted dictionary word.



// This is a comparison function used for the qsort() in the next function.
int compareLetters(const void *a, const void *b)
{
    char letter1, letter2;

    // Dereference the given pointers to get the letters to compare
    letter1 = *(char *)a;
    letter2 = *(char *)b;

    // Compare the two letters.  Return 1 if they are out of order, -1 if they
    // are in order, and 0 if they're equal.
    if (letter1 > letter2)
        return 1;
    else if (letter1 < letter2)
        return -1;
    else
        return 0;
}

// This function sorts the middle letters of the given word.
void sortMiddleLetters(char *word)
{
    int numPass;
    bool exchanged;

    // Skip the sort if the word has three letters or less (the word is
    // already sorted)
    if (strlen(word) <= 3)
        return;

    // Call the qsort() function to sort the middle letters.  Start with the
    // second letter (word[1]) and go for the length of the word minus two
    // letters (we're skipping the first and the last letter).
    qsort(&word[1], strlen(word) - 2, sizeof(char), compareLetters);
}

// Dictionary checking function.  Returns the index of the word in the
// dictionary if it is found, or -1 if not.  This function asssumes the
// middle letters of the test word are sorted, and checks the given sorted
// word against the sorted dictionary words.
int dictCheck(char *sortedWord)
{
    int i;

    // A more efficient algorithm would sort the dictionary words and use a 
    // binary search for this, but this linear search should be good enough 
    // in this case.  In a contest situation, you don't want to make things
    // more complicated than they need to be.
    for (i = 0; i < dictSize; i++)
    {
        if (strcasecmp(sortedDictionary[i], sortedWord) == 0)
            return i;
    }

    return -1;
}

// Trims the newline character off of the end of a string
void trim(char *string)
{
    char *nl;

    nl = strchr(string, '\n');
    if (nl != NULL)
        *nl = 0;
}

// Returns true if the given character is a letter
bool isLetter(char ch)
{
    if (((ch >= 'a') && (ch <= 'z')) ||
        ((ch >= 'A') && (ch <= 'Z')))
        return true;
    else
        return false;
}

// Returns true if the given character is a valid punctuation mark or a space
bool isSpaceOrPunct(char ch)
{
    if ((ch == '.') || (ch == ',') || (ch == '?') || (ch == '!') || (ch == ' '))
        return true;
    else
        return false;
}

// Tokenizing function.  We can't use strtok() here, because we need to
// handle letters, punctuation and spacing in a special way.  This is why
// text problems can sometimes be the hardest problems to deal with.
void tokenize(char *sentence)
{
    int index;
    int wordIndex;
    bool lastCharWasLetter;

    // Start at the beginning of the sentence
    index = 0;
    wordCount = 0;
    wordIndex = 0;

    // Clear the current sentence array
    memset(testSentence, 0, sizeof(testSentence));

    // Figure out which kind of character we're starting with (should
    // be a letter)
    if (isLetter(sentence[index]))
        lastCharWasLetter = true;
    else
        lastCharWasLetter = false;
   
    // Traverse the sentence, character by character, and organize the
    // characters into words and punctuation, separate, but in order.
    while (index < strlen(sentence))
    {
        // Proceed according to the type of character we saw last time
        if (lastCharWasLetter)
        {
            // Keep reading the current word until we see something
            // other than a letter
            while ((index < strlen(sentence)) && 
                   (isLetter(sentence[index])))
            {
                // Copy the letter to the current word in the
                // tokenized sentence
                testSentence[wordCount][wordIndex] = sentence[index];

                // Increment the word index and the original sentence
                // index
                wordIndex++;
                index++;
            }

            // We've seen something other than a letter, so increment
            // the word count (finish the current word), and start over
            // with the next word
            wordCount++;
            wordIndex = 0;
            
            // Don't bother figuring out what to do next, unless there
            // are more characters to parse.
            if (index < strlen(sentence))
            {
                if (isLetter(sentence[index]))
                    lastCharWasLetter = true;
                else
                    lastCharWasLetter = false;
            }
        }
        else
        {    
            // Keep reading the current word until we see something
            // other than a punctuation mark or space
            while ((index < strlen(sentence)) && 
                   (isSpaceOrPunct(sentence[index])))
            {
                // Copy the letter to the current word in the
                // tokenized sentence
                testSentence[wordCount][wordIndex] = sentence[index];

                // Increment the word index and the original sentence
                // index
                wordIndex++;
                index++;
            }

            // We've seen something other than a punctuation mark or space, so 
            // increment the word count (finish the current word), and start 
            // over with the next word
            wordCount++;
            wordIndex = 0;
        
            // Don't bother figuring out what to do next, unless there
            // are more characters to parse.
            if (index < strlen(sentence))
            {
                if (isLetter(sentence[index]))
                    lastCharWasLetter = true;
                else
                    lastCharWasLetter = false;
            }
        }
    }
}

// Parses the given sentence into token words, and tries to find a match 
// in the dictionary for each token
bool fixSentence(char *sentence)
{
    int i;
    int dictIndex;

    // Split the sentence into token groups of words, space, and punctuation
    tokenize(sentence);

    // For each token, if it is a word, try to rearrange its letters to 
    // match a word in the dictionary
    for (i = 0; i < wordCount; i++)
    {
        // Check the first character of the token to see if it is a letter.
        if (isLetter(testSentence[i][0]))
        {
            // Also check to make sure the word is at least 4 characters long.
            // If not, there's no reason to do a permutation.
            if (strlen(testSentence[i]) >= 4)
            {
                // Sort the middle letters of the test word
                sortMiddleLetters(testSentence[i]);

                // Look up the sorted word in the sorted dictionary
                dictIndex = dictCheck(testSentence[i]);

                // See if the word was found
                if (dictIndex < 0)
                {
                    // Bail out, this is an invalid sentence
                    return false;
                }
                else
                {
                    // Copy the middle letters of the original word into the 
                    // place of the permuted word.
                    memcpy(&testSentence[i][1], &dictionary[dictIndex][1],
                        strlen(testSentence[i]) - 2);
                }
            }
            else
            {
                // Just do a dictionary check on this word directly (no
                // sorting or permutation necessary).  Bail out if it isn't
                // found.
                if (dictCheck(testSentence[i]) < 0)
                    return false;
            }
        }
    }

    // If we got this far without finding an invalid word, return true
    // to indicate success
    return true;
}

int main(void)
{
    FILE *fp;
    char line[80];
    char *nl;
    int i, j;
    int numSentences;

    // Open the input file
    fp = fopen("spelling.in", "r");

    // Get the number of words in the dictionary
    fgets(line, sizeof(line), fp);
    trim(line);
    dictSize = atoi(line);

    // Read in the dictionary words.  Keep the word as it is for the 
    // dictionary, then sort the word, and store the sorted word in a
    // sorted dictionary.  With this, we can use the sorted dictionary to
    // compare with sorted test words.  If there's a match, we can then
    // look up the original word by index in the regular dictionary.
    for (i = 0; i < dictSize; i++)
    {
        fgets(line, sizeof(line), fp);
        trim(line);
        strcpy(dictionary[i], line);
        sortMiddleLetters(line);
        strcpy(sortedDictionary[i], line);
    }

    // Read in the number of sentences to fix
    fgets(line, sizeof(line), fp);
    trim(line);
    numSentences = atoi(line);

    // Read and fix each sentence
    for (i = 0; i < numSentences; i++)
    {
        // Get the sentence to fix
        fgets(line, sizeof(line), fp);
        trim(line);

        // Print the first part of the output
        printf("Message %d: ", i+1);
        
        // Try to fix the sentence
        if (fixSentence(line))
        {
            // Print out the fixed sentence
            for (j = 0; j < wordCount; j++)
                printf("%s", testSentence[j]);
            printf("\n");
        }
        else
        {
            // Print that we couldn't fix this sentence
            printf("Invalid message.\n");
        }

        // Print the extra blank line
        printf("\n");

    }
    
    return 0;
}
