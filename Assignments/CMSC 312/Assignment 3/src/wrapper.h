#ifndef FILE_WRAPPER_SEEN
#define FILE_WRAPPER_SEEN

#include <fcntl.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/types.h>


/******************************************************/
/* Wrapper functions for POSIX utility functions.     */
/******************************************************/
pid_t Fork(void);
key_t Ftok(const char* pathname, int proj_id);

/******************************************************/
/* Wrapper functions for pthread operations.          */
/******************************************************/
int Pthread_cancel(pthread_t thread);
int Pthread_create(pthread_t *thread, const pthread_attr_t *attr, void * (*start_routine) (void *), void *arg);
int Pthread_join(pthread_t thread, void** retval);

/******************************************************/
/* Wrapper functions for semaphore operations.        */
/******************************************************/
int Sem_close(sem_t* sem);
sem_t* Sem_open(const char* name, int oflag, mode_t mode, unsigned int value);
int Sem_unlink(const char* name);
void P(sem_t* s);
void V(sem_t* s);

/******************************************************/
/* Wrapper functions for shared memory operations.    */
/******************************************************/
int Shmget(key_t key, size_t size, int shmflg);
void* Shmat(int shmid, const void* shmaddr, int shmflg);
int Shmdt(const void* shmaddr);
int Shmctl(int shmid, int cmd, struct shmid_ds* buf);

#endif /* !FILE_WRAPPER_SEEN */