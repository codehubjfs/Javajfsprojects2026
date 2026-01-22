package model;

public class Role {
	
	int roleId;
	String roleName;
	
	public Role(int roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}
	
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getRoleId() {
		return this.roleId;
	}

}
