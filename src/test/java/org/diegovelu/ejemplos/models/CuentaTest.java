package org.diegovelu.ejemplos.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void test_nombre_cuenta() {
        Cuenta c = new Cuenta("Diego", new BigDecimal("1000.12345"));
//        c.setPersona("Diego");
        String expected = "Diego";
        String actual = c.getPersona();
        assertEquals(expected, actual);

    }

    @Test
    void test_saldo_cuenta() {
        Cuenta c = new Cuenta("Diego", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, c.getSaldo().doubleValue());
        assertFalse(c.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(c.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void test_referencia_cuenta(){
        Cuenta c = new Cuenta("Diego", new BigDecimal("1000.12345"));
        Cuenta c2 = new Cuenta("Diego", new BigDecimal("1000.12345"));

        assertEquals(c2, c);
    }

    @Test
    void test_debito_cuenta() {
        Cuenta c = new Cuenta("Diego", new BigDecimal("1000.12345"));
        c.debito(new BigDecimal(100));
        assertNotNull(c.getSaldo());
        assertEquals(900, c.getSaldo().intValue());
        assertEquals("900.12345",c.getSaldo().toPlainString());
    }

    @Test
    void test_credito_cuenta() {
        Cuenta c = new Cuenta("Diego", new BigDecimal("1000.12345"));
        c.credito(new BigDecimal(100));
        assertNotNull(c.getSaldo());
        assertEquals(1100, c.getSaldo().intValue());
        assertEquals("1100.12345",c.getSaldo().toPlainString());
    }

}