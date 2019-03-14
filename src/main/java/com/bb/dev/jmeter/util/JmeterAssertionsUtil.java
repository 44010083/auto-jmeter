package com.bb.dev.jmeter.util;


import com.bb.dev.jmeter.model.ArgsModel;
import org.dom4j.Element;

import java.util.List;

public class JmeterAssertionsUtil {
    /*
      功能：给jmx测试文件增加断言。
            jmx测试文件其实就是xml格式，
            增加断言的方式就是在测试用例hashTree节点下增加ResponseAssertion节点

      输入参数：
          Element hashTree              测试用例hashTree节点
          List<ArgsModel> assertions    从测试用例数据文件获得的断言设置
   */
    public static Element add(Element hashTree, List<ArgsModel> assertions) {
        if (assertions.size() == 0) {
            return hashTree;
        }
        for (ArgsModel arg : assertions) {
            //构建 ResponseAssertion
            Element responseAssertion = hashTree.addElement("ResponseAssertion");
            responseAssertion.addAttribute("guiclass", "AssertionGui");
            responseAssertion.addAttribute("testclass", "ResponseAssertion");

            responseAssertion.addAttribute("testname", "assertion_" + arg.hashCode());
            responseAssertion.addAttribute("enabled", "true");
            //collectionProp of responseAssertion
            Element collectionProp = responseAssertion.addElement("collectionProp");
            collectionProp.addAttribute("name", "Asserion.test_strings");
            //stringProp of collectionProp
            Element stringProp = collectionProp.addElement("stringProp");
            stringProp.addAttribute("name", arg.getV());
            stringProp.setText(arg.getV());

            //stringProp of responseAssertion
            Element msg = responseAssertion.addElement("stringProp");
            msg.addAttribute("name", "Assertion.custom_message");
            Element field = responseAssertion.addElement("stringProp");
            field.addAttribute("name", "Assertion.test_field");
            field.setText(arg.getN());
            //boolProp of responseAssertion
            Element boolProp = responseAssertion.addElement("boolProp");
            boolProp.addAttribute("name", "Assertion.assume_success");
            boolProp.setText("false");
            //intProp of responseAssertion
            Element intProp = responseAssertion.addElement("intProp");
            intProp.addAttribute("name", "Assertion.test_type");
            intProp.setText(arg.getT());
            //构建 hashTree
            hashTree.addElement("hashTree");
        }

        return hashTree;
    }

}
