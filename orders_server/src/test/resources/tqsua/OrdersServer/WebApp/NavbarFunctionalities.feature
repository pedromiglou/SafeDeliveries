Feature: Riders navbar

  Background:
    Given I navigate to "http://localhost:3000/"

  Scenario: Navbar without Account
    When I am not logged in my account
    Then It should not have the functionalities "Request Delivery" and "Deliveries History"
    But It should have the functionality "Login"

  Scenario: Navbar with Account
    When I am logged in my account
    Then It should have the functionalities "Request Delivery" and "Deliveries History"





