package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AtualizaTodosAtivos extends Thread{

    public Ativo ativoA, ativoB, ativoC, ativoD;
    private ArrayList<Double> valoresA, valoresB, valoresC, valoresD;
    private ArrayList<String> dataA, dataB, dataC, dataD;
    private Corretora corretora;


    public AtualizaTodosAtivos(Corretora corretora, Ativo ativoA, Ativo ativoB, Ativo ativoC,Ativo ativoD)
    {
        this.ativoA = ativoA;
        this.ativoB = ativoB;
        this.ativoC = ativoC;
        this.ativoD = ativoD;
        this.corretora = corretora;

        this.valoresA = GetDocumentoFechamento("arquivo01");
        this.valoresB = GetDocumentoFechamento("arquivo02");
        this.valoresC = GetDocumentoFechamento("arquivo03");
        this.valoresD = GetDocumentoFechamento("arquivo04");

        this.dataA = RecuperData("arquivo01");
        this.dataB = RecuperData("arquivo02");
        this.dataC = RecuperData("arquivo03");
        this.dataD = RecuperData("arquivo04");

        //neccesário iniciar com valores para atender a todas as médias.
        for (int i = 0; i<corretora.tamMml; i++)
        {
            ativoA.valores.add(valoresA.get(i));
            ativoB.valores.add(valoresB.get(i));
            ativoC.valores.add(valoresC.get(i));
            ativoD.valores.add(valoresD.get(i));

            ativoA.dataTime.add(dataA.get(i));
            ativoB.dataTime.add(dataB.get(i));
            ativoC.dataTime.add(dataC.get(i));
            ativoD.dataTime.add(dataD.get(i));
        }

    }

    @Override
    public synchronized void run()
    {
        int i = ativoA.valores.size()-1;

        try
        {
            while(valoresA.size() > ativoA.valores.size())
            {
                AtualizaAtivo(ativoA, valoresA.get(i), dataA.get(i));
                AtualizaAtivo(ativoB, valoresB.get(i), dataB.get(i));
                AtualizaAtivo(ativoC, valoresC.get(i), dataC.get(i));
                AtualizaAtivo(ativoD, valoresD.get(i), dataD.get(i));
                i++;

                Thread.sleep(1000);
            }

            System.out.println();
            System.out.println("----------Operações executadas pelos clientes----------");

            for (int j = 0; j < corretora.timeOperacoes.size(); j++)
            {
                System.out.println("cliente: " + corretora.idCliente.get(j) +
                                   " operacacao: " + corretora.compraOuVenda.get(j) +
                                   " ativo: " +  corretora.nomeAtivosOperacao.get(j) +
                                   " quantidade de ativos: " + new DecimalFormat("#,##0.000").format(corretora.qtdAtivo.get(j)) +
                                   " time operacao: "+ corretora.timeOperacoes.get(j));
            }

            System.out.println("------------------------------------------");
            System.out.println();
            System.out.println("**************************");
            System.out.println("Desligando atualiza ativo");
            System.out.println("**************************");
            System.out.println();
            System.out.println("*******************************");
            System.out.println("Desligando THREADs de analise");
            System.out.println("*******************************");
            System.out.println();

            corretora.fim = false;
            Thread.interrupted();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    private void AtualizaAtivo(Ativo ativo, double valor, String data)
    {
        ativo.valores.add(valor);
        ativo.dataTime.add(data);
    }

    public static ArrayList<String> GetDocumento(String file)
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
    public static ArrayList<Double> ConvertStringToDouble(ArrayList<String> arquivo, int inicio, int fim)
    {
        ArrayList<Double> valores = new ArrayList<>();
        for (String val : arquivo) {
            valores.add(Double.parseDouble(val.substring(inicio, fim)));
        }
        return valores;
    }
    public static ArrayList<Double> GetDocumentoFechamento(String file){
        ArrayList<String> aux = GetDocumento(file);
        ArrayList<Double> dados = ConvertStringToDouble(aux,35,42);
        return dados;
    }
    public ArrayList<String> RecuperData (String path)
    {
        ArrayList<String> aux = GetDocumento(path);
        ArrayList<String> valores = new ArrayList<>();
        for (String val : aux) {
            valores.add(val.substring(0, 10));
        }
        return valores;
    }


}
