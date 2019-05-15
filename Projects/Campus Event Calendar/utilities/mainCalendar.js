document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');

  var calendar = new FullCalendar.Calendar(calendarEl, {
    eventSources: [
      {
        url: 'http://128.172.188.107/~V00773006/utilities/mainEventFeed.php',
        color: 'gold',    
        textColor: 'black' 
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
    height: 'parent'
  });

  calendar.render();
});

document.getElementById('logoutButton').addEventListener('click',
  function() {
    window.location.href = 'http://128.172.188.107/~V00773006/index.php?logout=1';
  });

document.getElementById('accountButton').addEventListener('click',
  function() {
    window.location.href = 'http://128.172.188.107/~V00773006/account.php';
  });