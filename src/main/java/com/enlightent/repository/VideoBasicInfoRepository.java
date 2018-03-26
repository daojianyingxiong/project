package com.enlightent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enlightent.entity.VideoBasicInfo;

@Repository
public interface VideoBasicInfoRepository extends JpaRepository<VideoBasicInfo, Long>, FindRepository<VideoBasicInfo> {
	
	List<VideoBasicInfo> findTop100ByNameLikeAndChannelType(String name, String channelType);
	
	VideoBasicInfo findOneByNameAndChannelType(String name, String channelType);
}
