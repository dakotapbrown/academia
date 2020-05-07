#ifndef FILE_HELPER_SEEN
#define FILE_HELPER_SEEN

#include <signal.h>

#include "queue.h"
#include "wrapper.h"

#define MUTEX "..mutex"		
#define SLOTS "..slots"		
#define ITEMS "..items"

static sigset_t signal_mask;

int producers;		// number of producer processes to fork
int consumers;		// number of consumer threads to create
int size;			// queue size that only consumers update after each dequeue
int producers_done;	// flag for signaling consumers that producers are done

key_t key;			// key for shared memory creation
int shmid;			// shared memory id
pid_t ppid;			// place holder for parent process/main thread id

sem_t* mutex;		// binary semaphore for mutual exclusion
sem_t* slots;		// number of available slots in queue
sem_t* items;		// number of items in queue


int random_num(int lower, int upper);

void shmem_init(void);
void shmem_destroy(void);

void seminit(void);
void semdestroy(void);

void sigint_handler(int sig);

void producer(void);
void* consumer(void* thread_n);

#endif /* !FILE_HELPER_SEEN */