#include <sys/times.h>

#include "error.h"
#include "helper.h"
#include "queue.h"
#include "wrapper.h"

clock_t start, stop;
double used;

int main(int argc, char* argv[]) {
	/******************************************************/
	/* Set signal handler immediately to prevent memory   */
	/* leaks from occuring in the event of a CTRL+C.      */
	/******************************************************/
	signal(SIGINT, sigint_handler);

	/******************************************************/
	/*********************** INIT *************************/
	/******************************************************/
	srand((unsigned int) (time(0)));
	ppid = getpid();
	seminit();
	shmem_init();
	producers = atoi(argv[1]);
	consumers = atoi(argv[2]);
	pthread_t thread[consumers];
	int thread_num[consumers];
	producers_done = 0;

	/******************************************************/
	/* Start time count. 								  */
	/******************************************************/
	start = clock();

	/******************************************************/
	/* Fork producer processes.                           */
	/******************************************************/
    int i = 0;
	pid_t pid;
	do {
		pid = Fork();
		i++;
	} while (i < producers && pid > 0);	

	/******************************************************/
    /******************   CHILD PROCESS   *****************/
    /******************************************************/
	if (pid == 0) { /* start producer process */
		producer();
	}

	/******************************************************/
    /******************   PARENT PROCESS   ****************/
    /******************************************************/
	if (pid > 0) { /* create consumer threads */
		for (i = 0; i < consumers; i++) {
			thread_num[i] = i;
			Pthread_create(&thread[i], NULL, consumer, &thread_num[i]);
		}
	}

	/******************************************************/
	/* Wait to reap all child processes                   */
	/******************************************************/
	if (getpid() == ppid) {
		while ((pid = waitpid(-1, NULL, 0))) {
            if (errno == ECHILD) {
                break;
            }
        }
	}

	/******************************************************/
	/* Signal the consumer threads so that they know when */
	/* the sem {items} is next zero, they can exit.       */
	/******************************************************/
	if (getpid() == ppid) {
		P(mutex);
	    producers_done = 1;
	    V(mutex);
	}

	/******************************************************/
	/* All child processes have exited, wait for consumer */
	/* threads to finish.                                 */
	/******************************************************/
	if (getpid() == ppid) {
		for (i = 0; i < consumers; i++) {
			Pthread_join(thread[i], NULL);
		}
	}

	/******************************************************/
	/* Stop time count 							     	  */
	/******************************************************/ 
	stop = clock();

	/******************************************************/
	/********************** CLEANUP ***********************/
	/******************************************************/
	if (getpid() == ppid) {
		shmem_destroy();
		semdestroy();
		
		used = (double) (stop - start) / CLOCKS_PER_SEC;
		printf ("\nElapsed time:   %10.2f \n", used);
	}

	return 0;
}
