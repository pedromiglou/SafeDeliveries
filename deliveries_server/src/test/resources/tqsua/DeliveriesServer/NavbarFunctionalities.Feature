Feature: Riders navbar

  Background:
    Given I navigate to "http://localhost:3000/"

  Scenario: Navbar with Account
    When I am logged in my account
    Then It should have the functionality "Search for a delivery"
    And It should have the functionality "Deliveries history"

  Scenario: Navbar without Account
    When I am not logged in my account
    Then It should not have the functionality "Search for delivery"
    And It should not have the functionality "Deliveries history"
    But It should have the functionality "Login"



