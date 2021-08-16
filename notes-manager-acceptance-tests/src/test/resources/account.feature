Feature: The API can manage user accounts

  Scenario: Create a new account
    When the client sends a request to create a new account
    Then The client should get a 201 response
    
  Scenario: Cannot create account with same username
    Given an existing user account
    When the client sends a request to create a new account
    Then The client should get a 409 response

  Scenario: Login to existing account
    Given an existing user account
    When the client has authenticated
    Then The client should get a 200 response

  Scenario: Authenticated user can view account details
    Given an existing user account
    And the client has authenticated
    When the client sends an authenticated request for account details
    Then The client should get a 200 response
