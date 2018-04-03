package com.docscarsexample.controller;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.docscarsexample.domain.Car;
import com.docscarsexample.repository.CarRepository;


@RepositoryRestController
public class CarController {
	
	@Autowired
	private CarRepository carRepository;
	
	@RequestMapping(value = "/cars", method = RequestMethod.POST)
	public ResponseEntity<Car> createCar(@RequestBody @Valid Car car) {
		
		return new ResponseEntity<>(carRepository.save(car), HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/cars/delete/{idCar}", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteCarFromService(@PathVariable("idCar") Long idCar, @RequestHeader HttpHeaders headers) {

		Long idUser = Long.parseLong(headers.getFirst("iduser"));
		
		if (carRepository.checkDefault(idUser, idCar)) throw new ValidationException("Default card cannot be deleted");
		Long id = carRepository.getIdFromIdCar(idCar);
		
		carRepository.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	

	@RequestMapping(value = "/cars/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> listByUserId(@PathVariable("id") Long id){
		
		return ResponseEntity.ok(carRepository.listCarsByUserId(id));
	}
	
}
