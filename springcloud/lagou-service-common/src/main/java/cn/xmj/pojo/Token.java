/**
 * FileName: Token.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 8:48
 * Description:
 */
package cn.xmj.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="lagou_token")
public class Token implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
