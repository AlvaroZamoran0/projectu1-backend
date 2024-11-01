package com.example.demo.Services;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Repositories.CreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreditServiceTest {

    @Spy
    @InjectMocks
    private CreditService creditService;

    @Mock
    private CreditRepository creditRepository;

    private CreditEntity credit1;
    private CreditEntity credit2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        credit1 = new CreditEntity(1L, "12345678-9", 10, 1, 1000f, null, 100000000f, 4.5f, 20, 632652.8f, "First Home", 1, 1);
        credit2 = new CreditEntity(2L, "98765432-1", 5, 2, 2000f, null, 100000f, 4f, 15, 4000f, "Second Home", 1, 1);
    }

    @Test
    void testGetCredit() {
        Long id = 1L;
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        CreditEntity foundCredit = creditService.getCredit(id);

        assertEquals(credit1, foundCredit);
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testGetUserCredits() {
        String n_doc = "12345678-9";
        when(creditRepository.findByDoc(n_doc)).thenReturn(Arrays.asList(credit1));

        List<CreditEntity> credits = creditService.getUserCredits(n_doc);

        assertEquals(1, credits.size());
        assertEquals(credit1, credits.get(0));
        verify(creditRepository).findByDoc(n_doc);
    }

    @Test
    void testGetByApproved() {
        Integer approved = 2;
        when(creditRepository.findByApprovedIsLessThan(approved)).thenReturn(Arrays.asList(credit1, credit2));

        List<CreditEntity> credits = creditService.getByApproved(approved);

        assertEquals(2, credits.size());
        verify(creditRepository).findByApprovedIsLessThan(approved);
    }

    @Test
    void testSaveCredit() {
        String n_doc = "12345678-9";
        Integer years = 10;
        Integer type_work = 1;
        float salary = 1000f;
        float amount = 100000000f;
        float interest = 4.5f;
        int period = 20;
        Float expectedQuota = 632652.8f;

        // Aquí stubbeamos simulate_credit en el servicio
        when(creditService.simulate_credit(amount, interest, period)).thenReturn(expectedQuota);

        // Stub para creditRepository.save
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llamada al método que estamos probando
        CreditEntity savedCredit = creditService.saveCredit(n_doc, years, type_work, salary, amount, interest, period, "First Home");

        // Verificaciones
        assertNotNull(savedCredit);
        assertEquals(expectedQuota, savedCredit.getQuota());
        assertEquals(n_doc, savedCredit.getDoc());
        verify(creditRepository).save(any(CreditEntity.class));
    }

    @Test
    void testSimulateCredit() {
        float amount = 100000000f;
        float interest = 4.5f;
        int period = 20;
        Float expectedQuota = 632652.8f;

        Float quota = creditService.simulate_credit(amount, interest, period);

        assertNotNull(quota);
        assertEquals(expectedQuota, quota);
    }

    @Test
    void testStep1Credit() {
        Long id = 1L;
        credit1.setQuota(3000f);
        credit1.setSalary(1000f);
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        Float relation = creditService.step1_credit(id);

        assertEquals(300.0f, relation); // (3000 / 1000) * 100 = 300
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep3Credit() {
        Long id = 1L;
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        String result = creditService.step3_credit(id);

        assertEquals("Todo en orden", result);
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep3Credit_NoAplicaParaCredito() {
        // Arrange
        Long id = 1L;
        CreditEntity creditNoAplica = new CreditEntity(1L, "12345678-9", 0, 1, 1000f, null, 100000000f, 4.5f, 20, 632652.8f, "First Home", 1, 1);
        when(creditRepository.findByIdCredit(id)).thenReturn(creditNoAplica);

        // Act
        String result = creditService.step3_credit(id);

        // Assert
        assertEquals("No aplica para crédito", result);
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep3Credit_RevisarDocumentos_DiferenteTypeWork() {
        // Arrange
        Long id = 2L;
        when(creditRepository.findByIdCredit(id)).thenReturn(credit2); // credit2 tiene type_work 2

        // Act
        String result = creditService.step3_credit(id);

        // Assert
        assertEquals("Revisar documentos", result);
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep3Credit_RevisarDocumentos_NegativoYears() {
        // Arrange
        Long id = 1L;
        CreditEntity creditNegativo = new CreditEntity(1L, "12345678-9", -1, 2, 1000f, null, 100000000f, 4.5f, 20, 632652.8f, "First Home", 1, 1);
        when(creditRepository.findByIdCredit(id)).thenReturn(creditNegativo);

        // Act
        String result = creditService.step3_credit(id);

        // Assert
        assertEquals("Revisar documentos", result);
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep4Credit() {
        Long id = 1L;
        credit1.setSalary(1000f);
        credit1.setQuota(3000f);
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        Float relation = creditService.step4_credit(id, 2000);

        assertEquals(500.0f, relation); // (5000 / 1000) * 100 = 500
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit() {
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("First Home");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        String result = creditService.step5_credit(id, 40000f);

        assertTrue(result.contains("Se puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit_FirstHome_NoPuedeFinanciar() {
        // Arrange
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("First Home");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        // Act
        String result = creditService.step5_credit(id, 45000f); // 45000/50000 = 90%

        // Assert
        assertTrue(result.contains("No puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit_SecondHome_PuedeFinanciar() {
        // Arrange
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("Second Home");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        // Act
        String result = creditService.step5_credit(id, 35000f); // 35000/50000 = 70%

        // Assert
        assertTrue(result.contains("Se puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit_SecondHome_NoPuedeFinanciar() {
        // Arrange
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("Second Home");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        // Act
        String result = creditService.step5_credit(id, 40000f); // 40000/50000 = 80%

        // Assert
        assertTrue(result.contains("No puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit_CommercialProperties_PuedeFinanciar() {
        // Arrange
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("Commercial Properties");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        // Act
        String result = creditService.step5_credit(id, 29000f); // 30000/50000 = 60%

        // Assert
        assertTrue(result.contains("Se puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit_CommercialProperties_NoPuedeFinanciar() {
        // Arrange
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("Commercial Properties");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        // Act
        String result = creditService.step5_credit(id, 40000f); // 40000/50000 = 80%

        // Assert
        assertTrue(result.contains("No puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit_Remodelation_PuedeFinanciar() {
        // Arrange
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("Remodelation");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        // Act
        String result = creditService.step5_credit(id, 20000f); // 20000/50000 = 40%

        // Assert
        assertTrue(result.contains("Se puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep5Credit_Remodelation_NoPuedeFinanciar() {
        // Arrange
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setType_credit("Remodelation");
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);

        // Act
        String result = creditService.step5_credit(id, 30000f); // 30000/50000 = 60%

        // Assert
        assertTrue(result.contains("No puede financiar el proyecto"));
        verify(creditRepository).findByIdCredit(id);
    }

    @Test
    void testStep6Credit() {
        Integer age = 30;
        Integer period = 10;

        String result = creditService.step6_credit(age, period);

        assertEquals("30: Cumple con el requerimiento de edad", result);
    }

    @Test
    void testStep6Credit_NoCumpleRequerimiento_AgeMenorDe18() {
        // Arrange
        Integer age = 17;
        Integer period = 5;

        // Act
        String result = creditService.step6_credit(age, period);

        // Assert
        assertEquals("17: No cumple con el requerimiento de edad", result);
    }

    @Test
    void testStep6Credit_NoCumpleRequerimiento_EdadYPeriodosSuperan75() {
        // Arrange
        Integer age = 70;
        Integer period = 6; // 70 + 6 = 76

        // Act
        String result = creditService.step6_credit(age, period);

        // Assert
        assertEquals("70: No cumple con el requerimiento de edad", result);
    }

    @Test
    void testStep7Credit() {
        Integer ticks = 4;

        String result = creditService.step7_credit(ticks);

        assertEquals("Capacidad de ahorro moderada", result);
    }

    @Test
    void testStep7Credit_CapacidadAhorroSolida() {
        // Arrange
        Integer ticks = 5;

        // Act
        String result = creditService.step7_credit(ticks);

        // Assert
        assertEquals("Capacidad de ahorro sólida", result);
    }

    @Test
    void testStep7Credit_CapacidadAhorroModerada() {
        // Arrange
        Integer ticks = 4;

        // Act
        String result = creditService.step7_credit(ticks);

        // Assert
        assertEquals("Capacidad de ahorro moderada", result);
    }

    @Test
    void testStep7Credit_CapacidadAhorroInsuficiente() {
        // Arrange
        Integer ticks = 2;

        // Act
        String result = creditService.step7_credit(ticks);

        // Assert
        assertEquals("Capacidad de ahorro insuficiente", result);
    }

    @Test
    void testStep7Credit_CapacidadAhorroInsuficiente_Case0() {
        // Arrange
        Integer ticks = 0;

        // Act
        String result = creditService.step7_credit(ticks);

        // Assert
        assertEquals("Capacidad de ahorro insuficiente", result);
    }

    @Test
    void testStep7Credit_CapacidadAhorroModerada_Case3() {
        // Arrange
        Integer ticks = 3;

        // Act
        String result = creditService.step7_credit(ticks);

        // Assert
        assertEquals("Capacidad de ahorro moderada", result);
    }

    @Test
    void testNextStep() {
        Long id = 1L;
        credit1.setStatus(1);
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreditEntity updatedCredit = creditService.next_step(id);

        assertEquals(2, updatedCredit.getStatus());
        verify(creditRepository).save(any(CreditEntity.class));
    }

    @Test
    void testTracking() {
        Long id = 1L;
        credit1.setApproved(1);
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreditEntity updatedCredit = creditService.tracking(id, 1);

        assertEquals(2, updatedCredit.getApproved());
        verify(creditRepository).save(any(CreditEntity.class));
    }

    @Test
    void testTracking_Approved() {
        // Arrange
        Long id = 1L;
        credit1.setApproved(1);
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreditEntity updatedCredit = creditService.tracking(id, 1);

        // Assert
        assertEquals(2, updatedCredit.getApproved());
        verify(creditRepository).save(any(CreditEntity.class));
    }

    @Test
    void testTracking_Denied() {
        // Arrange
        Long id = 1L;
        credit1.setApproved(1);
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreditEntity updatedCredit = creditService.tracking(id, 2);

        // Assert
        assertEquals(5, updatedCredit.getApproved());
        verify(creditRepository).save(any(CreditEntity.class));
    }

    @Test
    void testTracking_Canceled() {
        // Arrange
        Long id = 1L;
        credit1.setApproved(1);
        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreditEntity updatedCredit = creditService.tracking(id, 3);

        // Assert
        assertEquals(6, updatedCredit.getApproved());
        verify(creditRepository).save(any(CreditEntity.class));
    }

    @Test
    void testTotalCost() {
        Long id = 1L;
        credit1.setAmount(50000f);
        credit1.setInterest(5f);
        int period = 20;

        when(creditRepository.findByIdCredit(id)).thenReturn(credit1);
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreditEntity updatedCredit = creditService.total_cost(id, 50000f, 5f, period);

        assertNotNull(updatedCredit);
        verify(creditRepository).save(any(CreditEntity.class));
    }
}
