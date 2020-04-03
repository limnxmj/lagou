package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
    private Mapper mapper;

    public RequestProcessor(Socket socket, Mapper mapper) {
        this.socket = socket;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            Host host = mapper.getHostMap().get(request.getHost());
            if (host == null) {
                HttpProtocolUtil.getHttpHeader404();
                return;
            }
            String url = request.getUrl();
            String[] split = url.split("/");
            if (split.length < 3) {
                HttpProtocolUtil.getHttpHeader404();
                return;
            }
            Context context = host.getContextMap().get(split[1]);
            if (context == null) {
                HttpProtocolUtil.getHttpHeader404();
                return;
            }
            Wrapper wrapper = context.getWrapperMap().get("/" + split[2]);
            if (wrapper == null) {
                response.outputHtml(request.getUrl());
            } else{
                // 动态资源servlet请求
                HttpServlet httpServlet = wrapper.getInstance();
                httpServlet.service(request, response);
            }
            socket.close();

        }catch (Exception e) {
        }

    }
}
