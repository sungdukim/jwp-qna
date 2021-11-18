package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        // given
        final User writer = userRepository.save(
            TestUserFactory.create("javajigi", "password", "name", "javajigi@slipp.net")
        );
        final Question question = questionRepository.save(
            TestQuestionFactory.create("title1", "contents1", writer)
        );
        final Answer expected = TestAnswerFactory.create(writer, question, "Answers Contents1");

        // when
        final Answer actual = answerRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getWriter()).isNotNull(),
            () -> assertThat(actual.getQuestion()).isNotNull(),
            () -> assertThat(actual.getContents()).isNotNull()
        );
    }

    @Test
    void equals() {
        // given
        final User writer = userRepository.save(
            TestUserFactory.create("javajigi", "password", "name", "javajigi@slipp.net")
        );
        final Question question = questionRepository.save(
            TestQuestionFactory.create("title1", "contents1", writer)
        );
        final Answer answer1 = TestAnswerFactory.create(1L, writer, question, "Answers Contents1");
        final Answer answer2 = TestAnswerFactory.create(1L, writer, question, "Answers Contents1");

        // then
        assertThat(answer1).isEqualTo(answer2);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        final User writer = userRepository.save(
            TestUserFactory.create("javajigi", "password", "name", "javajigi@slipp.net")
        );
        final Question question = questionRepository.save(
            TestQuestionFactory.create("title1", "contents1", writer)
        );
        final Answer answer = TestAnswerFactory.create(writer, question, "Answers Contents1");

        // when
        answerRepository.save(answer);
        final List<Answer> actual =
            answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        final User writer = userRepository.save(
            TestUserFactory.create("javajigi", "password", "name", "javajigi@slipp.net")
        );
        final Question question = questionRepository.save(
            TestQuestionFactory.create("title1", "contents1", writer)
        );
        final Answer answer = TestAnswerFactory.create(writer, question, "Answers Contents1");

        // when
        answerRepository.save(answer);
        final Answer actual = answerRepository.findByIdAndDeletedFalse(answer.getId())
            .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(actual.getId()).isEqualTo(answer.getId());
    }
}
