package com.app.toeic.slider.controller;

import com.app.toeic.slider.model.Slider;
import com.app.toeic.slider.payload.SliderDto;
import com.app.toeic.slider.repo.SliderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/slider")
public class SliderController {
    private final SliderRepository sliderRepository;

    @GetMapping("/all")
    public Object getAllSlider(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return sliderRepository.findAll(PageRequest.of(page - 1, size));
    }

    @PostMapping("/add")
    public Object addSlider(@RequestBody SliderDto slider) {
        var lastSlider = sliderRepository
                .findLastByPosition();
        var position = 1;
        if (lastSlider.isPresent()) {
            position = lastSlider.get().getPosition() + 1;
        }

        var newSlider = Slider
                .builder()
                .image(slider.getImage())
                .position(position)
                .build();
        sliderRepository.save(newSlider);
        return "OK";
    }

    @DeleteMapping("/delete/{sliderId}")
    public Object deleteSlider(@PathVariable("sliderId") Long sliderId) {
        sliderRepository.deleteById(sliderId);
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
        Slider nextSlider = null;
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
