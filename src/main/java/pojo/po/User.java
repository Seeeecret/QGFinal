package pojo.po;

import constants.Role;

/**
 * @author Secret
 */
public class User {
   private  Integer userId;
   private String username;
   private String password;
   private Role role;
    private Integer parentId;
    private String jsonInfo;


    public User() {
    }

    public User(Integer userId, String username, String password, Role role, Integer parentId, String jsonInfoString) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.parentId = parentId;
        this.jsonInfo = jsonInfoString;
    }
    public User(String username, String password, Role role, Integer parentId, String jsonInfoString) {
        this.userId = null;
        this.username = username;
        this.password = password;
        this.role = role;
        this.parentId = parentId;
        this.jsonInfo = jsonInfoString;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getJsonInfo() {
        return jsonInfo;
    }

    public void setJsonInfo(String jsonInfo) {
        this.jsonInfo = jsonInfo;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
