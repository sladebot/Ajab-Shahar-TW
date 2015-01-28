package org.ajabshahar.core;


import org.ajabshahar.platform.daos.ReflectionDAO;
import org.ajabshahar.platform.models.Reflection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReflectionsTest {

    private Reflections reflections;
    private List<Reflection> reflectionList;

    @Mock
    private ReflectionDAO reflectionRepository;

    @Before
    public void setUp() {
        reflections = new Reflections(reflectionRepository);
        reflectionList = new ArrayList<>();
    }

    @Test
    public void shouldCreateReflection() throws Exception {
        Reflection reflection = new Reflection();
        when(reflectionRepository.create(reflection)).thenReturn(reflection);

        Reflection actual = reflections.create(reflection);

        assertEquals(reflection, actual);
    }

    @Test
    public void shouldGetReflections() throws Exception {
        when(reflectionRepository.findAll()).thenReturn(reflectionList);
        List<Reflection> actual = reflections.getAll();

        assertEquals(reflectionList, actual);
    }
}