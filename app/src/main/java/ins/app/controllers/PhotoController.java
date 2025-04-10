package ins.app.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ins.app.dtos.PhotoPageRequest;
import ins.app.dtos.PhotoPageResponse;
import ins.app.dtos.PhotoResponse;
import ins.app.dtos.PhotoSaveRequest;
import ins.app.services.PhotoService;
import ins.app.utilities.markers.CreateRequestSequence;
import ins.app.utilities.markers.EditRequestSequence;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    private final PhotoService photoService;

    private static final HttpHeaders IMAGE_HEADERS = new HttpHeaders();

    static {
        IMAGE_HEADERS.add("Content-Type", "image/png");
    }

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{id}")
    public PhotoResponse get(@PathVariable long id) {
        return photoService.get(id);
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        return new ResponseEntity<>(photoService.getImage(id), IMAGE_HEADERS, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable long id) {
        return new ResponseEntity<>(photoService.getThumbnail(id), IMAGE_HEADERS, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        photoService.delete(id);
    }

    @PostMapping("/search")
    public PhotoPageResponse search(@RequestBody PhotoPageRequest photoPageRequest) {
        return photoService.search(photoPageRequest);
    }

    @PostMapping(value = "/post")
    public PhotoResponse saveNew(
            @Validated(CreateRequestSequence.class)
            @ModelAttribute PhotoSaveRequest photo) {
        return photoService.save(photo);
    }

    @PutMapping(value = "/edit")
    public PhotoResponse edit(
            @Validated(EditRequestSequence.class)
            @ModelAttribute PhotoSaveRequest photo) {
        return photoService.save(photo);
    }
}
