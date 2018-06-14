/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.Arquivo;
import util.Config;
import util.SoftwareSpecifications;

/**
 *
 * @author cassio
 */
public class Painel extends Application {
    
    private Stage palco;
    
    private PainelController painelController;
    
    public static Config config;
    
    @Override
    public void start(Stage palco) throws Exception {
        this.palco = palco;
        this.painelController = new PainelController();
        this.painelController.setStage(palco);

        BorderPane painel = FXMLLoader.load(getClass().getResource("Painel.fxml"));
        
        palco.setScene(new Scene(painel));
        palco.centerOnScreen();
        palco.setMaximized(true);//Deixando a tela Maximizada
        palco.setTitle(SoftwareSpecifications.TITULO);
        palco.show();
        
        
        config = (Config) Arquivo.importar();
        
        if (config == null) {
            config = new Config();
        }
        
    }
    
}