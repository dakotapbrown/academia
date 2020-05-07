//
//  Tokenizer.h
//  Assignment 3 - CMSC 403
//
//  Created by Dakota Brown on 2/12/19.
//  Copyright © 2019 Dakota Brown. All rights reserved.
//

#ifndef Tokenizer_h
#define Tokenizer_h

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Givens.h"

_Bool tokenizer(struct lexics *aLex, int *numLex, FILE *inf);
void tokenize(char* lexeme, int len, struct lexics *aLex, int *numLex);
void tokLexHelper(char* lexeme, int len, struct lexics *aLex, int *numLex, int *loc);
void tokCharHelper(struct lexics *aLex, int *numLex, const int TOKEN, char* lexeme);

#endif /* Tokenizer_h */
