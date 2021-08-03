package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builders.LeilaoBuilder;
import br.com.alura.leilao.util.builders.UsuarioBuilder;
import br.com.alura.leilao.util.fabrics.LeilaoFabric;
import br.com.alura.leilao.util.fabrics.UsuarioFabric;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        Leilao leilao = LeilaoFabric.leilaoDataAtual(usuario);

        leilao = dao.salvar(leilao);

        Leilao salvo = dao.buscarPorId(leilao.getId());

        assertNotNull(salvo);
    }

    @Test
    void salvar_deveriaAtualizarLeilaoAlterado() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        Leilao leilao = LeilaoFabric.leilaoDataAtual(usuario);

        leilao.setNome("celular");
        leilao.setValorInicial(new BigDecimal("400"));
        leilao = dao.salvar(leilao);

        Leilao salvo = dao.buscarPorId(leilao.getId());

        assertEquals("celular", leilao.getNome());
        assertEquals(new BigDecimal("400"), leilao.getValorInicial());
    }

    @Test
    void buscarTodos_deveriaRetornarUmaListLeilao() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        Leilao leilao = LeilaoFabric.leilaoDataAtual(usuario);
        em.persist(leilao);
        Leilao leilao2 = new LeilaoBuilder()
                .comNome("Caderno")
                .comValorInicial("50")
                .comUsuario(usuario)
                .comData(LocalDate.now())
                .criar();
        em.persist(leilao2);

        List<Leilao> leilaos = dao.buscarTodos();

        assertEquals(2, leilaos.size());
        assertTrue(leilaos.contains(leilao));
        assertTrue(leilaos.contains(leilao2));
    }

    @Test
    void buscarTodos_deveriaRetornarUmaListVazia_quandoNaoExistirLeiloes() {

        List<Leilao> leilaos = dao.buscarTodos();

        assertTrue(leilaos.isEmpty());
    }

    @Test
    void buscarLeiloesDoPeriodo_deveriaRetornarApenasLeiloesDoPeriodoIformado() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        LocalDate primeiraData = LocalDate.of(2021, 1, 1);
        LocalDate segundaData = LocalDate.of(2021, 12, 1);
        Leilao leilao = new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("70")
                .comUsuario(usuario)
                .comData(primeiraData)
                .criar();
        em.persist(leilao);
        Leilao leilao2 = new LeilaoBuilder()
                .comNome("Caderno")
                .comValorInicial("50")
                .comUsuario(usuario)
                .comData(segundaData)
                .criar();
        em.persist(leilao2);

        List<Leilao> leilaos = dao.buscarLeiloesDoPeriodo(segundaData, LocalDate.of(2021, 12, 30));

        assertEquals(1, leilaos.size());
        assertTrue(leilaos.contains(leilao2));
    }

    @Test
    void buscarLeiloesDoPeriodo_deveriaRetornarListaVazia_QuandoNaoExistirLeilaoNoPeriodoIformado() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        LocalDate primeiraData = LocalDate.of(2021, 1, 1);
        LocalDate segundaData = LocalDate.of(2021, 12, 1);
        Leilao leilao = new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("70")
                .comUsuario(usuario)
                .comData(primeiraData)
                .criar();
        em.persist(leilao);
        Leilao leilao2 = new LeilaoBuilder()
                .comNome("Caderno")
                .comValorInicial("50")
                .comUsuario(usuario)
                .comData(segundaData)
                .criar();
        em.persist(leilao2);

        List<Leilao> leilaos = dao.buscarLeiloesDoPeriodo(
                LocalDate.of(2021,12,29),
                LocalDate.of(2021, 12, 30));

        assertTrue(leilaos.isEmpty());
    }

    @Test
    void buscarLeiloesDoUsuario_deveriaRetornarListaLeiloesDoUsuarioInformado() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        LocalDate primeiraData = LocalDate.of(2021, 1, 1);
        Leilao leilao = new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("70")
                .comUsuario(usuario)
                .comData(primeiraData)
                .criar();
        em.persist(leilao);


        List<Leilao> leilaos = dao.buscarLeiloesDoUsuario(usuario);

        assertEquals(1, leilaos.size());
        assertEquals(usuario, leilaos.get(0).getUsuario());
    }

    @Test
    void buscarLeiloesDoUsuario_deveriaRetornarListaVaziaQuandoUsuarioInformadoNaoTiverLeilao() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        Usuario usuarioSemLeilao = new UsuarioBuilder()
                .comNome("Cicrano")
                .comEmail("cricrano@email.com")
                .comSenha("1234")
                .criar();
        em.persist(usuarioSemLeilao);
        Leilao leilao = LeilaoFabric.leilaoDataAtual(usuario);
        em.persist(leilao);

        List<Leilao> leilaos = dao.buscarLeiloesDoUsuario(usuarioSemLeilao);

        assertTrue(leilaos.isEmpty());
    }
}