package br.com.alura.leilao.util.fabrics;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.builders.UsuarioBuilder;

public class UsuarioFabric {
    public static Usuario novoUsuario() {
        return new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email.com")
                .comSenha("1234")
                .criar();
    }
}
