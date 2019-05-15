<?php
   session_start();
   
   if(isset($_SESSION['user'])) {
      header("Location: http://128.172.188.107/~V00773006/profile.php");
   }
?>
<html>
   <head>
      <title>
         Login
      </title>
      <link href="https://developer.mozilla.org/static/build/styles/samples.37902ba3b7fe.css" rel="stylesheet" type="text/css">
      <link rel="stylesheet" type="text/css" href="utilities/style.css" />
   </head>
   <body>
      <div id = "loginForm" />
         <?php include('utilities/loginForm.html'); ?>
      </div>
   </body>
</html>