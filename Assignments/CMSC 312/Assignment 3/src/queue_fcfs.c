#include "queue.h"
#include "error.h"
#include "wrapper.h"

void queue_init(void) {
    q->front = 0;
    q->rear = MAX_CAPACITY;
    q->size = 0;

    int i;
    for (i = 0; i < LENGTH; i++) {
        q->jobs[i].process_id = QNULL;
        q->jobs[i].job_size = QNULL;
    }
}

Node* node_init( Node* node, int process_id, int job_size) {

    node->process_id = process_id;
    node->job_size = job_size;

    return node;
}

void enqueue(Node *node) {
	if (q->size < MAX_CAPACITY) {
		q->rear = (q->rear + 1) % LENGTH;

		q->jobs[q->rear].process_id = node->process_id;
		q->jobs[q->rear].job_size = node->job_size;
		q->size++;
	} else {
		app_error("Queue overflow");
		exit(1);
	}
}

Node* dequeue() {
	if (q->size == 0) {
		app_error("Empty queue");
		exit(1);
	} else {
		Node* node = (Node *) malloc(sizeof(Node));

        node->process_id = q->jobs[q->front].process_id;
        node->job_size = q->jobs[q->front].job_size;

		q->jobs[q->front].process_id = QNULL;
		q->jobs[q->front].job_size = QNULL;

		q->front = (q->front + 1) % LENGTH;
		q->size--;

		return node;
	}
}

void printq(void) {

	int i = q->front;
	int count = 0;
	while (count < q->size && i != (q->rear + 1) % LENGTH) {
		printf("node[%d]:\n", i);
		printf("\tprocess_id: %d\n", q->jobs[i].process_id);
		printf("\tjob_size: %d\n", q->jobs[i].job_size);

		i = (i + 1) % LENGTH;
		count++;
	}

}

void merge(Node arr[], int l, int m, int r) { 
    int i, j, k; 
    int n1 = m - l + 1; 
    int n2 =  r - m; 
  
    /* create temp arrays */
    Node L[n1]; 
    Node R[n2]; 
  
    /* Copy data to temp arrays L[] and R[] */
    for (i = 0; i < n1; i++) {
        L[i] = arr[l + i]; 
    }
    for (j = 0; j < n2; j++) {
        R[j] = arr[m + 1 + j]; 
    }
  
    /* Merge the temp arrays back into arr[l..r]*/
    i = 0; // Initial index of first subarray 
    j = 0; // Initial index of second subarray 
    k = l; // Initial index of merged subarray 
    while (i < n1 && j < n2) { 
        if (L[i].job_size <= R[j].job_size) { 
            arr[k] = L[i]; 
            i++; 
        } 
        else { 
            arr[k] = R[j]; 
            j++; 
        } 
        k++; 
    } 
  
    /* Copy the remaining elements of L[], if there 
       are any */
    while (i < n1) { 
        arr[k] = L[i]; 
        i++; 
        k++; 
    } 
  
    /* Copy the remaining elements of R[], if there 
       are any */
    while (j < n2) { 
        arr[k] = R[j]; 
        j++; 
        k++; 
    } 
} 

void mergeSort(Node arr[], int l, int r) { 
    if (l < r) { 
        // Same as (l+r)/2, but avoids overflow for 
        // large l and h 
        int m = l+(r-l)/2; 
  
        // Sort first and second halves 
        mergeSort(arr, l, m); 
        mergeSort(arr, m+1, r); 
  
        merge(arr, l, m, r); 
    } 
}

void sort(void) {
	if (q->rear < q->front) {
		mergeSort(q->jobs, q->front, MAX_CAPACITY);
		mergeSort(q->jobs, 0, q->rear);
	} else {
		mergeSort(q->jobs, q->front, q->rear);
	}
}









