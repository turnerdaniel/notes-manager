Feature: Validate API can respond to successful requests

  Scenario: Add a new note
    When The client sends a POST request to /v1/notes with the body:
      """
      {"title": "Title", "description": "Description"}
      """
    Then The client should get a 201 response
    And The response should contain a location header ending with /v1/notes/1
    And The response should contain a note with the title of "Title"
    And The response should contain a note with the description of "Description"

  Scenario: Retrieve all previously created notes
    Given The client has successfully created 2 notes
    When The client sends a GET request to /v1/notes
    Then The client should get a 200 response
    And The response should contain 2 notes

  Scenario: Retrieve a previously created note
    Given The client sends a POST request to /v1/notes with the body:
      """
      {"title": "Title", "description": "Description"}
      """
    When The client sends a GET request to /v1/notes/1
    Then The client should get a 200 response
    And The response should contain a note with the title of "Title"
    And The response should contain a note with the description of "Description"

  Scenario: Overwrite a previously created note
    Given The client has successfully created 3 notes
    When The client sends a PUT request to /v1/notes/2 with the body:
    """
    {"description": "New Description"}
    """
    Then The client should get a 200 response
    And The response should contain a note with the title of ""
    And The response should contain a note with the description of "New Description"
    And The response should contain a note with an ID of 2

  Scenario: Delete a previously created note
    Given The client has successfully created 3 notes
    When The client sends a DELETE request to /v1/notes/3
    Then The client should get a 204 response
