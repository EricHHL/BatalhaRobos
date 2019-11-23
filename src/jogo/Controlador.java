/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import entidades.Arma;
import entidades.Bomba;
import entidades.Entidade;
import entidades.Robo;
import entidades.Virus;
import excecoes.ExcecaoMorte;
import excecoes.SaveCorrompidoException;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import util.JSONUtil;
import util.JogoSalvo;

/**
 *
 * @author ehhl
 */
public class Controlador extends javax.swing.JFrame {

    int turno = 1;

    Robo robo1;
    Robo robo2;

    Thread threadJogo;
    boolean executando = false;
    boolean fimDeJogo = true;

    /**
     * Creates new form Jogo
     */
    public Controlador() {
        initComponents();

        try {
            this.threadJogo();
        } catch (InterruptedException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        MyLogger.listen(this::log);
    }

    final void threadJogo() throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (executando) {
                        try {
                            if (turno > 0) {
                                lidaComColisao(robo1, robo1.executaTurno());
                            } else {
                                lidaComColisao(robo2, robo2.executaTurno());
                            }
                        } catch (ExcecaoMorte ex) {
                            JOptionPane.showMessageDialog(rootPane, "Fim do jogo\n" + ex.getMorto().getNome() + " morreu");
                            mnuSalvar.setEnabled(false);
                            fimDeJogo = true;
                            executando = false;
                        }

                        turno *= -1;
                        atualizaGUIInfos();
                    }
                }
            }
        }.start();
    }

    void lidaComColisao(Robo robo, Entidade colisao) throws ExcecaoMorte {
        if (colisao == null || colisao instanceof Robo) {
            return;
        }
        if (colisao instanceof Bomba) {
            Bomba bomba = (Bomba) colisao;
            robo.daDano(bomba.getDano());
            MyLogger.log(robo.getNome() + " tomou " + bomba.getDano() + " de dano de: Bomba");
            return;
        }
        if (colisao instanceof Arma) {
            Arma arma = (Arma) colisao;
            int resp = JOptionPane.showOptionDialog(rootPane,
                    "Deseja pegar a arma " + arma.getNome() + "?\nDano: " + arma.getDano() + "\nAlcance: " + arma.getAlcance() + "\nChance de tiro: " + arma.getChanceTiro(),
                    turno > 0 ? "Jogador 1" : "Jogador 2",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    new javax.swing.ImageIcon(getClass().getResource("/imagens/" + arma.getImagem())),
                    new String[]{"Trocar", "Ignorar"},
                    "Trocar"
            );
            if (resp == JOptionPane.YES_OPTION) {
                robo.setArma(arma);
                atualizaGUIImagens();
                MyLogger.log(robo.getNome() + " trocou de arma");
            }
            return;
        }
        if (colisao instanceof Virus) {
            MyLogger.log(robo.getNome() + " foi infectado por um virus");
            robo.infecta((Virus) colisao);
        }

    }

    private void inicializaJogo(Robo robo1, Robo robo2, Arma arma1, Arma arma2) {
        arena.reset();
        robo1.setXY(4, 1);
        robo2.setXY(4, 8);

        robo1.setJogador(1);
        robo2.setJogador(2);

        robo1.setOponente(robo2);
        robo2.setOponente(robo1);

        arena.novaEntidade(robo1);
        arena.novaEntidade(robo2);

        this.robo1 = robo1;
        this.robo2 = robo2;

        arma1.inicializa();
        robo1.setArma(arma1);

        arma2.inicializa();
        robo2.setArma(arma2);

        ArrayList<Arma> armas = JSONUtil.getArmas(false);
        for (int i = 0; i < 15; i++) {
            Arma arma = armas.get((int) (Math.random() * armas.size()));
            int[] pos = arena.getPosicaoAleatoriaVazia();
            arma.setXY(pos[0], pos[1]);
            arena.novaEntidade(arma);
        }
        for (int i = 0; i < 5; i++) {
            Bomba bomba = new Bomba(2);
            int[] pos = arena.getPosicaoAleatoriaVazia();
            bomba.setXY(pos[0], pos[1]);
            arena.novaEntidade(bomba);
        }
        for (int i = 0; i < 5; i++) {
            Virus virus = new Virus(3);
            int[] pos = arena.getPosicaoAleatoriaVazia();
            virus.setXY(pos[0], pos[1]);
            arena.novaEntidade(virus);
        }

        atualizaGUIImagens();
        executando = false;
        fimDeJogo = false;
        mnuSalvar.setEnabled(true);
        btControle.setText("Iniciar");
    }

    private void carregaJogo(JogoSalvo save) {
        arena.reset();
        turno = save.getTurno();
        save.getEntidades().forEach((entidade) -> {
            arena.novaEntidade(entidade);
            if(entidade instanceof Robo){
                if(((Robo) entidade).getJogador()==1){
                    this.robo1 = (Robo) entidade;
                }else{
                    this.robo2 = (Robo) entidade;
                }
            }
        });
        
        this.robo1.setOponente(robo2);
        this.robo2.setOponente(robo1);
        
        robo1.getArma().inicializa();
        robo2.getArma().inicializa();
        
        executando = false;
        fimDeJogo = false;
        mnuSalvar.setEnabled(true);
        btControle.setText("Iniciar");
        
        atualizaGUIImagens();
        atualizaGUIInfos();
    }

    private void atualizaGUIImagens() {
        lblRobo1.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo1.getImagem())));
        lblArma1.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo1.getArma().getImagem())));
        lblRobo2.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo2.getImagem())));
        lblArma2.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo2.getArma().getImagem())));

        lblRobo1.repaint();
    }

    private void atualizaGUIInfos() {
        lblVida1.setText(robo1.getVida() + "");
        lblDano1.setText(robo1.getArma().getDano() + "");
        lblAlcance1.setText(robo1.getArma().getAlcance() + "");
        lblChance1.setText(robo1.getArma().getChancePorcento());
        lblVida2.setText(robo2.getVida() + "");
        lblDano2.setText(robo2.getArma().getDano() + "");
        lblAlcance2.setText(robo2.getArma().getAlcance() + "");
        lblChance2.setText(robo2.getArma().getChancePorcento());
    }

    private void log(Object mensagem) {
        ((DefaultListModel) lstLog.getModel()).add(0, mensagem);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        arena = new jogo.Arena();
        jPanel1 = new javax.swing.JPanel();
        lblArma1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblVida1 = new javax.swing.JLabel();
        lblRobo1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblDano1 = new javax.swing.JLabel();
        lblAlcance1 = new javax.swing.JLabel();
        lblChance1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblArma2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblVida2 = new javax.swing.JLabel();
        lblRobo2 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblDano2 = new javax.swing.JLabel();
        lblAlcance2 = new javax.swing.JLabel();
        lblChance2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstLog = new javax.swing.JList<>();
        btControle = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuNovoJogo = new javax.swing.JMenuItem();
        mnuSalvar = new javax.swing.JMenuItem();
        mnuCarregar = new javax.swing.JMenuItem();
        mnuSair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Batalha de robôs");
        setMinimumSize(null);

        arena.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        arena.setMaximumSize(new java.awt.Dimension(480, 480));
        arena.setMinimumSize(new java.awt.Dimension(480, 480));
        arena.setPreferredSize(new java.awt.Dimension(483, 483));

        javax.swing.GroupLayout arenaLayout = new javax.swing.GroupLayout(arena);
        arena.setLayout(arenaLayout);
        arenaLayout.setHorizontalGroup(
            arenaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );
        arenaLayout.setVerticalGroup(
            arenaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Robô 1"));

        lblArma1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel2.setText("Vida:");

        lblVida1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        lblRobo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel6.setText("Dano:");

        jLabel7.setText("Alcance:");

        jLabel8.setText("Chance de tiro:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblArma1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRobo1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVida1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDano1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAlcance1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblChance1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRobo1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblVida1)
                            .addComponent(jLabel2))))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lblDano1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblAlcance1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(lblChance1)))
                    .addComponent(lblArma1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Robô 2"));

        lblArma2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel13.setText("Vida:");

        lblVida2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        lblRobo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel16.setText("Dano:");

        jLabel17.setText("Alcance:");

        jLabel18.setText("Chance de tiro:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblArma2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRobo2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVida2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDano2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAlcance2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblChance2)))
                .addGap(57, 57, 57))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRobo2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblVida2)
                            .addComponent(jLabel13))))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(lblDano2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(lblAlcance2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(lblChance2)))
                    .addComponent(lblArma2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lstLog.setModel(new DefaultListModel<>());
        jScrollPane1.setViewportView(lstLog);

        btControle.setText("Iniciar");
        btControle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btControleActionPerformed(evt);
            }
        });

        jMenu1.setText("Arquivo");

        mnuNovoJogo.setText("Novo jogo");
        mnuNovoJogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNovoJogoActionPerformed(evt);
            }
        });
        jMenu1.add(mnuNovoJogo);

        mnuSalvar.setText("Salvar");
        mnuSalvar.setEnabled(false);
        mnuSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSalvarActionPerformed(evt);
            }
        });
        jMenu1.add(mnuSalvar);

        mnuCarregar.setText("Carregar");
        mnuCarregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCarregarActionPerformed(evt);
            }
        });
        jMenu1.add(mnuCarregar);

        mnuSair.setText("Sair");
        mnuSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSairActionPerformed(evt);
            }
        });
        jMenu1.add(mnuSair);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(arena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(btControle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(arena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btControle)))
                .addGap(3, 3, 3))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuNovoJogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNovoJogoActionPerformed
        DialogNovoJogo dialog = new DialogNovoJogo(this, true);
        dialog.setVisible(true);
        inicializaJogo(dialog.getRoboJogador1(), dialog.getRoboJogador2(), dialog.getArmaJogador1(), dialog.getArmaJogador2());
        dialog.dispose();
    }//GEN-LAST:event_mnuNovoJogoActionPerformed

    private void btControleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btControleActionPerformed
        if (fimDeJogo) {
            return;
        }
        executando = !executando;
        btControle.setText(executando ? "Pausar" : "Resumir");
    }//GEN-LAST:event_btControleActionPerformed

    private void mnuSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mnuSairActionPerformed

    private void mnuSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSalvarActionPerformed
        if (fimDeJogo) {
            return;
        }
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("Jogos salvos", "brs");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            if (!f.getName().endsWith(".brs")) {
                f = new File(f.getAbsolutePath() + ".brs");
            }
            JSONUtil.salvarJogo(f, arena, turno);
            JOptionPane.showMessageDialog(null, "Jogo '"+f.getName()+"' salvo com sucesso!");
        }
    }//GEN-LAST:event_mnuSalvarActionPerformed

    private void mnuCarregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCarregarActionPerformed
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("Jogos salvos", "brs");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                JogoSalvo save = JSONUtil.carregarJogo(jfc.getSelectedFile());
                this.carregaJogo(save);
            } catch (SaveCorrompidoException ex) {
                JOptionPane.showMessageDialog(null, "O arquivo de jogo está corrompido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuCarregarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Controlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Controlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Controlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Controlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Controlador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private jogo.Arena arena;
    private javax.swing.JButton btControle;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlcance1;
    private javax.swing.JLabel lblAlcance2;
    private javax.swing.JLabel lblArma1;
    private javax.swing.JLabel lblArma2;
    private javax.swing.JLabel lblChance1;
    private javax.swing.JLabel lblChance2;
    private javax.swing.JLabel lblDano1;
    private javax.swing.JLabel lblDano2;
    private javax.swing.JLabel lblRobo1;
    private javax.swing.JLabel lblRobo2;
    private javax.swing.JLabel lblVida1;
    private javax.swing.JLabel lblVida2;
    private javax.swing.JList<String> lstLog;
    private javax.swing.JMenuItem mnuCarregar;
    private javax.swing.JMenuItem mnuNovoJogo;
    private javax.swing.JMenuItem mnuSair;
    private javax.swing.JMenuItem mnuSalvar;
    // End of variables declaration//GEN-END:variables
}
