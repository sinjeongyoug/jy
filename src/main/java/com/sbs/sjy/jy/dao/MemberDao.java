package com.sbs.sjy.jy.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.sjy.jy.dto.Member;

@Mapper
public interface MemberDao {

	Member getMemberById(@Param("id") int id);
	
}
