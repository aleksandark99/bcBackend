package com.garbagecollectors.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsersNumForEventDTO {

    private int usersNum;
    private StringResponse response;
}
