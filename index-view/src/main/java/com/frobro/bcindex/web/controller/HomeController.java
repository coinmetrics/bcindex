package com.frobro.bcindex.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Created by rise on 3/21/17.
 */
@Controller
public class HomeController {


  @RequestMapping("/")
  public String index(Model model) throws IOException {
    return renderHome(model);
  }

  @RequestMapping("/home")
  public String home(Model model) throws IOException {
    return renderHome(model);
  }

  private String renderHome(Model model) throws IOException {
    return "home";
  }
}
