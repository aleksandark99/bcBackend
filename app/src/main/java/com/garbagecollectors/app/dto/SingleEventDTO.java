package com.garbagecollectors.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SingleEventDTO {

    private String eventName, imgBeforeURL, imgAfterURL, imgTeamURL, dateCreated, organizedBy, locationURL, locationString, eventDescription;
    private boolean isFinished;
    private int organisatorId;
}
