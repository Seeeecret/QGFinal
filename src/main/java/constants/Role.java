package constants;

/**
 * 角色枚举类
 *
 * @author Secret
 * @date 2023/04/26
 */
public enum Role {
    // 管理员
    ADMIN(1, "admin"),
    // 企业用户
    ENTERPRISE_USER(2, "enterpriseUser"),
    // 普通用户
    ORDINARY_USER(3, "ordinaryUser");

    public int getRoleId() {
        return roleId;
    }

    private final int roleId;

    private final String roleName;

    Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public static Role fromRoleId(int roleId) {
        for (Role role : Role.values()) {
            if (role.roleId == roleId) {
                return role;
            }
        }
        return null;
    }

    public String getRoleName() {
        return roleName;
    }
}
