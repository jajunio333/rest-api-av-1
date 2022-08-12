package com.av1.restapiav1.utils;
import com.av1.restapiav1.entities.Ativo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.List;
public class CotacaoExterna extends Thread{
    public Ativo ativoA, ativoB, ativoC, ativoD;
    private Corretora corretora;
    public CotacaoExterna(Corretora corretora, Ativo ativoA, Ativo ativoB, Ativo ativoC, Ativo ativoD)
    {
        this.ativoA = ativoA;
        this.ativoB = ativoB;
        this.ativoC = ativoC;
        this.ativoD = ativoD;
        this.corretora = corretora;
    }
    @Override
    public synchronized void run()
    {
        try
        {
            GetDocumento(ativoA, ativoB, ativoC, ativoD);

            Thread.sleep(1000);

            PrintOperacoesCorretora();

            corretora.fim = false;
            this.interrupt();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void GetDocumento(Ativo ativoA, Ativo ativoB, Ativo ativoC, Ativo ativoD)
    {
        try {
            String path1 = "C:\\Users\\jajun\\Desktop\\rest-api-av-1\\src\\main\\java\\com\\av1\\restapiav1\\files\\arquivo01.csv";
            String path2 = "C:\\Users\\jajun\\Desktop\\rest-api-av-1\\src\\main\\java\\com\\av1\\restapiav1\\files\\arquivo02.csv";
            String path3 = "C:\\Users\\jajun\\Desktop\\rest-api-av-1\\src\\main\\java\\com\\av1\\restapiav1\\files\\arquivo03.csv";
            String path4 = "C:\\Users\\jajun\\Desktop\\rest-api-av-1\\src\\main\\java\\com\\av1\\restapiav1\\files\\arquivo04.csv";

            FileReader filereader = new FileReader(path1);
            FileReader filereader1 = new FileReader(path2);
            FileReader filereader2 = new FileReader(path3);
            FileReader filereader3 = new FileReader(path4);

            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            CSVReader csvReader1 = new CSVReaderBuilder(filereader1).withSkipLines(1).build();
            CSVReader csvReader2 = new CSVReaderBuilder(filereader2).withSkipLines(1).build();
            CSVReader csvReader3 = new CSVReaderBuilder(filereader3).withSkipLines(1).build();

            List<String[]> allData = csvReader.readAll();
            List<String[]> allData1 = csvReader1.readAll();
            List<String[]> allData2 = csvReader2.readAll();
            List<String[]> allData3 = csvReader3.readAll();

            for (int i = 0; i < allData.size()-1; i++) {

                String[] row1 = allData.get(i);
                String[] row2 = allData1.get(i);
                String[] row3 = allData2.get(i);
                String[] row4 = allData3.get(i);

                for (String cell : row1) {
                   ativoA.valores.add(Double.parseDouble(cell.substring(35, 42)));
                   ativoA.dataTime.add(cell.substring(0, 10));
                }
                for (String cell : row2) {
                    ativoB.valores.add(Double.parseDouble(cell.substring(35, 42)));
                    ativoB.dataTime.add(cell.substring(0, 10));
                }
                for (String cell : row3) {
                    ativoC.valores.add(Double.parseDouble(cell.substring(35, 42)));
                    ativoC.dataTime.add(cell.substring(0, 10));
                }
                for (String cell : row4) {
                    ativoD.valores.add(Double.parseDouble(cell.substring(35, 42)));
                    ativoD.dataTime.add(cell.substring(0, 10));
                }

                if (i > corretora.tamMml)
                {
                    Thread.sleep(1200);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void PrintOperacoesCorretora ()
    {
        System.out.println();
        System.out.println("----------Operações executadas pelos clientes na corretora----------");
        for (int j = 0; j < corretora.timeOperacoes.size()-1; j++)
        {
            System.out.println("cliente: " + (corretora.idCliente.size() > j ? corretora.idCliente.get(j) : -1) +
                    " operacacao: " + (corretora.compraOuVenda.size() > j ? corretora.compraOuVenda.get(j) : 'x') +
                    " ativo: " +  (corretora.nomeAtivosOperacao.size() > j ? corretora.nomeAtivosOperacao.get(j) : -1) +
                    " quantidade de ativos (Corretora): " + new DecimalFormat("#,##0.000")
                    .format(corretora.qtdAtivoEntrada.size() > j ? corretora.qtdAtivoEntrada.get(j) : -1) +
                    " quantidade de ativos (Cliente): " + new DecimalFormat("#,##0.000")
                    .format(corretora.qtdAtivoSaida.size() > j ? corretora.qtdAtivoSaida.get(j) : -1) +
                    " quantidade de ativos (Cliente) Reconciliado: " + new DecimalFormat("#,##0.000")
                    .format(corretora.qtdAtivoSaidaRec.size() > j ? corretora.qtdAtivoSaidaRec.get(j) : -1) +
                    " time operacao: "+ corretora.timeOperacoes.get(j));
        }
        System.out.println("------------------------------------------");
        System.out.println();

        System.out.println("**************************");
        System.out.println("Desligando cotacao externa");
        System.out.println("**************************");
        System.out.println();
        System.out.println("*******************************");
        System.out.println("Desligando THREADs de analise");
        System.out.println("*******************************");
        System.out.println();
    }
}
