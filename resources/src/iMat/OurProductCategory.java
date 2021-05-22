package iMat;

public enum OurProductCategory {
    Startsidan("Startsidan"),
    Kolonialvaror("Kolonialvaror"),
    Fisk_Skaldjur("Fisk & Skaldjur"),
    Frukt_Grönt("Frukt & Grönt"),
    Konfekt("Konfekt"),
    Charkvaror("Charkvaror"),
    Mejeri("Mejeri"),
    Mjöl("Mjöl, Gryn & Bak");

    private String name;

    OurProductCategory(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}


