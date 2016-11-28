package com.tiyujia.homesport.entity;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;
import com.tiyujia.homesport.util.Convert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class JsonConvert<T> implements Converter<T> {

    private Type type;

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public T convertSuccess(Response response) throws Exception {
        JsonReader jsonReader = new JsonReader(response.body().charStream());

        if (type == null) {
            //以下代码是通过泛型解析实际参数,泛型必须传
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            type = params[0];
        }
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
        Type rawType = ((ParameterizedType) type).getRawType();
        //无数据类型
        if (rawType == Void.class) {
            SimpleResponse baseWbgResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
            //noinspection unchecked
            return (T) baseWbgResponse.toLzyResponse();
        }
        //有数据类型
        if (rawType == LzyResponse.class) {
            LzyResponse lzyResponse = Convert.fromJson(jsonReader, type);
            int state = lzyResponse.state;
            if (state == 0) {
                //noinspection unchecked
                return (T) lzyResponse;
            } else if (state == 40003) {
                throw new IllegalStateException("手机号码未注册");
            } else if (state == 40301) {
                throw new IllegalStateException("用户ID与用户不匹配，查询失败！！！");
            } else if (state == 401) {
                throw new IllegalStateException("地址参数错误");
            } else if (state == 300) {
                throw new IllegalStateException("其他乱七八糟的等");
            } else {
                throw new IllegalStateException("错误代码：" + state + "，错误信息：" + lzyResponse.successmsg);
            }
        }
        throw new IllegalStateException("基类错误无法解析!");
    }
}