<?php
	// Start session
	session_start();

	// Get username and password from login form
	$user = $_POST['loginStudentId'];
	$pass = $_POST['loginPassword'];
	
	// TODO: Prevent SQL injection
	
	// Connect to Oracle database
	$conn = oci_connect("V00773006", "V00773006", "//localhost/xe");
	if (!$conn) {
		$m = oci_error();
		echo $m['message'], "\n";
		exit;
	} else {
		// Check database for user and password				
		$query = 'select student_id as "user", student_pwd as "pass" from students
					where student_id = :student
					and student_pwd = :pass';
		$stid = oci_parse($conn, $query);
		oci_bind_by_name($stid, ":student", $user);
		oci_bind_by_name($stid, ":pass", $pass);
		$r = oci_execute($stid);
		
		// TODO: Use proper error handling; this is a security nightmare
		if (!$r) {
			$err = oci_error($stid);
			print_r($err);
		}
		
		// Fetch each row in an associative array
		$row = oci_fetch_array($stid, OCI_RETURN_NULLS+OCI_ASSOC);
		$dbuser = $row['user'];
		$dbpass = $row['pass'];
		
		if ($dbuser == $user && $dbpass == $pass) {
			// Login user
			$_SESSION['user'] = $user;
			$_SESSION['logged_in'] = 1;
			header("Location: http://128.172.188.107/~V00773006/profile.php");
		} else {
			// Send back to login.php and tell user username/passoword incorrect
			header("Location: http://128.172.188.107/~V00773006/login.php?login_unsuccessful=1");
		}
	}
	
	// Close the Oracle connection
	oci_free_statement($stid);
	oci_close($conn);
?>