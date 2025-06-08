package com.ssafy.kiriya.model.service.trip;

import java.util.List;

import com.ssafy.kiriya.model.dto.Trip;

public interface TripService {
	
	public void add(Trip trip);
	
	public Trip get(int tno);
	
	public List<Trip> getAll();
	
	public List<Trip> getByMno(int mno);
	
	public void update(Trip trip);
	
	public void delete(int tno);
	
	public void addTripByCopy(int originalTno, int gno);
	
	public List<Trip> getByGroup(int gno);
}
