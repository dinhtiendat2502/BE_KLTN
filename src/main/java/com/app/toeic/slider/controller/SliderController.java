package com.app.toeic.slider.controller;

import com.app.toeic.external.service.FirebaseStorageService;
import com.app.toeic.slider.model.Slider;
import com.app.toeic.slider.repo.SliderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/slider")
public class SliderController {
    private final SliderRepository sliderRepository;
    private final FirebaseStorageService firebaseStorageService;

    @GetMapping("/all")
    public Object getAllSlider(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return sliderRepository.findAll(PageRequest.of(page - 1, size));
    }

    @PostMapping("/add")
    public Object addSlider(@RequestParam("file") MultipartFile file) throws
            IOException {
        if (file == null) return "FAIL";
        var image = firebaseStorageService.uploadFile(file);
        var lastSlider = sliderRepository
                .findLastByPosition();
        var position = 1;
        if (lastSlider.isPresent()) {
            position = lastSlider.get().getPosition() + 1;
        }
        var newSlider = Slider
                .builder()
                .image(image)
                .position(position)
                .build();
        sliderRepository.save(newSlider);
        return "OK";
    }

    @DeleteMapping("/delete/{sliderId}")
    public Object deleteSlider(@PathVariable("sliderId") Long sliderId) {
        var slider = sliderRepository
                .findById(sliderId)
                .orElse(null);
        if (slider == null) return "FAIL";
        var position = slider.getPosition();
        sliderRepository.deleteById(sliderId);
        // update position of all slider after deleted slider
        sliderRepository.updateAllByPositionGreaterThanOrderByPosition(position);
        return "OK";
    }

    // action = up | down
    @PatchMapping("/update/{sliderId}/{position}/{action}")
    public Object updateSlider(
            @PathVariable("sliderId") Long sliderId,
            @PathVariable("position") Integer position,
            @PathVariable("action") String action
    ) {
        var lastSlider = sliderRepository
                .findLastByPosition();

        if (lastSlider.isPresent()
                && "down".equals(action)
                && Objects.equals(lastSlider.get().getPosition(), position)) {
            return "OK";
        }
        if ("up".equals(action)
                && Objects.equals(1, position)) {
            return "OK";
        }

        var slider = sliderRepository
                .findById(sliderId)
                .orElseThrow();
        Slider nextSlider;
        var tempImg = slider.getImage();
        if (action.equals("up")) {
            nextSlider = sliderRepository
                    .findByPosition(position - 1)
                    .orElseThrow();
        } else {
            nextSlider = sliderRepository
                    .findByPosition(position + 1)
                    .orElseThrow();
        }
        slider.setImage(nextSlider.getImage());
        nextSlider.setImage(tempImg);
        sliderRepository.save(nextSlider);
        sliderRepository.save(slider);
        return "OK";
    }
}
