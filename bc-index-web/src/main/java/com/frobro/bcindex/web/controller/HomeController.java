package com.frobro.bcindex.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rise on 3/21/17.
 */
@Controller
public class HomeController {

  @RequestMapping("/")
  public String index() {
    return "home";
  }
}
