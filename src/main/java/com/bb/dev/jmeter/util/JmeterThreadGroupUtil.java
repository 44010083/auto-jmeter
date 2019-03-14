package com.bb.dev.jmeter.util;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;

public class JmeterThreadGroupUtil {
    /*
      功能：设置测试用例文件的Thread Group节点的线程数

      输入参数：
          Element root     测试用例root节点
          int num          线程数参数
    */
    public static Element changeNum(Element root, int num) {
        if (num < 1) {
            num = 1;
        }
        Element nondesL1 = root.element("hashTree");
        Element nondesL2 = nondesL1.element("hashTree");
        Element threadGroup = nondesL2.element("ThreadGroup");
        Iterator<Element> it = threadGroup.elementIterator();
        // 遍历
        while (it.hasNext()) {
            // 获取某个子节点对象
            Element e = it.next();
            if (e.getName().equalsIgnoreCase("stringProp")) {
                // 获取当前节点的所有属性节点
                List<Attribute> list = e.attributes();
                // 遍历属性节点
                for (Attribute attr : list) {
                    if (attr.getText().equalsIgnoreCase("ThreadGroup.num_threads")) {
                        e.setText("" + num);
                        return root;
                    }
                }
            }
        }
        return root;
    }
}
