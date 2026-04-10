package br.com.guilda.service;

import br.com.guilda.dto.ProdutoLojaResponse;
import br.com.guilda.model.ProdutoLojaDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import org.springframework.stereotype.Service;
import br.com.guilda.dto.AgrupamentoQuantidadeResponse;
import br.com.guilda.dto.FaixaPrecoResponse;
import br.com.guilda.dto.PrecoMedioResponse;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.io.IOException;
import java.util.List;

@Service
public class ProdutoBuscaService {

    private static final String INDEX = "guilda_loja";

    private final ElasticsearchClient client;

    public ProdutoBuscaService(ElasticsearchClient client) {
        this.client = client;
    }

    public List<ProdutoLojaResponse> buscarPorNome(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .match(m -> m
                                        .field("nome")
                                        .query(termo)
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<ProdutoLojaResponse> buscarPorDescricao(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .match(m -> m
                                        .field("descricao")
                                        .query(termo)
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<ProdutoLojaResponse> buscarPorFraseExata(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .matchPhrase(mp -> mp
                                        .field("descricao")
                                        .query(termo)
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<ProdutoLojaResponse> buscarFuzzyPorNome(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field("nome")
                                        .value(termo)
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<ProdutoLojaResponse> buscarEmMultiplosCampos(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .multiMatch(mm -> mm
                                        .query(termo)
                                        .fields("nome", "descricao")
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<ProdutoLojaResponse> buscarPorDescricaoComCategoria(String termo, String categoria) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .match(mm -> mm
                                                        .field("descricao")
                                                        .query(termo)
                                                )
                                        )
                                        .filter(f -> f
                                                .term(t -> t
                                                        .field("categoria")
                                                        .value(categoria)
                                                )
                                        )
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<ProdutoLojaResponse> buscarPorFaixaDePreco(Double min, Double max) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .range(r -> r
                                        .field("preco")
                                        .gte(JsonData.of(min))
                                        .lte(JsonData.of(max))
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<ProdutoLojaResponse> buscarAvancada(String categoria,
                                                    String raridade,
                                                    Double min,
                                                    Double max) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .bool(b -> b
                                        .filter(f -> f
                                                .term(t -> t
                                                        .field("categoria")
                                                        .value(categoria)
                                                )
                                        )
                                        .filter(f -> f
                                                .term(t -> t
                                                        .field("raridade")
                                                        .value(raridade)
                                                )
                                        )
                                        .filter(f -> f
                                                .range(r -> r
                                                        .field("preco")
                                                        .gte(JsonData.of(min))
                                                        .lte(JsonData.of(max))
                                                )
                                        )
                                )
                        ),
                ProdutoLojaDocument.class
        );

        return mapear(response);
    }

    public List<AgrupamentoQuantidadeResponse> agruparPorCategoria() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                        .index(INDEX)
                        .size(0)
                        .aggregations("por_categoria", a -> a
                                .terms(t -> t
                                        .field("categoria")
                                        .size(20)
                                )
                        ),
                Void.class
        );

        List<AgrupamentoQuantidadeResponse> resultado = new ArrayList<>();

        var buckets = response.aggregations()
                .get("por_categoria")
                .sterms()
                .buckets()
                .array();

        for (var bucket : buckets) {
            resultado.add(new AgrupamentoQuantidadeResponse(
                    bucket.key().stringValue(),
                    bucket.docCount()
            ));
        }

        return resultado;
    }

    public List<AgrupamentoQuantidadeResponse> agruparPorRaridade() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                        .index(INDEX)
                        .size(0)
                        .aggregations("por_raridade", a -> a
                                .terms(t -> t
                                        .field("raridade")
                                        .size(20)
                                )
                        ),
                Void.class
        );

        List<AgrupamentoQuantidadeResponse> resultado = new ArrayList<>();

        var buckets = response.aggregations()
                .get("por_raridade")
                .sterms()
                .buckets()
                .array();

        for (var bucket : buckets) {
            resultado.add(new AgrupamentoQuantidadeResponse(
                    bucket.key().stringValue(),
                    bucket.docCount()
            ));
        }

        return resultado;
    }

    public PrecoMedioResponse calcularPrecoMedio() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                        .index(INDEX)
                        .size(0)
                        .aggregations("preco_medio", a -> a
                                .avg(avg -> avg.field("preco"))
                        ),
                Void.class
        );

        Double valor = response.aggregations()
                .get("preco_medio")
                .avg()
                .value();

        BigDecimal precoMedio = valor != null
                ? BigDecimal.valueOf(valor)
                : BigDecimal.ZERO;

        return new PrecoMedioResponse(precoMedio);
    }

    public List<FaixaPrecoResponse> agruparPorFaixaDePreco() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                        .index(INDEX)
                        .size(0)
                        .aggregations("faixas_preco", a -> a
                                .range(r -> r
                                        .field("preco")
                                        .ranges(rr -> rr.to("100"))
                                        .ranges(rr -> rr.from("100").to("300"))
                                        .ranges(rr -> rr.from("300").to("700"))
                                        .ranges(rr -> rr.from("700"))
                                )
                        ),
                Void.class
        );

        List<FaixaPrecoResponse> resultado = new ArrayList<>();

        var buckets = response.aggregations()
                .get("faixas_preco")
                .range()
                .buckets()
                .array();

        for (int i = 0; i < buckets.size(); i++) {
            String faixa;

            if (i == 0) {
                faixa = "Abaixo de 100";
            } else if (i == 1) {
                faixa = "De 100 a 300";
            } else if (i == 2) {
                faixa = "De 300 a 700";
            } else {
                faixa = "Acima de 700";
            }

            resultado.add(new FaixaPrecoResponse(
                    faixa,
                    buckets.get(i).docCount()
            ));
        }

        return resultado;
    }

    private List<ProdutoLojaResponse> mapear(SearchResponse<ProdutoLojaDocument> response) {
        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .filter(doc -> doc != null)
                .map(doc -> new ProdutoLojaResponse(
                        doc.getNome(),
                        doc.getDescricao(),
                        doc.getCategoria(),
                        doc.getRaridade(),
                        doc.getPreco()
                ))
                .toList();
    }
}