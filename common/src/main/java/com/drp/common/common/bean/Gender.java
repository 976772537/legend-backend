package com.drp.common.common.bean;

/**
 * @author dongruipeng
 * @Descrpition gender of USER
 * @date 2019year 12month04day  11:45:31
 */
public enum Gender {
    man('m'), woman('f');

    private char gender;

    Gender(char gender) {
        this.gender = gender;
    }

    public char getGender() {
        return gender;
    }
}
