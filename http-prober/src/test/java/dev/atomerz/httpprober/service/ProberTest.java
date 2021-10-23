package dev.atomerz.httpprober.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.atomerz.httpprober.entity.HttpService;
import dev.atomerz.httpprober.entity.HttpService.Status;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProberTest {

  private Prober prober;

  @BeforeEach
  void beforeEach() {
    prober = new Prober();
  }

  @Test
  void testProbeServices() {
    var google = new HttpService("Google", "https://www.google.com");
    var twitter = new HttpService("Twitter", "http://twitter.com");
    var notExist = new HttpService("does not exist", "http://www.thisdoesnotexist.net/");
    var services = Arrays.asList(google, twitter, notExist);
    prober.updateStatus(services);
    assertEquals(Status.OK, google.getStatus());
    assertEquals(Status.FAIL, twitter.getStatus());
    assertEquals(Status.FAIL, notExist.getStatus());
  }

}