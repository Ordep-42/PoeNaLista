package br.ufrn.poenalista.house;

import java.time.Instant;

import br.ufrn.poenalista.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/** Vínculo entre um usuário e uma casa. */
@Entity
@Table(name = "residents", uniqueConstraints = @UniqueConstraint(columnNames = { "house_id", "user_id" }))
public class Resident {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "house_id", nullable = false)
	private House house;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ResidentRole role;

	@Column(name = "joined_at", nullable = false, updatable = false)
	private Instant joinedAt;

	protected Resident() {
	}

	public Resident(House house, User user, ResidentRole role) {
		this.house = house;
		this.user = user;
		this.role = role;
	}

	@PrePersist
	void onCreate() {
		joinedAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public House getHouse() {
		return house;
	}

	public User getUser() {
		return user;
	}

	public ResidentRole getRole() {
		return role;
	}

	public Instant getJoinedAt() {
		return joinedAt;
	}

}
