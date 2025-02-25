package ins.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ins.bl.dtos.PhotoDto;
import ins.bl.dtos.PhotoUploadDto;
import ins.bl.services.PhotoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{id}")
    public PhotoDto GetPhoto(@PathVariable(required = true) long id) {
        return photoService.getPhoto(id);
    }

    @PostMapping("/post")
    public PhotoDto AddPhoto(@RequestBody @Valid PhotoUploadDto photo) {
        return photoService.savePhoto(photo);
    }
}
