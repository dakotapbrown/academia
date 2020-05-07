#ifndef FILE_ERROR_SEEN
#define FILE_ERROR_SEEN

#include <errno.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>


void unix_error(char* msg); /* Unix-style error */
void posix_error(int code, char* msg); /* POSIX-style error */
// void gai_erro(int code, char* msg); /* getaddrinfo-style error */
void app_error(char* msg);/* Application error */

#endif /* FILE_ERROR_SEEN */