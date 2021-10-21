package dev.atomerz.httpprober.controller;

import dev.atomerz.httpprober.entity.HttpService;
import dev.atomerz.httpprober.repository.HttpServiceRepository;
import java.util.List;
import java.util.Optional;
import javax.print.attribute.standard.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpProberController {

  @Autowired
  HttpServiceRepository repository;

  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public List<HttpService> list() {
    return repository.findAll();
  }

  @GetMapping(
      path = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Optional<HttpService> get(@PathVariable long id) {
    return repository.findById(id);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public HttpService create(@RequestBody HttpService service) {
    return repository.save(service);
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public HttpService update(@RequestBody HttpService service) {
    return repository.save(service);
  }

  @DeleteMapping(
      path = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public void delete(@PathVariable long id) {
    repository.deleteById(id);
  }

}
