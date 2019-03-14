package com.bb.dev.jmeter.util;

import com.bb.dev.jmeter.model.JmeterCaseModel;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;

public class JmeterFileUtil {
    /*
        功能：依据输入的jmeterCase，保存用例文件
                根据jmeterCase参数的不同，可能要保存冒烟测试用例、压力测试用例、常规自动化测试用例
        输入参数：
            JmeterCaseModel jmeterCase      jmeter用例结构化数据对象
            String rootPath                 写入jmeter用例文件的根路径
            boolean reWrite                 遇到同名文件是否覆盖重写
     */
    public static void save3File(JmeterCaseModel jmeterCase, String rootPath, boolean reWrite) {
        String smokePath = rootPath + "/smoke";
        String testsPath = rootPath + "/tests";
        String stressPath = rootPath + "/stress";
        String filePath = "/" + jmeterCase.getMethod() + "(" + jmeterCase.getPath().replaceAll("/", "@") + ")";
        if (jmeterCase.getSmoke()) {
            saveFile(jmeterCase, smokePath + filePath, 1, reWrite);
        }
        if (jmeterCase.getStress() > 1) {
            saveFile(jmeterCase, stressPath + filePath, jmeterCase.getStress(), reWrite);
        }
        saveFile(jmeterCase, testsPath + filePath, jmeterCase.getStress(), reWrite);
    }

    /*
        功能：依据输入的jmeterCase，保存用例文件
        输入参数：
            JmeterCaseModel jmeterCase      jmeter用例结构化数据对象
            String filePath                 写入jmeter用例文件的保存路径
            int threads                     jmeter用例文件的线程数设置（写入文件）
            boolean reWrite                 遇到同名文件是否覆盖重写
     */
    public static void saveFile(JmeterCaseModel jmeterCase, String filePath, int threads, boolean reWrite) {
        File file = new File(filePath);
        if (reWrite || !file.exists()) {
            saveFile(jmeterCase, filePath, threads);
        }

    }

    /*
        功能：依据输入的jmeterCase，保存用例文件
        输入参数：
            JmeterCaseModel jmeterCase      jmeter用例结构化数据对象
            String filePath                 写入jmeter用例文件的保存路径
     */
    public static void saveFile(JmeterCaseModel jmeterCase, String filePath) {
        saveFile(jmeterCase, filePath, 1);
    }
    /*
        功能：依据输入的jmeterCase，保存用例文件
        输入参数：
            JmeterCaseModel jmeterCase      jmeter用例结构化数据对象
            String filePath                 写入jmeter用例文件的保存路径
            int threads                     jmeter用例文件的线程数设置（写入文件）
     */

    public static void saveFile(JmeterCaseModel jmeterCase, String filePath, int threads) {
        Document document = JmeterElementUtil.getJmeterDoc(jmeterCase, threads);
        if (document != null) {
            File file = new File(filePath);
            file.mkdirs();

            XMLWriter writer = null;
            try {
                writer = new XMLWriter(new FileOutputStream(filePath + "/" + jmeterCase.getName() + ".jmx"),
                        OutputFormat.createPrettyPrint());
                writer.write(document);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
        功能：保存一份maven执行jmeter用例的pom.xml
        输入参数：
            String pomFile     pom文件的保存路径
     */
    public static void savePom(String pomFile) {
        String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">    <modelVersion>4.0.0</modelVersion>    <groupId>com.ob</groupId>    <artifactId>jmeter</artifactId>    <version>5.0</version>    <packaging>pom</packaging>    <name>jmeter tests</name>    <description>Demo project for jmeter tests</description>    <repositories>    </repositories>    <dependencies>    </dependencies>    <build>        <plugins>            <plugin>                 <groupId>com.lazerycode.jmeter</groupId>                  <artifactId>jmeter-maven-plugin</artifactId>                  <version>2.5.0</version>                  <executions>                 <execution>                     <id>jmeter-tests</id>                      <phase>verify</phase>                      <goals>                     <goal>jmeter</goal>                     </goals>                      <configuration>                     <testFilesDirectory>${CASESDIR}</testFilesDirectory>                      <ignoreResultFailures>true</ignoreResultFailures>                      <ignoreResultErrors>true</ignoreResultErrors>                      <resultsFileFormat>xml</resultsFileFormat>                     </configuration>                 </execution>                 </executions>             </plugin>        </plugins>    </build></project>";

        try {
            Document document = DocumentHelper.parseText(pomXml);
            XMLWriter writer = new XMLWriter(new FileOutputStream(pomFile), OutputFormat.createPrettyPrint());
            writer.write(document);
            writer.close();
        } catch (Exception e) {

        }
    }
}
