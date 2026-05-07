package com.example.transactional_service.outbox;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    @Query(value = """
      select *from outbox o 
           where o.status=:status
     """, nativeQuery = true)
    List<Outbox> findAllByStatus(Pageable pageable, String status);
}
