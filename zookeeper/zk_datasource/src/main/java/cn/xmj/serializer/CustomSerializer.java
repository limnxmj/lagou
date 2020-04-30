/**
 * FileName: CustomSerializer.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 15:53
 * Description:
 */
package cn.xmj.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

public class CustomSerializer implements ZkSerializer {

    private String charset = "UTF-8";

    public CustomSerializer() {
    }

    public CustomSerializer(String charset) {
        this.charset = charset;
    }

    public byte[] serialize(Object data) throws ZkMarshallingError {
        try {
            byte[] bytes = String.valueOf(data).getBytes(charset);
            return bytes;
        } catch (UnsupportedEncodingException e) {
            throw new ZkMarshallingError("Wrong Charset:" + charset);
        }
    }

    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        String result = null;
        try {
            result = new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new ZkMarshallingError("Wrong Charset:" + charset);
        }
        return result;
    }

}
