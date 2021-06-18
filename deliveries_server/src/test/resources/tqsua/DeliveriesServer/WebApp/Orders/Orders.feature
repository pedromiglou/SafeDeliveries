Feature: Orders functionalities

  Background:
    Given I access the url "http://localhost:3000/"
    And I am logged into normal account

  Scenario: Accept Notification
    And I have a car
    When I receive a notification of a order
    And I click Accept
    Then My status changed to Delivering

  Scenario: Change status
    And I have status Delivering
    When I change my status to Online
    Then Should appear a modal with a error message

  #Scenario: Decline Notification
  #  When I receive a notification of a order
  #  And I click Decline
  #  Then My status stays Online