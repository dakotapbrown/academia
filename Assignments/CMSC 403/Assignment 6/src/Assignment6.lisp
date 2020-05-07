; literally just uses the list funciton repeatedly
(defun myList ()
	(return-from myList 
		(list 4  (list 7 22) "art" (list "math" (list 8) 99) 100)))


(defun leapYear ()
	(loop for n from 1800 to 2019
		; check that n is divisible by 4, but not 100
		; OR
		; that it is divisible by both 4 and 400
		when (or 
				(and 								
					(equal (mod n 4) 0) 			
					(not (equal (mod n 100) 0)))
				(and 
					(equal (mod n 4) 0) 
					(equal (mod n 400) 0)))
		; if it's either, collect it
		collect n into leapYears
		finally (return leapYears))
	)

(defun union- (list1 list2)
		(return-from union- 
			(append 
				; lambda checks each element of list1's
				; membership in list2, removes it if so
				(remove-if 
					#'(lambda (elem) (member elem list2)) 
					list1) 
				; join the modified list1 and unmodified list2
				list2)))

(defun avg (aList &optional (currentSum 0) (size 0))
	(if 
		; if aList is null and size is 0,
		; no recursive calls have been made and
		; avg was passed a null list
		(and 
			(NULL aList) 
			(equal size 0))
		(return-from avg (NIL))
		; otherwise, we've made recursive calls
		(if 					
			(and 
				(NULL aList) 
				(> size 0))
			; now, if aList is null, we're done
			(return-from avg (/ currentSum size))
			; if not, then keep making recurisve calls
			(avg 
				(cdr aList) 
				(+ (car aList) currentSum) 
				(+ size 1)))))

(defun isType (dataType)
	(return-from isType 
		(lambda (data) 
			(typep data dataType))))

(defun taxCalculator (limit rate values)
	; loop through the values list
	(loop for n in values
		; if n is greater than limit, tax it
		; and add it to our list
		if (> n limit)
			collect (* n rate)
		; otherwise, just leave it alone and add it
		else	
			collect n))

(defun clean (aFunc aList)
	(loop for elem in aList
		; call aFunc on each element in aList
		if (funcall aFunc elem)
			collect elem into elements
		; if it returns false, it's possilbe the
		; element is a list
		else
			; if it isn't, don't do anyhting
			; if it is, call clean on this sublist and 
			; collect its result
			if (listp elem)
				collect (funcall 'clean aFunc elem) into elements
		finally (return elements)))

(defmacro threeWayBranch (x y toExecute)
	`(if ,(< x y) 
		; get the firs element of toExecute
		(progn ,@(car toExecute))
		(if ,(> x y) 
			; get the second element of toExecute
			(progn ,@(car (cdr toExecute)))
			(if ,(= x y) 
				; get the third element of toExecute
				(progn ,@(car (cdr (cdr toExecute))))))))

