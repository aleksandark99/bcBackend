package com.garbagecollectors.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventsUserResponse {
	
	List<EventForUserDto> events;
	private StringResponse stringResponse;
}
