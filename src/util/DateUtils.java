package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//Observação: O Java considera janeiro como o mês 0. É preciso levar isto em consideração ao trabalhar com datas
public class DateUtils {

    private static final Locale locale = new Locale("pt", "BR");
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", locale);
    private static final DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy", locale);
    private static final DateFormat df2 = new SimpleDateFormat("yyyy/MM/d", locale);

    /**
     * Formata um objeto Date no formato dd/MM/yyyy
     *
     * @param Long data
     * @return String
     */
    public static String formatDate(Long data) {
        if (data == null) {
            return null;
        }
        return df.format(new Date(data));
    }

    public static LocalDate createLocalDate(long data) {
        String data1 = formatDate(data);
        int[] data2 = parseDateInfo(data1);
        return LocalDate.of(data2[2], data2[1], data2[0]);
    }

    public static String formatDate2(Long date) {
        if (date == null) {
            return null;
        }
        return df2.format(new Date(date));
    }

    /**
     * Formata um objeto Date no formato dd/MM/yyyy
     *
     * @param LocalDate data
     * @return String
     */
    public static String formatDate(LocalDate date) {
        String data = "";
        data += date.getDayOfMonth() + "/";
        data += date.getMonthValue() + "/";
        data += date.getYear();
        return data;
    }

    //retorna o mes por extenso
    public static String getMes(LocalDate data) {
        String mes = "";
        switch (data.getMonthValue()) {
            case 1:
                mes = "Janeiro";
                break;
            case 2:
                mes = "Fevereiro";
                break;
            case 3:
                mes = "Março";
                break;
            case 4:
                mes = "Abril";
                break;
            case 5:
                mes = "Maio";
                break;
            case 6:
                mes = "Junho";
                break;
            case 7:
                mes = "Julho";
                break;
            case 8:
                mes = "Agosto";
                break;
            case 9:
                mes = "Setembro";
                break;
            case 10:
                mes = "Outubro";
                break;
            case 11:
                mes = "Novembro";
                break;
            case 12:
                mes = "Dezembro";
                break;
        }
        return mes;
    }

    /**
     * retorna data no formato "01 de jan de 2018"
     *
     * @param Long dateLong
     * @return String
     */
    public static String formatDateExtenso(Long dateLong) {
        if (dateLong == null) {
            return null;
        }

        Date date = new Date(dateLong);

        String data = df.format(date);
        int mes = Integer.parseInt(data.substring(3, 5));

        if (mes == 1) {
            data = df.format(date).substring(0, 2) + " de jan";
        } else if (mes == 2) {
            data = df.format(date).substring(0, 2) + " de fev";
        } else if (mes == 3) {
            data = df.format(date).substring(0, 2) + " de mar";
        } else if (mes == 4) {
            data = df.format(date).substring(0, 2) + " de abr";
        } else if (mes == 5) {
            data = df.format(date).substring(0, 2) + " de mai";
        } else if (mes == 6) {
            data = df.format(date).substring(0, 2) + " de jun";
        } else if (mes == 7) {
            data = df.format(date).substring(0, 2) + " de jul";
        } else if (mes == 8) {
            data = df.format(date).substring(0, 2) + " de ago";
        } else if (mes == 9) {
            data = df.format(date).substring(0, 2) + " de set";
        } else if (mes == 10) {
            data = df.format(date).substring(0, 2) + " de out";
        } else if (mes == 11) {
            data = df.format(date).substring(0, 2) + " de nov";
        } else {
            data = df.format(date).substring(0, 2) + " de dez";
        }

//        if (date.getYear() == new Date().getYear()) {
//            return data;
//        } else {
//            return data + " de " + date.getYear();
//        }
        return data;

    }

    //retorna data no formato "01 de jan de 2018"
    public static String formatDateExtenso(LocalDate date) {
        if (date == null) {
            return null;
        }

        String data = String.valueOf(date.getDayOfMonth());
        int mes = date.getMonthValue();

        switch (mes) {
            case 1:
                data += " de Janeiro";
                break;
            case 2:
                data += " de Fevereiro";
                break;
            case 3:
                data += " de Março";
                break;
            case 4:
                data += " de Abril";
                break;
            case 5:
                data += " de Maio";
                break;
            case 6:
                data += " de Junho";
                break;
            case 7:
                data += " de Julho";
                break;
            case 8:
                data += " de Agosto";
                break;
            case 9:
                data += " de Setembro";
                break;
            case 10:
                data += " de Outubro";
                break;
            case 11:
                data += " de Novembro";
                break;
            default:
                data += " de Dezembro";
                break;
        }

        return data + " de " + String.valueOf(date.getYear());
    }

