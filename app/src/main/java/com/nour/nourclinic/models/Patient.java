package com.nour.nourclinic.models;

public class Patient {
    int id;
    String patientName;
    String patientAddress;


    public Patient(int id, String patientName, String patientAddress) {
        this.id = id;
        this.patientName = patientName;
        this.patientAddress = patientAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    @Override
    public String toString() {
        return patientName + "\n" + patientAddress;
    }
}
