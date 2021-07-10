Feature: The API can manage notes for authenticated users

  Background:
    Given an existing user account
    And the client has authenticated

  Scenario: Add a new note
    When the client sends an authenticated request to create a new note
    Then The client should get a 201 response
    And the response should contain a location header ending with /notes/1
    And the response should contain a note with a matching title
    And the response should contain a note with a matching description

  Scenario: Retrieve all previously created notes
    Given the client has successfully created 3 notes
    When the client sends an authenticated request to retrieve all notes
    Then The client should get a 200 response
    And the response should contain 3 notes

  Scenario: Retrieve a previously created note
    Given the client has successfully created 1 note
    When the client sends an authenticated request to retrieve the 1st note
    Then The client should get a 200 response
    And the response should contain a note with a matching title
    And the response should contain a note with a matching description

  Scenario: Overwrite a previously created note
    Given the client has successfully created 1 note
    When the client sends an authenticated request to update the 1st note
    Then The client should get a 200 response
    And the response should contain a note with a matching title
    And the response should contain a note with a matching description

  Scenario: Delete a previously created note
    Given the client has successfully created 1 note
    When the client sends an authenticated request to delete the 1st note
    Then The client should get a 204 response
