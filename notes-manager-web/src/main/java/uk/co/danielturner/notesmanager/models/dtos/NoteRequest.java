package uk.co.danielturner.notesmanager.models.dtos;

public class NoteRequest {

  private String title;
  private String description;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public static final class Builder {

    private String title;
    private String description;

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public NoteRequest build() {
      NoteRequest request = new NoteRequest();
      request.setTitle(title);
      request.setDescription(description);
      return request;
    }
  }
}
