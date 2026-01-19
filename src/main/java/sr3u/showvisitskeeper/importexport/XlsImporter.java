package sr3u.showvisitskeeper.importexport;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Slf4j
public class XlsImporter implements Importer {
    public static final DataFormatter DATA_FORMATTER = new DataFormatter();
    DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final Set<String> HEADER_ROW = new HashSet<>(Arrays.asList(
            "дата", "тип", "зал", "место", "цена билета", "цена 1 билета",
            "кто был", "название", "автор", "дирижер", "солисты", "режиссер",
            "компания", "примечание"
    ));

    @SneakyThrows
    public List<ImportItem> read(InputStream inputStream) {
        Workbook wb = null;
        try {
            //noinspection ConstantValue
            if (wb == null) {
                wb = new HSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (wb == null) {
                wb = new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wb == null) {
            throw new RuntimeException("workbook not supported");
        }
        Sheet sheet = wb.getSheetAt(0);
        Stream<Row> rows = StreamSupport.stream(sheet.spliterator(), false);
        return rows.map(this::rowToItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    private ImportItem rowToItem(Row row) {
        // Skipping header rows
        boolean isHeader = Optional.of(row.getFirstCellNum())
                .map(row::getCell)
                .filter(cell -> cell.getCellType() == CellType.STRING)
                .map(Cell::getStringCellValue)
                .map(String::toLowerCase).map(HEADER_ROW::contains).orElse(false);
        if (isHeader) {
            return null;
        }
        return ImportItem.builder()
                .date(getDateFromRow(row, 0))
                .type(filterEmpty(getCellText(row, 1)))
                .venue(filterEmpty(getCellText(row, 2)))
                .placeType(filterEmpty(getCellText(row, 3)))
                .ticketPrice(getCellNumber(row, 4))
                .attendees(parseSeparatedValues(filterEmpty(getCellText(row, 5))))
                .showName(filterEmpty(getCellText(row, 6)))
                .composer(filterEmpty(getCellText(row, 7)))
                .conductor(filterEmpty(getCellText(row, 8)))
                .actors(parseSeparatedValues(filterEmpty(getCellText(row, 9))))
                .director(filterEmpty(getCellText(row, 10)))
                .additionalAttendees(parseSeparatedValues(filterEmpty(getCellText(row, 11))))
                .additionalInfo(filterEmpty(getCellText(row, 12)))
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private Optional<BigDecimal> getCellNumber(Row row, int i) {
        try {
            return Optional.of(new BigDecimal(getCellText(row, i)));
        } catch (Exception e) {
            try {
                return Optional.of(BigDecimal.valueOf(row.getCell(i).getNumericCellValue()));
            } catch (Exception ex) {
                return Optional.empty();
            }
        }
    }

    private String getCellText(Row row, int i) {
        return Optional.ofNullable(row.getCell(i)).map(DATA_FORMATTER::formatCellValue).orElse("");
    }

    private Optional<String> filterEmpty(String cellText) {
        if (cellText == null || isBlank(cellText)) {
            return Optional.empty();
        }
        return Optional.of(cellText);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Set<String> parseSeparatedValues(Optional<String> cellText) {
        return cellText.map(s -> Arrays.stream(s.split(" ")).collect(Collectors.toSet())).orElseGet(HashSet::new);
    }

    private Optional<LocalDate> getDateFromRow(Row row, @SuppressWarnings("SameParameterValue") int cellIndex) {
        LocalDate date = null;
        try {
            date = Optional.of(row.getCell(cellIndex))
                    .map(Cell::getLocalDateTimeCellValue)
                    .map(LocalDateTime::toLocalDate)
                    .orElseThrow(() -> new Exception("oops"));
        } catch (Exception e) {
            date = filterEmpty(getCellText(row, cellIndex))
                    .map(s -> parseDate(s))
                    .orElse(null);
        }
        return Optional.ofNullable(date);
    }

    private LocalDate parseDate(String s) {
        try{
            return LocalDate.parse(s, DATEFORMATTER);
        } catch (Exception e) {
            String msg = "Failed to parse date from string: " + s;
            log.error(msg,e);
            return LocalDate.EPOCH;
        }
    }
}
