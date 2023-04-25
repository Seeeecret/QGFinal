package constants;

/**
 * 统一数据集处理需要的枚举类，届时需要根究此类构造JSON返回给前端
 *
 * @author Secret
 * @date 2023/04/25
 */
public enum ResultCodeEnum {
    // 成功
    OK(true, 200, "成功,请求正常处理并返回数据"),

    NO_CONTENT(true, 204, "成功,请求正常处理但无返回数据"),

    // 失败
    SERVER_ERROR(false, 500, "失败,服务器内部错误"),

    BAD_REQUEST(false, 400, "失败,请求参数错误"),
    UNAUTHORIZED(false, 401, "失败,请求未认证"),
    NOT_FOUND(false, 404, "失败,资源不存在或路径错误"),
    FORBIDDEN(false, 403, "失败,没有资源访问权限");

    private final boolean success;
    private final int code;
    private final String message;

    ResultCodeEnum(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public static ResultCodeEnum fromCode(int code) {
        for (ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()) {
            if (resultCodeEnum.code == code) {
                return resultCodeEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "ResultCodeEnum{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
