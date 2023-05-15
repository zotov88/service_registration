package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainMVCController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
