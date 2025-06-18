package com.estoque.frontend;

import javax.swing.*;
import java.awt.*;
import com.estoque.model.*;
import com.estoque.service.*;
import com.estoque.repository.*;
import com.estoque.frontend.panels.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.estoque")
@EntityScan("com.estoque.model")
@EnableJpaRepositories("com.estoque.repository")
public class EstoqueDesktopApp extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final EstoqueService estoqueService;
    private final RelatorioService relatorioService;

    public EstoqueDesktopApp(ProdutoService produtoService, CategoriaService categoriaService, EstoqueService estoqueService, RelatorioService relatorioService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.estoqueService = estoqueService;
        this.relatorioService = relatorioService;
        
        setTitle("Sistema de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Painel de Produtos
        ProdutoPanel produtosPanel = new ProdutoPanel(produtoService, categoriaService);
        tabbedPane.addTab("Produtos", produtosPanel);

        // Painel de Movimentações
        MovimentacaoPanel movimentacoesPanel = new MovimentacaoPanel(produtoService, estoqueService);
        tabbedPane.addTab("Movimentações", movimentacoesPanel);

        // Painel de Categorias
        CategoriaPanel categoriasPanel = new CategoriaPanel(categoriaService);
        tabbedPane.addTab("Categorias", categoriasPanel);

        // Painel de Relatórios
        RelatoriosPanel relatoriosPanel = new RelatoriosPanel(relatorioService);
        tabbedPane.addTab("Relatórios", relatoriosPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EstoqueDesktopApp.class)
            .headless(false)
            .run(args);

        SwingUtilities.invokeLater(() -> {
            EstoqueDesktopApp app = new EstoqueDesktopApp(
                context.getBean(ProdutoService.class),
                context.getBean(CategoriaService.class),
                context.getBean(EstoqueService.class),
                context.getBean(RelatorioService.class)
            );
            app.setVisible(true);
        });
    }
} 