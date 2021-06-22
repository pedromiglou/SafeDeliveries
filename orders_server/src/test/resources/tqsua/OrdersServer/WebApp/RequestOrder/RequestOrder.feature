Feature: User Requests a Order

  Background:
    Given I go to "http://localhost:3001/"
    And I am logged in

  Scenario: Request Order
    And I click on Request Delivery Tab
    Given I fill pick up address (Addres "R.Circular 3", Country "Portugal", City "Pereira", Postal Code "3140")
    And I fill delivery address (Addres "Rua De Pedride 223", Country "Portugal", City "São Lourenço", Postal Code "4635-616")
    And I add item(s) by clicking on button Add
    And filling the fields with name "bola", category "brinquedo" and weight "2"
    And adding another item with name "almofada", category "utensilios" and weight "1"
    When I click on button Place Order
    Then I should see the message "Order was created successfully!"
    And I should be redirected to the Waiting For a Rider Page
    When a rider accepts my order, I should be redirected to the Order Details Page
    And I can see the order that I requested has pick up address "R. Circular 3, 3140 Pereira, Portugal"
    And delivery address "Rua De Pedride 223, 4635-616 São Lourenço, Portugal"
    And the Items with names "bola" and "almofada", categories "brinquedo" and "utensilios" and weights "2" and "1"

  Scenario: Accept Order
    And I click on History Tab
    Then It should appear a order
    When I click in the order
    Then It sould appear the same information
    When I accept Order
    Then It should appear status Delivered

  Scenario: Request Order with no Items
    And I click on Request Delivery Tab
    Given I want to do a order but i forget to add items
    And I click to Place Order
    Then I get the warning "Error. Order with 0 items."
