package constants;

/**
 * 角色对应权限的枚举类
 *
 * @author Secret
 * @date 2023/04/26
 */
public enum Permisssion {
//    可以查看所有用户
    VIEW_ALL_USER(1, "viewAllUser"),
    VIEW_ENTERPRISE_USER(2, "viewEnterpriseUser"),

    VIEW_ENTERPRISE_PRINTER(3, "viewEnterprisePrinter"),
    VIEW_ALL_PRINTER(4, "viewAllPrinter");
    private final int permissionId;

    private final String permissionName;

    Permisssion(int permissionId, String permissionName) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
