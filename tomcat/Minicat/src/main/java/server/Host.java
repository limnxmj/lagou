/**
 * FileName: Host.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/3 12:05
 * Description:
 */
package server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Host {

    private String name;
    private String appBase;
    private File appBaseFile;

    private Map<String, Context> contextMap = new HashMap<>();

    public Host() {
    }

    public Host(String name, String appBase, File appBaseFile) {
        this.name = name;
        this.appBase = appBase;
        this.appBaseFile = appBaseFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppBase() {
        return appBase;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

    public File getAppBaseFile() {
        return appBaseFile;
    }

    public void setAppBaseFile(File appBaseFile) {
        this.appBaseFile = appBaseFile;
    }

    public Map<String, Context> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, Context> contextMap) {
        this.contextMap = contextMap;
    }
}
