<?php
  session_start();
  
  if (!isset($_SESSION['user'])) {
		header('Location: http://128.172.188.107/~V00773006/index.php');
	}
?>

<html lang='en'>
  <head>
    
    <title>
      Profile
    </title>
    
    <meta charset='utf-8' />
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!--Stylesheets-->
    <!--Local-->
    <link rel='stylesheet' href='utilities/calendar.css' />
    <link rel='stylesheet' href='fullcalendar/core/main.css' />
    <link rel='stylesheet' href='fullcalendar/daygrid/main.css' />
    <link rel='stylesheet' href='fullcalendar/list/main.css' />
    <link rel='stylesheet' href='fullcalendar/timegrid/main.css' />
    
    <!--Remote-->
    <link rel='stylesheet' href='//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css' />

    <!--JavasScript sources-->
    <!--Local-->
    <script src='fullcalendar/core/main.js'></script>
    <script src='fullcalendar/daygrid/main.js'></script>
    <script src='fullcalendar/list/main.js'></script>
    <script src='fullcalendar/timegrid/main.js'></script>
    <script src='fullcalendar/interaction/main.js'></script>

    <!--Remote-->
    <script src='https://unpkg.com/popper.js/dist/umd/popper.min.js'></script>
    <script src='https://unpkg.com/tooltip.js/dist/umd/tooltip.min.js'></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="http://benalman.com/code/projects/jquery-outside-events/jquery.ba-outside-events.js"></script>
  
  </head>
  
  <body>
   
      <div id='navbar' class='fc-toolbar fc-header-toolbar'>
        <div class='fc-left'></div>
        <div id='links' class='fc-center'>
          <div id='myButtons' class="fc-button-group">
            <button type='button' class='fc-button fc-button-primary' id='logoutButton'>logout</button>
            <!--<a href='http://128.172.188.107/~V00773006/index.php?logout=1'>Logout</a>-->
            <button type='button' class='fc-button fc-button-primary'  id='accountButton'>account</button>
            <!--<a href='http://128.172.188.107/~V00773006/account.php'>Account</a>-->
          </div>
        </div>
        <div class='fc-right'></div>
      </div>
    
    
    
    <div id='calendar-container'>
      <div id='calendar'>
        <script src='utilities/mainCalendar.js'></script>
      </div>
    </div>
  </body>
</html>