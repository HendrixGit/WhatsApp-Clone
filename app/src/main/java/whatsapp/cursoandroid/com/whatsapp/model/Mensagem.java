package whatsapp.cursoandroid.com.whatsapp.model;

import java.util.Date;

public class Mensagem {
    private String idUsuario;
    private String mensagem;
    private Date data;

    public Mensagem() {

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getData() { return data; }

    public void setData(Date data) {
        this.data = data;
    }
}