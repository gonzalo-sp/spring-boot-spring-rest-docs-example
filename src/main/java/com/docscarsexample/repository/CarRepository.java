package com.docscarsexample.repository;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.docscarsexample.domain.Car;


public interface CarRepository extends CrudRepository<Car, Long> {

	<S extends Car> S save(@Valid S cars);
	
	@RestResource(exported = false)
	@Query("SELECT c.id FROM Car c WHERE idCar = :idCar)")
	Long getIdFromIdCar(@Param("idCar") Long idCar);
	
	@RestResource(exported = false)
	@Query("SELECT c FROM Car c WHERE c.idUser = :idUser)")
	List<Car> listCarsByUserId(@Param("idUser") Long idUser);
	
	@RestResource(exported = false)
	@Query("SELECT c.isDefault FROM Car c WHERE c.idUser = :idUser AND c.idCar = :idCar)")
	boolean checkDefault(@Param("idUser") Long idUser, @Param("idCar") Long idCar);
	
}
