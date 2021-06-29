package com.company.common.dto;

public class WorkClientDto {
    public int Id;
    public String FirstName;
    public String LastName;

    public WorkClientDto(int id, String firstName, String lastName) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
    }
}
