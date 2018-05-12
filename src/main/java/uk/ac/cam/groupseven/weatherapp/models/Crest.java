package uk.ac.cam.groupseven.weatherapp.models;

public enum Crest {
    NONE("None", "none"),
    GONVILLE_AND_CAIUS("Gonville_and_Caius", "gonvillecaius"),
    ST_CATHARINES("St Catharines", "stcatharines"),
    CHRISTS("Christs", "christs"),
    CHURCHILL("Churchill", "churchill"),
    CLARE("Clare", "clare"),
    CLARE_HALL("Clare Hall", "clarehall"),
    CORPUS_CHRISTI("Corpus Christi", "corpuschristi"),
    DARWIN("Darwin", "darwin"),
    DOWNING("Downing", "downing"),
    ST_EDMUNDS("St Edmunds", "stedmunds"),
    EMMANUEL("Emmanuel", "emmanuel"),
    FITZWILLIAM("Fitzwilliam", "fitzwilliam"),
    GIRTON("Girton", "girton"),
    HOMERTON("Homerton", "homerton"),
    HUGHES_HALL("Hughes Hall", "hugheshall"),
    JESUS("Jesus", "jesus"),
    ST_JOHNS("St Johns", "stjohns"),
    KINGS("Kings", "kings"),
    LUCY_CAVENDISH("Lucy Cavendish", "lucy"),
    MAGDALENE("Magdalene", "magdalene"),
    MURRAY_EDWARDS("Murray_edwards", "murray-edwards"),
    NEWHALL("Newhall", "newhall"),
    NEWNHAM("Newnham", "newnham"),
    PEMBROKE("Pembroke", "pembroke"),
    PETERHOUSE("Peterhouse", "peterhouse"),
    QUEENS("Queens", "queens"),
    RIDLEY_HALL("Ridley Hall", "ridleyhall"),
    ROBINSON("Robinson", "robinson"),
    SELWYN("Selwyn", "selwyn"),
    SIDNEY_SUSSEX("Sidney Sussex", "sidneysussex"),
    TRINITY("Trinity", "trinity"),
    TRINITY_HALL("Trinity Hall", "trinityhall"),
    WESLEY_HOUSE("Wesley House", "wesleyhouse"),
    WESTCOTT_HOUSE("Westcott House", "westcotthouse"),
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
        return Crest.NONE;
    }

    public String getCode() {
        return code;
    }
}
