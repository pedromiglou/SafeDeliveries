Feature: Admin Funcionalities

  Background:
    Given I go to the url "http://localhost:3000/"
    And I am logged into admin account
    Then It should have admin text

  Scenario: Statistics Page
    When I click on the statistics page
    Then I have statistics page with total orders

