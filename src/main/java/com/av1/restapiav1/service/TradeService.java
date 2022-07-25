package com.av1.restapiav1.service;

import com.av1.restapiav1.entities.Ativo;
import com.av1.restapiav1.repositories.CsvRepository;
import com.av1.restapiav1.utils.Cliente;
import com.av1.restapiav1.utils.Corretora;
import com.av1.restapiav1.utils.DynamicDataDemo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Number;
import jxl.write.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TradeService {

    private CsvRepository csvRepository;

    public TradeService()
    {
        this.csvRepository = csvRepository;
    }


    public void Iniciar () //método para gerenciar a simulação de trades
    {
        Ativo ativo1 = new Ativo();
        Ativo ativo2 = new Ativo();
        Ativo ativo3 = new Ativo();
        Ativo ativo4 = new Ativo();

        Cliente cliente = new Cliente(ativo1, ativo2, ativo3, ativo4);
        cliente.run();
    }

}
