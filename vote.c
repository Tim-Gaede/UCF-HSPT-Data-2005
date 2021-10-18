#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// A structure to keep track of an amendment. It is a node in a
// doubly-linked list.
struct Amendment
{
    int amendmentNumber;
    int numFor;
    int numAgainst;
    struct Amendment *prev;
    struct Amendment *next;
};

struct Amendment* makeNewAmendment(int amendmentNumber, const char *vote);
void printDistrictInfo(struct Amendment *list, int districtNumber);

int main()
{
    FILE *in;
    struct Amendment *amends = NULL;
    struct Amendment *temp = NULL;
    struct Amendment *newAmends = NULL;
    char tempString[50];
    char voteDirection[20];
    int numSets;
    int numVotes;
    int didInsert;
    int tempAmendmentNumber;
    int i, j;
    
    // Open the file for reading, so we can get the input.
    in = fopen("vote.in", "r");
    
    // Read in the number of sets.
    fscanf(in, "%d", &numSets);

    // This is the outer loop to handle individual sets.
    for(i = 0; i < numSets; i++)
    {
        // Read in the number of votes for this district.
        fscanf(in, "%d", &numVotes);
        
        for(j = 0; j < numVotes; j++)
        {
            // Read in the temporary word (should be "Amendment")
            fscanf(in, "%s", tempString);
        
            // Read in the amendment number
            fscanf(in, "%d", &tempAmendmentNumber);

            // Read in the string representing which way this vote goes (for/against)
            fscanf(in, "%s", voteDirection);

            // Look through the list to see if we can find the amendment number. If
            // we don't find it, insert it into the sorted location in the list. The
            // linked list should always be maintained in ascending order by amendment
            // number, because it needs to be printed out in that order.
            temp = amends;
            didInsert = 0;
            while(temp != NULL)
            {
                didInsert = 1;
                // If the value is greater than this nodes' number, then we need
                // to keep moving along the list
                if(tempAmendmentNumber > temp->amendmentNumber)
                {
                    temp = temp->next;
                    continue;
                }
                // Otherwise, see if this node already exists; if it does, simply
                // increment its for/against count.
                if(tempAmendmentNumber == temp->amendmentNumber)
                {
                    // See if the vote is "for" the amendment
                    if(!strcmp(voteDirection, "for"))
                    {
                        temp->numFor++;
                    }
                    else
                    {
                        temp->numAgainst++;
                    }
                    
                    // Don't need to do anything else for this vote...
                    didInsert = 2;
                    break;
                }
                
                didInsert = 2;
                newAmends = makeNewAmendment(tempAmendmentNumber, voteDirection);
                
                newAmends->prev = temp->prev;
                newAmends->next = temp;
                
                // Insert this node into the amendment list.
                if(temp->prev != NULL)
                {
                    temp->prev->next = newAmends;
                }
                else
                {
                    amends = newAmends;
                }
                temp->prev = newAmends;
                break;
            }
            // Check to see if it wasn't inserted... this could either be because
            // this is the first node in the list, or it needs to go as the last
            // node. If didInsert = 1, that means that the list was traversed.
            // If didInsert = 0, that means the list was not traversed and this
            // node should therefore be the only node in the list. If didInsert
            // == 2, that means the node was inserted correctly.
            if(didInsert == 0)
            {
                newAmends = makeNewAmendment(tempAmendmentNumber, voteDirection);
                amends = newAmends;
            }
            else if(didInsert == 1)
            {
                // Traverse the list until we get to the last node
                temp = amends;
                while(temp != NULL && temp->next != NULL)
                    temp = temp->next;
                    
                newAmends = makeNewAmendment(tempAmendmentNumber, voteDirection);
                newAmends->prev = temp;
                temp->next = newAmends;
            }
            
        }
       
        printDistrictInfo(amends, i+1);
        
        // Clear the linked list.
        temp = amends;
        while(amends != NULL)
        {
            amends = temp->next;
            free(temp);
            temp = amends;
        }
    }
    
    fclose(in);
}
        
// A function to make a new node in the amendment linked list.
struct Amendment* makeNewAmendment(int amendmentNumber, const char *vote)
{
    struct Amendment *newAmends;
    
     // Make the new node.
    newAmends = (struct Amendment*)(malloc(sizeof(struct Amendment)));
    newAmends->prev = NULL;
    newAmends->next = NULL;
    newAmends->amendmentNumber = amendmentNumber;
    // See if the vote is "for" the amendment
    if(!strcmp(vote, "for"))
    {
        newAmends->numFor = 1;
        newAmends->numAgainst = 0;
    }
    else
    {
        newAmends->numAgainst = 1;
        newAmends->numFor = 0;
    }
    return newAmends;
}

// A function to print out the information for a district's amendments.
void printDistrictInfo(struct Amendment *list, int districtNumber)
{
    struct Amendment *temp;

    // Done making the list, now all we need to do is print it all out.
    printf("District %d:\n", districtNumber);
    temp = list;
    // Traverse the list
    while(temp != NULL)
    {
        // Print out the vote information
        printf("Amendment %d : %d for, %d against : ", temp->amendmentNumber,
                temp->numFor, temp->numAgainst);
                
        // Decide if the amendment passes or not
        if(temp->numFor > temp->numAgainst)
        {
            printf("YEA.\n");
        }
        else if(temp->numFor < temp->numAgainst)
        {
            printf("NAY.\n");
        }
        else
        {
            printf("UND.\n");
        }
        temp = temp->next;
    }
    printf("\n");
}
        
        
        
