/*
 * Painel Projetos
 * ---------------------------------------
 * 
 * - Esta classe representa a visao de dentro do programa
 * - Eh uma extensao de JPanel.
 * - Mostra todos projetos.
 * 
 */
package view.panel;

import Model.Departamento;
import Model.Projeto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import view.Principal;
import view.ViewObjectPool;
import view.formularios.FormProjetos;
import view.formularios.FormProjetosFuncionarios;
import view.formularios.FormProjetosPropagandas;

public final class PainelProjetos extends JPanel  implements ActionListener {	
    
    private static final long serialVersionUID = 1L;
    private JButton novo = new JButton("Novo"); 
    private JButton editar = new JButton("Editar");
    private JButton excluir = new JButton("Excluir");
    private JButton empregados = new JButton("Empregados");
    private JButton balanco =  new JButton("Balanco Financeiro");
    private JButton publicidade =  new JButton("Publicidade");
    private JTextField txtBusca  = new JTextField();
    private JButton btnBusca  = new JButton("Pesquisar");
    private JComboBox<String> comboBusca = new JComboBox<String>();
    private JLabel contaRegistros;
    
    public static JTable tabela;
    public static DefaultTableModel modelo;
    public static String[] colunas;
    
    private static FormProjetos formprojetos = null;
    private static FormProjetosFuncionarios formFuncionarios = null; 
    private static FormProjetosPropagandas formPropagandas = null;
    
    public PainelProjetos(){			
        
        tabela = new JTable(){
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int rowIndex, int vColIndex){
                    return false;
            }
        };

        colunas = new String [] {"Nome Projeto", "Numero", "Localizacao Projeto", "Nome Departamento", "Dno"}; 
  
