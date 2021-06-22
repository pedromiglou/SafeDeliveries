Feature: Riders navbar

  Background:
    Given I navigate to "http://localhost:3000/"

  Scenario: Navbar without Account
    When I am not logged in my account
    Then It should not have the functionalities "Deliveries History"
    But It should have the functionality "Login"

  Scenario: Navbar with Account
    When I am logged in my account
    Then It should have the functionalities "Deliveries History"

  Scenario: Status change
    When I am logged in my account
    Then It should have status "Online"
    When I change status to "Delivering"
    Then It should have status "Delivering"
    When I change status to "Offline"
    Then It should have status "Offline"


