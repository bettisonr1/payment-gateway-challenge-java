Running the code:
Start the code as you would any Spring Boot project with the main class being PaymentGatewayApplication.java

Design decisions:
I have split the application into four main areas:
The controller, handling incoming REST requests from the client.

The validator, validating that card details are correct.

The payment gateway, forwarding a request on to the aquiring bank once validations are passed, and persisting the payment.

The aquiring bank service, responsible for contacting the bank.

Testing:
I have a mixture of unit tests and integration tests. I have integration tested the controller to get a thorough test of the application. I have tried to unit test each file individually where there is some logic that could be broken by future developments.

