package com.sbs.sjy.jy.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbs.sjy.jy.dao.ArticleDao;
import com.sbs.sjy.jy.dto.Article;
import com.sbs.sjy.jy.dto.Reply;
import com.sbs.sjy.jy.dto.Member;
import com.sbs.sjy.jy.dto.ResultData;
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

	public List<Reply> getForPrintReplies(@RequestParam Map<String, Object> param) {
		List<Reply> replies = articleDao.getForPrintReplies(param);
		
		Member actor = (Member)param.get("actor");
		
		for ( Reply reply : replies ) {
			// 출력용 부가 데이터를 추가함.
			updateForPrintInfo(actor, reply);
			
		}
		
		return replies;
		
	}

	private void updateForPrintInfo(Member actor, Reply reply) {
		reply.getExtra().put("actorCanDelete", actorCanDelete(actor, reply));
		reply.getExtra().put("actorCanModify", actorCanModify(actor, reply));
	}
	
	//해당 댓글의 삭제를 할 수 있는 지 알려줌
	public boolean actorCanModify(Member actor, Reply reply) {
		return actor != null && actor.getId() == reply.getMemberId() ? true : false;
	}

	// 액터가 해당 댓글을 삭제할 수 있는지 알려준다.
	public boolean actorCanDelete(Member actor, Reply reply) {
		return actorCanModify(actor, reply);
	}


	public void deleteReply(int id) {
		articleDao.deleteReply(id);
	}

	public Reply getForPrintReplyById(int id) {
		return articleDao.getForPrintReplyById(id);
	}

	public ResultData modfiyReply(Map<String, Object> param) {
		articleDao.modifyReply(param);
		return new ResultData("S-1", String.format("%d번 댓글을 수정하였습니다.", Util.getAsInt(param.get("id"))), param);
	}
}