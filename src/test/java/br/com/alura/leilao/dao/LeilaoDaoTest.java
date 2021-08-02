package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
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
        Leilao leilao = criarLeilao();

        leilao = dao.salvar(leilao);

        Leilao salvo = dao.buscarPorId(leilao.getId());

        assertNotNull(salvo);
    }

    @Test
    void salvar_deveriaAtualizarLeilaoAlterado() {
        Leilao leilao = criarLeilao();
        leilao = dao.salvar(leilao);

        leilao.setNome("celular");
        leilao.setValorInicial(new BigDecimal("400"));
        leilao = dao.salvar(leilao);

        Leilao salvo = dao.buscarPorId(leilao.getId());

        assertEquals("celular", leilao.getNome());
        assertEquals(new BigDecimal("400"), leilao.getValorInicial());
    }

    private Leilao criarLeilao() {
        Usuario usuario = criarUsuario();
        Leilao leilao = new Leilao("Mochila", new BigDecimal("70"), LocalDate.now(), usuario);
        return leilao;
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario("fulano", "fulano@email.com", "1234");
        em.persist(usuario);
        return usuario;
    }
}