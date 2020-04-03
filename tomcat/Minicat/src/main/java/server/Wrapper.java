/**
 * FileName: Wrapper.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/3 12:04
 * Description:
 */
package server;

public class Wrapper {

    protected String servletClass = null;

    private volatile HttpServlet instance;

    public Wrapper() {
    }

    public Wrapper(String servletClass, HttpServlet instance) {
        this.servletClass = servletClass;
        this.instance = instance;
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    public HttpServlet getInstance() {
        return instance;
    }

    public void setInstance(HttpServlet instance) {
        this.instance = instance;
    }
}
