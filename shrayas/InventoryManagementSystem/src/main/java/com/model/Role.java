package com.model;

import com.enums.RoleType;

public class Role {
	
	private int roleId;
    private RoleType roleType;
	
	public Role() {
	}
	
	public Role(RoleType roleType) {
		this.roleType = roleType;
	}
	public int getRoleId() {
		return roleId;
	}
	
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	@Override
	public String toString() {
		return "Role [roleId=" +
	           roleId + ", roleType=" + roleType + "]";
	}
	
}