    // Cria um objeto Date a partir de uma string no formato dd/MM/yyyy
    public static Date createDate(String date) {
        int[] info = parseDateInfo(date);
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.DAY_OF_MONTH, info[0]);
        c.set(Calendar.MONTH, info[1] - 1);
        c.set(Calendar.YEAR, info[2]);
        return c.getTime();
    }

    //retorna Long de uma data no formato dd/MM/yyyy
    public static Long getLongFromDate(String data) {
        Date date = createDate(data);
        return date.getTime();
    }

    public static Long getLong(LocalDate localDate) {
        int dia = localDate.getDayOfMonth();
        int mes = localDate.getMonthValue();
        int ano = localDate.getYear();
        return getLongFromDate(formatDate(ano, mes, dia));
    }

    // Formata um string no formato dd/MM/yyyy a partir do ano, mês e dia fornecidos
    public static String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return String.format(locale, "%02d/%02d/%04d", dayOfMonth, monthOfYear, year);
    }

    // Retorna a data de hoje em forma de um array
    // Posição 0: dia
    // Posição 1: mês
    // Posição 2: ano
    public static int[] today() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        return new int[]{day, month, year};
    }

    // Retorna um data qualquer, no formato dd/MM/yyyy, em forma de um array
    // Posição 0: dia
    // Posição 1: mês
    // Posição 2: ano
    public static int[] parseDateInfo(String date) {
        try {
            String[] tokens = date.split("/");
            int day = Integer.parseInt(tokens[0]);
            int month = Integer.parseInt(tokens[1]);
            int year = Integer.parseInt(tokens[2]);

            return new int[]{day, month, year};

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Retorna um intervalo de datas que compreende o mês e ano fornecidos. O retorno é em forma de um array
    // Posição 0: data inicial (em milisegundos)
    // Posição 1: data final (em milisegundos)
    public static long[] getRange(int mes, int ano) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(ano, mes - 1, 1);
        long dateFrom = c.getTimeInMillis();

        c.set(ano, mes - 1, 1, 23, 59, 59);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

        long dateTo = c.getTimeInMillis();

        return new long[]{dateFrom, dateTo};
    }

    public static String getHoraFromDate(Long date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String formatDataHora(Date date) {
        String dataStr = "";
        if (date == null) {
            return dataStr;
        }

        dataStr = df.format(date) + ", " + getHoraFromDate(date.getTime());
        return dataStr;
    }
    
    public static String getDataHoraPonto(Long date) {
        String data = df1.format(new Date(date));
        
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH");
        SimpleDateFormat sdfMinuto = new SimpleDateFormat("mm");

        String sHora, sMinuto;

        int hora = Integer.parseInt(sdfHora.format(System.currentTimeMillis()));
        int minuto = Integer.parseInt(sdfMinuto.format(System.currentTimeMillis()));

        if (hora < 10) {
            sHora = "0" + hora;
        } else {
            sHora = String.valueOf(hora);
        }

        if (minuto < 10) {
            sMinuto = "0" + minuto;
        } else {
            sMinuto = String.valueOf(minuto);
        }
        
        data += " - " + sHora + ":" + sMinuto;

        return data;
    }

    public static String getDataHora(Long date) {
        String teste = "14.06.18 - 17:46";
        String data = formatDate(date);

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH");
        SimpleDateFormat sdfMinuto = new SimpleDateFormat("mm");

        String sHora, sMinuto;

        int hora = Integer.parseInt(sdfHora.format(System.currentTimeMillis()));
        int minuto = Integer.parseInt(sdfMinuto.format(System.currentTimeMillis()));

        if (hora < 10) {
            sHora = "0" + hora;
        } else {
            sHora = String.valueOf(hora);
        }

        if (minuto < 10) {
            sMinuto = "0" + minuto;
        } else {
            sMinuto = String.valueOf(minuto);
        }
        
        data += " - " + sHora + ":" + sMinuto;

        return data;
    }

    public static String[] getHoraAgora() {
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH");
            SimpleDateFormat sdfMinuto = new SimpleDateFormat("mm");

            String sHora, sMinuto;

            int hora = Integer.parseInt(sdfHora.format(System.currentTimeMillis()));
            int minuto = Integer.parseInt(sdfMinuto.format(System.currentTimeMillis()));

            if (hora < 10) {
                sHora = "0" + hora;
            } else {
                sHora = String.valueOf(hora);
            }

            if (minuto < 10) {
                sMinuto = "0" + minuto;
            } else {
                sMinuto = String.valueOf(minuto);
            }

            return new String[]{sHora, sMinuto};
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
