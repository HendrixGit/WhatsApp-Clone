package whatsapp.cursoandroid.com.whatsapp.model;

import java.util.Date;

public class Conversa {
    private String idUsuario;
    private String nome;
    private String mensagem;
    private Date data;

    public Conversa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getData() { return data; }

    public void setData(Date data) {
        this.data = data;
    }
}