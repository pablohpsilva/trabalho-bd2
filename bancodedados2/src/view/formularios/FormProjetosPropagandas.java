package view.formularios;

import Model.Empregado;
import Model.Projeto;
import Model.Trabalha;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import view.Principal;

public class FormProjetosPropagandas extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 1L;    
    
    private JButton novo  = new JButton("Novo");
    private JButton editarHora  = new JButton("Editar");
    private JButton excluir = new JButton("Excluir");   
    private JLabel nomeProjeto = new JLabel();
    private JLabel localProjeto = new JLabel();
    private JLabel depProjeto = new JLabel();

    private JButton btnOK = new JButton("OK");
    
    private static JTable tabela;
    private static DefaultTableModel modelo;
    private static String[] colunas;
    private static FormProjetosFuncionariosEditPro editFormProj = null;
    private static Projeto proj;    
    
    public FormProjetosPropagandas(Projeto p){
        super(Principal.janela,"Todas Propagandas do Projeto", true);
                               
        btnOK.setPreferredSize(new Dimension(100, 25));
        proj = p;
        
        btnOK.addActionListener(this);
        editarHora.addActionListener(this);

        JLabel nome = new JLabel("Nome: ");                 
        JPanel grid = new JPanel();
        grid.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        grid.setLayout(new GridLayout(3, 2, 5, 5));
        grid.add(nome);
        grid.add(nomeProjeto);      
        grid.add(new JLabel("Localizacao Projeto: "));
        grid.add(localProjeto);        
        grid.add(new JLabel("Departamento: "));
        grid.add(depProjeto);
        
        nome.setPreferredSize(new Dimension(250, 25));
        
        JPanel botoes = new JPanel();
        botoes.setLayout(new BoxLayout(botoes, BoxLayout.X_AXIS));
        botoes.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,Color.LIGHT_GRAY));                

        botoes.add(Box.createHorizontalStrut(5));
        botoes.add(novo);
        botoes.add(Box.createHorizontalStrut(3));
        botoes.add(editarHora);
        botoes.add(Box.createHorizontalStrut(3));
        botoes.add(excluir);
        botoes.add(Box.createVerticalStrut(45));
        botoes.add(Box.createHorizontalStrut(3));
        botoes.add(btnOK);       
        botoes.add(Box.createHorizontalStrut(5));
        
        novo.addActionListener(this);
                
        tabela = new JTable(){
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int rowIndex, int vColIndex){
                    return false;
            }
        };

        colunas = new String [] { "Nome", "Ssn", "Sexo", "Endereco", "Salario", "Data de Nascimento",
                                           "Departamento", "Dno", "Supervisor", "SuperSnn"};  
        
        this.setDataTableFuncionariosProjetos();
        
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setGridColor(new Color(220,220,220));        
        
        this.setSizeColumnFuncionariosProjetos();
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());        
        
        editar(proj);
        
        JPanel painel = new JPanel();
        painel.add(grid);              
        
        this.add(painel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);        
        this.add(botoes, BorderLayout.SOUTH);
        this.setSize(750, 610);
        this.setLocation((java.awt.Toolkit.getDefaultToolkit()
                                        .getScreenSize().width / 2)
                                        - (this.getWidth() / 2), (java.awt.Toolkit
                                        .getDefaultToolkit().getScreenSize().height / 2)
                                        - (this.getHeight() / 2));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
 
    public void editar(Projeto p){
        proj = p;
        
        nomeProjeto.setText(p.getNome());
        localProjeto.setText(p.getLocalizacao());
        depProjeto.setText(p.getDepartamento().getNome());
        
        FormProjetosPropagandas.setDataTableFuncionariosProjetos();
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object origem = e.getSource();
        int item = tabela.getSelectedRow();
        
        if(origem == novo){
            
            Trabalha t = new Trabalha();
            t.setProjeto(proj);
            t.setEssn(null);
            
            try {
                FormFuncionarioProjetosForm(t);
            } catch (Exception ex) {
                System.err.println("Erro: " + ex);
            }
            
         } else if(origem == editarHora && (item != -1)){
            String ssn = (String) tabela.getValueAt(item, tabela.getColumnModel().getColumnIndex("Ssn"));
            
            Trabalha t = null;
            Empregado em;
           
            try {
                t = Principal.cf.buscarEmpregadoProjeto(ssn, proj.getNumero());
                //FormFuncionarioProjetosForm(t);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex, "Atenção", JOptionPane.ERROR_MESSAGE);
            }                 
            
         }else if(origem == btnOK)
            this.dispose();   
    }

    public static void setSizeColumnFuncionariosProjetos(){
        tabela.getTableHeader().getColumnModel().getColumn(0).setMinWidth(250);
        tabela.getTableHeader().getColumnModel().getColumn(1).setMinWidth(35);
        tabela.getTableHeader().getColumnModel().getColumn(2).setMinWidth(100);
        tabela.getTableHeader().getColumnModel().getColumn(3).setMinWidth(250);
        tabela.getTableHeader().getColumnModel().getColumn(4).setMinWidth(30);
        tabela.getTableHeader().getColumnModel().getColumn(5).setMinWidth(100);
        tabela.getTableHeader().getColumnModel().getColumn(6).setMinWidth(250);
        tabela.getTableHeader().getColumnModel().getColumn(7).setMinWidth(35);
        tabela.getTableHeader().getColumnModel().getColumn(8).setMinWidth(250);
        tabela.getTableHeader().getColumnModel().getColumn(9).setMinWidth(35);         
    }
    
    public static void setDataTableFuncionariosProjetos(){
        String[][] dados = null;
        
        dados = Principal.cf.getEmpregadosTable(Principal.cf.buscarEmpregadoProjeto(proj.getNumero()));        
        
        FormProjetosPropagandas.modelo = new DefaultTableModel(dados, FormProjetosPropagandas.colunas);
        FormProjetosPropagandas.tabela.setModel(FormProjetosPropagandas.modelo);                    
        FormProjetosPropagandas.setSizeColumnFuncionariosProjetos();        
    }
    
    public static FormProjetosFuncionariosEditPro FormFuncionarioProjetosForm(Trabalha e) throws Exception{
        if(editFormProj == null)
            editFormProj = new FormProjetosFuncionariosEditPro(e);
        else
           editFormProj.editar(e);        
        
        return editFormProj;
    }     
}