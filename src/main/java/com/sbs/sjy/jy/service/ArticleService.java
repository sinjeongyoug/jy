package com.sbs.sjy.jy.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.sjy.jy.dao.ArticleDao;
import com.sbs.sjy.jy.dto.Article;
import com.sbs.sjy.jy.util.Util;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;

	public List<Article> getForPrintArticles() {
		List<Article> articles = articleDao.getForPrintArticles();

		return articles;
	}

	public Article getForPrintArticleById(int id) {
		Article article = articleDao.getForPrintArticleById(id);

		return article;
	}

	public int write(Map<String, Object> param) {
		articleDao.write(param);

		return Util.getAsInt(param.get("id"));
	}
}