package model;

public class Role {
	private int _roleId;
	private String _roleName;
	
	public Role() {
		
	}
	
	public Role(String roleName) {
		this._roleName = roleName;
	}
	
	public int getRoleId() {
		return _roleId;
	}
	
	public void setRoleId(int roleId) {
		this._roleId = roleId;
	}
	
	public String getRoleName() {
		return _roleName;
	}
	
	public void setRoleName(String roleName) {
		this._roleName = roleName;
	}
}
