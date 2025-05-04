## Rob Bettison Payment Gateway Challenge in Java ##

# Running the code: #

Start the code as you would any Spring Boot project with the main class being PaymentGatewayApplication.java. Note that applications.properties is required for the BANK_URL environment variable and a jackson deserialization config variable which catches doubles being entered for the payment amount (instead of rounding them it fails validation).

# Assumptions: #
The code is designed for the challenge scenario in mind, it is not designed to be highly scalabale. 

# Security: #
Care is taken not to log full card numbers.

# Design decisions: #

I have split the application into four main areas:
The controller - PaymentGatewayController, handling incoming REST requests from the client.

The validator - PaymentValidator, validating that the card details are correct.

The payment gateway service - PaymentGatewayService, responsible for forwarding a request on to the aquiring bank once validations are passed and persisting the payment.

The aquiring bank service - AquiringBankService, responsible for contacting the bank.

# Exception handling: #

Validations are handled via a mixture of auto-validation using Spring functionality and a manual check on the expiry date being in the future. If a validation requirement is found to be violated, a CardDetailsException is thrown. The exception is caught in CommonExceptionHandler after we log the details and a suitable response is sent to the client. 

The other exception we throw is a BankException when we cannot communicate with the bank. In this scenario we return a 500 internal service error to the client. 

# Testing: #
I have a mixture of unit tests and integration tests. I have integration tested the controller to get a thorough test of the application. I have tried to unit test each file individually where there is some logic that could be broken by future developments.



