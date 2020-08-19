package com.sbs.sjy.jy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.sjy.jy.dao.ArticleDao;
import com.sbs.sjy.jy.dto.Article;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	
	public List<Article> getForPrintArticles() {
		List<Article> articles = articleDao.getForPrintArticles();

		return articles;
	}

	public Article getForPrintArticlesById(int id) {
		Article article = articleDao.getForPrintArticles(id);
		
		return article;
	}
}
