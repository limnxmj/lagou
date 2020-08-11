/**
 * FileName: Position.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/11 21:57
 * Description:
 */
package cn.xmj.pojo;

import java.io.Serializable;

public class Position implements Serializable {

    private Long id;
    private String name;
    private String salary;
    private String city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary='" + salary + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
