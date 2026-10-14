/**
 * pacote que coordena o frame do chat no que se  refere a interface gráfica e comunicação com o usuário
 */
package chatMulticast;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import util.ComunicadorUDP;

/**
 *
 * @author Turma Sistemas Distribuídos 2019-2
 */
public class JFrame_chatMulticast extends javax.swing.JFrame {
    
    HashMap<String, InetAddress> usuariosOnline = new HashMap<>();

    public JFrame_chatMulticast() {
        initComponents();
        carregarHistorico();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_Chat = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Mensagens = new javax.swing.JTextArea();
        jTextField_textoDeEnvio = new javax.swing.JTextField();
        jButton_Enviar = new javax.swing.JButton();
        jLabel_Porta = new javax.swing.JLabel();
        jTextField_Porta = new javax.swing.JTextField();
        jLabel_ServidorIP = new javax.swing.JLabel();
        jButton_Sair = new javax.swing.JButton();
        jTextField_GrupoIP = new javax.swing.JTextField();
        jLabel_Nick = new javax.swing.JLabel();
        jTextField_Nick = new javax.swing.JTextField();
        jButton_Conectar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_usuariosOnline = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea_Mensagens.setEditable(false);
        jTextArea_Mensagens.setColumns(20);
        jTextArea_Mensagens.setRows(5);
        jTextArea_Mensagens.setFocusable(false);
        jScrollPane1.setViewportView(jTextArea_Mensagens);

        jTextField_textoDeEnvio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_textoDeEnvioKeyPressed(evt);
            }
        });

        jButton_Enviar.setText("Enviar");
        jButton_Enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EnviarActionPerformed(evt);
            }
        });

        jLabel_Porta.setText("Porta");

        jTextField_Porta.setText("3456");

        jLabel_ServidorIP.setText("Grupo IP:");

        jButton_Sair.setText("Sair");
        jButton_Sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SairActionPerformed(evt);
            }
        });

        jTextField_GrupoIP.setText("239.1.2.3");

        jLabel_Nick.setText("Nick:");

        jButton_Conectar.setText("Conectar");
        jButton_Conectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ConectarActionPerformed(evt);
            }
        });

        jList_usuariosOnline.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Todos" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList_usuariosOnline);

        javax.swing.GroupLayout jPanel_ChatLayout = new javax.swing.GroupLayout(jPanel_Chat);
        jPanel_Chat.setLayout(jPanel_ChatLayout);
        jPanel_ChatLayout.setHorizontalGroup(
            jPanel_ChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ChatLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel_ServidorIP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField_GrupoIP, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Porta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField_Porta, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_Nick)
                .addGap(18, 18, 18)
                .addComponent(jTextField_Nick, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButton_Conectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jButton_Sair)
                .addGap(2, 2, 2))
            .addGroup(jPanel_ChatLayout.createSequentialGroup()
                .addComponent(jTextField_textoDeEnvio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_Enviar))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane2)
        );
        jPanel_ChatLayout.setVerticalGroup(
            jPanel_ChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ChatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_ChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_textoDeEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Enviar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel_ChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_ServidorIP)
                    .addComponent(jTextField_GrupoIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Porta)
                    .addComponent(jTextField_Porta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Nick)
                    .addComponent(jTextField_Nick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Conectar)
                    .addComponent(jButton_Sair))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel_Chat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * classe interna que garante que o processo de ler/ouvir mensagens seja executado concomitantemente
     */
    class ThreadReceptora extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket pacote = ComunicadorUDP.recebeMensagem(socket);
                if (pacote == null) continue; 

                String msgRecebida = new String(pacote.getData(), 0, pacote.getLength());
                InetAddress ipRemetente = pacote.getAddress();
                String meuNick = jTextField_Nick.getText();

                if (msgRecebida.startsWith("[SYS_ONLINE]")) {
                    String nickExistente = msgRecebida.substring(12).trim();
                    
                   
                    if (!nickExistente.equals(meuNick)) {
                        usuariosOnline.put(nickExistente, ipRemetente);
                        atualizarListaInterface();
                    }
                    
                    
                    continue; 
                }


                if (msgRecebida.contains("entrou na sala")) {
                    String nickNovo = msgRecebida.split(" ")[0];
                    
                    usuariosOnline.put(nickNovo, ipRemetente);
                    atualizarListaInterface();
                    
                  
                    if (!nickNovo.equals(meuNick)) {
                        String msgResposta = "[SYS_ONLINE]" + meuNick;
                        DatagramPacket pctResp = ComunicadorUDP.montaMensagem(msgResposta, jTextField_GrupoIP.getText(), Integer.parseInt(jTextField_Porta.getText()));
                        socket.send(pctResp); 
                    }
                }
                
       
                else if (msgRecebida.contains("saiu da sala")) {
                    String nickSaindo = msgRecebida.split(" ")[0];
                    usuariosOnline.remove(nickSaindo);
                    atualizarListaInterface();
                }

                
                lista.add(msgRecebida);
                salvarHistorico(msgRecebida);
                
              
                jTextArea_Mensagens.append(msgRecebida + "\n");
                
            } catch (Exception e) {
                System.out.println("Erro na recepção multicast: " + e.getMessage());
            }
        }
    }
}
    
    
    class ThreadReceptoraUnicast extends Thread {
        @Override
        public void run() {
            JFrame meuFrame = JFrame_chatMulticast.this;
            while (true) {
                try{
                    DatagramPacket pacote = ComunicadorUDP.recebeMensagem(socketUnicast);
                    
                    if (pacote != null){
                        String msgRecebida = new String(pacote.getData(), 0, pacote.getLength());
                        
                        lista.add(msgRecebida);
                        salvarHistorico(msgRecebida);
                        
                        jTextArea_Mensagens.append(msgRecebida + "\n");
                        
                        
                    }
                }catch(Exception e){
                        System.out.println("Erro na thread unicast: " + e.getMessage());
                }
            }
        }
    }
    
    private void atualizarListaInterface(){
        DefaultListModel<String> modelo = new DefaultListModel<>();
        
        modelo.addElement("Todos");
        
        for (String nick : usuariosOnline.keySet()){
            if(!nick.equals(jTextField_Nick.getText())){
                modelo.addElement(nick);
            }
        }
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                jList_usuariosOnline.setModel(modelo);
            }
            
        });
    }
    
    private void salvarHistorico(String mensagem){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("historico_chat.txt", true))){
            bw.write(mensagem);
            bw.newLine();
        }catch (IOException e){
            System.err.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }
    
   private void carregarHistorico(){
       File arquivo = new File("historico_chat.txt");
       if (arquivo.exists()){
           try (BufferedReader br = new BufferedReader(new FileReader(arquivo))){
               String linha;
               jTextArea_Mensagens.append("---- Histórico Anterior ----\n");
               while ((linha = br.readLine()) != null){
                   jTextArea_Mensagens.append(linha + "\n");
               }
               jTextArea_Mensagens.append("------------------------\n");
               
           }catch(IOException e){
               System.err.println("Erro ao ler histórico: " + e.getMessage());
           }
       }
   }

    /**
     * método privado que avisa que um computador saiu do grupo e encerra a conexão
     * @throws IOException
     * @throws NumberFormatException
     * @throws NullPointerException 
     */
    private void sairDoSistema() throws IOException, NumberFormatException, NullPointerException {
        try {
            String msg = jTextField_Nick.getText() + " saiu da sala";
            DatagramPacket pacote = ComunicadorUDP.montaMensagem(msg, jTextField_GrupoIP.getText(), Integer.parseInt(jTextField_Porta.getText()));
            socket.send(pacote);

        } catch (IOException | NumberFormatException | NullPointerException e) {
            if (e.getClass().toString().equals("class java.lang.NullPointerException")) {
                JOptionPane.showMessageDialog(this, "Você está saindo sem ter se conectado");
            } else {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } finally {
            System.exit(0);
        }
    }

    /**
     * método privado que realiza a conexão do computador em um grupo multicast,
     * tendo como referência endereço virtual do grupo e a porta do socket. O processo de escuta/leitura
     * é circundado por thread de leitura
     * @param evt contém o evento recebido pelo tratador
     */
    private void jButton_ConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ConectarActionPerformed
        jTextField_GrupoIP.getText();
        jTextField_Porta.getText();
        try {
            if (jTextField_Nick.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Preencha seu nick");
            } else {
                //DEFINO O IP DO GRUPO
                grupo = InetAddress.getByName(jTextField_GrupoIP.getText());
                
                //CRIO O SOCKET MULTICAST COM A PORTA ESPECIFICADA
                socket = new MulticastSocket(Integer.parseInt(jTextField_Porta.getText()));
                
                //ENTRA     NO GRUPO MULTICAST PARA RECEBER AS MENSAGENS
                socket.joinGroup(grupo);
                
                
                int portaPrivada = Integer.parseInt(jTextField_Porta.getText()) + 1;
                socketUnicast = new DatagramSocket(portaPrivada);
                
                
                //CRIO A THREAD PARA RECEBER AS MENSAGENS
                ThreadReceptora tR = new ThreadReceptora();
                tR.start();
                
                ThreadReceptoraUnicast tUnicast = new ThreadReceptoraUnicast();
                tUnicast.start();
                
                JOptionPane.showMessageDialog(this, "Conectado com sucesso!");
                jButton_Conectar.setEnabled(false);
                jTextField_Nick.setEnabled(false);
                jTextField_GrupoIP.setEnabled(false);
                jTextField_Porta.setEnabled(false);

                String msg = jTextField_Nick.getText() + " entrou na sala";
                DatagramPacket pacote = ComunicadorUDP.montaMensagem(msg, jTextField_GrupoIP.getText(), Integer.parseInt(jTextField_Porta.getText()));
                socket.send(pacote);
                jTextField_textoDeEnvio.setText("");
                jTextField_textoDeEnvio.requestFocus();
            }
        } catch (HeadlessException | IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jButton_ConectarActionPerformed
   
    /**
     * método privado que captura a mensagem escrita na caixa de texto mais o apelido da caixa de texto nick
     * monta a mensagem e a envia ao grupo
     * @throws IOException
     * @throws NumberFormatException
     * @throws NullPointerException 
     */
    private void enviarMsg() throws IOException, NumberFormatException, NullPointerException {
        try {
            String horaAtual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String msg = "[" + horaAtual + "] " + jTextField_Nick.getText() + ": " + jTextField_textoDeEnvio.getText();
            
            String selecionado = jList_usuariosOnline.getSelectedValue();
            
            int porta = Integer.parseInt(jTextField_Porta.getText());
            
            if (selecionado == null || selecionado.equals("Todos")){
                DatagramPacket pacote = ComunicadorUDP.montaMensagem(msg, jTextField_GrupoIP.getText(), porta);
                socket.send(pacote);
            }else{
                InetAddress ipDestino = usuariosOnline.get(selecionado);
                if (ipDestino != null) {
            int portaPrivadaDestino = Integer.parseInt(jTextField_Porta.getText()) + 1;
            
            
            String msgParaRede = "[" + horaAtual + "] [PRIVADO] " + jTextField_Nick.getText() + " diz: " + jTextField_textoDeEnvio.getText();
            DatagramPacket pacotePrivado = new DatagramPacket(msgParaRede.getBytes(), msgParaRede.length(), ipDestino, portaPrivadaDestino);
            socketUnicast.send(pacotePrivado); 
            
    
            String msgMinhaTela = "[" + horaAtual + "] [PRIVADO para " + selecionado + "] eu: " + jTextField_textoDeEnvio.getText();
            jTextArea_Mensagens.append(msgMinhaTela + "\n");
            
      
            lista.add(msgMinhaTela);
            salvarHistorico(msgMinhaTela);
            
        } else {
            JOptionPane.showMessageDialog(this, "Erro: IP do usuário não encontrado.");
        }
            }
        } catch (IOException | NumberFormatException | NullPointerException e) {
            if (e.getClass().toString().equals("class java.lang.NullPointerException")) {
                JOptionPane.showMessageDialog(this, "Você precisa conectar antes de enviar msgs");
            } else {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    /**
     * método privado que trata o envio de mensagens escritas na caixa de texto de mensagens 
     * @param evt contém o evento recebido pelo tratador
     */
    private void jButton_EnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EnviarActionPerformed
        try {
            this.enviarMsg();
            jTextField_textoDeEnvio.setText("");
            jTextField_textoDeEnvio.requestFocus();
        } catch (IOException | NumberFormatException | NullPointerException e) {

        }
    }//GEN-LAST:event_jButton_EnviarActionPerformed

    /**
     * método privado que trata o evento do botão sair, ou seja, finaliza o sistema
     * @param evt contém o evento recebido pelo tratador
     */
    private void jButton_SairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SairActionPerformed
        try {
            this.sairDoSistema();
        } catch (IOException | NumberFormatException | NullPointerException e) {

        }
    }//GEN-LAST:event_jButton_SairActionPerformed

    /**
     * método privado que trata o pressionamento das teclas Enter ou Esc quando o foco estiver na caixa de envio de texto
     * @param evt contém o evento recebido pelo tratador
     */
    private void jTextField_textoDeEnvioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_textoDeEnvioKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                this.enviarMsg();
            } catch (IOException | NumberFormatException | NullPointerException e) {

            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            try {
                this.sairDoSistema();
            } catch (IOException | NumberFormatException | NullPointerException e) {

            }
        }
    }//GEN-LAST:event_jTextField_textoDeEnvioKeyPressed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrame_chatMulticast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame_chatMulticast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame_chatMulticast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame_chatMulticast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrame_chatMulticast().setVisible(true);
            }
        });
    }

    InetAddress grupo;
    MulticastSocket socket;
    LinkedList<String> lista = new LinkedList<>();
    DatagramSocket socketUnicast;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Conectar;
    private javax.swing.JButton jButton_Enviar;
    private javax.swing.JButton jButton_Sair;
    private javax.swing.JLabel jLabel_Nick;
    private javax.swing.JLabel jLabel_Porta;
    private javax.swing.JLabel jLabel_ServidorIP;
    private javax.swing.JList<String> jList_usuariosOnline;
    private javax.swing.JPanel jPanel_Chat;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea_Mensagens;
    private javax.swing.JTextField jTextField_GrupoIP;
    private javax.swing.JTextField jTextField_Nick;
    private javax.swing.JTextField jTextField_Porta;
    private javax.swing.JTextField jTextField_textoDeEnvio;
    // End of variables declaration//GEN-END:variables
}
