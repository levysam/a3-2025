package com.estoque.frontend.panels;

import com.estoque.service.RelatorioService;
import com.estoque.dto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RelatoriosPanel extends JPanel {
    private final RelatorioService relatorioService;
    private final JTabbedPane tabbedPane;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public RelatoriosPanel(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Adiciona as abas
        tabbedPane.addTab("Lista de Preços", criarPainelListaPrecos());
        tabbedPane.addTab("Balanço Físico/Financeiro", criarPainelBalanco());
        tabbedPane.addTab("Produtos Abaixo do Mínimo", criarPainelProdutosAbaixoMinimo());
        tabbedPane.addTab("Produtos Acima do Máximo", criarPainelProdutosAcimaMaximo());
        tabbedPane.addTab("Produtos por Categoria", criarPainelProdutosPorCategoria());

        // Botão de atualizar
        JButton atualizarButton = new JButton("Atualizar Relatórios");
        atualizarButton.addActionListener(e -> atualizarTodosRelatorios());
        add(atualizarButton, BorderLayout.SOUTH);

        // Carrega os dados iniciais
        atualizarTodosRelatorios();
    }

    private JPanel criarPainelListaPrecos() {
        String[] colunas = {"Produto", "Preço Unitário", "Unidade", "Categoria"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JButton exportarButton = new JButton("Exportar PDF");
        exportarButton.addActionListener(e -> {
            try {
                relatorioService.exportarListaPrecosPdf(relatorioService.gerarListaPrecos());
                JOptionPane.showMessageDialog(this, "PDF gerado em relatorios/lista_precos.pdf");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(exportarButton);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.putClientProperty("tableModel", model);
        return panel;
    }

    private JPanel criarPainelBalanco() {
        String[] colunas = {"Produto", "Quantidade", "Preço Unitário", "Valor Total"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JLabel totalLabel = new JLabel("Valor Total do Estoque: R$ 0,00");
        JButton exportarButton = new JButton("Exportar PDF");
        exportarButton.addActionListener(e -> {
            try {
                relatorioService.exportarBalancoPdf(relatorioService.gerarBalanco());
                JOptionPane.showMessageDialog(this, "PDF gerado em relatorios/balanco.pdf");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(exportarButton);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(totalLabel, BorderLayout.SOUTH);
        panel.putClientProperty("tableModel", model);
        panel.putClientProperty("totalLabel", totalLabel);
        return panel;
    }

    private JPanel criarPainelProdutosAbaixoMinimo() {
        String[] colunas = {"Produto", "Quantidade Mínima", "Quantidade Atual"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JButton exportarButton = new JButton("Exportar PDF");
        exportarButton.addActionListener(e -> {
            try {
                relatorioService.exportarProdutosAbaixoMinimoPdf(relatorioService.gerarRelatorioProdutosAbaixoMinimo());
                JOptionPane.showMessageDialog(this, "PDF gerado em relatorios/produtos_abaixo_minimo.pdf");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(exportarButton);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.putClientProperty("tableModel", model);
        return panel;
    }

    private JPanel criarPainelProdutosAcimaMaximo() {
        String[] colunas = {"Produto", "Quantidade Máxima", "Quantidade Atual"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JButton exportarButton = new JButton("Exportar PDF");
        exportarButton.addActionListener(e -> {
            try {
                relatorioService.exportarProdutosAcimaMaximoPdf(relatorioService.gerarRelatorioProdutosAcimaMaximo());
                JOptionPane.showMessageDialog(this, "PDF gerado em relatorios/produtos_acima_maximo.pdf");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(exportarButton);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.putClientProperty("tableModel", model);
        return panel;
    }

    private JPanel criarPainelProdutosPorCategoria() {
        String[] colunas = {"Categoria", "Quantidade de Produtos"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JButton exportarButton = new JButton("Exportar PDF");
        exportarButton.addActionListener(e -> {
            try {
                relatorioService.exportarProdutosPorCategoriaPdf(relatorioService.gerarRelatorioQuantidadePorCategoria());
                JOptionPane.showMessageDialog(this, "PDF gerado em relatorios/produtos_por_categoria.pdf");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(exportarButton);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.putClientProperty("tableModel", model);
        return panel;
    }

    private void atualizarTodosRelatorios() {
        try {
            atualizarListaPrecos();
            atualizarBalanco();
            atualizarProdutosAbaixoMinimo();
            atualizarProdutosAcimaMaximo();
            atualizarProdutosPorCategoria();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar relatórios: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaPrecos() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(0);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<ListaPrecoDTO> lista = relatorioService.gerarListaPrecos();
        for (ListaPrecoDTO item : lista) {
            model.addRow(new Object[]{
                item.getNome(),
                currencyFormat.format(item.getPrecoUnitario()),
                item.getUnidadeMedida(),
                item.getCategoria()
            });
        }
    }

    private void atualizarBalanco() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(1);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        JLabel totalLabel = (JLabel) panel.getClientProperty("totalLabel");
        model.setRowCount(0);

        List<BalancoDTO> balanco = relatorioService.gerarBalanco();
        BigDecimal valorTotalEstoque = BigDecimal.ZERO;

        for (BalancoDTO item : balanco) {
            model.addRow(new Object[]{
                item.getNome(),
                item.getQuantidadeDisponivel(),
                currencyFormat.format(item.getPrecoUnitario()),
                currencyFormat.format(item.getValorTotal())
            });
            valorTotalEstoque = valorTotalEstoque.add(item.getValorTotal());
        }

        totalLabel.setText("Valor Total do Estoque: " + currencyFormat.format(valorTotalEstoque));
    }

    private void atualizarProdutosAbaixoMinimo() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(2);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<ProdutoQuantidadeDTO> lista = relatorioService.gerarRelatorioProdutosAbaixoMinimo();
        for (ProdutoQuantidadeDTO item : lista) {
            model.addRow(new Object[]{
                item.getNome(),
                item.getQuantidadeMinima(),
                item.getQuantidadeAtual()
            });
        }
    }

    private void atualizarProdutosAcimaMaximo() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(3);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<ProdutoQuantidadeDTO> lista = relatorioService.gerarRelatorioProdutosAcimaMaximo();
        for (ProdutoQuantidadeDTO item : lista) {
            model.addRow(new Object[]{
                item.getNome(),
                item.getQuantidadeMaxima(),
                item.getQuantidadeAtual()
            });
        }
    }

    private void atualizarProdutosPorCategoria() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(4);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<CategoriaProdutosDTO> lista = relatorioService.gerarRelatorioQuantidadePorCategoria();
        for (CategoriaProdutosDTO item : lista) {
            model.addRow(new Object[]{
                item.getCategoria(),
                item.getQuantidadeProdutos()
            });
        }
    }
} 