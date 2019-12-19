package com.enjoyball.config;

import com.enjoyball.appointment.controller.AppointmentController;
import com.enjoyball.contest.controller.ContestController;
import com.enjoyball.entity.ApplyInfo;
import com.enjoyball.entity.Collection;
import com.enjoyball.entity.Comment;
import com.enjoyball.entity.Contest;
import com.enjoyball.entity.DTeamMember;
import com.enjoyball.entity.DemandInfo;
import com.enjoyball.entity.News;
import com.enjoyball.entity.Team;
import com.enjoyball.entity.TeamDemand;
import com.enjoyball.entity.TeamMember;
import com.enjoyball.entity.User;
import com.enjoyball.entity.UserFans;
import com.enjoyball.information.contorller.Information;
import com.enjoyball.news.controller.NewsController;
import com.enjoyball.team.controller.TeamController;
import com.enjoyball.user.controller.UserController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

public class AppConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(true);
		me.setBaseUploadPath("/img");
	}

	@Override
	public void configRoute(Routes me) {
		// 设置控制器映射路径
		me.add("news",NewsController.class);
		me.add("user",UserController.class);
		me.add("information",Information.class);
		me.add("team",TeamController.class);
		me.add("contest",ContestController.class);
		me.add("appointment",AppointmentController.class);
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		DruidPlugin dp = new DruidPlugin("jdbc:mysql://localhost/enjoyball_db?characterEncoding=utf8", "root", "");
		me.add(dp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
	    me.add(arp);
	    arp.addMapping("news_info","news_id", News.class);
	    arp.addMapping("user_info","user_id",User.class);
	    arp.addMapping("user_fans", "user_fansid",UserFans.class);
	    arp.addMapping("comment_info","comment_id", Comment.class);
	    arp.addMapping("team_info", "team_id",Team.class);
	    arp.addMapping("team_member", "team_memberid",TeamMember.class);
	    arp.addMapping("dteam_member", DTeamMember.class);
	    arp.addMapping("demand_info", "demand_id",DemandInfo.class);
	    arp.addMapping("demand_team", TeamDemand.class);
	    arp.addMapping("user_collection", "collection_id",Collection.class);
	    arp.addMapping("game_info", "game_id", Contest.class);
	    arp.addMapping("apply_info", ApplyInfo.class);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}

}
