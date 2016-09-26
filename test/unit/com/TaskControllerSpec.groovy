package com

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(TaskController)
@Mock(Task)
class TaskControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }


    void "list() should return no results with no records in DB"() {
        given:
        def model = controller.list()
        expect:
        model.taskInstanceList.size() == 0
        model.taskInstanceTotal == 0
    }



    @Unroll
    void "list() should return '#size' item(s) of '#total' total results with query '#search_query'"() {
        given:
        (1..20).each { i -> new Task(description: "Task #${i}").save() }
        params.query = search_query
        and:
        def model = controller.list()
        expect:
        model.taskInstanceList.size() == size
        model.taskInstanceTotal == total
        where:
        search_query    | size  | total
        null            | 5     | 20
        ""              | 5     | 20
        "no task"       | 0     | 0
        "17"            | 1     | 1
        "Task #1"       | 5     | 11
        "Task"          | 5     | 20
    }
}

