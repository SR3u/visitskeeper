package sr3u.showvisitskeeper.controller.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sr3u.showvisitskeeper.service.CompositionService;
import sr3u.showvisitskeeper.service.CompositionTypeService;
import sr3u.showvisitskeeper.service.PersonService;
import sr3u.showvisitskeeper.service.VenueService;
import sr3u.showvisitskeeper.service.VisitService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    PersonService personService;
    @Autowired
    CompositionService compositionService;
    @Autowired
    CompositionTypeService compositionTypeService;
    @Autowired
    VisitService visitService;
    @Autowired
    VenueService venueService;

    @GetMapping("/person")
    public Object person(@RequestParam(name = "id") UUID id) {
        return personService.personInfo(id);
    }

    @GetMapping("/composition")
    public Object composition(@RequestParam(name = "id") UUID id) {
        return compositionService.compositionInfo(id);
    }

    @GetMapping("/composition_type")
    public Object compositionType(@RequestParam(name = "id") UUID id) {
        return compositionTypeService.compositionTypeInfo(id);
    }

    @GetMapping("/visit")
    public Object visit(@RequestParam(name = "id") UUID id) {
        return visitService.visitInfo(id);
    }

    @GetMapping("/visit/search")
    public List<?> searchVisit(@RequestParam(name = "id", required = false) UUID id,
                         @RequestParam(name = "venueId", required = false) UUID venueId,
                         @RequestParam(name = "directorId", required = false) UUID directorId,
                         @RequestParam(name = "conductorId", required = false) UUID conductorId,
                         @RequestParam(name = "artistId", required = false) UUID artistId,
                         @RequestParam(name = "attendeeId", required = false) UUID attendeeId,
                         @RequestParam(name = "composerId", required = false) UUID composerId,
                         @RequestParam(name = "compositionId", required = false) UUID compositionId) {
        return visitService.find(id, venueId, directorId, conductorId, composerId,compositionId, artistId, attendeeId);
    }

    @GetMapping("/composition/search")
    public List<?> searchComposition(@RequestParam(name = "id", required = false) UUID id,
                         @RequestParam(name = "venueId", required = false) UUID venueId,
                         @RequestParam(name = "directorId", required = false) UUID directorId,
                         @RequestParam(name = "conductorId", required = false) UUID conductorId,
                         @RequestParam(name = "artistId", required = false) UUID artistId,
                         @RequestParam(name = "attendeeId", required = false) UUID attendeeId,
                         @RequestParam(name = "composerId", required = false) UUID composerId,
                         @RequestParam(name = "compositionId", required = false) UUID visitId) {
        return compositionService.find(id, venueId, directorId, conductorId, composerId,visitId, artistId, attendeeId);
    }

    @GetMapping("/venue")
    public Object venue(@RequestParam(name = "id") UUID id) {
        return venueService.venueInfo(id);
    }


}
