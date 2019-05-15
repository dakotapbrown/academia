<?php
	session_start();

	if(isset($_GET['logout'])) {
		if($_GET['logout'] = 1) {
		  session_unset();
		  session_destroy();
		}
	}
	
	if(isset($_SESSION['logged_in'])) {
		if($_SESSION['logged_in'] = 1) {
			header('Location: http://128.172.188.107/~V00773006/profile.php');
		}
	}
?>
<html>
	<head>
		<title>
			Main
		</title>
	</head>
	
	<body>
		<h1>
			Welcome to VCU Events!
		</h1>
		
		<label>Returning user?</label>
		<br><a href="http://128.172.188.107/~V00773006/login.php" id="signInLink">Sign In</a><br>
		<label>New user? Start using VCU Events now! </label>
		<br><a href="http://128.172.188.107/~V00773006/signup.php" id="signUpLink">Sign Up</a><br>
	</body>
</html>

