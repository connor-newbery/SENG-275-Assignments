package lab06;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class InvoiceFilterTest {
    private InvoiceFilter invoiceFilter;
    private IssuedInvoices issuedInvoices;

    @Test
    void allHighValueInvoices() {
        IssuedInvoices issuedInvoices = mock(IssuedInvoices.class);
        invoiceFilter = new InvoiceFilter(issuedInvoices);

        when(issuedInvoices.all()).thenReturn(List.of(new Invoice(100), new Invoice(200)));

        assertThat(invoiceFilter.lowValueInvoices()).containsExactly();

        verify(issuedInvoices, times(1)).all();
        verifyNoMoreInteractions(issuedInvoices);
    }

    @Test
    void allLowValueInvoices() {
        IssuedInvoices issuedInvoicesMock = mock(IssuedInvoices.class);
        invoiceFilter = new InvoiceFilter(issuedInvoicesMock);

        when(issuedInvoicesMock.all()).thenReturn(List.of(new Invoice(43), new Invoice(99)));

        assertThat(invoiceFilter.lowValueInvoices()).containsExactly(new Invoice(43), new Invoice(99));

        verify(issuedInvoicesMock, times(1)).all();
        verifyNoMoreInteractions(issuedInvoicesMock);
    }

    @Test
    void someLowValueInvoices() {
        IssuedInvoices issuedInvoicesMock = mock(IssuedInvoices.class);
        invoiceFilter = new InvoiceFilter(issuedInvoicesMock);

        when(issuedInvoicesMock.all()).thenReturn(List.of(new Invoice(49), new Invoice(200)));

        assertThat(invoiceFilter.lowValueInvoices()).containsExactly(new Invoice(49));

        verify(issuedInvoicesMock, times(1)).all();
        verifyNoMoreInteractions(issuedInvoicesMock);
    }

}
