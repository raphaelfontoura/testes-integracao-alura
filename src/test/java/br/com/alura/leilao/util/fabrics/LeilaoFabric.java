package br.com.alura.leilao.util.fabrics;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.builders.LeilaoBuilder;

import java.time.LocalDate;

public class LeilaoFabric {
    public static Leilao leilaoDataAtual(Usuario usuario) {
        return new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("70")
                .comUsuario(usuario)
                .comData(LocalDate.now())
                .criar();
    }
}
