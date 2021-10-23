package dev.atomerz.httpprober.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class HttpService {

  @Id
  @GeneratedValue
  private Long id;
  @Column(unique = true)
  private String name;
  @Column(nullable = false)
  private String url;
  @CreationTimestamp
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date creationDate;
  @Column(nullable = false)
  private Status status;

  public HttpService() {
    this.status = Status.UNKNOWN;
  }

  public HttpService(String name, String url) {
    this();
    this.name = name;
    this.url = url;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HttpService that = (HttpService) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(url, that.url) &&
        Objects.equals(creationDate, that.creationDate) &&
        status == that.status;
  }

  @Override
  public String toString() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public static enum Status {
    UNKNOWN,
    OK,
    FAIL;
  }

}
