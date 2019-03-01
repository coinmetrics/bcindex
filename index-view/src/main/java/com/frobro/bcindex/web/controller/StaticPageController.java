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

  @RequestMapping("/annc")
  public String annc(Model model) {
    return "annc";
  }

  @RequestMapping("downloads")
  public String downloads(Model model) {
    return "downloads";
  }

  @RequestMapping("/blog")
  public String blog() {
    return "blog";
  }

  @RequestMapping("/blog/idx_perf_post")
  public String idxPerfPost() {
    return "idx_perf_post";
  }

  @RequestMapping("/blog/blog_5_20_2018")
  public String blog_5_20_2018() {
    return "blog_5_20_2018";
  }

  @RequestMapping("/blog/blog_5_27_2018")
  public String blog_5_27_2018() {
    return "blog_5_27_2018";
  }

  @RequestMapping("/blog/blog_6_3_2018")
  public String blog_6_3_2018() {
    return "blog_6_3_2018";
  }

  @RequestMapping("/blog/blog_july_2018")
  public String blog_july_2018() {
    return "blog_july_2018";
  }

  @RequestMapping("/blog/blog_august_2018")
  public String blog_august_2018() {
    return "blog_august_2018";
  }

  @RequestMapping("/blog/blog_september_2018")
  public String blog_september_2018() {
  return "blog_september_2018";
  }

  @RequestMapping("/blog/blog_october_2018")
  public String blog_october_2018() {
  return "blog_october_2018";
  }

  @RequestMapping("/blog/blog_november_2018")
  public String blog_november_2018() {
  return "blog_november_2018";
  }

  @RequestMapping("/blog/blog_december_2018")
  public String blog_december_2018() {
  return "blog_december_2018";
  }

  @RequestMapping("/blog/blog_january_2019")
  public String blog_january_2019() {
  return "blog_january_2019";
  }

  @RequestMapping("/blog/blog_february_2019")
  public String blog_february_2019() {
  return "blog_february_2019";
  }

  @RequestMapping("/blog/blog_march_2019")
  public String blog_march_2019() {
  return "blog_march_2019";
  }
}
