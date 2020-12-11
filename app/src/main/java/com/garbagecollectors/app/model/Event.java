package com.garbagecollectors.app.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;

import org.springframework.lang.NonNull;

import lombok.*;

@Entity
@Table(name="events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int event_id;
	
	@NonNull
	@Column(unique = false, nullable = false)
	private String event_name;
	
	@Lob
	@Column(unique = false, nullable = false)
	private String event_desc;
	
	@NonNull
	@Column(unique = false, nullable = false)
	private String event_location;
	
	@Column(nullable = false, unique = false)
	private String start_date;
	
	@Column(nullable = false, unique = false)
	private String  due_date;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="start_pic_id", referencedColumnName = "picture_id")
	private Picture start_picture;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="end_pic_id", referencedColumnName = "picture_id")
	private Picture end_picture;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="group_pic_id", referencedColumnName = "picture_id")
	private Picture team_picture;
	
	@ManyToMany(mappedBy = "user_events")
	private Set<User> users;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private User isOrganizedBy;
}
