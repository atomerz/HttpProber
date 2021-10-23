package dev.atomerz.httpprober.service;

import dev.atomerz.httpprober.repository.HttpServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProberService {

  private static Logger logger = LoggerFactory.getLogger(Prober.class);

  @Autowired
  private Prober prober;
  @Autowired
  private HttpServiceRepository repository;

  @Scheduled(fixedDelay = 10_000)
  public void update() {
    logger.info("Updating status.");

    var services = repository.findAll();
    prober.updateStatus(services);
    repository.saveAll(services);

    logger.info("Status updated.");
  }

}
