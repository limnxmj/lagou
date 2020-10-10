package cn.xmj.entity;


import java.io.Serializable;

public class Position implements Serializable {

    private Long id;
    private String name;
    private String salary;
    private String city;

    public Position() {
    }

    public Position(Long id, String name, String salary, String city) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.city = city;
    }

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
