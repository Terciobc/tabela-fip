package com.example.tabelafipe.principal;

import com.example.tabelafipe.modelo.Dados;
import com.example.tabelafipe.modelo.Modelos;
import com.example.tabelafipe.modelo.Veiculo;
import com.example.tabelafipe.service.ConsumoApi;
import com.example.tabelafipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/";
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        var menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão
                            
                Digite uma das opções para consulta:
                            
                """;

        System.out.println(menu);
        var opcao = scanner.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("carr")) {
            endereco = BASE_URL + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = BASE_URL + "motos/marcas";
        } else {
            endereco = BASE_URL + "caminhoes/marcas";
        }
        var json = consumo.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta.");
        var codigoMarca = scanner.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);
        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\n Digite um trecho do nome do carro a ser buscado.");
        var nomeVeiculo = scanner.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(e -> e.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("\nModelos filtrados:");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite por favor o código do modelo para busca os valores de avaliação");
        var codigoModelo = scanner.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);
    }
}