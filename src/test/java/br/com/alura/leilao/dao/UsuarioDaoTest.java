package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.fabrics.UsuarioFabric;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDaoTest {

    private UsuarioDao dao;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        this.em = JPAUtil.getEntityManager();
        this.dao = new UsuarioDao(em);
        this.em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        this.em.getTransaction().rollback();
    }

    @Test
    void buscarPorUsername_deveriaRetornarUsuario_quandoUsuarioCadastrado() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);
        Usuario resultado = dao.buscarPorUsername(usuario.getNome());
        assertNotNull(resultado);
        assertEquals(usuario, resultado);
    }

    @Test
    void buscarPorUsername_DeveriaRetornarNoResultException_QuandoUsuarioNaoCadastrado() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);

        assertThrows(NoResultException.class, () -> dao.buscarPorUsername("beltrano"));
    }

    @Test
    void deletar_deveriaExcluirUmUsuario() {
        Usuario usuario = UsuarioFabric.novoUsuario();
        em.persist(usuario);

        dao.deletar(usuario);

        assertThrows(NoResultException.class, () -> dao.buscarPorUsername(usuario.getNome()));
    }

}