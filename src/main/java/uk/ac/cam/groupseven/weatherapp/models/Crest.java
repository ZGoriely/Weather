package uk.ac.cam.groupseven.weatherapp.models;

public enum Crest {
    CHRISTS("Christs", "christs"),
    CHURCHILL("Churchill", "churchill"),
    CLARE("Clare", "clare"),
    CLARE_HALL("Clare Hall", "clarehall"),
    CORPUS_CHRISTI("Corpus Christi", "corpuschristi"),
    DARWIN("Darwin", "darwin"),
    DOWNING("Downing", "downing"),
    EMMANUEL("Emmanuel", "emmanuel"),
    FITZWILLIAM("Fitzwilliam", "fitzwilliam"),
    GIRTON("Girton", "girton"),
    GONVILLE_AND_CAIUS("Gonville_and_Caius", "gonvillecaius"),
    HOMERTON("Homerton", "homerton"),
    HUGHES_HALL("Hughes Hall", "hugheshall"),
    JESUS("Jesus", "jesus"),
    KINGS("Kings", "kings"),
    LUCY_CAVENDISH("Lucy Cavendish", "lucy"),
    MAGDALENE("Magdalene", "magdalene"),
    MURRAY_EDWARDS("Murray_edwards", "murray-edwards"),
    NEWNHAM("Newnham", "newnham"),
    PELBY("Pelby", "pelby"),
    PEMBROKE("Pembroke", "pembroke"),
    PETERHOUSE("Peterhouse", "peterhouse"),
    QUEENS("Queens", "queens"),
    ROBINSON("Robinson", "robinson"),
    SELWYN("Selwyn", "selwyn"),
    SIDNEY_SUSSEX("Sidney Sussex", "sidneysussex"),
    ST_CATHARINES("St Catharines", "stcatharines"),
    ST_EDMUNDS("St Edmunds", "stedmunds"),
    ST_JOHNS("St Johns", "stjohns"),
    TRINITY("Trinity", "trinity"),
    TRINITY_HALL("Trinity Hall", "trinityhall"),
    WOLFSON("Wolfson", "wolfson");

    private final String displayName;
    private final String code;

    Crest(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public static Crest getCrestFromCode(String code) {
        for (Crest crest : values()) {
            if (crest.code.equals(code))
                return crest;
        }
        return Crest.PELBY;
    }

    public String getCode() {
        return code;
    }
}
