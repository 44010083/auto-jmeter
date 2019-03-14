package com.bb.dev.jmeter.model;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class JmeterCaseModel {
    //用例id
    private String id;
    //用例名称
    private String name;
    //用例所依附的测试工程
    private String prjId;
    // 被测服务器，可以是域名或者IP
    private String domain;
    // 被测服务的端口
    private int port;
    // 被测服务的协议类型，可以是http或者https
    private String protocol;
    private String encoding;
    // 接口路径，例如/hello/{name}
    private String path;
    // 测试接口方法，可以是GET,POST,PUT,DELETE
    private String method;
    // 测试的输入参数
    private List<ArgsModel> args;
    private int threads;
    private List<ArgsModel> requestHeaders;
    private List<ArgsModel> assertions;
    // before_Test前置执行测试用例，所有需要依赖的用例按顺序输入
    private List<JmeterCaseModel> preCases;
    // after_Test后置执行测试用例，所有需要依赖的用例按顺序输入
    private List<JmeterCaseModel> proCases;
    //测试请求参数jsonArray转字符串
    private String argsStr;
    //测试请求头转字符串
    private String headersStr;
    //断言设置jsonArray转字符串
    private String assertionsStr;
    //testBefore用例
    private String previous;
    //testAfter用例
    private String forth;
    //创建者
    private String creator;
    //最近修改者
    private String mender;
    //用例描述
    private String idesc;
    // 是否冒烟测试用例
    private boolean smoke;
    // 是否压力测试，当stress=0，表示不加入压力测试，当stress > 0时，stress表示每秒并发测试请求数
    private int stress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrjId() {
        return prjId;
    }

    public void setPrjId(String prjId) {
        this.prjId = prjId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        if (method != null) {
            return method.toUpperCase();
        }
        return method;
    }

    public void setMethod(String method) {
        if (method != null) {
            this.method = method.toUpperCase();
        } else {
            this.method = method;
        }

    }

    public List<ArgsModel> getArgs() {
        if (args == null || (args != null && args.size() == 0)) {
            args = new ArrayList<ArgsModel>();
            if (argsStr != null && !"".equalsIgnoreCase(argsStr)) {
                return JSON.parseArray(argsStr, ArgsModel.class);
            }
        }

        return args;
    }

    public void setArgs(List<ArgsModel> args) {
        this.args = args;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public List<ArgsModel> getRequestHeaders() {
        if (requestHeaders == null || (requestHeaders != null && requestHeaders.size() == 0)) {
            requestHeaders = new ArrayList<ArgsModel>();
            if (headersStr != null && !"".equalsIgnoreCase(headersStr)) {
                return JSON.parseArray(headersStr, ArgsModel.class);
            }
        }
        return requestHeaders;
    }

    public void setRequestHeaders(List<ArgsModel> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public List<ArgsModel> getAssertions() {
        if (assertions == null || (assertions != null && assertions.size() == 0)) {
            assertions = new ArrayList<ArgsModel>();
            if (assertionsStr != null && !"".equalsIgnoreCase(assertionsStr)) {
                return JSON.parseArray(assertionsStr, ArgsModel.class);
            }
        }
        return assertions;
    }

    public void setAssertions(List<ArgsModel> assertions) {
        this.assertions = assertions;
    }

    public List<JmeterCaseModel> getPreCases() {
        if (preCases == null || (preCases != null && preCases.size() == 0)) {
            preCases = new ArrayList<JmeterCaseModel>();
        }
        return preCases;
    }

    public void setPreCases(List<JmeterCaseModel> preCases) {
        this.preCases = preCases;
    }

    public List<JmeterCaseModel> getProCases() {
        if (proCases == null || (proCases != null && proCases.size() == 0)) {
            proCases = new ArrayList<JmeterCaseModel>();
        }
        return proCases;
    }

    public void setProCases(List<JmeterCaseModel> proCases) {
        this.proCases = proCases;
    }

    public String getArgsStr() {
        return argsStr;
    }

    public void setArgsStr(String argsStr) {
        this.argsStr = argsStr;
    }

    public String getHeadersStr() {
        return headersStr;
    }

    public void setHeadersStr(String headersStr) {
        this.headersStr = headersStr;
    }

    public String getAssertionsStr() {
        return assertionsStr;
    }

    public void setAssertionsStr(String assertionsStr) {
        this.assertionsStr = assertionsStr;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getForth() {
        return forth;
    }

    public void setForth(String forth) {
        this.forth = forth;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender;
    }

    public String getIdesc() {
        return idesc;
    }

    public void setIdesc(String idesc) {
        this.idesc = idesc;
    }

    public boolean getSmoke() {
        return smoke;
    }

    public void setSmoke(boolean smoke) {
        this.smoke = smoke;
    }

    public int getStress() {
        return stress;
    }

    public void setStress(int stress) {
        this.stress = stress;
    }
}
