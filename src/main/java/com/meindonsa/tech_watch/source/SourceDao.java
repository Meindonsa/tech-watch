package com.meindonsa.tech_watch.source;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SourceDao extends JpaRepository<Source, Long> {

    Source findByUrl(String url);

    Source findByFid(String fid);

    List<Source> findByActive(boolean active);

    @Query("SELECT s FROM Source s WHERE :key IS NULL OR LOWER(s.name) LIKE  CONCAT('%',:key,'%')")
    Page<Source> search(String key, Pageable pageable);
}
