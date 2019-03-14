package com.bb.dev.jmeter;


import com.alibaba.fastjson.JSONObject;
import com.bb.dev.jmeter.util.Data2CasesUtil;
import com.bb.dev.jmeter.util.Data2FilesUtil;
import com.bb.dev.jmeter.util.Data4JmeterUtil;

import java.io.File;

public class JmeterApplication {
    /*
        功能：定义执行输入参数模式，只支持2种模式
                java -jar jmeter-center.jar ut2jmx $utRoot $jmeterRoot
                java -jar jmeter-center.jar data2file $jmeterRoot $baseWebUrl $reWrite

     */
    public static void main(String[] args) {
        boolean err = false;
        //首先对输入参数的个数进行识别，只能输入3个或者4个参数
        if (args.length != 3 && args.length != 4) {
            err = true;

            //当输入3个参数的时候，第一个参数必须是ut2jmx
        } else if (args.length == 3 && "ut2jmx".equalsIgnoreCase(args[0])) {
            //当输入3个参数的时候，第2个参数表示路径且必须存在
            if (new File(args[1]).exists()) {
                System.out.println("从ut测试数据utRoot转换测试数据给jmeter用");
                Data4JmeterUtil.getAllJmxData(args[1], args[2]);
            } else {
                System.out.println("输入参数utRoot路径不存在");
                err = true;
            }

            //当输入4个参数的时候，第1个参数必须是data2file
        } else if (args.length == 4 && "data2file".equalsIgnoreCase(args[0])) {
            //当输入3个参数的时候，第2个参数表示路径且其子目录data必须存在
            if (!new File(args[1]).exists()) {
                System.out.println("输入参数jmeterRoot路径不存在");
                err = true;
            }

            if (!new File(args[1] + "/data").exists()) {
                System.out.println("输入参数jmeterRoot路径下不存在data目录");
                err = true;
            }
            //当输入3个参数的时候，第3个参数表示webUrl，必须是标准的http/https格式
            String baseWebUrl = args[2];
            JSONObject basePath = Data2CasesUtil.getPathData(baseWebUrl);
            if (basePath == null || basePath.get("protocol") == null) {
                System.out.println("输入参数baseWebUrl不是正确webUrl");
                err = true;
            }
            //当输入3个参数的时候，第4个参数为true时，表示reWrite = true
            boolean reWrite = false;
            if ("true".equalsIgnoreCase(args[3])) {
                reWrite = true;
            }

            if (!err) {
                System.out.println("将data数据转化为jmx格式用例文件");
                Data2FilesUtil.saveFiles(args[1], baseWebUrl, reWrite);
            }

        } else {
            err = true;
        }
        if (err) {
            System.out.println("请正确输入参数，支持2种模式：");
            System.out.println("\t模式1：");
            System.out.println("\t\t从ut测试数据utRoot转换测试数据给jmeter用，存放到jmeterRoot路径");
            System.out.println("\t\t命令格式java -jar jmeter-center.jar ut2jmx $utRoot $jmeterRoot");
            System.out.println("\t模式2：");
            System.out.println("\t\t将data数据转化为jmx格式用例文件");
            System.out.println("\t\t参数：jmeterRoot，测试数据和测试用例文件的存放路径");
            System.out.println("\t\t参数：baseWebUrl，接口服务部署实例的地址");
            System.out.println("\t\t参数：reWrite，遇到同名测试用例文件是否覆盖");
            System.out.println("\t\t命令格式java -jar jmeter-center.jar data2file $jmeterRoot $baseWebUrl $reWrite");
        }
    }
}
