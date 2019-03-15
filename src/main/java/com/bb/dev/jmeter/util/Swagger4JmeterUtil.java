package com.bb.dev.jmeter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bb.dev.jmeter.model.ArgsModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Swagger4JmeterUtil {
    /*
        功能：为了提高测试数据的通用性，将swagger格式的api定义存成文件，在这里转化为jmeter模板数据
        输入参数：
            File swaggerFile    swagger格式的api定义存成文件
            String jmxRoot      jmeter测试数据的根目录，会在该目录下module子目录
     */
    public static void getAllJmxData(File swaggerFile, String jmxRoot) throws IOException {

        String jmxModulePath = jmxRoot + "/module";
        String swaggerStr = FileUtils.readFileToString(swaggerFile);
        JSONObject swaggerJson = JSONObject.parseObject(swaggerStr);
        JSONObject paths = swaggerJson.getJSONObject("paths");
        for (Map.Entry<String, Object> path : paths.entrySet()) {
            String pathName = path.getKey();
            JSONObject pathApis = JSONObject.parseObject(path.getValue().toString());
            for (Map.Entry<String, Object> api : pathApis.entrySet()) {
                saveApiModuleFile(pathName, api, jmxModulePath);
            }
        }

    }

    /*
        功能：根据具体的API定义，输出jmx测试数据模板文件
        输入参数：
            String pathName                     api接口的path，例如：/hello
            Map.Entry<String, Object> api       API定义
            String modulePath                   输出模板文件的存放路径
     */
    private static void saveApiModuleFile(String pathName, Map.Entry<String, Object> api, String modulePath) throws IOException {
        String apiType = api.getKey();
        JSONObject apiJson = JSONObject.parseObject(api.getValue().toString());
        String moduleFile = modulePath + "/" + apiType + "(" + pathName.replaceAll("/", "@") + ")/module.json";
        //模板初始化格式
        String moduleStr = "{\"stress\":1,\"requestHeaders\":[{\"v\":\"\",\"n\":\"\"}],\"previous\":\"\",\"smoke\":false,\"assertions\":[{\"t\":\"8\",\"v\":\"200\",\"n\":\"Assertion.response_code\"}],\"forth\":\"\",\"out\":\"\"}";

        JSONObject module = JSONObject.parseObject(moduleStr);
        if (apiJson.get("parameters") != null) {
            module.put("in", getApiInput(apiJson.getJSONArray("parameters")));
        }
        File file = new File(moduleFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileWriter writer = new FileWriter(moduleFile);
        writer.write(JSON.toJSONString(module));
        writer.flush();
        writer.close();
    }

    /*
        功能：将swagger定义的API输入参数转化为jmx的输入参数
        输入参数：
            JSONArray parameters    swagger定义的API输入参数
     */
    private static List<ArgsModel> getApiInput(JSONArray parameters) {
        List<ArgsModel> args = new ArrayList<ArgsModel>();
        for (Object parameter : parameters) {
            JSONObject parameterJson = (JSONObject) parameter;
            ArgsModel thisArg = new ArgsModel();
            //参数名称
            thisArg.setN(parameterJson.getString("name"));
            //参数在API上的作用类型，例如：body、path
            thisArg.setT(parameterJson.getString("in"));
            //获得参数的数据类型
            if (parameterJson.get("type") != null) {
                thisArg.setV(parameterJson.getString("type"));
            } else if (parameterJson.get("schema") != null) {
                try {
                    if (parameterJson.getJSONObject("schema").get("$ref") != null) {
                        thisArg.setV(parameterJson.getJSONObject("schema").getString("$ref").replaceAll("#/definitions/", ""));
                    } else if (parameterJson.getJSONObject("schema").get("type") != null) {
                        thisArg.setV(parameterJson.getJSONObject("schema").getString("type"));
                    }
                } catch (Exception e) {

                }
            }
            args.add(thisArg);
        }
        return args;
    }

}
