//
//  Parser.h
//  Assignment3_403
//
//  Created by Dakota Brown on 2/12/19.
//  Copyright © 2019 Dakota Brown. All rights reserved.
//

#ifndef Parser_h
#define Parser_h

#include "Givens.h"

_Bool parser(struct lexics *someLexics, int numberOfLexics);
void function(void);
void header(void);
void body(void);
void arg_decl(void);
void statement_list(void);
void statement(void);
void while_loop(void);
void return_(void);
void assignment(void);
void expression(void);
void term(void);

_Bool statementFound(void);
void consume(const int TOKEN);

#endif /* Parser_h */
