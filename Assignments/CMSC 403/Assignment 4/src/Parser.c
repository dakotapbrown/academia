//
//  Parser.c
//  Assignment 4 - CMSC 403
//
//  Last edited by Dakota Brown on 10/05/19.
//  Copyright © 2019 Dakota Brown. All rights reserved.
//

//-----Includes-----//

#include <stdlib.h>
#include "Parser.h"

//-----Includes-----//


//-----Defines-----//

#define isempty (globalCount == 0)

//-----Defines-----//


//-----Globals-----//

struct lexics *globalLex;
int globalCount;
int loc;
_Bool syntaxErr = FALSE;

//-----Globals-----//


/*-------------------------Methods-------------------------*/

_Bool parser(struct lexics *someLexics, int numberOfLexics){
    if (numberOfLexics > 0) {   // Integrity check
        // Assign to globals
        globalLex = someLexics;
        globalCount = numberOfLexics;
        
        // Index of what token is being read
        loc = 0;
        
        function();
    }
    
    return !syntaxErr;
}

void function() {
    header();
    body();
}

void header() {
    consume(VARTYPE);
    consume(IDENTIFIER);
    consume(LEFT_PARENTHESIS);
    if (!syntaxErr && globalLex[loc].token == VARTYPE) {
        arg_decl();
    }
    consume(RIGHT_PARENTHESIS);
}

void body() {
    consume(LEFT_BRACKET);
    if (!syntaxErr && globalLex[loc].token != RIGHT_BRACKET) {
        statement_list();
    }
    consume(RIGHT_BRACKET);
}

void arg_decl() {
    consume(VARTYPE);
    consume(IDENTIFIER);
    while (!syntaxErr && globalLex[loc].token == COMMA) {
        consume(COMMA);
        consume(VARTYPE);
        consume(IDENTIFIER);
    }
}

void statement_list() {
    do {
        statement();
    } while(statementFound());
}

void statement() {
    if (!syntaxErr) {
        if (globalLex[loc].token == WHILE_KEYWORD) {
            while_loop();
        } else if (globalLex[loc].token == RETURN_KEYWORD) {
            return_();
        } else if (globalLex[loc].token == IDENTIFIER) {
            assignment();
        } else if (globalLex[loc].token == LEFT_BRACKET) {
            body();
        } else {
            syntaxErr = TRUE;
        }
    } else {
        syntaxErr = TRUE;
    }
}

void while_loop() {
    consume(WHILE_KEYWORD);
    consume(LEFT_PARENTHESIS);
    expression();
    consume(RIGHT_PARENTHESIS);
    statement();
}

void return_() {
    consume(RETURN_KEYWORD);
    expression();
    consume(EOL);
}

void assignment() {
    consume(IDENTIFIER);
    consume(EQUAL);
    expression();
    consume(EOL);
}

void expression() {
    term();
    if (!syntaxErr && globalLex[loc].token == BINOP) {
        consume(BINOP);
        term();
    } else if (!syntaxErr && globalLex[loc].token == LEFT_PARENTHESIS) {
        consume(LEFT_PARENTHESIS);
        expression();
        consume(RIGHT_PARENTHESIS);
    }
}

void term() {
    if (!syntaxErr && globalLex[loc].token == IDENTIFIER) {
        consume(IDENTIFIER);
    } else if (!syntaxErr && globalLex[loc].token == NUMBER) {
        consume(NUMBER);
    }
}

void consume(const int TOKEN) {
    if (globalLex[loc].token != TOKEN) {
        // you done fucked up
        syntaxErr = TRUE;
    } else if (isempty) {
        // you REALLY fucked up
        syntaxErr = TRUE;
    } else {
        globalCount--;
        loc++;
    }
}

_Bool statementFound(void) {
    if (!syntaxErr && (globalLex[loc].token == WHILE_KEYWORD
                     || globalLex[loc].token == RETURN_KEYWORD
                     || globalLex[loc].token == IDENTIFIER
                     || globalLex[loc].token == LEFT_BRACKET)) {
        return TRUE;
    }
    return FALSE;
}

/*-------------------------Methods-------------------------*/
