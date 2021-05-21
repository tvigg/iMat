package iMat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.chalmers.cse.dat216.project.IMatDataHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("iMat_front.fxml"));
        primaryStage.setTitle("iMat");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //IMatCategoryController cc = new IMatCategoryController();
        //cc.cat();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> IMatDataHandler.getInstance().shutDown()));
        launch(args);
    }
}
