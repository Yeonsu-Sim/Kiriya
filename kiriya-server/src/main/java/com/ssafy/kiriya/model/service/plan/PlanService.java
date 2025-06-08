package com.ssafy.kiriya.model.service.plan;

import java.util.List;

import com.ssafy.kiriya.model.dto.Plan;

public interface PlanService {
	
    public int add(Plan plan);
    
    public Plan getByPno(int pno);
    
    public List<Plan> getByTno(int mno);
    
    public int set(Plan plan);
    
    public int remove(int pno);
    
}
