package sr3u.showvisitskeeper;

public final class Tables {

    public static final class Persons {
        public static final String _TABLE_NAME = "PERSONS";
        public static final String ID = "ID";

        public static final String SHORT_NAME = "SHORT_NAME";

        public static final String CREATED_AT = "CREATED_AT";

        public static final String TYPE = "TYPE";
        public static final String FULL_NAME = "FULL_NAME";
    }

    public static final class ComnpositionType {
        public static final String _TABLE_NAME = "SHOW_TYPES";
        public static final String ID = "ID";

        public static final String VALUE = "STR_VALUE";
        public static final String SHORT_NAME = "SHORT_NAME";

        public static final String CREATED_AT = "CREATED_AT";
    }

    public static final class Composition {
        public static final String _TABLE_NAME = "COMPOSITION";
        public static final String ID = "ID";

        public static final String NAME = "NAME";

        public static final String TYPE = "TYPE";

        public static final String COMPOSER_ID = "COMPOSER_ID";

        public static final String CREATED_AT = "CREATED_AT";
    }


    public static final class Venue {
        public static final String _TABLE_NAME = "VENUES";
        public static final String ID = "ID";
        public static final String SHORT_NAME = "SHORT_NAME";

        public static final String CREATED_AT = "CREATED_AT";
    }

    public static final class Visit {
        public static final String _TABLE_NAME = "VISITS";
        public static final String ID = "ID";
        public static final String DATE = "DATE";
        public static final String CONDUCTOR_ID = "CONDUCTOR_ID";
        public static final String DIRECTOR_ID = "DIRECTOR_ID";
        public static final String COMPOSITION_ID = "COMPOSITION_ID";
        public static final String VENUE_ID = "VENUE_ID";
        public static final String TICKET_PRICE = "TICKET_PRICE";
        public static final String DETAILS = "DETAILS";
        public static final String P_HASH = "P_HASH";
    }

}
