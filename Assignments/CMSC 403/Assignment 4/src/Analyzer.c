#include "Analyzer.h"

int main(){
	//initialize file pointer to NULL 
	FILE *infile = NULL; 
	//Define a character array to store the name of the file to read and write
	char filename[MY_CHAR_MAX];  
	//Prompt the user to input a filename and continue to prompt the user until they enter a correct one
	while(infile == NULL) {  
		printf("Enter filename: ");  
		scanf("%s",filename);
		//When given a filename, use fopen to create a new file pointer. 
			//If fopen can not find the file, it returns null
		infile = fopen(filename, "r+");
		if(infile == NULL){ 
			printf("ERROR: file %s cannot be opened\n", filename);
	  	}
	}


	struct lexics allLexics[1024];
	int numberOfLexics = 0;
	printf("Did tokenize: %d\n",tokenizer(allLexics, &numberOfLexics, infile));
	printf("Number of lexemes: %d\n",numberOfLexics);
	printf("Lexemes: ");
	for(int i = 0; i < numberOfLexics; i++){
		printf("%s  ",allLexics[i].lexeme);
	}
	printf("\n");
	printf("Did parse: %d\n",parser(allLexics, numberOfLexics));

}