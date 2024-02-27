package sr3u.showvisitskeeper.controller.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.service.SearchService;

import java.util.Collection;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService searchService;


    @GetMapping("/pages")
    public long pages(@RequestParam(name = "s") String searchString,
                      @RequestParam(name = "pageSize") long pageSize) {
        searchString = searchString.toLowerCase();
        return searchService.pages(Query.streamQuery().searchString(searchString.toLowerCase()).build(), pageSize);
    }

    @GetMapping("/json")
    public Collection<SearchListEntity> search(@RequestParam(name = "s", required = false) String searchString,
                                               @RequestParam(name = "skip", required = false) Long skip,
                                               @RequestParam(name = "limit", required = false) Long limit,
                                               @RequestParam(name = "page", required = false) Long page,
                                               @RequestParam(name = "pageSize", required = false) Long pageSize) {
        searchString = searchString.toLowerCase();
        return map(searchRaw(searchString.toLowerCase(), skip, limit, page, pageSize).getResults());
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

    @GetMapping("/html")
    public String searchHtml(@RequestParam(name = "s", required = false) String searchString,
                             @RequestParam(name = "page", required = false) Long page,
                             @RequestParam(name = "pageSize", required = false) Long pageSize) {
        if (page == null) {
            page = 0L;
        }
        if (pageSize == null) {
            pageSize = 25L;
        }
        searchString = searchString.toLowerCase();
        long pagesTotal = this.pages(searchString, pageSize);
        StringBuilder res = new StringBuilder("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>");
        res.append("<form action=\"/search/html\">");
        res.append("<label for=\"fSearchString\">Поиск:</label><br>\n")
                .append("<input type=\"text\" id=\"fSearchString\" name=\"s\" value=\"")
                .append(searchString)
                .append("\"><br>");
        res.append("<input type=\"submit\" value=\"Найти\">");
        res.append("</form>");
        res.append("<table>");
        SearchService.Result searchResult = this.searchRaw(searchString, null, null, page, pageSize);
        searchResult.getResults().forEach(sr -> res.append(sr.toHtml()));
        res.append("</table>");
        res.append("<table><tr>");
        res.append("<td><a href=\"/search/html?pageSize=").append(pageSize)
                .append("&page=0&s=").append(searchString).append("\">начало</a></td>");
        int end = 10;
        for (int i = -3; i < end; i++) {
            long _page = page + i;
            if (_page >= 0 && _page < pagesTotal) {
                res.append("<td><a href=\"/search/html?pageSize=").append(pageSize).append("&page=")
                        .append(_page).append("&s=").append(searchString)
                        .append("\">").append(_page).append("</a></td>");
            } else {
                end += 1;
            }
        }
        res.append("<td><a href=\"/search/html?pageSize=").append(pageSize)
                .append("&page="+pagesTotal+"&s=").append(searchString).append("\">конец</a></td>");
        res.append("</tr></table>");
        res.append("</body>\n" +
                "</html>\n");
        return res.toString();
    }

    private Collection<SearchListEntity> map(Collection<SearchListEntity> search) {
        return search;
    }


}
