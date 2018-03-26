package com.enlightent.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enlightent.entity.ScheduleTaskInfo;

@Repository
public interface ScheduleTaskInfoRepository extends JpaRepository<ScheduleTaskInfo, Long> {

	List<ScheduleTaskInfo> findByTaskId(Long taskId, Pageable pageable);
	
}
