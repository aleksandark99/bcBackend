package com.garbagecollectors.app.model;

import javax.persistence.*;

import org.springframework.lang.NonNull;

import lombok.*;


@Entity
@Table(name="profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int profile_id;
	
	@NonNull
	@Column(length = 100, nullable = false, unique = false)
	private String first_name;
	
	@NonNull
	@Column(length = 100, nullable = false, unique = false)
	private String last_name;
	
	@NonNull
	private int points_num;
	
	@NonNull
	private int events_num;
	
	@NonNull
	private int organized_events_num;


	@OneToOne(mappedBy = "userProfile")
	private User profile;



}
