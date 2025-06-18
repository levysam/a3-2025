package com.estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estoque.repository.ProdutoRepository;
import com.estoque.repository.CategoriaRepository;
import com.estoque.dto.*;
import com.estoque.model.Produto;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class RelatorioService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ListaPrecoDTO> gerarListaPrecos() {
        return produtoRepository.findAllWithCategoriaOrderByNome().stream()
            .map(p -> new ListaPrecoDTO(
                p.getNome(),
                p.getPrecoUnitario(),
                p.getUnidadeMedida(),
                p.getCategoria().getNome()))
            .collect(Collectors.toList());
    }

    public List<BalancoDTO> gerarBalanco() {
        List<BalancoDTO> balanco = produtoRepository.findAllWithCategoriaOrderByNome().stream()
            .map(p -> new BalancoDTO(
                p.getNome(),
                p.getQuantidadeAtual(),
                p.getPrecoUnitario(),
                p.getPrecoUnitario().multiply(BigDecimal.valueOf(p.getQuantidadeAtual()))))
            .collect(Collectors.toList());

        return balanco;
    }

    public BigDecimal calcularValorTotalEstoque() {
        return produtoRepository.findAllWithCategoriaOrderByNome().stream()
            .map(p -> p.getPrecoUnitario().multiply(BigDecimal.valueOf(p.getQuantidadeAtual())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAbaixoMinimo() {
        return produtoRepository.findProdutosAbaixoDoMinimo().stream()
            .map(p -> new ProdutoQuantidadeDTO(
                p.getNome(),
                p.getQuantidadeMinima(),
                p.getQuantidadeMaxima(),
                p.getQuantidadeAtual()))
            .collect(Collectors.toList());
    }

    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAcimaMaximo() {
        return produtoRepository.findProdutosAcimaDoMaximo().stream()
            .map(p -> new ProdutoQuantidadeDTO(
                p.getNome(),
                p.getQuantidadeMinima(),
                p.getQuantidadeMaxima(),
                p.getQuantidadeAtual()))
            .collect(Collectors.toList());
    }

    public List<CategoriaProdutosDTO> gerarRelatorioQuantidadePorCategoria() {
        return categoriaRepository.countProdutosPorCategoria().stream()
            .map(result -> new CategoriaProdutosDTO(
                result.getCategoria(),
                result.getQuantidade()))
            .collect(Collectors.toList());
    }

    public void exportarListaPrecosPdf(List<ListaPrecoDTO> lista) throws Exception {
        Document document = new Document();
        String path = "relatorios/lista_precos.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        document.add(new Paragraph("Lista de Preços"));
        PdfPTable table = new PdfPTable(4);
        table.addCell("Produto");
        table.addCell("Preço Unitário");
        table.addCell("Unidade");
        table.addCell("Categoria");
        for (ListaPrecoDTO item : lista) {
            table.addCell(item.getNome());
            table.addCell(item.getPrecoUnitario().toString());
            table.addCell(item.getUnidadeMedida());
            table.addCell(item.getCategoria());
        }
        document.add(table);
        document.close();
    }

    public void exportarBalancoPdf(List<BalancoDTO> balanco) throws Exception {
        Document document = new Document();
        String path = "relatorios/balanco.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        document.add(new Paragraph("Balanço Físico/Financeiro"));
        PdfPTable table = new PdfPTable(4);
        table.addCell("Produto");
        table.addCell("Quantidade");
        table.addCell("Preço Unitário");
        table.addCell("Valor Total");
        for (BalancoDTO item : balanco) {
            table.addCell(item.getNome());
            table.addCell(item.getQuantidadeDisponivel().toString());
            table.addCell(item.getPrecoUnitario().toString());
            table.addCell(item.getValorTotal().toString());
        }
        document.add(table);
        document.close();
    }

    public void exportarProdutosAbaixoMinimoPdf(List<ProdutoQuantidadeDTO> lista) throws Exception {
        Document document = new Document();
        String path = "relatorios/produtos_abaixo_minimo.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        document.add(new Paragraph("Produtos Abaixo do Mínimo"));
        PdfPTable table = new PdfPTable(3);
        table.addCell("Produto");
        table.addCell("Quantidade Mínima");
        table.addCell("Quantidade Atual");
        for (ProdutoQuantidadeDTO item : lista) {
            table.addCell(item.getNome());
            table.addCell(item.getQuantidadeMinima().toString());
            table.addCell(item.getQuantidadeAtual().toString());
        }
        document.add(table);
        document.close();
    }

    public void exportarProdutosAcimaMaximoPdf(List<ProdutoQuantidadeDTO> lista) throws Exception {
        Document document = new Document();
        String path = "relatorios/produtos_acima_maximo.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        document.add(new Paragraph("Produtos Acima do Máximo"));
        PdfPTable table = new PdfPTable(3);
        table.addCell("Produto");
        table.addCell("Quantidade Máxima");
        table.addCell("Quantidade Atual");
        for (ProdutoQuantidadeDTO item : lista) {
            table.addCell(item.getNome());
            table.addCell(item.getQuantidadeMaxima().toString());
            table.addCell(item.getQuantidadeAtual().toString());
        }
        document.add(table);
        document.close();
    }

    public void exportarProdutosPorCategoriaPdf(List<CategoriaProdutosDTO> lista) throws Exception {
        Document document = new Document();
        String path = "relatorios/produtos_por_categoria.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        document.add(new Paragraph("Produtos por Categoria"));
        PdfPTable table = new PdfPTable(2);
        table.addCell("Categoria");
        table.addCell("Quantidade de Produtos");
        for (CategoriaProdutosDTO item : lista) {
            table.addCell(item.getCategoria());
            table.addCell(item.getQuantidadeProdutos().toString());
        }
        document.add(table);
        document.close();
    }
} 