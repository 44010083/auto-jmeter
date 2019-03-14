package com.bb.dev.jmeter.util;


import com.bb.dev.jmeter.model.JmeterCaseModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class JmeterElementUtil {
    /*
      功能：生成测试用例xml格式的Document

      输入参数：
          JmeterCaseModel jmeterCase    测试用例结构化数据对象
          int threads                   指定测试用例执行时并发线程数
                                        非压力测试用例，我们都设置threads=1
                                        压力测试用例是读取用例的stress值
    */
    public static Document getJmeterDoc(JmeterCaseModel jmeterCase, int threads) {
        //jmx测试用例模板
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<jmeterTestPlan version=\"1.2\" properties=\"5.0\" jmeter=\"5.0 r1840935\">\n" + "  <hashTree>\n"
                + "    <TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">\n"
                + "      <stringProp name=\"TestPlan.comments\"></stringProp>\n"
                + "      <boolProp name=\"TestPlan.functional_mode\">false</boolProp>\n"
                + "      <boolProp name=\"TestPlan.serialize_threadgroups\">false</boolProp>\n"
                + "      <elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n"
                + "        <collectionProp name=\"Arguments.arguments\"/>\n" + "      </elementProp>\n"
                + "      <stringProp name=\"TestPlan.user_define_classpath\"></stringProp>\n" + "    </TestPlan>\n"
                + "    <hashTree>\n"
                + "      <ThreadGroup guiclass=\"ThreadGroupGui\" testclass=\"ThreadGroup\" testname=\"Thread Group\" enabled=\"true\">\n"
                + "        <elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop Controller\" enabled=\"true\">\n"
                + "          <boolProp name=\"LoopController.continue_forever\">false</boolProp>\n"
                + "          <stringProp name=\"LoopController.loops\">1</stringProp>\n" + "        </elementProp>\n"
                + "        <stringProp name=\"ThreadGroup.num_threads\">1</stringProp>\n"
                + "        <stringProp name=\"ThreadGroup.ramp_time\">1</stringProp>\n"
                + "        <longProp name=\"ThreadGroup.start_time\">1281132211000</longProp>\n"
                + "        <longProp name=\"ThreadGroup.end_time\">1281132211000</longProp>\n"
                + "        <boolProp name=\"ThreadGroup.scheduler\">false</boolProp>\n"
                + "        <stringProp name=\"ThreadGroup.on_sample_error\">continue</stringProp>\n"
                + "        <stringProp name=\"ThreadGroup.duration\"></stringProp>\n"
                + "        <stringProp name=\"ThreadGroup.delay\"></stringProp>\n" + "      </ThreadGroup>\n"
                + "      <hashTree>\n"
                + "        <CookieManager guiclass=\"CookiePanel\" testclass=\"CookieManager\" testname=\"HTTP Cookie Manager\" enabled=\"true\">\n"
                + "          <collectionProp name=\"CookieManager.cookies\"/>\n"
                + "          <boolProp name=\"CookieManager.clearEachIteration\">false</boolProp>\n"
                + "          <stringProp name=\"CookieManager.policy\">rfc2109</stringProp>\n"
                + "        </CookieManager>\n" + "        <hashTree/>\n"
                + "        <Arguments guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n"
                + "          <collectionProp name=\"Arguments.arguments\">\n"
                + "            <elementProp name=\"VIEWSTATE\" elementType=\"Argument\">\n"
                + "              <stringProp name=\"Argument.name\">VIEWSTATE</stringProp>\n"
                + "              <stringProp name=\"Argument.value\"></stringProp>\n"
                + "              <stringProp name=\"Argument.metadata\">=</stringProp>\n" + "            </elementProp>\n"
                + "            <elementProp name=\"jsessionid\" elementType=\"Argument\">\n"
                + "              <stringProp name=\"Argument.name\">jsessionid</stringProp>\n"
                + "              <stringProp name=\"Argument.value\"></stringProp>\n"
                + "              <stringProp name=\"Argument.metadata\">=</stringProp>\n" + "            </elementProp>\n"
                + "          </collectionProp>\n" + "        </Arguments>\n" + "        <hashTree/>\n"
                + "        <HeaderManager guiclass=\"HeaderPanel\" testclass=\"HeaderManager\" testname=\"HTTP Header Manager\" enabled=\"true\">\n"
                + "          <collectionProp name=\"HeaderManager.headers\">\n"
                + "            <elementProp name=\"\" elementType=\"Header\">\n"
                + "              <stringProp name=\"Header.name\">User-Agent</stringProp>\n"
                + "              <stringProp name=\"Header.value\">Mozilla/5.0 (Windows NT 6.2; WOW64; Trident/7.0; rv:11.0) like Gecko</stringProp>\n"
                + "            </elementProp>\n" + "            <elementProp name=\"\" elementType=\"Header\">\n"
                + "              <stringProp name=\"Header.name\">Accept</stringProp>\n"
                + "              <stringProp name=\"Header.value\">image/gif, image/jpeg, image/pjpeg, application/x-ms-application, application/xaml+xml, application/x-ms-xbap, */*</stringProp>\n"
                + "            </elementProp>\n" + "            <elementProp name=\"\" elementType=\"Header\">\n"
                + "              <stringProp name=\"Header.name\">Accept-Language</stringProp>\n"
                + "              <stringProp name=\"Header.value\">zh-CN</stringProp>\n" + "            </elementProp>\n"
                + "          </collectionProp>\n" + "        </HeaderManager>\n" + "        <hashTree/>\n" +

                "      </hashTree>\n" + "    </hashTree>\n" + "  </hashTree>\n" + "</jmeterTestPlan>";

        try {
            //将字符串格式的模板转化为xml的Document对象
            Document document = DocumentHelper.parseText(xml);
            //获得Document对象的root
            Element root = document.getRootElement();
            //给root填充实质内容
            JmeterElementUtil.getNewRoot(root, jmeterCase, threads);
            //返回xml的Document对象
            return document;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
      功能：根据测试用例数据，生成测试文件的root节点

      输入参数：
          Element root                  测试文件初始化的root节点内容
          JmeterCaseModel jmeterCase    测试用例结构化数据对象
          int threads                   指定测试用例执行时并发线程数
                                        非压力测试用例，我们都设置threads=1
                                        压力测试用例是读取用例的stress值
      输出：
          Element root                  处理过后的root节点内容
    */
    public static Element getNewRoot(Element root, JmeterCaseModel jmeterCase, int threads) {
        //改变root的ThreadGroup关于线程数的设置
        root = JmeterThreadGroupUtil.changeNum(root, threads);
        //增加前置依赖用例执行
        if (jmeterCase != null && jmeterCase.getPreCases() != null) {

            for (JmeterCaseModel tastCase : jmeterCase.getPreCases()) {

                root = add(root, tastCase);
            }
        }
        //增加目标用例执行
        root = add(root, jmeterCase);
        //增加后置用例执行
        if (jmeterCase != null && jmeterCase.getProCases() != null) {
            for (JmeterCaseModel tastCase : jmeterCase.getProCases()) {
                root = add(root, tastCase);
            }
        }
        return root;
    }

    /*
      功能：根据测试用例数据，生成测试文件的root节点

      输入参数：
          Element root                  测试文件初始化的root节点内容
          JmeterCaseModel jmeterCase    测试用例结构化数据对象
      输出：
          Element root                  处理过后的root节点内容
    */
    public static Element add(Element root, JmeterCaseModel jmeterCase) {
        //获得root的 level 1级别hashTree节点
        Element nondesL1 = root.element("hashTree");
        //获得root的 level 2级别hashTree节点
        Element nondesL2 = nondesL1.element("hashTree");
        //获得root的 level 3级别hashTree节点
        Element nondesL3 = nondesL2.element("hashTree");
        //在level 3级别hashTree节点增加HTTPSamplerProxy子节点
        Element httpSamplerProxy = nondesL3.addElement("HTTPSamplerProxy");
        httpSamplerProxy = JmeterHttpSamplerProxyUtil.add(httpSamplerProxy, jmeterCase);
        //在level 3级别hashTree节点增加hashTree子节点
        Element hashTree = nondesL3.addElement("hashTree");
        hashTree = JmeterHashTreeUtil.add(hashTree, jmeterCase);

        return root;

    }

}
