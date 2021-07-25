Feature: The API isolates content between multiple users

  Background:
    Given an existing user account
    And the client has authenticated
    And the client has successfully created 1 note

  Scenario: Can only view notes created by own account
    Given the client has authenticated with a different user account
    And the client has successfully created 2 notes
    When the client sends an authenticated request to retrieve all notes
    Then The client should get a 200 response
    And the response should contain 2 notes

  Scenario: Cannot view a note from a different account
    Given the client has authenticated with a different user account
    When the client sends an authenticated request to retrieve the 1st note
    Then The client should get a 404 response

  Scenario: Cannot overwrite notes from different account
    Given the client has authenticated with a different user account
    When the client sends an authenticated request to update the 1st note
    Then The client should get a 404 response

  Scenario: Cannot delete notes from different account
    Given the client has authenticated with a different user account
    When the client sends an authenticated request to delete the 1st note
    Then The client should get a 404 response
