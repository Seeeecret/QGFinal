package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import utils.MyIOUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 基地servlet
 *
 * @author Secret
 * @date 2023/04/18
 */
public class BaseServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String requestContentType = request.getContentType();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String methodName = null;
        Class<? extends BaseServlet> actionClass = this.getClass();
        try {
//        如果是json格式的请求，就从请求体中获取method参数
            if (requestContentType != null && requestContentType.contains("application/json")) {
                String jsonString = MyIOUtil.toString(request.getInputStream(), "UTF-8");
                JSONObject jsonObject = JSON.parseObject(jsonString);
                methodName = jsonObject.getString("method");
                Method method = actionClass.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class, JSONObject.class);
                method.invoke(this, request, response, jsonObject);
//        如果是get格式的请求，就从请求参数中获取method参数
            } else {
                methodName = request.getParameter("method");
                if (methodName == null || methodName.isEmpty()) {
                    throw new RuntimeException("没有传入method参数");
                }
                Method method = actionClass.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(this, request, response);
            }// 通过反射调用子类的方法
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String methodName = null;
        Class<? extends BaseServlet> actionClass = this.getClass();
        String jsonString = MyIOUtil.toString(request.getInputStream(), "UTF-8");
        JSONObject jsonObject = JSON.parseObject(MyIOUtil.URLtoJson(jsonString));
        methodName = jsonObject.getString("method");
        try{
        Method method = actionClass.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class, JSONObject.class);
        method.invoke(this, request, response, jsonObject);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
