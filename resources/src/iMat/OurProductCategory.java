package iMat;
public class OurProductCategory {
    public enum OurCategory {
        Startsidan("Startsidan"),
        Kolonialvaror("Kolonialvaror"),
        Fisk_Skaldjur("Fisk & Skaldjur"),
        Frukt_Grönt("Frukt & Grönt"),
        Konfekt("Konfekt"),
        Charkvaror("Charkvaror"),
        Mejeri("Mejeri"),
        Mjöl("Mjöl, Socker & Salt");

        private String name;

        OurCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
    public enum Startsidan {
        BREAD,
        HOT_DRINKS,
        COLD_DRINKS,
        NUTS_AND_SEEDS,
        PASTA,
    }

    public enum Kolonialvaror {

    }

    public enum Fisk_Skaldjur {
        FISH;
    }

    public enum Frukt_GröntEnum {
        POD,
        BERRY,
        CITRUS_FRUIT,
        EXOTIC_FRUIT,
        VEGETABLE_FRUIT,
        CABBAGE,
        MELONS,
        POTATO_RICE,
        ROOT_VEGETABLE,
        FRUIT,
        HERB;
    }

    public enum Konfekt {
        SWEET;
    }

    public enum Charkvaror {
        MEAT;
    }

    public enum Mejeri {
        DAIRIES;
    }

    public enum Mjöl {
        FLOUR_SUGAR_SALT
    }
}
