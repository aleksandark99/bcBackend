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
public class ScoreBoardResponse {
	
	private List<UserStatsDto> scoreBoards;
	private StringResponse response;
	

}
