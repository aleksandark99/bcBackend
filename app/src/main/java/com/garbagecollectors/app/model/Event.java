package com.garbagecollectors.app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Column(nullable = false, unique = false)
	private boolean isVerified;
	
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
