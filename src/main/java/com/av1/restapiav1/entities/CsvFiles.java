package com.av1.restapiav1.entities;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;


import java.util.ArrayList;


@EntityScan
@Getter
@Setter
public class CsvFiles {
    private ArrayList<String> documento;
    private ArrayList<String> mediaMovel;
    private ArrayList<String> mediaMovelExp;
    private String caminho;

    public CsvFiles (String caminho, ArrayList<String> documento,
                     ArrayList<String> mediaMovel, ArrayList<String> mediaMovelExp)
    {
        this.caminho = caminho;
        this.documento = documento;
        this.mediaMovel = mediaMovel;
        this.mediaMovelExp = mediaMovelExp;
    }
}
