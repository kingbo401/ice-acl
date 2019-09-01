package com.kingbo401.acl;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kingbo401.iceacl.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Transactional
@Rollback
public class TestBase {

}