        PainelProjetos.setDataTable();
        
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setGridColor(new Color(220,220,220));        
        PainelProjetos.setSizeColumn();
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel botoes = new JPanel();
        botoes.setLayout(new BoxLayout(botoes, BoxLayout.X_AXIS));
        botoes.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(64, 64, 64)));

        JLabel imagem = new JLabel();
        imagem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/busca.png")));

        txtBusca.setPreferredSize(new Dimension(200, 24));
        txtBusca.setMaximumSize(new Dimension(200, 24));
        
        comboBusca.addItem("Nome");
        comboBusca.addItem("Numero");
        comboBusca.addItem("Todos Projetos");
        comboBusca.setPreferredSize(new Dimension(100, 24));
        comboBusca.setMaximumSize(new Dimension(100, 24));
        
        contaRegistros = new JLabel();
        contaRegistros.setText(tabela.getRowCount() + " registro(s) encontrado(s)");

        botoes = nivelView(botoes, imagem);
        
        novo.addActionListener(this);
        editar.addActionListener(this);
        btnBusca.addActionListener(this);
        excluir.addActionListener(this);        
        empregados.addActionListener(this);
        publicidade.addActionListener(this);
        balanco.addActionListener(this);
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
        this.add(botoes, BorderLayout.SOUTH);
        this.add(scrollPane, BorderLayout.CENTER);		
    }

    public JPanel nivelView(JPanel botoes, JLabel imagem)
    {        
        if(Principal.user.getTipoLogin() == 1){          
            botoes.add(Box.createHorizontalStrut(5));
            botoes.add(novo);
            botoes.add(Box.createHorizontalStrut(3));
            botoes.add(editar);
            botoes.add(Box.createHorizontalStrut(3));
            botoes.add(excluir);
            botoes.add(Box.createHorizontalStrut(3));
            botoes.add(empregados);    
            botoes.add(Box.createHorizontalStrut(3));
            botoes.add(publicidade);     
            botoes.add(Box.createHorizontalStrut(3));
            botoes.add(balanco);              
            botoes.add(Box.createHorizontalGlue());
            botoes.add(contaRegistros);
            botoes.add(Box.createHorizontalGlue());            
        }            
        else if(Principal.user.getTipoLogin() == 0){                                     
            botoes.add(Box.createHorizontalGlue());          
            botoes.add(Box.createHorizontalGlue());
            botoes.add(contaRegistros);      ;            
            botoes.add(Box.createHorizontalGlue());
        }
        botoes.add(Box.createVerticalStrut(45));  
        if(Principal.user.getTipoLogin() == 2){                     
            botoes.add(imagem);
            botoes.add(Box.createHorizontalStrut(5));
            botoes.add(txtBusca);
            botoes.add(Box.createHorizontalStrut(5));
            botoes.add(comboBusca);
            botoes.add(Box.createHorizontalStrut(5));
            botoes.add(btnBusca);
            botoes.add(Box.createHorizontalStrut(7));            
        }
        
        return botoes;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object origem = e.getSource();	
        int item = tabela.getSelectedRow();
        
        if (origem == novo)
            formProjeto(null);
        else if (origem == editar && (item != -1)){           
            String id = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Numero"));                
            
            try {
                Projeto p = Principal.cf.getProjetoByNumero(Integer.parseInt(id));
                formProjeto(p);        
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex, "Atenção", JOptionPane.ERROR_MESSAGE);
            }
               
        } else if (origem == excluir  && (item != -1)) {
            String numero = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Numero"));
            String nome = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Nome Projeto"));
            
            int opcao = JOptionPane.showConfirmDialog(this,"Deseja remover projeto "+nome+"?","Atenção!",JOptionPane.YES_NO_OPTION);    
            
            if(opcao == JOptionPane.YES_OPTION) {
                try {
                    Principal.cf.apagarProjeto(Integer.parseInt(numero));
                    modelo.removeRow(item);                    
                    FormProjetos.updatePool();
                    contaRegistros.setText(tabela.getRowCount() + " registro(s) encontrado(s)");                          
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(this,ex, "Atenção", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else if (origem == empregados && (item != -1)){    
                formFuncinarios(dadosLista(item));
        } else if (origem == btnBusca){
            String[][] dados = null;

            try {                   
                if(comboBusca.getSelectedIndex() == 0 && !txtBusca.getText().equals(""))//busca nome
                    dados = Principal.cf.getProjetosTable(Principal.cf.buscaProjetosByNome(txtBusca.getText()));
                else if(comboBusca.getSelectedIndex() == 1 && !txtBusca.getText().equals(""))//busca numero
                    dados = Principal.cf.getProjetosTable(Principal.cf.getProjetoByNumeroVector(Integer.parseInt(txtBusca.getText())));
                else
                    dados = Principal.cf.getProjetosTable(Principal.cf.listarProjetos());
                txtBusca.setText("");
            } catch (Exception ex) {
                System.err.println("Erro Painel Projetos: " + ex);
            }

            PainelProjetos.modelo = new DefaultTableModel(dados, PainelProjetos.colunas);
            PainelProjetos.tabela.setModel(PainelProjetos.modelo);                    
            PainelProjetos.setSizeColumn();     
            contaRegistros.setText(tabela.getRowCount() + " registro(s) encontrado(s)");                    

        } else if (origem == publicidade  && (item != -1)){    
                formPropagandas(dadosLista(item));
        } else if (origem == balanco  && (item != -1)){  
           
            try {
                String numero = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Numero"));

                double receitas = 0.0;
                double despesas = 0.0;
                double total = 0.0;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);

                receitas = Principal.cf.receita(Integer.valueOf(numero));     
                despesas  = Principal.cf.despesa(Integer.valueOf(numero));     
                total = receitas - despesas;
                JOptionPane.showMessageDialog(null,"Receitas: R$ "+ df.format(receitas) +"\nDespesas: R$ "+ df.format(despesas) + "\nTotal: R$ " + df.format(total) , "Balanco Geral", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Erro Balanco Sql: " + ex, "Atenção", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,"Erro Balanco Exception: " + ex, "Atenção", JOptionPane.ERROR_MESSAGE);
            }
        }
    }   
    
    //em teste
    public Projeto getProjeto(String id){
        Projeto p = null;
        
        try {
            p = Principal.cf.getProjetoByNumero(Integer.parseInt(id));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex, "Atenção", JOptionPane.ERROR_MESSAGE);
        }
        return p;
    }
    
    public static void setSizeColumn(){
        tabela.getTableHeader().getColumnModel().getColumn(0).setMinWidth(350);
        tabela.getTableHeader().getColumnModel().getColumn(1).setMinWidth(80);
        tabela.getTableHeader().getColumnModel().getColumn(2).setMinWidth(250);
        tabela.getTableHeader().getColumnModel().getColumn(3).setMinWidth(250);
        tabela.getTableHeader().getColumnModel().getColumn(4).setMinWidth(80);
    }
    
    public static void setDataTable(){       
        String[][] dados = null;

        try {        
            Vector<Projeto> values = (Vector<Projeto>) ViewObjectPool.get("todosProj");
            dados = Principal.cf.getProjetosTable(values);
        } catch (Exception ex) {
            System.err.println("Erro Painel Projetos: " + ex);
        }
        
        PainelProjetos.modelo = new DefaultTableModel(dados, PainelProjetos.colunas);
        PainelProjetos.tabela.setModel(PainelProjetos.modelo);                    
        PainelProjetos.setSizeColumn();        
    }
    
    
    public static void formProjeto(Projeto p){
        if(formprojetos == null)
            formprojetos = new FormProjetos(p);
        else{
            try {
                formprojetos.editar(p);
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(null,"Erro Projeto: " + ex, "Atenção", JOptionPane.ERROR_MESSAGE);                                                                        
            }
            formprojetos.setVisible(true);
        }
    }       
    
    public static void formFuncinarios(Projeto p){
        if(formFuncionarios == null)
            formFuncionarios = new FormProjetosFuncionarios(p);
        else{
            try {
                formFuncionarios.editar(p);
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(null,"Erro Empregado: " + ex, "Atenção", JOptionPane.ERROR_MESSAGE);                                                                        
            }
            formFuncionarios.setVisible(true);
        }
    } 
    
    public static void formPropagandas(Projeto p){
        if(formPropagandas == null)
            formPropagandas = new FormProjetosPropagandas(p);
        else{
            try {
                formPropagandas.editar(p);
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(null,"Erro Propagandas: " + ex, "Atenção", JOptionPane.ERROR_MESSAGE);                                                                        
            }
            formPropagandas.setVisible(true);
        }
    }    
    
    public Projeto dadosLista(int item){
        String numero = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Numero"));
        String nome = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Nome Projeto"));
        String local = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Localizacao Projeto"));
        String dep = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Nome Departamento"));
        String depNum = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Dno"));

        Projeto p = new Projeto();
        p.setNumero(Integer.parseInt(numero));
        p.setNome(nome);
        p.setLocalizacao(local);
        
        Departamento d = new Departamento();
        d.setNome(dep);
        d.setNumero(Integer.parseInt(depNum));       
        
        d.setGerenteSsn(null);
        p.setDepartamento(d);        
        
        return p;
    }
}
