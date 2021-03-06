package com.casosemergencias.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.casosemergencias.logic.UserService;

@Controller
@RequestMapping("/private")
public class PageController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value={"/hello"}, method = RequestMethod.GET)
	public ModelAndView hello(HttpServletRequest request, HttpServletResponse response, @RequestParam("param")String param) {
		
		System.out.println("Parametro: " + param);
		
		ModelAndView model = new ModelAndView();
		model.addObject("name",  "Holaa");
		model.setViewName("private/hello");
		return model;
		
	}
	
}
