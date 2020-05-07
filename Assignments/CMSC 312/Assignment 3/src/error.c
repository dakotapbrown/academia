#include "error.h"

void unix_error(char* msg) { /* Unix-style error */
	fprintf(stderr, "%s: %s\n", msg, strerror(errno));
	exit(0);
}

void posix_error(int code, char* msg) { /* POSIX-style error */
	fprintf(stderr, "%s: %s\n", msg, strerror(code));
	exit(0);
}

// void gai_error(int code, char* msg) { /* getaddrinfo-style error */
// 		fprintf(stderr, "%s: %s\n", msg, gai_strerr(code));
// 		exit(0);
// }

void app_error(char* msg) { /* Application error */
	fprintf(stderr, "%s\n", msg);
	exit(0);
}