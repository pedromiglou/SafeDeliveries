Feature: Orders functionalities

  Background:
    Given I access the url "http://localhost:3000/"
    And I am logged into normal account

  Scenario: Accept Notification
    Given I have a car
    When I receive a notification of a order
    And I click Accept
    Then My status changed to "Delivering"
    When I change my status to Online
    Then Should appear a modal with a error message
    And Click close modal
    When The Client confirms the delivery
    When I change my status to Online
    Then My status changed to "Online"

  Scenario: Decline Notification
    When I receive a notification of a order
    And I click Decline
    Then My status stays Online

  Scenario: View Orders History
    When I click on Deliveries History
    Then It should appear a order
    When I click in the order
    Then It sould appear the same information