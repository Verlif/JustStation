package idea.verlif.justtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 11:20
 */
@RestController
public class TestController {

    @RequestMapping("/")
    public String index() {
        return "Hello World!!!";
    }
}
