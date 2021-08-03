package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Lance;
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
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LanceDaoTest {

    private LanceDao dao;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        em = JPAUtil.getEntityManager();
        dao = new LanceDao(em);
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        em.getTransaction().rollback();
    }

    @Test
    void buscarMaiorLanceDoLeilao_deveriaRetornarLeilaoComMaiorLance() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        Leilao leilao = LeilaoFabric.leilaoDataAtual(usuario);
        em.persist(leilao);

        Lance lanceMenor = new Lance(usuario, new BigDecimal("60"));
        lanceMenor.setLeilao(leilao);
        em.persist(lanceMenor);
        Lance lanceMaior = new Lance(usuario, new BigDecimal("100"));
        lanceMaior.setLeilao(leilao);
        em.persist(lanceMaior);

        Lance lance = dao.buscarMaiorLanceDoLeilao(leilao);

        assertNotNull(lance);
        assertEquals(lanceMaior.getValor(), lance.getValor());
    }
}