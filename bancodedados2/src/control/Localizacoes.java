/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import DAO.FactoryDAO;
import DAO.IObjectDAO;
import DAO.LocalizacaoDAO;
import Model.Departamento;
import Model.Localizacao;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author yuricampos
 */
public class Localizacoes {
    
    private LocalizacaoDAO dao;
    
    public Localizacoes(){
        this.dao = (LocalizacaoDAO) FactoryDAO.getFactory("Localizacao");
    }
    
    private Localizacao createObject(int dnumero, String nome) throws Exception{
        Departamento departamento = (Departamento) FactoryDAO.getFactory("Departamento").read(dnumero);
        Localizacao localizacao = new Localizacao();
        localizacao.setDepartamento(departamento);
        localizacao.setNome(nome);
        return localizacao;
    }
    
    public void post(int dnumero, String nome) throws Exception {
        FuncoesControle f = new FuncoesControle();
        
        if(f.verificarExistenciaDepartamento(dnumero) == false)
            throw new Exception("Erro: departamento informado nao foi encontrado");
        else
            this.dao.post(this.createObject(dnumero, nome));
        
    }

    public void update(int dnumero, String nome) throws Exception {
        FuncoesControle f = new FuncoesControle();
        
        if(f.verificarExistenciaDepartamento(dnumero) == false)
           throw new Exception("Erro: departamento informado nao foi encontrado"); 
        else
            this.dao.post(this.createObject(dnumero, nome));
    }
    
    public Vector<Localizacao>  getAll() throws Exception {
        Vector<Localizacao> localizacao = new Vector<Localizacao>();
        
        for(Object aux : this.dao.getAll())
            localizacao.add((Localizacao) aux);
        
        return localizacao;
    } 
    
        public Vector<Localizacao>  getAllByDep(int depid) throws Exception {
        Vector<Localizacao> localizacao = new Vector<Localizacao>();
        
        for(Object aux : this.dao.getAllByDept(depid))
            localizacao.add((Localizacao) aux);
        
        return localizacao;
    } 
    
    public Vector<Localizacao>  SearchByName(String input){
        Vector<Localizacao> localizacao = new Vector<Localizacao>();
        
        for(Object aux : (ArrayList<Object>) this.dao.read(input))
            localizacao.add((Localizacao) aux);
        
        return localizacao;
    } 
    
    public void delete(String localizacao) throws Exception{
        this.dao.delete(localizacao);
    }
    
}
