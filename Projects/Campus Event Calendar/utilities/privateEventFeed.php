<?php
	// Start session
	session_start();
	
	// Require our Event class and datetime utilities
	require('utils.php');

	// Short-circuit if the client did not give us a date range.
	if (!isset($_GET['start']) || !isset($_GET['end'])) {
	  die("Please provide a date range.");
	}
	
	// Parse the timeZone parameter if it is present.
	$timeZone = null;
	if (isset($_GET['timeZone'])) {
	  $timeZone = new DateTimeZone($_GET['timeZone']);
	}
	
	// Parse the start/end parameters.
	// These are assumed to be ISO8601 strings
	$range_start = parseDateTime($_GET['start'], $timeZone);
	$range_end = parseDateTime($_GET['end'], $timeZone);
	
	// Kill program if user is not logged in
	if(!isset($_SESSION['user'])) {
		die("You must be logged in to view user events.");
	}
	$studentID = $_SESSION['user'];

	
	// TODO: Prevent SQL injection
	
	// Connect to Oracle database
	$conn = oci_connect("V00773006", "V00773006", "//localhost/xe");
	if (!$conn) {
		// TODO: proper error handling
		$m = oci_error();
		echo $m['message'], "\n";
		exit;	
	} else {
		// Query for events in range 
		$query = 'BEGIN getPrivateUserEvents(:event_start, :event_end, :studentID); END;';
		$stid = oci_parse($conn, $query);
		
		// Format range dates to ISO8601 for SQL procedure
		$range_start_f = $range_start->format('c');
		$range_end_f = $range_end->format('c');
		
		// Bind elements to statment
		oci_bind_by_name($stid, ":event_start", $range_start_f);
		oci_bind_by_name($stid, ":event_end", $range_end_f);
		oci_bind_by_name($stid, ":studentID", $studentID);
		
		$r = oci_execute($stid);
		
		// TODO: Use proper error handling; this is a security nightmare
		if (!$r) {
			$err = oci_error($stid);
			print_r($err);
			exit;
		}
		
		// Accumulate an output array of event data arrays.
		$eventList = array();
		
		//Fetch into associative array
		while ($row = oci_fetch_array($stid, OCI_ASSOC+OCI_RETURN_NULLS)) {
			// Convert the input array into a useful Event object
			$event = new Event($row, $timeZone);
			
			// If the event is in-bounds, add it to the output
			if ($event->isWithinDayRange($range_start, $range_end)) {
			  $eventList[] = $event->toArray();
			}
		}
		
		// Close the Oracle connection
		oci_free_statement($stid);
		oci_close($conn);
		
		// Post the JSON data for FullCalender
		header('Content-Type: application/json');
		echo json_encode($eventList);
	}
?>