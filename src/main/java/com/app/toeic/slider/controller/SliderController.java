package com.app.toeic.slider.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.FirebaseStorageService;
import com.app.toeic.slider.model.Slider;
import com.app.toeic.slider.payload.SliderDTO;
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
        var sliders = sliderRepository
                .findAllByPositionGreaterThanOrderByPosition(position);
        for (var s : sliders) {
            s.setPosition(s.getPosition() - 1);
            sliderRepository.save(s);
        }
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
        } else if ("up".equals(action)
                && Objects.equals(1, position)) {
            return "OK";
        }

        var slider = sliderRepository
                .findById(sliderId)
                .orElseThrow();
        Slider nextSlider;
        if (action.equals("up")) {
            slider.setPosition(slider.getPosition() - 1);
            nextSlider = sliderRepository
                    .findByPosition(slider.getPosition() - 1)
                    .orElseThrow();
        } else {
            slider.setPosition(slider.getPosition() + 1);
            nextSlider = sliderRepository
                    .findByPosition(slider.getPosition() + 1)
                    .orElseThrow();
        }
        nextSlider.setPosition(position);
        sliderRepository.save(nextSlider);
        sliderRepository.save(slider);
        return "OK";
    }
}
