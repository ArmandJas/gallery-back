package ins.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class DummyController {
    @GetMapping("/dummy")
    public Timestamp GetTime() {
        return new Timestamp(System.currentTimeMillis());
    }
}
