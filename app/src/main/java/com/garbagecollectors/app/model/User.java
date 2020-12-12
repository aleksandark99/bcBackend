package com.garbagecollectors.app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import com.garbagecollectors.app.model.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@SqlResultSetMappings({ //
	//

	@SqlResultSetMapping(name = "findScoreBoardMapping",
			classes = {@ConstructorResult(targetClass=com.garbagecollectors.app.dto.UserStatsDto.class,
			columns = {@ColumnResult(name="user_id", type=Integer.class),
					   @ColumnResult(name="points_num", type=Integer.class),
					   @ColumnResult(name="events_count", type=Integer.class),
					   @ColumnResult(name="first_name", type=String.class),
					   @ColumnResult(name="last_name", type=String.class),
					
			})} ),
	
	@SqlResultSetMapping(name = "findEventsForUserMapping",
			classes = {@ConstructorResult(targetClass=com.garbagecollectors.app.dto.EventForUserDto.class,
			columns = {@ColumnResult(name="eventId", type=Integer.class),
					   @ColumnResult(name="imageURLstart", type=String.class),
					   @ColumnResult(name="imageURLend", type=String.class),
					   @ColumnResult(name="imageURLteam", type=String.class),
					   @ColumnResult(name="event_name", type=String.class),
					   @ColumnResult(name="successfull", type=Boolean.class),
			
			})} ),


			
	
	})
@NamedNativeQueries(value = {
	
		@NamedNativeQuery(name = "findScoreBoard", query = "" 
		+ "SELECT u.user_id, p.points_num, count(ue.event_id) AS events_count, p.first_name, p.last_name "
		+ "FROM users u "
		+ "JOIN user_events ue ON ue.user_id = u.user_id "
		+ "JOIN events e ON ue.event_id = e.event_id "
		+ "JOIN profiles p ON u.user_profile_id = p.profile_id "
		+ "WHERE e.verified IS TRUE "
		+ "GROUP BY u.user_id"
				
		,resultSetMapping = "findScoreBoardMapping"),
		
		@NamedNativeQuery(name = "findEventsForUser", query = "" 
		+ "select e.event_id, picStart.picture_url AS imageURLstart, picEnd.picture_url AS imageURLend, picTeam.picture_url AS imageURLteam, e.event_name, e.successfull " 
		+ "from users u " 
		+ "join user_events ue ON ue.user_id = u.user_id "  
		+ "join events e ON e.event_id = ue.event_id " 
		+ "join pictures picStart ON picStart.picture_id = e.start_pic_id " 
		+ "join pictures picEnd ON picEnd.picture_id = e.end_pic_id "  
		+ "join pictures picTeam ON picTeam.picture_id = e.group_pic_id " 
		+ "where u.user_id = 1 AND e.finished IS TRUE AND e.verified IS TRUE"
						
		,resultSetMapping = "findEventsForUserMapping"),

	})
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

	@Column(nullable = true, name = "jwt")
	private String jwt;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ERole user_role;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_profile_id", referencedColumnName = "profile_id")
	private Profile userProfile;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name="user_events", 
	joinColumns=@JoinColumn(name="user_id", referencedColumnName = "user_id"), 
	inverseJoinColumns = @JoinColumn(name="event_id", referencedColumnName = "event_id") )
	private Set<Event> user_events;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "isOrganizedBy")
	private Set<Event> eventWhichOrganized;
	
}
