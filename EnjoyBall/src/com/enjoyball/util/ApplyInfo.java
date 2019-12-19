package com.enjoyball.util;

public class ApplyInfo {
    private Integer id;
    private Integer sender;
    private Integer receiver;
    private Integer teamId;
    private Integer demandId;
    private Integer isInvite;
    private Integer handle;

    public ApplyInfo() {}

    public Integer getHandle() {
        return handle;
    }

    public void setHandle(Integer handle) {
        this.handle = handle;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getDemandId() {
        return demandId;
    }

    public void setDemandId(Integer demandId) {
        this.demandId = demandId;
    }

    public Integer getIsInvite() {
        return isInvite;
    }

    public void setIsInvite(Integer isInvite) {
        this.isInvite = isInvite;
    }

    

    @Override
	public String toString() {
		return "ApplyInfo [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", teamId=" + teamId
				+ ", demandId=" + demandId + ", isInvite=" + isInvite + ", handle=" + handle + "]";
	}

	public ApplyInfo(Integer id, Integer sender, Integer receiver, Integer teamId, Integer demandId, Integer isInvite) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.teamId = teamId;
        this.demandId = demandId;
        this.isInvite = isInvite;
    }
}
