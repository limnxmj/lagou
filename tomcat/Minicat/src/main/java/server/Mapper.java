/**
 * FileName: Mapper.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/3 12:01
 * Description:
 */
package server;

import java.util.HashMap;
import java.util.Map;

public final class Mapper {

    private Map<String, Host> hostMap = new HashMap<>();

    public Map<String, Host> getHostMap() {
        return hostMap;
    }

    public void setHostMap(Map<String, Host> hostMap) {
        this.hostMap = hostMap;
    }
}
