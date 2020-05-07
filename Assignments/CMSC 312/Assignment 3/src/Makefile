CC = gcc
CFLAGS = -g
SOURCE_FILES = main.c helper.c wrapper.c error.c
LDFLAGS = -pthread

all: sjf fcfs

sjf:
	$(CC) $(CFLAGS) $(SOURCE_FILES) queue_sjf.c -o sjf $(LDFLAGS)

fcfs:
	$(CC) $(CFLAGS) $(SOURCE_FILES) queue_fcfs.c -o fcfs $(LDFLAGS)

clean:
	rm fcfs sjf