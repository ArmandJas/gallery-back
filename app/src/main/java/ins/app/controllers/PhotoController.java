package ins.app.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ins.app.dtos.PhotoDto;
import ins.app.dtos.PhotoUploadRequest;
import ins.app.services.PhotoService;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{id}")
    public PhotoDto getPhoto(@PathVariable long id) {
        return photoService.getPhoto(id);
    }

    @PostMapping(value = "/post")
    public PhotoDto addPhoto(@ModelAttribute @Validated PhotoUploadRequest photo) {
        return photoService.savePhoto(photo);
    }
}
