package iMat;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.lang.reflect.Array;
import java.util.List;

public class Controller {

    public ProductCategory[] getCategories(){
        return ProductCategory.values();

    }
}
