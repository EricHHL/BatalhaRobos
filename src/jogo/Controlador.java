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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author ehhl
 */
public class Controlador extends javax.swing.JFrame {

    int turno = 1;

    Robo robo1;
    Robo robo2;

    /**
     * Creates new form Jogo
     */
    public Controlador() {
        initComponents();

        robo1 = new Robo("robo5.png");
        robo1.setXY(4, 1);
        robo1.setNome("Robo1");
        robo2 = new Robo("robo2.png");
        robo2.setXY(4, 8);
        robo2.setNome("Robo2");

        robo1.setOponente(robo2);
        robo2.setOponente(robo1);

        arena.novaEntidade(robo1);
        arena.novaEntidade(robo2);

        Arma arma1 = new Arma("arma1-2.png", 5, 10, 0.5f);
        arma1.inicializa();
        robo1.setArma(arma1);

        Arma arma2 = new Arma("arma3-1.png", 5, 10, 0.5f);
        arma2.inicializa();
        robo2.setArma(arma2);

        Arma armaNoChao = new Arma("arma1-1.png", 10, 20, 0.5f);
        armaNoChao.setXY(4, 2);
        arena.novaEntidade(armaNoChao);

        Bomba bomba = new Bomba(1);
        bomba.setXY(5, 1);
        arena.novaEntidade(bomba);
        Bomba bomba2 = new Bomba(1);
        bomba2.setXY(3, 1);
        arena.novaEntidade(bomba2);

        Virus virus = new Virus(5);
        virus.setXY(4, 0);
        arena.novaEntidade(virus);

        try {
            this.threadJogo();
        } catch (InterruptedException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        inicializaGUI();
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
                    Entidade colisao;
                    try {
                        if (turno > 0) {
                            colisao = robo1.executaTurno();
                        } else {
                            colisao = robo2.executaTurno();
                        }
                        lidaComColisao(robo1, colisao);
                    } catch (ExcecaoMorte ex) {
                        JOptionPane.showMessageDialog(rootPane, "Fim do jogo\n" + ex.getMorto().getNome() + " morreu");
                        System.exit(0);
                    }

                    turno *= -1;
                    atualizaGUI();
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
                    "Deseja trocar de arma?\nDano: " + arma.getDano() + "\nAlcance: " + arma.getAlcance() + "\nChance de tiro: " + arma.getChanceTiro(),
                    "Você encontrou uma arma",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    new javax.swing.ImageIcon(getClass().getResource("/imagens/" + arma.getImagem())),
                    new String[]{"Trocar", "Ignorar"},
                    "Trocar"
            );
            if (resp == JOptionPane.YES_OPTION) {
                robo.setArma(arma);

                MyLogger.log(robo.getNome() + " trocou de arma");
            }
            return;
        }
        if (colisao instanceof Virus) {
            MyLogger.log(robo.getNome() + " foi infectado por um virus");
            robo.infecta((Virus) colisao);
        }

    }

    private void inicializaGUI() {
        lblRobo1.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo1.getImagem())));
        lblArma1.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo1.getArma().getImagem())));
        lblRobo2.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo2.getImagem())));
        lblArma2.setIcon(new ImageIcon(getClass().getResource("/imagens/" + robo2.getArma().getImagem())));

        lblRobo1.repaint();
    }

    private void atualizaGUI() {
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
        ((DefaultListModel) lstLog.getModel()).addElement(mensagem);
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuNovoJogo = new javax.swing.JMenuItem();
        mnuSair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
        lblArma1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/arma1-1.png"))); // NOI18N

        jLabel2.setText("Vida:");

        lblVida1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lblVida1.setText("20");

        lblRobo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRobo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/robo1.png"))); // NOI18N

        jLabel6.setText("Dano:");

        jLabel7.setText("Alcance:");

        jLabel8.setText("Chance de tiro:");

        lblDano1.setText("5");

        lblAlcance1.setText("10");

        lblChance1.setText("50%");

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
        lblArma2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/arma4-3.png"))); // NOI18N

        jLabel13.setText("Vida:");

        lblVida2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lblVida2.setText("20");

        lblRobo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRobo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/robo3.png"))); // NOI18N

        jLabel16.setText("Dano:");

        jLabel17.setText("Alcance:");

        jLabel18.setText("Chance de tiro:");

        lblDano2.setText("15");

        lblAlcance2.setText("6");

        lblChance2.setText("40%");

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

        jMenu1.setText("Arquivo");

        mnuNovoJogo.setText("Novo jogo");
        mnuNovoJogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNovoJogoActionPerformed(evt);
            }
        });
        jMenu1.add(mnuNovoJogo);

        mnuSair.setText("Sair");
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
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(arena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuNovoJogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNovoJogoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuNovoJogoActionPerformed

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
    private javax.swing.JMenuItem mnuNovoJogo;
    private javax.swing.JMenuItem mnuSair;
    // End of variables declaration//GEN-END:variables
}
