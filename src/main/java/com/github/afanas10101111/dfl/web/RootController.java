package com.github.afanas10101111.dfl.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@ApiIgnore
@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        log.info("root");
        return "index";
    }
}
