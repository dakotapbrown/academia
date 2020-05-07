(load "Assignment6.lisp")

(defun doesFunctionExist (funcName)
	(handler-case (funcall (intern (string-upcase funcName))) 
		(UNDEFINED-FUNCTION () (format t "FAIL: ~a is not defined~%" funcName) NIL) 
		(T () T)
	)
)

(defun doesMacroExist (macroName)
	(locally
		(declare (sb-ext:muffle-conditions cl:warning))
			(if (macro-function (intern (string-upcase macroName)))
				;True Branch
				T
				;false branch
				(progn (format t "FAIL: ~a is not defined~%" macroName) NIL)
			)    	
    )
)

(defun unbindFunctions () 
	(fmakunbound 'myList)
	(fmakunbound 'leapYear)
	(fmakunbound 'union-)
	(fmakunbound 'avg)
	(fmakunbound 'isType)
	(fmakunbound 'taxCalculator)
	(fmakunbound 'clean)
	(fmakunbound 'threeWayBranch)
)

(format t "~%myList Tests~%")
(if (doesFunctionExist "myList")
	(let* 
		((actual (myList)) 
		(expected (list 4 (list 7 22) "art" (list "math" (list 8) 99) 100))
		(result (equal actual expected)))


		(if (not (NULL result))
			 (format t "PASS: List is built correctly~%") 
			 (format t "FAIL: ~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
		)
	)
)

(format t "~%leapYear Tests~%") 
(if (doesFunctionExist "leapYear")
	(let (
		(actual 
			(handler-case
				(leapYear)
			(error ()
				'(()) )
			)
		 )

		(expected '(1804 1808 1812 1816 1820 1824 1828 1832 1836 1840 1844 1848 1852 1856 1860
	 1864 1868 1872 1876 1880 1884 1888 1892 1896 1904 1908 1912 1916 1920 1924
	 1928 1932 1936 1940 1944 1948 1952 1956 1960 1964 1968 1972 1976 1980 1984
	 1988 1992 1996 2000 2004 2008 2012 2016))
		)
		;Test if we have all the expected leap years
		(let ((result ()))

			(dolist (x expected) (if (find x actual) () (push x result)))

			(if (NULL result)
			 (format t "PASS: Have all leap years~%") 
			 (format t "FAIL: Missing leap years: ~a~%" result)
			)

		)
		;Test if we have any years that are not leap years 
		(let ((result ()))

			(dolist (x actual) (if (find x expected) () (push x result)))

			(if (NULL result)
			 (format t "PASS: Only has leap years~%") 
			 (format t "FAIL: The following are not leap years: ~a~%" result)
			)

		)
	)
)

(format t "~%union- Tests~%")
(if (doesFunctionExist "union-")
	(let ()
		;Exclusive lists 
		(let* 
			((actual (union- '(1 2 3 4 5) '(6 7 8 9 10))) 
			(expected '(1 2 3 4 5 6 7 8 9 10))
			;set-exclusive-or tests if actual and expected have the same elements
			(correctElements (NULL (set-exclusive-or actual expected)))
			;Check that there are no duplicates in actual by calling remove-duplicates on actual and comparing it to actual 
			(noDuplicates (equal actual (remove-duplicates actual)))
			;And the two booleans together to determine if the test passes or fails 
			(result (AND correctElements noDuplicates)))


			(if (not (NULL result))
				 (format t "PASS: all difference elements~%") 
				 (format t "FAIL: all difference elements~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Identical lists 
		(let* 
			((actual (union- '(1 2 3 4 5) '(5 4 3 2 1))) 
			(expected '(1 2 3 4 5))
			;set-exclusive-or tests if actual and expected have the same elements
			(correctElements (NULL (set-exclusive-or actual expected)))
			;Check that there are no duplicates in actual by calling remove-duplicates on actual and comparing it to actual 
			(noDuplicates (equal actual (remove-duplicates actual)))
			;And the two booleans together to determine if the test passes or fails 
			(result (AND correctElements noDuplicates)))


			(if (not (NULL result))
				 (format t "PASS: identical lists~%") 
				 (format t "FAIL: identical lists~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Overlapping lists 
		(let* 
			((actual (union- '(1 2 3 6 7) '(7 6 8 9 10))) 
			(expected '(1 2 3 6 7 8 9 10))
			;set-exclusive-or tests if actual and expected have the same elements
			(correctElements (NULL (set-exclusive-or actual expected)))
			;Check that there are no duplicates in actual by calling remove-duplicates on actual and comparing it to actual 
			(noDuplicates (equal actual (remove-duplicates actual)))
			;And the two booleans together to determine if the test passes or fails 
			(result (AND correctElements noDuplicates)))


			(if (not (NULL result))
				 (format t "PASS: overlapping lists~%") 
				 (format t "FAIL: overlapping lists~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;One NIL list 
		(let* 
			((actual (union- '(1 2 3 9 10) ())) 
			(expected '(1 2 3 9 10))
			;set-exclusive-or tests if actual and expected have the same elements
			(correctElements (NULL (set-exclusive-or actual expected)))
			;Check that there are no duplicates in actual by calling remove-duplicates on actual and comparing it to actual 
			(noDuplicates (equal actual (remove-duplicates actual)))
			;And the two booleans together to determine if the test passes or fails 
			(result (AND correctElements noDuplicates)))


			(if (not (NULL result))
				 (format t "PASS: one NIL list~%") 
				 (format t "FAIL: one NIL list~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Both NIL lists
		(let* 
			((actual (union- () ())) 
			(expected ())
			;set-exclusive-or tests if actual and expected have the same elements
			(correctElements (NULL (set-exclusive-or actual expected)))
			;Check that there are no duplicates in actual by calling remove-duplicates on actual and comparing it to actual 
			(noDuplicates (equal actual (remove-duplicates actual)))
			;And the two booleans together to determine if the test passes or fails 
			(result (AND correctElements noDuplicates)))


			(if (not (NULL result))
				 (format t "PASS: Both NIL lists~%") 
				 (format t "FAIL: Both NIL lists~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Non numeric lists
		(let* 
			((actual 
				(handler-case
					(union- '(2 3 X Y Z) '(1 2 3 A B C X Y))
				(error ()
					'("Exception Thrown") )
				)
			 ) 
			(expected '(1 2 3 A C B X Y Z))
			;set-exclusive-or tests if actual and expected have the same elements
			(correctElements (NULL (set-exclusive-or actual expected)))
			;Check that there are no duplicates in actual by calling remove-duplicates on actual and comparing it to actual 
			(noDuplicates (equal actual (remove-duplicates actual)))
			;And the two booleans together to determine if the test passes or fails 
			(result (AND correctElements noDuplicates)))


			(if (not (NULL result))
				 (format t "PASS: Non numeric lists~%") 
				 (format t "FAIL: Non numeric lists~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)
	)
)
;;TODO make a test for a NIL input
(format t "~%avg Tests~%")
(if (doesFunctionExist "avg")
	(let ()
		;List with one element
		(let* 
			((actual 
				(handler-case
					(avg '(1))
				(error ()
					NIL )
				)
			 ) 
			(expected 1)
			(result 
				(handler-case
					(= actual expected)
						(error ()
							NIL)
						))
			)

			(if (not (NULL result))
				 (format t "PASS: List with one element~%") 
				 (format t "FAIL: List with one element~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Avg is whole number
		(let* 
			((actual 
				(handler-case
					(avg '(2 5 3 10))
				(error (c)
					c )
				)
			 )
			(expected 5)
			(result 
				(handler-case
					(= actual expected)
						(error ()
							NIL)
						))
			)


			(if (not (NULL result))
				 (format t "PASS: Avg is whole number~%") 
				 (format t "FAIL: Avg is whole number~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Avg is fraction 
		(let* 
			((actual 
				(handler-case
					(avg '(20 1 63 163 1/5 10/6 97 35))
				(error ()
					NIL )
				)
			 )
			(expected 5713/120)
			(result 
				(handler-case
					(= actual expected)
						(error ()
							NIL)
						))
			)


			(if (not (NULL result))
				 (format t "PASS: Avg is fraction ~%") 
				 (format t "FAIL: Avg is fraction ~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)
	)
)

(format t "~%isType Tests~%")
(if (doesFunctionExist "isType")
	(let ()
		;isType returns a function
		(let* 
			((actual 
				(handler-case
					(functionp (isType 'String))
				(error ()
					NIL)
				)
			 ) 
			(expected T)
			(result (AND actual expected)))




			(if (not (NULL result))
				 (format t "PASS: isType returns a function~%") 
				 (format t "FAIL: isType returns a function~%")
			)
		)

		;Anonymous function bound to 'String given String
		(let* 
			((actual 
				(handler-case
					(funcall (isType 'String) "String")
				(error ()
					NIL)
				)
			 ) 
			(expected T)
			(result (AND actual expected)))




			(if (not (NULL result))
				 (format t "PASS: isType passed a 'String argument, returned anonymous function passed a String~%") 
				 (format t "FAIL: isType passed a 'String argument, expected the returned anonymous function to return T when passed a String~%")
			)
		)

		;Anonymous function bound to 'String given list
		(let* 
			((actual 
				(handler-case
					(funcall (isType 'String) '(3 4 2))
				(error ()
					"isType error")
				)
			 )  
			(expected NIL)
			;NIL AND NIL make nil so to test that actual and expected are both NIL we invert them to T then do the AND operation
			(result (AND (NOT actual) (NOT expected))))




			(if (not (NULL result))
				 (format t "PASS: isType passed a 'String argument, returned anonymous function passed a list~%") 
				 (format t "FAIL: isType passed a 'String argument, expected the returned anonymous function to return NIL when passed a list~%")
			)
		)

		;Anonymous function bound to 'Number given Number
		(let* 
			((actual 
				(handler-case
					(funcall (isType 'Number) 4/5)
				(error ()
					NIL)
				)
			 )  
			(expected T)
			(result (AND actual expected)))




			(if (not (NULL result))
				 (format t "PASS: isType passed a 'Number argument, returned anonymous function passed a number~%") 
				 (format t "FAIL: isType passed a 'Number argument, expected the returned anonymous function to return T when passed a number~%")
			)
		)

		;Anonymous function bound to 'Number given list
		(let* 
			((actual 
				(handler-case
					(funcall (isType 'Number) '(3 4 2))
				(error ()
					"isType error")
				)
			 ) 
			(expected NIL)
			;NIL AND NIL make nil so to test that actual and expected are both NIL we invert them to T then do the AND operation
			(result (AND (NOT actual) (NOT expected))))


	 

			(if (not (NULL result))
				 (format t "PASS: isType passed a 'Number argument, returned anonymous function passed a list~%") 
				 (format t "FAIL: isType passed a 'Number argument, expected the returned anonymous function to return NIL when passed a list~%")
			)
		)
	)
)

(format t "~%taxCalculator Tests~%")
(if (doesFunctionExist "taxCalculator")
	(let ()
		;No elements greater than limit 
		(let* 
			((actual 
				(handler-case
					(taxCalculator 100 1/2 '(1 99 24 12 64 52 91 38 10 2 4 6))
				(error ()
					T)
				)
			 ) 
			(expected '(1 99 24 12 64 52 91 38 10 2 4 6))
			(result (equal actual expected)))

			

			(if (not (NULL result))
				 (format t "PASS: No elements greater than limit~%") 
				 (format t "FAIL: No elements greater than limit~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Some elements greater than limit 
		(let* 
			((actual 
				(handler-case
					(taxCalculator 50 1/2 '(1 99 24 12 64 52 91 38 10 2 4 6))
				(error ()
					T)
				)
			 )
			(expected '(1 99/2 24 12 32 26 91/2 38 10 2 4 6))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: Some elements greater than limit~%") 
				 (format t "FAIL: Some elements greater than limit~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;All elements greater than limit 
		(let* 
			((actual 
				(handler-case
					(taxCalculator 20 2/3 '(100/2 99 24 122 64 52 91 38 70 246))
				(error ()
					T)
				)
			 )
			(expected '(100/3 66 16 244/3 128/3 104/3 182/3 76/3 140/3 164))
			(result (equal actual expected)))


			(if (not (NULL result))
				 (format t "PASS: All elements greater than limit~%") 
				 (format t "FAIL: All elements greater than limit~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;Values is an empty list 
		(let* 
			((actual
				;Set the value of actual to the result of tax calcualtor or to the exception thrown by taxCalcualtor  
				(handler-case
					(taxCalculator 50 0 '())  
				(error ()
					T))
			) 
			(expected NIL)
			;NIL AND NIL make nil so to test that actual and expected are both NIL we invert them to T then do the AND operation
			(result (AND (NOT actual) (NOT expected))))


			(if (not (NULL result))
				 (format t "PASS: Values is an empty list~%") 
				 (format t "FAIL: Values is an empty list~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)
	)
)

(format t "~%clean Tests~%")
(if (doesFunctionExist "clean")
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
)

(format t "~%threeWayBranch Tests~%")
(if (doesMacroExist "threeWayBranch")
	(let ()
		;x < y single statement 
		(let* 
			((actual 
				(handler-case
					(threeWayBranch 2 3 (   ((+ 10 2)) ((- 10 2)) ((* 3 3)) ))
				(error ()
					35505)
				)
			 ) 
			(expected 12)
			(result 
				(handler-case
					(= actual expected)
				(error ()
					T)
				))
			)

			(if (not (NULL result))
				 (format t "PASS: x < y single statement~%") 
				 (format t "FAIL: x < y single statement~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;x > y single statement 
		(let* 
			((actual 
				(handler-case
					(threeWayBranch 3 2 (   ((+ 10 2)) ((- 10 2)) ((* 3 3)) ))
				(error ()
					35505)
				)
			 ) 
			(expected 8)
					(result 
				(handler-case
					(= actual expected)
				(error ()
					T)
				))
			)

			(if (not (NULL result))
				 (format t "PASS: x > y single statement~%") 
				 (format t "FAIL: x > y single statement~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;x = y single statement 
		(let* 
			((actual 
				(handler-case
					(threeWayBranch 3 3 (   ((+ 10 2)) ((- 10 2)) ((* 3 3)) ))
				(error ()
					35505)
				)
			 ) 
			(expected 9)
					(result 
				(handler-case
					(= actual expected)
				(error ()
					T)
				))
			)

			(if (not (NULL result))
				 (format t "PASS: x = y single statement~%") 
				 (format t "FAIL: x = y single statement~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;x < y multiple statements
		(let* 
			((testVar NIL)
			(actual 
				(handler-case
					(threeWayBranch 2 3 ( ((setf testVar 10)(+ testVar 2)) ((setf testVar 2)(- 10 testVar)) ((setf testVar 3)(* testVar testVar))))
				(error ()
					35505)
				)
			)  
			(expected 12)
					(result 
				(handler-case
					(= actual expected)
				(error ()
					T)
				))
			)

			(if (not (NULL result))
				 (format t "PASS: x < y multiple statements~%") 
				 (format t "FAIL: x < y multiple statements~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;x > y multiple statements
		(let* 
			((testVar NIL)
			(actual 
				(handler-case
					(threeWayBranch 3 2 ( ((setf testVar 10)(+ testVar 2)) ((setf testVar 2)(- 10 testVar)) ((setf testVar 3)(* testVar testVar))))
				(error ()
					35505)
				)
			)  
			(expected 8)
			(result 
				(handler-case
					(= actual expected)
				(error ()
					T)
				))
			)

			(if (not (NULL result))
				 (format t "PASS: x > y multiple statements~%") 
				 (format t "FAIL: x > y multiple statements~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)

		;x = y multiple statements
		(let* 
			((testVar NIL)
			(actual 
				(handler-case
					(threeWayBranch 3 3 ( ((setf testVar 10)(+ testVar 2)) ((setf testVar 2)(- 10 testVar)) ((setf testVar 3)(* testVar testVar))))
				(error ()
					35505)
				)
			) 
			(expected 9)
			(result 
				(handler-case
					(= actual expected)
				(error ()
					T)
				))
			)

			(if (not (NULL result))
				 (format t "PASS: x = y multiple statements~%") 
				 (format t "FAIL: x = y multiple statements~%	ACTUAL:   ~a~%	EXPECTED: ~a~%" actual expected)
			)
		)
	)
)

(unbindFunctions)