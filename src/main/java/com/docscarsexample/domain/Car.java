package com.docscarsexample.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="\"Car\"", catalog="postgres", schema="public")
public class Car {
	
	@Id
	@GenericGenerator(name = "carSequenceGenerator", strategy = "increment")
	@GeneratedValue(generator = "carSequenceGenerator")
	private Long id;
	
	@JsonProperty("id_car")
	@Column(name="id_car", nullable = false)
	@NotNull
	private Long idCar;
	
	@JsonProperty("id_user")
	@Column(name="id_user", nullable = false)
	@NotNull
	private Long idUser;
	
	@JsonProperty("brand")
	@Column(name="brand", nullable = false)
	@NotEmpty
	private String brand;
	
	@JsonProperty("model")
	@Column(name="model", nullable = false)
	@NotEmpty
	private String model;
	
	@JsonProperty("is_default")
	@Column(name="is_default", nullable = true)
	private Boolean isDefault = true;
	
	@JsonProperty("created_at")
	@Column(name="created_at", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable=false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCar() {
		return idCar;
	}

	public void setIdCar(Long idCar) {
		this.idCar = idCar;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
}
