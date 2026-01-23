package enums;

public enum RoleType {

    SYSTEM_ADMIN(1),
    FRONT_OFFICER(2),
    CUSTOMER(3);

    private int _roleId;

    RoleType(int roleId) {
        this._roleId = roleId;
    }

    public int getRoleId() {
        return _roleId;
    }
}
