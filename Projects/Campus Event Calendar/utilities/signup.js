// There are many ways to pick a DOM node; here we get the form itself and the email
// input box, as well as the span element into which we will place the error message.

var form  = document.getElementsByTagName('form')[0];
var email = document.getElementById('email');
var error = document.getElementById('emailError');

email.addEventListener("input", function (event) {
  // Validation occurs as user types
  if (email.validity.valid) {
	// Remove error msg when field is valid
	error.innerHTML = ""; // Reset the content of the message
	error.className = "error"; // Reset the visual state of the message
  }
}, false);

form.addEventListener("submit", function (event) {
  // Check email is valid before sending form
  if (!email.validity.valid) {
	// If the field is not valid, we display a an error msg
	error.innerHTML = "Enter a valid email; i.e. email@host.com";
	error.className = "error active";
	
	// And we prevent the form from being sent by canceling the event
	event.preventDefault();
  }
}, false);