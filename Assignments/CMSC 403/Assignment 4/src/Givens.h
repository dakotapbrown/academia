#ifndef GIVENS_H
#define GIVENS_H

#include <regex.h>

#define FALSE 0
#define TRUE !FALSE


#define LEFT_PARENTHESIS 3 
#define RIGHT_PARENTHESIS 4
#define LEFT_BRACKET 8
#define RIGHT_BRACKET 9
#define WHILE_KEYWORD 12
#define RETURN_KEYWORD 13
#define EQUAL 1
#define COMMA 2
#define EOL 7
#define VARTYPE 17
#define IDENTIFIER 55
#define BINOP 22
#define NUMBER 51

#define LEXEME_MAX 256
#define MY_CHAR_MAX 256

struct lexics{
	int token;
	char lexeme[LEXEME_MAX];
};
/*Spring 2019 Legecy pls remove 
_Bool vaildNumber(char* aLexeme);
*/

_Bool validNumber(char* aLexeme);
_Bool validIdentifier(char * aLexeme);

//Stuck on file I/O? Research fgets! 
//fgets(input, MY_CHAR_MAX, inf) != NULL

#endif