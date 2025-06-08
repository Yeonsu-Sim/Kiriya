package com.ssafy.kiriya.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Trip;

@Mapper
public interface TripDao {
	
	public int add(Trip trip);
	
	public Trip get(int tno);
	
	public List<Trip> getAll();

	public List<Trip> getByMno(int mno);
	
	public int set(Trip trip);
	
	public int remove(int tno);
	
	public int copyTrip(Map<String, Object> param);
	
	public List<Trip> getByGno(int gno);
}
