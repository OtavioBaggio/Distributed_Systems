/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastro;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Otávio Baggio
 */
public class Pessoa implements Serializable{
    
    private String nome;
    private String dataNascimento;
    private String email;

    public Pessoa(String nome, String dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return  "nome = " + nome + ", Data de Nascimento = " + dataNascimento + ",\n E-mail = " + email + '}';
    }
    
    //duas pessoas são iguais se tiverem mesmo nome e mesma data
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pessoa other = (Pessoa) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return Objects.equals(this.dataNascimento, other.dataNascimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, dataNascimento);
    }
    
    
}
