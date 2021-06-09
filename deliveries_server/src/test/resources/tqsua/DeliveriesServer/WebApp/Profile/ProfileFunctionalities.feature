Feature: Profile Area

  Background:
    Given I go to "http://localhost:3000/"
    And I am logged into my account
    When I click on the user-logo
    And I click on Profile

  Scenario: Add new vehicles
    When I want to add a new vehicle, I have to click on button add
    Then I have to fill the registration("05-GC-32"), maker("BMW"), model("M4"), capacity("20"kg) and type("passenger car") fields
    Then I have to click on button confirm
    And Check that the vehicle with registration ("05-GC-32") is in the table

  Scenario: Edit vehicles
    When I want to edit a vehicle, I Click on the pencil icon of the vehicle with registration ("05-GC-32")
    Then Change the field referring to "capacity" to "10"kg and confirm
    And Check that the vehicle was edited, and now has in the field "capacity" the value "10"kg

  Scenario: Delete vehicles
    When I want to delete a vehicle, I click on the delete icon of the vehicle with registration ("05-GC-32")
    Then Check that the vehicle is no longer in the table

  Scenario: Update last name
    When I want to update my last name, I click on edit icon on top
    Then Change the "last name" to "Cruz" and confirm
    And Check that the "last name" is "Cruz"
