#ifndef FILE_QUEUE_SEEN
#define FILE_QUEUE_SEEN

#include <semaphore.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#include <sys/types.h>
#include <sys/wait.h>
#include <sys/shm.h>

#include "error.h"
#include "wrapper.h"

#define QNULL 9999		// queue null value
#define MAX_CAPACITY 30
#define LENGTH (MAX_CAPACITY + 1)

typedef struct print_request Node;
typedef struct queue Queue;

struct print_request {
	int process_id;
	int job_size;
};

struct queue {
	Node jobs[LENGTH];
	int front;
	int rear;
	int size;
};

Queue* q;

Node* node_init( Node* node, int process_id, int job_size);
void queue_init(void);
void enqueue(Node *node);
Node* dequeue(void);
void printq(void);
void merge(Node arr[], int l, int m, int r);
void mergeSort(Node arr[], int l, int r);
void sort(void);

#endif /* !FILE_QUEUE_SEEN */
