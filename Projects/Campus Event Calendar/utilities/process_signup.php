<?php
	// Start session
	session_start();

	// Get username and password from signup form
	$studentID = $_POST['studentIdSignup'];
	$password = $_POST['passwordSignup'];
	$firstName = $_POST['firstNameSignup'];
	$lastName = $_POST['lastNameSignup'];
	$email = $_POST['emailSignup'];
	
	// TODO: Prevent SQL injection
	
	// Connect to Oracle database
	$conn = oci_connect("V00773006", "V00773006", "//localhost/xe");
	if (!$conn) {
		$m = oci_error();
		echo $m['message'], "\n";
		exit;
	} else {
		// Attempt INSERT statement		
		$query = 'BEGIN createNewUser(:firstName, :lastName, :studentID, :studentPass, :email); END;';
		$stid = oci_parse($conn, $query);
		oci_bind_by_name($stid, ":studentID", $studentID);
		oci_bind_by_name($stid, ":studentPass", $password);
		oci_bind_by_name($stid, ":firstName", $firstName);
		oci_bind_by_name($stid, ":lastName", $lastName);
		oci_bind_by_name($stid, ":email", $email);
		$r = oci_execute($stid);
		
		// TODO: Use proper error handling; this is a security nightmare
		if (!$r) {
			$err = oci_error($stid);
			print_r($err);
		} else {
			header("Location: http://128.172.188.107/~V00773006/login.php?signup_successful=1");
		}
	}
	
	// Close the Oracle connection
	oci_free_statement($stid);
	oci_close($conn);
?>