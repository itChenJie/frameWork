package org.basis.framework.test;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
}