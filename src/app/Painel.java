/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
    
    public static Stage palco;
    
    private PainelController painelController;
    
    public static Config config;
    
    @Override
    public void start(Stage palco) throws Exception {
        this.palco = palco;
        this.painelController = new PainelController();
        this.painelController.setStage(palco);

        BorderPane painel = FXMLLoader.load(getClass().getResource("Painel.fxml"));
        
        try {
            palco.getIcons().addAll(new Image(getClass().getResourceAsStream("/img/icons/icone.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        palco.setScene(new Scene(painel));
        palco.centerOnScreen();
        palco.setMaximized(true);//Deixando a tela Maximizada
        palco.setTitle(SoftwareSpecifications.TITULO);
        palco.show();
        
        String diretorio = System.getProperty("java.class.path");
        diretorio = diretorio.replaceAll("SGC-InfoCel.jar", "");
        
        try {
            config = (Config) Arquivo.importar(diretorio);
            System.out.println("Arquivo de Configurações encontrado!");
        } catch (Exception ex) {
            System.err.println("Arquivo de Configurações não encontrado!");
        }
        
        if (config == null) {
            config = new Config(diretorio);
        }
        
    }
    
}