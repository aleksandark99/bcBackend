package com.garbagecollectors.app.model;



import java.util.Set;

import javax.persistence.*;

import org.springframework.lang.NonNull;

import com.myproject.app.entities.enums.ERole;

import lombok.*;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id", unique = true, nullable = false)
	private int user_id;
	
	@NonNull
	@Column(length = 100, nullable = false, unique = true, name = "username")
	private String username;
	
	@NonNull
	@Column(length = 100, nullable = false, name = "password")
	private String password;

	@NonNull
	@Column(nullable = true, name = "jwt")
	private String jwt;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ERole user_role;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_profile_id", referencedColumnName = "profile_id")
	private Profile userProfile;
	
	@ManyToMany
	@JoinTable(name="user_events", 
	joinColumns=@JoinColumn(name="user_id", referencedColumnName = "user_id"), 
	inverseJoinColumns = @JoinColumn(name="event_id", referencedColumnName = "event_id") )
	private Set<Event> user_events;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "isOrganizedBy")
	private Set<Event> eventWhichOrganized;
}
