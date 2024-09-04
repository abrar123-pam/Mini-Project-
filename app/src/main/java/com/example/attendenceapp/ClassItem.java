package com.example.attendenceapp;

public class ClassItem {



    private long did;
    public ClassItem(String departmentName, String subjectName) {
        this.departmentName = departmentName;
        this.subjectName = subjectName;
    }



    private String departmentName;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    private String subjectName;

    public ClassItem(long did, String departmentName, String subjectName) {
        this.did = did;
        this.departmentName = departmentName;
        this.subjectName = subjectName;
    }



    public long getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }
}

