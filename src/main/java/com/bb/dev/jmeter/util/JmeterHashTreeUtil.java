package com.bb.dev.jmeter.util;

import com.bb.dev.jmeter.model.JmeterCaseModel;
import org.dom4j.Element;

public class JmeterHashTreeUtil {
    /*
      功能：生成测试用例文件的用例请求头和断言

      输入参数：
          Element hashTree              测试用例hashTree节点
          JmeterCaseModel jmeterCase    测试用例结构化数据对象
    */
    public static Element add(Element hashTree, JmeterCaseModel jmeterCase) {
        //请求头
        if (jmeterCase != null && jmeterCase.getRequestHeaders() != null) {
            hashTree = JmeterRequestHeadersUtil.add(hashTree, jmeterCase.getRequestHeaders());
        }
        //断言
        if (jmeterCase != null && jmeterCase.getAssertions() != null) {
            hashTree = JmeterAssertionsUtil.add(hashTree, jmeterCase.getAssertions());
        }

        return hashTree;
    }
}
