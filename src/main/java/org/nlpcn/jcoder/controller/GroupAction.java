package org.nlpcn.jcoder.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.nlpcn.jcoder.domain.Group;
import org.nlpcn.jcoder.domain.Task;
import org.nlpcn.jcoder.domain.UserGroup;
import org.nlpcn.jcoder.filter.AuthoritiesManager;
import org.nlpcn.jcoder.service.GroupService;
import org.nlpcn.jcoder.service.TaskService;
import org.nlpcn.jcoder.util.Restful;
import org.nlpcn.jcoder.util.StaticValue;
import org.nlpcn.jcoder.util.dao.BasicDao;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IocBean
@Filters(@By(type = AuthoritiesManager.class, args = { "userType", "1", "/login.html" }))
@Ok("json")
public class GroupAction {

	private static final Logger LOG = LoggerFactory.getLogger(GroupAction.class);

	@Inject
	private GroupService groupService ;

	public BasicDao basicDao = StaticValue.systemDao;

	@Inject
	private TaskService taskService;

	@At("/auth/delUserGroup")
	@Ok("raw")
	@Fail("jsp:/fail.jsp")
	public boolean delUserGroup(@Param("id") long id) throws Exception {

		List<Task> tasksList = taskService.tasksList(id); // 从数据库中查出，不污染内存中的task

		for (Task task : tasksList) {
			taskService.delete(task);
			taskService.delByDB(task);
		}

		boolean flag = basicDao.delById(id, UserGroup.class);
		if (flag) {
			LOG.info("add userGroup which id:" + id);
		}
		return flag;
	}

	@At("/auth/updateUserGroup")
	@Ok("raw")
	public boolean updateUserGroup(@Param("groupId") Long groupId, @Param("auth") Integer auth, @Param("userId") Long userId) throws Exception {
		Condition con = Cnd.where("groupId", "=", groupId).and("userId", "=", userId);
		UserGroup userGroup = basicDao.findByCondition(UserGroup.class, con);
		if (userGroup == null) {
			userGroup = new UserGroup();
			userGroup.setUserId(userId);
			userGroup.setGroupId(groupId);
			userGroup.setCreateTime(new Date());
		}

		if (auth == 0) {
			basicDao.delById(userGroup.getId(), UserGroup.class);
		} else {
			userGroup.setAuth(auth);
			if (userGroup.getId() == null) {
				basicDao.save(userGroup);
			} else {
				basicDao.update(userGroup);
			}
		}

		return true;
	}

	/**
	 * 列出用户的所有权限
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@At("/auth/authUser")
	@Ok("json")
	public List<UserGroup> authUser(@Param("userId") long userId) throws Exception {
		return basicDao.search(UserGroup.class, Cnd.where("userId", "=", userId));
	}


	@At("/group/nameDiff")
	@Ok("raw")
	public boolean groupNameDiff(@Param("name") String name) {
		Condition con = Cnd.where("name", "=", name);
		int count = basicDao.searchCount(Group.class, con);
		return count == 0;
	}



	@At("/admin/group/add")
	public Restful addG(@Param("..") Group group) throws Exception {
		if (groupNameDiff(group.getName())) {
			groupService.save(group) ;
		}
		return Restful.OK.msg("添加成功！");
	}

	@At("/admin/group/del")
	public Restful delG(@Param("..") Group group) {
		groupService.delete(group);
		/*List<Group> groups = (List<Group>) Mvcs.getHttpSession().getAttribute("GROUP_LIST");
		Iterator<Group> it = groups.iterator();
		while (it.hasNext()) {
			Group g = it.next();
			if (g.getId() == group.getId()) {
				it.remove();
			}
		}
		Mvcs.getHttpSession().setAttribute("GROUP_LIST", groups);*/
		return Restful.OK.msg("删除成功！");
	}

	@At("/admin/group/modify")
	public Restful modifyG(@Param("..") Group group) throws Exception {
		if (group == null) {
			return Restful.ERR.msg("修改失败！");
		}
		boolean flag = basicDao.update(group);
		if (flag) {
			LOG.info("modify group:" + group.toString());
		}
		return Restful.OK.msg("修改成功！");
	}
}
