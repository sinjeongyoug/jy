package com.sbs.sjy.jy.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.sjy.jy.dto.Article;
import com.sbs.sjy.jy.dto.Board;


@Mapper
public interface ArticleDao {
	List<Article> getForPrintArticles();

	Article getForPrintArticleById(@Param("id") int id);

	Article getArticleById(@Param("id") int id);

	void write(Map<String, Object> param);
	
	void modify(Map<String, Object> param);
	
	Board getBoardByCode(String boardCode);

	void increaseArticleHit(@Param("id") int id);
	
	int getLikePointByMemberId(@Param("id") int id, @Param("memberId") int memberId);

	void cancelLikeArticle(@Param("id") int id, @Param("memberId") int memberId);

	void likeArticle(@Param("id") int id, @Param("memberId") int memberId);
	
	Article getForPrintArticle(int id);

	int getLikePoint(int id);

	int getArticlesCount(Map<String, Object> param);

	Board getBoardByBoardCode(String boardCode);

	List<Article> getForPrintArticles(Map<String, Object> param);
	
}