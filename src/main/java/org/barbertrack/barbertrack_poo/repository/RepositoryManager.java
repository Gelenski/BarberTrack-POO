package org.barbertrack.barbertrack_poo.repository;

import java.io.*;
import java.util.ArrayList;

public class RepositoryManager {

    public static void salvar(String caminhoArquivo, ArrayList<Object> dados) {
        File arquivo = new File(caminhoArquivo);

        try {
            arquivo.getParentFile().mkdirs();

            FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(dados);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException a) {
            System.out.println("Erro ao salvar em arquivo: " + a.getMessage());
        }
    }


    public static ArrayList<Object> carregar(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            System.out.println("O arquivo a ser carregado não existe.");
            return new ArrayList<>();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(arquivo);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            ArrayList<Object> dados = (ArrayList<Object>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return dados;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}



