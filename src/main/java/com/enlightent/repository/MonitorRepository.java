package com.enlightent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enlightent.entity.Monitor;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long>, FindRepository<Monitor> {
	@Query(value = "select barrageFrequency from monitor where videoName=?1 and channelType =?2  ORDER BY barrageFrequency desc limit 1", nativeQuery = true)
	Object findBarrageFrequency(String name,String channelType);
	
	@Query(value = "select * from monitor where status=1 and toDate> now()", nativeQuery = true)
	List<Monitor> findNormalMonitor();
	
}
