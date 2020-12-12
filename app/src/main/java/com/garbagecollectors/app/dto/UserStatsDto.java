package com.garbagecollectors.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserStatsDto {
	
	
	private int userId, score, eventsCount;
	private String firstName, lastName;
	
	public UserStatsDto(int user_id, int credit, int eventsCount, String firstName, String lastName) {
		
		this.userId = user_id;
		this.score = credit;
		this.eventsCount = eventsCount;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
}
