package cn.xmj.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PositionService {

    public List<Map<String, Object>> searchPos(String name, String keyword, int pageNo, int pageSize) throws IOException;

    public void importAll() throws IOException;
}
