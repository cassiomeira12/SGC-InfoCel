package util;

import java.io.Serializable;

public class Config implements Serializable {
    public String DIRETORIO_BACKUP;
    public boolean BACKUP_AUTOMATICO;
    public int BACKUP_A_CADA_DIA;
    public Long ULTIMO_BACKUP;
    public Long PROXIMO_BACKUP;
    
    public String DIRETORIO_RELATORIOS;
    
    public Config() {
        DIRETORIO_BACKUP = "Backups SGC";
        BACKUP_AUTOMATICO = false;
        BACKUP_A_CADA_DIA = 1;
        ULTIMO_BACKUP = null;
        PROXIMO_BACKUP = null;
        DIRETORIO_RELATORIOS = "Relatorios SGC/";
    }
    
    public String getUltimoBackup() {
        return DateUtils.getDataHora(ULTIMO_BACKUP);
    }
    
    public String getProximoBackup() {
        return DateUtils.formatDate(PROXIMO_BACKUP);
    }
    
    public boolean salvarArquivo() {
        return Arquivo.gravar(this);
    }
}
