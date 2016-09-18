package com.curiosity.mycurriculum.bean;

import java.util.List;

public class CourseInfo {
    //课程名
    private String name;
    //星期
    private int dayOfWeek;
    //课时起始  第XX节
    private int timeFrom;
    //课时结束
    private int timeTo;
    //周时起始 第XX周
    private int weekFrom;
    //周时结束
    private int weekTo;
    //授课教师名
    private String techName;
    //课室
    private String classNum;
    //特殊周时标志 0无特殊 1单周 2双周
    private int weekFlag = 0;
    //课程调整标志 0无调整 1有调整
    private int changeFlag = 0;
    //调整编号 changFlag = 1 时有效
    private List<String> changeType;

    public CourseInfo() {
        this.weekFlag = 0;
        this.changeFlag = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    public int getWeekFrom() {
        return weekFrom;
    }

    public void setWeekFrom(int weekFrom) {
        this.weekFrom = weekFrom;
    }

    public int getWeekTo() {
        return weekTo;
    }

    public void setWeekTo(int weekTo) {
        this.weekTo = weekTo;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public int getWeekFlag() {
        return weekFlag;
    }

    public void setWeekFlag(int weekFlag) {
        this.weekFlag = weekFlag;
    }

    public int getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(int changeFlag) {
        this.changeFlag = changeFlag;
    }

    public List<String> getChangeType() {
        return changeType;
    }

    public void setChangeType(List<String> changeType) {
        this.changeType = changeType;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "name='" + name + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", weekFrom=" + weekFrom +
                ", weekTo=" + weekTo +
                ", techName='" + techName + '\'' +
                ", classNum='" + classNum + '\'' +
                ", weekFlag=" + weekFlag +
                ", changeFlag=" + changeFlag +
                ", changeType=" + changeType +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        CourseInfo info;
        if(obj instanceof CourseInfo)
            info = (CourseInfo) obj;
        else
            return false;
        if(!getName().equals(info.getName()))
            return false;
        if(getDayOfWeek() != info.getDayOfWeek())
            return false;
        if(getTimeFrom() != info.getTimeFrom())
            return false;
        if(getTimeTo() != info.getTimeTo())
            return false;
        if(getWeekFrom() != info.getWeekFrom())
            return false;
        if(getWeekTo() != info.getWeekTo())
            return false;
        if(!getTechName().equals(info.getTechName()))
            return false;
        if(!getClassNum().equals(info.getClassNum()))
            return false;
        if(getWeekFlag() != info.getWeekFlag())
            return false;
        if(getChangeFlag() != info.getChangeFlag())
            return false;
        else {
            if(getChangeType().size() != info.getChangeType().size())
                return false;
            for(int i = 0; i < getChangeType().size(); ++i) {
                if(!getChangeType().get(i).equals(info.getChangeType().get(i)))
                    return false;
            }
        }
        return true;
    }
}
