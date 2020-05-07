#include "Givens.h"

/*Spring 2019 Legecy pls remove
_Bool vaildNumber(char* aLexeme){
	regex_t numberRegex;
	regcomp(&numberRegex, "^[0-9][0-9]*$", REG_EXTENDED);
	return !regexec(&numberRegex, aLexeme, 0, 0, 0);	
}
*/

_Bool validNumber(char* aLexeme){
	regex_t numberRegex;
	regcomp(&numberRegex, "^[0-9][0-9]*$", REG_EXTENDED);
	return !regexec(&numberRegex, aLexeme, 0, 0, 0);	
}

_Bool validIdentifier(char * aLexeme){
	regex_t identifierRegex;
	regcomp(&identifierRegex, "^[a-zA-Z][a-zA-Z0-9]*$", REG_EXTENDED);
	return !regexec(&identifierRegex, aLexeme, 0, 0, 0);
}