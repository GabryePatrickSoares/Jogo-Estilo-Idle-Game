package br.com.harpyjam;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TelaJogo extends JFrame {

    // moeda = energia (kWh)
    double energia = 5.0;

    // UI principais
    private JLabel lblEnergia;
    private JPanel contentPanel; // painel que fica dentro do JScrollPane
    private JScrollPane scrollPane;
    private JPanel bottomPanel;

    // estruturas de geradores e gerentes
    final List<Gerador> geradores = new ArrayList<>();
    private final List<Gerente> gerentes = new ArrayList<>();

    // Dialog de gerentes
    private JDialog dialogGerentes;
    private JPanel painelGerentesList;

    public TelaJogo() {
        inicializarDados();
        initComponents();
        carregarJogo();
        atualizarInterface();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarJogo();
            }
        });
    }

    // -------------------- Inicialização dos dados --------------------
    private void inicializarDados() {
        geradores.clear();
        geradores.add(new Gerador("Gerador de Limão", 5.0, 0.5, 2000));
        geradores.add(new Gerador("Gerador de Batata", 20.0, 1.0, 4000));
        geradores.add(new Gerador("Gerador de Bicicleta", 100.0, 3.0, 8000));
        geradores.add(new Gerador("Mini Turbina de Água", 500.0, 10.0, 16000));
        geradores.add(new Gerador("Gerador Eólico Caseiro", 2000.0, 25.0, 32000));
        geradores.add(new Gerador("Gerador Solar Caseiro", 10000.0, 60.0, 64000));
        geradores.add(new Gerador("Painel Solar Avançado", 50000.0, 150.0, 128000));
        geradores.add(new Gerador("Turbina Eólica Industrial", 200000.0, 400.0, 256000));
        geradores.add(new Gerador("Usina de Biomassa", 800000.0, 1000.0, 510000));
        geradores.add(new Gerador("Usina Geotérmica", 2000000.0, 2500.0, 1020000));
        geradores.add(new Gerador("Usina das Marés", 5000000.0, 6000.0, 2040000));
        geradores.add(new Gerador("Usina de Hidrogênio Verde", 10000000.0, 15000.0, 4080000));
        geradores.add(new Gerador("Estação Orbital Solar", 50000000.0, 50000.0, 8160000));

        for (Gerador g : geradores) {
            gerentes.add(new Gerente(g.nome + " - Gerente", Math.round(g.custoInicial * 10.0), g));
        }
    }

    // -------------------- UI --------------------
    private void initComponents() {
        setTitle("Energy Idle");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(420, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(255, 204, 41));
        topPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        lblEnergia = new JLabel();
        lblEnergia.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(lblEnergia);
        add(topPanel, BorderLayout.PAGE_START);

        // Content scroll
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        getContentPane().setBackground(new Color(255, 255, 204));
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // Geradores
        for (int i = 0; i < geradores.size(); i++) {
            contentPanel.add(new PainelGerador(i));
            contentPanel.add(Box.createVerticalStrut(6));
        }

        // Bottom panel
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(255, 204, 41));
        bottomPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
        JButton btnGerentes = new JButton("GERENTES");
        btnGerentes.addActionListener(e -> abrirDialogGerentes());
        bottomPanel.add(btnGerentes);
        add(bottomPanel, BorderLayout.PAGE_END);

        // Dialog gerentes
        dialogGerentes = new JDialog(this, "Gerentes", true);
        dialogGerentes.setSize(540, 440);
        dialogGerentes.setLocationRelativeTo(this);
        dialogGerentes.getContentPane().setBackground(new Color(255, 255, 204)); // fundo claro igual ao principal

        painelGerentesList = new JPanel();
        painelGerentesList.setLayout(new BoxLayout(painelGerentesList, BoxLayout.Y_AXIS));
        painelGerentesList.setOpaque(true);
        painelGerentesList.setBackground(new Color(255, 255, 204)); // amarelo claro do corpo principal

        JScrollPane sc = new JScrollPane(painelGerentesList);
        sc.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 41), 3)); // borda amarela
        sc.setBackground(new Color(255, 255, 204));
        sc.getViewport().setBackground(new Color(255, 255, 204));
        sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sc.getVerticalScrollBar().setUnitIncrement(16);

        // adiciona cabeçalho opcional
        JPanel topDialog = new JPanel();
        topDialog.setBackground(new Color(255, 204, 41));
        topDialog.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel tituloGerentes = new JLabel("Gerentes Disponíveis");
        tituloGerentes.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topDialog.add(tituloGerentes);

        dialogGerentes.setLayout(new BorderLayout());
        dialogGerentes.add(topDialog, BorderLayout.NORTH);
        dialogGerentes.add(sc, BorderLayout.CENTER);

        dialogGerentes.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                atualizarInterface();
            }
        });

        dialogGerentes.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                atualizarInterface();
            }
        });
    }

    // -------------------- PainelGerador como classe interna --------------------
    private class PainelGerador extends JPanel {
        public PainelGerador(int index) {
            super(new BorderLayout());
            Gerador g = geradores.get(index);

            // Borda do título
            TitledBorder borda = BorderFactory.createTitledBorder(
                    BorderFactory.createEmptyBorder(),
                    g.nome,
                    TitledBorder.LEFT, TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLACK
            );
            setBorder(BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(6, 6, 6, 6)));
            setOpaque(false);

            // Centro: botão gerar + progresso + melhorar
            JPanel painelCentro = new JPanel(new GridBagLayout());
            painelCentro.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;

            // Botão gerar
            JButton btnGerar = criarBotao("", "/br/com/harpyjam/images/imgBtnGerar_" + index + ".png", 90, 90);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 0;
            gbc.weightx = 0; gbc.ipady = 0;
            painelCentro.add(btnGerar, gbc);

            // Barra de progresso
            JProgressBar barra = new BarraProgressoAmarela();
            barra.setValue(0);
            gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 3; gbc.gridheight = 1;
            gbc.weightx = 0.7; gbc.ipady = 0;
            painelCentro.add(barra, gbc);

            // Botão melhorar
            String imgMelhorarHabilitado = "/br/com/harpyjam/images/imgBtnMelhorar.png";
            String imgMelhorarDesabilitado = "/br/com/harpyjam/images/imgBtnMelhorarDes.png";

            JButton btnMelhorar = criarBotao(
                "Melhorar  " + formatNumber(g.custoUpgrade),
                energia >= g.custoUpgrade ? imgMelhorarHabilitado : imgMelhorarDesabilitado,
                220, 65
            );
            gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
            painelCentro.add(btnMelhorar, gbc);

            // Timer
            JLabel lblTimer = new JLabel("00:00:00");
            gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 1;
            painelCentro.add(lblTimer, gbc);

            // Painel compra
            JPanel painelCompra = new JPanel(new GridBagLayout());
            painelCompra.setOpaque(false);
            // Caminhos das imagens
            String imgComprarHabilitado = "/br/com/harpyjam/images/imgBtnComprar.png";
            String imgComprarDesabilitado = "/br/com/harpyjam/images/imgBtnComprarDes.png";

            // Criar botão inicialmente com base na energia disponível
            JButton btnComprar = criarBotao(
                    "Comprar  " + formatNumber(g.custoInicial),
                    energia >= g.custoInicial ? imgComprarHabilitado : imgComprarDesabilitado,
                    330, 80
            );
            painelCompra.add(btnComprar, new GridBagConstraints());


            add(painelCentro, BorderLayout.CENTER);
            add(painelCompra, BorderLayout.SOUTH);

            // Estado inicial
            painelCentro.setVisible(g.comprado);
            painelCompra.setVisible(!g.comprado);

            // Listeners
            btnComprar.addActionListener(e -> {
                if (energia >= g.custoInicial) {
                    energia -= g.custoInicial;
                    g.comprado = true;
                    atualizarEnergiaLabel();
                    painelCentro.setVisible(true);
                    painelCompra.setVisible(false);
                    btnMelhorar.setText("Melhorar  " + formatNumber(g.custoUpgrade));
                } else {
                    JOptionPane.showMessageDialog(TelaJogo.this, "Energia insuficiente para comprar " + g.nome, "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            btnGerar.addActionListener(e -> iniciarGeracaoManual(index, btnGerar, barra, lblTimer));

            btnMelhorar.addActionListener(e -> {
                if (energia >= g.custoUpgrade) {
                    energia -= g.custoUpgrade;
                    g.level++;
                    g.ganho *=1.05;
                    g.custoUpgrade = Math.round(g.custoUpgrade * 1.1);
                    atualizarEnergiaLabel();
                    btnMelhorar.setText("Melhorar  " + formatNumber(g.custoUpgrade));
                } else {
                    JOptionPane.showMessageDialog(TelaJogo.this, "Energia insuficiente para melhorar " + g.nome, "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            g.bindComponents(btnGerar, barra, lblTimer, btnMelhorar, btnComprar, painelCentro, painelCompra);
        }
    }

    // -------------------- Cria JButton com imagem e texto --------------------
    private JButton criarBotao(String texto, String pathIcon, int largura, int altura) {
        JButton btn = new JButton(texto);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.CENTER);

        if (largura > 0 && altura > 0) {
            btn.setPreferredSize(new Dimension(largura, altura));
            btn.setMinimumSize(new Dimension(largura, altura));
            btn.setMaximumSize(new Dimension(largura, altura));
        }

        if (pathIcon != null) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(pathIcon));
                btn.addComponentListener(new java.awt.event.ComponentAdapter() {
                    int lastWidth = 0, lastHeight = 0;
                    @Override
                    public void componentResized(java.awt.event.ComponentEvent e) {
                        int w = btn.getWidth(), h = btn.getHeight();
                        if (w > 0 && h > 0 && (w != lastWidth || h != lastHeight)) {
                            lastWidth = w; lastHeight = h;
                            SwingUtilities.invokeLater(() -> {
                                Image scaled = icon.getImage().getScaledInstance(w - 10, h - 10, Image.SCALE_SMOOTH);
                                btn.setIcon(new ImageIcon(scaled));
                            });
                        }
                    }
                });
            } catch (Exception ex) {
                System.err.println("Imagem não encontrada: " + pathIcon);
            }
        }
        return btn;
    }

    // -------------------- Geração manual --------------------
    private void iniciarGeracaoManual(int index, JButton btnGerar, JProgressBar barra, JLabel lblTimer) {
        Gerador g = geradores.get(index);
        if (!g.comprado || !btnGerar.isEnabled()) return;

        btnGerar.setEnabled(false);
        barra.setValue(0);
        lblTimer.setText("");

        int tempoMs = Math.max(200, g.tempoMs);
        int steps = 100;
        int delay = Math.max(10, tempoMs / steps);

        Timer timer = new Timer(delay, null);
        timer.addActionListener(new AbstractAction() {
            int progresso = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                progresso++;
                barra.setValue(progresso);
                int restanteSec = Math.max(0, (tempoMs * (100 - progresso) / 100) / 1000);
                lblTimer.setText(formatTempo(restanteSec));
                if (progresso >= steps) {
                    timer.stop();
                    synchronized (TelaJogo.this) { energia += g.ganho * g.level; }
                    SwingUtilities.invokeLater(() -> {
                        atualizarEnergiaLabel();
                        lblTimer.setText("00:00:00");
                        barra.setValue(0);
                        btnGerar.setEnabled(true);
                    });
                }
            }
        });
        timer.start();
    }

    // -------------------- Geração automática pelo gerente --------------------
    private void iniciarGeracaoAutomatica(Gerador g) {
        if (!g.comprado || !g.gerenteAtivo || (g.autoTimer != null && g.autoTimer.isRunning())) return;

        int tempoMs = Math.max(200, g.tempoMs);
        int steps = 100;
        int delay = Math.max(10, tempoMs / steps);

        Timer t = new Timer(delay, null);
        t.addActionListener(new AbstractAction() {
            int progresso = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (g.barra == null) return;
                progresso++;
                g.barra.setValue(progresso);
                int restanteSec = Math.max(0, (tempoMs * (100 - progresso) / 100) / 1000);
                g.lblTimer.setText(formatTempo(restanteSec));
                if (progresso >= steps) {
                    progresso = 0;
                    synchronized (TelaJogo.this) { energia += g.ganho * g.level; }
                    atualizarEnergiaLabel();
                }
            }
        });
        t.start();
        g.autoTimer = t;
        if (g.btnGerar != null) g.btnGerar.setEnabled(false);
    }

    // -------------------- Gerentes UI --------------------
    private void abrirDialogGerentes() {
        painelGerentesList.removeAll();
        painelGerentesList.setBorder(new EmptyBorder(8,8,8,8));

        for (int i = 0; i < gerentes.size(); i++) {
            Gerente gm = gerentes.get(i);
            JPanel row = new JPanel(new BorderLayout());
            row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 204, 41), 2),
                new EmptyBorder(8, 8, 8, 8)
            ));
            row.setBackground(new Color(255, 255, 230)); // tom levemente mais claro

            JLabel nome = new JLabel(gm.nome);
            JButton comprar = new JButton("Comprar Gerente  " + formatNumber(gm.custo));
            if (gm.ativo) { comprar.setText("Gerente Ativo"); comprar.setEnabled(false); }

            int idx = i;
            comprar.addActionListener(e -> {
                if (energia >= gm.custo) {
                    energia -= gm.custo;
                    gm.ativo = true;
                    gm.vinculado.gerenteAtivo = true;
                    iniciarGeracaoAutomatica(gm.vinculado);
                    atualizarEnergiaLabel();
                    abrirDialogGerentes();
                } else {
                    JOptionPane.showMessageDialog(this, "Energia insuficiente para contratar gerente", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            row.add(nome, BorderLayout.WEST);
            row.add(comprar, BorderLayout.EAST);
            painelGerentesList.add(row);
            comprar.setBackground(new Color(255, 204, 41));
            comprar.setForeground(Color.BLACK);
            comprar.setFont(new Font("Segoe UI", Font.BOLD, 13));
            comprar.setFocusPainted(false);
            comprar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        }

        painelGerentesList.revalidate();
        painelGerentesList.repaint();
        dialogGerentes.setVisible(true);
    }

    // -------------------- Atualizações UI --------------------
    private void atualizarEnergiaLabel() {
        lblEnergia.setText(String.format("Energia:  %,.2f kWh", energia));
        // Atualiza todos os botões de compra
        for (Gerador g : geradores) {
            atualizarBotaoComprar(g);
            atualizarBotaoMelhorar(g);
        }
    }
    
    private void atualizarBotaoComprar(Gerador g) {
        if (g.comprado || g.btnComprar == null) return;
        String imgHabilitado = "/br/com/harpyjam/images/imgBtnComprar.png";
        String imgDesabilitado = "/br/com/harpyjam/images/imgBtnComprarDes.png";
        String caminho = (energia >= g.custoInicial) ? imgHabilitado : imgDesabilitado;

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(caminho));
            Image scaled = icon.getImage().getScaledInstance(g.btnComprar.getWidth()-10, g.btnComprar.getHeight()-10, Image.SCALE_SMOOTH);
            g.btnComprar.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            System.err.println("Imagem não encontrada: " + caminho);
        }
    }

    private void atualizarInterface() {
        atualizarEnergiaLabel();
        for (Gerador g : geradores) {
            if (g.gerenteAtivo && g.comprado) iniciarGeracaoAutomatica(g);
            if (g.btnComprar != null) g.btnComprar.setVisible(!g.comprado);
            if (g.painelCentro != null) g.painelCentro.setVisible(g.comprado);
            if (g.btnMelhorar != null) g.btnMelhorar.setText("Melhorar  " + formatNumber(g.custoUpgrade));
            if (g.btnGerar != null) g.btnGerar.setEnabled(!g.gerenteAtivo);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private String formatTempo(int segundos) {
        int horas = segundos / 3600;
        int minutos = (segundos % 3600) / 60;
        int seg = segundos % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, seg);
    }

    private String formatNumber(double value) {
        return (value >= 1000) ? String.format("%,.0f", value) : String.format("%.2f", value);
    }

    // -------------------- Classes internas --------------------
    private static class Gerador {
        String nome;
        double custoInicial, custoUpgrade, ganho;
        int tempoMs, level = 1;
        boolean comprado = false, gerenteAtivo = false;
        JButton btnGerar, btnMelhorar, btnComprar;
        JProgressBar barra;
        JLabel lblTimer;
        JPanel painelCentro, painelCompra;
        Timer autoTimer;

        public Gerador(String nome, double custoInicial, double ganho, int tempoMs) {
            this.nome = nome;
            this.custoInicial = custoInicial;
            this.custoUpgrade = Math.round(custoInicial * 1.25);
            this.ganho = ganho;
            this.tempoMs = tempoMs;
        }

        public void bindComponents(JButton btnGerar, JProgressBar barra, JLabel lblTimer, JButton btnMelhorar, JButton btnComprar, JPanel painelCentro, JPanel painelCompra) {
            this.btnGerar = btnGerar;
            this.barra = barra;
            this.lblTimer = lblTimer;
            this.btnMelhorar = btnMelhorar;
            this.btnComprar = btnComprar;
            this.painelCentro = painelCentro;
            this.painelCompra = painelCompra;
        }
    }

    private static class Gerente {
        String nome;
        double custo;
        boolean ativo = false;
        Gerador vinculado;

        public Gerente(String nome, double custo, Gerador vinculado) {
            this.nome = nome;
            this.custo = custo;
            this.vinculado = vinculado;
        }
    }

    private static class BarraProgressoAmarela extends JProgressBar {
        private final Color corContorno = new Color(255, 204, 41);
        private final Color corPreenchimento = new Color(102, 204, 0);

        public BarraProgressoAmarela() {
            super(0, 100);
            setBorderPainted(false);
            setStringPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int largura = getWidth();
            int altura = getHeight();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(corPreenchimento);
            g2.fillRoundRect(0, 0, (int)(getPercentComplete()*largura), altura, altura, altura);
            g2.setStroke(new BasicStroke(5f));
            g2.setColor(corContorno);
            g2.drawRoundRect(2, 2, largura-4, altura-4, altura, altura);
            g2.dispose();
        }
    }
    
    private void atualizarBotaoMelhorar(Gerador g) {
        if (!g.comprado || g.btnMelhorar == null) return;
        String imgHabilitado = "/br/com/harpyjam/images/imgBtnMelhorar.png";
        String imgDesabilitado = "/br/com/harpyjam/images/imgBtnMelhorarDes.png";
        String caminho = (energia >= g.custoUpgrade) ? imgHabilitado : imgDesabilitado;

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(caminho));
            Image scaled = icon.getImage().getScaledInstance(g.btnMelhorar.getWidth()-10, g.btnMelhorar.getHeight()-10, Image.SCALE_SMOOTH);
            g.btnMelhorar.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            System.err.println("Imagem não encontrada: " + caminho);
        }
    }


    // -------------------- SALVAR / CARREGAR --------------------
    private void salvarJogo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("savegame.txt"))) {
            writer.println(energia);
            for (Gerador g : geradores) {
                writer.printf("%s;%.2f;%.2f;%.2f;%d;%d;%b;%b%n",
                        g.nome, g.custoInicial, g.custoUpgrade, g.ganho,
                        g.tempoMs, g.level, g.comprado, g.gerenteAtivo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar o jogo: " + e.getMessage());
        }
    }

    private void carregarJogo() {
        File arquivo = new File("savegame.txt");
        if (!arquivo.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha = reader.readLine();
            if (linha != null) energia = Double.parseDouble(linha.trim().replace(",", "."));
            int i = 0;
            while ((linha = reader.readLine()) != null && i < geradores.size()) {
                String[] dados = linha.split(";");
                if (dados.length < 8) continue;
                Gerador g = geradores.get(i);
                g.nome = dados[0];
                g.custoInicial = Double.parseDouble(dados[1].replace(",", "."));
                g.custoUpgrade = Double.parseDouble(dados[2].replace(",", "."));
                g.ganho = Double.parseDouble(dados[3].replace(",", "."));
                g.tempoMs = Integer.parseInt(dados[4]);
                g.level = Integer.parseInt(dados[5]);
                g.comprado = Boolean.parseBoolean(dados[6]);
                g.gerenteAtivo = Boolean.parseBoolean(dados[7]);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar o jogo: " + e.getMessage());
        }
    }

    // -------------------- Main --------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaJogo().setVisible(true));
    }
}
