package dev.atomerz.httpprober.repository;

import dev.atomerz.httpprober.entity.HttpService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpServiceRepository extends JpaRepository<HttpService, Long> {

}
