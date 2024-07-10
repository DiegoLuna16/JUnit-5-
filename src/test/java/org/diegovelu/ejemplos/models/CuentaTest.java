package org.diegovelu.ejemplos.models;

import org.diegovelu.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void setUp(TestInfo testInfo,TestReporter testReporter) {
        this.cuenta = new Cuenta("Diego", new BigDecimal("1000.12345"));
        System.out.println("Iniciando el metodo.");
        System.out.println("Ejecutando " + testInfo.getDisplayName() + " " + Objects.requireNonNull(testInfo.getTestMethod().orElse(null)).getName()
                + "con las etiquetas " + testInfo.getTags());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo.");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println( "Iniciando el TEST.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el TEST.");
    }

    @Nested
    class CuentaNombreSaldoTest{
        @Test
        @DisplayName("probando nombre de la cuenta")
//    @Disabled
        void test_nombre_cuenta() {
//        c.setPersona("Diego");
            String expected = "Diego";
            String actual = cuenta.getPersona();
            assertEquals(expected, actual);

        }

        @Test
        void test_saldo_cuenta() {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        void test_referencia_cuenta() {
            Cuenta c = new Cuenta("Diego", new BigDecimal("1000.12345"));
            Cuenta c2 = new Cuenta("Diego", new BigDecimal("1000.12345"));
            assertEquals(c2, c);
        }
    }

    @Nested
    class OperacionesBancoTest{
        @Test
        void test_debito_cuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        void test_credito_cuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }
    }

    @Test
    void test_dinero_insuficiente_exception() {
        Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = ex.getMessage();
        String expected = "Dinero insuficiente";
        assertEquals(expected, actual);

    }

    @Test
    void test_transferir_dinero_cuenta() {
        Cuenta c1 = new Cuenta("Diego Luna", new BigDecimal("2500"));
        Cuenta c2 = new Cuenta("Paola Meza", new BigDecimal("1500.50"));
        Banco b = new Banco();
        b.addCuenta(c1);
        b.addCuenta(c2);
        b.setNombre("Santander");
        b.transferir(c1, c2, new BigDecimal(500));
        assertEquals("2000", c1.getSaldo().toPlainString());
        assertEquals("2000.50", c2.getSaldo().toPlainString());
        assertEquals(2, b.getCuentas().size());
        assertEquals("Santander", c1.getBanco().getNombre());

        assertEquals("Diego Luna", b.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Diego Luna"))
                .findFirst()
                .get().getPersona());

        assertTrue(b.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("Paola Meza")));
    }

    @Test
    void test_relacion_banco_cuenta() {
        Cuenta c1 = new Cuenta("Diego Luna", new BigDecimal("2500"));
        Cuenta c2 = new Cuenta("Paola Meza", new BigDecimal("1500.50"));
        Banco b = new Banco();
        b.addCuenta(c1);
        b.addCuenta(c2);
        b.setNombre("Santander");
        b.transferir(c1, c2, new BigDecimal(500));

        assertAll(
                () -> assertEquals("2000", c1.getSaldo().toPlainString()),
                () -> assertEquals("2000.50", c2.getSaldo().toPlainString(), () -> "El valor no es el esperado"),
                () -> assertEquals(2, b.getCuentas().size()),
                () -> assertEquals("Santander", c1.getBanco().getNombre()),
                () -> assertEquals("Diego Luna", b.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Diego Luna"))
                        .findFirst()
                        .get().getPersona()),
                () -> assertTrue(b.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("Paola Meza"))));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void test_windows() {
    }

    @Test
    @EnabledOnOs({OS.LINUX,OS.MAC})
    void test_linux_mac() {
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void test_no_windows() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void test_solo_jdk8() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void test_solo_jdk17() {
    }

    @Test
    void printSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach(( k,v ) -> System.out.println(k + ": " + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "18Z99LA")
    void test_user_name() {
    }

    @Test
    void imprimir_variables_ambiente() {
        Map<String, String> getenv = System.getenv();
        getenv.forEach(( k, v ) -> System.out.println(k + " : " + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named="USERDOMAIN", matches = "LAPTOP-LUNAGOD")
    void test_user_domain() {
    }

    @Test
    @DisplayName("11111 test saldo cuenta")
    void test_saldo_cuenta_dev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumingThat (esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        });
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }


    @RepeatedTest(value = 5, name = "Repeticion numero {currentRepetition} de {totalRepetitions}")
    @DisplayName("Repetir")
    void test_debito_cuenta_repetir(RepetitionInfo info) {
        if( info.getCurrentRepetition() == 3){
            System.out.println("Estamos en la repetiicion " + info.getCurrentRepetition());
        }
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("param")
    @Nested
    class PruebasParametrizadas{
        @ParameterizedTest
        @DisplayName("Parameterized - Value Source")
        @ValueSource(strings = {"100","200","300","400","700","1000"})
        void test_debito_cuenta1(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest
        @DisplayName("Parameterized - CsvSource")
        @CsvSource({"1,100","2,200","3,300","4,400","5,700","6,1000"})
        void test_debito_cuenta_csv_source(String index, String monto) {
            System.out.println(index + " - " + monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest
        @DisplayName("Parameterized - CsvSource - Saldo - Nombre")
        @CsvSource({"200,100,Diego,Diego","250,200,Maria,Maria","310,300,Luis,Luis","420,400,Paola,Paola"})
        void test_debito_cuenta_csv_source_2(String saldo, String monto, String expected, String actual) {
            System.out.println(saldo + " - " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);
            assertNotNull(cuenta.getPersona());
            assertNotNull(cuenta.getSaldo());
            assertEquals(expected,actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest
        @DisplayName("Parameterized - CsvFileSource")
        @CsvFileSource(resources = "/data.csv")
        void test_debito_cuenta_file_source(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }


    }

    @ParameterizedTest
    @DisplayName("Parameterized - MethodSource")
    @MethodSource("montoList")
    void test_debito_cuenta_method_source(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    private static List<String> montoList(){
        return Arrays.asList("100","200","300","400","700","1000");
    }

    @Tag("timeout")
    @Nested
    class TimeoutTest{

        @Test
        @Timeout(2)
        void test_timeout() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
        void test_timeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(1000);
        }

        @Test
        @Timeout(6)
        void test_timeout_Assertions() throws InterruptedException {
            assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.SECONDS.sleep(2);
            });
        }

    }


}