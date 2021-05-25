package iMat;

import se.chalmers.cse.dat216.project.ProductCategory;

public class OurProductCategory {
    public enum OurCategory {
        HOT_DRINKS("Varma drycker"),                //    HOT_DRINKS,
        COLD_DRINKS("Kalla drycker"),               //    COLD_DRINKS,
        BREAD("Bröd"),                              //    BREAD,
        BERRY("Bär"),                               //    BERRY,
        POD("Bönor"),                               //    POD,
        CITRUS_FRUIT("Citrus Frukter"),             //    CITRUS_FRUIT,
        EXOTIC_FRUIT("Exotiska frukter"),           //    EXOTIC_FRUIT
        FRUIT("Frukt"),                             //    FRUIT,
        CABBAGE("Kål"),                             //    CABBAGE,
        MELONS("Melon"),                            //    MELONS,
        NUTS_AND_SEEDS("Nötter & Fröer"),           //    NUTS_AND_SEEDS,
        POTATO_RICE("Potatis & Ris"),               //    POTATO_RICE,
        ROOT_VEGETABLE("Rotfrukter"),               //    ROOT_VEGETABLE,
        VEGETABLE_FRUIT("Stenfrukter"),             //    VEGETABLE_FRUIT
        HERB("Örter"),                              //    HERB;
        PASTA("Pasta"),                             //    PASTA,
        FISH("Fisk"),                               //    FISH,
        MEAT("Kött"),                               //    MEAT,
        DAIRIES("Mejeri"),                          //    DAIRIES,
        FLOUR_SUGAR_SALT("Mjöl, Socker & Salt"),    //    FLOUR_SUGAR_SALT,
        SWEET("Konfekt");                           //    SWEET,

        private String name;

        OurCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
        //public ProductCategory getProductCategory() { this.getProductCategory() }
    }
}
