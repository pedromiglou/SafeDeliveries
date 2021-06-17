Feature: Orders functionalities

  Background:
    Given I access the url "http://localhost:3000/"
    And I am logged into normal account

  Scenario: Accept Notification
    And I have a car
    When I receive a notification of a order
    And I click Accept
    Then My status changed to Delivering

  Scenario: Decline Notification
    When I receive a notification of a order
    And I click Decline
    Then My status stays Online