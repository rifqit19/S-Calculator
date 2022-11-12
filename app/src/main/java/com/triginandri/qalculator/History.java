package com.triginandri.qalculator;

public class History {

    int first;
    int second;
    double result;
    String operator;


    public History(int first, int second, double result, String operator) {
        this.first = first;
        this.second = second;
        this.result = result;
        this.operator = operator;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
