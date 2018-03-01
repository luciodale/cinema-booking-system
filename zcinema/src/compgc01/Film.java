package compgc01;

/**
 * A class represeting a film.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 11.12.2017
 */
public class Film {

    private String title = "Default Title", description = "Default Description", trailer = "Default Trailer",
            startDate = "yyyy-mm-dd", endDate = "yyyy-mm-dd";
    private String[] times = {"hh:mm", "hh:mm", "hh:mm"};

    Film(String title, String description, String trailer, String startDate, String endDate, String[] times) {

        if (!title.isEmpty() && !description.isEmpty() && !trailer.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty() && !(times.length == 0))
            this.title = title;
        this.description = description;
        this.trailer = trailer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.times = times;
    }

    String getTitle() {

        return title;
    }

    void setTitle(String title) {

        if (!title.isEmpty())
            this.title = title;
    }

    String getDescription() {

        return description;
    }

    void setDescription(String description) {

        if (!description.isEmpty())
            this.description = description;
    }

    String getTrailer() {

        return trailer;
    }

    void setTrailer(String trailer) {

        if (!trailer.isEmpty())
            this.trailer = trailer;
    }
    
    String getStartDate() {

        return startDate;
    }

    void setStartDate(String startDate) {

        if (!startDate.isEmpty())
            this.startDate = startDate;
    }

    String getEndDate() {

        return endDate;
    }

    void setEndDate(String endDate) {

        if (!endDate.isEmpty())
            this.endDate = endDate;
    }

    String[] getTimes() {

        return times;
    }

    void setTimes(String[] times) {

        if (!(times.length == 0))
            this.times = times;
    }
}