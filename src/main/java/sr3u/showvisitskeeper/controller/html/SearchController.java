package sr3u.showvisitskeeper.controller.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sr3u.showvisitskeeper.dto.PagedCollection;
import sr3u.showvisitskeeper.dto.PagesInfo;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.service.SearchService;

import java.util.Optional;

//@CrossOrigin(origins = "${app.cors.origins}", maxAge = 86400, methods = {
//        RequestMethod.GET,
//        RequestMethod.HEAD,
//        RequestMethod.POST,
//        RequestMethod.PUT,
//        RequestMethod.PATCH,
//        RequestMethod.DELETE,
//        RequestMethod.OPTIONS,
//        RequestMethod.TRACE
//})

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService searchService;


//    @GetMapping("/pages")
//    public PagesInfo pages(@RequestParam(name = "s") String searchString,
//                           @RequestParam(name = "pageSize") long pageSize) {
//        searchString = Optional.ofNullable(searchString).map(String::toLowerCase).orElse("");
//        return searchService.pages(Query.streamQuery().searchString(searchString.toLowerCase()).build(), pageSize);
//    }

    @GetMapping("/json")
    public PagedCollection<SearchListEntity> search(@RequestParam(name = "s", required = false) String searchString,
                                                    @RequestParam(name = "skip", required = false) Long skip,
                                                    @RequestParam(name = "limit", required = false) Long limit,
                                                    @RequestParam(name = "page", required = false) Long page,
                                                    @RequestParam(name = "pageSize", required = false) Long pageSize) {
        searchString = Optional.ofNullable(searchString).map(String::toLowerCase).orElse("");
        return map(searchRaw(searchString, skip, limit, page, pageSize));
    }

    public SearchService.Result searchRaw(@RequestParam(name = "s", required = false) String searchString,
                                          @RequestParam(name = "skip", required = false) Long skip,
                                          @RequestParam(name = "limit", required = false) Long limit,
                                          @RequestParam(name = "page", required = false) Long page,
                                          @RequestParam(name = "pageSize", required = false) Long pageSize) {
        if (page != null) {
            return searchService.search(Query.pagedQuery().searchString(searchString.toLowerCase()).page(page).pageSize(pageSize).build());
        }
        if (skip != null && limit != null) {
            return searchService.search(Query.streamQuery().searchString(searchString.toLowerCase()).skip(skip).limit(limit).build());
        }
        return searchService.search(Query.streamQuery().searchString(searchString.toLowerCase()).build());
    }

    private PagedCollection<SearchListEntity> map(SearchService.Result result) {
        return new PagedCollection<>(result.getPagesInfo(), result.getResults());
    }


}
