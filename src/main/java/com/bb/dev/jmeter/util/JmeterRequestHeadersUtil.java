package com.bb.dev.jmeter.util;

import com.bb.dev.jmeter.model.ArgsModel;
import org.dom4j.Element;

import java.util.List;

public class JmeterRequestHeadersUtil {
    /*
      功能：测试用例文件的请求头构建方法

      输入参数：
          Element hashTree                  测试用例hashTree节点
          List<ArgsModel> requestHeaders    测试用例请求头结构化数据对象
    */
    public static Element add(Element hashTree, List<ArgsModel> requestHeaders) {
        if (requestHeaders.size() == 0) {
            return hashTree;
        }
        //构建 headerManager
        Element headerManager = hashTree.addElement("HeaderManager");
        headerManager.addAttribute("guiclass", "HeaderPanel");
        headerManager.addAttribute("testclass", "HeaderManager");
        headerManager.addAttribute("testname", "requestHeaders");
        headerManager.addAttribute("enabled", "true");
        //collectionProp of headerManager
        Element collectionProp = headerManager.addElement("collectionProp");
        collectionProp.addAttribute("name", "HeaderManager.headers");
        //构建 多个elementProp
        for (ArgsModel arg : requestHeaders) {
            Element elementProp = collectionProp.addElement("elementProp");
            elementProp.addAttribute("elementType", "Header");
            elementProp.addAttribute("name", "");
            Element name = elementProp.addElement("stringProp");
            name.addAttribute("name", "Header.name");
            name.setText(arg.getN());
            Element value = elementProp.addElement("stringProp");
            value.addAttribute("name", "Header.value");
            value.setText(arg.getV());
        }
        //构建 hashTree
        hashTree.addElement("hashTree");
        return hashTree;
    }
}
