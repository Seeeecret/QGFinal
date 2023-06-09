package pojo.dto;

import constants.ResultCodeEnum;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 响应结果集
 * JSON响应的统一结果集类，通过ResultCodeEnum类构造JSON返回给前端
 *
 * @author Secret
 * @date 2023/04/25
 */
public class ResponseResultSet {
    private boolean success;
    private int code;
    private String message;
    private HashMap<String, Object> data = new HashMap<>();

    private ResponseResultSet() {
    }

    /**
     *通用的返回成功结果集的方法
     *
     * @return {@link ResponseResultSet}
     */
    public static ResponseResultSet success(HttpServletResponse response) {
        response.setStatus(200);
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.OK.getCode());
        r.setMessage(ResultCodeEnum.OK.getMessage());
        r.setSuccess(ResultCodeEnum.OK.isSuccess());
        return r;
    }

    public static ResponseResultSet partialContent(HttpServletResponse response) {
        response.setStatus(206);
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.PARTIAL_CONTENT.getCode());
        r.setMessage(ResultCodeEnum.PARTIAL_CONTENT.getMessage());
        r.setSuccess(ResultCodeEnum.PARTIAL_CONTENT.isSuccess());
        return r;
    }

    /**
     * 通用的返回服务器错误结果集的方法
     *
     * @return {@link ResponseResultSet}
     */
    public static ResponseResultSet error(HttpServletResponse response) {
        response.setStatus(500);
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.SERVER_ERROR.getCode());
        r.setMessage(ResultCodeEnum.SERVER_ERROR.getMessage());
        r.setSuccess(ResultCodeEnum.SERVER_ERROR.isSuccess());
        return r;
    }

    /**
     * 通用的返回请求参数错误结果集的方法
     *
     * @return {@link ResponseResultSet}
     */
    public static ResponseResultSet fail(HttpServletResponse response) {
        response.setStatus(400);
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.BAD_REQUEST.getCode());
        r.setMessage(ResultCodeEnum.BAD_REQUEST.getMessage());
        r.setSuccess(ResultCodeEnum.BAD_REQUEST.isSuccess());
        return r;
    }

    public static ResponseResultSet unauthorized(HttpServletResponse response) {
        response.setStatus(401);
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.UNAUTHORIZED.getCode());
        r.setMessage(ResultCodeEnum.UNAUTHORIZED.getMessage());
        r.setSuccess(ResultCodeEnum.UNAUTHORIZED.isSuccess());
        return r;
    }

    public static ResponseResultSet forbidden(HttpServletResponse response) {
        response.setStatus(403);
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.FORBIDDEN.getCode());
        r.setMessage(ResultCodeEnum.FORBIDDEN.getMessage());
        r.setSuccess(ResultCodeEnum.FORBIDDEN.isSuccess());
        return r;
    }

    public static ResponseResultSet notFound(HttpServletResponse response) {
        response.setStatus(404);
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.NOT_FOUND.getCode());
        r.setMessage(ResultCodeEnum.NOT_FOUND.getMessage());
        r.setSuccess(ResultCodeEnum.NOT_FOUND.isSuccess());
        return r;
    }
    /**
     * 根据结果的枚举类返回结果
     *
     * @param resultCodeEnum 枚举结果代码
     * @return {@link ResponseResultSet}
     */
    public static ResponseResultSet fromResultCodeEnum(ResultCodeEnum resultCodeEnum) {
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMessage());
        r.setSuccess(resultCodeEnum.isSuccess());
        return r;
    }

    /**以下是实现本类链式编程的五个方法，返回值均为类本身
     * 增加一个键值对数据
     *
     * @param key   键
     * @param value 值
     * @return {@link ResponseResultSet}
     */
    public ResponseResultSet data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    /**
     * 修改data的Map为传入的Map
     *
     * @param dataMap 数据地图
     * @return {@link ResponseResultSet}
     */
    public ResponseResultSet data(HashMap<String, Object> dataMap) {
        this.setData(dataMap);
        return this;
    }

    public ResponseResultSet message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResponseResultSet code(int code) {
        this.setCode(code);
        return this;
    }

    public ResponseResultSet success(boolean success) {
        this.setSuccess(success);
        return this;
    }
    public boolean isSuccess() {
        return success;
    }

    private void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    private void setData(HashMap<String, Object> data) {
        this.data = data;
    }
}
