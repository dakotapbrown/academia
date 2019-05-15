<?php
	// Start session
	session_start();
	
	$msg = array();

	// Get username and password from signup form
	$title = isset($_GET['title']) ? $_GET['title'] : null;
	$start = isset($_GET['start']) ? $_GET['start'] : null;
	$end = isset($_GET['end']) ? $_GET['end'] : null;
	if(!$title || !$start || !$end) {
		$msg['error'] = 'Event must have title, start date/time, and end date/time.';
	}
	$desc = $_GET['desc'];
	$loc = $_GET['loc'];
	$partp = $_GET['partp'];
	$priv = $_GET['priv'];
	if(!$priv) {
		$msg['error'] = 'Privacy level must be set.';
	}
	$tag = $_GET['tag'];
	$creator = isset($_SESSION['user']) ? $_SESSION['user'] : null;
	if(!$creator) {
		$msg['error'] = 'Must be logged in to perform this operation.';
	}
	
	// TODO: Prevent SQL injection
	
	// Connect to Oracle database
	$conn = oci_connect("V00773006", "V00773006", "//localhost/xe");
	if (!$conn) {
		$m = oci_error();
		echo json_encode($m);
		exit;
	} else {
		// Attempt INSERT statement		
		$query = 'BEGIN createNewEvent(:eventTitle, :creator, :eventStart, :eventEnd, :eventDesc, :loc, :tag, :partp, :priv); END;';
		$stid = oci_parse($conn, $query);
		oci_bind_by_name($stid, ":eventTitle", $title);
		oci_bind_by_name($stid, ":eventStart", $start);
		oci_bind_by_name($stid, ":eventEnd", $end);
		oci_bind_by_name($stid, ":eventDesc", $desc);
		oci_bind_by_name($stid, ":loc", $loc);
		oci_bind_by_name($stid, ":tag", $tag);
		oci_bind_by_name($stid, ":partp", $partp);
		oci_bind_by_name($stid, ":creator", $creator);
		oci_bind_by_name($stid, ":priv", $priv);
		$r = oci_execute($stid);
		
		// TODO: Use proper error handling; this is a security nightmare
		if (!$r) {
			$err = oci_error($stid);
			echo json_encode($err);
		}
	}
	
	if ($msg['error']) {
		echo json_encode($msg);
	}
	
	// Close the Oracle connection
	oci_free_statement($stid);
	oci_close($conn);
?>