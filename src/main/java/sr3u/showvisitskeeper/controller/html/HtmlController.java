package sr3u.showvisitskeeper.controller.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.CompositionTypeRepository;
import sr3u.showvisitskeeper.repo.PersonRepository;
import sr3u.showvisitskeeper.repo.VenueRepository;
import sr3u.showvisitskeeper.repo.VisitRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/html")
public class HtmlController {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    CompositionRepository compositionRepository;
    @Autowired
    CompositionTypeRepository compositionTypeRepository;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    VenueRepository venueRepository;

    @GetMapping("/person")
    public String personHtml(@RequestParam(name = "id") UUID personId) {
        StringBuilder res = new StringBuilder("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>");
        res.append(backToSearch());
        PersonEntity person = personRepository.findById(personId).orElseThrow(NotFoundException::new);
        res.append("<h2 title=\"").append(person.getDisplayName()).append("\">")
                .append(person.getDisplayName()).append(" (").append(person.getType()).append(")")
                .append("</h2>");
        appendCompositions(res, "Произведения", compositionRepository.findByComposerId(personId));
        appendVisits(res, "Режиссировал", visitRepository.findByDirectorIdIn(List.of(personId)));
        appendVisits(res, "Дирижировал", visitRepository.findByConductorIdIn(List.of(personId)));
        appendVisits(res, "Играл", visitRepository.findByArtistIdsIn(List.of(personId)));
        appendVisits(res, "Посетил", visitRepository.findByAttendeeIdsIn(List.of(personId)));

        res.append("</body>\n" +
                "</html>\n");
        return res.toString();
    }

    private void appendVisits(StringBuilder res, String title, Collection<VisitEntity> visitsByPerson) {
        if (!visitsByPerson.isEmpty()) {
            res.append("<h2 title=\"").append(title).append("\">")
                    .append(title)
                    .append("</h2>");
            res.append("<table>");
            for (VisitEntity visit : visitsByPerson) {
                res.append("<tr>");
                res.append(row(visitUrl(visit.getId()), Arrays.asList(visit.getDate(), getComposition(visit.getCompositionId()))));
                res.append("</tr>");
            }
            res.append("</table>");
        }
    }

    private void appendCompositions(StringBuilder res, String title, Collection<CompositionEntity> compositions) {
        if (!compositions.isEmpty()) {
            res.append("<h2 title=\"").append(title).append("\">")
                    .append(title)
                    .append("</h2>");
            res.append("<table>");
            for (CompositionEntity composition : compositions) {
                res.append("<tr>");
                res.append(row(compositionUrl(composition.getId()), Arrays.asList(composition.getDisplayName(), getCompositionType(composition.getType()))));
                res.append("</tr>");
            }
            res.append("</table>");
        }
    }

    @GetMapping("/visit")
    public String visitHtml(@RequestParam(name = "id") UUID visitId) {
        VisitEntity visit = visitRepository.findById(visitId).orElseThrow(NotFoundException::new);
        CompositionEntity composition = compositionRepository.findById(visit.getCompositionId()).orElseThrow(NotFoundException::new);
        VenueEntity venue = venueRepository.findById(visit.getVenueId()).orElseThrow(NotFoundException::new);
        StringBuilder res = new StringBuilder("<html>\n")
                .append("<body>");
        res.append(backToSearch())
                .append("<h2>")
                .append(visit.getDate())
                .append(" ")
                .append(venue.getDisplayName())
                .append(" ")
                .append(composition.getDisplayName())
                .append("</h2>");

        res.append("<table>");
        res.append("<tr>");
        res.append(row(compositionUrl(composition.getId()), Collections.singletonList(composition.getDisplayName())));
        res.append("</tr>");
        res.append("</table>");

        res.append("<table>");
        res.append("<tr>");
        res.append(row(venueUrl(venue.getId()), Collections.singletonList(venue.getDisplayName())));
        res.append("</tr>");
        res.append("</table>");

        appendPersonsList(res, "Зрители", personRepository.findAllById(visit.getAttendeeIds()));
        appendPersonsList(res, "Авторы", personRepository.findAllById(Collections.singleton(composition.getComposerId())));
        appendPersonsList(res, "Режиссёры", personRepository.findAllById(Collections.singleton(visit.getDirectorId())));
        appendPersonsList(res, "Дирижёры", personRepository.findAllById(Collections.singleton(visit.getConductorId())));
        appendPersonsList(res, "Артисты", personRepository.findAllById(visit.getArtistIds()));

        res.append("</body>\n" +
                "</html>\n");
        return res.toString();
    }

