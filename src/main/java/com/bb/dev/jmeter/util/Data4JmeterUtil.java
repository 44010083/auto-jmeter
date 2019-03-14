package com.bb.dev.jmeter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bb.dev.jmeter.model.ApiModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Data4JmeterUtil {
    /*
        功能：为了提高测试数据的通用性，将ut生成的测试数据转化为jmeter数据
        输入参数：
            String utRoot   ut测试数据的根目录，该目录下含有data子目录、module子目录
            String jmxRoot  jmeter测试数据的根目录，会在该目录下创建data子目录、module子目录
     */
    public static void getAllJmxData(String utRoot, String jmxRoot) {

        String jmxModulePath = jmxRoot + "/module";
        String jmxDataPath = jmxRoot + "/data";

        String utModulePath = utRoot + "/module";
        String utDataPath = utRoot + "/data";
        //ut自动化生成的api简要信息文件
        String apisInfoPath = utModulePath + "/apis-info.json";

        File utInfoFile = new File(apisInfoPath);
        File jmxInfoFile = new File(jmxModulePath + "/apis-info.json");
        //apis-info.json文件是能进行根据data文件自动生成测试用例文件的基础，要求必须存在这个文件
        if (utInfoFile.exists()) {
            //copy apis-info.json
            try {
                if (!jmxInfoFile.getParentFile().exists()) {
                    jmxInfoFile.getParentFile().mkdirs();
                }
                FileUtils.copyFile(utInfoFile, jmxInfoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String apisStr = FileUtils.readFileToString(utInfoFile);
                List<ApiModel> apis = JSON.parseArray(apisStr, ApiModel.class);
                for (ApiModel api : apis) {
                    //copy 模板和测试数据
                    getJmxFiles(api, jmxModulePath, utModulePath);
                    getJmxFiles(api, jmxDataPath, utDataPath);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /*
        功能：将utPath路径下的ut用例数据文件转化为jmxPath路径下的jmeter用例数据文件
              做的动作有2个：
                    1、子目录的转化（为了方便查找与解读，jmeter的用例文件所在子目录与ut采取不同的方式）
                    2、给测试数据文件增加jmeter用例需要的配置项
        输入参数：
            String utPath       ut用例数据文件目录
            String jmxPath      jmeter用例数据文件目录
            ApiModel api        API结构化数据
    */
    private static void getJmxFiles(ApiModel api, String jmxPath, String utPath) {
        String packName = api.getPackageName().replaceAll("\\.", "/");
        String fileName = api.getFileName();
        String apiType = api.getApiType();
        String apiName = api.getApiName();
        String apiPath = api.getApiPath();
        String utDataFilePath = utPath + "/" + packName;
        utDataFilePath += "/" + fileName;
        utDataFilePath += "/" + apiType + "_" + apiName;
        String jmxDataFilePath = jmxPath + "/" + apiType + "(" + apiPath.replaceAll("/", "@") + ")";
        File utDataFiles = new File(utDataFilePath);
        File jmxDataFiles = new File(jmxDataFilePath);
        //如果路径不存在就创建
        if (!jmxDataFiles.exists()) {
            jmxDataFiles.mkdirs();
        }
        for (File f : utDataFiles.listFiles()) {
            if (f.isFile() && f.getName().endsWith(".json")) {
                //增加jmx用例特有参数，写文件
                getJmxDataFile(f, jmxDataFilePath);
            }
        }
    }


    /*
        功能：jmx的测试数据文件，需要比ut的更多几个参数，这里实现添加
        输入参数：
                File file       ut测试用例文件对象
                String outPath  生成的jmeter测试数据文件保存路径
     */
    static void getJmxDataFile(File file, String outPath) {

        try {
            //将测试数据读取为字符串。FileUtils.readFileToString
            String fileStr = FileUtils.readFileToString(file);
            //转化为json格式
            JSONObject fileJson = JSON.parseObject(fileStr);

            JSONArray requestHeaders = new JSONArray();
            JSONObject requestHeader = new JSONObject();
            requestHeader.put("n", "");
            requestHeader.put("v", "");
            requestHeaders.add(requestHeader);
            fileJson.put("requestHeaders", requestHeaders);
            JSONArray assertions = new JSONArray();
            JSONObject assertion = new JSONObject();
            assertion.put("n", "Assertion.response_code");
            assertion.put("v", "200");
            assertion.put("t", "8");
            assertions.add(assertion);
            fileJson.put("assertions", assertions);
            fileJson.put("smoke", false);
            fileJson.put("stress", 1);
            fileJson.put("previous", "");
            fileJson.put("forth", "");
            //System.out.println(JSON.toJSONString(fileJson));
            String fileName = file.getName();
            String outFilePath = outPath + "/" + fileName;
            //System.out.println(outFilePath);
            FileWriter writer = new FileWriter(outFilePath);
            writer.write(JSON.toJSONString(fileJson));
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
    }

}
