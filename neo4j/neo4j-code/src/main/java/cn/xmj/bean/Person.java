/**
 * FileName: Person.java
 * Author:   limn_xmj@163.com
 * Date:     2020/7/6 10:16
 * Description:
 */
package cn.xmj.bean;

import org.neo4j.ogm.annotation.*;

import java.util.Set;

@NodeEntity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    @Property("cid")
    private int pid;
    private String name;
    private String character;
    private double money;
    private int age;
    private String description;

    @Relationship(type = "好友", direction = Relationship.OUTGOING)
    private Set<Person> friendsPerson;

    @Relationship(type = "父子", direction = Relationship.UNDIRECTED)
    private Set<Person> fatherPerson;

    public Person() {
    }

    public Person(Long id, int pid, String name, String character, double money, int age, String description) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.character = character;
        this.money = money;
        this.age = age;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Person> getFriendsPerson() {
        return friendsPerson;
    }

    public void setFriendsPerson(Set<Person> friendsPerson) {
        this.friendsPerson = friendsPerson;
    }

    public Set<Person> getFatherPerson() {
        return fatherPerson;
    }

    public void setFatherPerson(Set<Person> fatherPerson) {
        this.fatherPerson = fatherPerson;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", character='" + character + '\'' +
                ", money=" + money +
                ", age=" + age +
                ", description='" + description + '\'' +
                ", friendsPerson=" + friendsPerson +
                ", fatherPerson=" + fatherPerson +
                '}';
    }
}
