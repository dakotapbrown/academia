(defun clean (aFunc aList)
	(loop for elem in aList
		if (funcall aFunc elem)
			collect elem into elements
		else
			do if (listp elem)
				collect (funcall 'clean aFunc elem) into elements
		finally (return elements)
		)
	)

(let (
	   (stringTest (lambda (value) (typep value 'String)))
	   (numberTest (lambda (value) (typep value 'Number)))
	   (charTest (lambda (value) (typep value 'Character)))
		 )
		;Single list nothing to remove 
		(let* 
			((actual 
				(handler-case
					(clean numberTest '(1 2 3 4 5 6 7))
				(error ()
					T)
				)
			 ) 
			(expected '(1 2 3 4 5 6 7))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Single list nothing to remove~%") 
				 (format t "FAIL: Single list nothing to remove~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Single list remove all elements 
		(let* 
			((actual 
				(handler-case
					(clean stringTest '(1 2 3 4 5 6 7))
				(error ()
					T)
				)
			 ) 
			(expected ())
			;NIL AND NIL make nil so to test that actual and expected are both NIL we invert them to T then do the AND operation
			(result (AND (NOT actual) (NOT expected))))


			(if (not (NULL result))
				 (format t "PASS: Single list remove all elements~%") 
				 (format t "FAIL: Single list remove all elements~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Single list remove some things
		(let* 
			((actual 
				(handler-case
					(clean charTest '(1 #\x 3 #\y 5 "Jello" 7 #\z))
				(error ()
					T)
				)
			 ) 
			(expected '(#\x #\y #\z))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Single list remove some things~%") 
				 (format t "FAIL: Single list remove some things~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Nested list nothing to remove 
		(let* 
			((actual 
				(handler-case
					(clean numberTest '(1 (2 3) 4 (5 6 7)))
				(error ()
					T)
				)
			 )
			(expected '(1 (2 3) 4 (5 6 7)))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Nested list nothing to remove~%") 
				 (format t "FAIL: Nested list nothing to remove~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Nested list remove all elements 
		(let* 
			((actual 
				(handler-case
					(clean stringTest '(1 (2 3) 4 (5 6 7)))
				(error ()
					T)
				)
			 )
			(expected '(() ()))
			;NIL AND NIL make nil so to test that actual and expected are both NIL we invert them to T then do the AND operation
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Nested list remove all elements~%") 
				 (format t "FAIL: Nested list remove all elements~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Nested list remove some things
		(let* 
			((actual 
				(handler-case
					(clean charTest '(1 (#\x 3) #\y 5 ("Jello" 7 #\z)))
				(error ()
					T)
				)
			 )
			(expected '((#\x) #\y (#\z)))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Nested list remove some things~%") 
				 (format t "FAIL: Nested list remove some things~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Nested list remove all elements of sublist  
		(let* 
			((actual 
				(handler-case
					(clean stringTest '("Hello" (2 3 4 5 6 7) "World"))
				(error ()
					T)
				)
			 )
			(expected '("Hello" () "World"))
			;NIL AND NIL make nil so to test that actual and expected are both NIL we invert them to T then do the AND operation
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Nested list remove all elements of sublist~%") 
				 (format t "FAIL: Nested list remove all elements of sublist~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Multiple nested lists nothing to remove 
		(let* 
			((actual 
				(handler-case
					(clean numberTest '(1 (2 (18 19 (-1 -2 -3) 20) 3) 4 (5 6 7)))
				(error ()
					T)
				)
			 )
			(expected '(1 (2 (18 19 (-1 -2 -3) 20) 3) 4 (5 6 7)))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Multiple nested lists nothing to remove ~%") 
				 (format t "FAIL: Multiple nested lists nothing to remove ~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Multiple nested lists remove some things
		(let* 
			((actual 
				(handler-case
					(clean charTest '(1 (#\c (#\b #\a (-1 #\x -3) 20) #\y) "4" (5 #\z 7)))
				(error ()
					T)
				)
			 )
			(expected '((#\c (#\b #\a (#\x) ) #\y) (#\z)))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Multiple nested lists remove some things ~%") 
				 (format t "FAIL: Multiple nested lists remove some things ~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)
	)