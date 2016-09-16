package com.curiosity.bean;

import java.util.List;

public class CurriculumInfo {
    private String year;
    private String semester;
    private List<CourseInfo> courses;

    public CurriculumInfo() {
    }

    public CurriculumInfo(String year, String semester, List<CourseInfo> courses) {
        this.year = year;
        this.semester = semester;
        this.courses = courses;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<CourseInfo> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseInfo> courses) {
        this.courses = courses;
    }
}
