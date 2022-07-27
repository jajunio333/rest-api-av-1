package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Corretora extends Thread{

    public Ativo ativoA, ativoB, ativoC, ativoD;

    private Semaphore caixa1, caixa2;
    public static final int tamMml= 100; //média mais longa requer 100 valores
    public static boolean fim = true;

    public Corretora(Ativo ativoA, Ativo ativoB, Ativo ativoC,Ativo ativoD)
    {
        this.ativoA = ativoA;
        this.ativoB = ativoB;
        this.ativoC = ativoC;
        this.ativoD = ativoD;
    }

    @Override
    public synchronized void run() {

            AtualizaTodosAtivos atualizaTodosAtivos = new AtualizaTodosAtivos(ativoA, ativoB, ativoC, ativoD);

            Thread att = new Thread(atualizaTodosAtivos);

            att.start();



        //TODO: após atualizar todos, exibe o balaço final de cada cliente -> salvar pelo ID?
        //TODO: medotodos de compra e venda -->ideia retorna boleano sinalizando compra/venda executada (SEMAFORO)
    }









    /*
    private void compra(Ativo ativo){

        if(caixa1.tryAcquire()){
            ativo.valor = getAtivo(ativo.nomeAtivo).valor[0];
            ativo.comprado = true;
            caixa1.release();
        }else{
            if(caixa2.tryAcquire()){
                ativo.valor = getAtivo(ativo.nomeAtivo).valor[0];
                ativo.comprado = true;
                caixa2.release();
            }
        }

    }

    private void venda(Ativo ativo){

        if(caixa1.tryAcquire()){
            ativo.valor = getAtivo(ativo.nomeAtivo).valor[0];
            ativo.comprado = false;
            caixa1.release();
        }else{
            if(caixa2.tryAcquire()){
                ativo.valor = getAtivo(ativo.nomeAtivo).valor[0];
                ativo.comprado = false;
                caixa2.release();
            }
        }

    }



    private Ativo getAtivo(String nome){
        if(nome == ativoA.nomeAtivo)
        {
            return ativoA;
        }
        else
        {
            if(nome == ativoB.nomeAtivo)
            {
                return ativoB;
            }
            else
            {
                if(nome == ativoC.nomeAtivo)
                {
                    return ativoC;
                }
                else
                {
                    if(nome == ativoD.nomeAtivo)
                    {
                        return ativoD;
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }
    }
*/

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
    public ArrayList<Double> GetDocumentoFechamento(String file){
        ArrayList<String> aux = GetDocumento(file);
        ArrayList<Double> dados = ConvertStringToDouble(aux,35,42);
        return dados;
    }

}
