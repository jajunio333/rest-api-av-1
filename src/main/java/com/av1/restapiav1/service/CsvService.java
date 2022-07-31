package com.av1.restapiav1.service;

import com.av1.restapiav1.repositories.CsvRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileReader;
import jxl.write.Number;
import java.util.*;

@Service
public class CsvService {
    private CsvRepository csvRepository;
    public CsvService()
    {
        this.csvRepository = csvRepository;
    }

    public ArrayList<String> GetDocumento(String file)
    {
        try {
            String path = "C:\\Users\\jajun\\Desktop\\rest-api-av-1\\src\\main\\java\\com\\av1\\restapiav1\\files\\" + file + ".csv";
            ArrayList<String> arquivoConvertido = new ArrayList();
            FileReader filereader = new FileReader(path);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();

            for (String[] row : allData) {
                for (String cell : row) {
                    arquivoConvertido.add(cell);
                }
            }


            return arquivoConvertido;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public ArrayList<Double> ConvertStringToDouble (ArrayList<String> arquivo, int inicio, int fim)
    {
        ArrayList<Double> valores = new ArrayList<>();
        for (String val : arquivo) {
            valores.add(Double.parseDouble(val.substring(inicio, fim)));
        }
        return valores;
    }
    public ArrayList<String> RecuperData (ArrayList<String> arquivo, int inicio, int fim)
    {
        ArrayList<String> valores = new ArrayList<>();
        for (String val : arquivo) {
            valores.add(val.substring(inicio, fim));
        }
        return valores;
    }
    public ArrayList<Double> MediaSimples(String path, int tamanhoMedia)
    {
        ArrayList<String> arquivoLido = GetDocumento(path);
        ArrayList<Double> valores = ConvertStringToDouble(arquivoLido,35,42);
        ArrayList<Double> medias = new ArrayList<>();
        Collections.reverse(valores);

        for (int i = 0; i <= (valores.size() - tamanhoMedia); i++) {
            double valorMedia = 0;
            for (int j = i; j< (i+tamanhoMedia); j++)
            {
                valorMedia += valores.get(j);
            }
            medias.add(Math.round((valorMedia/tamanhoMedia)*100000.0)/100000.0) ;
        }
        Collections.reverse(medias);

        return medias;
    }
    public ArrayList<Double> MediaExp(String path, int tamanhoMedia)
    {
        ArrayList<String> listaArquivo = GetDocumento(path);
        ArrayList<Double> valores = ConvertStringToDouble(listaArquivo,35,42);
        ArrayList<Double> listaMediasExp = MediaSimples(path,tamanhoMedia);
        double cte = (double)2/(tamanhoMedia+1);

        for (int i = 1; i < listaMediasExp.size() ; i++) {
            double val = ((valores.get(i) - listaMediasExp.get(i-1)) * cte) + listaMediasExp.get(i-1);
            listaMediasExp.set(i, (double) Math.round(val*100000.0)/100000.0) ;
        }

        return listaMediasExp;
    }
    public ArrayList<Double> Volatilidade(String path, int tamanhoMedia)
    {
        ArrayList<String> listaArquivo = GetDocumento(path);
        ArrayList<Double> valores = ConvertStringToDouble(listaArquivo,35,42);
        ArrayList<Double> listaMedias = MediaSimples(path,tamanhoMedia);
        ArrayList<Double> listaVolatilidade = new ArrayList<>();

        for (int i = 0; i < (listaMedias.size()); i++) {
            double aux = (Math.pow((valores.get(i+(tamanhoMedia-1))-listaMedias.get(i)),2)/tamanhoMedia);
            listaVolatilidade.add(Math.pow(aux, 0.5));
        }

        return listaVolatilidade;
    }
    public void ExportaExcel(String path)
    {
        try
        {
            ArrayList<String> listaArquivo = GetDocumento(path);
            ArrayList<String> datas = RecuperData(listaArquivo,0 ,10);
            ArrayList<Double> fechamento = ConvertStringToDouble(listaArquivo,35,42);
            ArrayList<Double> mmc = MediaSimples(path,5);
            ArrayList<Double> mmi = MediaSimples(path,20);
            ArrayList<Double> mml = MediaSimples(path,100);
            ArrayList<Double> mmexp = MediaExp(path,5);
            ArrayList<Double> volatilidade = Volatilidade(path,5);

            WritableWorkbook planilha = Workbook.createWorkbook(new File(
                    "C:\\Users\\jajun\\Downloads\\saida.xls"));
            // Adicionando o nome da aba
            WritableSheet aba = planilha.createSheet("DADOS PROCESSADOS", 0);

            // Cabeçalhos
            String cabecalho[] = new String[7];
            cabecalho[0] = "DATA";
            cabecalho[1] = "FECHAMENTO";
            cabecalho[2] = "MEDIA CURTA";
            cabecalho[3] = "MEDIA INTERMEDIARIA";
            cabecalho[4] = "MEDIA LONGA";
            cabecalho[5] = "MEDIA EXPONENCIAL - 5 PERIODOS";
            cabecalho[6] = "VOLATILIDADE - 5 PERIODOS";

            // Cor de fundo das celular
            Colour bckcolor = Colour.DARK_GREEN;
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setBackground(bckcolor);

            // Cor e tipo de fonte
            WritableFont fonte = new WritableFont(WritableFont.ARIAL);
            fonte.setColour(Colour.GOLD);
            cellFormat.setFont(fonte);
            cellFormat.setAlignment(Alignment.CENTRE);

            // Write the Header to the excel file
            for (int i = 0; i < cabecalho.length; i++) {
                Label label = new Label(i, 0, cabecalho[i]);
                aba.addCell(label);
                WritableCell cell = aba.getWritableCell(i, 0);
                cell.setCellFormat(cellFormat);
            }
            aba.setColumnView(0,14);
            aba.setColumnView(1,14);
            aba.setColumnView(2,14);
            aba.setColumnView(3,24);
            aba.setColumnView(4,15);
            aba.setColumnView(5,35);
            aba.setColumnView(6,28);

            WritableCellFormat cellForm = new WritableCellFormat();
            cellForm.setAlignment(Alignment.CENTRE);

            for (int i = 0; i < datas.size(); i++) {

                int linha = i+1;
                Label label = new Label(0, linha, datas.get(i),cellForm);
                aba.addCell(label);
                Number number = new Number(1, linha, fechamento.get(i),cellForm);
                aba.addCell(number);
                Number number1 = new Number(2, linha+4, mmc.size() > i? mmc.get(i) : 0,cellForm);
                if(number1.getValue() != 0) aba.addCell(number1);
                Number number2 = new Number(3, linha+19, mmi.size() > i? mmi.get(i) : 0,cellForm);
                if(number2.getValue() != 0) aba.addCell(number2);
                Number number3 = new Number(4, linha+99, mml.size() > i? mml.get(i) : 0,cellForm);
                if(number3.getValue() != 0) aba.addCell(number3);
                Number number4 = new Number(5, linha+4,mmexp.size() > i? mmexp.get(i) : 0,cellForm);
                if(number4.getValue() != 0) aba.addCell(number4);
                Number number5 = new Number(6, linha+4,volatilidade.size() > i?volatilidade.get(i) : 0,cellForm);
                if(number5.getValue() != 0) aba.addCell(number5);
            }
            planilha.write();
            System.out.println("Excel exportado!");
            planilha.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
