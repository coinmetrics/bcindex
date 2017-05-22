package com.frobro.bcindex.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rise on 4/22/17.
 */
@Controller
public class StaticPageController {

  @RequestMapping("/about")
  public String about(Model model) {
    return "about";
  }

  @RequestMapping("/contact")
  public String contact(Model model) {
    return "contact";
  }

  @RequestMapping("/methodology")
  public String methodology(Model model) {
    return "methodology";
  }
}
