/**
 * FileName: Context.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/3 12:05
 * Description:
 */
package server;

import loader.Loader;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private String classesPath;

    private String libraryPath;

    private String docBase;

    private String path;

    private Loader loader;

    private ClassLoader parentClassLoader = this.getClass().getClassLoader();

    public Context() {
    }

    public Context(String docBase, String path, String contextPath) {
        this.docBase = docBase;
        this.path = path;
        this.classesPath = contextPath + "/classes";
        this.libraryPath = contextPath + "/lib";
        loader = new Loader(parentClassLoader, this);
    }

    private Map<String, Wrapper> wrapperMap = new HashMap<>();

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Wrapper> getWrapperMap() {
        return wrapperMap;
    }

    public void setWrapperMap(Map<String, Wrapper> wrapperMap) {
        this.wrapperMap = wrapperMap;
    }

    public String getClassesPath() {
        return classesPath;
    }

    public void setClassesPath(String classesPath) {
        this.classesPath = classesPath;
    }

    public Loader getLoader() {
        return loader;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public ClassLoader getParentClassLoader() {
        return parentClassLoader;
    }

    public void setParentClassLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }
}
