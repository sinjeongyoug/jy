package com.sbs.sjy.jy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.sjy.jy.dto.Article;

@Mapper
public interface ArticleDao {
	List<Article> getForPrintArticles();
	
}
