Feature: The API can reject calls from unauthenticated users

  Scenario: Cannot add a new note when not authenticated
    When the client sends an unauthenticated request to create a new note
    Then The client should get a 403 response

  Scenario: Cannot retrieve all notes when not authenticated
    When the client sends an unauthenticated request to retrieve all notes
    Then The client should get a 403 response

  Scenario: Cannot retrieve a single note when not authenticated
    When the client sends an unauthenticated request to retrieve the 1st note
    Then The client should get a 403 response

  Scenario: Cannot overwrite a note when not authenticated
    When the client sends an unauthenticated request to update the 1st note
    Then The client should get a 403 response

  Scenario: Cannot delete a note when not authenticated
    When the client sends an unauthenticated request to delete the 1st note
    Then The client should get a 403 response

  Scenario: Cannot view account details when not authenticated
    When the client sends an unauthenticated request for account details
    Then The client should get a 403 response

  @bug
  Scenario: Cannot authenticate with a malformed bearer token
    Given an existing user account
    And the client has authenticated
    When the subject within the authentication token is altered
    And the client sends an authenticated request for account details
    Then The client should get a 403 response