    private void appendPersonsList(StringBuilder res, String title, List<PersonEntity> persons) {
        if (!persons.isEmpty()) {
            res.append("<h2 title=\"").append(title).append("\">")
                    .append(title)
                    .append("</h2>");
            res.append("<table>");
            for (PersonEntity person : persons) {
                res.append("<tr>");
                res.append(row(personUrl(person.getId()), Collections.singletonList(person.getDisplayName())));
                res.append("</tr>");
            }
            res.append("</table>");
        }
    }

    @GetMapping("/composition")
    public String compositionHtml(@RequestParam(name = "id") UUID compositionId) {
        CompositionEntity composition = compositionRepository.findById(compositionId).orElseThrow(NotFoundException::new);
        StringBuilder res = new StringBuilder("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>");
        res.append(backToSearch());
        res.append("<h2 title=\"").append(composition.getDisplayName()).append("\">")
                .append(composition.getDisplayName()).append(" (")
                .append(getCompositionType(composition.getType()))
                .append(")")
                .append("</h2>");
        appendPersonsList(res, "Авторы", personRepository.findAllById(Collections.singleton(composition.getComposerId())));
        appendVisits(res, "Представления", visitRepository.findByCompositionIdIn(List.of(compositionId)));

        res.append("</body>\n" +
                "</html>\n");
        return res.toString();
    }

    @GetMapping("/venue")
    public String venueHtml(@RequestParam(name = "id") UUID venueId) {
        VenueEntity venue = venueRepository.findById(venueId).orElseThrow(NotFoundException::new);
        StringBuilder res = new StringBuilder("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>");
        res.append(backToSearch());
        res.append("<h2 title=\"").append(venue.getDisplayName()).append("\">")
                .append(venue.getDisplayName())
                .append("</h2>");
        appendVisits(res, "Представления", visitRepository.findByVenueIdIn(List.of(venueId)));
        res.append("</body>\n" +
                "</html>\n");
        return res.toString();
    }

    private String compositionUrl(UUID compositionId) {
        return getUrl(compositionId, "/html/composition?id=");
    }

    private String visitUrl(UUID visitId) {
        return getUrl(visitId, "/html/visit?id=");
    }

    private String venueUrl(UUID venueId) {
        return getUrl(venueId, "/html/venue?id=");
    }

    private String personUrl(UUID personId) {
        return getUrl(personId, "/html/person?id=");
    }

    private static String getUrl(UUID visitId, String prefix) {
        return Optional.ofNullable(visitId)
                .map(String::valueOf)
                .map(id -> prefix + id)
                .orElse(null);
    }

    private String getComposition(UUID compositionId) {
        Optional<CompositionEntity> byId = compositionRepository.findById(compositionId);
        return byId.map(c -> Stream.of(getCompositionType(c.getType()), c.getDisplayName()))
                .orElse(Stream.empty()).collect(Collectors.joining(" "));
    }

    private String getCompositionType(UUID compositionType) {
        return compositionTypeRepository.findById(compositionType)
                .map(CompositionTypeEntity::getDisplayName).orElse("");
    }

    private String row(String url, List<Object> row) {
        StringBuilder res = new StringBuilder();
        for (Object s : row) {
            res.append("<td>").append(href(url, String.valueOf(s))).append("</td>");
        }
        return res.toString();
    }

    public String href(String url, String content) {
        if (url == null) {
            return content;
        } else {
            return "<a href=\"" + url + "\">" + content + "</a>";
        }
    }
    public String backToSearch(){
        return href("/search/html?page=0&s=", "Поиск");
    }
}
