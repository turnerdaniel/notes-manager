package uk.co.danielturner.notesmanager.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "NOTES")
public class Note {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column
  private String title;

  @Column
  private String description;

  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date updatedAt;

  public Note() {
    this.title = "";
    this.description = "";
  }

  public Note(String title, String description) {
    Date now = new Date();
    setTitle(title);
    setDescription(description);
    this.createdAt = now;
    this.updatedAt = now;
  }

  public long getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = (title == null) ? "" : title;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = (description == null) ? "" : description;
  }

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public Date getUpdatedAt() {
    return this.updatedAt;
  }
}
