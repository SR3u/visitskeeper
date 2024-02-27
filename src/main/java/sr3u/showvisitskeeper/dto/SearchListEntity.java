package sr3u.showvisitskeeper.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SearchListEntity {
    String fullName;
    String description;
    String type;
    String url;
    UUID id;

    public String toHtml() {
        return "<tr>" +
                "<td>" + href(fullName) + "</td>" +
                "<td>" + href(type) + "</td>" +
                "<td>" + href(description) + "</td>";

    }

    public String href(String content) {
        if (url == null) {
            return content;
        } else {
            return "<a href=\"" + url + "\">" + content + "</a>";
        }
    }
}
