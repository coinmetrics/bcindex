package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.TimerService;
import com.frobro.bcindex.web.service.persistence.EvenIdxRepo;
import com.frobro.bcindex.web.service.persistence.IndexRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
