package org.ajabshahar.core;

import org.ajabshahar.platform.daos.CoupletDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoupletsTest {

    public static final int ID = 1;
    @Mock
    private CoupletDAO coupletRepository;

    @Test
    public void shouldGetCoupletById() throws Exception {
        Couplets couplets = new Couplets(coupletRepository);
        List<org.ajabshahar.platform.models.Couplet> expectedResult = new ArrayList<>();
        when(coupletRepository.findBy(ID)).thenReturn(expectedResult);

        List<org.ajabshahar.platform.models.Couplet> actualResult = couplets.findBy(ID);

        assertEquals(expectedResult, actualResult);
    }
}