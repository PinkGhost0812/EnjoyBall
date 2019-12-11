package com.example.lenovo.Util;

import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.User;

public class ApplyUtil {
	private User user;
	private DemandInfo demand;
	private ApplyInfo applyInfo;
	
	public ApplyUtil() {}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public DemandInfo getDemand() {
		return demand;
	}
	public void setDemand(DemandInfo demand) {
		this.demand = demand;
	}
	public ApplyInfo getApplyInfo() {
		return applyInfo;
	}
	public void setApplyInfo(ApplyInfo applyInfo) {
		this.applyInfo = applyInfo;
	}
	@Override
	public String toString() {
		return "ApplyUtil [user=" + user + ", demand=" + demand + ", applyInfo=" + applyInfo + "]";
	}
	public ApplyUtil(User user, DemandInfo demand, ApplyInfo applyInfo) {
		super();
		this.user = user;
		this.demand = demand;
		this.applyInfo = applyInfo;
	}
	
	
}
