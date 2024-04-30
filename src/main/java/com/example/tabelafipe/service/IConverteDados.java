package com.example.tabelafipe.service;

import java.util.List;

public interface IConverteDados {

    //  <T>  Método genérico para tratar com qualquer tipo de objetos. O T é o retorno do método.
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
