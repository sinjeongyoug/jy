package com.sbs.sjy.jy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbs.sjy.jy.dto.Article;
import com.sbs.sjy.jy.service.ArticleService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/article/list")
	public String showList(Model model) {
		int count = articleService.getCount();
		List<Article> articles = articleService.getForPrintArticles();
		
		model.addAttribute("count", count);
		model.addAttribute("articles", articles);
		
		return "article/list";
	}
}
