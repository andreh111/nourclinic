package com.nour.nourclinic.models;

public class Appointment {
    int id;
    String date;
    String time;
    String problem;
    String patientName;

    public Appointment(int id, String date, String time, String patientName,String problem) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.problem = problem;
        this.patientName = patientName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
