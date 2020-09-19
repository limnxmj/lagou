package cn.xmj.bean;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

public class Position implements Serializable {

    @Field
    private String id;
    @Field
    private String companyName;
    @Field
    private String positionAdvantage;
    @Field
    private String companyId;
    @Field
    private String positionName;
    @Field
    private String salary;

    public Position() {
    }

    public Position(String id, String companyName, String positionAdvantage, String companyId, String positionName, String salary) {
        this.id = id;
        this.companyName = companyName;
        this.positionAdvantage = positionAdvantage;
        this.companyId = companyId;
        this.positionName = positionName;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPositionAdvantage() {
        return positionAdvantage;
    }

    public void setPositionAdvantage(String positionAdvantage) {
        this.positionAdvantage = positionAdvantage;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id='" + id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", positionAdvantage='" + positionAdvantage + '\'' +
                ", companyId='" + companyId + '\'' +
                ", positionName='" + positionName + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
