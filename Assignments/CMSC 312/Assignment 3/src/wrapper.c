#include "wrapper.h"
#include "error.h"


/******************************************************/
/* Wrapper functions for POSIX utility functions.     */
/******************************************************/
pid_t Fork(void) {

	pid_t pid;

	if ((pid = fork()) < 0) {
		unix_error("fork error");
	}
	return pid;
}

key_t Ftok(const char* pathname, int proj_id) {

	key_t key;

	if ((key = ftok(pathname, proj_id)) < 0) {
		unix_error("ftok error");
	}
	return key;
}


/******************************************************/
/* Wrapper functions for semaphore operations.        */
/******************************************************/
int Sem_close(sem_t* sem) {

	int rc;
	if ((rc = sem_close(sem)) < 0) {
		unix_error("sem_close error");
		exit(0);
	}
	return rc;
}

sem_t* Sem_open(const char *name, int oflag, mode_t mode, unsigned int value) {

	sem_t* sem;
	if ((sem = sem_open(name, oflag, mode, value)) == SEM_FAILED) {
		unix_error("sem_open error");
		exit(0);
	}
	return sem;
}

int Sem_unlink(const char* name) {

	int rc;
	if ((rc = sem_unlink(name)) < 0) {
		unix_error("sem_unlink error");
		exit(0);
	}
	return rc;
}

void P(sem_t* s) {

	if (sem_wait(s) < 0) {
		unix_error("sem_wait error");
		exit(0);
	}
}

void V(sem_t* s) {

	if (sem_post(s) < 0) {
		unix_error("sem_post error");
		exit(0);
	}
}


/******************************************************/
/* Wrapper functions for pthread operations.          */
/******************************************************/
int Pthread_cancel(pthread_t thread) {

	int rc;
	if ((rc = pthread_cancel(thread)) != 0) {
		posix_error(rc, "pthread_cancel error");
		exit(0);
	}
	return rc;
}

int Pthread_create(pthread_t *thread, const pthread_attr_t *attr,
					void * (*start_routine) (void *), void *arg) {

	int rc;
	if ((rc = pthread_create(thread, attr, start_routine, arg)) != 0) {
		posix_error(rc, "pthread_create error");
	}
	return rc;
}

int Pthread_join(pthread_t thread, void** retval) {

	int rc;
	if ((rc = pthread_join(thread, retval)) != 0) {
		posix_error(rc, "pthread_join error");
	}
	return rc;
}


/******************************************************/
/* Wrapper functions for shared memory operations.    */
/******************************************************/
int Shmget(key_t key, size_t size, int shmflg) {

	int shmid;

	if ((shmid = shmget(key, size, shmflg)) < 0) {
		unix_error("shmget error");
	}
	return shmid;
}

void* Shmat(int shmid, const void* shmaddr, int shmflg) {

	void* data;

	if ((data = shmat(shmid, shmaddr, shmflg)) == (char *) (-1)) {
		unix_error("shmat error");
	}
	return data;
}

int Shmdt(const void* shmaddr) {

	int rc;

	if ((rc = shmdt(shmaddr)) < 0) {
		unix_error("shmdt error");
	}
	return rc;
}

int Shmctl(int shmid, int cmd, struct shmid_ds* buf) {

	int rc;

	if ((rc = shmctl(shmid, cmd, buf)) < 0) {
		unix_error("shmctl error");
	}
	return rc;
}
