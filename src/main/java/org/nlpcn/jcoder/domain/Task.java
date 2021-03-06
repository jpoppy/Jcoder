package org.nlpcn.jcoder.domain;

import java.util.Date;

import org.nlpcn.jcoder.util.StringUtil;
import org.nlpcn.jcoder.run.java.JavaSourceUtil;
import org.nlpcn.jcoder.util.StaticValue;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("task")
public class Task {

	@Id
	private Long id;

	@Column
	private String name;

	@Column
	private String description;

	// while 至少有一台机器保持运行
	@Column("schedule_str")
	private String scheduleStr;

	@Column
	private String code;

	@Column("group_id")
	private Long groupId;

	@Column("create_user")
	private String createUser;

	@Column("update_user")
	private String updateUser;

	@Column("create_time")
	private Date createTime;

	@Column("update_time")
	private Date updateTime;

	@Column("version")
	private String version;

	private String groupName ;

	private String runStatus;
	
	private CodeInfo codeInfo = new CodeInfo();

	// <option value=1>Api</option>
	// <option value=2>Cron</option>
	@Column("type")
	private Integer type;

	// 任务状态 0,停止 1運行
	@Column("status")
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.name = JavaSourceUtil.findClassName(code);
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getScheduleStr() {
		return scheduleStr;
	}

	public void setScheduleStr(String scheduleStr) {
		this.scheduleStr = scheduleStr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}


	public long success() {
		return StaticValue.space().getSuccess(this.getId());
	}

	
	public long error() {
		return StaticValue.space().getError(this.getId());
	}

	public void updateError() {
		StaticValue.space().counter(this.getId(),false);
	}

	public void updateSuccess() {
		StaticValue.space().counter(this.getId(),true);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public CodeInfo codeInfo() {
		return codeInfo;
	}

	public void setName(String name) {
		this.name = name ;
	}

	public String getGroupName(){
		if(groupName==null) {
			Group group = StaticValue.systemDao.find(groupId, Group.class);
			this.groupName = group.getName();
		}
		return groupName ;
	}
}
