document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');
  
  var dialogDiv = $(document.getElementById('dialog-form'));
  dialogDiv.dialog({
    title: 'New Event',
    autoOpen: false,
    modal: true,
    height: 'auto',
    width: 500,
    position: { my: "center", at: "center", of: window },
    buttons: {
      "Create event": createEvent,
      Cancel: function() {
        $(this).dialog("close");
      }
    }
  });

  var calendar = new FullCalendar.Calendar(calendarEl, {
    eventSources: [
		{
		  url: 'http://128.172.188.107/~V00773006/utilities/publicEventFeed.php',
		  color: 'gold',    
		  textColor: 'black' 
		},
		{
		  url: 'http://128.172.188.107/~V00773006/utilities/privateEventFeed.php',
		  color: 'red',    
		  textColor: 'white' 
		}
	],
    plugins: [ 'interaction', 'dayGrid', 'list', 'timeGrid' ],
    eventClick:
      function(info) {
        var newDiv = $(document.createElement('div'));
        var eventObj = info.event;
        var options = {
          weekday: 'short',
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: 'numeric',
          minute: 'numeric'
        };
        
        newDiv.dialog({
          title: eventObj.title,
          open: 
            function() {
              var markup = '<p> Start: ' + eventObj.start.toLocaleDateString('en-US', options) + '</p>' +
                '<p> End: ' + eventObj.end.toLocaleDateString('en-US', options) + '</p>' +
                '<p> Description: ' + eventObj.extendedProps.description + '</p>' +
                '<p> Location: ' + eventObj.extendedProps.location + '</p>';
              $(this).html(markup);
            },
          height: 'auto',
          width: 500,
          position: { my: "center", at: "center", of: window }
        });
      },
    eventRender:
      function(info) {
        var tooltip = new Tooltip(info.el, {
          title: info.event.extendedProps.description,
          placement: 'top',
          trigger: 'hover',
          container: 'body'
        });
    },
    timezone: 'EST',
    selectable: true,
    nowIndicator: true, 
    defaultView: 'dayGridMonth',
    header: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    height: 'parent',
    dateClick: function(dateInfo) {
      var addEventButton = document.getElementById('addEventButton');
      addEventButton.addEventListener('click',
        function addAfterDateClickListener() {
          addEventButton.removeEventListener('click', addAfterDateClickListener);
          setForms(dateInfo);
          dialogDiv.dialog('open');
        });
    }
  });

  calendar.render();
  
  document.getElementById('logoutButton').addEventListener('click',
    function() {
      window.location.href = 'http://128.172.188.107/~V00773006/index.php?logout=1';
    });
  
  document.getElementById('mainButton').addEventListener('click',
    function() {
      window.location.href = 'http://128.172.188.107/~V00773006/profile.php';
    });
  
  document.getElementById('addEventButton').addEventListener('click', function() {
      setForms(new Date());
      dialogDiv.dialog('open');
    });
  
  function createEvent() {
    const dateInfo = {
      start: $('#startDateTimePicker').data('DateTimePicker').date().format(),
      end: $('#endDateTimePicker').data('DateTimePicker').date().format(),
      title: document.getElementById('eventTitle').value,
      desc: document.getElementById('description').value,
      loc: document.getElementById('location').value,
      tag: document.getElementById('tag').value,
      partp: document.getElementById('partp').value,
      priv: document.getElementById('privacy').value
    };
    
    jQuery.get('http://128.172.188.107/~V00773006/utilities/process_newEvent.php',
                dateInfo,
                function (return_data) {
                    alert(return_data);
                  },
                'json');
    
    dialogDiv.dialog('close');
    
    calendar.refetchEvents();
  }
  
  function setForms(dateInfo) {
    $('#startDateTimePicker').datetimepicker({
        stepping: 5,
        date: moment(dateInfo.date)
      });
    
    $('#endDateTimePicker').datetimepicker({
        date: moment(dateInfo.date)
      });
    
    $('#startDateTimePicker').on('dp.change', function(e) {
        $('#endDateTimePicker').data('DateTimePicker').minDate(e.date.subtract(1, 'days'));
        $('#endDateTimePicker').data('DateTimePicker').date(e.date.add(1, 'days'));
      });
  }
});

