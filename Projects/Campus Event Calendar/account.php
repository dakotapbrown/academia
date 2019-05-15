<?php
  session_start();
  
  if (!isset($_SESSION['user'])) {
		header('Location: http://128.172.188.107/~V00773006/index.php');
	}
?>

<html lang='en'>
  <head>
    
    <title>
      Account
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
	<link rel='stylesheet' href='utilities/formstyle.css' />
	<link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.min.css" />
	<link rel="stylesheet" href="bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css" />

    
    <!--Remote-->
    <link rel='stylesheet' href='//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css' />

    <!--JavasScript sources-->
    <!--Local-->
    <script src='fullcalendar/core/main.js'></script>
    <script src='fullcalendar/daygrid/main.js'></script>
    <script src='fullcalendar/list/main.js'></script>
    <script src='fullcalendar/timegrid/main.js'></script>
    <script src='fullcalendar/interaction/main.js'></script>
	<script type="text/javascript" src="bower_components/jquery/dist/jquery.min.js"></script>
	<script type="text/javascript" src="bower_components/moment/min/moment.min.js"></script>
	<script type="text/javascript" src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>

 
    <!--Remote-->
    <script src='https://unpkg.com/popper.js/dist/umd/popper.min.js'></script>
    <script src='https://unpkg.com/tooltip.js/dist/umd/tooltip.min.js'></script>
    <script src='https://code.jquery.com/ui/1.12.1/jquery-ui.js'></script>
	
  
  </head>
  
  <body>
   
      <div id='navbar' class='fc-toolbar fc-header-toolbar'>
        <div class='fc-left'></div>
        <div id='links' class='fc-center'>
          <div id='myButtons' class="fc-button-group">
            <button type='button' class='fc-button fc-button-primary' id='logoutButton'>logout</button>
            <button type='button' class='fc-button fc-button-primary'  id='mainButton'>main</button>
          </div>
        </div>
        <div class='fc-right'>
			<button type='button' class='fc-button fc-button-primary'  id='addEventButton'>new</button>
		</div>
      </div>
    
    
    
    <div id='calendar-container'>
      <div id='calendar'>
        <script src='utilities/userCalendar.js'></script>
      </div>
    </div>
			
	<div id="dialog-form">
	
		<?php include('utilities/newEventForm.html'); ?>
	</div>
  </body>
</html>