package com.sbs.sjy.jy.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDao {

	void save(Map<String, Object> param);

}