package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builders.LeilaoBuilder;
import br.com.alura.leilao.util.builders.UsuarioBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LeilaoDaoTest {

    private LeilaoDao dao;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        this.em = JPAUtil.getEntityManager();
        this.dao = new LeilaoDao(em);
        this.em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        this.em.getTransaction().rollback();
    }

    @Test
    void salvar_deveriaCadastrarLeilao() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email.com")
                .comSenha("1234")
                .criar();
        em.persist(usuario);
        Leilao leilao = new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("70")
                .comUsuario(usuario)
                .comData(LocalDate.now())
                .criar();

        leilao = dao.salvar(leilao);

        Leilao salvo = dao.buscarPorId(leilao.getId());

        assertNotNull(salvo);
    }

    @Test
    void salvar_deveriaAtualizarLeilaoAlterado() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email.com")
                .comSenha("1234")
                .criar();
        em.persist(usuario);
        Leilao leilao = new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("70")
                .comUsuario(usuario)
                .comData(LocalDate.now())
                .criar();

        leilao.setNome("celular");
        leilao.setValorInicial(new BigDecimal("400"));
        leilao = dao.salvar(leilao);

        Leilao salvo = dao.buscarPorId(leilao.getId());

        assertEquals("celular", leilao.getNome());
        assertEquals(new BigDecimal("400"), leilao.getValorInicial());
    }

}