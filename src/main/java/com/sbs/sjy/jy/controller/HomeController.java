package com.sbs.sjy.jy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
	@RequestMapping("home/main")
	@ResponseBody
	public String showMain() {
		return "안녕";
	}
}
