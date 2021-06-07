Feature: Riders authentication

  Background:
    Given I navigate to "http://localhost:3000/"

  Scenario: Create an account
    When I am not logged in my account
    and I click in login button
    and I submit the register form
    Then It should have the message Your account has been created successfully!

  Scenario: Login
    When I am not logged in my account
    and I click in login button
    and I submit the login form
    Then I should be redirected to the main page






