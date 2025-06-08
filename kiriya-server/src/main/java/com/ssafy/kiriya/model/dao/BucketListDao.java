package com.ssafy.kiriya.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.kiriya.model.dto.BucketList;

public interface BucketListDao {
	
    int[] add(BucketList bucketList) throws SQLException;
    
    BucketList get(int mno, int ano) throws SQLException;
    
    List<BucketList> getByMember(int mno) throws SQLException;
    
    int remove(int mno, int ano) throws SQLException;
    
    int remove(int mno) throws SQLException;
}
