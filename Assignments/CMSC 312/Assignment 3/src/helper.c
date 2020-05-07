#include "helper.h"

int random_num(int lower, int upper) {
	int num;

	// 3617 is a prime number
	do {
		num = rand() % 3617;
	} while (num < lower || num > upper);

	return num;
}

void shmem_init(void) {
	
	/******************************************************/
	/* Initialize shared memory and assign it to {q}.     */
	/******************************************************/
	key = Ftok("./main.c", 'R');
	shmid = Shmget(key, sizeof(*q), 0644 | IPC_CREAT);
	q = (Queue *) Shmat(shmid, (void *) 0, 0);

	/******************************************************/
	/* Initialize queue variables.                        */
	/******************************************************/
	queue_init();
}

void shmem_destroy(void) {

	/******************************************************/
	/* Detach from shared memory and then mark it for     */
	/* removal.                                           */
	/******************************************************/
	Shmdt(q);
	Shmctl(shmid, IPC_RMID, NULL);
}

void seminit(void) {

	/******************************************************/
	/* Initialize semaphores. Unlink each one IMMEDIATELY */
	/* after opening to prevent them existing in the      */
	/* event of a crash.                                  */
	/******************************************************/
	mutex = Sem_open(MUTEX, O_CREAT | O_EXCL, 0644, 1);
	Sem_unlink(MUTEX);

	slots = Sem_open(SLOTS, O_CREAT | O_EXCL, 0644, MAX_CAPACITY);
	Sem_unlink(SLOTS);

	items = Sem_open(ITEMS, O_CREAT | O_EXCL, 0644, 0);
	Sem_unlink(ITEMS);
}

void semdestroy(void) {

	/******************************************************/
	/* Close all semaphores.                              */
	/******************************************************/
	Sem_close(mutex);
	Sem_close(items);
	Sem_close(slots);
}


void sigint_handler(int sig) {

	/******************************************************/
	/* Kill all the producers and then reap them. We      */
	/* don't have to kill the threads because when main   */
	/* thread exits, they will die anyway.                */
	/******************************************************/
	if (getpid() == ppid) {
		kill(0, SIGUSR1);
		while ((waitpid (-1, NULL, 0))) ;
	}

	/******************************************************/
	/* Cleanup the shared memory and named semaphores.    */
	/******************************************************/
	shmem_destroy();
	semdestroy();

	exit(0);
}

void sigusr1_handler(int sig) {

	exit(0);
}

void producer(void) {
	/******************************************************/
	/* Change the seed for the PRNG, otherwise each       */
	/* process will be using the same seed and numbers    */
	/* won't be random.                                   */
	/******************************************************/
	srand((unsigned int) (time(0) ^ (getpid()<<16)));
	signal(SIGUSR1, sigusr1_handler);
	int i, process_id, job_size, jobs;
	struct timespec time;

	/******************************************************/
	/* Set time to sleep to 1/10th a sec multiplied by    */
	/* the first digit of the job size, unless the size   */
	/* 1000. Then sleep for 1s.                           */
	/******************************************************/
	time.tv_sec = 0;
	time.tv_nsec = 500000000L;

	jobs = random_num(1, 30);
	for (i = 0; i < jobs; i++) {

		P(slots);
		P(mutex);

		Node* node = (Node *) malloc(sizeof(Node));
		process_id = getpid();
		job_size = random_num(100, 1000);

		node_init(node, process_id, job_size);
		enqueue(node);
		free(node);

		V(mutex);
		V(items);

		printf("Process %d added %d to queue\n", process_id, job_size);
		nanosleep(&time, NULL);
	}
}

void* consumer(void* thread_n) {
	struct timespec time;
	int thread_num = *((int *) (thread_n));
	int process_id, job_size;

	/******************************************************/
	/* If producers aren't done (i.e. forked processes    */
	/* haven't exited), then we don't care about {size}.  */
	/* Once {producers_done} has been set, then the       */
	/* consumers need to continue consuming until {size}  */
	/* is zero.                                           */
	/******************************************************/
	while (!producers_done || size) {
		sem_trywait(items);
		if (errno == EAGAIN) {
			/******************************************************/
			/* Semaphore {items} was already zero. Reset errno    */
			/* and loop again to be sure {size} isn't zero        */
			/******************************************************/
			errno = 0;
			continue;
		}
		P(mutex);

		Node* node = dequeue();
		size = q->size;
		job_size = node->job_size;
		process_id = node->process_id;
		free(node);

		V(mutex);
		V(slots);

		printf("Consumer %d dequeue %d, %d from queue\n",
				thread_num,
				process_id,
				job_size);

		/******************************************************/
		/* Set time to sleep to 1/10th a sec multiplied by    */
		/* the first digit of the job size, unless the size   */
		/* is 1000. Then sleep for 1 second.                  */
		/******************************************************/
		job_size = job_size / 100;
		time.tv_sec = (time_t) (job_size < 10 ? 0 : 1);
		time.tv_nsec = (long) (job_size < 10 ? job_size * 100000000L : 0);

		nanosleep(&time, NULL);
	}

	pthread_exit(0);
}