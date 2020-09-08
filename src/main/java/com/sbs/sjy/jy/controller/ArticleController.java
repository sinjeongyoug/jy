package com.sbs.sjy.jy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.sjy.jy.dto.Article;
import com.sbs.sjy.jy.dto.Board;
import com.sbs.sjy.jy.dto.Member;
import com.sbs.sjy.jy.dto.ResultData;
import com.sbs.sjy.jy.service.ArticleService;
import com.sbs.sjy.jy.util.Util;


@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;

	@RequestMapping("/usr/article/{boardCode}-list")
	public String showList(Model model, @PathVariable("boardCode") String boardCode, String searchKeyword, String searchType,
			@RequestParam(value = "page", defaultValue = "1") int page, HttpServletRequest request) {
		int loginedMemberId = (int) request.getAttribute("loginedMemberId");
		Board board = articleService.getBoardByCode(boardCode);
		
		if ( searchType != null ) {
			searchType = searchType.trim();
		}
		
		if ( searchKeyword != null ) {
			searchKeyword = searchKeyword.trim();
		}

		Map<String, Object> getForPrintArticlesByParam = new HashMap();
		getForPrintArticlesByParam.put("boardCode", boardCode);
		getForPrintArticlesByParam.put("actorMemberId", loginedMemberId);
		getForPrintArticlesByParam.put("searchKeyword", searchKeyword);
		getForPrintArticlesByParam.put("searchType", searchType);

		int pageItemsCount = 10;

		int limitCount = pageItemsCount;
		int limitFrom = (page - 1) * pageItemsCount;
		getForPrintArticlesByParam.put("limitCount", limitCount);
		getForPrintArticlesByParam.put("limitFrom", limitFrom);
		int totalCount = articleService.getArticlesCount(getForPrintArticlesByParam);
		int totalPage = (int) Math.ceil((double) totalCount / pageItemsCount);
		List<Article> articles = articleService.getForPrintArticlesByParam(getForPrintArticlesByParam);

		model.addAttribute("articles", articles);
		model.addAttribute("board", board);

		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPage", totalPage);

		int pageBoundSize = 5;
		int pageStartsWith = page - pageBoundSize;
		if (pageStartsWith < 1) {
			pageStartsWith = 1;
		}
		int pageEndsWith = page + pageBoundSize;
		if (pageEndsWith > totalPage) {
			pageEndsWith = totalPage;
		}

		model.addAttribute("pageStartsWith", pageStartsWith);
		model.addAttribute("pageEndsWith", pageEndsWith);

		boolean beforeMorePages = pageStartsWith > 1;
		boolean afterMorePages = pageEndsWith < totalPage;

		model.addAttribute("beforeMorePages", beforeMorePages);
		model.addAttribute("afterMorePages", afterMorePages);
		model.addAttribute("pageBoundSize", pageBoundSize);

		model.addAttribute("needToShowPageBtnToFirst", page != 1);
		model.addAttribute("needToShowPageBtnToLast", page != totalPage);
		
		model.addAttribute("board", board);

		model.addAttribute("articles", articles);

		return "article/list";
	}

	@RequestMapping("/usr/article/{boardCode}-detail")
	public String showDetail(Model model, @RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, String listUrl) {
		
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		
		model.addAttribute("listUrl", listUrl);
		
		Board board = articleService.getBoardByCode(boardCode);
		
		model.addAttribute("board", board);
		
		int id = Integer.parseInt((String) param.get("id"));
		
		articleService.increaseArtuckeHit(id);
		
		Member loginedMember = (Member)req.getAttribute("loginedMember");
		
		Article article = articleService.getForPrintArticleById(loginedMember, id);

		model.addAttribute("article", article);

		return "article/detail";
		
	}
	
	@RequestMapping("/usr/article/{boardCode}-modify")
	public String showModify(Model model, @RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, String listUrl) {
		model.addAttribute("listUrl", listUrl);
		
		Board board = articleService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		int id = Integer.parseInt((String) param.get("id"));
		
		Member loginedMember = (Member)req.getAttribute("loginedMember");
		Article article = articleService.getForPrintArticleById(loginedMember, id);

		model.addAttribute("article", article);

		return "article/modify";
	}

	@RequestMapping("/usr/article/{boardCode}-write")
	public String showWrite(@PathVariable("boardCode") String boardCode, Model model, String listUrl) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		Board board = articleService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		return "article/write";
	}
	
	@RequestMapping("/usr/article/{boardCode}-doModify")
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req, int id, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = articleService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		Map<String, Object> newParam = Util.getNewMapOf(param, "title", "body", "fileIdsStr", "articleId", "id");
		Member loginedMember = (Member)req.getAttribute("loginedMember");
		
		ResultData checkActorCanModifyResultData = articleService.checkActorCanModify(loginedMember, id);
		
		if (checkActorCanModifyResultData.isFail() ) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", checkActorCanModifyResultData.getMsg());
			
			return "common/redirect";
		}
		
		articleService.modify(newParam);
		
		String redirectUri = (String) param.get("redirectUri");

		return "redirect:" + redirectUri;
	}

	@RequestMapping("/usr/article/{boardCode}-doWrite")
	public String doWrite(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = articleService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		Map<String, Object> newParam = Util.getNewMapOf(param, "title", "body", "fileIdsStr");
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		newParam.put("boardId", board.getId());
		newParam.put("memberId", loginedMemberId);
		int newArticleId = articleService.write(newParam);

		String redirectUri = (String) param.get("redirectUri");
		redirectUri = redirectUri.replace("#id", newArticleId + "");

		return "redirect:" + redirectUri;
	}
	
	@RequestMapping("/usr/article/doCancelLike")
	public String doCancelLike(Model model, int id, String redirectUrl, HttpServletRequest request) {

		int loginedMemberId = (int) request.getAttribute("loginedMemberId");

		Map<String, Object> articleCancelLikeAvailable = articleService.getArticleCancelLikeAvailable(id, loginedMemberId);

		if (((String) articleCancelLikeAvailable.get("resultCode")).startsWith("F-")) {
			model.addAttribute("alertMsg", articleCancelLikeAvailable.get("msg"));
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}
		
		

		Map<String, Object> rs = articleService.cancelLikeArticle(id, loginedMemberId);

		String msg = (String) rs.get("msg");

		model.addAttribute("alertMsg", msg);
		model.addAttribute("locationReplace", redirectUrl);

		return "common/redirect";
	}
	
	@RequestMapping("/usr/article/doLike")
	public String doLike(Model model, int id, String redirectUrl, HttpServletRequest request) {

		int loginedMemberId = (int) request.getAttribute("loginedMemberId");

		Map<String, Object> articleLikeAvailableRs = articleService.getArticleLikeAvailable(id, loginedMemberId);
		
		if (((String) articleLikeAvailableRs.get("resultCode")).startsWith("F-")) {
			model.addAttribute("alertMsg", articleLikeAvailableRs.get("msg"));
			
			model.addAttribute("historyBack", true);
			return "common/redirect";
		}
		
		Map<String, Object> rs = articleService.likeArticle(id, loginedMemberId);
		
		String msg = (String) rs.get("msg");
	
		model.addAttribute("alertMsg", msg);
		model.addAttribute("locationReplace", redirectUrl);

		return "common/redirect";
	}
	
	@RequestMapping("/usr/article/doLikeAjax")
	@ResponseBody
	public Map<String, Object> doLikeAjax(int id, HttpServletRequest request) {

		Map<String, Object> rs = new HashMap<>();
		int loginedMemberId = (int) request.getAttribute("loginedMemberId");

		Map<String, Object> articleLikeAvailableRs = articleService.getArticleLikeAvailable(id, loginedMemberId);

		if (((String) articleLikeAvailableRs.get("resultCode")).startsWith("F-")) {
			rs.put("resultCode", articleLikeAvailableRs.get("resultCode"));
			rs.put("msg", articleLikeAvailableRs.get("msg"));

			return rs;
		}

		Map<String, Object> likeArticleRs = articleService.likeArticle(id, loginedMemberId);

		String resultCode = (String) likeArticleRs.get("resultCode");
		String msg = (String) likeArticleRs.get("msg");

		int likePoint = articleService.getLikePoint(id);

		rs.put("resultCode", resultCode);
		rs.put("msg", msg);
		rs.put("likePoint", likePoint);

		return rs;
	}
}