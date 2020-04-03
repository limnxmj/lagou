package server;

import java.io.IOException;
import java.io.InputStream;

/**
 * 把请求信息封装为Request对象（根据InputSteam输入流封装）
 */
public class Request {

    private String method; // 请求方式，比如GET/POST
    private String url;  // 例如 /,/index.html
    private String host;

    private InputStream inputStream;  // 输入流，其他属性从输入流中解析出来


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Request() {
    }


    // 构造器，输入流传入
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        // 从输入流中获取请求信息
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        String inputStr = new String(bytes);
        System.out.println(inputStr);
        // 获取第一行请求头信息
        String firstLineStr = inputStr.split("\\n")[0];  // GET / HTTP/1.1
        String secondLine = inputStr.split("\\n")[1];
        String[] strings = firstLineStr.split(" ");

        this.method = strings[0];
        this.url = strings[1];
        this.host = secondLine.split(":")[1].trim();

        System.out.println("=====>>method:" + method);
        System.out.println("=====>>url:" + url);
        System.out.println("=====>>host:" + host);

    }
}
