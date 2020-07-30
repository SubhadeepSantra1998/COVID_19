package com.example.covid_19;

public class StatesModel {
    private String states,confirmed,active,deaths,recovered;

    public StatesModel(){
    }

    public StatesModel(String states, String confirmed, String active, String deaths, String recovered) {
        this.states = states;
        this.confirmed = confirmed;
        this.active = active;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
}
