package com.bb.dev.jmeter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bb.dev.jmeter.model.ArgsModel;
import com.bb.dev.jmeter.model.JmeterCaseModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Data2CasesUtil {
    /*
        功能：根据目录化的结构，获取对象数据JmeterCaseModel
        输入参数：
                File f目录路径对象化，例如：目录./jmeter/data/get(@hello)
                JSONObject data被测实例web地址解析后得到的结构化数据
         输出：
                一个测试用例的基本数据
     */

    public static JmeterCaseModel getCase(File f, JSONObject data) {
        JmeterCaseModel jmxCase = new JmeterCaseModel();
        if (f.isDirectory() && f.getName().endsWith(")") && f.getName().contains("(")) {
            //获得当前目录名，例如：get(@hello)
            String fName = f.getName();
            String method = "";
            if (fName.startsWith("get")) {
                method = "get";
            }
            if (fName.startsWith("post")) {
                method = "post";
            }
            if (fName.startsWith("put")) {
                method = "put";
            }
            if (fName.startsWith("delete")) {
                method = "delete";
            }
            jmxCase.setMethod(method);
            if (!"".equalsIgnoreCase(method) && fName.startsWith(method)) {
                String path = fName.substring(method.length());
                path = path.replaceAll("@", "/");
                path = path.replaceAll("\\(", "");
                path = path.replaceAll("\\)", "");

                jmxCase.setDomain(data.getString("domain"));
                jmxCase.setPath(data.getString("path") + path);
                jmxCase.setPort(data.getInteger("port"));
                jmxCase.setProtocol(data.getString("protocol"));
                jmxCase.setEncoding("UTF-8");
            }

        }
        return jmxCase;
    }

    /*
     功能：根据目测试数据文件，获取对象数据JmeterCaseModel
     输入参数：
             File ff测试数据文件对象化
             JmeterCaseModel jmxCase执行getCase(File f, JSONObject data)获得的测试用例基本数据
      输出：
             一个测试用例的完善数据
  */
    public static JmeterCaseModel getCase(File ff, JmeterCaseModel jmxCase) {
        if (jmxCase == null || jmxCase.getPath() == null || "".equalsIgnoreCase(jmxCase.getPath())) {
            return jmxCase;
        }
        String ffName = ff.getName();
        if (ffName.endsWith(".json")) {
            try {

                //System.out.println(ff.getPath());
                //将测试数据读取为字符串。FileUtils.readFileToString
                String fileStr = FileUtils.readFileToString(ff);
                //转化为json格式
                JSONObject fileJson = JSON.parseObject(fileStr);
                //输入参数
                jmxCase.setArgsStr(fileJson.getString("in"));
                jmxCase.setArgs(JSON.parseArray(fileJson.getString("in"), ArgsModel.class));
                //断言
                jmxCase.setAssertions(JSON.parseArray(fileJson.getString("assertions"), ArgsModel.class));
                //请求头
                jmxCase.setRequestHeaders(JSON.parseArray(fileJson.getString("requestHeaders"), ArgsModel.class));
                //testAfter用例
                jmxCase.setForth(fileJson.getString("forth"));
                //testABefore用例
                jmxCase.setPrevious(fileJson.getString("previous"));
                //压力测试值（大于1时候表示筛选为压力测试用例）
                jmxCase.setStress(fileJson.getInteger("stress"));
                //是否筛选为冒烟测试用例，true表示筛选为冒烟测试用例
                jmxCase.setSmoke(fileJson.getBoolean("smoke"));
                //唯一化ID
                jmxCase.setId(jmxCase.getMethod() + "_" + jmxCase.getPath() + "_" + ffName.substring(0, ffName.length() - 5));
                //用例名称
                jmxCase.setName(ffName.substring(0, ffName.length() - 5));


                //jmxCase.setAssertions();
                return jmxCase;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return jmxCase;
    }
    /*
     功能：根据被测实例baseWebUrl转化为结构化数据
     输入参数：
             String baseWebUrl  被测实例的web地址
      输出：
             web地址结构化数据
             {
                "protocol":"",
                "domain":"",
                "path":"",
                "port":80
              }
    */

    public static JSONObject getPathData(String baseWebUrl) {

        JSONObject path = new JSONObject();
        if (baseWebUrl != null || !"".equalsIgnoreCase(baseWebUrl)) {
            baseWebUrl = baseWebUrl.trim();
            int port = 80;
            if (baseWebUrl.startsWith("http://")) {
                baseWebUrl = baseWebUrl.substring(7);
                path.put("protocol", "http");
            } else if (baseWebUrl.startsWith("https://")) {
                port = 443;
                baseWebUrl = baseWebUrl.substring(8);
                path.put("protocol", "https");
            } else {
                return path;
            }
            String hostsWithPort = "";
            String uriPath = "";
            if (baseWebUrl.contains("/")) {
                hostsWithPort = baseWebUrl.substring(0, baseWebUrl.indexOf("/"));
                uriPath = baseWebUrl.substring(hostsWithPort.length());
            } else {
                hostsWithPort = baseWebUrl;
            }
            String domain = "";
            if (hostsWithPort.contains(":")) {
                String[] hp = hostsWithPort.split(":");
                if (hp.length == 2) {
                    port = Integer.parseInt(hp[1]);
                    domain = hp[0];
                } else {
                    return path;
                }
            } else {
                domain = hostsWithPort;
            }
            path.put("port", port);
            path.put("domain", domain);
            if ("".equalsIgnoreCase(uriPath)) {
                path.put("path", uriPath);
            } else if ("/".equalsIgnoreCase(uriPath)) {
                path.put("path", "");
            } else {
                path.put("path", uriPath);
            }

        }
        return path;
    }

    /*
        功能：根据输入的用例字符串casesStr（多个用例用逗号分隔），获得用例list
        输入参数：
            String dependCasesStr   用例字符串casesStr（多个用例用逗号分隔）
            String dataPath         用例文件data目录
            JSONObject uriJson       被测实例webUrl转化后获得的结构化数据
     */
    static List<JmeterCaseModel> getDependCases(JSONObject uriJson, String dataPath, String dependCasesStr) {

        List<JmeterCaseModel> cases = new ArrayList<JmeterCaseModel>();
        if (dependCasesStr == null || dataPath == null || uriJson == null) {
            return cases;
        }
        String[] casesStr = dependCasesStr.split(",");
        for (String caseStr : casesStr) {
            File ff = new File(dataPath + "/" + caseStr);
            if (ff.exists() && !"".equalsIgnoreCase(caseStr)) {
                String[] casePathAndName = caseStr.split("/");
                if (casePathAndName.length == 2) {
                    File f = new File(dataPath + "/" + casePathAndName[0]);
                    if (f.exists()) {
                        JmeterCaseModel baseCase = Data2CasesUtil.getCase(f, uriJson);
                        JmeterCaseModel thisCase = Data2CasesUtil.getCase(ff, baseCase);
                        cases.add(thisCase);
                    }
                }
            }
        }
        return cases;
    }
}
