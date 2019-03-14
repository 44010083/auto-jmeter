package com.bb.dev.jmeter.util;

import com.alibaba.fastjson.JSONObject;
import com.bb.dev.jmeter.model.JmeterCaseModel;

import java.io.File;

public class Data2FilesUtil {
    /*
        功能：获得指定目录下的jmeter测试数据文件，生成jmx用例文件
        输入参数：
                String rootPath     jemeter工作根目录，含有data子目录，并且data子目录含有jmeter测试数据文件
                String baseWebUrl   被测实例的web地址
                boolean reWrite     遇到同名jmx测试文件是否允许覆盖重写
     */
    public static void saveFiles(String rootPath, String baseWebUrl, boolean reWrite) {
        String dataPath = rootPath + "/data";
        JmeterFileUtil.savePom(rootPath + "/pom.xml");
        JSONObject setDataJson = Data2CasesUtil.getPathData(baseWebUrl);
        File dataFile = new File(dataPath);
        File[] dataFiles = dataFile.listFiles();
        if (dataFiles != null) {
            for (File f : dataFiles) {
                if (f.isDirectory() && f.getName().endsWith(")") && f.getName().contains("(")) {
                    File[] fFiles = f.listFiles();
                    for (File ff : fFiles) {
                        //获得用例
                        JmeterCaseModel baseCase = Data2CasesUtil.getCase(f, setDataJson);
                        JmeterCaseModel thisCase = Data2CasesUtil.getCase(ff, baseCase);
                        //获得依赖用例
                        if (thisCase != null && thisCase.getName() != null && !"".equalsIgnoreCase(thisCase.getName())) {
                            thisCase.setPreCases(Data2CasesUtil.getDependCases(setDataJson, dataPath, thisCase.getPrevious()));
                            thisCase.setProCases(Data2CasesUtil.getDependCases(setDataJson, dataPath, thisCase.getForth()));
                            JmeterFileUtil.save3File(thisCase, rootPath, reWrite);
                        }
                    }
                }
            }
        }

    }

}
