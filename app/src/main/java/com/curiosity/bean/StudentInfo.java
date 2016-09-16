package com.curiosity.bean;

public class StudentInfo {
    private String admission;
    private String stuNum;
    private String name;
    private String institute;
    private String major;
    private String clas;

    public StudentInfo() {
    }

    public StudentInfo(String admission, String stuNum, String name, String institute, String major, String clas) {
        this.admission = admission;
        this.stuNum = stuNum;
        this.name = name;
        this.institute = institute;
        this.major = major;
        this.clas = clas;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "admission='" + admission + '\'' +
                ", stuNum='" + stuNum + '\'' +
                ", name='" + name + '\'' +
                ", institute='" + institute + '\'' +
                ", major='" + major + '\'' +
                ", clas='" + clas + '\'' +
                '}';
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }
}
