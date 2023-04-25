package pojo.dto;

import constants.ResultCodeEnum;

import java.util.HashMap;

/**
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
     *通用返回成功的方法
     *
     * @return {@link ResponseResultSet}
     */
    public static ResponseResultSet success() {
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.OK.getCode());
        r.setMessage(ResultCodeEnum.OK.getMessage());
        r.setSuccess(ResultCodeEnum.OK.isSuccess());
        return r;
    }

    /**
     * 通用返回失败的方法
     *
     * @return {@link ResponseResultSet}
     */
    public static ResponseResultSet fail() {
        ResponseResultSet r = new ResponseResultSet();
        r.setCode(ResultCodeEnum.SERVER_ERROR.getCode());
        r.setMessage(ResultCodeEnum.SERVER_ERROR.getMessage());
        r.setSuccess(ResultCodeEnum.SERVER_ERROR.isSuccess());
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
