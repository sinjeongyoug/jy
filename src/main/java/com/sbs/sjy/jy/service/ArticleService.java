package com.sbs.sjy.jy.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbs.sjy.jy.dao.ArticleDao;
import com.sbs.sjy.jy.dto.Article;
import com.sbs.sjy.jy.dto.ArticleReply;
import com.sbs.sjy.jy.dto.Member;
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

	public int writeReply(Map<String, Object> param) {
		articleDao.writeReply(param);

		return Util.getAsInt(param.get("id"));
	}

	public List<ArticleReply> getForPrintArticleReplies(@RequestParam Map<String, Object> param) {
		List<ArticleReply> articleReplies = articleDao.getForPrintArticleReplies(param);
		
		Member actor = (Member)param.get("actor");
		
		for ( ArticleReply articleReply : articleReplies ) {
			// 출력용 부가 데이터를 추가함.
			updateForPrintInfo(actor , articleReply);
			
		}
		
		return articleReplies;
		
	}

	private void updateForPrintInfo(Member actor, ArticleReply articleReply) {
		articleReply.getExtra().put("actorCanDelete", actorCanDelete(actor, articleReply));
		articleReply.getExtra().put("actorCanUpdate", actorCanUpdate(actor, articleReply));
		
		
	}
	
	//해당 댓글의 삭제를 할 수 있는 지 알려줌
	private Object actorCanUpdate(Member actor, ArticleReply articleReply) {
		return actor != null && actor.getId() == articleReply.getMemberId() ? true : false;
	}

	// 액터가 해당 댓글을 삭제할 수 있는지 알려준다.
	private Object actorCanDelete(Member actor, ArticleReply articleReply) {
		return actorCanUpdate(actor, articleReply);
	}


	public void deleteReply(int id) {
		articleDao.deleteReply(id);
	}
	
	
}