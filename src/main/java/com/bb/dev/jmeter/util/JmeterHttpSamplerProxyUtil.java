package com.bb.dev.jmeter.util;

import com.bb.dev.jmeter.model.ArgsModel;
import com.bb.dev.jmeter.model.JmeterCaseModel;
import org.dom4j.Element;

public class JmeterHttpSamplerProxyUtil {
    /*
      功能：生成测试用例文件的httpSamplerProxy
            httpSamplerProxy是jmeter测试 REST API的重中之中

      输入参数：
          Element httpSamplerProxy      测试用例httpSamplerProxy节点
          JmeterCaseModel jmeterCase    测试用例结构化数据对象
    */
    public static Element add(Element httpSamplerProxy, JmeterCaseModel jmeterCase) {
        boolean isBodyRaw = false;
        String domain = jmeterCase.getDomain();
        String path = jmeterCase.getPath();
        String protocol = jmeterCase.getProtocol();
        int port = jmeterCase.getPort();
        String testName = "";

        //elementProp
        Element elementProp = httpSamplerProxy.addElement("elementProp");
        elementProp.addAttribute("name", "HTTPsampler.Arguments");
        elementProp.addAttribute("elementType", "Arguments");

        //collectionProp of elementProp
        Element collectionProp = elementProp.addElement("collectionProp");
        collectionProp.addAttribute("name", "Arguments.arguments");

        for (ArgsModel arg : jmeterCase.getArgs()) {

            if ("var".equalsIgnoreCase(arg.getT()) && path.contains("{" + arg.getN() + "}")) {
                path = path.replace("{" + arg.getN() + "}", arg.getV());
            } else {
                Element argElementProp = collectionProp.addElement("elementProp");
                argElementProp.addAttribute("elementType", "HTTPArgument");
                if ("body".equalsIgnoreCase(arg.getT())) {
                    isBodyRaw = true;
                    argElementProp.addAttribute("name", "");
                } else {
                    argElementProp.addAttribute("name", arg.getN());
                    //<boolProp name="HTTPArgument.use_equals">true</boolProp>
                    Element argElementBoolProp_1 = argElementProp.addElement("boolProp");
                    argElementBoolProp_1.addAttribute("name", "HTTPArgument.use_equals");
                    argElementBoolProp_1.setText("true");

                    //<stringProp name="Argument.name">tenantId</stringProp>
                    Element argElementStringProp_0 = argElementProp.addElement("stringProp");
                    argElementStringProp_0.addAttribute("name", "Argument.name");
                    argElementStringProp_0.setText(arg.getN());
                }

                //<boolProp name="HTTPArgument.always_encode">true</boolProp>
                Element argElementBoolProp_0 = argElementProp.addElement("boolProp");
                argElementBoolProp_0.addAttribute("name", "HTTPArgument.always_encode");
                argElementBoolProp_0.setText("true");

                //<stringProp name="Argument.value">75km8j2o7jv42yyqgiwpzkyj4fhvuqt</stringProp>
                Element argElementStringProp_1 = argElementProp.addElement("stringProp");
                argElementStringProp_1.addAttribute("name", "Argument.value");
                argElementStringProp_1.setText(arg.getV());

                //<stringProp name="Argument.metadata">=</stringProp>
                Element argElementStringProp_2 = argElementProp.addElement("stringProp");
                argElementStringProp_2.addAttribute("name", "Argument.metadata");
                argElementStringProp_2.setText("=");


            }
        }

        if (("http".equalsIgnoreCase(protocol) && 80 == port) || ("https".equalsIgnoreCase(protocol) && 443 == port)) {
            testName = protocol + "://" + domain + path;
        } else {
            testName = protocol + "://" + domain + ":" + port + path;
        }

        //属性
        httpSamplerProxy.addAttribute("guiclass", "HttpTestSampleGui");
        httpSamplerProxy.addAttribute("testclass", "HTTPSamplerProxy");
        httpSamplerProxy.addAttribute("enabled", "true");
        httpSamplerProxy.addAttribute("testname", jmeterCase.getId());

        if (!isBodyRaw) {
            elementProp.addAttribute("guiclass", "HTTPArgumentsPanel");
            elementProp.addAttribute("testclass", "Arguments");
            elementProp.addAttribute("testname", "User Defined Variables");
            elementProp.addAttribute("enabled", "true");
        }
        //下一级节点
        //这个是判断是否消息体参数，如果是，需要增加<boolProp name="HTTPSampler.postBodyRaw">true</boolProp>
        if (isBodyRaw) {
            Element boolProp_0 = httpSamplerProxy.addElement("boolProp");
            boolProp_0.addAttribute("name", "HTTPSampler.postBodyRaw");
            boolProp_0.setText("true");

        }
        //<boolProp name="HTTPSampler.follow_redirects">false</boolProp>
        Element boolProp_1 = httpSamplerProxy.addElement("boolProp");
        boolProp_1.addAttribute("name", "HTTPSampler.follow_redirects");
        boolProp_1.setText("false");

        //<boolProp name="HTTPSampler.auto_redirects">true</boolProp>
        Element boolProp_2 = httpSamplerProxy.addElement("boolProp");
        boolProp_2.addAttribute("name", "HTTPSampler.auto_redirects");
        boolProp_2.setText("true");

        //<boolProp name="HTTPSampler.use_keepalive">true</boolProp>
        Element boolProp_3 = httpSamplerProxy.addElement("boolProp");
        boolProp_3.addAttribute("name", "HTTPSampler.use_keepalive");
        boolProp_3.setText("true");

        //<boolProp name="HTTPSampler.DO_MULTIPART_POST">false</boolProp>
        Element boolProp_4 = httpSamplerProxy.addElement("boolProp");
        boolProp_4.addAttribute("name", "HTTPSampler.DO_MULTIPART_POST");
        boolProp_4.setText("false");

        //<stringProp name="HTTPSampler.domain">dev.yihecloud.com</stringProp>
        Element stringProp_0 = httpSamplerProxy.addElement("stringProp");
        stringProp_0.addAttribute("name", "HTTPSampler.domain");
        stringProp_0.setText(domain);

        //<stringProp name="HTTPSampler.port">443</stringProp>
        Element stringProp_1 = httpSamplerProxy.addElement("stringProp");
        stringProp_1.addAttribute("name", "HTTPSampler.port");
        stringProp_1.setText("" + port);

        //<stringProp name="HTTPSampler.protocol">https</stringProp>
        Element stringProp_2 = httpSamplerProxy.addElement("stringProp");
        stringProp_2.addAttribute("name", "HTTPSampler.protocol");
        stringProp_2.setText(protocol);

        //<stringProp name="HTTPSampler.contentEncoding"></stringProp>
        Element stringProp_3 = httpSamplerProxy.addElement("stringProp");
        stringProp_3.addAttribute("name", "HTTPSampler.contentEncoding");
        stringProp_3.setText(jmeterCase.getEncoding());

        //<stringProp name="HTTPSampler.path">/apimanager/api/v1/spring/cloud/gw</stringProp>
        Element stringProp_4 = httpSamplerProxy.addElement("stringProp");
        stringProp_4.addAttribute("name", "HTTPSampler.path");
        stringProp_4.setText(path);

        //<stringProp name="HTTPSampler.method">POST</stringProp>
        Element stringProp_5 = httpSamplerProxy.addElement("stringProp");
        stringProp_5.addAttribute("name", "HTTPSampler.method");
        stringProp_5.setText(jmeterCase.getMethod().toUpperCase());

        //<<stringProp name="HTTPSampler.embedded_url_re"></stringProp>
        Element stringProp_6 = httpSamplerProxy.addElement("stringProp");
        stringProp_6.addAttribute("name", "HTTPSampler.embedded_url_re");
        stringProp_6.setText("");

        //<stringProp name="HTTPSampler.implementation"></stringProp>
        Element stringProp_7 = httpSamplerProxy.addElement("stringProp");
        stringProp_7.addAttribute("name", "HTTPSampler.implementation");
        stringProp_7.setText("java");

        //<stringProp name="HTTPSampler.connect_timeout"></stringProp>
        Element stringProp_8 = httpSamplerProxy.addElement("stringProp");
        stringProp_8.addAttribute("name", "HTTPSampler.connect_timeout");
        stringProp_8.setText("");

        //<stringProp name="HTTPSampler.response_timeout"></stringProp>
        Element stringProp_9 = httpSamplerProxy.addElement("stringProp");
        stringProp_9.addAttribute("name", "HTTPSampler.response_timeout");
        stringProp_9.setText("");


        return httpSamplerProxy;
    }

}
