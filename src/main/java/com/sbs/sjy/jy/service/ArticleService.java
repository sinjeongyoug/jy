package com.sbs.sjy.jy.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.sjy.jy.dao.ArticleDao;
import com.sbs.sjy.jy.dto.Article;
import com.sbs.sjy.jy.dto.Board;
import com.sbs.sjy.jy.dto.File;
import com.sbs.sjy.jy.dto.Member;
import com.sbs.sjy.jy.dto.ResultData;
import com.sbs.sjy.jy.util.Util;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private FileService fileService;

	public List<Article> getForPrintArticles() {
		List<Article> articles = articleDao.getForPrintArticles();

		return articles;
	}

	private void updateForPrintInfo(Member actor, Article article) {
		Util.putExtraVal(article, "actorCanDelete", actorCanDelete(actor, article));
		Util.putExtraVal(article, "actorCanModify", actorCanModify(actor, article));
	}

	// 액터가 해당 댓글을 수정할 수 있는지 알려준다.
	public boolean actorCanModify(Member actor, Article article) {
		return actor != null && actor.getId() == article.getMemberId() ? true : false;
	}

	// 액터가 해당 댓글을 삭제할 수 있는지 알려준다.
	public boolean actorCanDelete(Member actor, Article article) {
		return actorCanModify(actor, article);
	}

	public Article getForPrintArticleById(Member actor, int id) {
		Article article = articleDao.getForPrintArticleById(id);

		updateForPrintInfo(actor, article);

		List<File> files = fileService.getFiles("article", article.getId(), "common", "attachment");

		Map<String, File> filesMap = new HashMap<>();

		for (File file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}

		Util.putExtraVal(article, "file__common__attachment", filesMap);

		return article;
	}

	public int write(Map<String, Object> param) {
		articleDao.write(param);
		int id = Util.getAsInt(param.get("id"));

		String fileIdsStr = (String) param.get("fileIdsStr");

		if (fileIdsStr != null && fileIdsStr.length() > 0) {
			fileIdsStr = fileIdsStr.trim();

			if (fileIdsStr.startsWith(",")) {
				fileIdsStr = fileIdsStr.substring(1);
			}
		}

		if (fileIdsStr != null && fileIdsStr.length() > 0) {
			fileIdsStr = fileIdsStr.trim();

			if (fileIdsStr.equals(",")) {
				fileIdsStr = "";
			}
		}

		if (fileIdsStr != null && fileIdsStr.length() > 0) {
			List<Integer> fileIds = Arrays.asList(fileIdsStr.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());

			// 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 relId가 일단 0으로 저장된다.
			// 그것을 뒤늦게라도 이렇게 고처야 한다.
			for (int fileId : fileIds) {
				fileService.changeRelId(fileId, id);
			}
		}

		return id;
	}

	public boolean actorCanModify(Member actor, int id) {
		Article article = articleDao.getArticleById(id);

		return actorCanModify(actor, article);
	}

	public ResultData checkActorCanModify(Member actor, int id) {
		boolean actorCanModify = actorCanModify(actor, id);

		if (actorCanModify) {
			return new ResultData("S-1", "가능합니다.", "id", id);
		}

		return new ResultData("F-1", "권한이 없습니다.", "id", id);
	}

	public void modify(Map<String, Object> param) {
		articleDao.modify(param);

		int id = Util.getAsInt(param.get("id"));

		String fileIdsStr = (String) param.get("fileIdsStr");

		if (fileIdsStr != null && fileIdsStr.length() > 0) {
			fileIdsStr = fileIdsStr.trim();

			if (fileIdsStr.startsWith(",")) {
				fileIdsStr = fileIdsStr.substring(1);
			}
		}

		if (fileIdsStr != null && fileIdsStr.length() > 0) {
			List<Integer> fileIds = Arrays.asList(fileIdsStr.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());

			// 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 relId가 일단 0으로 저장된다.
			// 그것을 뒤늦게라도 이렇게 고처야 한다.
			for (int fileId : fileIds) {
				fileService.changeRelId(fileId, id);
			}
		}
		
	}

	public Board getBoardByCode(String boardCode) {
		return articleDao.getBoardByCode(boardCode);
	}

	public Map<String, Object> getArticleLikeAvailable(int id, int actorMemberId) {
		Article article = articleDao.getArticleById(id);
		
		Map<String, Object> rs = new HashMap<>();
		
		if (article.getMemberId() == actorMemberId) {
			rs.put("resultCode", "F-1");
			rs.put("msg", "본인은 추천 할 수 없습니다.");
			return rs;
			
		}
		
		int likePoint = articleDao.getLikePointByMemberId(id, actorMemberId);
		if (likePoint > 0) {
			rs.put("resultCode", "F-2");
			rs.put("msg", "이미 좋아요를 하셨습니다.");
			return rs;
		}
		
		rs.put("resultCode", "S-1");
		rs.put("msg", "가능합니다.");
		return rs;
	}


	public Map<String, Object> likeArticle(int id, int actorMemberId) {
		articleDao.likeArticle(id, actorMemberId);
		Map<String, Object> rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("msg", String.format("%d번 게시물을 추천하였습니다.", id));

		return rs;
	}

	public Map<String, Object> getArticleCancelLikeAvailable(int id, int actorMemberId) {
		Map<String, Object> rs = new HashMap<>();

		int likePoint = articleDao.getLikePointByMemberId(id, actorMemberId);

		if (likePoint == 0) {
			rs.put("resultCode", "F-1");
			rs.put("msg", "추천하신 분만 취소가 가능합니다.");

			return rs;
		}

		rs.put("resultCode", "S-1");
		rs.put("msg", "가능합니다.");

		return rs;
	}


	public Map<String, Object> cancelLikeArticle(int id, int actorMemberId) {
		articleDao.cancelLikeArticle(id, actorMemberId);

		Map<String, Object> rs = new HashMap<>();

		rs.put("resultCode", "S-1");
		rs.put("msg", String.format("%d번 게시물 추천을 취소하였습니다.", id));

		return rs;
	}

	public void increaseArtuckeHit(int id) {
		articleDao.increaseArticleHit(id);		
	}
}