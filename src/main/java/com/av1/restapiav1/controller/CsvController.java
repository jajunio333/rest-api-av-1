package com.av1.restapiav1.controller;

import com.av1.restapiav1.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/dados")
public class CsvController {

    @Autowired
    private CsvService service;

    @GetMapping("/getdados/{path}")
    public ArrayList<String> GetDocumento(@PathVariable("path") String file){
        ArrayList<String> dados = service.GetDocumento(file);
        return dados;
    }
    @GetMapping("/getdados/fechamento/{path}")
    public ArrayList<Double> GetDocumentoFechamento(@PathVariable("path") String file){
        ArrayList<String> aux = service.GetDocumento(file);
        ArrayList<Double> dados = service.ConvertStringToDouble(aux,35,42);
        return dados;
    }
    @GetMapping("/mma/{path}/{size}")
    public ArrayList<Double> MediaSimples(@PathVariable("path") String path,@PathVariable("size") int size){
        ArrayList<Double> MMA = service.MediaSimples(path, size);
        return MMA;
    }
    @GetMapping("/mma/curta/{path}")
    public ArrayList<Double> MediaCurta(@PathVariable("path") String path){
        ArrayList<Double> MMA = service.MediaSimples(path, 5);
        return MMA;
    }
    @GetMapping("/mma/intermediaria/{path}")
    public ArrayList<Double> MediaIntermediaria(@PathVariable("path") String path){
        ArrayList<Double> MMA = service.MediaSimples(path, 20);
        return MMA;
    }
    @GetMapping("/mma/longa/{path}")
    public ArrayList<Double> MediaLonga(@PathVariable("path") String path){
        ArrayList<Double> MMA = service.MediaSimples(path, 100);
        return MMA;
    }
    @GetMapping("/mme/{path}/{size}")
    public ArrayList<Double> MediaExp(@PathVariable("path") String path,@PathVariable("size") int size){
        ArrayList<Double> MME = service.MediaExp(path, size);
        return MME;
    }
    @GetMapping("/lista-volatilidade/{path}/{size}")
    public ArrayList<Double> ListaVolatilidade(@PathVariable("path") String path,@PathVariable("size") int size){
        ArrayList<Double> dados = service.Volatilidade(path, size);
        return dados;
    }
    @GetMapping("/export-excel/{path}")
    public void ExportExcel(@PathVariable("path") String path) {
        service.ExportaExcel(path);
    }





    @GetMapping("/print/{path}")
    public void PrintRealTime(String path)
    {
        service.PrintRealTime(GetDocumentoFechamento("arquivo01"));
    }
}
