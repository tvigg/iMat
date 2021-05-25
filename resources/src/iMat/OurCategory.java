package iMat;

import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

public class OurCategory {
    private ProductCategory category;
    private String name;

    OurCategory(ProductCategory category) {
        this.category = category;
        String str = null;
        switch (category) {
            case HOT_DRINKS:
                str = ("Varma drycker");                //    HOT_DRINKS,
                break;
            case COLD_DRINKS:
                str = ("Kalla drycker");               //    COLD_DRINKS,
                break;
            case BREAD:
                str = ("Bröd");                              //    BREAD,
                break;
            case BERRY:
                str = ("Bär");                               //    BERRY,
                break;
            case POD:
                str = ("Bönor");                               //    POD,
                break;
            case CITRUS_FRUIT:
                str = ("Citrus Frukter");             //    CITRUS_FRUIT,
                break;
            case EXOTIC_FRUIT:
                str = ("Exotiska frukter");           //    EXOTIC_FRUIT
                break;
            case FRUIT:
                str = ("Frukt");                             //    FRUIT,
                break;
            case CABBAGE:
                str = ("Kål");                             //    CABBAGE,
                break;
            case MELONS:
                str = ("Melon");                            //    MELONS,
                break;
            case NUTS_AND_SEEDS:
                str = ("Nötter & Fröer");           //    NUTS_AND_SEEDS,
                break;
            case POTATO_RICE:
                str = ("Potatis & Ris");               //    POTATO_RICE,
                break;
            case ROOT_VEGETABLE:
                str = ("Rotfrukter");               //    ROOT_VEGETABLE,
                break;
            case VEGETABLE_FRUIT:
                str = ("Stenfrukter");             //    VEGETABLE_FRUIT
                break;
            case HERB:
                str = ("Örter");                              //    HERB;
                break;
            case PASTA:
                str = ("Pasta");                             //    PASTA,
                break;
            case FISH:
                str = ("Fisk");                               //    FISH,
                break;
            case MEAT:
                str = ("Kött");                               //    MEAT,
                break;
            case DAIRIES:
                str = ("Mejeri");                          //    DAIRIES,
                break;
            case FLOUR_SUGAR_SALT:
                str = ("Mjöl, Socker & Salt");    //    FLOUR_SUGAR_SALT,
                break;
            case SWEET:
                str = ("Konfekt");                           //    SWEET;
                break;
        }
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
    public ProductCategory getProductCategory() { return this.category; }
}
