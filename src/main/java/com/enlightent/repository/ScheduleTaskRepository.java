package com.enlightent.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.enlightent.entity.ScheduleTask;

public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Long>, FindRepository<ScheduleTask> {
	List<ScheduleTask> findByStatus(Integer status);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE `schedule_task` SET lastFinishedDate =?2, findTotal = ?3, errTotal = ?4, message = ?5 WHERE id = ?1 ", nativeQuery = true)
	void updateLastDate(Long id, String lastFinishedDate, Integer findTotal, Integer errTotal, String message);
}
