Feature: Profile Area

  Background:
    Given I access "http://localhost:3000/"
    And I am logged into my account
    When I click on the logo
    And I click on Profile

  Scenario: Add new vehicles
    When I want to add a new vehicle, I have to click on button add
    Then I have to fill the maker("BMW"), model("M4"), capacity("20kg") and type("passenger car") fields
    Then I have to click on button confirm
    And Check that the vehicle added is in the table

  Scenario: Edit vehicles
    When I want to edit a vehicle, I Click on the pencil icon of the vehicle "Honda PCX"
    Then Change the field referring to "capacity" to "10kg" and confirm
    And Check that the vehicle "Honda PCX" was edited

  Scenario: Delete vehicles
    When I want to delete a vehicle, I click on the delete icon of the vehicle "BMW Z3"
    Then I click on button confirm
    And Check that the vehicle "BMW Z3" is no longer in the table