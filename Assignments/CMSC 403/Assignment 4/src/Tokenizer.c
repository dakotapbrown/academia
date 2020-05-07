//
//  Tokenizer.c
//  Assignment 4 - CMSC 403
//
//  Last edited by Dakota Brown on 10/05/19.
//  Copyright © 2019 Dakota Brown. All rights reserved.
//

#include <ctype.h>
#include "Tokenizer.h"

_Bool lexErr = FALSE;

_Bool validVarType(char* aLexeme){
    regex_t identifierRegex;
    regcomp(&identifierRegex, "^int$|^void$", REG_EXTENDED);
    return !regexec(&identifierRegex, aLexeme, 0, 0, 0);
}

_Bool validBinOp(char* aLexeme){
    regex_t identifierRegex;
    regcomp(&identifierRegex, "[+]|[*]|!=|==|%", REG_EXTENDED);
    return !regexec(&identifierRegex, aLexeme, 0, 0, 0);
}

_Bool tokenizer(struct lexics *aLex, int *numLex, FILE *inf){
    char lex[LEXEME_MAX];   // Stores alphanumeric char that have been read
    char buf[MY_CHAR_MAX];  // Stores (optomistically) each line of file; max 256 char
    int j = -1;     // Keeps tabs of where in 'lex' last char has been stored
    
    while(fgets(buf, MY_CHAR_MAX, inf) != NULL) {
        // Traverse 'buf' to read char at a time
        int i = 0;
        while (i < MY_CHAR_MAX && buf[i] != '\0') {     // Quit parsing line if null terminator or end of 'buf'
            
            if (isalnum(buf[i])) {
                // Add char to 'lex' then increment 'buf' index
                lex[++j] = buf[i++];
            } else if (isspace(buf[i])) {
                // Tokenize anything in 'lex' buffer encoutered before now
                tokLexHelper(lex, j+1, aLex, numLex, &j);
                
                if (buf[i] == '\n') {
                    // If the space was '\n', skip to reading next line
                    break;
                }
                i++;
            } else {
                // Tokenize anything in 'lex' buffer encoutered before now
                tokLexHelper(lex, j+1, aLex, numLex, &j);
                
                // Determine what non-alphanumeric char was encountered
                switch(buf[i]) {
                        
                    case '!':   // if '!', check next char
                        if (buf[i+1] == '=') {
                            // Tokenize as BINOP and 'consume' the equals
                            tokCharHelper(aLex, numLex, BINOP, "!=");
                            i++;
                        } else {
                            // Lexical error; quit!
                            return FALSE;
                        }
                        break;
                        
                    case '=':   // Could be '==', check next char
                        if (buf[i+1] == '=') {
                            // Tokenize as BINOP and 'consume' the equals
                            tokCharHelper(aLex, numLex, BINOP, "==");
                            i++;
                        } else {
                            // Tokenize as EQUAL
                            tokCharHelper(aLex, numLex, EQUAL, "=");
                        }
                        break;
                        
                        // Tokenize the character if it matches our lexical structure
                    case '(':
                        tokCharHelper(aLex, numLex, LEFT_PARENTHESIS, "(");
                        break;
                        
                    case ')':
                        tokCharHelper(aLex, numLex, RIGHT_PARENTHESIS, ")");
                        break;
                        
                    case '{':
                        tokCharHelper(aLex, numLex, LEFT_BRACKET, "{");
                        break;
                        
                    case '}':
                        tokCharHelper(aLex, numLex, RIGHT_BRACKET, "}");
                        break;
                        
                    case ';':
                        tokCharHelper(aLex, numLex, EOL, ";");
                        break;
                        
                    case ',':
                        tokCharHelper(aLex, numLex, COMMA, ",");
                        break;
                        
                    case '%':
                        tokCharHelper(aLex, numLex, BINOP, "%");
                        break;
                }
                
                i++;    // 'Consume' last character read
            }
        }
    }
    
    return !lexErr;
}

void tokLexHelper(char* lexeme, int len, struct lexics *aLex, int *numLex, int *loc) {
    if (*loc >= 0) {    // Integrity check
        tokenize(lexeme, *loc+1, aLex, numLex);
        
        // Reset index for 'lex' buffer in 'tokenizer' method
        *loc = -1;
    }
}

void tokCharHelper(struct lexics *aLex, int *numLex, const int TOKEN, char* lexeme) {
    
    struct lexics tmp = {TOKEN, ""};
    strcpy(tmp.lexeme, lexeme);
    aLex[(*numLex)++] = tmp;
}

void tokenize(char* lexeme, int len, struct lexics *aLex, int *numLex) {
    struct lexics lex = {-1, ""};
    lexeme[len] = '\0'; // Must terminate strings for regex mathcing
    
    if (strcmp(lexeme, "return") == 0) {
        lex.token = RETURN_KEYWORD;
    } else if (strcmp(lexeme, "while") == 0) {
        lex.token = WHILE_KEYWORD;
    } else if (validNumber(lexeme)) {
        lex.token = NUMBER;
    } else if (validBinOp(lexeme)) {
        lex.token = BINOP;
    } else if (validVarType(lexeme)) {
        lex.token = VARTYPE;
    } else if (validIdentifier(lexeme)) {
        lex.token = IDENTIFIER;
    } else {
        // Lexical error encountered; 'lexeme' is alphanumeric, but doesn't
        // fit our defined lexical structure
        
        lexErr = TRUE;
        return;
    }
    
    
    strncpy(lex.lexeme, lexeme, len);
    aLex[(*numLex)++] = lex;    // Add 'lexic' struct to 'aLex' array
}
