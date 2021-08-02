package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
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
        Usuario usuario = criarUsuario();

        Usuario resultado = dao.buscarPorUsername("fulano");
        assertNotNull(resultado);
        assertEquals(usuario, resultado);
    }

    @Test
    void buscarPorUsername_DeveriaRetornarNoResultException_QuandoUsuarioNaoCadastrado() {
        criarUsuario();

        assertThrows(NoResultException.class, () -> dao.buscarPorUsername("beltrano"));
    }

    @Test
    void deletar_deveriaExcluirUmUsuario() {
        Usuario usuario = criarUsuario();

        dao.deletar(usuario);

        assertThrows(NoResultException.class, () -> dao.buscarPorUsername(usuario.getNome()));
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario("fulano", "fulano@email.com", "1234");
        em.persist(usuario);
        return usuario;
    }
}