package com.garbagecollectors.app.model;

import javax.persistence.*;

import org.springframework.lang.NonNull;

import lombok.*;

@Entity
@Table(name="pictures")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="picture_id", unique = true, nullable = false)
	private Long picture_id;
	
	@NonNull
	private String picture_name;
	
	@Column(unique = false, nullable = false)
	private long picture_size;
	
	@Column(unique = false, nullable = false)
	private String mime_type;
	
	@Column(unique = true, nullable = false)
	private String picture_url;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="start_picture")
	private Event eventStart;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="end_picture")
	private Event eventEnd;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "team_picture")
	private Event eventTeam;
}
