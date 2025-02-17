package ins.app.controllers;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @GetMapping("/dummy")
    public String[] GetTime() {
        String time = LocalDateTime.now().toString();
        return new String[] {time};
    }
}
