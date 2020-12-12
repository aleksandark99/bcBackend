package com.garbagecollectors.app.dto;



public class UserStatsDto {
	
	
	private int userId, totalScore, eventsOrganized;
	private String firstName, lastName, username;
	
	public UserStatsDto(Integer userId, String firstName, String lastName, String username, Integer totalScore, Integer eventsOrganized) {
		
		this.username = username;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.totalScore = totalScore;
		this.eventsOrganized = eventsOrganized;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getEventsOrganized() {
		return eventsOrganized;
	}

	public void setEventsOrganized(int eventsOrganized) {
		this.eventsOrganized = eventsOrganized;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
