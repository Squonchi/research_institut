package com.kursovaya.institut.business;

import com.kursovaya.institut.models.*;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

public class TestCaseRunner {

    public static Result testCaseRun(TestCase testCase) {
        RequestSpecification spec = given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON);
        ValidatableResponse valResponse;
        Result result = Result.builder()
                .runDateTime(LocalDateTime.now())
                .testCase(testCase)
                .resultStatus(ResultStatus.DONE)
                .errorMessage("Тест успешно пройден")
                .build();
        try {
            for (TestStep step : testCase.getSteps()) {
                if (step.getBody() == null)
                    step.setBody("");
                switch (step.getHttpMethod()) {
                    case GET:
                        valResponse = spec.get(step.getUrl())
                                .then();
                        break;
                    case POST:
                        valResponse = spec.body(step.getBody())
                                .post(step.getUrl())
                                .then();
                        break;
                    case PUT:
                        valResponse = spec.body(step.getBody())
                                .put(step.getUrl())
                                .then();
                        break;
                    case DELETE:
                        valResponse = spec.body(step.getBody())
                                .delete(step.getUrl())
                                .then();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + step.getHttpMethod());
                }
                try {
                    valResponse
                            .statusCode(step.getExpStatusCode().intValue());
                } catch (AssertionError e) {
                    long actual = valResponse.extract().response().statusCode();
                    long expected = step.getExpStatusCode();
                    result.setErrorMessage(String.format("Код ответа отличается от ожидаемого:\nActual:%d\nExpected:%d", actual, expected));
                    result.setResultStatus(ResultStatus.FAIL);
                    break;
                }
                if (step.getValuePath() != null && !step.getValuePath().equals("")) {
                    try {
                        valResponse.body(step.getValuePath(), Matchers.notNullValue());
                        if (step.getValue() != null && !step.getValue().equals("")) {
                            //Comparable<?> expected;
                            //assertThat("", getMatcher(step.getCondition(), step.getValue(), step.getValueType()).matches(step.));
                            switch (step.getValueType()) {
                                case INT:
                                    BigDecimal numberValue = valResponse.extract().jsonPath().getObject(step.getValuePath(), BigDecimal.class);
                                    assertThat("", getMatcher(step.getCondition(), numberValue, step.getValueType()).matches(new BigDecimal(step.getValue())));
                                    break;
                                case STR:
                                    String stringValue = valResponse.extract().jsonPath().getObject(step.getValuePath(), String.class);
                                    assertThat("", getMatcher(step.getCondition(), stringValue, step.getValueType()).matches(step.getValue()));
                                case BOOL:
                                    Boolean booleanValue = valResponse.extract().jsonPath().getObject(step.getValuePath(), Boolean.class);
                                    assertThat("", getMatcher(step.getCondition(), booleanValue, step.getValueType()).matches(Boolean.valueOf(step.getValue())));
                                    break;
                                default:
                                    throw new Exception();
                            }

                            valResponse
                                    .body(step.getValuePath(), getMatcher(step.getCondition(), step.getValue(), step.getValueType()));
                        }
                    } catch (AssertionError e) {
                        String path = step.getValuePath();
                        String actual = valResponse.extract().body().jsonPath().get(step.getValuePath()).toString();
                        String expected = step.getValue();
                        result.setErrorMessage(String.format("Значение параметра '%s' отличается от ожидаемого:\nActual:%s\nExpected:%s", path, actual, expected));
                        result.setResultStatus(ResultStatus.FAIL);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            result.setErrorMessage("Тест сломан.\nПроверьте правильность ввода данных или обратитесь к администратору.");
            result.setResultStatus(ResultStatus.BROKEN_TEST);
        }
        return result;
    }


    private static <T extends Comparable<T>> Matcher<?> getMatcher(Condition condition, T value, ValueType valueType) throws Exception {
        switch (condition) {
            case GREATER_THAN:
                return greaterThan(value);
            case LESS_THAN:
                return lessThan(value);
            case GREATER_THAN_EQUAL:
                return greaterThanOrEqualTo(value);
            case LESS_THAN_EQUAL:
                return lessThanOrEqualTo(value);
            case NOT_EQUAL:
                return not(equalTo(value));
            case EQUAL:
                return equalTo(value);
            case MATCH:
                return containsString((String) value);
            case MATCH_START:
                return startsWith((String) value);
            case MATCH_END:
                return endsWith((String) value);
            default:
                return null;
        }
    }
}
