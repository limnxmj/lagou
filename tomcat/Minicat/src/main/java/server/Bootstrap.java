package server;

import loader.Loader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.*;

/**
 * Minicat的主类
 */
public class Bootstrap {

    /**定义socket监听的端口号*/
    private int port = 8080;
    private static final int DEFAULT_PORT = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Minicat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {
        init();
        // 加载解析相关的配置，servler.xml
        loadServer();

        // 定义一个线程池
        int corePoolSize = 10;
        int maximumPoolSize =50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>Minicat start on port：" + port);

        /*
            多线程改造（使用线程池）
         */
        while(true) {

            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, mapper);
            requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }
    }


    private Mapper mapper = new Mapper();

    private void loadServer() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            Element serviceElement = (Element) rootElement.selectSingleNode("Service");

            Element connectorElement = (Element) serviceElement.selectSingleNode("Connector");
            String portVal = connectorElement.attributeValue("port");
            try {
                port = Integer.parseInt(portVal);
            } catch (NumberFormatException e) {
                port = DEFAULT_PORT;
            }

            Element engineElement = (Element) serviceElement.selectSingleNode("Engine");
            List<Element> hostList = engineElement.selectNodes("//Host");
            for (int i = 0; i < hostList.size(); i++) {
                Element hostElement = hostList.get(i);
                String hostName = hostElement.attributeValue("name");
                String hostAppBase = hostElement.attributeValue("appBase");
                if (isBlank(hostName) || isBlank(hostAppBase) || mapper.getHostMap().containsKey(hostName)) {
                    continue;
                }

                File hostFile = new File(hostAppBase);
                if (!hostFile.exists() || hostFile.isFile()) {
                    continue;
                }
                Host host = new Host(hostName, hostAppBase, hostFile);
                loadHost(host);
                mapper.getHostMap().put(hostName, host);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHost(Host host) {
        File[] contextFiles = host.getAppBaseFile().listFiles();
        for (int i = 0; i < contextFiles.length; i++) {
            File contextFile = contextFiles[i];
            String contextFileName = contextFile.getName();
            String contextFilePath = contextFile.getPath();
            File webFile = new File(contextFilePath + "/WEB-INF/web.xml");
            if (webFile.exists()) {
                Context context = new Context(contextFileName, "/" + contextFileName, contextFilePath + "/WEB-INF");
                context.setParentClassLoader(contextDaemon.getParentClassLoader());
                loadServlet(context, webFile);
                host.getContextMap().put(contextFileName, context);
            }
        }
    }

    private boolean isBlank(String string) {
        return string == null || "".equals(string.trim());
    }

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet(Context context, File webFile) {

        Map<String, Wrapper> wrapperMap = new HashMap<>();
        try {
            InputStream resourceAsStream = new FileInputStream(webFile);
            SAXReader saxReader = new SAXReader();

            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.LagouServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                wrapperMap.put(urlPattern, new Wrapper(servletClass, loadServlet(context, servletClass)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        context.getWrapperMap().putAll(wrapperMap);
    }

    public HttpServlet loadServlet(Context context, String servletClass) {
        HttpServlet servlet = null;
        Class<?> clazz = null;

        ClassLoader cl = context.getLoader().getClassLoader();
        try {
            clazz = cl.loadClass(servletClass);
            servlet = (HttpServlet) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servlet;
    }

    static {
        // Will always be non-null
        String userDir = System.getProperty("user.dir");

        // Home first
        String home = System.getProperty("minicat.base");
        File homeFile = null;

        if (home != null) {
            File f = new File(home);
            try {
                homeFile = f.getCanonicalFile();
            } catch (IOException ioe) {
                homeFile = f.getAbsoluteFile();
            }
        }

        if (homeFile == null) {
            // Fall-back. Use current directory
            File f = new File(userDir);
            try {
                homeFile = f.getCanonicalFile();
            } catch (IOException ioe) {
                homeFile = f.getAbsoluteFile();
            }
        }
        System.setProperty("minicat.base", homeFile.getPath());
    }

    private ClassLoader createClassLoader(ClassLoader parent) throws Exception {
        Set<URL> set = new LinkedHashSet<>();
        File libDir = new File(System.getProperty("minicat.base"), "lib");
        if (libDir.exists()) {
            set.add(new URL(libDir.toURI().toString()));

            String filenames[] = libDir.list();
            if (filenames != null) {
                for (int j = 0; j < filenames.length; j++) {
                    String filename = filenames[j].toLowerCase(Locale.ENGLISH);
                    if (!filename.endsWith(".jar"))
                        continue;
                    File file = new File(libDir, filenames[j]);
                    file = file.getCanonicalFile();
                    URL url = new URL(file.toURI().toString());
                    set.add(url);
                }
            }
        }
        File log = new File(System.getProperty("minicat.base"), "logback.xml");
        set.add(new URL(log.toURI().toString()));

        final URL[] array = set.toArray(new URL[set.size()]);
        if (parent != null) {
            return new URLClassLoader(array, parent);
        } else {
            return new URLClassLoader(array);
        }
    }

    /**
     * Daemon reference.
     */
    private Context contextDaemon = null;

    private ClassLoader commonLoader = null;

    private void init() throws Exception {
        commonLoader = createClassLoader(null);

        // 设置当前线程上下文类加载器为 commonLoader，后续的资源或者 class 基本上都在 ${minicat.base}/lib 下
        Thread.currentThread().setContextClassLoader(commonLoader);

        Class<?> clazz = commonLoader.loadClass("server.Context");
        contextDaemon = (Context) clazz.newInstance();
        String methodName = "setParentClassLoader";
        Class<?>[] paramTypes = new Class[] { Class.forName("java.lang.ClassLoader") };
        Method md = contextDaemon.getClass().getMethod(methodName, paramTypes);
        md.invoke(contextDaemon, commonLoader);
    }

    /**
     * Minicat 的程序启动入口
     *
     * @param args
     */
    public static void main(String[] args) {

        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动Minicat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
