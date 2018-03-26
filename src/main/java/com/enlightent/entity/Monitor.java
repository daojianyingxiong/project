package com.enlightent.entity;
 
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
 
@Table(name = "monitor")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(value = { "createTime", "users"})
public class Monitor implements Serializable {
 
    private static final long serialVersionUID = -3080983858162628456L;
 
    @Id
    @GeneratedValue
    private Long id;
 
    private String videoName;
 
    private String channelType;
    
    @OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="monitor") 
    private List<MonitorChannel> monitorChannels;
        
    private Integer barrageFrequency = null;
    
    private Integer status;
    
    public Integer getStatus() {
        return status;
    }
 
    public void setStatus(Integer status) {
        this.status = status;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getVideoName() {
        return videoName;
    }
 
    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
 
    public String getChannelType() {
        return channelType;
    }
 
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
 
    public List<MonitorChannel> getMonitorChannels() {
        return monitorChannels;
    }
 
    public void setMonitorChannels(List<MonitorChannel> monitorChannels) {
        this.monitorChannels = monitorChannels;
    }
 
    public Integer getBarrageFrequency() {
        return barrageFrequency;
    }
 
    public void setBarrageFrequency(Integer barrageFrequency) {
        this.barrageFrequency = barrageFrequency;
    }
    
}