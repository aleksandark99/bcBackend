package com.garbagecollectors.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventForUserDto {
	
	private int eventId;
	private String imageURLend, eventName, organized, eventDescription, imageURLstart, imageURLteam;
	private boolean verified;

	
	
}
