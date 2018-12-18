package stenden.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import stenden.spring.data.jpa.TrackRepository;
import stenden.spring.data.model.Track;

import java.util.List;

@CrossOrigin
@RestController
public class TrackController {

    private final TrackRepository trackRepository;

    @Autowired
    public TrackController(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @GetMapping("/api/tracks")
    public List<Track> index(){
        return trackRepository.findAll();
    }
}
