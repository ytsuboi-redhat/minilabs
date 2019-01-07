package com.redhat.labsjp.sample.todobackend.it;

import static org.junit.Assert.assertThat;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.redhat.labsjp.sample.todobackend.CsvDataSetLoader;
import com.redhat.labsjp.sample.todobackend.domain.Todo;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * On PC
 * $ docker run -e MYSQL_ROOT_PASSWORD=P@ssw0rd -e MYSQL_USER=todo -e MYSQL_PASSWORD=P@ssw0rd -e MYSQL_DATABASE=todo -p 3306:3306 --name=dev-mysql todo-mysql
 * $ mvn verify -P it
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class, databaseConnection = "mysqlDatabaseConnection")
@Transactional
public class TodoApiIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DatabaseSetup("classpath:dbunit/init-todo/")
    @ExpectedDatabase("classpath:dbunit/init-todo/")
    public void getTodos() {
        ResponseEntity<Todo[]> entity = restTemplate.getForEntity("http://localhost:" + port + "/todos", Todo[].class);

        assertThat(entity.getStatusCodeValue(), Matchers.is(200));
        Todo[] todos = entity.getBody();
        assertThat(todos.length, Matchers.is(2));
        assertThat(todos[0].getTitle(), Matchers.is("it_ログイン機能を実装する"));
        assertThat(todos[0].getStatus(), Matchers.is("open"));
        assertThat(todos[0].getDescription(), Matchers.is("it_認証されたユーザのみアプリケーションにアクセス可能とするため"));
        assertThat(todos[1].getTitle(), Matchers.is("it_TODO表示機能を実装する"));
        assertThat(todos[1].getStatus(), Matchers.is("doing"));
        assertThat(todos[1].getDescription(), Matchers.is("it_何がTODOとして存在し、どのようなステータスなのかを確認可能とするため"));
    }
}