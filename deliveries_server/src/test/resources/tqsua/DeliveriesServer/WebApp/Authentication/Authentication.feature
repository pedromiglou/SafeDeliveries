Feature: Riders authentication

  Background:
    Given I access "http://localhost:3000/"
    And I click in login tab

  Scenario: Create an account
    When I click on create account
    And I fill all the details asked
    And I click on create
    Then It should have the message "Your account has been created successfully!"

  Scenario: Login
    When I fill my credentials
    And I click in enter button
    Then I should be redirected to the main page






